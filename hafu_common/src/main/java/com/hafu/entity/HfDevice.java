package com.hafu.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 设备表（外设表）
 */
@Entity
@Table(name="hf_device")
public class HfDevice extends IdEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long userId;//设备拥有者id 
	private String code;//设备编号
	private Date createDate;//创建时间
	private Integer deleteFlag;//是否删除
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
