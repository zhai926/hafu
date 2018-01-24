package com.hafu.examination.vo;

import java.io.Serializable;
import java.util.List;

public class ResponseExamination implements Serializable{

	private static final long serialVersionUID = 5393963643852463058L;
	private String code;					//查询状态代码：SUCESS成功,FAIL出错
	private String message;					//出错原因
	private List<ExaminationVo> checkData;	//数据
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<ExaminationVo> getCheckData() {
		return checkData;
	}
	public void setCheckData(List<ExaminationVo> checkData) {
		this.checkData = checkData;
	}
	
}
