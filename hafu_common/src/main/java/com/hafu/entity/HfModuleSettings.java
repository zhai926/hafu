package com.hafu.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 首页设置
 */
@Entity
@Table(name="hf_module_settings")
public class HfModuleSettings extends IdEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long concernUserId;
	private String moduleCode;
	private Date createDate;
	
	
	public Long getConcernUserId() {
		return concernUserId;
	}
	public void setConcernUserId(Long concernUserId) {
		this.concernUserId = concernUserId;
	}
	public String getModuleCode() {
		return moduleCode;
	}
	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
	
}
