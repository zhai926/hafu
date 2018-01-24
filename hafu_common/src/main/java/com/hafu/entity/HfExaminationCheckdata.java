package com.hafu.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 一体机体检数据
 * @author Darker
 *
 */
@Entity
@Table(name="hf_examination_checkdata")
public class HfExaminationCheckdata extends IdEntity{

	private static final long serialVersionUID = 1L;
	
	private String cardNo;				//身份证
	private String name;				//姓名
	private Timestamp uploadTime;		//上传时间
	private Timestamp recordTime;		//体检时间
	private String dataFrom;			//数据来源
	private String data;				//数据json
	private String dataId;				//数据id
	private Long examinationId;		//一体机同步查询计划id
	private Integer accessStatus;		//
	
	
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Timestamp getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(Timestamp uploadTime) {
		this.uploadTime = uploadTime;
	}
	public Timestamp getRecordTime() {
		return recordTime;
	}
	public void setRecordTime(Timestamp recordTime) {
		this.recordTime = recordTime;
	}
	public String getDataFrom() {
		return dataFrom;
	}
	public void setDataFrom(String dataFrom) {
		this.dataFrom = dataFrom;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getDataId() {
		return dataId;
	}
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	public Long getExaminationId() {
		return examinationId;
	}
	public void setExaminationId(Long examinationId) {
		this.examinationId = examinationId;
	}
	public Integer getAccessStatus() {
		return accessStatus;
	}
	public void setAccessStatus(Integer accessStatus) {
		this.accessStatus = accessStatus;
	}
}
