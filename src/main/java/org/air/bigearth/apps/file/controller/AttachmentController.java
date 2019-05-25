

package org.air.bigearth.apps.file.controller;

import com.alibaba.fastjson.JSON;
import net.lingala.zip4j.io.ZipOutputStream;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.air.bigearth.apps.constant.*;
import org.air.bigearth.apps.exception.service.BaseException;
import org.air.bigearth.apps.file.domain.Attachment;
import org.air.bigearth.apps.file.domain.FileStrategy;
import org.air.bigearth.apps.file.domain.MultipartFileParam;
import org.air.bigearth.apps.file.service.IAttachmentService;
import org.air.bigearth.apps.file.service.IFileStrategyService;
import org.air.bigearth.apps.file.util.AttachmentUtil;
import org.air.bigearth.apps.file.vo.ResultStatus;
import org.air.bigearth.apps.file.vo.ResultVo;
import org.air.bigearth.apps.system.domain.basic.User;
import org.air.bigearth.apps.util.*;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;


/**
 * 附件 控制层
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-02-27
 */
@Controller
@RequestMapping(value = "/file/")
public class AttachmentController {
    private static final Logger logger = LoggerFactory.getLogger(AttachmentController.class);
    private final String PREFIX = "/file";
    private final String FILE_UPLOAD_PROGRESS = "FileUploadProgress";

    @Resource
    private IAttachmentService attachmentService;
    @Resource
    private IFileStrategyService fileStrategysService;

    /**
     * 秒传判断，断点判断
     *
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "checkFile", method = RequestMethod.POST)
    @ResponseBody
    public ResultVo checkFile(HttpServletRequest request) throws IOException {
        //Map<String, String> requestParams = DataTransform.requestToMap(request);
        //String md5 = requestParams.get("md5");
        //String fileName = requestParams.get("fileName");
        //FileStrategy fileStrategy = fileStrategysService.getByCode(FileStrategyConstants.DEFAULT);
        ////JSONObject fileStrategyObj = JSONObject.fromObject(fileStrategy);
        ////requestParams.put("fileStrategyStr", fileStrategyObj.toString());
        //// 构造临时路径来存储上传的文件
        //File targetPath = AttachmentUtil.getTargetFilePath(fileStrategy.getBasePath(), fileStrategy.getFilepathRule(), md5);
        //String uploadPath = targetPath != null ? targetPath.toString() : fileStrategy.getBasePath();
        ////ResultVo resultVo = uploadValidation(requestParams);
        ////if (resultVo != null) {
        ////    return resultVo;
        ////}
        ///**
        // * 判断文件（数据服务器）是否存在。
        // *   1.不存在，判断本地是否创建过临时文件。
        // *     1.1 若未创建，则直接进行上传。
        // *     1.2 若已创建，则进行断点续传。
        // *   2.存在，判断当前用户是否上传过该文件。
        // *     2.1 若上传，更新数据库。
        // *     2.2 若未上传，插入数据库。
        // */
        //String fileIsExistStr = AttachmentUtil.checkFileIsExistByFileMd5(md5);
        //JSONObject fileMd5Obj = JSONObject.fromObject(fileIsExistStr);
        //// 判断文件（数据服务器）是否存在。
        //if ("0".equals(fileMd5Obj.get("code"))) {
        //    // 1.不存在，判断本地是否创建过临时文件。
        //    String tmpPath = uploadPath + File.separator + fileName + "_tmp";
        //    File tmpFile = new File(tmpPath);
        //    if (tmpFile.exists()) {
        //        String confPath = uploadPath + File.separator + fileName + ".conf";
        //        File confFile = new File(confPath);
        //        byte[] completeList = org.apache.commons.io.FileUtils.readFileToByteArray(confFile);
        //        List<String> missChunkList = new LinkedList<>();
        //        for (int i = 0; i < completeList.length; i++) {
        //            if (completeList[i] != Byte.MAX_VALUE) {
        //                missChunkList.add(i + "");
        //            }
        //        }
        //        return new ResultVo<>(ResultStatus.ING_HAVE, missChunkList);
        //    }else {
        //        return new ResultVo(ResultStatus.NO_HAVE);
        //    }
        //} else if ("1".equals(fileMd5Obj.get("code"))) {
        //    // 2.存在，判断当前用户是否上传过该文件。
        //    requestParams.put("fileIsExistStr", fileIsExistStr);
        //    attachmentService.saveAttachment(requestParams);
        //    return new ResultVo(ResultStatus.IS_HAVE);
        //}

