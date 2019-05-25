package org.air.bigearth.apps.image.controller;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.air.bigearth.apps.constant.ImageConstants;
import org.air.bigearth.apps.constant.RemoteFileConstants;
import org.air.bigearth.apps.image.domain.DataAccess;
import org.air.bigearth.apps.image.service.IDataAccessService;
import org.air.bigearth.apps.image.util.ImageHandleUtil;
import org.air.bigearth.apps.util.DataTransform;
import org.air.bigearth.apps.util.HttpToServer;
import org.air.bigearth.apps.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.Map;


/**
 * 陆地卫星 控制层
 *
 * @author wangxuming
 * @version 1.0
 * @date 2018-12-05
 */
@Controller
@RequestMapping("/retrieve")
public class LandsatController {
    private static final Logger logger = LoggerFactory.getLogger(LandsatController.class);
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private IDataAccessService dataAccessService;

    /**
     * 根据条件查询polygon
     *
     * @param polygonJson JSON字符串参数集
     * @param request
     * @return Map<String, Object> polygon结果集
     */
    @PostMapping(value = "/query3")
    @ResponseBody
    public Map<String, Object> query(@RequestBody String polygonJson, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        logger.info(polygonJson);
        if (StringUtils.isEmpty(polygonJson)) {
            result.put("success", false);
            result.put("msg", "请求参数为空");
            return result;
        }

        Subject subject = SecurityUtils.getSubject();
        DataAccess polygonDataAccess = ImageHandleUtil.getDataAccess(subject);
        // 如果请求对象为null, 则发起请求；否则从session会话中获取数据，直接返回
        if (polygonDataAccess == null) {
            polygonDataAccess = ImageHandleUtil.setDataAccess(polygonJson, request);
            subject.getSession().setAttribute(ImageConstants.POLYGON_DATA_ACCESS, polygonDataAccess);
        } else {
            if (StringUtils.isNotEmpty(polygonDataAccess.getUrl()) && polygonDataAccess.getUrl().equals(String.valueOf(request.getRequestURL()))
                    && StringUtils.isNotEmpty(polygonDataAccess.getParam()) && polygonDataAccess.getParam().equals(polygonJson)) {
                result = (Map<String, Object>) subject.getSession().getAttribute(ImageConstants.POLYGON_DATA_SET);
                return result;
            }
        }

        // 起始页
        int pageNum = 0;
        // 每页显示的条数
        int pageSize = 0;
        Map<String, Object> requestMap = new HashMap<String, Object>();
        Map<String, Object> polygonMap = DataTransform.parseJsonObjectStrToMap(polygonJson);
        if (StringUtil.isNotNullOrBlank(polygonMap)) {
            getParamValByMap(result, requestMap, polygonMap);
            if (StringUtil.isNotNullOrBlank(polygonMap.get("pageNum"))) {
                pageNum = Integer.valueOf(polygonMap.get("pageNum").toString());
            }
            if (StringUtil.isNotNullOrBlank(polygonMap.get("pageSize"))) {
                pageSize = Integer.valueOf(polygonMap.get("pageSize").toString());
            }
        }

        StringBuffer thumbnailPaths = new StringBuffer();
        callPolygonData(result, requestMap, thumbnailPaths, pageNum, pageSize);
        callThumbnailPicture(result, thumbnailPaths);
        result.put("success", true);
        subject.getSession().setAttribute(ImageConstants.POLYGON_DATA_SET, result);

        return result;
    }

    /**
     * 获取请求参数polygon
     *
     * @param result     结果集参数
     * @param requestMap 后台请求的已转换的polygon数据的参数
     * @param polygonMap 前台传递的待处理polygon参数
     */
    public Map<String, Object> getParamValByMap(Map<String, Object> result, Map<String, Object> requestMap, Map<String, Object> polygonMap) {
        if (StringUtil.isNotNullOrBlank(ImageConstants.LANDSAT8)) {
            requestMap.put("ds", ImageConstants.LANDSAT8);
        }
        if (StringUtil.isNotNullOrBlank(polygonMap.get("sdate"))) {
            requestMap.put("st", polygonMap.get("sdate").toString());
        }
        if (StringUtil.isNotNullOrBlank(polygonMap.get("edate"))) {
            requestMap.put("et", polygonMap.get("edate").toString());
        }

        if (StringUtil.isEmpty(polygonMap.get("polygon"))) {
            return result;
        }
        Map<String, Object> extentMap = DataTransform.parseJsonObjectStrToMap(polygonMap.get("polygon").toString());
        if (!extentMap.containsKey("geometry")) {
            return result;
        }

        StringBuffer polygon = new StringBuffer();
        Map<String, Object>  geometryMap= DataTransform.parseJsonObjectStrToMap(extentMap.get("geometry").toString());
        if (StringUtil.isNotNullOrBlank(geometryMap.get("coordinates"))) {
            String coordinates = geometryMap.get("coordinates").toString();
            if (coordinates.contains("[")) {
                coordinates = coordinates.replaceAll("\\[", "");
            }
            if (coordinates.contains("]")) {
                coordinates = coordinates.replaceAll("\\]", "");
            }
            String[] coordinatesArray = coordinates.split(",");
            //polygon.append(coordinates).append(",").append(coordinatesArray[0]).append(",").append(coordinatesArray[1]);
            polygon.append(coordinates);
        }
        requestMap.put("polygon", polygon.toString());
        result.put("polygonStr", polygonMap.get("polygon"));

        return result;
    }


