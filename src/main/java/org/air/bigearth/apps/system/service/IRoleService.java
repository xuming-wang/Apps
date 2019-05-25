package org.air.bigearth.apps.system.service;

import org.air.bigearth.apps.system.domain.basic.Role;
import org.air.bigearth.apps.system.domain.vm.RoleResourceVM;

import java.util.List;

/**
 * 角色 业务层
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
public interface IRoleService {

     void saveRole(Role role);

     void deleteRole(String roleId);

     void updateRole(Role role);

     List<Role> getRoleList();

     void authorize(RoleResourceVM roleResourceVM);

     Boolean isExist(String code);

}
