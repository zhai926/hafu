package com.hafu.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hafu.timer.TimerUtilCollection;
import com.hafu.weixin.CoreService;
import com.hafu.weixin.WeixinCommon;
import com.hafu.weixin.menu.MenuManager;
import com.hafu.weixin.menu.pojo.AccessToken;
import com.hafu.weixin.util.SignUtil;
import com.hafu.weixin.util.WeixinUtil;

/**
 * 微信业务类
 */
@Controller
@RequestMapping(value = "/weixin")
public class WeixinController {
	// LOG日志	
	private static Logger log = LoggerFactory.getLogger(WeixinController.class);
	/**
	 * 根据请求方式执行相对应的业务
	 */
	@RequestMapping(value = "")
	public void weixinService(HttpServletRequest request,HttpServletResponse response) throws Exception {
		// 获取请求的方式
		String methodStyle = request.getMethod();
		// 如果为get请求
		if ("get".equalsIgnoreCase(methodStyle)) {
			// 响应get 验证token
			this.verificationToken(request, response);
		} else if ("post".equalsIgnoreCase(methodStyle)) {
			// 执行post请求
			this.post(request, response);
		}
	}
	
	/**
	 * 执行post请求
	 * 
	 * @throws IOException
	 */
	private void post(HttpServletRequest request,HttpServletResponse response) throws IOException {
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		// 调用核心业务类接收消息、处理消息
		String respMessage = "";// CoreService.processRequest(request, response);

		// 响应消息
		PrintWriter out = response.getWriter();
		out.print(respMessage);
		out.close();
	}
	
	/**
	 * 响应get 验证token的方法
	 * 
	 * @throws IOException
	 */
	private void verificationToken(HttpServletRequest request,HttpServletResponse response) throws IOException {
		// 加密签名
		String signature = request.getParameter("signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		// 随机字符串
		String echostr = request.getParameter("echostr");
		
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		// response返回
		PrintWriter out = response.getWriter();
		
		// 验证签名 
		if (SignUtil.checkSignature(signature, timestamp, nonce)) {
			out.write(echostr);
		}
		
		out.flush();
		out.close();
		
	}
	
	/**
	 * 手动创建菜单
	 * @throws IOException
	 */
	@RequestMapping(value = "menuCreate",method = RequestMethod.GET)	
	public void menuMain(HttpServletRequest request,HttpServletResponse response) throws IOException {
		// 获取access_token
		AccessToken at = TimerUtilCollection.accessToken;
		// access_token不为空
		if (at != null) {
			// 调用接口创建菜单
			int result = WeixinUtil.createMenu(MenuManager.getMenu(), at.getToken());
	
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			// 判断菜单创建结果
			if (0 == result) {
				out.write("菜单创建成功！");
			} else {
				out.write("菜单创建失败，错误码：" + result);
			}
			out.flush();
			out.close();
		}
	}
	
	/**
	 * 通过code获取用户信息
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "getUserInfo/{where}", method= RequestMethod.GET)
	public String getUserInfo(@PathVariable("where") int where, HttpServletRequest request, HttpServletResponse response) throws IOException {
/*		// 获取code
		String code = request.getParameter("code");
		
		// 如果用户不允许授权
		if (StringUtils.isBlank(code)) {
			return "";
		}
		
		// 通过code换取网页授权access_token链接
		String webTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
		webTokenUrl = webTokenUrl.replace("APPID", WeixinCommon.APPID).replace("SECRET", WeixinCommon.APPSECRET)
				.replace("CODE", code);
		log.error("换取的code::"+code);
		log.error(webTokenUrl+"获取access_token请求，包含openId");
		// 获取json形式的返回结果
		JSONObject tokenJson = WeixinUtil.doHttpsRequest(webTokenUrl, "GET", null);
		
		// 获取用户openID
		String openId = tokenJson.getString("openid");
		log.error(tokenJson.toString()+":::::::"+openId);
		// 放入cookie保存
		WeixinUtil.saveCookieByOpenid(openId, response);
		
		switch(where) {
			case 1: return "redirect:/index/index";	// 首页		
			case 2: return "redirect:/health/dataCompare";//数据中心
			case 3: return "redirect:/index/device_user_list"; //设置
			default: return "";
		}*/
		
		// 获取code
		String openId = request.getParameter("openId");
		// 如果用户不允许授权
		if (StringUtils.isBlank(openId)) {
			return "";
		}
		log.error("微信openId:::::::"+openId);
		// 放入cookie保存
		//WeixinUtil.saveCookieByOpenid(openId, response);
		//放入session保存
		request.getSession().setAttribute("openId", openId);
		request.getSession().setAttribute("openId_flag", "1");
		switch(where) {
			case 1: return "redirect:/index/index";	// 首页		
			case 2: return "redirect:/health/dataCompare";//数据中心
			case 3: return "redirect:/index/device_user_list"; //设置
			default: return "";
		}
		
		
	}
	
}
