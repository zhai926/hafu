package com.hafu.eban.entity;

public class BoundParam {
	
	private String code;	//设备编码
	private String name;	//真实名字
	private String nickname;//被关注用户名昵称
	private String height;	//身高
	private String gender;	//性别
	private String weight;	//体重
	private String mobile;	//手机号
	private String casehistory;//既往病史
	private String birthday;//生日
	
	public String toUrlString(){
		return "?code="+code+"&name="+name+"&nickname="+nickname+"&height="+height+""
				+ "&gender="+gender+"&weight="+weight+"&mobile="+mobile+""
						+ "&casehistory="+casehistory+"&birthday="+birthday;
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
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
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
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

}
