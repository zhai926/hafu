package com.hafu.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 
	* <p>Title: HfPropmtLog</p>
	* <p>Description: 关爱提醒操作实体类</p>
	* <p>Company: 嘉善网络</p>
	* @author 时志多
	* @date 2016年7月6日上午10:27:41
 */
@Entity
@Table(name="hf_prompt_log")
public class HfPromptLog extends IdEntity{

	private static final long serialVersionUID = 1L;
	
	private Long promptId;	//promptId
	private String code;		//设备编号
	private String content;		//关爱提醒内容
	private Long remindTime;	//提醒时间
	private Long expiredTime;	//过期时间
	private Date createTime; 	//创建时间
	private Date updateTime;	//更新时间
	private Integer deleteFlag;	//是否删除 0未删除，1删除
	private String repateType;	//提醒类型
	private Date remindDate;	//提醒日期
	private Integer ebanId;		//eban方关爱提醒ID，用于删除
	public Long getPromptId() {
		return promptId;
	}
	public void setPromptId(Long promptId) {
		this.promptId = promptId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Long getRemindTime() {
		return remindTime;
	}
	public void setRemindTime(Long remindTime) {
		this.remindTime = remindTime;
	}
	public Long getExpiredTime() {
		return expiredTime;
	}
	public void setExpiredTime(Long expiredTime) {
		this.expiredTime = expiredTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	public String getRepateType() {
		return repateType;
	}
	public void setRepateType(String repateType) {
		this.repateType = repateType;
	}
	public Date getRemindDate() {
		return remindDate;
	}
	public void setRemindDate(Date remindDate) {
		this.remindDate = remindDate;
	}
	public Integer getEbanId() {
		return ebanId;
	}
	public void setEbanId(Integer ebanId) {
		this.ebanId = ebanId;
	}
	

}
