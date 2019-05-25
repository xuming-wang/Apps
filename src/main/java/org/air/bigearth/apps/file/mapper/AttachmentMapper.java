package org.air.bigearth.apps.file.mapper;

import org.air.bigearth.apps.base.PlatformMapper;
import org.air.bigearth.apps.file.domain.Attachment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


/**
 * 附件 数据访问层
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-02-22
 */
@Mapper
public interface AttachmentMapper extends PlatformMapper {

	void deleteAttachment(Map<String, Object> params);

	void updateSaveState(String[] attIds);

	List<Attachment> attachmentParams(Map<String, Object> params);

	void saveStateAttachment(Map<String, Object> params);

	void deleteAttachmentIds(String[] attachmentIds);

	List<Attachment> attachmentBusiIds(String[] busiIds);
	
	/**
	 * 根据附件ID批量查询附件
	 * @param attIdList
	 * @return
	 */
	List<Attachment> selectAttachmentListByIds(List<String> attIdList);

}
