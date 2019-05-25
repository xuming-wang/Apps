package org.air.bigearth.apps.image.mapper;

import org.air.bigearth.apps.base.PlatformMapper;
import org.air.bigearth.apps.image.domain.DataAccess;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
/**
 * 数据访问层
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-23
 */
@Mapper
public interface DataAccessMapper extends PlatformMapper {

    DataAccess selectDataAccessByDataSet(Map<String, Object> param);

    List<DataAccess> searchByParamsPage(Map<String, Object> params);
}
