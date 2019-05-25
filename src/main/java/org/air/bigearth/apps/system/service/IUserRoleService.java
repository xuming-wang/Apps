package org.air.bigearth.apps.system.service;

import java.util.List;

import org.air.bigearth.apps.system.domain.vm.UserRoleSaveVMS;

/**
 * 用户角色 业务层
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
public interface IUserRoleService {

    /**
     * 给用户授权
     * 单个用户
     *
     * @param userRoleVMS
     */
    public void delegate(UserRoleSaveVMS userRoleVMS);

    /**
     * 根据用户id 删除用户角色关系
     */
    public void deleteByUserId(String userId);

    /**
     * 根据角色id删除用户角色关系
     */
    public void deleteByRoleId(String roleId);

    /**
     * 根据‘用户id查所拥有的角色e
     */
    public List<String> getRoleByUser(String userId);
}
