package org.air.bigearth.apps.image.service.impl;

import org.air.bigearth.apps.exception.service.BaseException;
import org.air.bigearth.apps.image.domain.Composite;
import org.air.bigearth.apps.image.mapper.CompositeMapper;
import org.air.bigearth.apps.image.service.ICompositeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 图像合成 业务层处理
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-23
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CompositeServiceImpl implements ICompositeService {
    @Resource
    private CompositeMapper compositeMapper;

    @Override
    public boolean insertDto(Composite dto) throws BaseException {
        try {
            compositeMapper.insert(dto);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateDto(Composite dto) throws BaseException {
        return false;
    }
    @Override
    public boolean deleteByIds(String[] ids) throws BaseException {
        return false;
    }
    @Override
    public Composite getById(String id) throws BaseException {
        return null;
    }
    @Override
    public List<Composite> searchByParamsPage(Map<String, Object> params) throws BaseException {
        List<Composite> listComposites = null;
        try {
            listComposites = compositeMapper.searchByParamsPage(params);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return listComposites;
    }
}
