package com.hafu.eban.entity.response;

import java.io.Serializable;

public class MessageConutResponse implements Serializable{

	
	private static final long serialVersionUID = 1L;

	private String success;
	private String alert;
	private String voice;
	
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getAlert() {
		return alert;
	}
	public void setAlert(String alert) {
		this.alert = alert;
	}
	public String getVoice() {
		return voice;
	}
	public void setVoice(String voice) {
		this.voice = voice;
	}
	
}
