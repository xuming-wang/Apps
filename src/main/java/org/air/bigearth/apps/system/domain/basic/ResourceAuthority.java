package org.air.bigearth.apps.system.domain.basic;

/**
 * 资源权限关联表 apps_t_sys_resouce_authority
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
public class ResourceAuthority {
    private String id;

    private String resourceId;

    private String authorityId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(String authorityId) {
        this.authorityId = authorityId;
    }
}