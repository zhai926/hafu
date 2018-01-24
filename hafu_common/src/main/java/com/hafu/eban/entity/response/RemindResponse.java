package com.hafu.eban.entity.response;

import java.io.Serializable;
import java.util.List;

public class RemindResponse implements Serializable{

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
	public String getRemind_id() {
		return remind_id;
	}
	public void setRemind_id(String remind_id) {
		this.remind_id = remind_id;
	}
	public List<RemindResponse_list> getList() {
		return list;
	}
	public void setList(List<RemindResponse_list> list) {
		this.list = list;
	}
	private String remind_id;
	private List<RemindResponse_list> list;

}
