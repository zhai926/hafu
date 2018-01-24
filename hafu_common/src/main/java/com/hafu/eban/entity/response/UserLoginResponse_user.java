package com.hafu.eban.entity.response;

import java.io.Serializable;

public class UserLoginResponse_user implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String 	id;	//id
	private String user_id;	//账号ID
	private String nickname;	//昵称
	private String code;	//设备号
	private String avatar;	//头像
	private String name;	//姓名
	private String gender;	//性别
	private String birthday;	///生日
	private String weight;	//体重
	private String height;	//身高
	private String mobile;	//手机号码
	private String casehistory;	//既往病史
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
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
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
