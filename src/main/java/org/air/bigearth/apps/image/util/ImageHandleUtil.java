package org.air.bigearth.apps.image.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.air.bigearth.apps.constant.ImageConstants;
import org.air.bigearth.apps.constant.LocalFileConstants;
import org.air.bigearth.apps.image.domain.DataAccess;
import org.air.bigearth.apps.util.DataKeyUtil;
import org.air.bigearth.apps.util.DateUtil;
import org.air.bigearth.apps.util.FileUtils;
import org.air.bigearth.apps.util.StringUtil;
import org.apache.shiro.subject.Subject;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * 图像处理工具类
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-22
 */
public class ImageHandleUtil {


    /**
     * 为数据请求记录赋值
     *
     * @param str     字符串参数集
     * @param request
     * @return DataAccess对象
     */
    public static DataAccess setDataAccess(String str, HttpServletRequest request) {
        DataAccess dataAccess = new DataAccess();
        dataAccess.setId(DataKeyUtil.getDataKey());
        dataAccess.setUrl(String.valueOf(request.getRequestURL()));
        dataAccess.setParam(str);
        dataAccess.setCreateTime(DateUtil.getCurrentTime());
        return dataAccess;
    }

    /**
     * JSON特殊字符处理
     *
     * @param polygonJson 待处理的JSON字符串
     * @return
     */
    public static String specialCharacterProcessing(String polygonJson) {
        polygonJson = polygonJson.startsWith("\"") ? polygonJson.substring(1) : polygonJson;
        polygonJson = polygonJson.endsWith("\"") ? polygonJson.substring(0, polygonJson.length() - 1) : polygonJson;
        polygonJson = polygonJson.contains("\\") ? polygonJson.replaceAll("\\\\", "") : polygonJson;
        return polygonJson;
    }

    /**
     * @param str   待处理的字符串
     * @param index 索引
     * @return
     */
    public static Double getPointVal(String str, int index) {
        str = str.replace("[", "").replace("]", "");
        str = str.split(",")[index];
        return Double.parseDouble(str);
    }

    /**
     * 获取数据访问记录
     *
     * @param subject subject对象
     * @return
     */
    public static DataAccess getDataAccess(Subject subject) {
        return (DataAccess) subject.getSession().getAttribute(ImageConstants.POLYGON_DATA_ACCESS);
    }

    /**
     * 获取缩略图路径
     *
     * @param thumbnailPaths 缩略图路径
     * @param dataArr        数据集
     * @param i              索引
     * @param dataObj        数据记录
     * @return
     */
    public static boolean getThumbnailPaths(StringBuffer thumbnailPaths, JSONArray dataArr, int i, JSONObject dataObj, StringBuffer layers) {
        if (StringUtil.isNotNullOrBlank(dataObj.get("filePath"))) {
            String filePath = dataObj.getString("filePath").trim();
            String newLocation = LocalFileConstants.LOCAL_SCP_DIR + filePath.replace("_MTL.txt", ".jpg");
            String absolutelyPath = LocalFileConstants.APP_SOCKET + filePath.replace("_MTL.txt", ".jpg");
            String defaultPath = LocalFileConstants.APP_SOCKET + "/scale.jpeg";
            if (layers != null) {
                String layersStr = filePath.substring(0, filePath.lastIndexOf("/"));
                layersStr = layersStr.substring(layersStr.lastIndexOf("/") + 1);
                if (i == dataArr.size() - 1) {
                    layers.append(layersStr);
                } else {
                    layers.append(layersStr).append(",");
                }
            }
            File file = new File(newLocation);
            // 存在则直接返回，不存在就创建路径，并根据缩略图路径到服务器拉取
            if (file.exists()) {
                dataObj.put("filePath", absolutelyPath);
                return true;
            }

            if (i == dataArr.size() - 1) {
                thumbnailPaths.append(newLocation);
            } else {
                thumbnailPaths.append(newLocation).append(",");
            }

            String generatefilePath = LocalFileConstants.LOCAL_SCP_DIR + filePath.substring(0, filePath.lastIndexOf("/"));
            FileUtils.createFileAndWritePermission(generatefilePath);
            if (!file.exists()) {
                dataObj.put("filePath", defaultPath);
                return true;
            }
            dataObj.put("filePath", absolutelyPath);
        }

        return false;
    }


}
