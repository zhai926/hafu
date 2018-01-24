package com.hafu.aop;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hafu.entity.HfAlert;
import com.hafu.entity.HfHealthPoint;

@Component
@Aspect
public class SendAlertMessageAop {
	private static Logger log = LoggerFactory.getLogger(SendAlertMessageAop.class);
//	报警推送端口(product)
	private static String sendUrl = "http://localhost:9002";
	
	
	/**
	 * 保存报警后置增强
	 */
	@After("execution(public * com.hafu.service.HfAlertService.save(..))")
	public void sendMessage(JoinPoint jointPoint){
		
			try {
				List<Object> args = Arrays.asList(jointPoint.getArgs());
				HfAlert al = null;
				if(args!=null && args.size()>0){
					al = (HfAlert) args.get(0);
				}
				if(al!=null){
					JSONObject jsonObject = null;
					String requestUrl = sendUrl+"/sendMessage/alert?alertId="+al.getId();
					StringBuffer buffer = new StringBuffer();
					URL url = new URL(requestUrl);
					HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
					httpUrlConn.setDoOutput(true);
					httpUrlConn.setDoInput(true);
					httpUrlConn.setUseCaches(false);
					// 设置请求方式（GET/POST）
					httpUrlConn.setRequestMethod("GET");
					httpUrlConn.connect();
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
					if(StringUtils.isEmpty(buffer.toString())){
						jsonObject = null;
						log.error("send message ,but no result");
					}
					else{
						jsonObject = JSONObject.fromObject(buffer.toString());
						if(jsonObject.getString("status").equals("error")){
							log.error(jsonObject.getString("msg"));
						}else{
							log.info("send alert message success");
						}
					}
				}
			} catch (ConnectException ce) {
				log.error("sendmessage server connection timed out.");
				ce.printStackTrace();
			} catch (Exception e) {
				log.error("https request error:{}", e);
				e.printStackTrace();
			}
		
	}
	
	public String getPort() {
		return sendUrl;
	}
	
	
	/**
	 * 健康报告后置增强
	 */
	/*@After("execution(public * com.hafu.service.HfHealthPointService.save(..))")*/
	public void sendHealthReportMessage(JoinPoint jointPoint){
		
			try {
				List<Object> args = Arrays.asList(jointPoint.getArgs());
				HfHealthPoint al = null;
				if(args!=null && args.size()>0){
					al = (HfHealthPoint) args.get(0);
				}
				if(al!=null){
					JSONObject jsonObject = null;
					String requestUrl = sendUrl+"/sendMessage/healthReport?healthId="+al.getId();
					StringBuffer buffer = new StringBuffer();
					URL url = new URL(requestUrl);
					HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
					httpUrlConn.setDoOutput(true);
					httpUrlConn.setDoInput(true);
					httpUrlConn.setUseCaches(false);
					// 设置请求方式（GET/POST）
					httpUrlConn.setRequestMethod("GET");
					httpUrlConn.connect();
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
					if(StringUtils.isEmpty(buffer.toString())){
						jsonObject = null;
						log.error("send healthReport message ,but no result");
					}
					else{
						jsonObject = JSONObject.fromObject(buffer.toString());
						if(jsonObject.getString("status").equals("error")){
							log.error(jsonObject.getString("msg"));
						}else{
							log.info("send healthReport message success");
						}
					}
				}
			} catch (ConnectException ce) {
				log.error("sendmessage healthReport server connection timed out.");
				ce.printStackTrace();
			} catch (Exception e) {
				log.error("https request error:{}", e);
				e.printStackTrace();
			}
		
	}
	
}
