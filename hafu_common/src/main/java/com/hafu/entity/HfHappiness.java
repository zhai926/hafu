package com.hafu.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * 记录被关注人用户  快乐指数 表
 * 
 * @author 翟会林
 *
 */
@Entity
@Table(name="hf_happiness")
public class HfHappiness extends IdEntity {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String code;//设备编号
	private Long userId; //用户id
	private Date createDate;//记录时间
	private Date endDate;//记录结束时间
	private Integer happiness;//快乐指数
	private String createDay;//创建日期
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Integer getHappiness() {
		return happiness;
	}
	public void setHappiness(Integer happiness) {
		this.happiness = happiness;
	}
	public String getCreateDay() {
		return createDay;
	}
	public void setCreateDay(String createDay) {
		this.createDay = createDay;
	}
	
	
	
	
}
