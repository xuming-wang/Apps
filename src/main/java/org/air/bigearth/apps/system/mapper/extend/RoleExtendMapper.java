package org.air.bigearth.apps.system.mapper.extend;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 角色扩展 数据访问层
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
@Mapper
public interface RoleExtendMapper {

	/**
	 * 校验角色code唯一性
	 */
	Boolean isExist(@Param(value="code")String code);
}
