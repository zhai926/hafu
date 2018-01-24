package com.hafu.eban.entity.response;

import java.io.Serializable;
import java.util.List;

public class MessageAlertResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private String success;
	private String message;
	private List<MessageAlertResponse_alert> alert;
	private MessageAlertResponse_alertCount alert_count;
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
	public List<MessageAlertResponse_alert> getAlert() {
		return alert;
	}
	public void setAlert(List<MessageAlertResponse_alert> alert) {
		this.alert = alert;
	}
	public MessageAlertResponse_alertCount getAlert_count() {
		return alert_count;
	}
	public void setAlert_count(MessageAlertResponse_alertCount alert_count) {
		this.alert_count = alert_count;
	}	

}
