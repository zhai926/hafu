package com.hafu.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * 用户表
 */
@Entity
@Table(name="hf_user")
public class HfUser extends IdEntity{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name; //  登录名（帐户名称） VARCHAR(30) 30 是    
	private String realName; //  真实姓名 VARCHAR(20) 20 是    
	private String nickName; //  昵称 VARCHAR(20) 20 是    
	private String avater; // 头像(图片地址) VARCHAR(100) 100 是    
	private String gender; //  性别（男 女 保密） VARCHAR(5) 5 是    
	private String birthday; //  用户生日(2012-08-15) VARCHAR(30) 30 是    
	private Integer height; //  身高(cm) INT(3) 3 是    
	private Integer weight; //  体重(kg) INT(3) 3 是    
	private String mobile; //  手机号 VARCHAR(15) 15 是    
	 private String telephone; //  电话号 VARCHAR(15) 15 是    
	 private String email; //  用户邮箱 VARCHAR(30) 30 是    
	 private String address; //  用户地址 VARCHAR(100) 100 是    
	 private String country; // 用户国家 VARCHAR(30) 30 是    
	 private String province; // 省（直辖市） VARCHAR(30) 30 是    
	 private String city; //  城市 VARCHAR(30) 30 是    
	 private String area; // 区（县） VARCHAR(30) 30 是    
	 private Integer age; // 用户年龄 INT(3) 3 是    
	 private String  card; // 身份证号 VARCHAR(20) 20 是    
	 private String specialty; // 特长 VARCHAR(200) 200 是    
	 private String casehistory;//既往病史
	 private String openId; // 微信open_id VARCHAR(20) 20 是    
	 private Date createDate; // 创建时间 DATE  是    
	 private Integer deleteFlag; // 是否删除 INT(2) 2 是 
	 private String contactPerson;
	 
	public String getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	public String getCasehistory() {
		return casehistory;
	}
	public void setCasehistory(String casehistory) {
		this.casehistory = casehistory;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getAvater() {
		return avater;
	}
	public void setAvater(String avater) {
		this.avater = avater;
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
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getCard() {
		return card;
	}
	public void setCard(String card) {
		this.card = card;
	}
	public String getSpecialty() {
		return specialty;
	}
	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
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

	
	
}
