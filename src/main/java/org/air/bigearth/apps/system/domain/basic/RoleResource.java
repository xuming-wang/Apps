package org.air.bigearth.apps.system.domain.basic;

/**
 * 角色资源关联表 apps_t_sys_role_resource
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
public class RoleResource {
    private String id;

    private String roleId;

    private String resourceId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
}