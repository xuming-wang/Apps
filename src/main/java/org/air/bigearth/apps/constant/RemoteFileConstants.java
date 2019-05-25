package org.air.bigearth.apps.constant;

/**
 * 远程文件常量信息
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-01-25
 */
public class RemoteFileConstants {
    /**
     * IP端口组合叫Socket，中文是套接字
     */
    public static final String CLUSTER_DATA_SOCKET = "http://192.168.1.151:8080";
    public static final String LANDSAT_DATA_URL = CLUSTER_DATA_SOCKET + "/es/query";
    public static final String DATASET = CLUSTER_DATA_SOCKET + "/collection/queryAll";
    public static final String PAGE_DATASET = CLUSTER_DATA_SOCKET + "/es/collection";

    public static final String DATA_SOCKET = "http://192.168.1.159:8080";
    public static final String ES_LANDSAT_DATA_URL = DATA_SOCKET + "/es/query";
    public static final String ES_LANDSAT_METADATA_URL = DATA_SOCKET + "/es/query";

    public static final String ALGORITHM_SOCKET = "http://192.168.1.106:8090";
    public static final String ALGORITHM_TMS_SOCKET = "http://192.168.1.106:8080";

    public static final String UPLOAD_FILE_EXIST = "/es/upload";
    public static final String HDFS_URL = "hdfs://192.168.1.151:8020";

    public static final String HDFS_DIR = "/zds";

    public static final String DATAT_SOCKET = "http://192.168.1.107:8080";
    public static final String THUMBNAIL_URL = DATAT_SOCKET + "/retrieve/push";
    public static final String UPLOAD_URL = DATAT_SOCKET + "/retrieve/load";
    public static final String DOWNLOAD_URL = DATAT_SOCKET + "/retrieve/download";
    public static final String GENERATE_URL = DATAT_SOCKET + "/retrieve/hdfsdata";








}
