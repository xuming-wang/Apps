package org.air.bigearth.apps.image.controller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.air.bigearth.apps.constant.ImageConstants;
import org.air.bigearth.apps.constant.LocalFileConstants;
import org.air.bigearth.apps.constant.RemoteFileConstants;
import org.air.bigearth.apps.image.domain.Composite;
import org.air.bigearth.apps.image.service.ICompositeService;
import org.air.bigearth.apps.system.domain.basic.User;
import org.air.bigearth.apps.system.service.IUserService;
import org.air.bigearth.apps.util.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 图像合成 控制层
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-02-21
 */
@Controller
public class CompositeController {
    private static final Logger logger = LoggerFactory.getLogger(CompositeController.class);

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private Composite composite;
    @Resource
    private ICompositeService compositeService;
    @Resource
    private IUserService userService;

    /**
     * 产品合成
     *
     * @param proJson
     *          JSON字符串参数集
     * @param request
     * @return
     */
    @RequestMapping(value = "/proComposite", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> proComposite(@RequestBody String proJson, HttpServletRequest request, HttpServletResponse response) {
        Map<String,Object> result = new HashMap<String, Object>();
        logger.info(proJson);

        if (StringUtils.isEmpty(proJson)) {
            result.put("success", false);
            result.put("message", "请求参数为空");
            return result;
        }

        // 从session中获取图片路径列表
        Map<String, Object> polygonDataSet = getPolygonDataSetMap();

        String polygonStr = null;
        StringBuffer imgUrls = new StringBuffer();
        if (StringUtil.isNotNullOrBlank(polygonDataSet)) {
            JSONArray polygonArray = (JSONArray)polygonDataSet.get(ImageConstants.POLYGON_DATA);
            polygonStr = (String)polygonDataSet.get("polygonStr");
            for (int i=0; polygonArray != null && i < polygonArray.size(); i++) {
                JSONObject polygonObj = polygonArray.getJSONObject(i);
                if (polygonObj.get("target") != null) {
                    String target = polygonObj.get("target").toString().trim();
                    target = target.substring(0, target.lastIndexOf("/"));
                    if (target.contains(RemoteFileConstants.HDFS_URL)) {
                        target = target.replaceAll(RemoteFileConstants.HDFS_URL, "");
                    }
                    if (i == polygonArray.size() - 1) {
                        imgUrls.append(target);
                    } else {
                        imgUrls.append(target).append(",");
                    }
                }
            }
        } else {
            result.put("success", false);
            result.put("message", "未选中数据");
            return result;
        }

        String[] images = null;
        if (imgUrls != null) {
            images = imgUrls.toString().split(",");
        }

        StringBuffer urlWithParam = new StringBuffer();
        String url = RemoteFileConstants.ALGORITHM_SOCKET + "/jobs?";
        urlWithParam.append(url)
            .append("sync=").append(composite.isSync())
            .append("&context=").append(composite.getContext())
            .append("&appName=").append(composite.getAppName())
            .append("&timeout=").append(composite.getTimeout())
            .append("&classPath=").append(composite.getClassPath());

        Map<String, Map<String, Object>> reqMap = new HashMap<String, Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        User user =(User)SecurityUtils.getSubject().getPrincipal();
        String hdfsUrl = RemoteFileConstants.HDFS_URL;
        String userId = user.getId().replaceAll("-", "");
        String outputPath = RemoteFileConstants.HDFS_DIR + File.separator + userId + File.separator + DateUtil.getDateFormat() + ".Tiff";
        JSONObject proMap = JSONObject.fromObject(proJson);
        map.put("bandsList", proMap.get("bandsList"));
        map.put("images", images);
        map.put("outputPath", outputPath);
        map.put("polygonStr", polygonStr);
        map.put("cellSize", proMap.getInt("cellSize"));
        map.put("selectMethod", proMap.get("selectMethod"));
        map.put("hdfsUrl", hdfsUrl);
        map.put("numPartitions", proMap.getInt("numPartitions"));
        map.put("tileCRS", "LatLng");
        reqMap.put("input", map);

        try {
            logger.info(JSONObject.fromObject(reqMap).toString());
            Composite composite = new Composite();
            composite.setDatakey(DataKeyUtil.getDataKey());
            composite.setUserId(userId);
            composite.setOutputPath(outputPath);
            composite.setPolygonStr(polygonStr);
            composite.setHdfsUrl(hdfsUrl);
            compositeService.insertDto(composite);

            boolean isStop = false;
            String tiffComposeJob = restTemplate.postForObject(urlWithParam.toString(), reqMap, String.class);
            if (StringUtil.isNotNullOrBlank(tiffComposeJob)) {
                JSONObject tiffComposeJobObj = JSONObject.fromObject(tiffComposeJob);
                result.put("result", tiffComposeJobObj);
                /*if ("FINISHED".equals(tiffComposeJobObj.get("status"))) {
                    result.put("result", tiffComposeJobObj);
                } else {
                    while (!isStop) {
                        result = getJobStatusByJobId(tiffComposeJobObj);
                        if ("FINISHED".equals(result.get("status")) || "ERROR".equals(result.get("status"))) {
                            isStop = true;
                        } else {
                            Thread.sleep(5000);
                        }
                    }
                }*/
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "请求失败");
            e.printStackTrace();
        }

        return result;
    }

    @RequestMapping(value = "download2")
    @ResponseBody
    private void downloadGenerateTiff(Composite composite, HttpServletRequest request, HttpServletResponse response) {

        User user = ShiroUtil.getSysUser();
        String fileDownloadPath = LocalFileConstants.LOCAL_SCP_DIR + "/user/" + user.getLoginName() + "/generate";
        FileUtils.createFileAndWritePermission(fileDownloadPath);
        String requestUrl = RemoteFileConstants.GENERATE_URL;
        String filePath = request.getParameter("filePath");
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        String param = "filePath=" + filePath + "&fileDownloadPath=" + fileDownloadPath;
        File file = new File(fileDownloadPath + File.separator + fileName);
        if (!file.exists()) {
            String isDownload = HttpToServer.sendGetForHttp(requestUrl, param);
        }
        response.setCharacterEncoding(request.getCharacterEncoding());
        response.setContentType("application/octet-stream");
        FileInputStream fis = null;
        try {
            File file2 = new File(fileDownloadPath + File.separator + fileName);
            fis = new FileInputStream(file2);
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            IOUtils.copy(fis, response.getOutputStream());
            response.flushBuffer();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 根据jobId获取任务执行结果
     *
     * @param
     * @return
     */
    @RequestMapping(value = "getJobStatusByJobId2")
    @ResponseBody
    public Map<String, Object> getJobStatusByJobId2(String jobId) {
        Map<String, Object> result = new HashMap<String, Object>();
        String jobUrl = RemoteFileConstants.ALGORITHM_SOCKET + "/jobs/" + jobId;
        String jobResponseStr = HttpToServer.sendGetForHttp(jobUrl, "");
        if (StringUtil.isNotNullOrBlank(jobResponseStr)) {
            JSONObject jobResponseObj = JSONObject.fromObject(jobResponseStr);
            result.put("result", jobResponseObj);
        }
        return result;
    }

    /**
     * 根据jobId获取任务执行结果
     *
     * @param tiffComposeJobObj
     * @return
     */
    public Map<String, Object> getJobStatusByJobId(JSONObject tiffComposeJobObj) {
        Map<String, Object> result = new HashMap<String, Object>();
        String jobUrl = RemoteFileConstants.ALGORITHM_SOCKET + "/jobs/" + tiffComposeJobObj.get("jobId").toString();
        String jobResponseStr = HttpToServer.sendGetForHttp(jobUrl, "");
        if (StringUtil.isNotNullOrBlank(jobResponseStr)) {
            JSONObject jobResponseObj = JSONObject.fromObject(jobResponseStr);
            if ("ERROR".equals(jobResponseObj.get("status"))) {
                result.put("success", false);
                result.put("status", jobResponseObj.get("status"));
                result.put("result", jobResponseObj);
                return result;
            }else if("FINISHED".equals(jobResponseObj.get("status"))) {
                result.put("success", true);
                result.put("status", jobResponseObj.get("status"));
                result.put("result", jobResponseObj);
            }
        }
        return result;
    }

    /**
     * TMS服务转发
     *
     * @param //tmsJson
     *          JSON字符串参数集
     * @param path
     * @param x
     * @param y
     * @param z
     *          路径参数
     * @return  二进制流图片
     *
     */
    @RequestMapping(value = "/tmsServer/{path}/{z}/{x}/{y}", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    @CrossOrigin
    @ResponseBody
    public byte[] tmsServer(@RequestParam ("mask") String mask,
                                       @PathVariable(value="path") String path,
                                       @PathVariable(value="z") String z,
                                       @PathVariable(value="x") String x,
                                       @PathVariable(value="y") String y) {
        Map<String, Object> polygonDataSet = getPolygonDataSetMap();

        String layers = null;
        if (StringUtil.isNotNullOrBlank(polygonDataSet)) {
            layers = (String)polygonDataSet.get(ImageConstants.LAYERS);
        } else {
            layers = "LC08_L1TP_122032_20140812,LC08_L1TP_123032_20180102_20180104_01_T1";
        }

        String url = RemoteFileConstants.ALGORITHM_TMS_SOCKET + File.separator + path + File.separator + z + File.separator + x + File.separator + y;
        String param = "layers=" + layers + "&mask=" + mask;

        byte streamImgStr[] = HttpToServer.sendGetForHttp(url, param, null);
        return streamImgStr;
    }

    public Map<String, Object> getPolygonDataSetMap() {
        Subject subject = SecurityUtils.getSubject();
        return (Map<String, Object>) subject.getSession().getAttribute(ImageConstants.POLYGON_DATA_SET);
    }

}
