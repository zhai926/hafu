package com.hafu.weixin.message.resquest;
/**
 * 文本消息类
 * @author Dong
 *
 */
public class TextMessage extends UserBaseMessage {
	// 消息内容
	private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

}
