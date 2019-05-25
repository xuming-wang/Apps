package org.air.bigearth.apps.system.mapper.basic;

import org.air.bigearth.apps.system.domain.basic.RoleResource;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

/**
 * 角色资源 数据访问层
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
@Mapper
public interface RoleResourceMapper {
    int deleteByPrimaryKey(String id);

    int insert(RoleResource record);

    RoleResource selectByPrimaryKey(String id);

    List<RoleResource> selectAll();

    int updateByPrimaryKey(RoleResource record);
}