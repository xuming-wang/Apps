package org.air.bigearth.apps.system.mapper.basic;

import org.air.bigearth.apps.system.domain.basic.ResourceAuthority;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

/**
 * 资源权限 数据访问层
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
@Mapper
public interface ResourceAuthorityMapper {
    int deleteByPrimaryKey(String id);

    int insert(ResourceAuthority record);

    ResourceAuthority selectByPrimaryKey(String id);

    List<ResourceAuthority> selectAll();

    int updateByPrimaryKey(ResourceAuthority record);
}