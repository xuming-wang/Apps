package org.air.bigearth.apps.image.controller;

import org.air.bigearth.apps.exception.service.BaseException;
import org.air.bigearth.apps.file.domain.Attachment;
import org.air.bigearth.apps.file.service.IAttachmentService;
import org.air.bigearth.apps.image.domain.Composite;
import org.air.bigearth.apps.image.service.ICompositeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 页面跳转 控制层
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-23
 */
@Controller
@RequestMapping
public class PageController {

    @Resource
    private IAttachmentService attachmentService;
    @Resource
    private ICompositeService compositeService;

    @RequestMapping(value = "testindex")
    public String testIndex() {
        return "testindex";
    }

    @RequestMapping(value = "webUploader2")
    public String webUploader2() {
        return "webUploader2";
    }

    @RequestMapping(value = "fileDownload2")
    public String fileDownload2(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        try {
            //PageHelper.startPage(1, 2);
            // 添加后台分页
            List<Composite> listAttachments = compositeService.searchByParamsPage(params);
            //PageInfo<Composite> pageInfo = new PageInfo<Composite>(listAttachments);
            request.setAttribute("listComposites", listAttachments);
        } catch (BaseException e) {
            e.printStackTrace();
        }
        return "fileDownload2";
    }

    @RequestMapping(value = "fileDownload")
    public String fileDownload(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        try {
            List<Attachment> listAttachments = attachmentService.searchByParamsPage(params);
            request.setAttribute("listAttachments", listAttachments);
        } catch (BaseException e) {
            e.printStackTrace();
        }
        return "fileDownload";
    }

    @RequestMapping(value = "toRegister")
    public String register() {
        return "register";
    }

    @RequestMapping(value = "user_save")
    public String user_save() {
        return "user_save";
    }

    @RequestMapping(value = "index2")
    public String index() {
        return "index2";
    }
}
