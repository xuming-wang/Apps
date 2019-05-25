package org.air.bigearth.apps.system.domain.basic;

/**
 * 角色业务对象
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-05-06
 */
public class RoleBusiness {
    private String id;

    private String roleId;

    private String classPk;

    private String className;

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

    public String getClassPk() {
        return classPk;
    }

    public void setClassPk(String classPk) {
        this.classPk = classPk;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}