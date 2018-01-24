package com.hafu.utils;

import java.text.SimpleDateFormat;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class JsonDateValueProcessor implements JsonValueProcessor{
	private String format;
	public JsonDateValueProcessor(String format){
		this.format=format;
	}
    public Object processObjectValue(String key, Object value,JsonConfig arg2){
      if(value==null) return "";
      if (value instanceof java.util.Date) {
            String str = new SimpleDateFormat(format).format((java.util.Date) value);
            return str;
      }
       return value.toString();
    }
    public Object processArrayValue(Object value, JsonConfig arg1){
               return null;
    }
    
    
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
    
}
