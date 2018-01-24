package com.hafu.weixin.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.hafu.modules.utils.Encodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hafu.timer.TimerUtilCollection;
import com.hafu.utils.AjaxResutObject;
import com.hafu.weixin.WeixinCommon;
import com.hafu.weixin.menu.pojo.AccessToken;
import com.hafu.weixin.menu.pojo.Menu;

/**
 * 公众平台通用接口工具类
 * @author Dong
 *
 */
public class WeixinUtil {
	
	// 菜单创建（POST）URL 
	public static String menu_create_url = " https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	
	//微信用户信息url
	public static String weixin_user_info = "http://wx.hafu365.com/auth/getUser?appid=APPID&openId=OPENID";
	//"https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	

	//下载多媒体文件 
	public static String download_media = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
	// LOG日志	
	private static Logger log = LoggerFactory.getLogger(WeixinUtil.class);

	/**
	 * 发起https请求并获取结果
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * @param outputStr
	 *            提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject doHttpsRequest(String requestUrl,
			String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			System.out.println(requestUrl+"获取access_token");
			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url
					.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (ConnectException ce) {
			log.error("Weixin server connection timed out.");
			ce.printStackTrace();
		} catch (Exception e) {
			log.error("https request error:{}", e);
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	/**
	 * 发起http请求并获取结果
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * @param outputStr
	 *            提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject doHttpRequest(String requestUrl,
			String requestMethod, String outputStr ) {
		log.info("start to send the request");
		
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url
					.openConnection();

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod)) {
				httpUrlConn.connect();
			}
             
			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			log.info(buffer.toString());
			if(StringUtils.isEmpty(buffer.toString()))
				jsonObject = null;
			else
				jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (ConnectException ce) {
			log.error("Weixin server connection timed out.");
			ce.printStackTrace();
		} catch (Exception e) {
			log.error("https request error:{}", e);
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	/**
	 * 返回string 
	 * @param requestUrl
	 * @param requestMethod
	 * @param outputStr
	 * @return
	 */
	public static String doHttpRequestString(String requestUrl,
			String requestMethod, String outputStr ) {
		log.info("start to send the request");
		
		String jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url
					.openConnection();

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod)) {
				httpUrlConn.connect();
			}
             
			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			log.info(buffer.toString());
			jsonObject = buffer.toString();
		} catch (ConnectException ce) {
			log.error("Weixin server connection timed out.");
			ce.printStackTrace();
		} catch (Exception e) {
			log.error("https request error:{}", e);
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	/**
	 * 创建菜单 
	 * 
	 * @param menu 菜单实例 
	 * @param accessToken 有效的access_token 
	 * @return 0表示成功，其他值表示失败 
	 */  
	public static int createMenu(Menu menu, String accessToken) {  
	    int result = 0;
	    
	    // 拼装创建菜单的url
	    String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);  
	    // 将菜单对象转换成json字符串
	    String jsonMenu = JSONObject.fromObject(menu).toString();
	    // 调用接口创建菜单
	    JSONObject jsonObject = doHttpsRequest(url, "POST", jsonMenu);  
	    if (jsonObject != null) { 
	        if (jsonObject.getInt("errcode") != 0) {  
	            result = jsonObject.getInt("errcode");  
	            log.error("创建菜单失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));  
	        } 
	    } 
	    
	    return result;  
	}
	
	/**
	 * 获取code
	 * @param request
	 * @param response
	 * @param where
	 * @throws IOException
	 */
	private static void getAuthOrizeCode(HttpServletRequest request, HttpServletResponse response,int where) throws IOException {
		// 用户同意授权，获取code URL
		/*String authOrizeUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=1#wechat_redirect";
		
		// 获取根路径
		String path = request.getContextPath();
		// 基础路径
		String basePath = request.getScheme() + "://"
				+ request.getServerName() + path + "/";
		
		authOrizeUrl = authOrizeUrl.replace("APPID", WeixinCommon.APPID).replace("REDIRECT_URI", URLEncoder.encode(basePath + "weixin/getUserInfo/" + where)).replace("SCOPE", "snsapi_userinfo");
		log.error("url address = " + authOrizeUrl);*/
		String authOrizeUrl = "http://wx.hafu365.com/auth/admin?p=REDIRECT_URI";
		
		// 获取根路径
		String path = request.getContextPath();
		// 基础路径
		String basePath = request.getScheme() + "://"
				+ request.getServerName() + path + "/";
		String redirectUrl = basePath + "weixin/getUserInfo/" + where+"?appid=APPID".replace("APPID", WeixinCommon.APPID);
		String baseRedirecUrl = Encodes.encodeUrlSafeBase64(redirectUrl.getBytes());
		authOrizeUrl = authOrizeUrl.replace("REDIRECT_URI", baseRedirecUrl);
		log.error("url address = " + authOrizeUrl);
		response.sendRedirect(authOrizeUrl);
	}
	
	/**
	 * 获取openID
	 * @param request
	 * @param response
	 * @param where
	 * @return
	 * @throws IOException
	 */
	public static String getOpenId(HttpServletRequest request,HttpServletResponse response, int where) throws IOException {
		// 从cookie中读取openId
		//String openId = getCookie("openId", request);	
		//从session取
		String openId = (String)request.getSession().getAttribute("openId");
		// 如果openId为空值
		if (StringUtils.isBlank(openId)) {
			// 重新获取
			request.getSession().removeAttribute("openId_flag");
			getAuthOrizeCode(request, response, where);
			// 从cookie中重新读取openId
			//openId = getCookie("openId", request);
			//从session里重新读取openId
			String openId_flag = (String) request.getSession().getAttribute("openId_flag");
			int time = 0;
			while(StringUtils.isEmpty(openId_flag)||(!openId_flag.equals("1"))){
				time++;
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				openId_flag = (String) request.getSession().getAttribute("openId_flag");
				if(time==6) break;
			}
			openId = (String)request.getSession().getAttribute("openId");
			log.error("get openId from weixin: " + openId);
		}else{
			log.error("get openId from session: " + openId);
		}
		
		return openId;
	}
	
	/**
	 * 获取微信用户的信息
	 * @param opendId
	 * @throws IOException
	 */
	public static JSONObject getWeixinUserInfo(String openId) throws IOException {
		/*AccessToken at = TimerUtilCollection.accessToken;
		String requestUrl = weixin_user_info.replace("ACCESS_TOKEN", at.getToken()).
				replace("OPENID",openId);
	    // 请求返回结果
		JSONObject jsonObject = WeixinUtil.doHttpsRequest(requestUrl, "GET", null);
		if(jsonObject==null || jsonObject.containsKey("errcode")){
			log.error("weixin get userInfo happen error,errorcode:"+jsonObject.getString("errorcode")+",msg:"+jsonObject.getString("errmsg"));
			return null;
		}
	    return jsonObject;*/
		log.error("用于获取用户信息access_token:"+TimerUtilCollection.accessToken.getToken()+"和openid:"+openId);
		String requestUrl = weixin_user_info.replace("APPID", WeixinCommon.APPID).
				replace("OPENID",openId);
	    // 请求返回结果
		JSONObject jsonObject = WeixinUtil.doHttpRequest(requestUrl, "GET", null);
		if(jsonObject==null ){
			log.error("weixin get userInfo happen error,errorcode:返回null");
			return null;
		}else if( jsonObject.containsKey("errcode")){
			log.error("weixin get userInfo happen error,errorcode:"+jsonObject.getString("errcode")+",msg:"+jsonObject.getString("errmsg"));
			return null;
		}
		log.error("<<<<<<<<<<<<<<变更用户使用>>>>>>>>>>>>>>>昵称："+String.valueOf(jsonObject.get("nickname"))+">>>>>>openid："+String.valueOf(jsonObject.get("openid"))+">>>>>>>headpic："+String.valueOf(jsonObject.get("headimgurl"))+">>>>>>>>>>>province："+String.valueOf(jsonObject.get("province")));
	    return jsonObject;
	}
	
	
	/**
	 * 将openid 保存至cookie
	 * 
	 * @param openid
	 */
	public static void saveCookieByOpenid(String openid, HttpServletResponse response) {
		Cookie cookie = new Cookie("openId", openid);
		cookie.setMaxAge(-1);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	
	/**
	 * 读取cookie
	 * 
	 * @param name
	 * @return
	 */
	public static String getCookie(String name, HttpServletRequest request) {
		// 获取cookie
		Cookie[] cs = request.getCookies();
		if (cs != null) {
			for (Cookie c : cs) {
				if (name.equals(c.getName().trim())) {
					return c.getValue();
				}
			}
		}

		return "";
	}
	

	/**
	 * 下载微信服务器文件到本地 服务器
	 * @param opendId
	 * @throws IOException
	 */
	public static AjaxResutObject downloadMedia(String serverId) {
		/*InputStream inputStream = null;
		try{
			AccessToken at = TimerUtilCollection.accessToken;
			String requestUrl = download_media.replace("ACCESS_TOKEN", at.getToken()).
					replace("MEDIA_ID",serverId);
		    // 请求返回结果
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url
					.openConnection();

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod("GET");
			httpUrlConn.connect();
			// 将返回的输入流转换成字符串
			inputStream = httpUrlConn.getInputStream();
			log.error("下载图片（"+at.getToken()+"）。。。。（"+serverId+"）"+inputStream.read());
		    return inputStream;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}*/
		
		AjaxResutObject ajax = new AjaxResutObject();
		byte[] result = null;
		HttpClient client = null;
		String fileName = "";
		try {
			AccessToken at = TimerUtilCollection.accessToken;
			String requestUrl = download_media.replace("ACCESS_TOKEN", at.getToken()).
					replace("MEDIA_ID",serverId);
			client = new DefaultHttpClient();
			HttpGet httpGet = getHttpGet(requestUrl, null, null);
			HttpResponse response = client.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				Header[] hs = response.getAllHeaders();
				for (Header header : hs) {
					if (header.getName().equals("Content-disposition")) {
						fileName = header.getValue().substring(
								header.getValue().indexOf("filename") + 10,
								header.getValue().length() - 1);
						break;
					}
				}
				result = EntityUtils.toByteArray(response.getEntity());
			} else {
				StringBuffer errorMsg = new StringBuffer();
				errorMsg.append("httpStatus:");
				errorMsg.append(response.getStatusLine().getStatusCode());
				errorMsg.append(response.getStatusLine().getReasonPhrase());
				errorMsg.append(", Header: ");
				Header[] headers = response.getAllHeaders();
				for (Header header : headers) {
					errorMsg.append(header.getName());
					errorMsg.append(":");
					errorMsg.append(header.getValue());
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				client.getConnectionManager().shutdown();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ajax.setMsg(fileName);
		ajax.setObByte(result);
		return ajax;
	}

	
	
	
	
	private static HttpGet getHttpGet(String url, Map<String, String> params,
			String encode) {
		StringBuffer buf = new StringBuffer(url);
		if (params != null) {
			String flag = (url.indexOf('?') == -1) ? "?" : "&";
			for (String name : params.keySet()) {
				buf.append(flag);
				buf.append(name);
				buf.append("=");
				try {
					String param = params.get(name);
					if (param == null) {
						param = "";
					}
					buf.append(URLEncoder.encode(param, encode));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				flag = "&";
			}
		}
		HttpGet httpGet = new HttpGet(buf.toString());
		return httpGet;
	}
	
	
}
