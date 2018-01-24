package com.hafu.weixin;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * 微信开发共同信息
 * @author zhang
 *
 */
public class WeixinCommon {
	
	public static final ResourceBundle BUNDLE = PropertyResourceBundle.getBundle("smsconfig");
	
	/**
	 * 开发者凭据AppId
	 */
	public final static String APPID = BUNDLE.getString("WEIXIN_APPID");//"wxed9c58702c7f93c5";

	/**
	 * 开发者凭据Secret
	 */
	public final static String APPSECRET = "fd6ebf73b5ea8daba2ab641e31ab46ec";
	
	/**
	 * Access Token
	 */
	public final static String TOKEN = "hafu365";
	
	/**
	 * 生产url
	 */
	public final static String MENU_BASE = "http://fb.hafu365.com";
	
}
