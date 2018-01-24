package org.hafu.modules.utils;

import java.io.Serializable;
import java.util.Properties;

public class PropertiesUtil implements Serializable {
	private static final long serialVersionUID = 1L;
	private static PropertiesUtil instance  = null;
	private Properties props 				= null;
	private static String classPath 		= "common.properties";
	
	
	
	/**
	 * 获取PropertiesUtil实例
	 * @return
	 */
	public static PropertiesUtil getInstance(String path) {
		classPath = path;
	    instance = new PropertiesUtil();
		return instance;
	}
	
	
	private PropertiesUtil(){
		refresh();
	}
	
	/*
	 * 装载properties文件
	 */
	
	private void refresh() {
		try {			
			props = new Properties(); 
			props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(classPath));
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	/**
	 * 跟据properties文件中的key值获取相对应的val值
	 * @param key
	 * @return
	 */
	public String getValue(String key){
		return props.getProperty(key);
	}

	
	public static void main(String[] args) {
		PropertiesUtil proUtil = PropertiesUtil.getInstance("common.properties");
		
		String requestToken = proUtil.getValue("token");
		
		System.out.println(requestToken);
	}
} 