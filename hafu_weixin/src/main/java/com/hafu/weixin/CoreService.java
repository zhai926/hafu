package com.hafu.weixin;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hafu.weixin.message.response.TextMessage;
import com.hafu.weixin.util.MessageUtil;

/**
 * 核心服务类
 * @author Dong
 *
 */
public class CoreService {
	
	// 日志log对象
	private static Logger log = LoggerFactory.getLogger(CoreService.class); 
	
	/**
	 * 处理微信发来的请求
	 * 
	 * @param request
	 * @param cardBo 
	 * @return
	 */
	public static String processRequest(HttpServletRequest request, HttpServletResponse response) {
		// 返回值
		String respMessage= "";
		
		// 默认返回的文本消息内容
		String respContent = "感谢您关注哈福！";
					
		try {
			// xml请求解析
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			
			// 开发者微信号
			String toUserName = requestMap.get("ToUserName");
			
			// 发送方帐号(用户OpenID)
			String fromUserName = requestMap.get("FromUserName");
			log.info("fromUser openId is " + fromUserName);
			
			// 消息类型
			String msgType = requestMap.get("MsgType");
			log.info("msgType is " + msgType);
			
			// 默认回复文本消息
			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
			textMessage.setFuncFlag(0); 
			
			textMessage.setContent(respContent);
			respMessage = MessageUtil.textMessageToXml(textMessage);
			
			// 消息类型为推送型时
			if (MessageUtil.REQ_MESSAGE_TYPE_EVENT.equalsIgnoreCase(msgType)) {
				// 事件类型
				String event = requestMap.get("Event");
				log.info("event:" + event);
				
				// 事件类型为订阅时
				if (MessageUtil.EVENT_TYPE_SUBSCRIBE.equalsIgnoreCase(event)) {
//					// 创建图文消息  
//					NewsMessage newsMessage = new NewsMessage(); 
//					newsMessage.setToUserName(fromUserName);  
//					newsMessage.setFromUserName(toUserName);
//					newsMessage.setCreateTime(new Date().getTime());
//					newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
//					newsMessage.setFuncFlag(0); 
//					
//					List<Article> articleList = new ArrayList<Article>();  
//					
//					// 单图文消息
//					Article article1 = new Article();
//					article1.setDescription("");
//					article1.setTitle("感谢您关注理财圈！");
//					article1.setPicUrl(WeixinCommon.MENU_BASE + "/static/images/bizconf.png");
//					article1.setUrl("http://mp.weixin.qq.com/s?__biz=MjM5MTQ5MjIyMg==&mid=201705338&idx=1&sn=24d3360e6749061de9a434d1981a13e8&scene=18#rd");
//					
//					Article article2 = new Article();
//                    article2.setTitle("【我的账号】 提供账号绑定功能");  
//                    article2.setDescription("");  
//                    // 将图片置为空  
//                    article2.setPicUrl("");  
//                    article2.setUrl(WeixinCommon.MENU_BASE + "/my_account/user/firstPage");  
//					
//                    Article article3 = new Article();  
//                    article3.setTitle("【WebEx会议】 您可以在线预约会议");  
//                    article3.setDescription("");  
//                    // 将图片置为空  
//                    article3.setPicUrl("");  
//                    article3.setUrl(WeixinCommon.MENU_BASE + "/webex_meeting/firstPage");
//					
//                    Article article4 = new Article();  
//                    article4.setTitle("【电话会议】 您可以实时召开和控制会议");  
//                    article4.setDescription("");  
//                    // 将图片置为空  
//                    article4.setPicUrl("");  
//                    article4.setUrl(WeixinCommon.MENU_BASE + "/phone_meeting/firstPage");
//					
//                    articleList.add(article1);
//                    articleList.add(article2);
//                    articleList.add(article3);
//                    articleList.add(article4);
//                    
//                    newsMessage.setArticleCount(articleList.size());
//                    newsMessage.setArticles(articleList);
//                    
//                    respMessage = MessageUtil.newsMessageToXml(newsMessage);
					
					// 事件类型为取消订阅时
				} else if (MessageUtil.EVENT_TYPE_UNSUBSCRIBE.equalsIgnoreCase(event)) {
					// 清楚cookie中保存的值
					/*Cookie cookie = new Cookie("openId",null);
					cookie.setMaxAge(0);
					response.addCookie(cookie);*/
					//清空session的值
					request.getSession().setAttribute("openId",null);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return respMessage;
		
	}
	
}

