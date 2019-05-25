package org.air.bigearth.apps.util;


import org.air.bigearth.apps.exception.service.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;


/**
 * 文件操作工具类
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-02-22
 */
public class FileUtil {
	private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);
	/**
	 * 获取classes路径
	 *
	 * @return
	 */
	public static String getClassPath(){
		String classPath = "";
		String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		if("\\".equals(File.separator)){//Windows
			classPath = path.substring(1);
			classPath = classPath.replace("/", "\\");
		}else if("/".equals(File.separator)){//Linux
			classPath = path.substring(1);
			classPath = classPath.replace("\\", "/");
		}
		return classPath;
	}

	/**
	 * 获取Web项目根目录
	 *
	 * @return
	 */
	public static String getRootPath(){
		String rootPath = "";
		String classPath = getClassPath();
		return rootPath.lastIndexOf("WEB-INF")>0?rootPath.substring(0, rootPath.lastIndexOf("WEB-INF")):"";
	}
	/**
	 * 创建文件夹
	 *
	 * @param strFilePath
	 *        需要创建的文件夹存放路径
	 */
	public static boolean createFile(String strFilePath) {
		boolean bFlag = false;
		try {
			File file = new File(strFilePath.toString());
			if (!file.exists()) {
				bFlag = file.mkdir();
			}else{
				System.out.println("该文件夹已存在");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bFlag;
	}

	/**
	 * 在指定路径下创建文件，并将内容写入文件
	 *
	 * @param strFilePath 创建文件路径及文件名称
	 * @param strFileContent 写入创建好的文件的内容
	 * @return
	 */
	public static boolean createFile(String strFilePath, String strFileContent) {
		boolean bFlag = false;
		try {
			File file = new File(strFilePath.toString());
			if (!file.exists()) { //文件不存在，则创建一个文件
				bFlag = file.createNewFile();
			}else{
				System.out.println("该路径下包含相同的文件名");
			}
			if (bFlag == Boolean.TRUE) { //创建文件成功后，写入内容
				FileWriter fw = new FileWriter(file);
				PrintWriter pw = new PrintWriter(fw);
				pw.println(strFileContent.toString());
				pw.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bFlag;
	}

	/**
	 * 删除文件
	 *
	 * @param strFilePath 路径加文件名称，如：C:\\Users\\Desktop\\aa（aa为文件名称）
	 * @return
	 */
	public static boolean removeFile(String strFilePath) {
		boolean result = false;
		if (strFilePath == null || "".equals(strFilePath)) {
			return result;
		}
		File file = new File(strFilePath);
		if (file.isFile() && file.exists()) {
			result = file.delete();
		}
		return result;
	}

	/**
	 * 删除指定文件夹下的所有文件及文件夹(包含路径中的文件夹)
	 *
	 * @param folderPath 路径加文件名称
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); //删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			myFilePath.delete(); //删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除指定文件夹下的所有文件及文件夹(不包含路径中的文件夹)
	 *
	 * @param path 路径加文件名称
	 * @return
	 */
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);//再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 获取目录下所有的文件（递归调用，包含下层目录文件夹下的文件）
	 *
	 * @param file
	 * @return resultFileName
	 */
	public static List<String> getAllFiles(File file) {
		List<String> resultFileName = null;
		File[] files = file.listFiles();
		if (files == null) {
			return resultFileName;// 判断目录下是不是空的
		}
		for (File f : files) {
			if (f.isDirectory()) {// 判断是否文件夹
				resultFileName.add(f.getPath());
				getAllFiles(f);// 调用自身,查找子目录
			} else {
				resultFileName.add(f.getPath());
			}
		}
		return resultFileName;
	}
	/**
	 * 获取目录下所有的文件（不包含下层目录文件夹下的文件）
	 *
	 * @param filePath
	 * @return resultFileName
	 */
	public static List<String> getAllFiles(String filePath) {
		List<String> resultFileName = null;
		File file = new File(filePath);
		File[] tempList = file.listFiles();
		for (int i = 0; i < tempList.length; i++) {
			if (tempList[i].isFile()) { // 文件
				System.out.println("文     件：" + tempList[i]);
				resultFileName.add(tempList[i].toString());
			}
			if (tempList[i].isDirectory()) { // 文件夹
				System.out.println("文件夹：" + tempList[i]);
				resultFileName.add(tempList[i].toString());
			}
		}
		return resultFileName;
	}

	/**
	 * 获取目录下所有的文件（不包含文件夹，本层目录下）
	 *
	 * @param file
	 * @return
	 */
	public static List<String> getFiles(File file) {
		List<String> resultFileName = null;
		File[] files = file.listFiles();
		if (files == null) {
			return resultFileName;// 判断目录下是不是空的
		}
		for (File f : files) {
			if (!f.isDirectory()) { // 不是文件夹
				resultFileName.add(f.getPath());
			}
		}
		return resultFileName;
	}

	/**
	 * 获取目录下指定后缀的所有文件
	 *
	 * @param path
	 * @return
	 */
	public static List getAllAppoint(String path) {
		List data = new ArrayList();
		File f = new File(path);
		if (f.isDirectory()) {
			File[] fs = f.listFiles();
			for (int i = 0; i < fs.length; i++) {
				data = getAllAppoint(fs[i].getPath());
			}
		} else if (f.getName().endsWith(".java")) {
			data.add(f.getName());
		}
		return data;
	}

	/**
	 * 文件复制
	 *
	 * @param filePath1
	 * @param filePath2
	 * @throws Exception
	 */
	public static void fileCopy(String filePath1, String filePath2)
			throws Exception {
		File f1 = new File(filePath1);
		File f2 = new File(filePath2);
		fileCopy(f1, f2);
	}

	public static void fileCopy(File f1, File f2) throws Exception {
		int length = 2097152;
		FileInputStream in = new FileInputStream(f1);
		FileOutputStream out = new FileOutputStream(f2);
		FileChannel inC = in.getChannel();
		FileChannel outC = out.getChannel();
		ByteBuffer b = null;
		while (true) {
			if (inC.position() == inC.size()) {
				outC.close();
				inC.close();
				break;
			}
			if ((inC.size() - inC.position()) < length) {
				length = (int) (inC.size() - inC.position());
			} else {
				length = 2097152;
			}
			b = ByteBuffer.allocateDirect(length);
			inC.read(b);
			b.flip();
			outC.write(b);
			outC.force(false);
		}
	}
	
	/**
	  * 获取classes路径
	  *
	  * @return
	  * @throws URISyntaxException String (返回类型)
	 */
	public static String getPath() throws BaseException {
		String path= Thread.currentThread().getContextClassLoader().getResource("/").getPath();
//		String path = FileUtil.class.getResource("/").toURI().getPath();
		return path;
	}
	
	public static String getContentFromFile(File file){
        StringBuffer sb = new StringBuffer();
        BufferedReader reader = null;
        String line = "";
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
			while ((line = reader.readLine()) != null) {
				sb.append(line+"\n");
			}
			return sb.toString();
		} catch (Exception e) {
			logger.error("得到文件内容报错，文件路径："+file.getAbsolutePath(),e);
		}finally{
			try {
				reader.close();
			} catch (IOException e) {
			}
		}
		
        return null;
	}



}
