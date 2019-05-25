package org.air.bigearth.apps.system.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

import org.air.bigearth.apps.base.BaseResult;
import org.air.bigearth.apps.util.ResultUtil;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.air.bigearth.apps.system.domain.basic.Authority;
import org.air.bigearth.apps.system.domain.vm.AuthoritySaveVM;
import org.air.bigearth.apps.system.domain.vm.AuthorityUpdateVM;
import org.air.bigearth.apps.system.domain.vm.AuthorityVMS;
import org.air.bigearth.apps.system.service.IAuthorityService;

/**
 * 权限 控制层
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
@RestController
@RequestMapping(value="/system")
public class AuthorityController {
	
	@Autowired
	IAuthorityService authorityService;

	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "Authorization", value = "token", dataType = "string", paramType = "header")
	})
	@ApiOperation(value="按controller分组，得到authority列表",notes="按controller分组，得到authority列表，需要authority_getAuthority权限或管理员权限")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@RequiresPermissions(value={"authority_getAuthority","administrator"},logical=Logical.OR)
	@RequestMapping(value="/authority",method=RequestMethod.GET)
	public BaseResult<List<AuthorityVMS>> getAuthority(){
		List<AuthorityVMS> list=authorityService.getAuthority();
		return ResultUtil.success(list);
	}
	
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "Authorization", value = "token", dataType = "string", paramType = "header"),
		@ApiImplicitParam(name = "method", value = "请求方式", dataType = "string", paramType = "query"),
		@ApiImplicitParam(name = "word", value = "关键字", dataType = "string", paramType = "query")
	})
	@ApiOperation(value="根据请求方式，按关键字模糊查询",notes="根据请求方式，按关键字模糊查询，需要authority_getLikeAuthority权限或管理员权限")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@RequiresPermissions(value={"authority_getLikeAuthority","administrator"},logical=Logical.OR)
	@RequestMapping(value="/getLikeAuthority",method=RequestMethod.GET)
	public BaseResult<List<Authority>> getLikeAuthority(@RequestParam("method")String method,@RequestParam("word")String word){
		List<Authority> list=authorityService.getLikeAuthority(method,word);
		return ResultUtil.success(list);
	}
	
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "Authorization", value = "token", dataType = "string", paramType = "header")
	})
	@ApiOperation(value="新增操作码",notes="新增操作码，需要authority_saveAuthority权限或管理员权限")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@RequiresPermissions(value={"authority_saveAuthority","administrator"},logical=Logical.OR)
	@RequestMapping(value="/authority",method=RequestMethod.POST)
	public BaseResult<String> saveAuthority(@RequestBody AuthoritySaveVM authoritySaveVM){
		authorityService.saveAuthority(authoritySaveVM);
		return ResultUtil.success();
	}
	
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "Authorization", value = "token", dataType = "string", paramType = "header")
	})
	@ApiOperation(value="修改操作码",notes="修改操作码，需要authority_updateAuhtority权限或管理员权限")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@RequiresPermissions(value={"authority_updateAuhtority","administrator"},logical=Logical.OR)
	@RequestMapping(value="/authority",method=RequestMethod.PUT)
	public BaseResult<String> updateAuhtority(@RequestBody AuthorityUpdateVM authorityUpdateVM){
		authorityService.updateAuhtority(authorityUpdateVM);
		return ResultUtil.success();
	}
	
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "Authorization", value = "token", dataType = "string", paramType = "header")
	})
	@ApiOperation(value="删除操作码",notes="删除操作码，需要authority_deleteAuthority权限或管理员权限")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@RequiresPermissions(value={"authority_deleteAuthority","administrator"},logical=Logical.OR)
	@RequestMapping(value="/authority/{id}",method=RequestMethod.DELETE)
	public BaseResult<String> deleteAuthority(@PathVariable("id")String id){
		authorityService.deleteAuthority(id);
		return ResultUtil.success();
	}
}
