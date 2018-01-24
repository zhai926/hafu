package com.hafu.eban.entity.response;

import java.io.Serializable;
import java.util.List;

public class UserLoginResponse implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String success;	//操作是否成功
	private String message;		//返回信息
	private String token;		//令牌
	private UserLoginResponse_appType apptype;		//APP类型和版本
	private String real_name;	//真实姓名
	private String name;		//账号
	private String user_id;		//用户ID
	private String avatar;		//用户头像
	private String address;		//地址
	private List<UserLoginResponse_user> user_info;	//对应绑定的设备信息
	
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public UserLoginResponse_appType getApptype() {
		return apptype;
	}
	public void setApptype(UserLoginResponse_appType apptype) {
		this.apptype = apptype;
	}
	public String getReal_name() {
		return real_name;
	}
	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public List<UserLoginResponse_user> getUser_info() {
		return user_info;
	}
	public void setUser_info(List<UserLoginResponse_user> user_info) {
		this.user_info = user_info;
	}

}
