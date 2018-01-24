package com.hafu.eban.entity.response;

import java.io.Serializable;

public class VoiceResponse_voceList implements Serializable{

	private static final long serialVersionUID = 1L;
		
	private String code;	//设备编号
	private String path;	//语音路径
	private String dateline;//时间（unix时间戳）
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getDateline() {
		return dateline;
	}
	public void setDateline(String dateline) {
		this.dateline = dateline;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	private String size;

}
