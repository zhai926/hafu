package com.hafu.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="hf_love")
public class HfLove extends IdEntity{
	private static final long serialVersionUID = 1L;
	private Integer id;//编号
	private String content;//短语内容
	private Integer loveType;//爱的类型    爱的类型(0:代表关爱一下 1:代表照顾爸妈 2:关爱提醒 3:代表传递思念  4:代表自定义)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getLoveType() {
		return loveType;
	}
	public void setLoveType(int loveType) {
		this.loveType = loveType;
	}
	


	

	
	
}
