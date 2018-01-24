package com.hafu.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 語音
 */
@Entity
@Table(name="hf_voice")
public class HfVoice extends IdEntity {

	private static final long serialVersionUID = 1L;
	
	private Long userId;	//用户id
	private String code;	//设备号
	private String voiceMsg;	//语音内容
	private Integer source;	// 语音来源 （0：发送，1：接收）
	private String voicePath;	//语音路径
	private String voiceUrl;	//语音网络路径
	private String serverId;	//语音ID
	private Date receiveDate;	// 接收时间
	private Date createDate;	//创建时间
	private Integer deleteFlag;	//删除标志(0:未删除，1:删除)
	private Integer duration;	//时长
	private Integer isRead;	//是否已阅读
	private Long voiceSpeakerId;//手机用户id 说话人的id

	@Transient          
	private Integer length;
	@Transient
	private String time;
	
	
	
	
	public Long getVoiceSpeakerId() {
		return voiceSpeakerId;
	}
	public void setVoiceSpeakerId(Long voiceSpeakerId) {
		this.voiceSpeakerId = voiceSpeakerId;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
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
	public String getVoiceMsg() {
		return voiceMsg;
	}
	public void setVoiceMsg(String voiceMsg) {
		this.voiceMsg = voiceMsg;
	}
	public Integer getSource() {
		return source;
	}
	public void setSource(Integer source) {
		this.source = source;
	}
	public String getVoicePath() {
		return voicePath;
	}
	public void setVoicePath(String voicePath) {
		this.voicePath = voicePath;
	}
	public Date getReceiveDate() {
		return receiveDate;
	}
	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
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
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public String getVoiceUrl() {
		return voiceUrl;
	}
	public void setVoiceUrl(String voiceUrl) {
		this.voiceUrl = voiceUrl;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public Integer getIsRead() {
		return isRead;
	}
	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}

}
