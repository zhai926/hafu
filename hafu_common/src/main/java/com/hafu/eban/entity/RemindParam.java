package com.hafu.eban.entity;

import org.apache.commons.lang3.StringUtils;

public class RemindParam {
	
	private String id;		//设备编码
	private String code;	//提醒ID
	private String type;	//提醒类型：0无 1睡觉 2起床 3活动 4休息 5吃药
	private String content;	//	提示内容信息
	private String repeat;	//重复方式：0不重复 1按天重复 2按周重复 3按月重复 4按年重复
	private String date;	//提醒日期（2013-09-11）
	private String time;	//提醒时间（20:10）	
	
	public String toUrlString(){
		StringBuffer sb = new StringBuffer();
		sb.append("?code="+code);
		if(StringUtils.isNotBlank(id)){
			sb.append("&id="+id);
		}
		sb.append("&type="+type+"&content="+content+"&repeat="+repeat+"&date="+date+"&time="+time);
		return sb.toString();
	}
	
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getRepeat() {
		return repeat;
	}
	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

}
