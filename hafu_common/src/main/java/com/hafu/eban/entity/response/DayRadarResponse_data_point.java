package com.hafu.eban.entity.response;

import java.io.Serializable;

public class DayRadarResponse_data_point implements Serializable {

	private static final long serialVersionUID = 1L;

	private String point;
	private String color;
	public String getPoint() {
		return point;
	}
	public void setPoint(String point) {
		this.point = point;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
}
