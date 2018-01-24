package com.hafu.eban.entity.response;

import java.io.Serializable;
import java.util.List;

public class BoundResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private String success;
	private String message;
	private List<BoundResponse_bound> res;
	private List<BoundResponse_bound> myFollow;

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

	public List<BoundResponse_bound> getRes() {
		return res;
	}

	public void setRes(List<BoundResponse_bound> res) {
		this.res = res;
	}

	public List<BoundResponse_bound> getMyFollow() {
		return myFollow;
	}

	public void setMyFollow(List<BoundResponse_bound> myFollow) {
		this.myFollow = myFollow;
	}

}
