package org.air.bigearth.apps.file.util;

import net.sf.json.JSONObject;
import org.air.bigearth.apps.constant.CommonConstants;
import org.air.bigearth.apps.constant.ImageConstants;
import org.air.bigearth.apps.constant.LocalFileConstants;
import org.air.bigearth.apps.constant.RemoteFileConstants;
import org.air.bigearth.apps.exception.service.BaseException;
import org.air.bigearth.apps.file.domain.Attachment;
import org.air.bigearth.apps.file.exception.FileException;
import org.air.bigearth.apps.system.domain.basic.User;
import org.air.bigearth.apps.util.*;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 附件操作工具类
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-24
 */
public class AttachmentUtil {
	private static final Logger logger = LoggerFactory.getLogger(AttachmentUtil.class);

	/**
	 * 文件快速拷贝
	 *
	 * @param srcFile 源文件
	 * @param destFile 目标文件
	 */
	public static boolean transferCopy(File srcFile,File destFile){
		FileInputStream input = null;
		FileOutputStream output = null;
		FileChannel inChannel = null;
		FileChannel outChannel = null;
		try{
			input = new FileInputStream(srcFile);
			output = new FileOutputStream(destFile);
			inChannel = input.getChannel();
			outChannel = output.getChannel();
			inChannel.transferTo(0, inChannel.size(), outChannel);
			return true;
		}catch(Exception e){
			logger.error("附件传输异常！文件路径："+srcFile.getPath(),e);
		}finally{
			try{
				if(inChannel!=null){
					inChannel.close();
				}
				if(outChannel!=null){
					outChannel.close();
				}
				if(output!=null){
					output.close();
				}
				if(input!=null){
					input.close();
				}
			}catch(Exception e){
				logger.error(e.getMessage(),e);
			}
		}
		return false;
	}
	
	/**
	 * 根据根目录按续生成目录（从1开始，每个文件夹下最多maxFiles个文件）
	 *
	 * @param basePath 根路径
	 * @param maxFiles 每个文件夹最多文件数
	 * @return File 目标存储路径
	 */
	private static File getSerialFoderPath(File basePath,int maxFiles){
		if(basePath.exists()){
			if(maxFiles<100 || maxFiles>1000){
				maxFiles = 1000;
			}
			int maxSerial = 1;
			File[] array = basePath.listFiles();
			if(array.length>0){
				for(int i=0;i<array.length;i++){
					if(array[i].isDirectory()){
						int fileSerial = Integer.valueOf(array[i].getName());
						maxSerial = (maxSerial>fileSerial?maxSerial:fileSerial);
					}
				}
			}
			
			File maxFile = new File(basePath.getPath()+File.separator+maxSerial);
			if(maxFile.listFiles()!=null && maxFile.listFiles().length>=maxFiles){
				maxFile = new File(basePath.getPath()+File.separator+(maxSerial+1));
			}
			if(!maxFile.exists()){
				maxFile.mkdir();
			}
			return maxFile;
		}else{//文件目录不存在
			return null;
		}
	}
	
