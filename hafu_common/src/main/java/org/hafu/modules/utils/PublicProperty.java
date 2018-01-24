package org.hafu.modules.utils;

import java.util.HashMap;
import java.util.Map;

public class PublicProperty {

	/**
	 * 生成6 位 随机数
	 */
	public static String generateRandomNum(int count) {
		StringBuffer codeBuilder = new StringBuffer();
		for (int i = 0; i < count; i++) {
			codeBuilder.append(Double.valueOf(Math.random() * 10).intValue());
		}
		return codeBuilder.toString();
	}
	//健康数据
	public static final String healthData = "JKSJ";
	//身体数据
	public static final String bodyData = "STSJ";
	//环境数据
	public static final String environmentData = "HJSJ";
	
	/**
	 * 健康数据类型
	 */
	public static final String healthData_XY ="JKSJ_XY";	//血压
	public static final String healthData_XZ ="JKSJ_XZ";	//血脂
	public static final String healthData_XT ="JKSJ_XT";	//血糖
	public static final String healthData_NJ ="JKSJ_NJ";	//尿检

	/**
	 * 身体数据类型
	 */
	public static final String bodyData_XL = "STSJ_XL";		//心率
	public static final String bodyData_SM = "STSJ_SM";		//睡眠
	public static final String bodyData_TN = "STSJ_TN";		//体能
	
	/**
	 * 环境数据类型
	 */
	public static final String environmentData_SD = "HJSJ_SD";		//湿度
	public static final String environmentData_ZY = "HJSJ_ZY";		//噪音
	
	public static final Map<String, String> INDEX_MODULE = new HashMap<String, String>();
	static{
		//a:b =  data: java
		INDEX_MODULE.put("INDEX_XWJL", "INDEX_XWJL");
		INDEX_MODULE.put("INDEX_YDGJ", "INDEX_YDGJ");
		INDEX_MODULE.put("INDEX_YDSJ", "INDEX_YDSJ");
		INDEX_MODULE.put("INDEX_TJSJ", "INDEX_TJSJ");
		INDEX_MODULE.put("INDEX_STSJ", "INDEX_STSJ");
		INDEX_MODULE.put("INDEX_HJSJ", "INDEX_HJSJ");
	}
}
