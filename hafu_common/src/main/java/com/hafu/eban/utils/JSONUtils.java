package com.hafu.eban.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hafu.eban.entity.response.DayInfoResponse;

public class JSONUtils {

	private static Logger logger = Logger.getLogger(JSONUtils.class);
	static ObjectMapper objectMapper;

	private JSONUtils() {
	}

	static {
		getInstance();
	}

    public static ObjectMapper getInstance() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            // 去掉默认的时间戳格式
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            // 设置为中国上海时区
            objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
            // 空值不序列化
            objectMapper.setSerializationInclusion(Include.NON_NULL);
            // 反序列化时，属性不存在的兼容处理
            objectMapper.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            // 序列化时，日期的统一格式
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            // 单引号处理
            objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            //空字串不序列化
            objectMapper.setSerializationInclusion(Include.NON_EMPTY);
            objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        }
        return objectMapper;
    }

	/**
	 * 如果JSON字符串为Null或"null"字符串,返回Null. 如果JSON字符串为"[]",返回空集合.
	 * 
	 * 如需读取集合如List/Map,且不是List<String>这种简单类型时使用如下语句: List<MyBean> beanList =
	 * JSONUtils.getInstance().readValue("[]",new TypeReference<List<MyBean>>()
	 * {}); TypeFactory typeFactory = JSONUtils.getInstance().getTypeFactory();
	 * CollectionType collectionType =
	 * typeFactory.constructCollectionType(List.class, MyBean.class);
	 * List<MyBean> beanList =
	 * JSONUtils.getInstance().readValue(resultMsg,collectionType);
	 */
	public static <T> T fromJson(String jsonString, Class<T> clazz) {
		if (jsonString == null || jsonString.equals("")
				|| jsonString.equals("null")) {
			return null;
		}

		try {
			return objectMapper.readValue(jsonString, clazz);
		} catch (Exception e) {
			logger.error("parse json string error:" + jsonString, e);
			return null;
		}
	}

	/**
	 * 如果对象为Null,返回"null". 如果集合为空集合,返回"[]".
	 */
	public static String toJson(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (Exception e) {
			logger.warn("write to json string error:" + object, e);
			return null;
		}
	}

	/**
	 * json string convert to map
	 */
	@SuppressWarnings("unchecked")
	public static <T> Map<String, Object> json2map(String jsonStr)
			throws Exception {
		return objectMapper.readValue(jsonStr, Map.class);
	}

	/**
	 * json string convert to map with javaBean
	 */
	public static <T> Map<String, T> json2map(String jsonStr, Class<T> clazz)
			throws Exception {
		Map<String, Map<String, Object>> map = objectMapper.readValue(jsonStr,
				new TypeReference<Map<String, T>>() {
				});
		Map<String, T> result = new HashMap<String, T>();
		for (Entry<String, Map<String, Object>> entry : map.entrySet()) {
			result.put(entry.getKey(), map2pojo(entry.getValue(), clazz));
		}
		return result;
	}

	/**
	 * json array string convert to list with javaBean
	 */
	public static <T> List<T> json2list(String jsonArrayStr, Class<T> clazz)
			throws Exception {
		List<Map<String, Object>> list = objectMapper.readValue(jsonArrayStr,
				new TypeReference<List<T>>() {
				});
		List<T> result = new ArrayList<T>();
		for (Map<String, Object> map : list) {
			result.add(map2pojo(map, clazz));
		}
		return result;
	}

	/**
	 * map convert to javaBean
	 */
	public static <T> T map2pojo(Map map, Class<T> clazz) {
		return objectMapper.convertValue(map, clazz);
	}
	
	public static void main(String[] args) {
		//String str = "﻿{\"success\":1,\"day_info\":{\"mask1\":\"#Dot|0-5217|在家,收拾,#none|5217-27762|永泰路金谊河畔,未佩戴,#Dot|27762-30187|在家,休息,#none|30187-33885|永泰路金谊河畔,未佩戴,#Dot|33885-35529|在家,休息,#none|35529-62829|永泰路金谊河畔,未佩戴,#Dot|62829-79419|在家,休息,#Dot|79419-86399|在家,休息,\",\"location\":\"#Dot|121.515346|31.140384|永泰路金谊河畔|15|1|17798\",\"point_today\":\"41\",\"step_sum\":\"182\",\"step_freq\":\"0\"}}";
		String str =  "﻿{\"success\":0}";
		String str1 = "{\"success\":0}";
		System.out.println(str +"   "+str1);
		DayInfoResponse dayInfoResponse = fromJson(str.substring(1, str.length()), DayInfoResponse.class);
		System.out.println(dayInfoResponse.getDay_info());
	
	}
}
