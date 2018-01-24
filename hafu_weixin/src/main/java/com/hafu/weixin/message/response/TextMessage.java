package com.hafu.weixin.message.response;

/**
 * 文本消息 公众平台 --> 普通用户
 * @author Dong
 *
 */
public class TextMessage extends ServerBaseMessage {
	// 回复的消息内容  
    private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
    
    
    

}
