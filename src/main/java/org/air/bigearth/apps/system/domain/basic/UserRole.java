package org.air.bigearth.apps.system.domain.basic;

/**
 * 用户角色关联表 apps_t_sys_user_role
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
public class UserRole {
	
	private String id;
	
    private String userId;

    private String roleId;

    private String ywId;
    
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getYwId() {
        return ywId;
    }

    public void setYwId(String ywId) {
        this.ywId = ywId;
    }
}