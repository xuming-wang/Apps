package org.air.bigearth.apps.system.mapper.extend;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import org.air.bigearth.apps.system.domain.basic.User;

/**
 * 用户扩展 数据访问层
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
@Mapper
public interface UserExtendMapper {

	User selectByName(@Param(value="account")String account);
	
	/**
	 * 校验用户名唯一,账号
	 */
	Integer isExist(@Param(value="loginName")String loginName);

	Integer phoneUnique(@Param(value="phone")String phone);

	Integer emailUnique(@Param(value="email")String email);
}
