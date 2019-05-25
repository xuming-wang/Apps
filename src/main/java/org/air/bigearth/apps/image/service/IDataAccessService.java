package org.air.bigearth.apps.image.service;

import org.air.bigearth.apps.base.IPlatformService;
import org.air.bigearth.apps.image.domain.DataAccess;

import java.util.Map;

/**
 * 数据访问 业务层
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-23
 */
public interface IDataAccessService extends IPlatformService<DataAccess> {

    DataAccess selectDataAccessByDataSet(Map<String, Object> param);

}
