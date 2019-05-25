package org.air.bigearth.apps.util;

import org.air.bigearth.apps.base.BaseResult;

/**
 * 响应结果工具类
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-23
 */
public class ResultUtil {
    public static <T> BaseResult<T> success() {
        return commonResult("1", 200, "操作成功");
    }

    public static <T> BaseResult<T> success(String successMsg) {
        return commonResult("1", 200, successMsg);
    }

    public static <T> BaseResult<T> success(T data) {
        return commonResult("1", 200, "成功", data);
    }

    public static <T> BaseResult<T> success(String successMsg, T data) {
        return commonResult("1", 200, successMsg, data);
    }

    public static <T> BaseResult<T> error() {
        return error(500, "操作失败");
    }

    public static <T> BaseResult<T> error(String errorMsg) {
        return error(500, errorMsg);
    }

    public static <T> BaseResult<T> error(Integer code, String errorMsg) {
        return commonResult("0", code, errorMsg, null);
    }

    private static <T> BaseResult<T> commonResult(String status, Integer code, String msg, T data) {
        BaseResult<T> result = new BaseResult<>();
        result.setStatus(status);
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    private static <T> BaseResult<T> commonResult(String status, Integer code, String msg) {
        BaseResult<T> result = new BaseResult<>();
        result.setStatus(status);
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

}
