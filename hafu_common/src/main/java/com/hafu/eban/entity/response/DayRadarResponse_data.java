package com.hafu.eban.entity.response;

import java.io.Serializable;

public class DayRadarResponse_data implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String step_sum;
	private String step_freq;
	private String sit_long;
	private String sleep_time;
	private String liveness;
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
	public String getSit_long() {
		return sit_long;
	}
	public void setSit_long(String sit_long) {
		this.sit_long = sit_long;
	}
	public String getSleep_time() {
		return sleep_time;
	}
	public void setSleep_time(String sleep_time) {
		this.sleep_time = sleep_time;
	}
	public String getLiveness() {
		return liveness;
	}
	public void setLiveness(String liveness) {
		this.liveness = liveness;
	}
	
	/*private DayRadarResponse_data_xiao xiao;
	private DayRadarResponse_data_point point;
	private DayRadarResponse_data_staticCount static_count;
	private DayRadarResponse_data_liveness liveness;
	private DayRadarResponse_data_stepCount step_count;
	public DayRadarResponse_data_xiao getXiao() {
		return xiao;
	}
	public void setXiao(DayRadarResponse_data_xiao xiao) {
		this.xiao = xiao;
	}
	public DayRadarResponse_data_point getPoint() {
		return point;
	}
	public void setPoint(DayRadarResponse_data_point point) {
		this.point = point;
	}
	public DayRadarResponse_data_staticCount getStatic_count() {
		return static_count;
	}
	public void setStatic_count(DayRadarResponse_data_staticCount static_count) {
		this.static_count = static_count;
	}
	public DayRadarResponse_data_liveness getLiveness() {
		return liveness;
	}
	public void setLiveness(DayRadarResponse_data_liveness liveness) {
		this.liveness = liveness;
	}
	public DayRadarResponse_data_stepCount getStep_count() {
		return step_count;
	}
	public void setStep_count(DayRadarResponse_data_stepCount step_count) {
		this.step_count = step_count;
	}*/

}
