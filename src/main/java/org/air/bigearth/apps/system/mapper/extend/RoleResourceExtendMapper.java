package org.air.bigearth.apps.system.mapper.extend;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import org.air.bigearth.apps.system.domain.basic.RoleResource;

/**
 * 角色资源扩展 数据访问层
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
@Mapper
public interface RoleResourceExtendMapper {

	/**
	 * 根据roleid 或resourceid 删除角色资源关系
	 */
	public void deleteRoleResource(
			@Param(value="roleId")String roleId,
			@Param(value="resourceId")String resourceId);
	
	/**
	 * 批量保存 角色资源关系
	 */
	public void batchInsert(@Param(value="list")List<RoleResource> list);
	
	/**
	 * 通过roleid得到拥有的resourceid
	 */
	public List<String> getResourceByRole(@Param(value="roleIds")List<String>roleIds);
}
