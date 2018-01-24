package com.hafu.weixin.message.resquest;
/**
 * 图片消息
 * @author Dong
 *
 */
public class ImageMessage extends UserBaseMessage{
	private String PicUrl; // 图片地址

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}
}
