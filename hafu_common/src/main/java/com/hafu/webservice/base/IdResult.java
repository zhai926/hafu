package com.hafu.webservice.base;

import javax.xml.bind.annotation.XmlType;

import com.hafu.webservice.WsConstants;

/**
 * 创建某个对象返回�?通用IdResult
 * 
 * @author badqiu
 *
 */
@XmlType(name = "IdResult", namespace = WsConstants.NS)
public class IdResult extends WSResult {
	private Long id;

	public IdResult() {
	}

	public IdResult(Long id) {
		super();
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
