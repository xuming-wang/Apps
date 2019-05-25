package org.air.bigearth.apps.system.service.impl;

import org.air.bigearth.apps.system.domain.basic.Resource;
import org.air.bigearth.apps.system.domain.basic.User;
import org.air.bigearth.apps.system.domain.vm.ResourceSaveVM;
import org.air.bigearth.apps.system.domain.vm.ResourceUpdateVM;
import org.air.bigearth.apps.system.domain.vm.ResourceVM;
import org.air.bigearth.apps.system.mapper.basic.ResourceMapper;
import org.air.bigearth.apps.system.mapper.extend.ResourceExtendMapper;
import org.air.bigearth.apps.system.service.*;
import org.air.bigearth.apps.util.Asserts;
import org.air.bigearth.apps.util.DataKeyUtil;
import org.air.bigearth.apps.util.EntityUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 资源 业务层处理
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
@Service
@Transactional
public class ResourceServiceImpl implements IResourceService {

	@Autowired
	ResourceMapper  resourceMapper;

	@Autowired
	IUserRoleService userRoleService;

	@Autowired
	IRoleResourceService roleResourceService;

	@Autowired
	ResourceExtendMapper resourceExtendMapper;

	@Autowired
	IResourceAuthorityService resourceAuthorityService;

	@Autowired
    IAuthorityService authorityService;
	
	//@Autowired
	//RedissonClient redissonClient;

	@Override
	public void addResource(ResourceSaveVM resourceSaveVM) {

		Asserts.validate(resourceSaveVM, "resourceSaveVM");
		
		Resource resource = EntityUtils.vm2Entity(resourceSaveVM, Resource.class);
		resource.setId(UUID.randomUUID().toString());
		
		//根据参数父节点code 查出父节点id set，并组合子节点path
		Resource parentResource = resourceExtendMapper.getIdByCode(resourceSaveVM.getParentCode());
		if(parentResource!=null){
			resource.setPid(parentResource.getId());
			resource.setPath(parentResource.getPath()+"/"+resource.getCode());
		}else{
			resource.setPid("pid");
			resource.setPath("/"+resource.getCode());
		}
		
		
		resourceMapper.insert(resource);
		
		User user = (User)SecurityUtils.getSubject().getPrincipal();
		String userId=user.getId();
		deleteRedis(userId);
	}

	@Override
	public void saveResource(Resource resource) {
		resource.setId(DataKeyUtil.getDataKey());
		//根据参数父节点code 查出父节点id set，并组合子节点path
		Resource parentResource = resourceExtendMapper.getIdByCode(resource.getParentCode());
		if(parentResource!=null){
			resource.setPid(parentResource.getId());
			resource.setPath(parentResource.getPath()+"/"+resource.getCode());
		}else{
			resource.setPid("pid");
			resource.setPath("/"+resource.getCode());
		}


		resourceMapper.insert(resource);

		User user = (User)SecurityUtils.getSubject().getPrincipal();
		String userId=user.getId();
		deleteRedis(userId);
	}

	@Override
	public void updateResource(ResourceUpdateVM resourceUpdateVM) {
		Asserts.validate(resourceUpdateVM,"resourceUpdateVM");
		
		Resource resource = EntityUtils.vm2Entity(resourceUpdateVM, Resource.class);
		
		//根据参数父节点code 查出父节点id set，并组合子节点path
		Resource parentResource = resourceExtendMapper.getIdByCode(resourceUpdateVM.getParentCode());
		resource.setPid(parentResource.getId());
		
		Resource lastResource = resourceMapper.selectByPrimaryKey(resource.getId());
		String lastCode=lastResource.getCode();
		String nextCode=resource.getCode();
		//如果code 修改了，下级的path要改
		if(!lastCode.equals(nextCode)){
			//根据code 查出  此节点资源  及以下的 所有资源
			List<Resource> resourceList = resourceExtendMapper.selectByCode(lastCode);
			for(Resource r:resourceList){
				r.setPath(r.getPath().replaceAll(lastCode, nextCode));
				resourceMapper.updateByPrimaryKey(r);
			}
		}else{
			resourceMapper.updateByPrimaryKey(resource);
		}
		
		User user = (User)SecurityUtils.getSubject().getPrincipal();
		String userId=user.getId();
		deleteRedis(userId);
	}

	@Override
	public void updateResource(Resource resource) {
		//根据参数父节点code 查出父节点id set，并组合子节点path
		Resource parentResource = resourceExtendMapper.getIdByCode(resource.getParentCode());
		resource.setPid(parentResource !=null ? parentResource.getId() : "");

		Resource lastResource = resourceMapper.selectByPrimaryKey(resource.getId());
		String lastCode=lastResource.getCode();
		String nextCode=resource.getCode();
		//如果code 修改了，下级的path要改
		if(!lastCode.equals(nextCode)){
			//根据code 查出  此节点资源  及以下的 所有资源
			List<Resource> resourceList = resourceExtendMapper.selectByCode(lastCode);
			for(Resource r:resourceList){
				r.setPath(r.getPath().replaceAll(lastCode, nextCode));
				resourceMapper.updateByPrimaryKey(r);
			}
		}else{
			resourceMapper.updateByPrimaryKey(resource);
		}

		User user = (User)SecurityUtils.getSubject().getPrincipal();
		String userId=user.getId();
		deleteRedis(userId);
	}

