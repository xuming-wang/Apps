package org.air.bigearth.apps.file.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.air.bigearth.apps.exception.service.BaseException;
import org.air.bigearth.apps.file.exception.FileException;
import org.air.bigearth.apps.file.mapper.FileStrategyMapper;
import org.air.bigearth.apps.file.domain.FileStrategy;
import org.air.bigearth.apps.file.service.IFileStrategyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



/**
 * 文件策略 业务层处理
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-02-22
 */
@Service
public class FileStrategyServiceImpl implements IFileStrategyService {
	@Resource
	private FileStrategyMapper fileStrategyDAO;
	
	@Override
	public boolean saveFileStrategyDTO(FileStrategy FileStrategyDTO)
			throws FileException {

		return fileStrategyDAO.insertFileStrategyDTO(FileStrategyDTO);
	}

	@Override
	public FileStrategy getByFileStrategyId(String FileStrategyId)
			throws FileException {
		return fileStrategyDAO.getByFileStrategyId(FileStrategyId);
	}

	@Override
	public FileStrategy getByType(String type) throws FileException {
		return fileStrategyDAO.getByType(type);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public boolean updateAllDto(FileStrategy FileStrategyDTO) {
		fileStrategyDAO.updateAllDto(FileStrategyDTO);
		return false;
	}
	
	@Override
	public boolean deleteByIds(String[] ids) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public FileStrategy getById(String strategyId) {
		return (FileStrategy)fileStrategyDAO.getById(strategyId);
	}

	@Override
	public List<FileStrategy> searchByParamsPage(
			Map<String, Object> params) {
		return (List<FileStrategy>)fileStrategyDAO.searchByParamsPage(params);
	}

	@Override
	public void enableByIds(Map<String, Object> map) {
		fileStrategyDAO.enableByIds(map);
	}
	/**
	 *
	 * 更改状态
	 *
	 * @param state 状态(1启用、2禁用)
	 * @return boolean 操作结果
	 */
	@Override
	public boolean changeState(String[] strategyIds,String state){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("strategyIds", strategyIds);
		params.put("state", state);
		return this.fileStrategyDAO.changeState(params)>0;
	}

	@Override
	public boolean insertDto(FileStrategy dto) throws BaseException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateDto(FileStrategy dto) throws BaseException {
		FileStrategy sysFileStrategyDTO = (FileStrategy)dto;
		fileStrategyDAO.updateDto(sysFileStrategyDTO);
		return false;
	}

	@Override
	public FileStrategy getByCode(String strategyCode) {
		// TODO Auto-generated method stub
		return fileStrategyDAO.getByCode(strategyCode);
	}
	/**
	 * 
	 * 查询文件策略列表
	 * 
	 * @return List<FileStrategyDTO> (返回类型)
	 */
	@Override
	public List<FileStrategy> selectFileStrategyList() throws BaseException{
		try{
			//return this.fileStrategyDAO.selectFileStrategyList();
			return null;
		}catch(Exception e){
			throw new FileException("查询文件策略异常!");
		}
	}

}
