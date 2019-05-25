package org.air.bigearth.apps.system.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.air.bigearth.apps.base.BaseResult;
import org.air.bigearth.apps.constant.ResultConstants;
import org.air.bigearth.apps.system.domain.basic.User;
import org.air.bigearth.apps.system.domain.vm.UserRoleSaveVMS;
import org.air.bigearth.apps.system.service.IUserRoleService;
import org.air.bigearth.apps.system.service.IUserService;
import org.air.bigearth.apps.util.EdsUtil;
import org.air.bigearth.apps.util.ResultUtil;
import org.air.bigearth.apps.util.StringUtil;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户 控制层
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
@Api(description = "对用户操作")
@RestController
@RequestMapping(value = "/system")
public class UserController {

    @Autowired
    IUserService userService;

    @Autowired
    IUserRoleService userRoleService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token", dataType = "string", paramType = "header")
    })
    @ApiOperation(value = "新增用户", notes = "新增用户，需要 user_saveUser权限或管理员权限")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success")})
    @RequiresPermissions(value = {"user_saveUser", "administrator"}, logical = Logical.OR)
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public BaseResult<String> saveUser(User user) {
        if (user != null) {
            if (StringUtil.isNotNullOrBlank(user.getLoginName())) {
                if (userService.isExist(user.getLoginName())) {
                    return ResultUtil.error(ResultConstants.ACCOUNT_EXIST);
                }
            }
            if (StringUtil.isNotNullOrBlank(user.getPassword())) {
                user.setPassword(EdsUtil.encryptBasedDes(user.getPassword()));
            }
        }
        userService.saveUser(user);

        return ResultUtil.success();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token", dataType = "string", paramType = "header")
    })
    @ApiOperation(value = "修改用户", notes = "修改用户，需要user_updateUser 权限或管理员权限")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success")})
    @RequiresPermissions(value = {"user_updateUser", "administrator"}, logical = Logical.OR)
    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    public BaseResult<String> updateUser(User user) {
        if (user != null) {
            if (StringUtil.isNotNullOrBlank(user.getPassword())) {
                user.setPassword(EdsUtil.encryptBasedDes(user.getPassword()));
            }
        }
        userService.updateUser(user);
        return ResultUtil.success();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token", dataType = "string", paramType = "header")
    })
    @ApiOperation(value = "删除用户", notes = "删除用户，需要 user_deleteUser权限或管理员权限")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success")})
    @RequiresPermissions(value = {"user_deleteUser", "administrator"}, logical = Logical.OR)
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public BaseResult<String> deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(id);
        return ResultUtil.success();

    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token", dataType = "string", paramType = "header")
    })
    @ApiOperation(value = "将角色授予用户", notes = "将角色授予用户，需要 user_delegate权限或管理员权限")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success")})
    @RequiresPermissions(value = {"user_delegate", "administrator"}, logical = Logical.OR)
    @RequestMapping(value = "/user/delegate", method = RequestMethod.POST)
    public BaseResult<String> delegate(UserRoleSaveVMS userRoleVMS) {
        userRoleService.delegate(userRoleVMS);
        return ResultUtil.success();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token", dataType = "string", paramType = "header")
    })
    @ApiOperation(value = "查所有用户", notes = "查所有用户，需要 user_findAllUser权限或管理员权限")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success")})
    @RequiresPermissions(value = {"user_findAllUser", "administrator"}, logical = Logical.OR)
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public BaseResult<List<User>> findAllUser() {
        List<User> userList = userService.findAllUser();
        return ResultUtil.success(userList);
    }

    /**
     * 分页查询用户
     *
     * @return
     */
    @RequestMapping(value = "/user/page", method = RequestMethod.GET)
    public BaseResult<PageInfo<User>> findAllUserWithPage() {
        PageHelper.startPage(1, 2);
        List<User> userList = userService.findAllUser();
        PageInfo<User> pageInfo = new PageInfo<User>(userList);
        return ResultUtil.success(pageInfo);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token", dataType = "string", paramType = "header")
    })
    @ApiOperation(value = "校验用户名唯一性，账号", notes = "校验用户名唯一性，需要 user_isExist权限或管理员权限")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success")})
    @RequiresPermissions(value = {"user_isExist", "administrator"}, logical = Logical.OR)
    @RequestMapping(value = "/user/isExist", method = RequestMethod.GET)
    public BaseResult<Boolean> isExist(@RequestParam(value="loginName")String loginName) {
        return ResultUtil.success(userService.isExist(loginName));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token", dataType = "string", paramType = "header")
    })
    @ApiOperation(value = "查单个用户所拥有的角色id", notes = "查单个用户所拥有的角色id，需要 user_getRoleByUser权限或管理员权限")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success")})
    @RequiresPermissions(value = {"user_getRoleByUser", "administrator"}, logical = Logical.OR)
    @RequestMapping(value = "/user/getRoleByUser", method = RequestMethod.GET)
    public BaseResult<List<String>> getRoleByUser(@RequestParam(value = "userId") String userId) {
        return ResultUtil.success(userRoleService.getRoleByUser(userId));
    }
}
