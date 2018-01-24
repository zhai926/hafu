package com.hafu.eban.entity.response;

import java.io.Serializable;

public class UserLoginResponse_appType implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private UserLoginResponse_appType_android android;
	private UserLoginResponse_appType_ios ios;
	
	public UserLoginResponse_appType_android getAndroid() {
		return android;
	}
	public void setAndroid(UserLoginResponse_appType_android android) {
		this.android = android;
	}
	public UserLoginResponse_appType_ios getIos() {
		return ios;
	}
	public void setIos(UserLoginResponse_appType_ios ios) {
		this.ios = ios;
	} 

}