	@Override
	public void deleteResource(String resourceId) {
		Asserts.notEmpty(resourceId);
		
		//先删资源对应的权限
		resourceAuthorityService.deleteResourceAuthority(resourceId,null);
		//删除角色资源关系
		roleResourceService.deleteRoleResource(null, resourceId);
		//根据code path删除本身及下级资源
		Resource resource = resourceMapper.selectByPrimaryKey(resourceId);
		resourceExtendMapper.deleteByPath(resource.getCode());
		
		User user = (User)SecurityUtils.getSubject().getPrincipal();
		String userId=user.getId();
		deleteRedis(userId);
	}

	/**
	 * 得到资源列表
	 * @return
	 */
	@Override
	public List<ResourceVM> getAllResource() {
		List<Resource> all = resourceMapper.selectAll();
		return parseTree(all);
	}

	/**
	 * 将资源集合封装为树
	 */
	private List<ResourceVM>parseTree(List<Resource>resourceList){
		
		List<ResourceVM> list = EntityUtils.entity2VMList(resourceList,ResourceVM.class);
		Map <String,ResourceVM>map=new HashMap<String,ResourceVM>();
		List<ResourceVM>root=new ArrayList<ResourceVM>();
		for(ResourceVM resouceVM:list){
			resouceVM.setChildren(new ArrayList<ResourceVM>());
			map.put(resouceVM.getId(),resouceVM);
		}
		
		for(ResourceVM resourceVM:list){
			ResourceVM parentResource = map.get(resourceVM.getPid());
			if(parentResource==null){
				root.add(resourceVM);
				continue;
			}
			parentResource.getChildren().add(resourceVM);
		}
		return root;
	}
	
	
	/**
	 * 得到当前用户资源树
	 * @return
	 */
	@Override
	public List<ResourceVM> getCurrentUserResourceTree() {
		
		User user = (User)SecurityUtils.getSubject().getPrincipal();
		String userId=user.getId();
		
		//先从缓存中拿，如果 前端有多个模块，根据code 查询资源树更合适
		//RmapCache<userID,HashMap<resource.code,resourceTree>>这个结构,可以很快拿到每个code节点下的 树
		
		/*RMapCache<String,List<ResourceVM>> mapCache = redissonClient.getMapCache(Constants.REDIS_RESOURCE_TREE);//每个userid对应的资源树
		RMapCache<String, List<Resource>> mapCache2 = redissonClient.getMapCache(Constants.REDIS_RESOURCE);//每个userid对应的无序资源集
		if(mapCache.get(userId)!=null){

			return mapCache.get(userId);

		}else if(mapCache2.get(userId)!=null){
			List<ResourceVM> parseTree = parseTree(mapCache2.get(userId));
			mapCache.put(userId, parseTree);
			return parseTree;

		}else*/
		if(user.getSuperman()){
			//超管
			List<ResourceVM> allResource = getAllResource();
			//mapCache.put(userId, allResource);
			return allResource;
		}
		else{
			//每个用户会有一个基本的角色，保证能看到一些基本菜单，不会出现 roleIds resourceIds空的情况
			List<Resource>resourceList=getResourceByUserId(userId);
			List<ResourceVM> parseTree = parseTree(resourceList);
			//mapCache.put(userId, parseTree);
			return parseTree;
		}
	}

	/**
	 * 根据资源id集合查出资源
	 */
	@Override
	public List<Resource> selectByResourceIds(List<String> resourceIds){
		if(CollectionUtils.isEmpty(resourceIds)){return null;}
		List<Resource> resourceList = resourceExtendMapper.selectByResourceIds(resourceIds);
		return resourceList;
	}
	
	/**
	 * 根据 userid查出用户拥有的资源
	 */
	@Override
	public List<Resource>getResourceByUserId(String userId){
		List<String> roleIds = userRoleService.getRoleByUser(userId);
		List<String> resourceIds = roleResourceService.getResource(roleIds);
		return selectByResourceIds(resourceIds);
	}
	/**
	 * 得到全部资源
	 */
	@Override
	public List<Resource>getAll(){
		return resourceMapper.selectAll();
	}
	
	/**
	 * 清空某用户id的资源树缓存，在资源变动时调用
	 */
	@Override
	public void deleteRedis(String userId){
		//RMapCache<String,List<ResourceVM>> mapCache = redissonClient.getMapCache(Constants.REDIS_RESOURCE_TREE);//每个userid对应的资源树
		//RMapCache<String, List<Resource>> mapCache2 = redissonClient.getMapCache(Constants.REDIS_RESOURCE);//每个userid对应的无序资源集
		//if(mapCache!=null){mapCache.remove(userId);}
		//if(mapCache2!=null){mapCache2.remove(userId);}
	}

}
