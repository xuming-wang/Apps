package org.air.bigearth.apps.image.domain;

import org.air.bigearth.apps.base.BaseDTO;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 图像合成表 apps_t_composite
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-23
 */
@Component
@PropertySource("classpath:config/tiff.properties")
@ConfigurationProperties(prefix = "tiff")
public class Composite extends BaseDTO {
    private String datakey;
    private boolean sync;
    private String context;
    private String appName;
    private String timeout;
    private String classPath;
    private String images;
    private String outputPath;
    private String polygonStr;
    private double cellSize;
    private String selectMethod;
    private String hdfsUrl;
    private int numPartitions;
    private int bandsList;
    private String userId;

    public String getDatakey() {
        return datakey;
    }

    public void setDatakey(String datakey) {
        this.datakey = datakey;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getPolygonStr() {
        return polygonStr;
    }

    public void setPolygonStr(String polygonStr) {
        this.polygonStr = polygonStr;
    }

    public double getCellSize() {
        return cellSize;
    }

    public void setCellSize(double cellSize) {
        this.cellSize = cellSize;
    }

    public String getSelectMethod() {
        return selectMethod;
    }

    public void setSelectMethod(String selectMethod) {
        this.selectMethod = selectMethod;
    }

    public String getHdfsUrl() {
        return hdfsUrl;
    }

    public void setHdfsUrl(String hdfsUrl) {
        this.hdfsUrl = hdfsUrl;
    }

    public int getNumPartitions() {
        return numPartitions;
    }

    public void setNumPartitions(int numPartitions) {
        this.numPartitions = numPartitions;
    }

    public int getBandsList() {
        return bandsList;
    }

    public void setBandsList(int bandsList) {
        this.bandsList = bandsList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
