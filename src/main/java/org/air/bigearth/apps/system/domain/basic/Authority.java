package org.air.bigearth.apps.system.domain.basic;

/**
 * 权限表 apps_t_sys_authority
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
public class Authority {
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 操作码
     */
    private String code;
    /**
     * url
     */
    private String url;
    /**
     * 请求方式
     */
    private String method;
    /**
     * controller
     */
    private String controller;
    /**
     * 描述
     */
    private String des;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}