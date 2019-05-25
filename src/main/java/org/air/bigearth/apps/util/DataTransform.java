package org.air.bigearth.apps.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.*;


/**
 * 数据转换工具类
 *
 * @author wangxuming
 * @version 1.0
 * @date 2018-12-17
 */
public class DataTransform {

	/**
	 * 从mapParam中替换key为replaceParam的key的值为replaceParam的值
	 *
	 * @param mapParam 源业务参数Map
	 * @param replaceParam 要替换的Map，如将
	 * @return Map<String,String>
	 */
	public static Map<String, String> handleMapParam(Map<String, String> mapParam, Map<String, String> replaceParam) {
		Map<String, String> newParam = new HashMap<String, String>();
				
//		if (null == replaceParam || replaceParam.size() <= 0) {
			newParam.putAll(mapParam);
//		}
		for (String key : replaceParam.keySet()) {
			if (mapParam.containsKey(key)) {
				newParam.put(key, replaceParam.get(key));
			}
		}
		return newParam;
	}
	
	/**
	 * 从mapParam中替换key为replaceParam的key的值为replaceParam的值
	 *
	 * @param mapParam 源业务参数Map
	 * @param replaceParam 要替换的Map，如将
	 * @return Map<String,Object>
	 */
	public static Map<String, Object> handleObjMapParam(Map<String, Object> mapParam, Map<String, Object> replaceParam) {
		Map<String, Object> newParam = new HashMap<String, Object>();
				
//		if (null == replaceParam || replaceParam.size() <= 0) {
			newParam.putAll(mapParam);
//		}
		for (String key : replaceParam.keySet()) {
			if (mapParam.containsKey(key)) {
				newParam.put(key, replaceParam.get(key));
			}
		}
		return newParam;
	}
	
	/**
	 * 取mapParam集合和replaceParam集合的并集及两个集合具有相同key值的replaceParam集合的value值
	 *
	 * @param mapParam 源业务参数Map
	 * @param replaceParam 要替换的Map，如将
	 * @return Map<String,Object>
	 */
	public static Map<String, String> handleCallBackMapParam(Map<String, String> mapParam, Map<String, String> replaceParam) {
		Map<String, String> newParam = new HashMap<String, String>();
		
		newParam.putAll(mapParam);
		
		for (String key : replaceParam.keySet()) {
			newParam.put(key, replaceParam.get(key));
		}
		return newParam;
	}
	
	/**
	 * 验证resultMap集合中是否含有key为paramKey的值，有就输出
	 *
	 * @param resultMap 目标集合
	 * @param paramKey key
	 * @return String
	 */
	public static String getParamValByMap(Map<String, Object> resultMap, String paramKey) {
		if (null == resultMap || resultMap.size() <= 0) {
			return "";
		}
		if (resultMap.containsKey(paramKey)) {
			return (String) resultMap.get(paramKey);
		}
		return "";
	}
	
	/**
	 * 验证resultMap集合中是否含有key为paramKey的值，有就输出
	 *
	 * @param resultMap 目标集合
	 * @param paramKey key
	 * @return String
	 */
	public static String getParamValByStrMap(Map<String, String> resultMap, String paramKey) {
		if (null == resultMap || resultMap.size() <= 0) {
			return "";
		}
		if (resultMap.containsKey(paramKey)) {
			return resultMap.get(paramKey);
		}
		return "";
	}

	/**
	 * 通过key查询map集合中map的键为key的值,如果集合为空或者集合大小大于1，返回空
	 *
	 * @param lstTragetDocParams
	 * @param key
	 * @return String
	 */
	public static String getParamValByLstMap(List<Map<String, Object>> lstTragetDocParams, String key) {
		if (null != lstTragetDocParams && lstTragetDocParams.size() == 1) {
			return getParamValByMap(lstTragetDocParams.get(0), key);
		}
		return "";
	}
	