	/**
	 * 根据文件策略获取文件目标保存路径(注：返回null时表示文件保存到数据库附件表中)
	 *
	 * @param baseFilePath 文件存储根路径
	 * @param filePathRule 文件路径生成规则:
	 * <p>1.按日期创建目录:每天创建一个以年月日命名的文件夹，所有文件存储到该文件夹下</p>
	 * <p>2.按复合方式创建目录(先按日期、再按数目创建目录):每天创建一个以年月日命名的文件夹，然后再在此文件夹下按文件数目创建目录。</p>
	 * <p>3.按文件数目创建目录：创建以数字1开始命名的文件夹，文件夹1中文件数达到1000，自动创建文件夹2，文件夹2中文件数达到1000，自动创建文件夹3，以此类推</p>
	 * @param fileMd5 文件MD5值
	 * @return
	 * @throws BaseException
	 */
	public static File getTargetFilePath(String baseFilePath,String filePathRule, String fileMd5) throws BaseException {
		User user = ShiroUtil.getSysUser();
		// 保存到磁盘
		if(!"4".equals(filePathRule)){
			File basePath = new File(baseFilePath);
			if(!basePath.exists()){
				if(basePath.isDirectory()){
					basePath.mkdirs();
				}else{
					if(StringUtil.isNotNullOrBlank(basePath.getParentFile())){
						basePath.getParentFile().mkdirs();
					}
					basePath.mkdirs();
				}
			}
			// 文件上传根目录不存在，默认上传到同项目根目录同级目录下
			//if(!basePath.exists()){
			//	basePath = new File(FileUtil.getRootPath());
			//	basePath = new File(basePath.getParent()+File.separator+"uploads");
			//	basePath.mkdirs();
			//}
			// 存在根目录
			if(basePath.exists()){
				// 按文件数目创建目录：创建以数字1开始命名的文件夹，文件夹1中文件数达到1000，自动创建文件夹2，文件夹2中文件数达到1000，自动创建文件夹3，以此类推
				if("3".equals(filePathRule)){
					return getSerialFoderPath(basePath,1000);
					// 按日期创建目录:每天创建一个以年月日命名的文件夹，所有文件存储到该文件夹下
				}else if("1".equals(filePathRule)){
					SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
					File targetPath = new File(basePath.getPath()+File.separator+format.format(new Date()));
					if(!targetPath.exists()){
						targetPath.mkdirs();
					}
					return targetPath;
				}else if("2".equals(filePathRule)) {
					if (StringUtil.isNotNullOrBlank(fileMd5)) {
						baseFilePath = baseFilePath + "/user/" + user.getLoginName() + File.separator + ImageConstants.UPLOAD + File.separator + fileMd5;
					} else {
						baseFilePath = baseFilePath + "/user/" + user.getLoginName() + File.separator + ImageConstants.UPLOAD;
					}
					File targetPath = new File(baseFilePath);
					//if(!targetPath.exists()){
					//	targetPath.mkdirs();
					//}
					return targetPath;
				}else{// 按复合方式创建目录(先按日期、再按数目创建目录):每天创建一个以年月日命名的文件夹，然后再在此文件夹下按文件数目创建目录。
					SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
					File targetPath = new File(basePath.getPath()+File.separator+format.format(new Date()));
					if(!targetPath.exists()){
						targetPath.mkdirs();
					}
					return getSerialFoderPath(targetPath,1000);
				}
			}else{
				throw new FileException("创建文件上传存储根目录【"+basePath+"】失败！");
			}
		}
		// 保存到数据库中
		return null;
	}

	/**
	 * 压缩文件
	 *
	 * @param AttachmentDTO
	 * @param filePath
	 * @param uploadPath
	 * @return
	 */
	public static File zipFile(Attachment AttachmentDTO, String filePath, String uploadPath){
		File outputFile = null;
		
		int compressedFlag = AttachmentDTO.getCompressedFlag();
		if(compressedFlag==1){
			try {
				outputFile = zipFile(filePath,new File(uploadPath));
				
			} catch (Exception e) {
				logger.error("压缩失败！-----"+e.getMessage());
				return null;
			}
		}
		if(outputFile==null){
			outputFile = new File(filePath);
		}else{
			new File(filePath).delete();
		}
		
		return outputFile;
	}
	public File unzipFile(File sourceFile){
		return null;
	}

	/**
	 * 加密
	 *
	 * @param inputFile
	 * @param uploadPath
	 * @param encryptType
	 * @return
	 */
	public static File encryptFile(File inputFile,String uploadPath,String encryptType){
		File outputFile = null;
		//加密方式 开始加密文件
       if(StringUtil.isNotNullOrBlank(encryptType)&&!encryptType.equals("0")){
       	encryptType = getEncryptType(encryptType);;
       	try {
       		String name = inputFile.getName();
        	String outputFileName = DataKeyUtil.getDataKey()+ name.substring(name.lastIndexOf("."));
			outputFile = new File(new File(uploadPath), outputFileName);
			
       		boolean flag = EncryptAES.encryptFile(inputFile, outputFile, encryptType);
       		if(flag){
       			return outputFile;
       		}else{
       			return null;
       		}
		} catch (Exception e) {
			logger.error("加密失败！-----"+e.getMessage());
				return null;
				
			}
       }//加密结束
		return outputFile;
	}
	public File decryptFile(File sourceFile){
		return null;
	}
	
	public File zipAndEncryptFile(File sourceFile){
		return null;
	}
	
	public File unzipAndDecryptFile(File sourceFile){
		return null;
	}
	
