package com.hafu.eban.entity.response;

import java.io.Serializable;

public class VoiceRespnse implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String success;
	private String message;
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getVoice() {
		return voice;
	}
	public void setVoice(String voice) {
		this.voice = voice;
	}
	private String voice;
	

}
