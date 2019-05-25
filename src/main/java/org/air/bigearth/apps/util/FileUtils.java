package org.air.bigearth.apps.util;


import org.air.bigearth.apps.constant.LocalFileConstants;
import org.air.bigearth.apps.file.domain.Attachment;
import org.air.bigearth.apps.image.domain.DataAccess;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 文件处理工具类
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2018-12-17
 */
public class FileUtils {
    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 把json格式的字符串写入到指定的文件中,并生成json文件记录或数据库记录,用于检索查询
     *
     * @param params
     */
    public static Attachment writeMessageToFile(Map<String, String> params) {
        // 1.把json格式的字符串写入到指定的文件中
        String fileType = null;
        if (StringUtils.isEmpty(params.get("fileType"))) {
            fileType = "default";
        } else {
            fileType = params.get("fileType");
        }

        String fileName = String.valueOf(System.currentTimeMillis());
        String fileSavePath = LocalFileConstants.BASE_PATH + fileType + File.separator + fileName;
        generateDirAndFile(fileSavePath);
        writeFile(fileSavePath, params.get("returnJson"));

        // 2.生成json文件记录
        /// String filePath = File.separator + fileType + File.separator + fileName;
        /*String jsonSavePath = FileConstants.BASE_PATH + fileType + ".json" ;
        generateDirAndFile(jsonSavePath);
        JSONObject object = JSONObject.fromObject(getAttachmentDTO(fileType, fileName, fileSavePath));
        writeFile(jsonSavePath, object.toString());*/
        return  getAttachmentDTO(fileType, fileName, fileSavePath);
    }

    /**
     * 为附件对象赋值
     *
     * @param fileType 文件类型
     * @param fileName 文件名称
     * @param filePath 文件路径
     * @return
     */
    public static Attachment getAttachmentDTO(String fileType, String fileName, String filePath) {
        Attachment dto = new Attachment();
        dto.setAttachmentId(DataKeyUtil.getDataKey());
        dto.setFileName(fileName);
        dto.setFilePath(filePath);
        //dto.setBashPath(FileConstants.BASE_PATH);
        //dto.setFileType(fileType);
        //dto.setCreateTime(DateUtil.getCurrentTime());
        return dto;
    }

    public static DataAccess writeMessageToLogFile(Map<String, String> params) {
        String fileType = null;
        if (StringUtils.isEmpty(params.get("fileType"))) {
            fileType = "default";
        } else {
            fileType = params.get("fileType");
        }

        String fileName = String.valueOf(System.currentTimeMillis());
        String fileSavePath = LocalFileConstants.LOG_BASE_PATH + params.get("userId") + File.separator + fileType + File.separator + fileName;
        generateDirAndFile(fileSavePath);
        writeFile(fileSavePath, params.get("mtl"));

//        String jsonSavePath = FileConstants.LOG_BASE_PATH + params.get("userId") + File.separator + fileType + ".json" ;
//        generateDirAndFile(jsonSavePath);
//        JSONObject object = JSONObject.fromObject(getHttpDTO(params));
//        writeFile(jsonSavePath, object.toString());
        return getHttpDTO(params);
    }

    public static DataAccess getHttpDTO(Map<String, String> params) {
        DataAccess dto = new DataAccess();
        /*dto.setId(DataKeyUtil.getDataKey());
        dto.setFileType(params.get("fileType") == null ? "default" : params.get("fileType"));
        dto.setCreateTime(DateUtil.getCurrentTime());
        dto.setUrl(params.get("url"));
        dto.setParam(params.get("param"));
        dto.setResult(params.get("mtl"));*/
        return dto;
    }



