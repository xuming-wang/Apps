package org.air.bigearth.apps.system.controller;

import io.swagger.annotations.*;
import org.air.bigearth.apps.base.BaseResult;
import org.air.bigearth.apps.system.domain.basic.Role;
import org.air.bigearth.apps.system.domain.vm.RoleResourceVM;
import org.air.bigearth.apps.system.service.IRoleResourceService;
import org.air.bigearth.apps.system.service.IRoleService;
import org.air.bigearth.apps.util.ResultUtil;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色 控制层
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
@RestController
@RequestMapping(value="/system")
public class RoleController {

	@Autowired
	IRoleService roleService;
	
	@Autowired
	IRoleResourceService roleResourceService;
	
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "Authorization", value = "token", dataType = "string", paramType = "header")
	})
	@ApiOperation(value="新增角色",notes="新增角色，需要 role_saveRole权限或管理员权限")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@RequestMapping(value="/role",method=RequestMethod.POST)
	@RequiresPermissions(value={"role_saveRole","administrator"},logical=Logical.OR)
	public BaseResult<String> saveRole(Role role){
		roleService.saveRole(role);
		return ResultUtil.success();
	}
	
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "Authorization", value = "token", dataType = "string", paramType = "header")
	})
	@ApiOperation(value="删除角色",notes="删除角色，需要role_deleteRole 权限或管理员权限")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@RequestMapping(value="/role/{roleId}",method=RequestMethod.DELETE)
	@RequiresPermissions(value={"role_deleteRole","administrator"},logical=Logical.OR)
	public BaseResult<String> deleteRole(@PathVariable("roleId")String roleId){
		roleService.deleteRole(roleId);
		return ResultUtil.success(roleId);
	}
	
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "Authorization", value = "token", dataType = "string", paramType = "header")
	})
	@ApiOperation(value="修改角色",notes="修改角色，需要 role_updateRole权限或管理员权限")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@RequiresPermissions(value={"role_updateRole","administrator"},logical=Logical.OR)
	@RequestMapping(value="/role",method=RequestMethod.PUT)
	public BaseResult<String>updateRole(Role role){
		roleService.updateRole(role);
		return ResultUtil.success();
	}
	
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "Authorization", value = "token", dataType = "string", paramType = "header")
	})
	@ApiOperation(value="查询角色列表",notes="查询角色列表，需要role_getRoleList权限或管理员权限")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@RequiresPermissions(value={"role_getRoleList","administrator"},logical=Logical.OR)
	@RequestMapping(value="/role",method=RequestMethod.GET)
	public BaseResult<List<Role>>getRoleList(){
		List<Role> roleList=roleService.getRoleList();
		return ResultUtil.success(roleList);
	}
	
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "Authorization", value = "token", dataType = "string", paramType = "header")
	})
	@ApiOperation(value="给角色授权",notes="给角色授权，需要role_authorize权限或管理员权限")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@RequiresPermissions(value={"role_authorize","administrator"},logical=Logical.OR)
	@RequestMapping(value="/role/authorize",method=RequestMethod.POST)
	public  BaseResult<String> authorize(RoleResourceVM roleResource){
		roleService.authorize(roleResource);
		return ResultUtil.success();
	}
	
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "Authorization", value = "token", dataType = "string", paramType = "header")
	})
	@ApiOperation(value="校验code唯一性",notes="校验code唯一性，需要role_isExist权限或管理员权限")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@RequiresPermissions(value={"role_isExist","administrator"},logical=Logical.OR)
	@RequestMapping(value="/role/isExist",method=RequestMethod.GET)
	public BaseResult<Boolean> isExist(@RequestParam(value="code")String code){
		Boolean isExist=roleService.isExist(code);
		return ResultUtil.success(isExist);
	}
	
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "Authorization", value = "token", dataType = "string", paramType = "header")
	})
	@ApiOperation(value="得到某角色拥有的资源",notes="校验code唯一性，需要role_isExist权限或管理员权限")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@RequiresPermissions(value={"role_getRoleResource","administrator"},logical=Logical.OR)
	@RequestMapping(value="/role/getRoleResource",method=RequestMethod.GET)
	public BaseResult<List<String>>getRoleResource(String roleId){
		List<String >roleIds=new ArrayList<>();
		roleIds.add(roleId);
		List<String>list=roleResourceService.getResource(roleIds);
		return ResultUtil.success(list);
	}
}
