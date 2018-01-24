package com.hafu.weixin.message.resquest;

import org.apache.commons.lang.StringUtils;

import com.hafu.weixin.WeixinCommon;

/**
 * 模板消息
 * @author zc
 *
 */
public class AlertTempMessage {
	private String touser;//接收人openid
	private String template_id; //模板id
	private String url; //跳转
	private Data data;
	
	private static String[] colors= new String[]{"#173177","#173177","#173177","#173177","#173177"};
	private static String TEMPLATEID="X0uTMaF4hgFrKd728QhyPD4xN77KjEd4FPUYbcX1KUQ";
	private static String URL = WeixinCommon.MENU_BASE+"/index/index";
	
	public String getTouser() {
		return touser;
	}

	
	public void setTouser(String touser) {
		this.touser = touser;
	}

	
	public String getTemplate_id() {
		if(StringUtils.isEmpty(template_id)) this.template_id = TEMPLATEID;
		return template_id;
	}


	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	
	public String getUrl() {
		if(StringUtils.isEmpty(url)) this.url = URL;
		return url;
	}

	
	public void setUrl(String url) {
		this.url = url;
	}

	
	public Data getData() {
		return data;
	}

	
	public void setData(Data data) {
		this.data = data;
	}

	public class Data{
		
		private First first;
		private Keyword1 keyword1;
		private Keyword2 keyword2;
		private Keyword3 keyword3;
		private Keyword4 keyword4;
		private Remark remark;
		

		public First getFirst() {
			return first;
		}

		public void setFirst(First first) {
			this.first = first;
		}
		
		
		public Keyword1 getKeyword1() {
			return keyword1;
		}

		public void setKeyword1(Keyword1 keyword1) {
			this.keyword1 = keyword1;
		}

		public Keyword2 getKeyword2() {
			return keyword2;
		}

		public void setKeyword2(Keyword2 keyword2) {
			this.keyword2 = keyword2;
		}

		public Keyword3 getKeyword3() {
			return keyword3;
		}

		public void setKeyword3(Keyword3 keyword3) {
			this.keyword3 = keyword3;
		}

		public Keyword4 getKeyword4() {
			return keyword4;
		}

		public void setKeyword4(Keyword4 keyword4) {
			this.keyword4 = keyword4;
		}

		public Remark getRemark() {
			return remark;
		}
		
		public void setRemark(Remark remark) {
			this.remark = remark;
		}
		public class First{
			private String value;
			private String color;
			public First(String value,String color){
				this.value = value;
				this.color = color;
			}
			public First(String value){
				this.value = value;
			}
			public String getValue() {
				return value;
			}
			public void setValue(String value) {
				this.value = value;
			}
			public String getColor() {
				if(StringUtils.isEmpty(color)) color = colors[0];
				return color;
			}
			public void setColor(String color) {
				this.color = color;
			}
			
		}
		public class Keyword1{
			private String value;
			private String color;
			public Keyword1(String value,String color){
				this.value = value;
				this.color = color;
			}
			public Keyword1(String value){
				this.value = value;
			}
			public String getValue() {
				return value;
			}
			public void setValue(String value) {
				this.value = value;
			}
			public String getColor() {
				if(StringUtils.isEmpty(color)) color = colors[1];
				return color;
			}
			public void setColor(String color) {
				this.color = color;
			}
		}
		public class Keyword2{
			private String value;
			private String color;
			public Keyword2(String value,String color){
				this.value = value;
				this.color = color;
			}
			public Keyword2(String value){
				this.value = value;
			}
			public String getValue() {
				return value;
			}
			public void setValue(String value) {
				this.value = value;
			}
			public String getColor() {
				if(StringUtils.isEmpty(color)) color = colors[2];
				return color;
			}
			public void setColor(String color) {
				this.color = color;
			}
		}
		public class Keyword3{
			private String value;
			private String color;
			public Keyword3(String value,String color){
				this.value = value;
				this.color = color;
			}
			public Keyword3(String value){
				this.value = value;
			}
			public String getValue() {
				return value;
			}
			public void setValue(String value) {
				this.value = value;
			}
			public String getColor() {
				if(StringUtils.isEmpty(color)) color = colors[3];
				return color;
			}
			public void setColor(String color) {
				this.color = color;
			}
		}
		public class Keyword4{
			private String value;
			private String color;
			public Keyword4(String value,String color){
				this.value = value;
				this.color = color;
			}
			public Keyword4(String value){
				this.value = value;
			}
			public String getValue() {
				return value;
			}
			public void setValue(String value) {
				this.value = value;
			}
			public String getColor() {
				if(StringUtils.isEmpty(color)) color = colors[3];
				return color;
			}
			public void setColor(String color) {
				this.color = color;
			}
		}
		public class Remark{
			private String value;
			private String color;
			public Remark(String value,String color){
				this.value = value;
				this.color = color;
			}
			public Remark(String value){
				this.value = value;
			}
			public String getValue() {
				return value;
			}
			public void setValue(String value) {
				this.value = value;
			}
			public String getColor() {
				if(StringUtils.isEmpty(color)) color = colors[4];
				return color;
			}
			public void setColor(String color) {
				this.color = color;
			}
		}
	}
	
	
}