        return new ResultVo(ResultStatus.NO_HAVE);
    }

    /**
     * 上传验证
     *
     * @param requestParams
     * @return
     */
    public Map<String, Object> uploadValidation(Map<String, String> requestParams) {
        Map<String, Object> result = new HashMap<String, Object>();
        ResultVo resultVo = null;
        String originalFileName = requestParams.get("name");
        String fileSuffix = AttachmentUtil.getFileSuffix(originalFileName);
        String fileSize = requestParams.get("fileSize");
        String fileStrategyStr = requestParams.get("fileStrategyStr");
        JSONObject fileStrategyObj = JSONObject.fromObject(fileStrategyStr);
        FileStrategy fileStrategy=(FileStrategy)JSONObject.toBean(fileStrategyObj, FileStrategy.class);
        if (StringUtil.isEmpty(originalFileName)) {
            result.put("success", false);
            result.put("msg", "请选择文件！");
            return result;
        }
        // 文件允许类型限制
        String uploadfileType = fileStrategy.getUploadfileType();
        if (StringUtil.isNotNullOrBlank(uploadfileType) && !uploadfileType.equals("*")) {
            if (!uploadfileType.contains(fileSuffix)) {
                result.put("success", false);
                result.put("msg", "上传的文件格式不正确！");
                return result;
            }
        }
        //不允许上传的文件类型限制
        String forbidFileType = fileStrategy.getForbidfileType();
        if (StringUtil.isNotNullOrBlank(forbidFileType)) {
            if (StringUtil.isNotNullOrBlank(fileSuffix) && forbidFileType.contains(fileSuffix)) {
                result.put("success", false);
                result.put("msg", "上传的文件格式为禁止上传类型！");
                return result;
            }
        }
        // 大小限制,单位M
        long lfileSize = Long.valueOf(fileSize).longValue();
        long fileSizeLimit = fileStrategy.getFilesizeLimit() * 1024 * 1024;
        if (fileSizeLimit > 0) {
            if (lfileSize > fileSizeLimit) {
                result.put("success", false);
                result.put("msg", "上传的单个文件大小不能超过！");
                return result;
            }
        }
        // 加入文件名单引号限制
        if (originalFileName.indexOf("'") != -1) {
            result.put("success", false);
            result.put("msg", "上传的文件名称中不能包含单引号！");
            return result;
        }
        return result;
    }



    /**
     * 文件上传
     *
     * @param multipartFileParam 大文件分片上传参数
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "fileUpload", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> fileUpload(MultipartFileParam multipartFileParam, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart) {
            logger.info("上传文件start。");
            try {
                // 方法1
                //attachmentService.uploadFileRandomAccessFile(param);
                //Map<String, String> requestParams = DataTransform.requestToMap(request);
                //FileStrategy fileStrategy = fileStrategysService.getByCode(FileStrategyConstants.DEFAULT);
                //JSONObject fileStrategyObj = JSONObject.fromObject(fileStrategy);
                //requestParams.put("fileStrategyStr", fileStrategyObj.toString());
                //result = uploadValidation(requestParams);
                //if (result.get("success") != null) {
                //    return result;
                //}
                // 方法2 更快
                result = attachmentService.uploadFileByMappedByteBuffer(multipartFileParam, request);
            } catch (IOException e) {
                logger.error("文件上传失败。" + multipartFileParam.toString(), e);
            }
            logger.info("上传文件end。");
        }
        result.put("success", true);
        result.put("msg", "上传成功");

        return result;
    }

    /**
     * 文件上传2
     *
     * @param request
     * @param response
     * @param files    上传一个或多个文件
     */
    @RequestMapping(value = "uploadAttachment2")
    @ResponseBody
    public Map<String, Object> uploadAttachment(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, value = "file") MultipartFile[] files) {
        Map<String, Object> result = new HashMap<String, Object>();
        PrintWriter out = null;
        Map paramMap = FileUtils.getFilePath2(request);
        System.out.println(request.getParameter("testColumn"));
        request.setAttribute("paramMap", paramMap);
        try {
            result = attachmentService.uploadAttachment(request, files);
            response.setContentType("text/html;charset=utf-8");
            out = response.getWriter();
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("msg", e.getMessage());
        }
        out.write(JSON.toJSONString(result));
        out.close();
        return result;
    }

    /**
     * 删除附件
     *
     * @param request
     * @param response
     * @return Map<String   ,   Object> (返回类型)
     */
    @RequestMapping(value = "deleteAttachment")
    @ResponseBody
    public Map<String, Object> deleteAttachment(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        String attachmentId = request.getParameter("attachmentId");
        try {
            result.put("attachmentId", attachmentId);
            attachmentService.deleteAttachment(attachmentId);
            result.put("success", true);
            result.put("msg", "删除成功！");
        } catch (Exception e) {
            result.put("success", false);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/fileDownload")
    public String fileDownload(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        try {
            List<Attachment> listAttachments = attachmentService.searchByParamsPage(params);
            request.setAttribute("listAttachments", listAttachments);
        } catch (BaseException e) {
            e.printStackTrace();
        }
        return "file/fileDownload";
    }

    @RequestMapping("/download")
    @ResponseBody
    public Map<String, Object> downloadFile(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        String attachmentId = request.getParameter("attachmentId");
        String fileName = request.getParameter("fileName");
        // 获取文件路径
        String requestUrl = RemoteFileConstants.DOWNLOAD_URL;
        User user = ShiroUtil.getSysUser();
        String fileDownloadPath = LocalFileConstants.LOCAL_SCP_DIR + "/user/" + user.getLoginName() + "/download/" + DateUtil.getDateFormat();
        FileUtils.createFileAndWritePermission(fileDownloadPath);
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("attachmentId", attachmentId);
        jsonObj.put("fileDownloadPath", fileDownloadPath);
        String str = HttpClientUtil.doPost(requestUrl, jsonObj.toString());
        response.setCharacterEncoding(request.getCharacterEncoding());
        response.setContentType("application/octet-stream");
        FileInputStream fis = null;
        try {
            File file = new File(fileDownloadPath + File.separator + fileName);
            fis = new FileInputStream(file);
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            IOUtils.copy(fis, response.getOutputStream());
            response.flushBuffer();
            result.put("success", true);
            result.put("msg", "下载成功");
        } catch (FileNotFoundException e) {
            result.put("success", false);
            result.put("msg", "下载失败");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

}
