package com.hafu.weixin.message.response;


/**
 * 音乐消息  公众平台 --> 普通用户
 * 
 * @author Dong
 * 
 */
public class MusicMessage extends ServerBaseMessage {

	// 音乐
	private Music Music;

	public Music getMusic() {
		return Music;
	}

	public void setMusic(Music music) {
		Music = music;
	}

}
