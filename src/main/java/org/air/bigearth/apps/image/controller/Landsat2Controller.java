package org.air.bigearth.apps.image.controller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.air.bigearth.apps.base.BaseResult;
import org.air.bigearth.apps.constant.ImageConstants;
import org.air.bigearth.apps.constant.RemoteFileConstants;
import org.air.bigearth.apps.image.domain.DataAccess;
import org.air.bigearth.apps.image.service.IDataAccessService;
import org.air.bigearth.apps.image.util.ImageHandleUtil;
import org.air.bigearth.apps.util.DataTransform;
import org.air.bigearth.apps.util.HttpToServer;
import org.air.bigearth.apps.util.ResultUtil;
import org.air.bigearth.apps.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
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
public class Landsat2Controller {
    private static final Logger logger = LoggerFactory.getLogger(Landsat2Controller.class);
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private IDataAccessService dataAccessService;

    /**
     * 根据时间、空间范围等参数查询polygon
     *
     * @param request
     * @return Map<String, Object> polygon数据结果集
     */
    @RequestMapping(value = "/query", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> query(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();

        String tableInfo = request.getParameter("tableInfo");
        String productId = request.getParameter("productId");
        String datatype = request.getParameter("datatype");
        String queryCriteria = request.getParameter("data");
        Map<String, Object> polygonParam = new HashMap<String, Object>();
        polygonParam.put("tableInfo", tableInfo);
        polygonParam.put("productId", productId);
        polygonParam.put("datatype", datatype);
        polygonParam.put("queryCriteria", queryCriteria);
        String polygonJson = polygonParam.toString();
        logger.info(polygonJson);

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

        JSONObject tableInfoObj = JSONObject.fromObject(tableInfo);
        Map<String, Object> requestMap = new HashMap<String, Object>();
        Map<String, Object> polygonMap = DataTransform.parseJsonObjectStrToMap(queryCriteria);
        if (StringUtil.isNotNullOrBlank(polygonMap)) {
            getParamValByMap(result, requestMap, polygonMap);
        }
        StringBuffer thumbnailPaths = new StringBuffer();
        callPolygonData(result, requestMap, thumbnailPaths, tableInfoObj);
        callThumbnailPicture(result, thumbnailPaths);

        result.put("success", true);
        subject.getSession().setAttribute(ImageConstants.POLYGON_DATA_SET, result);

        return result;
    }

    /**
     * 获取请求参数
     *
     * @param result     结果集参数
     * @param requestMap 后台请求的已转换的polygon数据的参数
     * @param polygonMap 前台传递的待处理polygon参数
     */
    public Map<String, Object> getParamValByMap(Map<String, Object> result, Map<String, Object> requestMap, Map<String, Object> polygonMap) {
        if (StringUtil.isNotNullOrBlank(ImageConstants.LANDSAT8)) {
            //requestMap.put("ds", ImageConstants.LANDSAT8);
        }
        if (StringUtil.isNotNullOrBlank(polygonMap.get("sdate"))) {
            requestMap.put("st", polygonMap.get("sdate").toString());
        }
        if (StringUtil.isNotNullOrBlank(polygonMap.get("edate"))) {
            requestMap.put("et", polygonMap.get("edate").toString());
        }
        StringBuffer polygon = new StringBuffer();
        if (StringUtil.isEmpty(polygonMap.get("polygon"))) {
            return result;
        }
        Map<String, Object> extentMap = DataTransform.parseJsonObjectStrToMap(polygonMap.get("polygon").toString());
        if (!extentMap.containsKey("geometry")) {
            return result;
        }
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
     * 拉取polygon响应结果集并获取缩略图路径、layers名称
     *
     * @param result         结果集参数
     * @param requestMap     请求参数
     * @param thumbnailPaths 缩略图路径集
     * @param tableInfoObj   分页信息
     * @return result        返回结果集
     */
    public Map<String, Object> callPolygonData(Map<String, Object> result, Map<String, Object> requestMap, StringBuffer thumbnailPaths, JSONObject tableInfoObj) {
        String polygonDataStr = null;
        String url = RemoteFileConstants.LANDSAT_DATA_URL + File.separator + ImageConstants.LANDSAT8;
        try {
            polygonDataStr = restTemplate.postForObject(url, requestMap, String.class);
        } catch (Exception e) {
            result.put("success", false);
            logger.error("polygon查询接口:", e);
        }

        // 偏移量
        int offset = tableInfoObj.getInt("offset");
        // 每页显示的条数
        int pageSize = tableInfoObj.getInt("pageSize");
        JSONObject polygonDataObj = JSONObject.fromObject(polygonDataStr);
        result.put("keyid", ImageConstants.KEYID);
        result.put("pageSize", pageSize);
        result.put("pid", ImageConstants.PID);
        result.put("offset", offset);
        result.put("total", polygonDataObj.get("total"));

        if (StringUtil.isEmpty(polygonDataObj.get("data"))) {
            result.put(ImageConstants.DATA, polygonDataObj.get("data"));
            return result;
        }
        StringBuffer layers = new StringBuffer();
        JSONArray polygonDataArr = JSONArray.fromObject(polygonDataObj.get("data"));
        JSONArray newPolygonDataArr = handlePolygonData(thumbnailPaths, polygonDataArr, offset, pageSize, layers);
        result.put(ImageConstants.LAYERS, layers.toString());
        result.put(ImageConstants.DATA, newPolygonDataArr);

        return result;
    }

    /**
     * 处理数据并获取待拉取缩略图路径
     *
     * @param thumbnailPaths 缩略图路径
     * @param polygonDataArr 数据集数据
     * @param offset         偏移量
     * @param pageSize       每页显示的条数
     * @return
     */
    public JSONArray handlePolygonData(StringBuffer thumbnailPaths, JSONArray polygonDataArr, int offset, int pageSize, StringBuffer layers) {
        // 起始条数
        int startNum = offset;
        // 当前页
        int currentPage = offset / pageSize + 1;
        // 终止条数
        int endNum = polygonDataArr.size() / pageSize >= currentPage ?  (currentPage * pageSize) - 1 : polygonDataArr.size() - 1;
        JSONArray newPolygonDataArr = new JSONArray();
        for (int i = startNum; polygonDataArr != null && i <= endNum; i++) {
            JSONObject polygonDataObj = getPolygonDataObj(polygonDataArr, i);
            ImageHandleUtil.getThumbnailPaths(thumbnailPaths, polygonDataArr, i, polygonDataObj, layers);
            newPolygonDataArr.add(polygonDataArr.getJSONObject(i));
        }

        return newPolygonDataArr;
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
     * 分页查询landsat数据
     * 请求参数格式:
     *      tableInfo: {"offset":40,"pageSize":10,"totalPage":24687,"totalSize":246867,"sortSet":[{"id":"datadate","sort":"desc"}],"filterSet":[]}
     *      data: {"datatype":"OLI_TIRS"}
     *      datatype: OLI_TIRS
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/query_dataset")
    @ResponseBody
    public Map<String, Object> query_dataset(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        String tableInfo = request.getParameter("tableInfo");
        String datatype = request.getParameter("datatype");
        String data = request.getParameter("data");

        JSONObject tableInfoObj = JSONObject.fromObject(tableInfo);
        // 偏移量
        int offset = tableInfoObj.getInt("offset");
        // 起始条数
        int startNum = offset + 1;
        // 每页显示的条数
        int pageSize = tableInfoObj.getInt("pageSize");

        String polygonDataStr = null;
        String url = RemoteFileConstants.PAGE_DATASET;
        Map<String, Object> params = new HashMap<>();
        params.put("index", ImageConstants.LANDSAT8);
        params.put("sn", String.valueOf(startNum));
        params.put("dn", String.valueOf(pageSize));
        params.put("field", "acquiredTime");
        params.put("sortOrder", "asc");
        try {
            polygonDataStr = restTemplate.postForObject(url, params, String.class);
        } catch (Exception e) {
            result.put("success", false);
            logger.error("数据集内数据查询分页接口:", e);
        }

        JSONObject polygonDataJson = JSONObject.fromObject(polygonDataStr);
        polygonDataJson.put("keyid", ImageConstants.KEYID);
        polygonDataJson.put("pageSize", pageSize);
        polygonDataJson.put("pid", ImageConstants.PID);
        polygonDataJson.put("offset", offset);
        polygonDataJson.put("total", polygonDataJson.get("total"));

        StringBuffer thumbnailPaths = new StringBuffer();
        JSONArray polygonDataArr = polygonDataJson.getJSONArray("data");
        handlePolygonData(thumbnailPaths, polygonDataArr);
        callThumbnailPicture(result, thumbnailPaths);

        return polygonDataJson;
    }


    /**
     * 处理数据并获取待拉取缩略图路径
     *
     * @param thumbnailPaths 缩略图路径
     * @param polygonDataArr 数据集数据
     */
    public void handlePolygonData(StringBuffer thumbnailPaths, JSONArray polygonDataArr) {
        for (int i = 0; polygonDataArr != null && i < polygonDataArr.size(); i++) {
            JSONObject polygonDataObj = getPolygonDataObj(polygonDataArr, i);
            ImageHandleUtil.getThumbnailPaths(thumbnailPaths, polygonDataArr, i, polygonDataObj, null);
        }
    }

    /**
     * 处理Polygon数据记录
     *
     * @param polygonDataArr 数据集
     * @param i 索引
     * @return
     */
    public JSONObject getPolygonDataObj(JSONArray polygonDataArr, int i) {
        JSONObject polygonDataObj = polygonDataArr.getJSONObject(i);
        if (StringUtil.isNotNullOrBlank(polygonDataObj.get("ulPoint")) && StringUtil.isNotNullOrBlank(polygonDataObj.get("lrPoint"))) {
            Double ulPointLongitude = ImageHandleUtil.getPointVal(polygonDataObj.getString("ulPoint"), 0);
            Double lrPointLongitude = ImageHandleUtil.getPointVal(polygonDataObj.getString("lrPoint"), 0);
            Double ct_long = (ulPointLongitude + lrPointLongitude) / 2;
            polygonDataObj.put("ct_long", ct_long);
        }
        if (StringUtil.isNotNullOrBlank(polygonDataObj.get("urPoint")) && StringUtil.isNotNullOrBlank(polygonDataObj.get("llPoint"))) {
            Double urPointLongitude = ImageHandleUtil.getPointVal(polygonDataObj.getString("urPoint"), 1);
            Double llPointLongitude = ImageHandleUtil.getPointVal(polygonDataObj.getString("llPoint"), 1);
            Double ct_lat = (urPointLongitude + llPointLongitude) / 2;
            polygonDataObj.put("ct_lat", ct_lat);
        }
        polygonDataObj.put("id", "");
        polygonDataObj.put("scale", "");
        polygonDataObj.put("dataexists", 0);
        polygonDataObj.put("dataid", polygonDataObj.getString("sceneId"));
        return polygonDataObj;
    }

    /**
     * 获取数据集
     *
     * @return
     */
    @GetMapping(value = "/datasets")
    @ResponseBody
    public BaseResult<String> datasets() {
        String url = RemoteFileConstants.DATASET;
        String datasetStr = HttpToServer.sendGetForHttp(url, null);
        return ResultUtil.success(datasetStr);
    }

}
