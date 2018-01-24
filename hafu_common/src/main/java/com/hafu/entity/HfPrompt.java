package com.hafu.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 关爱提醒
 */
@Entity
@Table(name="hf_prompt")
public class HfPrompt extends IdEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long userId; //  用户id INT(11) 11 是    
	private String code; //设备编号 
	private Integer hour; //  小时(上午：hour<=12,下午：12  INT(2) 2 是    
	private Integer minute; // 分钟 INT(2) 2 是    
	private Integer  isPrompt; // 是否提醒(0:提醒，1:不提醒) INT(2) 2 是    
	private Integer  promptType; // 提醒类型：0无 1睡觉 2起床 3活动 4休息 5吃药
	private String loopString; // 循环字符串（中文周一到周日转化成对应英文，以逗号为分隔符，type=2时特有数据） VARCHAR(100) 100 是    
	private String content; //  内容 VARCHAR(200) 200 是    
	private String cronExpression;// 执行时间，动态生成 VARCHAR(100) 100 是    
	private Date createDate; // 创建时间 DATE  是    
	private Date updateDate; // 修改时间 DATE  是    
	private Integer deleteFlag; // 删除标志(0:未删除，1:已删除) INT(2) 2 是 
	private Integer repeatType;//重复方式：0不重复 1按天重复 2按周重复 3按月重复 4按年重复
	
	
	
	public Integer getRepeatType() {
		return repeatType;
	}
	public void setRepeatType(Integer repeatType) {
		this.repeatType = repeatType;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Integer getHour() {
		return hour;
	}
	public void setHour(Integer hour) {
		this.hour = hour;
	}
	public Integer getMinute() {
		return minute;
	}
	public void setMinute(Integer minute) {
		this.minute = minute;
	}
	public Integer getIsPrompt() {
		return isPrompt;
	}
	public void setIsPrompt(Integer isPrompt) {
		this.isPrompt = isPrompt;
	}
	public Integer getPromptType() {
		return promptType;
	}
	public void setPromptType(Integer promptType) {
		this.promptType = promptType;
	}
	public String getLoopString() {
		return loopString;
	}
	public void setLoopString(String loopString) {
		this.loopString = loopString;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCronExpression() {
		return cronExpression;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
}
