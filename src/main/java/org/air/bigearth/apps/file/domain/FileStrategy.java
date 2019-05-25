package org.air.bigearth.apps.file.domain;


import org.air.bigearth.apps.base.BaseDTO;

/**
 * 文件策略表 apps_t_sys_filestrategy
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-17
 */
public class FileStrategy extends BaseDTO {
	private static final long serialVersionUID = 1L;

	/**
     * 策略ID
     */
	
    private String strategyId;
    
	private String stateName;

    /**
     * 策略编码
     */
    private String strategyCode;

    /**
     * 策略名称
     */
    private String strategyName;

    /**
     * 上传根路径
     */
    private String basePath;

    /**
     * 文件传输类型(方式)
     */
    private String transportType;

    /**
     * 文件传输目标主机地址
     */
    private String transportHost;

    /**
     * 文件传输端口号
     */
    private String transportPort;

    /**
     * 文件传输账号
     */
    private String transportAccount;

    /**
     * 文件传输密码
     */
    private String transportPassword;

    /**
     * 文件大小限制(单位：M)
     */
    private Integer filesizeLimit;

    /**
     * 允许文件上传类型
     */
    private String uploadfileType;

    /**
     * 禁止文件上传类型
     */
    private String forbidfileType;

    /**
     * 加密方式
     */
    private String encryptType;

    /**
     * 是否压缩(1压缩 2不压缩)
     */
    private String compressedFlag;

    /**
     * 文件存放路径规则
     */
    private String filepathRule;


    
    /**
     * 状态(1启用 2停用)
     */
    private String state;
    /**
     * 最大上传文件个数
     */
    private Integer maxUploadFileNumber;
    /**
     * 是否多文件上传
     */
    private Integer isMultiFileUpload;
    
	public Integer getIsMultiFileUpload() {
		return isMultiFileUpload;
	}

	public void setIsMultiFileUpload(Integer isMultiFileUpload) {
		this.isMultiFileUpload = isMultiFileUpload;
	}

	public Integer getMaxUploadFileNumber() {
		return maxUploadFileNumber;
	}

	public void setMaxUploadFileNumber(Integer maxUploadFileNumber) {
		this.maxUploadFileNumber = maxUploadFileNumber;
	}


	public String getStrategyId() {
		return strategyId;
	}

	public void setStrategyId(String strategyId) {
		this.strategyId = strategyId;
	}

	public String getStrategyCode() {
		return strategyCode;
	}

	public void setStrategyCode(String strategyCode) {
		this.strategyCode = strategyCode;
	}

	public String getStrategyName() {
		return strategyName;
	}

	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public String getTransportType() {
		return transportType;
	}

	public void setTransportType(String transportType) {
		this.transportType = transportType;
	}

	public String getTransportHost() {
		return transportHost;
	}

	public void setTransportHost(String transportHost) {
		this.transportHost = transportHost;
	}

	public String getTransportPort() {
		return transportPort;
	}

	public void setTransportPort(String transportPort) {
		this.transportPort = transportPort;
	}

	public String getTransportAccount() {
		return transportAccount;
	}

	public void setTransportAccount(String transportAccount) {
		this.transportAccount = transportAccount;
	}

	public String getTransportPassword() {
		return transportPassword;
	}

	public void setTransportPassword(String transportPassword) {
		this.transportPassword = transportPassword;
	}

	public Integer getFilesizeLimit() {
		return filesizeLimit;
	}

	public void setFilesizeLimit(Integer filesizeLimit) {
		this.filesizeLimit = filesizeLimit;
	}

	public String getUploadfileType() {
		return uploadfileType;
	}

	public void setUploadfileType(String uploadfileType) {
		this.uploadfileType = uploadfileType;
	}

	public String getForbidfileType() {
		return forbidfileType;
	}

	public void setForbidfileType(String forbidfileType) {
		this.forbidfileType = forbidfileType;
	}

	public String getEncryptType() {
		return encryptType;
	}

	public void setEncryptType(String encryptType) {
		this.encryptType = encryptType;
	}

	public String getCompressedFlag() {
		return compressedFlag;
	}

	public void setCompressedFlag(String compressedFlag) {
		this.compressedFlag = compressedFlag;
	}

	public String getFilepathRule() {
		return filepathRule;
	}

	public void setFilepathRule(String filepathRule) {
		this.filepathRule = filepathRule;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

    
}