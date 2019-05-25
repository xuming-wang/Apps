package org.air.bigearth.apps.util;

import java.util.Comparator;

/**
 * 字符串比较工具类
 * 
 * @author wangxuming
 * @version 1.0
 * @date 2019-04-23
 */
public class StringComparator implements Comparator<String> {
	@Override
	public int compare(String string1, String string2) {
		if(string1.trim().length()<string2.trim().length()){
			return -1;
		}else if(string1.trim().length()>string2.trim().length()) {
			return 1;
		}else{
			return string1.compareTo(string2);
		}
	}
}
