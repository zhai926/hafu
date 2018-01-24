package com.hafu.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 用户与绑定设备关系表。 
 * 用户与用户关系均通过该表管理查询
 */
@Entity
@Table(name="hf_concern_device")
public class HfConcernDevice extends IdEntity{
	
	private static final long serialVersionUID = 1L;
	private Long concernUserId; //被关注人id
	private String code; //添加设备的人的id ;	
	private Date createDate; //创建时间	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Long getConcernUserId() {
		return concernUserId;
	}
	public void setConcernUserId(Long concernUserId) {
		this.concernUserId = concernUserId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
	
			
}
