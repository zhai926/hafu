package com.hafu.eban.entity.response;

import java.io.Serializable;

public class DayWeatherResponse implements Serializable{

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

	
}
