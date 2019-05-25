package org.air.bigearth.apps.file.util;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * scp远程拷贝工具类
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
public class ScpUtil {
    private static final Logger logger = LoggerFactory.getLogger(ScpUtil.class);

    public static void localFileScpToRemoteDir(String localDir, String dataServerDestDir) {
        System.out.println(PropertiesUtil.getProperties("config/scp.properties","server.ip"));
        //文件scp到数据服务器
        Connection conn = new Connection(PropertiesUtil.getProperties("config/scp.properties","server.ip"));
        logger.info("开始scp文件");
        try {
            conn.connect();
            boolean isAuthenticated = conn.authenticateWithPassword(PropertiesUtil.getProperties("config/scp.properties","server.username"), PropertiesUtil.getProperties("config/scp.properties","server.password"));
            if (isAuthenticated == false)
                throw new IOException("Authentication failed.文件scp到数据服务器时发生异常");
            SCPClient client = new SCPClient(conn);
            //本地文件scp到远程目录
            client.put(localDir, dataServerDestDir);
            //远程的文件scp到本地目录
///            client.get(dataServerDestDir + "00审计.zip", localDir);
            conn.close();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("文件scp到数据服务器时发生异常");
        }
        logger.info("scp文件结束");
    }

    public static void main(String[] args) {
        localFileScpToRemoteDir("/home/centos/Desktop/apps接口说明.txt", "/home/henry/Desktop");
    }
}
