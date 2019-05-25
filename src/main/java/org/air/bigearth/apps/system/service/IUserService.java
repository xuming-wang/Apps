package org.air.bigearth.apps.system.service;

import org.air.bigearth.apps.system.domain.basic.User;

import java.util.List;

/**
 * 用户 业务层
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
public interface IUserService {


    /**
     * 根据用户名查用户对象
     *
     * @param account
     * @return
     */
     User selectByAccount(String account);

     void saveUser(User user);

     void updateUser(User user);

     void deleteUser(String id);

     List<User> findAllUser();

     Boolean isExist(String loginName);

     Boolean phoneIsExist(String phome);

     Boolean emailIsExist(String email);


}
