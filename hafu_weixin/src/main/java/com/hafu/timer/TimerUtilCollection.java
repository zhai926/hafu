package com.hafu.timer;

import javax.annotation.PostConstruct;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hafu.weixin.WeixinCommon;
import com.hafu.weixin.menu.pojo.AccessToken;
import com.hafu.weixin.menu.pojo.JsTicket;
import com.hafu.weixin.util.WeixinUtil;

/**
 * 定时工具类集合
 * @author zhang
 *
 */
@Component
public class TimerUtilCollection {
	
	// 获取access_token的接口地址（GET）
	public static String access_token_url = "http://wx.hafu365.com/auth/getToken?appid=APPID";
	//"https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	
	//获取js ticket 的借口地址（get）
	public static String js_ticket_url = "http://wx.hafu365.com/auth/getTicket?appid=APPID";
	//"https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
	
	
	// LOG日志	
	private static Logger log = LoggerFactory.getLogger(TimerUtilCollection.class);
	
	// access_token
	public static AccessToken accessToken = null;
	//js ticket
	public static JsTicket jsTicket = null;
	
	//@PostConstruct当bean加载完之后，就会执行init方法
	@PostConstruct
	public void init() {
		accessToken = getToken();
		jsTicket = getTicket();
	}
	
	/**
	 * 每隔5秒获取一次access_token
	 */
	@Scheduled(cron = "0/5 * * * * ?")
	public void getAccessToken() {
		accessToken = getToken();
		jsTicket = getTicket();
	}
	
	/**
	 * 每隔两小时获取一次jsapi_ticket
	 */
	//@Scheduled(cron = "0 0 */2 * * ?")
	/*public void getJsTicket() {
		jsTicket = getTicket();
	}*/
	
	/**
	 * 获取aceess_token
	 * @return
	 */
	private AccessToken getToken() {
		// 返回值初期化
		AccessToken token = null;
		
		// 请求地址
		String requestUrl = access_token_url.replace("APPID", WeixinCommon.APPID);
				//access_token_url.replace("APPID", WeixinCommon.APPID).replace("APPSECRET", WeixinCommon.APPSECRET);
	    // 请求返回结果
		//JSONObject jsonObject = WeixinUtil.doHttpsRequest(requestUrl, "GET", null);
		String jsonObject = WeixinUtil.doHttpRequestString(requestUrl, "GET", null);
	    // 如果请求成功 
	    if (jsonObject != null) {
	        try {
	        	token = new AccessToken();
	        	token.setToken(jsonObject);
                log.error("Get token success ! taken is :" + token.getToken() + " Expires in : " + token.getExpiresIn() + " sec");
	        } catch (JSONException e) {
	            // 获取token失败  
	            //log.error("get token failer errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));  
	        }  
	    }else{
			log.error(" accesstoken can not get"); 
		}
	    
	    return token;
	}
	
	
	/**
	 * 获取js ticket
	 * @return
	 */
	private JsTicket getTicket() {
		// 返回值初期化
		JsTicket ticket = null;
		if(accessToken!=null && accessToken.getToken()!=null){
			// 请求地址
			String requestUrl = js_ticket_url.replace("APPID", WeixinCommon.APPID);
					//js_ticket_url.replace("ACCESS_TOKEN", accessToken.getToken());
		    // 请求返回结果
			//JSONObject jsonObject = WeixinUtil.doHttpsRequest(requestUrl, "GET", null);
			String jsonObject = WeixinUtil.doHttpRequestString(requestUrl, "GET", null);
		    // 如果请求成功 
		    if (jsonObject != null) {
		        try {
		        	ticket = new JsTicket();
		        	ticket.setTicket(jsonObject);
                    log.error("Get ticket success! ticket is :" + ticket.getTicket() + " Expires in : " + ticket.getExpiresIn() + " sec");
		        } catch (JSONException e) {
		            // 获取token失败  
		            //log.error("get ticket failer errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));  
		        }  
		    }
		}else{
			log.error("can not get js ticket because accesstoken can not get"); 
		}
	    return ticket;
	}
	
	
}
