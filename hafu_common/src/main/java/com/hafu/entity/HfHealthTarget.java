package com.hafu.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 健康目标
 */
@Entity
@Table(name = "hf_health_target")
public class HfHealthTarget extends IdEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long userId; // 用户id INT(11) 11 是
	private String  code; //设备号		
	private Double totalActivity;//
	private Integer stepCount;// 步数（步态） INT(5) 5 是
	private Integer staticCount; // 静止次数(体态) INT(5) 5 是
	private Double sleepTime;// 睡眠时间
	private Integer liveness; // 活动次数(活跃度) INT(3) 3 是
	private Integer dataType;// 日期类型（0：日，1：周，2：月）
	private Date createDate;// 创建日期
	private Date updateDate;// 修改日期
	private Integer deleteFlag;// 删除标志（0:未删除，1：已删除）
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
	public Double getTotalActivity() {
		return totalActivity;
	}
	public void setTotalActivity(Double totalActivity) {
		this.totalActivity = totalActivity;
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
	public Double getSleepTime() {
		return sleepTime;
	}
	public void setSleepTime(Double sleepTime) {
		this.sleepTime = sleepTime;
	}
	public Integer getLiveness() {
		return liveness;
	}
	public void setLiveness(Integer liveness) {
		this.liveness = liveness;
	}
	public Integer getDataType() {
		return dataType;
	}
	public void setDataType(Integer dataType) {
		this.dataType = dataType;
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
