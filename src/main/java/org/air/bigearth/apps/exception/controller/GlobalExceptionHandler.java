package org.air.bigearth.apps.exception.controller;

import org.air.bigearth.apps.base.BaseResult;
import org.air.bigearth.apps.exception.service.BaseException;
import org.air.bigearth.apps.exception.service.DataValidateFiledException;
import org.air.bigearth.apps.util.ResultUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * SpringBoot全局异常处理，@ControllerAdvice默认只会处理Controller层抛出的异常
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-23
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 所有异常报错 @ExceptionHandler指定处理哪种异常（可指定多个）
     * @param //request
     * @param //exception
     * @return
     * @throws Exception
     */
   /* @ExceptionHandler(value=DataValidateFiledException.class)
    public BaseResult<String> allExceptionHandler(HttpServletRequest request,
    		DataValidateFiledException exception) throws Exception {
    	
    	String msg = exception.toString();
    	return ResultUtil.error(500, e.getMessage() == null ? "服务器内部错误" : e.getMessage());
    	
    }*/

    @ExceptionHandler(value = {Exception.class, DataValidateFiledException.class})
    @ResponseBody
    public <T> BaseResult<T> handle(Exception e) {
        if (e instanceof BaseException) {
            // 104 为 前端和后端约定好的返回码
            Integer code = 104;
            BaseException exception = (BaseException) e;
            if (exception.getCode() != 0) {
                code = exception.getCode();
            }
            return ResultUtil.error(code, e.getMessage());
        }
        return ResultUtil.error(500, e.getMessage() == null ? "服务器内部错误" : e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseEntity handle(BaseException e){
        //自己需要实现的异常处理
        return null;
    }

}