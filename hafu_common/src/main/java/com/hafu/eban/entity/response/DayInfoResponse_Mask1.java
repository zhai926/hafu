package com.hafu.eban.entity.response;

import java.io.Serializable;

public class DayInfoResponse_Mask1 implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String attribute;	//属性
	private String startTime;	//开始时间
	private String endTime;	//结束时间
	private String behaviorType;	//行为类型
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getBehaviorType() {
		return behaviorType;
	}
	public void setBehaviorType(String behaviorType) {
		this.behaviorType = behaviorType;
	}

}
