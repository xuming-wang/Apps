package org.air.bigearth.apps.file.service;

import org.air.bigearth.apps.base.IPlatformService;
import org.air.bigearth.apps.exception.service.BaseException;
import org.air.bigearth.apps.file.domain.FileStrategy;

import java.util.List;
import java.util.Map;


/**
 * 文件策略 业务层
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-02-22
 */
public interface IFileStrategyService extends IPlatformService<FileStrategy> {

	/**
	 * 保存文件策略，根据文件策略判断，如果存在则修改，如果不存在则新增
	 * @param FileStrategyDTO
	 * @return
	 * @throws BaseException
	 */
	public boolean saveFileStrategyDTO(FileStrategy FileStrategyDTO)throws BaseException;
	
	/**
	 * 根据文件策略Id得到对象
	 * @param FileStrategyId
	 * @return
	 * @throws BaseException
	 */
	public FileStrategy getByFileStrategyId(String FileStrategyId)throws BaseException;
	
	/**
	 * 根据类型得到对象
	 * @param type
	 * @return
	 * @throws BaseException
	 */
	public FileStrategy getByType(String type)throws BaseException;
	
	/**
	 * 
	 * 更改文件策略状态
	 * 
	 * @param map
	 * @param state 状态(1启用、2禁用)
	 * @return void 操作结果
	 */
	public void enableByIds(Map<String, Object> map)throws BaseException;
	/**
	 *
	 * 更改菜单状态
	 *
	 * @param strategyIds
	 * @param state 状态(1启用、2禁用)
	 * @return boolean 操作结果
	 */
	public boolean changeState(String[] strategyIds, String state)throws BaseException;
	/**
	 * 更新dto
	 * @param fileStrategyDTO
	 * @return
	 */
	public boolean updateAllDto(FileStrategy fileStrategyDTO)throws BaseException;

	public FileStrategy getByCode(String strategyCode)throws BaseException;
	/**
	 * 
	 * 查询文件策略列表
	 * 
	 * @return List<FileStrategyDTO> (返回类型)
	 */
	public List<FileStrategy> selectFileStrategyList() throws BaseException;
}
