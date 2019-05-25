package org.air.bigearth.apps.file.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * scp远程拷贝文件属性封装
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
@Component
@PropertySource("classpath:config/scp.properties")
@ConfigurationProperties(prefix = "server")
public class ScpProperties {

    private String serverIp;
    private String serverUsername;
    private String serverPassword;

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getServerUsername() {
        return serverUsername;
    }

    public void setServerUsername(String serverUsername) {
        this.serverUsername = serverUsername;
    }

    public String getServerPassword() {
        return serverPassword;
    }

    public void setServerPassword(String serverPassword) {
        this.serverPassword = serverPassword;
    }
}
