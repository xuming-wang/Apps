package org.air.bigearth.apps.file.service;

import org.air.bigearth.apps.base.IPlatformService;
import org.air.bigearth.apps.exception.service.BaseException;
import org.air.bigearth.apps.file.domain.Attachment;
import org.air.bigearth.apps.file.domain.FileStrategy;
import org.air.bigearth.apps.file.domain.MultipartFileParam;
import org.air.bigearth.apps.file.exception.FileException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;




/**
 * 附件 业务层
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-02-22
 */
public interface IAttachmentService extends IPlatformService<Attachment> {

	/**
	 * 采用spring提供的上传文件的方法
	 *
	 * @param request
	 * @return Map<String,Object> (返回类型)
	 * @throws BaseException
	 */
	public Map<String, Object> uploadAttachment(HttpServletRequest request, MultipartFile[] files) throws BaseException;

	/**
	 * 根据id删除附件
	 *
	 * @param attachmentIds 附件id,多个以逗号隔开
	 * @throws BaseException
	 */
	public void deleteAttachment(String attachmentIds) throws BaseException;
	
	/**
	 * 查询附件信息
	 *
	 * @param params 中参数1：attachmentId(附件id) 2:busiId(业务id) 3:busiType(业务类型)
	 * @return
	 * @throws BaseException
	 */
	public List<Attachment> attachmentParams(Map<String, Object> params) throws BaseException;

	/**
	 * 根据文件策略code获取文件策略信息
	 *
	 * @param code
	 * @return
	 * @throws BaseException
	 */
	public FileStrategy getFileStrategyCode(String code) throws BaseException;

	/**
	 * 获取加密类型
	 *
	 * @param cryptType
	 * @return
	 * @throws BaseException
	 */
	public String getEncryptType(String cryptType) throws BaseException;

	/**
	 * 文件解压缩
	 *
	 * @param AttachmentDTO
	 * @return
	 */
	public File unzip(Attachment AttachmentDTO);
	
	/**
	 * 根据批量附件ID查询附件
	 *
	 * @param attIdArray
	 * @return List<AttachmentDTO>
	 */
	public List<Attachment> selectAttListByIds(String[] attIdArray);

	/**
	 * 上传文件方法1
	 *
	 * @param param
	 * @throws IOException
	 */
	void uploadFileRandomAccessFile(MultipartFileParam param) throws IOException;

	/**
	 * 上传文件方法2
	 * 处理文件分块，基于MappedByteBuffer来实现文件的保存
	 *
	 * @param param
	 * @throws IOException
	 */
	Map<String, Object> uploadFileByMappedByteBuffer(MultipartFileParam param, HttpServletRequest request) throws IOException;

	/**
	 * 保存附件信息
	 *
	 * @param requestParams
	 * @return
	 */
	public Map<String, Object> saveAttachment(Map<String, String> requestParams);

}
