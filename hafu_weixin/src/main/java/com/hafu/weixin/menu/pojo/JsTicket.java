package com.hafu.weixin.menu.pojo;
/**
 * 微信JS接口的临时票据
 * @author zc
 *
 */
public class JsTicket {
	private String ticket; //票据
	// 凭证有效时间，单位：秒
	private int expiresIn;
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public int getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}
	
	
}
