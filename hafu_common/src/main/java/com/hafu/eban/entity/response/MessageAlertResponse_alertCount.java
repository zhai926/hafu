package com.hafu.eban.entity.response;

import java.io.Serializable;

public class MessageAlertResponse_alertCount implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String fall_over;	//自动报警条数
	private String today_man_alert;//其他报警条数
	private String total;	//总条数
	public String getFall_over() {
		return fall_over;
	}
	public void setFall_over(String fall_over) {
		this.fall_over = fall_over;
	}
	public String getToday_man_alert() {
		return today_man_alert;
	}
	public void setToday_man_alert(String today_man_alert) {
		this.today_man_alert = today_man_alert;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}

}
