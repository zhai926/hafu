package com.hafu.eban.entity.response;

import java.io.Serializable;

public class DayRadarResponse_sevn implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String count;
	private String stotal_activity;
	private String stotal_sport;
	private String sstep_count;
	private String sliveness;
	private String sstep_length;
	private String sstep_speed;
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getStotal_activity() {
		return stotal_activity;
	}
	public void setStotal_activity(String stotal_activity) {
		this.stotal_activity = stotal_activity;
	}
	public String getStotal_sport() {
		return stotal_sport;
	}
	public void setStotal_sport(String stotal_sport) {
		this.stotal_sport = stotal_sport;
	}
	public String getSstep_count() {
		return sstep_count;
	}
	public void setSstep_count(String sstep_count) {
		this.sstep_count = sstep_count;
	}
	public String getSliveness() {
		return sliveness;
	}
	public void setSliveness(String sliveness) {
		this.sliveness = sliveness;
	}
	public String getSstep_length() {
		return sstep_length;
	}
	public void setSstep_length(String sstep_length) {
		this.sstep_length = sstep_length;
	}
	public String getSstep_speed() {
		return sstep_speed;
	}
	public void setSstep_speed(String sstep_speed) {
		this.sstep_speed = sstep_speed;
	}

}
