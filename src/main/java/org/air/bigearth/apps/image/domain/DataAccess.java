package org.air.bigearth.apps.image.domain;


import org.air.bigearth.apps.base.BaseDTO;

/**
 * 数据访问表 apps_t_data_access
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-01-24
 */
public class DataAccess extends BaseDTO {

    /**
     * 主键
     */
    private String id;
    /**
     * 请求链接
     */
    private String url;
    /**
     * 请求参数
     *      格式：name1=value1&name2=value2
     */
    private String param;
    /**
     *
     */
    private String pattern;
    /**
     * 数据集
     */
    private String dataSet;
    /**
     * 请求结果
     */
    private String resultSet;
    /**
     * 创建者
     */
    private String creator;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 状态
     */
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public String getResultSet() {
        return resultSet;
    }

    public void setResultSet(String resultSet) {
        this.resultSet = resultSet;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "DataAccessDTO{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", param='" + param + '\'' +
                ", pattern='" + pattern + '\'' +
                ", dataSet='" + dataSet + '\'' +
                ", resultSet='" + resultSet + '\'' +
                ", creator='" + creator + '\'' +
                ", createTime='" + createTime + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
