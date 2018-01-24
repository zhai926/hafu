package com.hafu.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * 数据字典
 */
@Entity
@Table(name="hf_dic")
public class HfDic extends IdEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1235526046695579888L;
	private String dicCode;//字典数据编码
	private String dicName;//字典数据名称
	private String dicDescription;//字典数据描述
	private Integer dicDisplayOrder;//字典数据显示顺序
	private Integer deleteFlag;//删除标志(0:未删除，1:已删除)
	public String getDicCode() {
		return dicCode;
	}
	public void setDicCode(String dicCode) {
		this.dicCode = dicCode;
	}
	public String getDicName() {
		return dicName;
	}
	public void setDicName(String dicName) {
		this.dicName = dicName;
	}
	public String getDicDescription() {
		return dicDescription;
	}
	public void setDicDescription(String dicDescription) {
		this.dicDescription = dicDescription;
	}
	public Integer getDicDisplayOrder() {
		return dicDisplayOrder;
	}
	public void setDicDisplayOrder(Integer dicDisplayOrder) {
		this.dicDisplayOrder = dicDisplayOrder;
	}
	public Integer getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	
}
