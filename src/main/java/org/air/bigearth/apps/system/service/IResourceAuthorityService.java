package org.air.bigearth.apps.system.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.air.bigearth.apps.system.domain.basic.Authority;
import org.air.bigearth.apps.system.domain.basic.ResourceAuthority;
import org.air.bigearth.apps.system.mapper.basic.ResourceAuthorityMapper;
import org.air.bigearth.apps.system.mapper.extend.ResourceAuthorityExtendMapper;
import org.air.bigearth.apps.util.Asserts;

/**
 * 资源权限 业务层
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
public interface IResourceAuthorityService {


    /**
     * 根据参数资源 查出其拥有的 权限
     */
    public List<String> getAuthorityByResource(List<String> resourceList);

    /**
     * 查单个资源拥有的权限
     *
     * @param resourceId
     * @return
     */
    public List<Authority> getAuthorityByResource(String resourceId);

    /**
     * 取消授权
     *
     * @param resourceId
     * @param authorityId
     */
    public void deleteResourceAuthority(String resourceId, String authorityId);

    /**
     * 给资源授权
     *
     * @param resourceId
     * @param authorityId
     */
    public void authorize(String resourceId, String authorityId);

}
