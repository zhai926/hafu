package com.hafu.eban.entity.response;

import java.io.Serializable;

public class DayInfoResponse_dayIinfo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String mask1;
	private String location;
	private String point_today;
	private String step_sum;
	private String step_freq;
	private String valid_wear;
	
	public String getMask1() {
		return mask1;
	}
	public void setMask1(String mask1) {
		this.mask1 = mask1;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getPoint_today() {
		return point_today;
	}
	public void setPoint_today(String point_today) {
		this.point_today = point_today;
	}
	public String getStep_sum() {
		return step_sum;
	}
	public void setStep_sum(String step_sum) {
		this.step_sum = step_sum;
	}
	public String getStep_freq() {
		return step_freq;
	}
	public void setStep_freq(String step_freq) {
		this.step_freq = step_freq;
	}
	public String getValid_wear() {
		return valid_wear;
	}
	public void setValid_wear(String valid_wear) {
		this.valid_wear = valid_wear;
	}
	
	
}
