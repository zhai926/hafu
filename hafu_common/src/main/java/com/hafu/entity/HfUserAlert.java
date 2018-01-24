package com.hafu.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name="hf_user_alert")
public class HfUserAlert extends IdEntity{
	
	/**
	 * 
	 */
	private Long userId;
	private Long concernUserId;
	@Length(max=20)
	private String code;
	private Long alertId;
	private Integer readStatus;
	private Date createDate;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getConcernUserId() {
		return concernUserId;
	}
	public void setConcernUserId(Long concernUserId) {
		this.concernUserId = concernUserId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Long getAlertId() {
		return alertId;
	}
	public void setAlertId(Long alertId) {
		this.alertId = alertId;
	}
	public Integer getReadStatus() {
		return readStatus;
	}
	public void setReadStatus(Integer readStatus) {
		this.readStatus = readStatus;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	
	
}