	/**
	 * 获取附件文件(已被解密和解压缩)
	 *
	 * @param attDto
	 * @return
	 */
	public static File getHandledFile(Attachment attDto){
		try{
			String suffixs = attDto.getSuffix();
			String filePath = attDto.getFilePath();
			int compressedFlag = attDto.getCompressedFlag();
			String basePath = attDto.getBasePath();
			String encryptedFlag = attDto.getEncryptedType();
			String encryptKey = getEncryptType(encryptedFlag);
			File attFile = new File(filePath);
			if(compressedFlag==2 && !"1".equals(encryptedFlag)){//不加密、不压缩
				return attFile;
			}
			String suffix = (suffixs==null || "".equals(suffixs))?"":"."+suffixs.trim(); 
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			File targetTempPath = new File(basePath+File.separator+"targetTempPath"+File.separator + format.format(new Date()) + File.separator + Calendar.getInstance().get(Calendar.HOUR_OF_DAY));//目标文件临时目录
			if(compressedFlag==1 && "1".equals(encryptedFlag)){//压缩并加密
				File unZipedFile = new File(targetTempPath.getPath());
				File file = new File(targetTempPath.getPath()+File.separator+UUID.randomUUID().toString().replaceAll("-", "") + suffix);
				unZipedFile.getParentFile().mkdirs();//创建目录
				ZipKit.unzip(attFile, unZipedFile);//解压文件
				unZipedFile = new File(targetTempPath.getPath(),(attFile.getName().substring(0, attFile.getName().length()-4)+suffix));
				if(EncryptAES.decryptFile(unZipedFile, file, encryptKey)){
					return file;
				}
			}else if(compressedFlag==1 && !"1".equals(encryptedFlag)){//只压缩不加密
				File unZipedFile = new File(targetTempPath.getPath()+File.separator+UUID.randomUUID().toString().replaceAll("-", "") + suffix);
				unZipedFile.mkdirs();//创建目录
				ZipKit.unzip(attFile, unZipedFile);//解压文件
				return unZipedFile;
			}else if(compressedFlag!=1 && "1".equals(encryptedFlag)){//不压缩只加密
				File file = new File(targetTempPath.getPath()+File.separator+UUID.randomUUID().toString().replaceAll("-", "") + suffix);
				new File(file.getParent()).mkdirs();//创建目录
				if(EncryptAES.decryptFile(attFile, file, encryptKey)){
					return file;
				}
			}
		}catch(Exception e){
			logger.error("获取附件文件(已被解密和解压缩)异常！附件ID："+attDto.getAttachmentId(),e);
		}
		return new File("");
	}
	/** 压缩单个文件*/
   /* public static File zipFile(String filepath ,File zippath) {
    	File zipFile = null;
    	try {
    		// 取得文件尾缀
    		String orgiginalFileName = filepath.replaceAll("/", "\\").substring(filepath.lastIndexOf("\\")+1,filepath.length());
    		int lastPointIndex = orgiginalFileName.lastIndexOf(".");
			String sufixs = orgiginalFileName.substring(0,lastPointIndex)+".zip";
    		String of = zippath.getPath();
    		if(of.indexOf(".zip")==-1){
    			if(!zippath.exists()){
    				zippath.mkdirs();
    			}
    			zipFile = new File (zippath,sufixs);
    		}
            File file = new File(filepath);
            InputStream input = new FileInputStream(file);
            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
            zipOut.putNextEntry(new ZipEntry(file.getName()));
            int temp = 0;
            while((temp = input.read()) != -1){
                zipOut.write(temp);
            }
            input.close();
            zipOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return zipFile;
	}*/
    public static String getEncryptType(String cryptType){
		return "AES";
	}
    
    public static File zipFile(String filepath ,File zippath) {
    	File zipFile = null;
    	try {
    		// 取得文件尾缀
    		String orgiginalFileName = filepath.replaceAll("/", "\\").substring(filepath.lastIndexOf("\\")+1,filepath.length());
    		int lastPointIndex = orgiginalFileName.lastIndexOf(".");
			String sufixs = orgiginalFileName.substring(0,lastPointIndex)+".zip";
    		String of = zippath.getPath();
    		if(of.indexOf(".zip")==-1){
    			if(!zippath.exists()){
    				zippath.mkdirs();
    			}
    			zipFile = new File (zippath,sufixs);
    		}
    		
            File file = new File(filepath);
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);  
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream, new CRC32());  
            ZipOutputStream out = new ZipOutputStream(cos);  
            String basedir = "";  
            compressFile(file, out, basedir);  
            out.close();  
            
