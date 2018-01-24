package com.hafu.weixin.message.resquest;

/**
 * 语音消息类
 * 
 * @author Dong
 * 
 */
public class VoiceMessage extends UserBaseMessage {
	// 媒体ID
	private String MediaId;
	// 语音格式
	private String Format;

	public String getMediaId() {
		return MediaId;
	}

	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}

	public String getFormat() {
		return Format;
	}

	public void setFormat(String format) {
		Format = format;
	}

}
