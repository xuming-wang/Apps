package org.air.bigearth.apps.exception.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * SpringBoot全局异常处理，捕获404异常
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-03-30
 */
@Controller
public class NotFoundException implements ErrorController {
    /**
     * 自己定义方法，想返回什么就返回什么，无论是 html 还是json
     *
     * @return
     */
    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping(value = {"/error"})
    @ResponseBody
    public Object error(HttpServletRequest request) {
        Map<String,Object> result = new HashMap<>();
        result.put("error", "not found");
        result.put("code", "404");
        return result;
    }
}