    		/* File file = new File(filepath);
            InputStream input = new FileInputStream(file);
            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
            zipOut.putNextEntry(new ZipEntry(file.getName()));
            int temp = 0;
            while((temp = input.read()) != -1){
                zipOut.write(temp);
            }
            input.close();
            zipOut.close();*/
           
		} catch (Exception e) {
			e.printStackTrace();
		}
		return zipFile;
	}
    
    /** 压缩一个文件 */  
    private static void compressFile(File file, ZipOutputStream out, String basedir)  
    {  
        if (!file.exists())  
        {  
            return;  
        }  
        try  
        {  
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));  
            ZipEntry entry = new ZipEntry(basedir + file.getName());
            out.putNextEntry(entry);  
            int count;  
            byte data[] = new byte[8192];  
            while ((count = bis.read(data, 0, 8192)) != -1)  
            {  
                out.write(data, 0, count);  
            }  
            bis.close();  
        }  
        catch (Exception e)  
        {  
            throw new RuntimeException(e);  
        }  
    }

	/**
	 * 判断文件是否存在
	 *  数据格式：{"code":"1","data":[{"UUID":"d9aa50e5bdeb4945a076235b9693880e","md5Code":"d68b513a52df31f254c7823ee3ce8993","dataset":"landsat"}],"message":"文件已存在"}
	 *
	 * @param fileMd5 前端计算的md5值
	 * @return
	 */
	public static String checkFileIsExistByFileMd5(String fileMd5) {
		String url = RemoteFileConstants.CLUSTER_DATA_SOCKET + RemoteFileConstants.UPLOAD_FILE_EXIST;
		String param = "md5=" + fileMd5;
		try {
			String fileIsExistStr = HttpToServer.sendGetForHttp(url, param);
			return fileIsExistStr;
		} catch (Exception e) {
			logger.error("拉取文件MD5值失败！" + fileMd5, e);
		}

		return null;
	}

	/**
	 * 上传文件到服务器
	 *
	 * @param attachmentDTO 附件对象
	 * @param isExistInfo   文件验证信息
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String uploadFileToServer(Attachment attachmentDTO, String isExistInfo) {
		JSONObject attachmentObj = JSONObject.fromObject(attachmentDTO);
		attachmentObj.put("ftpUrl", attachmentDTO.getFilePath().substring(0, attachmentDTO.getFilePath().lastIndexOf("/")));
		attachmentObj.put("fileTypes", "text");
		attachmentObj.put("dataType", "upload");
		attachmentObj.put("dataset", ImageConstants.LANDSAT);
		JSONObject jsonFileInfo = null;
		if (StringUtil.isNotNullOrBlank(isExistInfo)) {
			jsonFileInfo = JSONObject.fromObject(isExistInfo);
			if ("1".equals(jsonFileInfo.get("code"))) {
				//attachmentObj.put("jsonFileInfo", jsonFileInfo);
				attachmentObj.put("UUID",jsonFileInfo.getJSONArray("data").getJSONObject(0).get("UUID"));
			} else {
				attachmentObj.put("UUID", DataKeyUtil.getDataKey());
			}
			attachmentObj.put("isExist", "1".equals(jsonFileInfo.get("code")) ? "true" : "false");
		}

		String url = RemoteFileConstants.UPLOAD_URL;
		String json = attachmentObj.toString();
		logger.info(json);
		String isUpload = null;
		try {
			isUpload = HttpClientUtil.doPostJson(url, json);
		}catch (Exception e) {
			logger.error("推送文件失败:" + isUpload, e);
		}
		return isUpload;
	}

	public static void createDir(File file) {
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	public static String getFileSuffix(String originalFileName) {
		int lastPointIndex = 0;
		String fileSuffix = null;
		// 取得文件尾缀
		if (originalFileName.contains(".")) {
			lastPointIndex = originalFileName.lastIndexOf(".");
			fileSuffix = originalFileName.substring(lastPointIndex + 1);
		} else {
			fileSuffix = "";
		}
		return fileSuffix;
	}
}
