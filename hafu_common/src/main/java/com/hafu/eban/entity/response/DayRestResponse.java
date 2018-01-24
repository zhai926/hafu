package com.hafu.eban.entity.response;

import java.io.Serializable;

public class DayRestResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String success;
	private String message;
	private String msg;
	private String corlor;
	private String detail_rest;
	private String voicecount;
	private String alert;
	private String xcount;
	
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
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getCorlor() {
		return corlor;
	}
	public void setCorlor(String corlor) {
		this.corlor = corlor;
	}
	public String getDetail_rest() {
		return detail_rest;
	}
	public void setDetail_rest(String detail_rest) {
		this.detail_rest = detail_rest;
	}
	public String getVoicecount() {
		return voicecount;
	}
	public void setVoicecount(String voicecount) {
		this.voicecount = voicecount;
	}
	public String getAlert() {
		return alert;
	}
	public void setAlert(String alert) {
		this.alert = alert;
	}
	public String getXcount() {
		return xcount;
	}
	public void setXcount(String xcount) {
		this.xcount = xcount;
	}

}
