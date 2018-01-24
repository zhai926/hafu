package com.hafu.eban.entity.response;

import java.io.Serializable;

public class ProductInfoResponse_info implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String code;
	private String name;
	private String gender;
	private String birthday;
	private String height;
	private String weight;
	private String mobile;
	private String casehistory;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getCasehistory() {
		return casehistory;
	}
	public void setCasehistory(String casehistory) {
		this.casehistory = casehistory;
	}
	

}
