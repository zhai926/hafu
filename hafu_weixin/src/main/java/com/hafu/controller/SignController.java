package com.hafu.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.ParseException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hafu.timer.TimerUtilCollection;
import com.hafu.weixin.WeixinCommon;
import com.hafu.weixin.menu.pojo.JsTicket;

/**
 * JSSDK 签名
 * */
@Controller
@RequestMapping("/wechatjs")
public class SignController {
	public static Map<String, String> ticketMap = new HashMap<String, String>();
	private static Logger logger = Logger.getLogger(SignController.class);
	private String appid = WeixinCommon.APPID;

	@RequestMapping(value = "/jsConfig", method = RequestMethod.POST)
	public void JSconfig(HttpServletRequest request,
			HttpServletResponse response) throws ParseException, IOException,
			java.text.ParseException {
		Map<String, String> ret = null;
		String url = request.getParameter("url");
		logger.info("url = " + url);
		Base64 base64 = new Base64();
		url = new String(base64.decode(url));
		JsTicket jsTicket = TimerUtilCollection.jsTicket;
		String jsapi_ticket = jsTicket.getTicket();
		try {
			ticketMap.put("time", new Date().getTime() + "");
			ticketMap.put("ticket", jsapi_ticket);
			ret = sign(jsapi_ticket, url);
			ret.put("appid", appid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject json = JSONObject.fromObject(ret);
		try {
			PrintWriter out = response.getWriter();
			out.write(json.toString());
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Map<String, String> sign(String jsapi_ticket, String url) {
		Map<String, String> ret = new HashMap<String, String>();
		String nonce_str = create_nonce_str();
		String timestamp = create_timestamp();
		String string1;
		String signature = "";
		// 注意这里参数名必须全部小写，且必须有序
		string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str
				+ "&timestamp=" + timestamp + "&url=" + url;

		logger.info("string1 =" + string1);
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(string1.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		logger.info(" signature = " + signature);
		ret.put("url", url);
		ret.put("jsapi_ticket", jsapi_ticket);
		ret.put("nonceStr", nonce_str);
		ret.put("timestamp", timestamp);
		ret.put("signature", signature);

		return ret;
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	private static String create_nonce_str() {
		return UUID.randomUUID().toString();
	}

	private static String create_timestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}
}
