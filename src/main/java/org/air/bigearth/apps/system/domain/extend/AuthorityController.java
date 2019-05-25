package org.air.bigearth.apps.system.domain.extend;

import java.util.List;

import org.air.bigearth.apps.system.domain.basic.Authority;

/**
 * 权限分组对象
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
public class AuthorityController {

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
