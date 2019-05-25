package org.air.bigearth.apps.util;

import java.security.MessageDigest;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 字符串操作工具类
 *
 * @author wangxuming
 * @version 1.0
 * @date 2019-02-22
 */
public class StringUtil {
	/** 
     * 此方法判断字符是否为数字0-9 
     * 		是返回true,不是返回false
	 *
     * @param c char 
     * @return boolean 
     */  
    public static boolean isDigit(char c) {  
        return (('0' <= c) && (c <= '9'));  
    }
    /**
     * 判断字符串是否'全部'为数字
	 *
     * @param inputStr
     * @return
     */
    public static boolean isDigit(String inputStr) {  
        char tempChar;  
        for (int i = 0; i < inputStr.length(); i++) {  
            tempChar = inputStr.charAt(i);  
            // 如果字符中有一个字符不是数字则返回false  
            if (!isDigit(tempChar)) {  
                return false;  
            }  
        }  
        return true;  
    }  
    /**
	 * 字符串是否包含数字，如果是返回true，否则返回false
	 *
	 * @return
	 */
	public static boolean isContainNum(String value){
		Pattern p = Pattern.compile(".*\\d+.*");

		Matcher m = p.matcher(value);
		
		return m.matches();
	}
    /**
	 * 字符串是否包含英文字母，如果是返回true，否则返回false
	 *
	 * @return
	 */
	public static boolean isContainLetter(String value){
		Pattern p = Pattern.compile(".*[a-zA-Z]+.*");

		Matcher m = p.matcher(value);
		
		return m.matches();
	}  
  
    /** 
     * 验证字符串是否包含中文
	 *
     * @param str 
     * @return 是否包含中文:中文-true，没有中文-false 
     */  
    public static boolean containChinese(String str) {  
    	String check = ".*[\u4e00-\u9fa5]+.*$"; 
        Pattern regex = Pattern.compile(check);  
        Matcher matcher = regex.matcher(str);  
        return matcher.find();  
    }  
  
    /** 
     * 验证是否包含空格
	 *
     * @param str 
     * @return 是否包含空格 
     * 		包含空格返回true，不包含返回false
     */  
    public static boolean containBlank(String str) {  
        if(str.length() > str.replace(" ", "").length()) {  
            return true;  
        } else {  
            return false;  
        }  
    }  
  
    /** 
     * 验证是否只含中文、英文和数字
	 *
     * @param str
     * @return 是否合法 
     * 		是：返回true
     */  
    public static boolean onlyEngAndNum(String str) {  
        String check = "^[\\u4E00-\\u9FA5A-Za-z0-9]+$";  
        Pattern regex = Pattern.compile(check);  
        Matcher matcher = regex.matcher(str);  
        return matcher.matches();  
    } 
    /**
	 * 如果是null或者空字符串，返回false，否则返回true
	 *
	 * @return
	 */
	public static boolean isNotNullOrBlank(String value){
		if(value == null){
			return false;
		}else if("".equalsIgnoreCase(value.trim())){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 判断object：字符、collection、map及对象数组是否为空
	 * 空返回false；不为空返回true
	 *
	 * @return
	 */
	public static boolean isNotNullOrBlank(Object value){
		if (null == value){
			return false; 
		}
		else if (value instanceof CharSequence){
			return ((CharSequence) value).length() != 0;
		}
		else if (value instanceof Collection){
			return !((Collection) value).isEmpty();
		}
		else if (value instanceof Map) {
			return !((Map) value).isEmpty();
		}
		else if (value instanceof Object[]) {  
            Object[] object = (Object[]) value;  
            if (object.length == 0) {  
                return false;  
            } 
        }
		else if("".equalsIgnoreCase(value.toString().trim())){
			return false;
		}
		return true;
	}
   
    /**
     * 截取字符串给定子串
	 *
     * @param str 给定字符串，firstPos起始位置，lastPos最后位置
     * @return
     */
    public static String getSubstr(String str,int firstPos,int lastPos){
    	if(str.length()>0){
    		if(firstPos<0){
    			firstPos = 0;
    		}
    		if(lastPos>str.length()){
    			lastPos=str.length();
    		}
    		return str.substring(firstPos, lastPos);
    	}else{
    		return "";
    	}
    }
    
    /**
     * 字符串替换
     * 说明：只替换字符串开头、结尾或全部
	 *
     * @param str 被替换的字符串
     * @param substr 需要替换的子串
     * @param start 需要替换的起始位置，值为-1或0
     * 说明：若需要从结尾替换字符串，则start传值为 -1;
     * 	         若需要从开头替换字符串，则start传值为 0;
     * @return
     */
    public static String replace(String str,String substr,int start){
		StringBuffer buffer = new StringBuffer(str);
		String getStr = str;
    	if(start == -1){ //替换字符串结尾的字符
    		if(str.length() >= substr.length()){
    			getStr = buffer.replace(str.length()-substr.length(), str.length(), substr).toString();
    		}else{ //替换全部
    			getStr = substr;
    		}
    		
    	}else if(start == 0){ //从开头替换字符串
    		if(str.length() >= substr.length()){
    			getStr = buffer.replace(0, substr.length(), substr).toString();
    		}else{ //替换全部
    			getStr = substr;
    		}
    	}
    	return getStr;
    }

	/**
	 * 字符串去空格
	 *
	 * @param str
	 * @return
	 */
	public static String trim(String str){
    	if(str==null){
			return "";
		}
		return str.trim();
    }
    /**
     * 获取字符串对象的值(并去空)
	 *
     * @param strObject
     * @return
     */
    public static String getStrObject(Object strObject){
    	if(strObject==null){
			return null;
		}
		return strObject.toString().trim();
    }

	/**
	 * 字符串转Int
	 *
	 * @param str
	 * @return
	 */
	public static Integer getInt(String str){
    	if(str==null || "".equals(str.trim())){
    		return null;
    	}
    	try{
    		return Double.valueOf(str.trim()).intValue();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return null;
    }
    
    public static boolean isEmpty(String str) {
		return str == null ? true : str.trim().length() <= 0;
	}
    
    public static boolean isEmpty(Object str) {
		return !isNotNullOrBlank(str);
	}
    
    public static String getString(int m){
    	return m+"";
    }
    
    public static String getString(Object m){
    	if(m != null){
    		return m.toString();
    	}
    	return null;
    }

	/**
	 * 获取指定字符串出现的次数
	 *
	 * @param srcText 源字符串
	 * @param findText 要查找的字符串
	 * @return
	 */
    public static int appearNumber(String srcText, String findText) {
        int count = 0;
        Pattern p = Pattern.compile(findText);
        Matcher m = p.matcher(srcText);
        while (m.find()) {
            count++;
        }
        return count;
    }
    
    /**
	 * 获取字符串MD5值
	 *
	 * @param str 字符串
	 * @return
	 */
	public static String getStringMD5(String str) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strByte = str.getBytes();
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(strByte);
			byte[] digestByteArray = digest.digest();
			int length = digestByteArray.length;
			char array[] = new char[length * 2];
			int index = 0;
			for (int i = 0; i < length; i++) {
				byte _byte = digestByteArray[i];
				array[index++] = hexDigits[_byte >>> 4 & 0xf];
				array[index++] = hexDigits[_byte & 0xf];
			}
			return new String(array);
		} catch (Exception e) {
			return null;
		}
	}

}
