package com.hafu.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 行为记录表
 */
@Entity
@Table(name="hf_behavior_record")
public class HfBehaviorRecord extends IdEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long userId; //用户id
	private String  code; //设备号	
	private Date  startTime; //开始时间（显示 小时：分钟）
	private Date endTime;//结果时间（显示 小时：分钟）
	private String  behaviorType; //行为类型（如：行走，睡眠，活动）
	private Date createDate;//创建时间
	private Integer deleteFlag; //删除标志(0:未删除，1:已删除)
	//业务字段
	private String dicItemName; //字典名称
	
	private Date updateDate;//更新时间
	
	private String bhType;//动作类型
	
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
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getBehaviorType() {
		return behaviorType;
	}
	public void setBehaviorType(String behaviorType) {
		this.behaviorType = behaviorType;
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
	
	@Transient
	public String getDicItemName() {
		return dicItemName;
	}
	public void setDicItemName(String dicItemName) {
		this.dicItemName = dicItemName;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
    public String getBhType() {
        return bhType;
    }
    public void setBhType(String bhType) {
        this.bhType = bhType;
    }
}
