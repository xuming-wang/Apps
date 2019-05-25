package org.air.bigearth.apps.image.mapper;

import org.air.bigearth.apps.system.domain.basic.Role;
import org.air.bigearth.apps.image.domain.Composite;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 图像合成 数据访问层
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-23
 */
@Mapper
public interface CompositeMapper {
    int deleteByPrimaryKey(String datakey);

    int insert(Composite record);

    Composite selectByPrimaryKey(String datakey);

    List<Composite> selectAll();

    int updateByPrimaryKey(Composite record);

    List<Composite> searchByParamsPage(Map<String, Object> params);
}
