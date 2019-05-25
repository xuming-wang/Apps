package org.air.bigearth.apps.system.mapper.basic;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import org.air.bigearth.apps.system.domain.basic.Role;

/**
 * 角色 数据访问层
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
@Mapper
public interface RoleMapper {
    int deleteByPrimaryKey(String id);

    int insert(Role record);

    Role selectByPrimaryKey(String id);

    List<Role> selectAll();

    int updateByPrimaryKey(Role record);
}