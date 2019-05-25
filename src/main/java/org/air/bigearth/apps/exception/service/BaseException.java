package org.air.bigearth.apps.exception.service;

/**
 * 自定义BaseException继承RuntimeException类，处理service层的异常
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-23
 */
public class BaseException extends RuntimeException {

	/**
	 * 序列化属性
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 错误信息
	 */
	private String errorMsg;
	/**
	 * 服务器状态码
	 */
	private Integer code;

	public BaseException(String errorMsg) {
		super(errorMsg);
		this.errorMsg = errorMsg;
	}

	public BaseException(Integer code, String errorMsg) {
		super(errorMsg);
		this.errorMsg = errorMsg;
		this.code = code;
	}


	public String getErrorMsg() {
		return errorMsg;
	}

	public Integer getCode() {
		return code;
	}


}
