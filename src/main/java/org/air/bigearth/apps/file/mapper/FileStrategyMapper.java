package org.air.bigearth.apps.file.mapper;

import org.air.bigearth.apps.base.PlatformMapper;
import org.air.bigearth.apps.file.domain.FileStrategy;
import org.air.bigearth.apps.file.exception.FileException;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


/**
 * 文件策略 数据访问层
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-02-22
 */
@Mapper
public interface FileStrategyMapper extends PlatformMapper {

	/**
	 * 新增文件策略
     *
	 * @param FileStrategyDTO
	 * @return
	 * @throws FileException
	 */
	public boolean insertFileStrategyDTO(FileStrategy FileStrategyDTO) ;
	
	/**
	 * 根据文件策略Id得到对象
     *
	 * @param FileStrategyId
	 * @return
	 * @
	 */
	public FileStrategy getByFileStrategyId(String FileStrategyId) ;
	
	/**
	 * 根据类型得到对象
     *
	 * @param type
	 * @return
	 * @
	 */
	public FileStrategy getByType(String type) ;
	
	/**
	 * 
	 * 更改文件策略状态
	 * 
	 * @param map
	 * @return 操作结果
	 */
	public void enableByIds(Map<String, Object> map);
	/**
	 * 
	 * 更改文件策略状态
	 * 
	 * @param params
	 * @return int 操作结果
	 */
	public int changeState(Map<String, Object> params);
	/**
	 * 更新dto
     *
	 * @param fileStrategy
	 * @return
	 */
	public boolean updateAllDto(FileStrategy fileStrategy);
	/**
	  * 根据菜单code查询
	  *
	  * @param strategyCode
	  * @return FileStrategyDTO (返回类型)
	 */
	public FileStrategy getByCode(String strategyCode);
	/**
	 * 
	 * 查询文件策略列表
	 * 
	 * @return List<FileStrategyDTO> (返回类型)
	 */
	public List<FileStrategy> selectFileStrategyList();
}
