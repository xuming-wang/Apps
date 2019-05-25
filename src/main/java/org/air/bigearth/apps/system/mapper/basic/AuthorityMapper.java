package org.air.bigearth.apps.system.mapper.basic;

import org.air.bigearth.apps.system.domain.basic.Authority;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

/**
 * 权限 数据访问层
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
@Mapper
public interface AuthorityMapper {
    int deleteByPrimaryKey(String id);

    int insert(Authority record);

    Authority selectByPrimaryKey(String id);

    List<Authority> selectAll();

    int updateByPrimaryKey(Authority record);
}