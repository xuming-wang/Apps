package org.air.bigearth.apps.util;

import java.util.Random;
import java.util.UUID;


/**
 * 主键生成工具类
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-01-10
 */
public class DataKeyUtil {

	/**
	 * 得到一个主键
	 *
	 * @return
	 */
	public static String getDataKey(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/**
	 * 得到一个主键(流程ID)
	 *
	 * @return
	 */
	public static String generateRandomDataKey() {
	    //长度
	    int length = 16;
	    //字符源，可以根据需要删减
	    String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";     
	    Random random = new Random();     
	    StringBuffer sb = new StringBuffer();    
	    sb.append("busi");
	    for (int i = 0; i < length; i++) {     
	        int number = random.nextInt(base.length());     
	        sb.append(base.charAt(number));     
	    }     
	    return sb.toString();     
	}

	/**
	 * 得到一个主键(文件ID)
	 *
	 * @return
	 */
	public static String generateRandomFileKey() {
		//长度
		int length = 16;
		//字符源，可以根据需要删减
		String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		//sb.append("busi");
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * 得到一个主键(订单号ID)
	 *
	 * @return
	 */
	public static String getOrderIdByUUId() {
		int machineId = 1;//最大支持1-9个集群机器部署
		int hashCodeV = UUID.randomUUID().toString().hashCode();
		if(hashCodeV < 0) {//有可能是负数
			hashCodeV = - hashCodeV;
		}
		// 0 代表前面补充0
		// 4 代表长度为4
		// d 代表参数为正数型
		return machineId + String.format("%015d", hashCodeV);
	}

	public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
			"g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
			"t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z" };


	public static String generateShortUuid() {
		StringBuffer shortUuid = new StringBuffer();
		shortUuid.append("repeat");
		String uuid = UUID.randomUUID().toString().replace("-", "");
		for (int i = 0; i < 8; i++) {
			String str = uuid.substring(i * 4, i * 4 + 4);
			int x = Integer.parseInt(str, 16);
			shortUuid.append(chars[x % 0x3E]);
		}
		return shortUuid.toString();

	}





}
