package com.hafu.eban.entity.response;

import java.io.Serializable;

public class DayRadarResponse_data_stepCount implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String step_count;
	private String color;
	public String getStep_count() {
		return step_count;
	}
	public void setStep_count(String step_count) {
		this.step_count = step_count;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}

}
