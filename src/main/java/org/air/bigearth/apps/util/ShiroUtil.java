package org.air.bigearth.apps.util;

import org.air.bigearth.apps.system.domain.basic.User;
import org.apache.shiro.SecurityUtils;

/**
 * Shiro工具类
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-23
 */
public class ShiroUtil {

    /**
     * 获取用户
     *
     * @return
     */
    public static User getSysUser() {
        return (User) SecurityUtils.getSubject().getPrincipal();
    }

}
