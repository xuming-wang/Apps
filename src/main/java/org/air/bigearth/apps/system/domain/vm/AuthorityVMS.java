package org.air.bigearth.apps.system.domain.vm;

import java.util.List;

import org.air.bigearth.apps.system.domain.basic.Authority;

/**
 * 权限验证对象
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-05-06
 */
public class AuthorityVMS {

	private String controller;
	
	List<Authority> authorityList;

	public String getController() {
		return controller;
	}

	public void setController(String controller) {
		this.controller = controller;
	}

	public List<Authority> getAuthorityList() {
		return authorityList;
	}

	public void setAuthorityList(List<Authority> authorityList) {
		this.authorityList = authorityList;
	}
	
}
