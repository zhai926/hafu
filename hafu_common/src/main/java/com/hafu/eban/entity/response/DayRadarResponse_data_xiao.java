package com.hafu.eban.entity.response;

import java.io.Serializable;

public class DayRadarResponse_data_xiao implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String xiao;
	private String color;
	public String getXiao() {
		return xiao;
	}
	public void setXiao(String xiao) {
		this.xiao = xiao;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	

	
}
