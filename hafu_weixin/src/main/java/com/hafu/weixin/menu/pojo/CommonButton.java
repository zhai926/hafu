package com.hafu.weixin.menu.pojo;
/**
 * 普通按钮(子按钮)
 * @author Dong
 *
 */
public class CommonButton extends Button {
	private String type; //类型
	private String key; // key 键

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
