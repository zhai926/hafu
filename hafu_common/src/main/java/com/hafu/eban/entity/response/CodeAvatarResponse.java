package com.hafu.eban.entity.response;

import java.io.Serializable;

public class CodeAvatarResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String success;
	private String message;
	private String avatar;
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
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
}
