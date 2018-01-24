package com.hafu.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * 数据字典子表
 */
@Entity
@Table(name="hf_dic_item")
public class HfDicItem extends IdEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1772143486327972854L;
	
	private String dicItemCode;//
	private String dicItemName;//
	private String dicItemDescription;//
	private Integer dicItemDisplayOrder;//
	private String dicItemValue;//
	private String dicCode;//引用hf_dic中dic_code
	private Integer deleteFlag;//删除标志(0:未删除，1：已删除)
	public String getDicItemCode() {
		return dicItemCode;
	}
	public void setDicItemCode(String dicItemCode) {
		this.dicItemCode = dicItemCode;
	}
	public String getDicItemName() {
		return dicItemName;
	}
	public void setDicItemName(String dicItemName) {
		this.dicItemName = dicItemName;
	}
	public String getDicItemDescription() {
		return dicItemDescription;
	}
	public void setDicItemDescription(String dicItemDescription) {
		this.dicItemDescription = dicItemDescription;
	}
	public Integer getDicItemDisplayOrder() {
		return dicItemDisplayOrder;
	}
	public void setDicItemDisplayOrder(Integer dicItemDisplayOrder) {
		this.dicItemDisplayOrder = dicItemDisplayOrder;
	}
	public String getDicItemValue() {
		return dicItemValue;
	}
	public void setDicItemValue(String dicItemValue) {
		this.dicItemValue = dicItemValue;
	}
	public String getDicCode() {
		return dicCode;
	}
	public void setDicCode(String dicCode) {
		this.dicCode = dicCode;
	}
	public Integer getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	
}
