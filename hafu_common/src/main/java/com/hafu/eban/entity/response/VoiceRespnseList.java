package com.hafu.eban.entity.response;

import java.io.Serializable;
import java.util.List;

public class VoiceRespnseList implements Serializable{

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
	public List<VoiceResponse_voceList> getVoice() {
		return voice;
	}
	public void setVoice(List<VoiceResponse_voceList> voice) {
		this.voice = voice;
	}
	private List<VoiceResponse_voceList> voice;
	

}
