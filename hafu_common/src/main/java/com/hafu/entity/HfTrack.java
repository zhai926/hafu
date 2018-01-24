package com.hafu.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 运动轨迹表
 */
@Entity
@Table(name="hf_track")
public class HfTrack extends IdEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long userId; // 用户id INT(11) 11 是   
	private String  code; //设备号		
	private String longitude; //  经度 VARCHAR(30) 30 是    
	private String latitude;  // 纬度 (30) 30 是    
	private String trackType; //  轨迹类型（返回为中文类型） VARCHAR(30) 30 是    
	private Integer typeId; //  轨迹类型(数字类型) INT(4) 4 是    
	private Date alertDate; //  报警时间 DATETIME  是    
	private String position; //  报警位置 VARCHAR(200) 200 是    
	private String radius; //  准确度 VARCHAR(20) 20 是    
	private String coordinate; //  坐标系 VARCHAR(20) 20 是 
	private Integer deleteFlag;//  删除标志 INT(2) 2 是 
	private Date createDate;
	private Date updateDate;
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
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getTrackType() {
		return trackType;
	}
	public void setTrackType(String trackType) {
		this.trackType = trackType;
	}
	public Integer getTypeId() {
		return typeId;
	}
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
	public Date getAlertDate() {
		return alertDate;
	}
	public void setAlertDate(Date alertDate) {
		this.alertDate = alertDate;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getRadius() {
		return radius;
	}
	public void setRadius(String radius) {
		this.radius = radius;
	}
	public String getCoordinate() {
		return coordinate;
	}
	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}
	public Integer getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
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
	
}
