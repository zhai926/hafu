package com.hafu.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 运动数据
 */
@Entity
@Table(name="hf_sport_data")
public class HfSportData extends IdEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long userId; // 用户id INT(11) 11 是  
	private String code; //设备编号	
	private Date duration; // 时长(持续时间，格式：HH:mm:ss) DATET  是    
	private Integer pace; //  配速 INT(5) 5 是    
	private Integer kcal; //  大卡（能量单位） INT(5) 5 是    
	private Integer stepNumber; //  步数 INT(5) 5 是    
	private Integer km; //  公里（千米） INT(5) 5 是    
	private Date sportDate; //  运动日期（格式：yyyy-MM-dd） DATE  是    
	private Date createDate; //  创建时间 DATE  是 
	private Integer deleteFlag;//  删除标志 INT(2) 2 是 
	
	
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
	public Date getDuration() {
		return duration;
	}
	public void setDuration(Date duration) {
		this.duration = duration;
	}
	public Integer getPace() {
		return pace;
	}
	public void setPace(Integer pace) {
		this.pace = pace;
	}
	public Integer getKcal() {
		return kcal;
	}
	public void setKcal(Integer kcal) {
		this.kcal = kcal;
	}
	public Integer getStepNumber() {
		return stepNumber;
	}
	public void setStepNumber(Integer stepNumber) {
		this.stepNumber = stepNumber;
	}
	public Integer getKm() {
		return km;
	}
	public void setKm(Integer km) {
		this.km = km;
	}
	public Date getSportDate() {
		return sportDate;
	}
	public void setSportDate(Date sportDate) {
		this.sportDate = sportDate;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Integer getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	
	
	
}
