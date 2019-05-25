package org.air.bigearth.apps.file.util;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 资源文件工具类
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
public class PropertiesUtil {

    private static Properties p = new Properties();


    /**
     * 根据key读取value
     *
     * @param filePath
     * @param keyWord
     * @return
     */
    public static String getProperties(String filePath, String keyWord) {
        Properties prop = null;
        String value = null;
        try {
            // 通过Spring中的PropertiesLoaderUtils工具类进行获取
            prop = PropertiesLoaderUtils.loadAllProperties(filePath);
            // 根据关键字查询相应的值
            value = prop.getProperty(keyWord);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return value;
    }

    public static Properties getProperties(String fileName) {
        InputStream in = null;
        try {
            // 类加载器
            in = PropertiesUtil.class.getResourceAsStream(fileName);
            p.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return p;
    }


    public static String get(String key) {
        String value = null;
        if (p.containsKey(key)) {
            value = p.getProperty(key);
        }
        return value;
    }



}
