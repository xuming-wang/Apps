package org.air.bigearth.apps.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期转换工具类
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-23
 */
public class DateUtil {

    /**
     * 得到当前时间
     *  固定日期格式：yyyy-MM-dd
     *
     * @return
     */
    public static String getCurrentTime() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(d);
    }

    public static String getDateFormat() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(d);
    }

}
