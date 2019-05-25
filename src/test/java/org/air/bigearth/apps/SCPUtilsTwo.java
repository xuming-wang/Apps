package org.air.bigearth.apps;

import ch.ethz.ssh2.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class SCPUtilsTwo {
    private static final Logger logger = LoggerFactory.getLogger(SCPUtilsTwo.class);

    private static int port = 22;//目标服务器端口
    private static String basePath="/home/henry/Desktop/modis数据介绍.xls";//目标服务器目录
    private static String copyHost="192.168.1.107";//目标服务器地址
    private static String copyUsername="henry"; //目标服务器用户名
    private static String copyPassword="18161816";//目标服务器密码
    private static String localFile="/home/centos/Desktop";//本地服务器指定目录//liux环境下会创建到根目录下

    public static void main(String[] args) {
        SCPInit();
    }

    public static void SCPInit(){
        String batchNo="modis数据介绍.xls";//文件夹名称
        Long d1 = Calendar.getInstance().getTime().getTime();
        System.out.println("localFile============"+localFile);
        System.out.println();
        /*File file=new File(localFile+"/"+batchNo);
        if(!file.exists()){
            file.mkdirs();
        }*/
        System.out.println("创建成功！");
        Connection conn=null;
        List<String> remoteFile=new ArrayList<String>();
        System.out.println("copyHost==="+copyHost);
        System.out.println("copyUsername==="+copyUsername);
        System.out.println("copyPassword==="+copyPassword);
        System.out.println("port==="+port);
        try{
            conn = new Connection(copyHost,port);
            System.out.println("开始连接");
            conn.connect();
            System.out.println("开始登录");
            boolean isAuthenticated = conn.authenticateWithPassword(copyUsername, copyPassword);
            if (isAuthenticated == false)
                throw new IOException("Authentication failed.");

            SFTPv3Client sftpv3Client = new SFTPv3Client(conn);

            //查看目标服务器此文件夹是否存在
            Boolean b = SCPUtilsTwo.isDir(sftpv3Client, basePath);
            if(!b){
                System.out.println("文件夹不存在");
            }else{
                //获取目标文件夹中所有文件
                Vector<?> v =sftpv3Client.ls(basePath+"/"+batchNo);
                Iterator<?> it = v.iterator();
                while (it.hasNext()) {
                    SFTPv3DirectoryEntry entry = ( SFTPv3DirectoryEntry) it.next();
                    String filename = entry.filename;
                    remoteFile.add(basePath+"/"+batchNo+"/"+filename);
                }
                SCPClient client= conn.createSCPClient();
                String [] remoteFiless=new String[remoteFile.size()];
                String[] remoteFiles =remoteFile.toArray(remoteFiless);
                //开始上传
                client.get(remoteFiles, localFile+"/"+batchNo);
                System.out.println("上传完成");
                Long d2 =Calendar.getInstance().getTime().getTime();
                Long m=(d2-d1)/1000;
                System.out.println("总耗时==="+m+"秒");
            }
        }catch (IOException e){
            System.out.println("文件上传异常"+e);
        }finally{
            conn.close();
        }

    }
    public static Boolean isDir(SFTPv3Client sftpv3Client, String path) {
        if(sftpv3Client != null && path!=null && !("").equals(path)) {
            SFTPv3FileAttributes sFTPv3FileAttributes;
            try {
                sFTPv3FileAttributes = sftpv3Client.lstat(path);
            } catch (IOException e) {
                System.out.println("文件加不存在："+e.getLocalizedMessage());
                return false;
            }
            return sFTPv3FileAttributes.isDirectory();
        }
        return false;
    }


}
