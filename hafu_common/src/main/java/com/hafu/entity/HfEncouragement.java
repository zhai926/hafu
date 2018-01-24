package com.hafu.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 鼓励表
 */
@Entity
@Table(name="hf_encouragement")
public class HfEncouragement extends IdEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long sendUserId; // 发送鼓励的用户id INT(11) 11 是    
	private Long receiveUserId; // 接收鼓励的用户id INT(11) 11 是    
	private String  code; //设备号	
	private Integer encouragementType; //  鼓励类型（1：鼓掌，2：献花，3：拥抱） INT(2) 2 是    
	private Date sendDate;//发送时间
	private Integer sendStatus;//发送状态(0:发送成功，1;发送失败)
	private Date createDate; // 创建时间 DATETIME  是    
	private Integer deleteFlag; //  删除标志 INT(2) 
	//业务
	private HfUser sendUser;
	
	
	public Date getSendDate() {
		return sendDate;
	}
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	public Integer getSendStatus() {
		return sendStatus;
	}
	public void setSendStatus(Integer sendStatus) {
		this.sendStatus = sendStatus;
	}
	public Long getSendUserId() {
		return sendUserId;
	}
	public void setSendUserId(Long sendUserId) {
		this.sendUserId = sendUserId;
	}
	public Long getReceiveUserId() {
		return receiveUserId;
	}
	public void setReceiveUserId(Long receiveUserId) {
		this.receiveUserId = receiveUserId;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public Integer getEncouragementType() {
		return encouragementType;
	}
	public void setEncouragementType(Integer encouragementType) {
		this.encouragementType = encouragementType;
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
	@ManyToOne
	@JoinColumn(insertable=false,updatable=false,name="sendUserId")
	public HfUser getSendUser() {
		return sendUser;
	}
	public void setSendUser(HfUser sendUser) {
		this.sendUser = sendUser;
	}
	
	
	

}
