package org.air.bigearth.apps.file.controller;

import org.air.bigearth.apps.exception.service.BaseException;
import org.air.bigearth.apps.file.domain.FileStrategy;
import org.air.bigearth.apps.file.service.IFileStrategyService;
import org.air.bigearth.apps.base.EditTypeEnum;
import org.air.bigearth.apps.util.StringUtil;
import org.omg.CORBA.SystemException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




/**
 * 文件策略 控制层
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-02-27
 */
@Controller
@RequestMapping(value = "/file/")
public class FileStrategyController {

	@Resource
	private IFileStrategyService fileStrategysService;

	@RequestMapping(value = "fileStrategyList")
	public ModelAndView fileStrategyList(HttpServletRequest request,
			FileStrategy sysFileStrategyDTO) throws SystemException {
		Map<String, Object> result = new HashMap<String, Object>();

		Map<String, Object> params = null;//PageUtil.getPageDtoUtilToMap(request);
		params.put("strategyCode", sysFileStrategyDTO.getStrategyCode());
		params.put("strategyName", sysFileStrategyDTO.getStrategyName());
		params.put("strategyId", sysFileStrategyDTO.getStrategyId());
		List<FileStrategy> FileStrategyDTOList = new ArrayList<FileStrategy>();
		try{
			FileStrategyDTOList = fileStrategysService.searchByParamsPage(params);
		}catch(Exception e){
			e.printStackTrace();
		}
		result.put("sysFileStrategyDTO", sysFileStrategyDTO);
		result.put("sysFileStrategyDTOList", FileStrategyDTOList);
		return new ModelAndView("/file/fileStrategyList", result);
	}

	/**
	  * 根据文件策略id获取code
	  *
	  * @param request
	  * @return FileStrategyDTO (返回类型)
	 */
	@RequestMapping(value = "fileStrategysCode")
	@ResponseBody
	public List<FileStrategy> fileStrategysCode(HttpServletRequest request){
		Map<String, Object> params = null;//PageUtil.getPageDtoUtilToMap(request);
		params.put("strategyId", request.getParameter("strategyId"));
		List<FileStrategy> FileStrategyDTOList = new ArrayList<FileStrategy>();
		try{
			FileStrategyDTOList = fileStrategysService.searchByParamsPage(params);
		}catch(Exception e){
			e.printStackTrace();
		}
		return FileStrategyDTOList;
	}
	@RequestMapping(value = "fileStrategysEdit")
	public ModelAndView fileStrategysEdit(HttpServletRequest request, FileStrategy sysFileStrategyDTO)
			throws BaseException {
		Map<String,Object> result = new HashMap<String, Object>( );
		String editType = sysFileStrategyDTO.getEditType();
		result.put("readOnly", EditTypeEnum.INFO.value.equalsIgnoreCase(editType));
		String updateFlag = request.getParameter("updateFlag");
		if (EditTypeEnum.UPDATE.value.equalsIgnoreCase(editType)) {
			String strategyId =sysFileStrategyDTO.getStrategyId();
			if(!strategyId.contains(",")){
				sysFileStrategyDTO = fileStrategysService.getById(strategyId);
			}
			result.put("updateFlag", updateFlag);
			sysFileStrategyDTO.setEditType(editType);
			result.put("sysFileStrategyDTO", sysFileStrategyDTO);
			return new ModelAndView("/file/fileStrategyEdit",result);
			
		} else if (EditTypeEnum.INFO.value.equalsIgnoreCase(editType)) {
			sysFileStrategyDTO = fileStrategysService.getById(sysFileStrategyDTO.getStrategyId());
			sysFileStrategyDTO.setEditType(editType);
			result.put("updateFlag", true);
			result.put("sysFileStrategyDTO", sysFileStrategyDTO);
			return new ModelAndView("/file/fileStrategyEdit",result);
		}
		return null;
	}
	
	@RequestMapping(value = "fileStrategysSave")
	@ResponseBody
	public Map<String, Object> fileStrategysSave(HttpServletRequest request, FileStrategy sysFileStrategyDTO)
			throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		if (EditTypeEnum.UPDATE.value.equalsIgnoreCase(sysFileStrategyDTO.getEditType())) {
		   try {
			   if (null != sysFileStrategyDTO.getStrategyId()
					   && !"".equals(sysFileStrategyDTO.getStrategyId())) {
				    
				    String updateFlag = request.getParameter("updateFlag");
				    String strategyId = sysFileStrategyDTO.getStrategyId();
					if(updateFlag.equals("false")){
						  String strategyIds[]= strategyId.split(",");
						  for (int i = 0; i < strategyIds.length; i++) {
							  sysFileStrategyDTO.setStrategyId(strategyIds[i]);
							  fileStrategysService.updateAllDto(sysFileStrategyDTO);
							  result.put("success", "批量修改成功!");
						  }
				   }else{
					   if(sysFileStrategyDTO.getIsMultiFileUpload()==2){
						   sysFileStrategyDTO.setMaxUploadFileNumber(1);
					   }
					   String uploadfileType =sysFileStrategyDTO.getUploadfileType().replaceAll(" ", "");
					   sysFileStrategyDTO.setUploadfileType(StringUtil.trim(uploadfileType));
					   String forbidfileType =sysFileStrategyDTO.getForbidfileType().replaceAll(" ", "");
					   sysFileStrategyDTO.setForbidfileType(StringUtil.trim(forbidfileType));
//					   fileStrategysService.updateDto(sysFileStrategyDTO);
					   result.put("success", "修改成功!");
				   }
			  } 
		   } catch (Exception e) {
			e.printStackTrace();
			result.put("err", true);
			result.put("errMsg", "对不起，保存失败...");
		   }
		}
		return result;
	}
	
	/**
	 * 
	 * 启用停用文件策略
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception ModelAndView
	 */
	@RequestMapping(value="changeStrategysState")
	@ResponseBody
	public Map<String,Object> changeStrategysState(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Map<String,Object> msg = new HashMap<String,Object>();
		boolean isSuc = false;
		String strategyIds = request.getParameter("strategyIds");
		if(strategyIds.replaceAll(",", "").length()<40){
			FileStrategy dto = this.fileStrategysService.getById(strategyIds.replaceAll(",", ""));
			if(dto!=null && "default".equalsIgnoreCase(dto.getStrategyCode())){
				msg.put("success", false);
				msg.put("msgFlag", 1);
				msg.put("msg", "默认策略不允许停用");
				return msg;
			}
		}
		String state = request.getParameter("state");
		if(StringUtil.isNotNullOrBlank(strategyIds)){
			try{
				isSuc = this.fileStrategysService.changeState(strategyIds.split(","), state);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		msg.put("success", isSuc);
		msg.put("msgFlag", 0);
		msg.put("msg", isSuc?"操作成功":"操作失败");
		return msg;
	}
	
	@RequestMapping(value="fileUploadPage")
	public ModelAndView sysFileUploadPage(HttpServletRequest request){
		Map<String,Object> result = new HashMap<String, Object>( );
		return new ModelAndView("/file/fileUploadPage",result);
	}
	
}