package org.air.bigearth.apps.base;

/**
 * 通用响应信息封装类
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-23
 */
public class BaseResult<T> {
    /**
     * 成功或者失败的code错误码
     */
    private Integer code;
    /**
     * 请求是否成功 1:处理成功, 0:处理失败
     */
    private String status;
    /**
     * 请求失败返回的提示信息，给前端进行页面展示的信息
     */
    private String msg;
    /**
     * 成功时返回的数据，失败时返回具体的异常信息
     */
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