    /**
     * 拉取polygon请求结果集并获取缩略图路径、layers名称
     *
     * @param result         结果集参数
     * @param requestMap     请求参数
     * @param thumbnailPaths 缩略图路径集
     * @param pageNum        起始页数
     * @param pageSize       每页显示的条数
     * @return result
     * 返回结果集
     */
    public Map<String, Object> callPolygonData(Map<String, Object> result, Map<String, Object> requestMap, StringBuffer thumbnailPaths, int pageNum, int pageSize) {
        String polygonDataStr = null;
        String url = RemoteFileConstants.LANDSAT_DATA_URL + File.separator + ImageConstants.LANDSAT8;
        try {
            polygonDataStr = restTemplate.postForObject(url, requestMap, String.class);
        } catch (Exception e) {
            result.put("success", false);
            logger.error("polygon查询接口:", e);
        }

        JSONObject polygonDataObj = JSONObject.fromObject(polygonDataStr);
        result.put("code", polygonDataObj.get("code"));
        result.put("total", polygonDataObj.get("total"));

        if (StringUtil.isEmpty(polygonDataObj.get("data"))) {
            return result;
        }

        JSONArray polygonDataArr = JSONArray.fromObject(polygonDataObj.get("data"));
        JSONArray newPolygonDataArr = new JSONArray();
        int currentPage = 0;
        int polygonSize = 0;
        currentPage = pageNum == 0 ? currentPage : pageNum;
        polygonSize = pageSize == 0 ? polygonDataArr.size() : pageSize;
        StringBuffer layers = new StringBuffer();

        for (int i = currentPage; polygonDataArr != null && i < polygonSize; i++) {
            JSONObject polygonDataObject = polygonDataArr.getJSONObject(i);
            ImageHandleUtil.getThumbnailPaths(thumbnailPaths, polygonDataArr, i, polygonDataObject, layers);
        }
        result.put(ImageConstants.LAYERS, layers.toString());
        if (pageNum != 0 && pageSize != 0) {
            for (int j = pageNum; j <= pageSize; j++) {
                newPolygonDataArr.add(polygonDataArr.get(j));
            }
            result.put(ImageConstants.POLYGON_DATA, newPolygonDataArr);
        } else {
            result.put(ImageConstants.POLYGON_DATA, polygonDataArr);
        }

        return result;
    }



    /**
     * 拉取缩略图
     *
     * @param result
     * @param fileUrls 待拉取缩略图路径，多个以逗号分隔
     * @return 缩略图拉取信息
     */
    private String callThumbnailPicture(Map<String, Object> result, StringBuffer fileUrls) {
        if ("".equals(fileUrls.toString())) {
            return null;
        }
        // 获取缩略图请求路径
        String reqUrl = RemoteFileConstants.THUMBNAIL_URL;
        Map<String, Object> mapParam = new HashMap<String, Object>();
        mapParam.put("method", ImageConstants.START);
        mapParam.put("fileurl", fileUrls.toString());
        try {
            String thumbnailStr = restTemplate.postForObject(reqUrl, mapParam, String.class);
            logger.info(thumbnailStr);
            return thumbnailStr;
        } catch (Exception e) {
            result.put("success", false);
            logger.error("缩略图处理接口:", e);
        }

        return null;
    }

    /**
     * 数据查询接口
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/query2")
    @ResponseBody
    public Map<String, Object> query2(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        String index = request.getParameter("index");
        String name = request.getParameter("name");
        String key = request.getParameter("key");

        String url = RemoteFileConstants.LANDSAT_DATA_URL;
        String param = "index=" + index + "&name=" + name + "&key=" + key;
        try {
            String dataStr = HttpToServer.sendGetForHttp(url, param);
            result.put("data", dataStr);
            result.put("success", true);
        } catch (Exception e) {
            result.put("success", false);
            logger.error("请求失败", e);
        }

        return result;
    }

    /**
     * 元数据查询接口
     *
     * @param request
     * @param response
     * @return json格式字符串
     */
    @RequestMapping(value = "/mtl")
    @ResponseBody
    public Map<String, Object> metadata(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        String index = request.getParameter("index");
        String type = request.getParameter("type");
        String id = request.getParameter("id");

        String url = RemoteFileConstants.LANDSAT_DATA_URL;
        String param = "index=" + index + "&type=" + type + "&id=" + id;
        try {
            String metadataStr = HttpToServer.sendGetForHttp(url, param);
            result.put("metadata", metadataStr);
            result.put("success", true);
        } catch (Exception e) {
            result.put("success", false);
            logger.error("请求失败", e);
        }

        return result;
    }

    @RequestMapping(value = "/insert")
    @ResponseBody
    public Map<String, Object> insert(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();

        String index = request.getParameter("index");
        String type = request.getParameter("type");
        String id = request.getParameter("id");
        String json = request.getParameter("json");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("index", index);
        map.put("type", type);
        map.put("id", id);
        map.put("json", json);

        String url = RemoteFileConstants.CLUSTER_DATA_SOCKET + "/es/insert";
        try {
            String str = restTemplate.postForObject(url, map, String.class);
            map.put("str", str);
            map.put("success", true);
        } catch (Exception e) {
            result.put("success", false);
            logger.error("请求失败", e);
        }

        return map;
    }

}
