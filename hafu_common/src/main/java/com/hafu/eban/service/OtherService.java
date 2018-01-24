package com.hafu.eban.service;

import org.apache.log4j.Logger;

import com.hafu.eban.entity.response.DayInfoResponse;
import com.hafu.eban.entity.response.DayRadarResponse;
import com.hafu.eban.entity.response.DayWeatherResponse;
import com.hafu.eban.entity.response.MessageAlertResponse;
import com.hafu.eban.entity.response.MessageConutResponse;
import com.hafu.eban.entity.response.ProductInfoResponse;
import com.hafu.eban.utils.CodeConverter;
import com.hafu.eban.utils.HttpClientUtil;
import com.hafu.eban.utils.JSONUtils;

/***
 * 其他
 * 
 * @author chang
 *
 */
public class OtherService {

	private static Logger logger = Logger.getLogger(OtherService.class);

	private static String productInfoUrl = "http://capi.9eban.com/common/ProductInfo";
	private static String dayInfoUrl = "http://capi.9eban.com/day/DayInfo";
	private static String messageConutUrl = "http://capi.9eban.com/massege/count";
	private static String messageAlertUrl = "http://capi.9eban.com/massege/alert";
	private static String dayRadarUrl = "http://capi.9eban.com/day/Radar";
	private static String healthEncourageUrl = "http://capi.9eban.com/health/encourage";

	public static void main(String[] args) {
		OtherService otherService = new OtherService();
		// ProductInfoResponse productInfoResponse =
		// otherService.productInfo("100100356");
		// System.out.println(productInfoResponse.getInfo().getMobile()+"   "+productInfoResponse.getList().size());
        DayInfoResponse dayInfoResponse = otherService.dayInfo("100202879", "2016-08-03");
		System.out.println(dayInfoResponse.getDay_info().getPoint_today());
		// MessageConutResponse messageConutResponse =
		// otherService.messageConut("100100356");
		// System.out.println(messageConutResponse.getAlert());
		// MessageAlertResponse messageAlertResponse =
		// otherService.messageAlert();
		// System.out.println(messageAlertResponse.getAlert().size());
		// DayRadarResponse dayRadarResponse =
		// otherService.dayRadar("100100356", "2016-07-04");
		// System.out.println(dayRadarResponse.getData());
		// DayWeatherResponse dayWeatherResponse =
		// otherService.dayWeather("100100356", "2016-06-02");
		// System.out.println(dayWeatherResponse.getSuccess());

		// DayWeatherResponse dayWeatherResponse =
		// otherService.healthEncourage("100202859", 1);
		// System.out.println(dayWeatherResponse.getSuccess());
	}

	/**
	 * 用户通过设备编号获取佩戴者相关信息
	 */
	public ProductInfoResponse productInfo(String code) {
		HttpClientUtil httpClientUtil = new HttpClientUtil();
		String str = httpClientUtil.getHttp(productInfoUrl + "?code=" + code);
		logger.debug("获取佩戴者 " + code + " 相关信息  = "
				+ CodeConverter.decodeUnicode2ZH(str));
		return JSONUtils.fromJson(CodeConverter.decodeUnicode2ZH(str),
				ProductInfoResponse.class);
	}

	/**
	 * 用户通过设备编号以及日期获取当天日报信息
	 */
    public DayInfoResponse dayInfo(String code, String date) {
        DayInfoResponse dayInfoResponse = null;
        HttpClientUtil httpClientUtil = new HttpClientUtil();
        String str = httpClientUtil.getHttp(dayInfoUrl + "?code=" + code + "&date=" + date);
        str = CodeConverter.decodeUnicode2ZH(str);
        logger.info(code + " 日报信息  = " + str);
        try {
            if (str.startsWith("{")&& str.indexOf("\"success\":1")!=-1) {
                dayInfoResponse = JSONUtils.fromJson(str, DayInfoResponse.class);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return dayInfoResponse;
    }

	/**
	 * 用户通过设备编号获取该设备的未阅读报警和语音条数信息
	 */
    public MessageConutResponse messageConut(String code) {
        HttpClientUtil httpClientUtil = new HttpClientUtil();
        String str = httpClientUtil.getHttp(messageConutUrl + "?code=" + code);
        logger.debug(code + " 未阅读报警和语音条数信息  = " + CodeConverter.decodeUnicode2ZH(str));
        return JSONUtils.fromJson(CodeConverter.decodeUnicode2ZH(str), MessageConutResponse.class);
    }

	/**
	 * 用户通过登录生成token获取该设备报警信息及报警位置
	 */
	public MessageAlertResponse messageAlert() {
		HttpClientUtil httpClientUtil = new HttpClientUtil();
		String str = httpClientUtil.getHttp(messageAlertUrl);
		logger.debug("设备报警信息及报警位置  = " + CodeConverter.decodeUnicode2ZH(str));
		return JSONUtils.fromJson(CodeConverter.decodeUnicode2ZH(str),
				MessageAlertResponse.class);
	}

	/**
	 * 雷达图数据接口详情
	 */
    public DayRadarResponse dayRadar(String code, String date) {
        HttpClientUtil httpClientUtil = new HttpClientUtil();
        try {
            String str = httpClientUtil.getHttp(dayRadarUrl + "?code=" + code + "&date=" + date);
            str = CodeConverter.decodeUnicode2ZH(str);
            // {"success":0,"data":""}
            logger.info(code + " 雷达图数据接口详情  = " + str);
            if (str.startsWith("{") && str.indexOf("\"success\":1") != -1) {
                return JSONUtils.fromJson(str, DayRadarResponse.class);
            }
        } catch (Exception e) {
            logger.error(code + " 雷达图数据接口详情  = " + e.getMessage());
        }
        return null;
    }

	/**
	 * 鼓励接口详情 鼓励类型 1：您今天的表现真棒，您的孩子为您热情鼓掌 2：您的孩子为您献上一枝花 3：您的孩子给您一个拥抱
	 */
    public DayWeatherResponse healthEncourage(String code, Integer type) {
        HttpClientUtil httpClientUtil = new HttpClientUtil();
        String str = httpClientUtil.getHttp(healthEncourageUrl + "?code=" + code + "&type=" + type);
        str = CodeConverter.decodeUnicode2ZH(str);
        logger.info(code + " 鼓励接口  = " + str);
        return JSONUtils.fromJson(str, DayWeatherResponse.class);
    }

}
