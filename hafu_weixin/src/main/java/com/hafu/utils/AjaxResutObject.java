package com.hafu.utils;

import java.util.List;

public class AjaxResutObject {
	
	
	private String msg;//信息
	private String status; //状态
	private List list ; //返回list 
	private Object ob ;// 返回对象
	private byte[] obByte;
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List getList() {
		return list;
	}
	public void setList(List list) {
		this.list = list;
	}
	public Object getOb() {
		return ob;
	}
	public void setOb(Object ob) {
		this.ob = ob;
	}
	public byte[] getObByte() {
		return obByte;
	}
	public void setObByte(byte[] obByte) {
		this.obByte = obByte;
	}

	
	
	
	
}
