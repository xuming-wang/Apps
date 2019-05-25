package org.air.bigearth.apps.util;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipOutputStream;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件解压缩工具类
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-23
 */
public class ZipKit {
	
	public String apath (String a,String b) throws Exception{
		return zipFile(new File(a), new File(b)).getPath();
	}
	
	/**
	 *  压缩单个文件
	 *
	 * @param inputFile 压缩的文件
	 * @param outFile 压缩后的文件
	 * @throws Exception
	 */
	public static File zipFile(File inputFile, File outFile) throws Exception {
		if(!inputFile.exists()){
			throw new Exception("文件或目录不存在!");
		}
		
		//生成zip包的 时间戳
		String sufixs = DataKeyUtil.getDataKey();
		String of = outFile.getPath();
		if(of.indexOf(".")==-1){
			if(!outFile.exists()){
				outFile.mkdirs();
			}
			outFile = new File (outFile,sufixs+".zip");
		}else{
			if((of.indexOf(File.separator, of.indexOf(File.separator)+1))!=-1){
				File _of = new File (of.substring(0,of.lastIndexOf(File.separator)));
				if(!_of.exists()){
					_of.mkdirs();
				}
			}
		}
	
		//输出目录的命名规则
		ZipFile zipFile = new ZipFile(outFile);  
		ZipParameters parameters = new ZipParameters();  
		parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); 
		parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);   
		
			File tempfile = null;
			//判断如果是文件，则进行下一步操作
			if(inputFile.isFile()){
				String tempPath = inputFile.getPath();
				ArrayList<File> filesToAdd = new ArrayList<File>(); 
				//带了文件名的
				if (tempPath.indexOf(".") != -1) {
					//文件路径 /
					if(tempPath.indexOf(File.separator, tempPath.indexOf(File.separator)+1)==-1){
						filesToAdd.add(inputFile);
					}else{
						//分离文件   和   文件名
						tempfile = new File(tempPath.substring(0,tempPath.lastIndexOf(File.separator)));
						//判断分离的文件是否存在,如果不存在创建文件
						if (!tempfile.exists())  tempfile.mkdirs();
						//如果    当前文件夹里面有重复的文件,自定义生成文件
						if (inputFile.exists()) {
							filesToAdd.add(new File(tempfile, sufixs + ".zip"));
						}else{
							//否则  添加当前文件
							filesToAdd.add(inputFile);
						}
					}
				}else{
					filesToAdd.add(new File(inputFile, sufixs + ".zip"));
				}
				zipFile.addFiles(filesToAdd, parameters);
			}else{
				zipFile.addFolder(inputFile, parameters);
			}
		return outFile;
	}

	/**
	 *  压缩多个文件
	 *
	 * @param inputFiles 压缩的文件
	 * @param outFile 压缩后的文件
	 * @throws Exception
	 */
	public static File zipFile(List<File> inputFiles,File outFile) throws Exception{
		
		if(null==inputFiles||inputFiles.size()<=0){
			throw new Exception("文件或目录不存在!");
		}
		
		for (File inputFile : inputFiles){
			outFile=zipFile(inputFile, outFile);
		}
		return outFile;
	}

	/**
	 *  压缩文件流
	 *
	 * @param file 压缩的文件
	 * @param out 压缩后的文件流
	 * @throws Exception
	 */
		public static OutputStream zipOutputStream(File file, OutputStream out)
				throws Exception {
			
			if(!file.exists()){
				throw new Exception("文件或目录不存在!");
			}
			
			ZipOutputStream outputStream = new ZipOutputStream(out);
			if (file.exists()) {
				ZipParameters parameters = new ZipParameters();
				parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
				parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
				parameters.setFileNameInZip(file.getName());
				parameters.setSourceExternalStream(true);
				outputStream.putNextEntry(file, parameters);

				if (file.isDirectory()) {
					outputStream.closeEntry();
					throw new Exception("此方法不能压缩目录...");
				}

				InputStream inputStream = new FileInputStream(file);
				byte[] bufferRead = new byte[4096];
				int length = -1;
				while ((length = inputStream.read(bufferRead)) != -1) {
					outputStream.write(bufferRead, 0, length);
				}
				outputStream.closeEntry();
				inputStream.close();
			}else{
				throw new Exception("文件不存在!");
			}
			outputStream.finish();
			return out;
		}

		
		/**
		 *  压缩文件流
		 *
		 * @param listFile 压缩的文件
		 * @param out 压缩后的文件流
		 * @throws Exception
		 */
		public static OutputStream zipOutputStream(List<File> listFile,
				OutputStream out) throws Exception {
			
			ZipOutputStream outputStream = new ZipOutputStream(out);
			for (File file : listFile) {
				if (file.exists()) {
					ZipParameters parameters = new ZipParameters();
					parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
					parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
					parameters.setFileNameInZip(file.getName());
					parameters.setSourceExternalStream(true);
					outputStream.putNextEntry(file, parameters);
					if (file.isDirectory()) {
						outputStream.closeEntry();
						continue;
					}
					InputStream inputStream = new FileInputStream(file);
					byte[] bufferRead = new byte[4096];
					int length = -1;
					while ((length = inputStream.read(bufferRead)) != -1) {
						outputStream.write(bufferRead, 0, length);
					}
					outputStream.closeEntry();
					inputStream.close();
				}else{
					throw new Exception("文件或目录不存在!");
				}
			}
			outputStream.finish();
			return out;
		}

		/**
		 *  解压文件
		 *
		 * @param zipfile 压缩包
		 * @param targetFile 解压后的文件
		 * @throws Exception
		 */
		public static void unzip(File zipfile, File targetFile) {
			try {
				ZipFile zipFile = new ZipFile(zipfile);
				zipFile.extractAll(targetFile.getPath());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 *  解压指定的文件
		 *
		 * @param zipfile 压缩包
		 * @param files 解压的指定文件
		 * @param targetFile 解压后的文件
		 * @throws ZipException 
		 * @throws Exception
		 */
		public static void unzip(File zipfile, File files,
				File targetFile) throws ZipException{
			
		    	ZipFile zipFile = new ZipFile(zipfile);

			    zipFile.extractFile(files.getPath(), targetFile.getPath());

		}
		
}