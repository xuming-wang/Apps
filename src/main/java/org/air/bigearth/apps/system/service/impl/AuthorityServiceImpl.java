package org.air.bigearth.apps.system.service.impl;

import org.air.bigearth.apps.system.domain.basic.Authority;
import org.air.bigearth.apps.system.domain.extend.AuthorityController;
import org.air.bigearth.apps.system.domain.vm.AuthoritySaveVM;
import org.air.bigearth.apps.system.domain.vm.AuthorityUpdateVM;
import org.air.bigearth.apps.system.domain.vm.AuthorityVMS;
import org.air.bigearth.apps.system.mapper.basic.AuthorityMapper;
import org.air.bigearth.apps.system.mapper.extend.AuthorityExtendMapper;
import org.air.bigearth.apps.system.service.IAuthorityService;
import org.air.bigearth.apps.system.service.IResourceAuthorityService;
import org.air.bigearth.apps.util.Asserts;
import org.air.bigearth.apps.util.EntityUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * 权限 业务层处理
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
@Service
@Transactional
public class AuthorityServiceImpl implements IAuthorityService {

    @Autowired
    AuthorityExtendMapper authorityExtendMapper;

    @Autowired
    AuthorityMapper authorityMapper;

    @Autowired
    IResourceAuthorityService resourceAuthorityService;


    /**
     * 根据 权限id 查权限对象集
     */
    @Override
    public List<Authority> getAuthorityByIds(List<String> ids){
        if(CollectionUtils.isEmpty(ids)){return null;}
        return authorityExtendMapper.getAuthorityByIds(ids);
    }


    /**
     * 按controller分组，得到authority列表
     * @return
     */
    @Override
    public List<AuthorityVMS> getAuthority() {
        List<AuthorityController> authorityList = authorityExtendMapper.getAuthority();
        return EntityUtils.entity2VMList(authorityList, AuthorityVMS.class);
    }

    @Override
    public List<Authority> getLikeAuthority(String method, String word) {
        Asserts.notEmpty(method);
        return authorityExtendMapper.getLikeAuthority(method, word);
    }

    @Override
    public void saveAuthority(AuthoritySaveVM authoritySaveVM) {
        Asserts.validate(authoritySaveVM,"authoritySaveVM");
        Authority authority = EntityUtils.entity2VM(authoritySaveVM, Authority.class);
        authority.setId(UUID.randomUUID().toString());
        authorityMapper.insert(authority);
    }

    @Override
    public void updateAuhtority(AuthorityUpdateVM authorityUpdateVM) {
        Asserts.validate(authorityUpdateVM, "authorityUpdateVM");
        //校验id是否存在

        Authority authority = EntityUtils.vm2Entity(authorityUpdateVM, Authority.class);
        authorityMapper.updateByPrimaryKey(authority);
    }

    @Override
    public void deleteAuthority(String id) {
        Asserts.notEmpty(id);

        //删除资源权限关系
        resourceAuthorityService.deleteResourceAuthority(null,id);

        //删除权限
        authorityMapper.deleteByPrimaryKey(id);
    }
}
