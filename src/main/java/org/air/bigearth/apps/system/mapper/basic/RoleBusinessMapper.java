package org.air.bigearth.apps.system.mapper.basic;

import org.air.bigearth.apps.system.domain.basic.RoleBusiness;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

/**
 * 角色业务 数据访问层
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
@Mapper
public interface RoleBusinessMapper {
    int deleteByPrimaryKey(String id);

    int insert(RoleBusiness record);

    RoleBusiness selectByPrimaryKey(String id);

    List<RoleBusiness> selectAll();

    int updateByPrimaryKey(RoleBusiness record);
}