package org.air.bigearth.apps.file.domain;


import org.air.bigearth.apps.base.BaseDTO;

import java.math.BigInteger;

/**
 * 附件表 apps_t_sys_attachment
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-02-20
 */
public class Attachment extends BaseDTO {

	private static final long serialVersionUID = 1L;

	/**
     * 附件ID
     */
    private String attachmentId;
    /**
     * 附件
     */
    private Object attachment;

    /**
     * 业务ID
     */
    private String busiId;

    /**
     * 业务类型
     */
    private String busiType;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 原始文件名
     */
    private String originalFileName;

    /**
     * 基于路径(根路径)
     */
    private String basePath;

    /**
     * 文件路径(完整路径)
     */
    private String filePath;

    /**
     * 文件大小(单位KB)
     */
    private BigInteger fileSize;

    /**
     * 文件后缀名(不带点)
     */
    private String suffix;

    /**
     * 是否加密(1加密 2不加密)
     */
    private String encryptedFlag;
    
    /**
     * 加密方式(值为NOCRYPTED时表示未加密)
     */
    private String encryptedType;

    /**
     * 加密密钥
     */
    private String encryptKey;

    /**
     * 是否压缩(1压缩 2不压缩)
     */
    private Integer compressedFlag;

    /**
     * 文件签名
     */
    private String signature;

    /**
     * 文件策略ID
     */
    private String strategyId;

    /**
     * 上传时间
     */
    private String uploadTime;

    /**
     * 附件版本
     */
    private Integer attachmentVersion;
    
    /**
     * 所属策略
     */
    private String strategyName;
    
    private String startTime;
    
    private String endTime;
    
    private Integer saveState;
	/**
	 * 文件编码：即文件md5值
	 */
    private String fileCode;
	/**
	 * 元数据信息（json格式）
	 */
	private String uploadMetadata;
	/**
	 * 是否共享 1:是,0:否
	 */
	private String fileShare;

	public String getStrategyName() {
		return strategyName;
	}

	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}

	public String getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}

	public String getBusiId() {
		return busiId;
	}

	public void setBusiId(String busiId) {
		this.busiId = busiId;
	}


	public String getBusiType() {
		return busiType;
	}

	public void setBusiType(String busiType) {
		this.busiType = busiType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public BigInteger getFileSize() {
		return fileSize;
	}

	public void setFileSize(BigInteger fileSize) {
		this.fileSize = fileSize;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getEncryptedFlag() {
		return encryptedFlag;
	}

	public void setEncryptedFlag(String encryptedFlag) {
		this.encryptedFlag = encryptedFlag;
	}
	
	public String getEncryptedType() {
		return encryptedType;
	}

	public void setEncryptedType(String encryptedType) {
		this.encryptedType = encryptedType;
	}

	public String getEncryptKey() {
		return encryptKey;
	}

	public void setEncryptKey(String encryptKey) {
		this.encryptKey = encryptKey;
	}

	public Integer getCompressedFlag() {
		return compressedFlag;
	}

	public void setCompressedFlag(Integer compressedFlag) {
		this.compressedFlag = compressedFlag;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getStrategyId() {
		return strategyId;
	}

	public void setStrategyId(String strategyId) {
		this.strategyId = strategyId;
	}

	public String getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}

	public Integer getAttachmentVersion() {
		return attachmentVersion;
	}

	public void setAttachmentVersion(Integer attachmentVersion) {
		this.attachmentVersion = attachmentVersion;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Object getAttachment() {
		return attachment;
	}

	public void setAttachment(Object attachment) {
		this.attachment = attachment;
	}
	public Integer getSaveState() {
		return saveState;
	}

	public void setSaveState(Integer saveState) {
		this.saveState = saveState;
	}

	public String getFileCode() {
		return fileCode;
	}

	public void setFileCode(String fileCode) {
		this.fileCode = fileCode;
	}

	public String getUploadMetadata() {
		return uploadMetadata;
	}

	public void setUploadMetadata(String uploadMetadata) {
		this.uploadMetadata = uploadMetadata;
	}

	public String getFileShare() {
		return fileShare;
	}

	public void setFileShare(String fileShare) {
		this.fileShare = fileShare;
	}
}