package com.hafu.eban.entity.response;

import java.io.Serializable;
import java.util.List;

/***
 * 紧急联系人实体
 * @author chang
 *
 */
public class ContactsResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer success;
	private String message;
	private List<ContactsResponse_list> list;
	public Integer getSuccess() {
		return success;
	}
	public void setSuccess(Integer success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<ContactsResponse_list> getList() {
		return list;
	}
	public void setList(List<ContactsResponse_list> list) {
		this.list = list;
	}

}
