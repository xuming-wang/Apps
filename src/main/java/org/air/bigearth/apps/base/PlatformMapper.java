package org.air.bigearth.apps.base;

import java.util.List;
import java.util.Map;



/**
 * 所有Mapper的超级接口
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-02-22
 */
public interface PlatformMapper {

	/**
	 * 新增dto
	 *
	 * @param dto
	 * @return
	 */
	public boolean insertDto(Object dto);
	
	/**
	 * 更新dto
	 *
	 * @param dto
	 * @return
	 */
	public boolean updateDto(Object dto);	
	
	/**
	 * 根据主键批量删除dto
	 *
	 * @param ids
	 * @return
	 */
	public boolean deleteByIds(String[] ids);
	
	/**
	 * 根据id查询对应的dto
	 *
	 * @param id
	 * @return
	 */
	public Object getById(String id);

	/**
	 * 根据条件检索
	 * @return
	 */
	public List<?> searchByParamsPage(Map<String, Object> params);
	

}
