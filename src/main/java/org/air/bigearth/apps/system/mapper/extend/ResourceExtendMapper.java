package org.air.bigearth.apps.system.mapper.extend;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import org.air.bigearth.apps.system.domain.basic.Resource;

/**
 * 资源扩展 数据访问层
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
@Mapper
public interface ResourceExtendMapper {
    
	/**
	 * 根据资源id集，查资源
	 */
	public List<Resource> selectByResourceIds(@Param(value="resourceIds")List<String>resourceIds);
	
	/**
	 * 根据 上级资源的code 模糊path 删除  本身及下级资源
	 */
	public void deleteByPath(@Param(value="code")String code);
	
	/**
	 * 根据资源code 查询本身 及 下级所有资源
	 */
	public List<Resource> selectByCode(@Param(value="code")String code);
	
	/**
	 * 根据资源code 查资源
	 */
	public Resource getIdByCode(@Param(value="code")String code);
}