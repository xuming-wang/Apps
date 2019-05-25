package org.air.bigearth.apps.image.service.impl;

import org.air.bigearth.apps.exception.service.BaseException;
import org.air.bigearth.apps.image.domain.DataAccess;
import org.air.bigearth.apps.image.mapper.DataAccessMapper;
import org.air.bigearth.apps.image.service.IDataAccessService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 数据访问 业务层处理
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-23
 */
@Service
@Transactional
public class DataAccessServiceImpl implements IDataAccessService {
    @Resource
    private DataAccessMapper dataAccessMapper;

    @Override
    public boolean insertDto(DataAccess dto) throws BaseException {

        return false;
    }

    @Override
    public boolean updateDto(DataAccess dto) throws BaseException {
        return false;
    }

    @Override
    public boolean deleteByIds(String[] ids) throws BaseException {
        return false;
    }

    @Override
    public DataAccess getById(String id) throws BaseException {
        return null;
    }

    @Override
    public List<DataAccess> searchByParamsPage(Map<String, Object> params) throws BaseException {
        return dataAccessMapper.searchByParamsPage(params);
    }

    @Override
    public DataAccess selectDataAccessByDataSet(Map<String, Object> param) {
        return null;
    }
}
