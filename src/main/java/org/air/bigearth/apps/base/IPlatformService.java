package org.air.bigearth.apps.base;

import org.air.bigearth.apps.exception.service.BaseException;

import java.util.List;
import java.util.Map;


/**
 * 所有Service的超级接口
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-02-22
 */
public interface IPlatformService<T extends BaseDTO> {

	/**
	 * 新增dto
	 *
	 * @param dto
	 * @return
	 * @throws Exception 
	 */
	public boolean insertDto(T dto) throws BaseException;
	
	/**
	 * 更新dto
	 *
	 * @param dto
	 * @return
	 */
	public boolean updateDto(T dto) throws BaseException, BaseException;
	
	/**
	 * 根据主键批量删除dto
	 *
	 * @param ids
	 * @return
	 */
	public boolean deleteByIds(String[] ids) throws BaseException;
	
	/**
	 * 根据id查询对应的dto
	 *
	 * @param id
	 * @return
	 */
	public T getById(String id) throws BaseException;
	
	/**
	 * 根据条件检索
	 *
	 * @return
	 */
	public List<T> searchByParamsPage(Map<String, Object> params) throws BaseException;
}
