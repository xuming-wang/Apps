package org.air.bigearth.apps.system.service;

import java.util.List;
import java.util.UUID;

import org.air.bigearth.apps.system.mapper.extend.AuthorityExtendMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.air.bigearth.apps.system.domain.basic.Authority;
import org.air.bigearth.apps.system.domain.extend.AuthorityController;
import org.air.bigearth.apps.system.domain.vm.AuthoritySaveVM;
import org.air.bigearth.apps.system.domain.vm.AuthorityUpdateVM;
import org.air.bigearth.apps.system.domain.vm.AuthorityVMS;
import org.air.bigearth.apps.system.mapper.basic.AuthorityMapper;
import org.air.bigearth.apps.util.Asserts;
import org.air.bigearth.apps.util.EntityUtils;

/**
 * 权限 业务层
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
public interface IAuthorityService {


    /**
     * 根据 权限id 查权限对象集
     */
    public List<Authority> getAuthorityByIds(List<String> ids);


    /**
     * 按controller分组，得到authority列表
     *
     * @return
     */
    public List<AuthorityVMS> getAuthority();


    public List<Authority> getLikeAuthority(String method, String word);


    public void saveAuthority(AuthoritySaveVM authoritySaveVM);


    public void updateAuhtority(AuthorityUpdateVM authorityUpdateVM);


    public void deleteAuthority(String id);


}
