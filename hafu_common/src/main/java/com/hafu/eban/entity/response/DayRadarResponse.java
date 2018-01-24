package com.hafu.eban.entity.response;

import java.io.Serializable;

public class DayRadarResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private String success;
	//private DayRadarResponse_rest rest;
	private DayRadarResponse_data data;
	//private DayRadarResponse_sevn sevn;
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public DayRadarResponse_data getData() {
		return data;
	}
	public void setData(DayRadarResponse_data data) {
		this.data = data;
	}

}
