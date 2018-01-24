package com.hafu.eban.entity.response;

import java.io.Serializable;

public class DayRadarResponse_data_staticCount implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String static_count;
	private String color;
	public String getStatic_count() {
		return static_count;
	}
	public void setStatic_count(String static_count) {
		this.static_count = static_count;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	

}
