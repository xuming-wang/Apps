package org.air.bigearth.apps.system.service.impl;

import org.air.bigearth.apps.system.domain.basic.Role;
import org.air.bigearth.apps.system.domain.basic.RoleResource;
import org.air.bigearth.apps.system.domain.vm.RoleResourceVM;
import org.air.bigearth.apps.system.mapper.basic.RoleMapper;
import org.air.bigearth.apps.system.mapper.extend.RoleExtendMapper;
import org.air.bigearth.apps.system.service.IRoleResourceService;
import org.air.bigearth.apps.system.service.IRoleService;
import org.air.bigearth.apps.system.service.IUserRoleService;
import org.air.bigearth.apps.util.Asserts;
import org.air.bigearth.apps.util.DataKeyUtil;
import org.air.bigearth.apps.exception.service.DataValidateFiledException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 角色 业务层处理
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
@Service
@Transactional
public class RoleServiceImpl implements IRoleService {

	@Autowired
	RoleMapper roleMapper;

	@Autowired
	IUserRoleService userRoleService;

	@Autowired
    IRoleResourceService roleResourceService;
	
	@Autowired
	RoleExtendMapper roleExtendMapper;

	@Override
	public void saveRole(Role role) {
		role.setId(DataKeyUtil.getDataKey());
		roleMapper.insert(role);
	}

	@Override
	public void deleteRole(String roleId) {
		if(StringUtils.isBlank(roleId)){
			throw new DataValidateFiledException("角色id不能为空");
		}
		//校验roleId  是否存在
		//删角色和用户的关系
		userRoleService.deleteByRoleId(roleId);
		//删角色和资源的关系
		roleResourceService.deleteRoleResource(roleId,null);
		//删角色
		roleMapper.deleteByPrimaryKey(roleId);
	}

	@Override
	public void updateRole(Role role) {
		roleMapper.updateByPrimaryKey(role);
	}

	@Override
	public List<Role> getRoleList() {
		List<Role> roleList = roleMapper.selectAll();
		Collections.sort(roleList);
		return roleList;
	}

	@Override
	public void authorize(RoleResourceVM roleResourceVM) {
		Asserts.validate(roleResourceVM, "roleResource");
		if(StringUtils.isBlank(roleResourceVM.getRoleIds())||StringUtils.isBlank(roleResourceVM.getResourceIds())){
			throw new DataValidateFiledException("角色或资源不能为空");
		}
		String roleIds=roleResourceVM.getRoleIds();
		String resourceIds=roleResourceVM.getResourceIds();
		
		List<String> resourceList = Arrays.asList(resourceIds.split(","));
		List<String>roleList =Arrays.asList(roleIds.split(","));
		
		List<RoleResource>roleResourceList=new ArrayList<RoleResource>();
		
		for(String roleId:roleList){
			
			for(String resourceId:resourceList){
				RoleResource roleResource=new RoleResource();
				roleResource.setId(UUID.randomUUID().toString());
				roleResource.setRoleId(roleId);
				roleResource.setResourceId(resourceId);
				roleResourceList.add(roleResource);
			}
			//删角色和资源的关系
			roleResourceService.deleteRoleResource(roleId,null);
		}
		
		roleResourceService.batchInsert(roleResourceList);
	}

	@Override
	public Boolean isExist(String code) {
		return roleExtendMapper.isExist(code);
	}

}
