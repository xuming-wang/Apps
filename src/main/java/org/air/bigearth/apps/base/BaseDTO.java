package org.air.bigearth.apps.base;

import java.io.Serializable;


/**
 * DTO基础类，由于使用缓存，所有DTO需要实现序列化接口，因此系统的DTO统一继承BaseDTO
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-02-20
 */
public class BaseDTO implements Serializable {

	/**
     * 创建人
     */
    private String createUserId;

    private String createBy;

    /**
     * 创建人组织机构ID
     */
    private String createOrgId;

    /**
     * 数据版本，每更新一次，版本号加1
     */
    private Integer dbVersion;

    /**
     * 逻辑删除标识(1存在 2删除)
     */
    private String status;
    
    /**
     * 页面编辑属性,只做传递数据使用，不保存数据库
     */
    private String editType;

    private String createTime;

    /**
     * 最后更新时间
     */
    private String updateTime;
    
    /**
     * 业务数据所属机构OrgId(可选，非必填)
     */
    private String belongOrgId;
     
    /**
     *页面提示消息
    */
    private String tipInfo;

    private String remark;

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getCreateOrgId() {
		return createOrgId;
	}

	public void setCreateOrgId(String createOrgId) {
		this.createOrgId = createOrgId;
	}

	public Integer getDbVersion() {
		return dbVersion;
	}

	public void setDbVersion(Integer dbVersion) {
		this.dbVersion = dbVersion;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getEditType() {
		return editType;
	}

	public void setEditType(String editType) {
		this.editType = editType;
	}

	public String getBelongOrgId(){
		return belongOrgId;
	}
	
	public void setBelongOrgId(String belongOrgId){
		this.belongOrgId = belongOrgId;
	}
	
	public String getTipInfo() {
		return tipInfo;
	}

	public void setTipInfo(String tipInfo) {
		this.tipInfo = tipInfo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}