package org.air.bigearth.apps.system.service;

import java.util.List;

import org.air.bigearth.apps.system.domain.basic.RoleResource;

/**
 * 角色资源 业务层
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
public interface IRoleResourceService {
	

	/**
	 * 根据roleid 或resourceid 删除角色和资源关系
	 */
	public void deleteRoleResource(String roleId,String resourceId);
	
	/**
	 * 批量新增角色资源关系
	 */
	public void batchInsert (List<RoleResource>list);
	
	/**
	 * 根据role ID集合，查这些角色拥有的资源id
	 */
	public List<String>getResource(List<String>roleIds);
}
