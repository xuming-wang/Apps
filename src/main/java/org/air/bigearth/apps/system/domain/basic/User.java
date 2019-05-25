package org.air.bigearth.apps.system.domain.basic;

import java.io.Serializable;

/**
 * 用户表 apps_t_sys_user
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-17
 */
public class User implements Serializable {
    /**
     * 用户编号
     */
    private String id;

    private String loginName;

    private String realName;

    private String password;
    /**
     * 是否超级管理员
     */
    private Boolean superman;

    private String phone;

    private String email;

    // ====================================================
    private String confirmPassword;

    public Boolean getSuperman() {
		return superman;
	}

	public void setSuperman(Boolean superman) {
		this.superman = superman;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}