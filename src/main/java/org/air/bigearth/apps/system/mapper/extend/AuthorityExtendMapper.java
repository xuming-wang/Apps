package org.air.bigearth.apps.system.mapper.extend;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import org.air.bigearth.apps.system.domain.basic.Authority;
import org.air.bigearth.apps.system.domain.extend.AuthorityController;

/**
 * 权限扩展 数据访问层
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
@Mapper
public interface AuthorityExtendMapper {

	/** 
	 * 根据 权限id 查权限对象集
	 */
	public List<Authority>getAuthorityByIds(@Param(value="ids")List<String>ids);
	
	/**
	 * 按controller分组查 操作码列表
	 */
	public List<AuthorityController>getAuthority();
	
	/**
	 * 根据请求方式，关键字模糊查询
	 */
	public List<Authority>getLikeAuthority(@Param(value="method")String method,@Param(value="word")String word);
}
