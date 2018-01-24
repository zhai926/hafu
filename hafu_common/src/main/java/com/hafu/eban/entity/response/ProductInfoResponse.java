package com.hafu.eban.entity.response;

import java.io.Serializable;
import java.util.List;

public class ProductInfoResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String success;
	private String message;
	private ProductInfoResponse_info info;
	private List<ProductInfoResponse_list> list;
	
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ProductInfoResponse_info getInfo() {
		return info;
	}
	public void setInfo(ProductInfoResponse_info info) {
		this.info = info;
	}
	public List<ProductInfoResponse_list> getList() {
		return list;
	}
	public void setList(List<ProductInfoResponse_list> list) {
		this.list = list;
	}
		
	
}
