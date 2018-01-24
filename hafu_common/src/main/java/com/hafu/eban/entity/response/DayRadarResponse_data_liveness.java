package com.hafu.eban.entity.response;

import java.io.Serializable;

public class DayRadarResponse_data_liveness implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String liveness;
	private String color;
	public String getLiveness() {
		return liveness;
	}
	public void setLiveness(String liveness) {
		this.liveness = liveness;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
}
