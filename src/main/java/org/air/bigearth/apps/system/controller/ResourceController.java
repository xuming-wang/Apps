package org.air.bigearth.apps.system.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

import org.air.bigearth.apps.base.BaseResult;
import org.air.bigearth.apps.system.domain.basic.Resource;
import org.air.bigearth.apps.util.ResultUtil;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.air.bigearth.apps.system.domain.basic.Authority;
import org.air.bigearth.apps.system.domain.vm.ResourceVM;
import org.air.bigearth.apps.system.service.IResourceAuthorityService;
import org.air.bigearth.apps.system.service.IResourceService;

/**
 * 资源 控制层
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
@RestController
@RequestMapping(value = "/system")
public class ResourceController {
	
	@Autowired
	IResourceService resourceService;
	
	@Autowired
	IResourceAuthorityService resourceAuthorityService;

	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "Authorization", value = "token", dataType = "string", paramType = "header")
	})
	@ApiOperation(value="新增资源",notes="新增资源，需要resource_addResource权限或管理员权限")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@RequiresPermissions(value={"resource_addResource","administrator"},logical=Logical.OR)
	@RequestMapping(value="/resource",method=RequestMethod.POST)
	public BaseResult<String> addResource(Resource resource){
		resourceService.saveResource(resource);
		return ResultUtil.success();
	}
	
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "Authorization", value = "token", dataType = "string", paramType = "header")
	})
	@ApiOperation(value="修改资源",notes="修改资源，需要resource_updateResource权限或管理员权限")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@RequiresPermissions(value={"resource_updateResource","administrator"},logical=Logical.OR)
	@RequestMapping(value="/resouce",method=RequestMethod.PUT)
	public BaseResult<String> updateResource(Resource resource){
		resourceService.updateResource(resource);
		return ResultUtil.success();
	}
	
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "Authorization", value = "token", dataType = "string", paramType = "header")
	})
	@ApiOperation(value="删除资源",notes="删除资源，需要resource_deleteResource权限或管理员权限")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@RequiresPermissions(value={"resource_deleteResource","administrator"},logical=Logical.OR)
	@RequestMapping(value="/resource/{resourceId}",method=RequestMethod.DELETE)
	public BaseResult<String> deleteResource(@PathVariable(value="resourceId")String resourceId){
		resourceService.deleteResource(resourceId);
		return ResultUtil.success();
	}
	
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "Authorization", value = "token", dataType = "string", paramType = "header")
	})
	@ApiOperation(value="查询资源列表",notes="查询资源列表，需要resource_getAllResource权限或管理员权限")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@RequiresPermissions(value={"resource_getAllResource","administrator"},logical=Logical.OR)
	@RequestMapping(value="/resource",method=RequestMethod.GET)
	public BaseResult<List<ResourceVM>>getAllResource(){
		List<ResourceVM> resouceList=resourceService.getAllResource();
		return ResultUtil.success(resouceList);
	}
	
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "Authorization", value = "token", dataType = "string", paramType = "header")
	})
	@ApiOperation(value="查询当前登录用户的资源树",notes="查询当前登录用户的资源树，需要resource_getCurrentUserResourceTree权限或管理员权限")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	//@RequiresPermissions(value={"resource_getCurrentUserResourceTree","administrator"},logical=Logical.OR)
	@RequestMapping(value="/resource/getCurrentUserResourceTree",method=RequestMethod.GET)
	public BaseResult<List<ResourceVM>>getCurrentUserResourceTree(){
		List<ResourceVM> list=resourceService.getCurrentUserResourceTree();
		return ResultUtil.success(list);
	}
	
	
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "Authorization", value = "token", dataType = "string", paramType = "header")
	})
	@ApiOperation(value="查单个资源 拥有的操作码",notes="查单个资源 拥有的操作码，需要resource_getAuthorityByResource权限或管理员权限")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@RequiresPermissions(value={"resource_getAuthorityByResource","administrator"},logical=Logical.OR)
	@RequestMapping(value="/resource/getAuthorityByResource",method=RequestMethod.GET)
	public BaseResult<List<Authority>>getAuthorityByResource(@RequestParam(value="ResourceId")String ResourceId){
		List<Authority> list=resourceAuthorityService.getAuthorityByResource(ResourceId);
		return ResultUtil.success(list);
	}
	
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "Authorization", value = "token", dataType = "string", paramType = "header"),
		@ApiImplicitParam(name = "resourceId", value = "resourceId", dataType = "string", paramType = "query"),
		@ApiImplicitParam(name = "authorityId", value = "authorityId，如果为null，则取消资源的所有权限"
		,required=false, dataType = "string", paramType = "query")
	})
	@ApiOperation(value="取消授权,取消一个资源的某个 或一些 权限",notes="取消授权，需要resource_deleteResourceAuthority权限或管理员权限")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@RequiresPermissions(value={"resource_deleteResourceAuthority","administrator"},logical=Logical.OR)
	@RequestMapping(value="/resource/deleteResourceAuthority",method=RequestMethod.POST)
	public BaseResult<String>deleteResourceAuthority(@RequestParam(value="resourceId")String resourceId,@RequestParam(value="authorityId")String authorityId){
		resourceAuthorityService.deleteResourceAuthority(resourceId,authorityId);
		return ResultUtil.success();
	}
	
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "Authorization", value = "token", dataType = "string", paramType = "header")
	})
	@ApiOperation(value="给资源授权,单个操作码给单个资源",notes="给资源授权，需要resource_authorize权限或管理员权限")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@RequiresPermissions(value={"resource_authorize","administrator"},logical=Logical.OR)
	@RequestMapping(value="/resource/authorize",method=RequestMethod.POST)
	public BaseResult<String> authorize(@RequestParam(value="resourceId")String resourceId,@RequestParam(value="authorityId")String authorityId){
		resourceAuthorityService.authorize(resourceId,authorityId);
		return ResultUtil.success();
	}
}
