package com.hafu.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * 检测数据表,包括（健康数据，身体数据，环境数据）
 */
@Entity
@Table(name="hf_check_data")
public class HfCheckData extends IdEntity implements Comparable<HfCheckData>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4093937710174411604L;
	
	private Long userId;//用户id
	private String code;
	private String checkCategory;//检查分类（主要包括：健康数据，身体数据，环境数据：引用数据字典）
	private String checkType;//检查类型（分类下的子栏目,如健康数据：血压、血脂、血糖、尿检，引用数据字典）
	private String checkData;//检查数据（对应类型的数据，如把血压中的舒张压、收缩压拼接到一块，以逗号为分割符保存）
	private Date checkDate;//检查时间(格式：yyyy-MM-dd)
	private String checkAddress;//检查地址
	private Date createDate;//创建时间
	private Integer deleteFlag;//删除标志（0：未删除，1：已删除）
	
	//逻辑
	private String dicItemName; //检测类别名
	
	
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
	public String getCheckCategory() {
		return checkCategory;
	}
	public void setCheckCategory(String checkCategory) {
		this.checkCategory = checkCategory;
	}
	public String getCheckType() {
		return checkType;
	}
	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}
	public String getCheckData() {
		return checkData;
	}
	public void setCheckData(String checkData) {
		this.checkData = checkData;
	}
	@DateTimeFormat(pattern="yyyy-MM-dd")
	public Date getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	public String getCheckAddress() {
		return checkAddress;
	}
	public void setCheckAddress(String checkAddress) {
		this.checkAddress = checkAddress;
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
	@Override
	public int compareTo(HfCheckData o) {
		// TODO Auto-generated method stub
		
		return checkDate.before(o.getCheckDate())?-1:1;
	}
	
}