	/**
	 * 将jsonArray字符串转为listMap格式数据
	 *
	 * @param str
	 * @return List<Map<String,Object>>
	 */
	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> strToLstMap(String str) throws JSONException {
		JSONArray array = strToJSONArray(str);
		return JSONArray.toList(array, new HashMap<String, Object>(), new JsonConfig());
	}
	
	/**
	 * 将字符串转为JSONArray格式数据
	 *
	 * @param str
	 * @return JSONArray
	 */
	public static JSONArray strToJSONArray(Object str) {
		if (null == str || StringUtils.isEmpty(str.toString())) {
			return new JSONArray();
		}
		JSONArray array = JSONArray.fromObject(str);
		return array;
	}
	
	/**
	 * 将字符串转为JSONObject格式数据
	 *
	 * @param str
	 * @return JSONObject
	 */
	public static JSONObject strToJSONObject(Object str) {
		if (null == str || StringUtils.isEmpty(str.toString())) {
			return new JSONObject();
		}
		JSONObject jsonObject = JSONObject.fromObject(str);
		return jsonObject;
	}
	
	/**
	 * 把jsonArray的字符串转换成List<Map<String, Object>>
	 *
	 * @param jsonArrayStr
	 * @return List<Map<String,Object>>
	 */
	public static List<Map<String, Object>> parseJsonArrayStrToListForMaps(String jsonArrayStr) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			if (jsonArrayStr != null) {
				JSONArray jsonArray = JSONArray.fromObject(jsonArrayStr);
				Map<String, Object> map = null;
				for (int j = 0; j < jsonArray.size(); j++) {
					JSONObject jsonObject = jsonArray.getJSONObject(j);
					map = parseJsonObjectStrToMap(jsonObject.toString());
					if (map != null) {
						list.add(map);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (list.size() == 0) {
			return null;
		}
		return list;
	}
	
	
	 /**
	  * 把JsonObject的字符串转换成Map<String, Object>
	  *
	  * @param jsonObjectStr
	  * @return Map<String,Object>
	  */
	public static Map<String, Object> parseJsonObjectStrToMap(String jsonObjectStr) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (jsonObjectStr != null) {
				JSONObject jsonObject = JSONObject.fromObject(jsonObjectStr);
				for (int j = 0; j < jsonObject.size(); j++) {
					Iterator<String> iterator = jsonObject.keys();
					while (iterator.hasNext()) {
						String key = iterator.next();
						Object value = jsonObject.get(key);
						map.put(key, value);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (map.size() == 0) {
			return null;
		}
		return map;
	}
	
	/**
	 * 将字符串转为Map格式数据
	 *
	 * @param str
	 * @return Map<String,Object>
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> strToMap(String str) throws JSONException {
		if (StringUtils.isEmpty(str)) {
			return new HashMap<String, Object>();
		}
		JSONObject obj = JSONObject.fromObject(str);
		return (Map<String, Object>)obj;
	}
	
	/**
	 * 比较两个map相同key所对应的value是否相等,用map的entrySet()的增强型for循环
	 *
	 * @param map1
	 * @param map2
	 * @return boolean
	 */
	public static boolean compareStrMap(Map<String, String> map1, Map<String, String> map2) {
		// 若两个map中的大小不一致
		if (map1.size() != map2.size()) {
			return false;
		}
		for (Map.Entry<String, String> entry1 : map1.entrySet()) {
			String value1 = StringUtils.isEmpty(entry1.getValue()) ? "" : entry1.getValue();
			// 若两个map中key不一样
			if (!map2.containsKey(entry1.getKey())) {
				return false;
			}
			String value2 = StringUtils.isEmpty(map2.get(entry1.getKey())) ? "" : map2.get(entry1.getKey());
			// 若两个map中相同key对应的value不相等
			if (!value1.equals(value2)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 比较两个map相同key所对应的value是否相等,用map的entrySet()的增强型for循环
	 *
	 * @param map1
	 * @param map2
	 * @return boolean
	 */
	public static boolean compareObjMap(Map<String, Object> map1, Map<String, Object> map2) {
		// 若两个map中的大小不一致
		if (map1.size() != map2.size()) {
			return false;
		}
		for (Map.Entry<String, Object> entry1 : map1.entrySet()) {
			String value1 = entry1.getValue() == null ? "" : entry1.getValue().toString();
			// 若两个map中key不一样
			if (!map2.containsKey(entry1.getKey())) {
				return false;
			}
			String value2 = map2.get(entry1.getKey()) == null ? "" : map2.get(entry1.getKey()).toString();
			// 若两个map中相同key对应的value不相等
			if (!value1.equals(value2)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 从多条结果集中获取信息，并将特殊字段值（batchId）抹掉，并添加一个子数据的批次ID作为明细批次ID
	 *
	 * @param //lstRelationResults 多条结果集
	 * @return Map<String,Object>
	 */
	/*public static Map<String, Object> getCommonRealteResult(List<Map<String, Object>> lstRelateResults) {
		Map<String, Object> relationResult = new HashMap<String, Object>();
		if (null != lstRelateResults && lstRelateResults.size() > 0) {
			relationResult.putAll(lstRelateResults.get(0));
			if (lstRelateResults.size() > 1) {
				relationResult.put("batchId", "");
				relationResult.put("detailDocID", lstRelateResults.get(0).get("batchId"));
				String busiParam = EfmisCommonUtils.getParamValByMap(relationResult, "busiParam"); // 获取业务参数（String类型）
				Map<String, String> replaceParam = new HashMap<String, String>();
				replaceParam.put("batchID", "");
				Map<String, String> newMapParam = EfmisCommonUtils.handleMapParam(CommonUtil.handleStrToMap(busiParam), replaceParam);
				relationResult.put("busiParam", CommonUtil.handleStrMapToStr(newMapParam));
			}
		}
		return relationResult;
	}*/
	
	public static List<String> getUserIdsByParam(List<String> userIdList){
		List<String> userList = new ArrayList<String>();
		for(String userIds : userIdList){
			userList.addAll(Arrays.asList(userIds.split(",")));
		}
		return userList;
	}

	/**
	 * 将request数据映射为map
	 *
	 * @param request
	 * @return
	 */
	public static Map<String, String> requestToMap(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		//request.getParameterMap()返回的是一个Map类型的值，该返回值记录着前端（如jsp页面）所提交请求中的请求参数和请求参数值的映射关系。这个返回值有个特别之处——只能读。
		Map parameterMap = request.getParameterMap();
		//Map.Entry是Map声明的一个内部接口，此接口为泛型，定义为Entry。它表示Map中的一个实体（一个key-value对）。接口中有getKey(),getValue方法。
		Iterator<Map.Entry<String, Object>> it = parameterMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> en = it.next();
			String value = request.getParameter(en.getKey());
			try {
				map.put(en.getKey(), URLDecoder.decode(value, "utf-8"));
			} catch (Exception e) {

			}
		}

		return map;
	}

	/**
	 * 将request数据映射为map
	 *
	 * @param request
	 * @return
	 */
	public static Map<String, Object> requestToObjectMap(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		//request.getParameterMap()返回的是一个Map类型的值，该返回值记录着前端（如jsp页面）所提交请求中的请求参数和请求参数值的映射关系。这个返回值有个特别之处——只能读。
		Map parameterMap = request.getParameterMap();
		//Map.Entry是Map声明的一个内部接口，此接口为泛型，定义为Entry。它表示Map中的一个实体（一个key-value对）。接口中有getKey(),getValue方法。
		Iterator<Map.Entry<String, Object>> it = parameterMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> en = it.next();
			String value = request.getParameter(en.getKey());
			try {
				map.put(en.getKey(), URLDecoder.decode(value, "utf-8"));
			} catch (Exception e) {

			}
		}

		return map;
	}

}
