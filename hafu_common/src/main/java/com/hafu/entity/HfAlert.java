package com.hafu.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 报警表
 */
@Entity
@Table(name="hf_alert")
public class HfAlert extends IdEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Long userId; //用户id
	private String  code; //设备号
	private String nickName; //昵称
	private String longitude; //经度
	private String  latitude; //维度
	private String content;//报警内容
	private Integer  alertType; //报警类型(1：自动报警，2：手动报警等..)
	private Date alertDate; //报警时间
	private String position;//报警时间 
	private Integer deleteFlag; //删除标志(0:未删除，1:已删除)
	private Date createDate;
	private Integer readStatus; //读取状态
	
	//业务字段
	private String mobile;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
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
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
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
	public Integer getAlertType() {
		return alertType;
	}
	public void setAlertType(Integer alertType) {
		this.alertType = alertType;
	}
	
	public Date getAlertDate() {
		return alertDate;
	}
	public void setAlertDate(Date alertDate) {
		this.alertDate = alertDate;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public Integer getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	public Integer getReadStatus() {
		return readStatus;
	}
	public void setReadStatus(Integer readStatus) {
		this.readStatus = readStatus;
	}
	@Transient
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	
			
}