    public static String readFile (String filePath) {
        BufferedReader br = null;
        String result = null;
        try {
            br = new BufferedReader(new FileReader(filePath));
            String line = null;
            while ((line = br.readLine()) != null) {
                result += line;
//                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static void writeFile (String filePath, String writeStr) {
        BufferedWriter bw = null;
        try {
            // 方法一：使用FileWriter()，每次程序运行后会产生一个新的文件，如果这个新的文件在相同目录下已经有同名文件存在，则覆盖掉该文件，只显示本次执行的结果。
//            bw = new BufferedWriter(new FileWriter(filePath));
            // 方法二：使用OutputStreamWriter（）时，每次程序运行后如果产生的文件在相同目录下有同名文件存在，则在原来文件内容后面续写，而不是覆盖。
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(filePath, true), "UTF-8");
            bw = new BufferedWriter(osw);
            bw.write(writeStr);
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void generateDirAndFile(String savePath) {
        // 文件存放path
        File file = new File(savePath);
        // 首先看父类文件夹有没有
        File fileParent = file.getParentFile();
        // 首先创建父类文件夹
        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }
        /*if (!file.exists()) {
            file.createNewFile();
        }*/
    }

    /**
     * 创建文件并赋予写权限
     *
     * @param outputPath 文件输出路径
     */
    public static void createFileAndWritePermission(String outputPath) {
        File file = new File(outputPath);
        if (!file.exists()) {
            file.mkdirs();
            // java 生成linux下的文件，有时候需要添加权限。比如755权限
            String command = "chmod 777 -R " + file;
            try {
                Runtime.getRuntime().exec(command);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param request
     * 根据请求解析请求中的参数(文件与非文件)返回Map集合,并将文件上传至服务器
     * @return
     */
    public static Map getFilePath(HttpServletRequest request) {
        String upload_directory = "upload";
        String slash_directory = "/";

        // 上传配置
        int memory_threshold   = 1024 * 1024 * 3;  // 3MB
        int max_file_size      = 1024 * 1024 * 40; // 40MB
        int max_request_size   = 1024 * 1024 * 50; // 50MB


        // 配置上传参数
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置内存临界值 - 超过后将产生临时文件并存储于临时目录中
        factory.setSizeThreshold(memory_threshold);
        // 设置临时存储目录
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        ServletFileUpload upload = new ServletFileUpload(factory);
        // 设置最大文件上传值
        upload.setFileSizeMax(max_file_size);

        // 设置最大请求值 (包含文件和表单数据)
        upload.setSizeMax(max_request_size);

        upload.setHeaderEncoding("UTF-8");

        String uploadPath =  request.getContextPath()+slash_directory+upload_directory;
        String fileName="";
        String filePath="";
        // 如果目录不存在则创建
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        Map paramMap = new HashMap();

        try {
            // 解析请求的内容提取文件数据
            @SuppressWarnings("unchecked")
            List<FileItem> formItems = upload.parseRequest(request);

            if (formItems != null && formItems.size() > 0) {
                // 迭代表单数据
                for (FileItem item : formItems) {
                    // 处理不在表单中的字段
                    if (!item.isFormField()) {
                        fileName = new File(item.getName()).getName();
                        filePath = uploadPath + slash_directory + fileName;
                        File storeFile = new File(filePath);
                        item.write(storeFile);
                        paramMap.put("fileName", fileName);
                        paramMap.put("filePath", filePath);
                    }else {
                        String value = item.getString("utf-8");
                        paramMap.put(item.getFieldName(), value);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       /*Iterator<Map.Entry> entries = paramMap.entrySet().iterator();
	   while (entries.hasNext()) {
	     Map.Entry<Integer, Integer> entry = entries.next();
	     System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
	   }*/
        return paramMap;
    }

    public static Map getFilePath2(HttpServletRequest request) {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        Map paramMap = new HashMap();

        try {
            // 解析请求的内容提取文件数据
            @SuppressWarnings("unchecked")
            List<FileItem> formItems = upload.parseRequest(request);

            if (formItems != null && formItems.size() > 0) {
                // 迭代表单数据
                for (FileItem item : formItems) {
                    // 处理不在表单中的字段
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        paramMap.put("fileName", fileName);
                    }else {
                        String value = item.getString("utf-8");
                        paramMap.put(item.getFieldName(), value);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       /*Iterator<Map.Entry> entries = paramMap.entrySet().iterator();
	   while (entries.hasNext()) {
	     Map.Entry<Integer, Integer> entry = entries.next();
	     System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
	   }*/
        return paramMap;
    }
}
