package com.hafu.eban.entity.response;

import java.io.Serializable;

public class DayRadarResponse_rest implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String total_activity;
	private String step_count;
	private String static_count;
	private String step_length;
	private String step_speed;
	private String rest;
	private String activity;
	private String liveness;
	private String point;
	
	public String getTotal_activity() {
		return total_activity;
	}
	public void setTotal_activity(String total_activity) {
		this.total_activity = total_activity;
	}
	public String getStep_count() {
		return step_count;
	}
	public void setStep_count(String step_count) {
		this.step_count = step_count;
	}
	public String getStatic_count() {
		return static_count;
	}
	public void setStatic_count(String static_count) {
		this.static_count = static_count;
	}
	public String getStep_length() {
		return step_length;
	}
	public void setStep_length(String step_length) {
		this.step_length = step_length;
	}
	public String getStep_speed() {
		return step_speed;
	}
	public void setStep_speed(String step_speed) {
		this.step_speed = step_speed;
	}
	public String getRest() {
		return rest;
	}
	public void setRest(String rest) {
		this.rest = rest;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public String getLiveness() {
		return liveness;
	}
	public void setLiveness(String liveness) {
		this.liveness = liveness;
	}
	public String getPoint() {
		return point;
	}
	public void setPoint(String point) {
		this.point = point;
	}
}
