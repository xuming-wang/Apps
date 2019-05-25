package org.air.bigearth.apps.system.mapper.basic;

import org.air.bigearth.apps.system.domain.basic.Resource;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

/**
 * 资源 数据访问层
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
@Mapper
public interface ResourceMapper {
    int deleteByPrimaryKey(String id);

    int insert(Resource record);

    Resource selectByPrimaryKey(String id);

    List<Resource> selectAll();

    int updateByPrimaryKey(Resource record);
}