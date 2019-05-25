package org.air.bigearth.apps.system.service;

import org.air.bigearth.apps.system.domain.basic.Resource;
import org.air.bigearth.apps.system.domain.vm.ResourceSaveVM;
import org.air.bigearth.apps.system.domain.vm.ResourceUpdateVM;
import org.air.bigearth.apps.system.domain.vm.ResourceVM;

import java.util.List;

/**
 * 资源 业务层
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
public interface IResourceService {


    public void addResource(ResourceSaveVM resourceSaveVM);

    public void saveResource(Resource resource);

    public void updateResource(ResourceUpdateVM resourceUpdateVM);

    public void updateResource(Resource resource);

    public void deleteResource(String resourceId);

    /**
     * 得到资源列表
     *
     * @return
     */
    public List<ResourceVM> getAllResource();

    /**
     * 得到当前用户资源树
     *
     * @return
     */
    public List<ResourceVM> getCurrentUserResourceTree();

    /**
     * 根据资源id集合查出资源
     */
    public List<Resource> selectByResourceIds(List<String> resourceIds);

    /**
     * 根据 userid查出用户拥有的资源
     */
    public List<Resource> getResourceByUserId(String userId);

    /**
     * 得到全部资源
     */
    public List<Resource> getAll();

    /**
     * 清空某用户id的资源树缓存，在资源变动时调用
     */
    public void deleteRedis(String userId);

}
