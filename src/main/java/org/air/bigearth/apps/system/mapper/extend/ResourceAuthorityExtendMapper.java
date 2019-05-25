package org.air.bigearth.apps.system.mapper.extend;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 资源权限扩展 数据访问层
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
@Mapper
public interface ResourceAuthorityExtendMapper {

	/**
	 * 根据参数资源 查出其拥有的 权限ids
	 */
	List<String> getAuthorityByResource(@Param(value="resourceList")List<String> resourceList);

	/**
	 * 取消授权
	 * @param resourceId
	 * @param authorityId
	 */
	void deleteResourceAuthority(
			@Param(value="resourceId")String resourceId,
			@Param(value="authorityId")String authorityId);
	
}
