package org.air.bigearth.apps.system.domain.vm;

import io.swagger.annotations.ApiModel;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 用户角色验证对象
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-05-06
 */
@ApiModel("批量新增用户角色关系VM")
public class UserRoleSaveVMS {

	@NotBlank
	private String userId;
	@NotBlank
	private String roleIds;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
	
	
	
}
