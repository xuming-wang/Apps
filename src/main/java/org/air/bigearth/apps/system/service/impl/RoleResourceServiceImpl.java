package org.air.bigearth.apps.system.service.impl;

import org.air.bigearth.apps.system.domain.basic.RoleResource;
import org.air.bigearth.apps.system.mapper.extend.RoleResourceExtendMapper;
import org.air.bigearth.apps.system.service.IRoleResourceService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色资源 业务层处理
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
@Service
@Transactional
public class RoleResourceServiceImpl implements IRoleResourceService {
	
	@Autowired
	RoleResourceExtendMapper roleResourceExtendMapper;

	/**
	 * 根据roleid 或resourceid 删除角色和资源关系
	 */
	@Override
	public void deleteRoleResource(String roleId,String resourceId){
		roleResourceExtendMapper.deleteRoleResource(roleId,null);
	}
	
	/**
	 * 批量新增角色资源关系
	 */
	@Override
	public void batchInsert (List<RoleResource>list){
		roleResourceExtendMapper.batchInsert(list);
	}
	
	/**
	 * 根据role ID集合，查这些角色拥有的资源id
	 */
	@Override
	public List<String>getResource(List<String>roleIds){
		if(roleIds!=null&&CollectionUtils.isNotEmpty(roleIds)){
			List<String> resourceIds = roleResourceExtendMapper.getResourceByRole(roleIds);
			return resourceIds;
		}
		return null;
	}
}
