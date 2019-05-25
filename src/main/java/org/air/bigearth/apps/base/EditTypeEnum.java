package org.air.bigearth.apps.base;

/**
 * 操作类型枚举类
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-23
 */
public enum EditTypeEnum {

	/**
	 * 添加
	 */
	ADD("add"),
	
	/**
	 * 修改
	 */
	UPDATE("update"),

	/**
	 * 详情
	 */
	INFO("info");
	
	public String value;

	EditTypeEnum(String value) {
		this.value = value;
	}
}
