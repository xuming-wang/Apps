package org.air.bigearth.apps.system.service.impl;

import org.air.bigearth.apps.system.domain.basic.Authority;
import org.air.bigearth.apps.system.domain.basic.ResourceAuthority;
import org.air.bigearth.apps.system.mapper.basic.ResourceAuthorityMapper;
import org.air.bigearth.apps.system.mapper.extend.ResourceAuthorityExtendMapper;
import org.air.bigearth.apps.system.service.IAuthorityService;
import org.air.bigearth.apps.system.service.IResourceAuthorityService;
import org.air.bigearth.apps.util.Asserts;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 资源权限 业务层处理
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
@Service
@Transactional
public class ResourceAuthorityServiceImpl implements IResourceAuthorityService {

	@Autowired
	ResourceAuthorityExtendMapper resourceAuthorityExtendMapper;

	@Autowired
    IAuthorityService authorityService;
	
	@Autowired
	ResourceAuthorityMapper resourceAuthorityMapper;

	/**
	 * 根据参数资源 查出其拥有的 权限
	 */
	@Override
	public List<String> getAuthorityByResource(List<String> resourceList){
		if(CollectionUtils.isEmpty(resourceList)){return null;}
		return resourceAuthorityExtendMapper.getAuthorityByResource(resourceList);
	}
	
	/**
	 * 查单个资源拥有的权限
	 * @param resourceId
	 * @return
	 */
	@Override
	public List<Authority> getAuthorityByResource(String resourceId) {
		List<String>list=new ArrayList<>();
		list.add(resourceId);
		List<String> authorityIds = resourceAuthorityExtendMapper.getAuthorityByResource(list);
		
		return authorityService.getAuthorityByIds(authorityIds);
	}

	/**
	 * 取消授权
	 * @param resourceId
	 * @param authorityId
	 */
	@Override
	public void deleteResourceAuthority(String resourceId, String authorityId) {
		resourceAuthorityExtendMapper.deleteResourceAuthority(resourceId,authorityId);
	}

	/**
	 * 给资源授权
	 * @param resourceId
	 * @param authorityId
	 */
	@Override
	public void authorize(String resourceId, String authorityId) {
		Asserts.notEmpty(resourceId);
		Asserts.notEmpty(authorityId);
		
		ResourceAuthority resourceAuthority=new ResourceAuthority();
		resourceAuthority.setId(UUID.randomUUID().toString());
		resourceAuthority.setAuthorityId(authorityId);
		resourceAuthority.setResourceId(resourceId);
		
		resourceAuthorityMapper.insert(resourceAuthority);
	}
	
}
