package org.air.bigearth.apps.system.service.impl;

import org.air.bigearth.apps.system.domain.basic.UserRole;
import org.air.bigearth.apps.system.domain.vm.UserRoleSaveVMS;
import org.air.bigearth.apps.system.mapper.basic.UserRoleRMapper;
import org.air.bigearth.apps.system.mapper.extend.UserRoleExtendMapper;
import org.air.bigearth.apps.system.service.IUserRoleService;
import org.air.bigearth.apps.util.Asserts;
import org.air.bigearth.apps.exception.service.DataValidateFiledException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 用户角色 业务层处理
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
@Service
@Transactional
public class UserRoleServiceImpl implements IUserRoleService {

	@Autowired
	UserRoleExtendMapper userRoleRExtendMapper;
	
	@Autowired
	UserRoleRMapper userRoleRMapper;
	
	/**
	 * 给用户授权
	 * 单个用户
	 * @param userRoleVMS
	 */
	@Override
	public void delegate(UserRoleSaveVMS userRoleVMS) {
		Asserts.validate(userRoleVMS, "userRole");
		
		String userId=userRoleVMS.getUserId();
		List<String> roleList = Arrays.asList(userRoleVMS.getRoleIds().split(","));
		List<UserRole> userRoleList=new ArrayList<UserRole>();
		List<String> userIdList=new ArrayList<String>();
		for(String roleId:roleList){
			UserRole userRole=new UserRole();
			userRole.setId(UUID.randomUUID().toString());
			userRole.setUserId(userId);
			userRole.setRoleId(roleId);
			userRoleList.add(userRole);
			userIdList.add(userRole.getUserId());
		}
		
		//先删再增
		userRoleRExtendMapper.deleteByUserId(userIdList);
		userRoleRExtendMapper.batchInsert(userRoleList);
	}
	
	/**
	 * 根据用户id 删除用户角色关系
	 */
	@Override
	public void deleteByUserId(String userId){
		if(StringUtils.isBlank(userId)){throw new DataValidateFiledException("用户Id不能为空");}
		
		List <String>userIdList=new ArrayList<String>();
		userIdList.add(userId);
		userRoleRExtendMapper.deleteByUserId(userIdList);
	}
	
	/**
	 * 根据角色id删除用户角色关系
	 */
	@Override
	public void deleteByRoleId(String roleId){
		if(StringUtils.isBlank(roleId)){throw new DataValidateFiledException("角色Id不能为空");}
		
		userRoleRExtendMapper.deleteByRoleId(roleId);
	}
	
	/**
	 * 根据‘用户id查所拥有的角色e
	 */
	@Override
	public List<String> getRoleByUser(String userId){
		Asserts.notEmpty(userId);
		List<String> roleIds = userRoleRExtendMapper.getRoleByUser(userId);
		return roleIds;
	}
}
