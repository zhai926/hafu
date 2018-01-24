package com.hafu.examination.vo;

import java.io.Serializable;

/**
 * 体检资料
 * @author Darker
 *
 */
public class ExaminationVo implements Serializable{

	private static final long serialVersionUID = 5393963643852463058L;
	private String recordTime;				//体检时间,必填
	private String name;					//居民姓名，必填
	private String cardNo;					//居民卡号，必填
	private String orgName;					//机构名称，选填
	private String deviceId;				//设备编码，选填
	private CheckData data;					//体检数据
	
	public String getRecordTime() {
		if(null!=recordTime&&!"".equals(recordTime)){
			if(recordTime.indexOf("T")!=-1){
				recordTime = recordTime.replace("T", " ");
				recordTime = recordTime.substring(0,recordTime.indexOf("."));
			}
		}
		return recordTime;
	}
	public void setRecordTime(String recordTime) {
		if(null!=recordTime&&!"".equals(recordTime)){
			recordTime = recordTime.replace("T", " ");
			recordTime = recordTime.substring(0,recordTime.indexOf("."));
		}
		this.recordTime = recordTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public CheckData getData() {
		return data;
	}
	public void setData(CheckData data) {
		this.data = data;
	}
	
}
