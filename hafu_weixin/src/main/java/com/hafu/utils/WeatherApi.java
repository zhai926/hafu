package com.hafu.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

public class WeatherApi {
	
	private String humidity;//湿度
	private double temperature;//温度
	private String lunarCalendar;//农历
	private String cityName;//城市名
	private String quality;//pm2.5
	private String weather;//天气
	private String weatherPic; //天气图片
	private String lunar;//阴历
	private String week;//星期
	private String termString;//节气
	private String windDirection; //风向
	private String windPower;//风力
	private boolean state=false;//查询状态 true查询成功false 查询失败
	private static ResourceBundle bundle;
	private Date date;
	static{
		bundle=PropertyResourceBundle.getBundle("smsconfig");
	}
	private WeatherApi(Double lng,Double lat){
		date=new Date();
		Lunar lu=new Lunar(date);
		//termString=lu.getTermString();
		//lunar=lu.getLunarDateString();
		week = dayOfWeek(lu.getDayOfWeek());
		String apikey=bundle.getString("apiKey");
		String url="http://apis.baidu.com/showapi_open_bus/weather_showapi/point?lng="+lng+"&lat="+lat+"&from=5";
		GetMethod method=new GetMethod(url);
		method.setRequestHeader(new Header("apikey", apikey));
		HttpClient client=new HttpClient();
		try {
			method.addRequestHeader("Content-Type","text/html;charset=UTF-8");
			int status=client.executeMethod(method);
			if(status==HttpStatus.SC_OK){
				InputStream is=method.getResponseBodyAsStream();
				StringBuffer buff=new StringBuffer();
				BufferedReader reader=new BufferedReader(new InputStreamReader(is,"UTF-8"));
				String line;
				while((line=reader.readLine())!=null){
					buff.append(line);
				}
				JSONObject obj=JSONObject.fromObject(buff.toString());
				int showapi_res_code=obj.optInt("showapi_res_code");
				if(showapi_res_code==0){
					JSONObject showapi_res_body=obj.optJSONObject("showapi_res_body");
					//地区查询没问题
					if(showapi_res_body!=null&&showapi_res_body.optInt("ret_code")!=-1){
						JSONObject city=showapi_res_body.optJSONObject("cityInfo");
						//c3获取的是城市名
						this.cityName=city.optString("c5");
						JSONObject now=showapi_res_body.optJSONObject("now");
						if(now!=null){
							JSONObject aqiDetail=now.optJSONObject("aqiDetail");
							try{
								this.quality=aqiDetail.optString("quality");
								this.temperature=now.optDouble("temperature");
								this.humidity=now.optString("sd");
								this.weather=now.optString("weather");
								this.weatherPic=now.optString("weather_pic");
								this.windDirection=now.optString("wind_direction");
								this.windPower=now.optString("wind_power");
								this.state=true;
							}catch(Exception e){
								//大陆以外地区这些数据不存在
								this.quality="";
								this.temperature=0;
								this.humidity="";
								this.weather="";
								this.windDirection="";
								this.windPower="";
							}
						}
					}
				}
			}
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static WeatherApi getInstance(Double lng,Double lat){
		WeatherApi weatherApi=null;
		if(lng==null||lat==null){
			weatherApi=new WeatherApi(Double.valueOf(bundle.getString("defaultLng")),Double.valueOf(bundle.getString("defaultLat")));
		}else{
			weatherApi=new WeatherApi(lng, lat);
		}
		return weatherApi;
	}
	
	public static String dayOfWeek(int i){
		if(i==1){
			return "星期日";
		}else if(i==2){
			return "星期一";
		}else if(i==3){
			return "星期二";
		}else if(i==4){
			return "星期三";
		}else if(i==5){
			return "星期四";
		}else if(i==6){
			return "星期五";
		}else if(i==7){
			return "星期六";
		}else{
			return "";
		}
	}
	
	public String getHumidity() {
		return humidity;
	}
	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}
	public double getTemperature() {
		return temperature;
	}
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	public String getLunarCalendar() {
		return lunarCalendar;
	}
	public void setLunarCalendar(String lunarCalendar) {
		this.lunarCalendar = lunarCalendar;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
	}
	
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	
	public String getLunar() {
		return lunar;
	}
	public void setLunar(String lunar) {
		this.lunar = lunar;
	}
	public String getTermString() {
		return termString;
	}
	public void setTermString(String termString) {
		this.termString = termString;
	}
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public boolean isState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
	public String getWeatherPic() {
		return weatherPic;
	}
	public void setWeatherPic(String weatherPic) {
		this.weatherPic = weatherPic;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public String getWindDirection() {
		return windDirection;
	}
	public void setWindDirection(String windDirection) {
		this.windDirection = windDirection;
	}
	public String getWindPower() {
		return windPower;
	}
	public void setWindPower(String windPower) {
		this.windPower = windPower;
	}
	
}
