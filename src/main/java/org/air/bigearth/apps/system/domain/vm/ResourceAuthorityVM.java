package org.air.bigearth.apps.system.domain.vm;

import java.util.List;

import org.air.bigearth.apps.system.domain.basic.ResourceAuthority;

/**
 * 资源权限对象
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-05-06
 */
public class ResourceAuthorityVM {

	List<ResourceAuthority>resourceAuthorityList;

	public List<ResourceAuthority> getResourceAuthorityList() {
		return resourceAuthorityList;
	}

	public void setResourceAuthorityList(
			List<ResourceAuthority> resourceAuthorityList) {
		this.resourceAuthorityList = resourceAuthorityList;
	}
	
	
}
