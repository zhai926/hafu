package com.hafu.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 健康评分表
 */
@Entity
@Table(name="hf_health_point")
public class HfHealthPoint extends IdEntity {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long userId; //  用户id INT(11) 11 是    
	private String  code; //设备号		
	private Integer stepCount; //  行走步数 INT(11) 11 是    
	private Double stepLength;//步长   
	private Integer stepSpeed;//歩速
	private Integer staticCount; //  久坐次数 INT(11) 11 是    
	private Double liveness; //  活跃度 DOUBLE  是    
	private Integer fatigue;//疲劳度
	private Integer activity;//活动得分
	private Integer rest;//作息得分
	private Integer point;//综合得分
	
	private Integer dateType; // 日期类型（0：日，1：周，2：月） INT(2) 2 是    
	private Date createDate; //  创建日期 DATE  是    
	private Date updateDate; //  修改日期 DATE  是    
	private Integer deleteFlag; //  删除标志（0:未删除，1：已删除） INT(2) 2 是 
	
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public Integer getStepCount() {
		return stepCount;
	}
	public void setStepCount(Integer stepCount) {
		this.stepCount = stepCount;
	}
	public Integer getStaticCount() {
		return staticCount;
	}
	public void setStaticCount(Integer staticCount) {
		this.staticCount = staticCount;
	}
	
	public Double getStepLength() {
		return stepLength;
	}
	public void setStepLength(Double stepLength) {
		this.stepLength = stepLength;
	}
	public Integer getStepSpeed() {
		return stepSpeed;
	}
	public void setStepSpeed(Integer stepSpeed) {
		this.stepSpeed = stepSpeed;
	}
	public Integer getFatigue() {
		return fatigue;
	}
	public void setFatigue(Integer fatigue) {
		this.fatigue = fatigue;
	}
	public Integer getActivity() {
		return activity;
	}
	public void setActivity(Integer activity) {
		this.activity = activity;
	}
	public Integer getRest() {
		return rest;
	}
	public void setRest(Integer rest) {
		this.rest = rest;
	}
	public Integer getPoint() {
		return point;
	}
	public void setPoint(Integer point) {
		this.point = point;
	}
	public Double getLiveness() {
		return liveness;
	}
	public void setLiveness(Double liveness) {
		this.liveness = liveness;
	}
	public Integer getDateType() {
		return dateType;
	}
	public void setDateType(Integer dateType) {
		this.dateType = dateType;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Integer getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}


	
}
