package com.hafu.eban.entity.response;

import java.io.Serializable;

public class DayInfoResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer success;
	private DayInfoResponse_dayIinfo day_info;
	
	public Integer getSuccess() {
		return success;
	}
	public void setSuccess(Integer success) {
		this.success = success;
	}
	public DayInfoResponse_dayIinfo getDay_info() {
		return day_info;
	}
	public void setDay_info(DayInfoResponse_dayIinfo day_info) {
		this.day_info = day_info;
	}

		
}
