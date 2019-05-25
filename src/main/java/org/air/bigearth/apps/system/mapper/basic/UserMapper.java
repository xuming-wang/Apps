package org.air.bigearth.apps.system.mapper.basic;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import org.air.bigearth.apps.system.domain.basic.User;

/**
 * 用户 数据访问层
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(String id);

    int insert(User record);

    User selectByPrimaryKey(String id);

    List<User> selectAll();

    int updateByPrimaryKey(User record);
}