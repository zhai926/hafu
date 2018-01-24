package com.hafu.eban.service;

import org.apache.log4j.Logger;

import com.hafu.eban.entity.RemindParam;
import com.hafu.eban.entity.response.RemindResponse;
import com.hafu.eban.utils.CodeConverter;
import com.hafu.eban.utils.DateUtil;
import com.hafu.eban.utils.HttpClientUtil;
import com.hafu.eban.utils.JSONUtils;

/**
 * e伴亲情提醒相关接口
 * 
 * @author chang
 *
 */

public class FamilyReminderService {
	private static Logger logger = Logger.getLogger(FamilyReminderService.class);
	
	private static String addRemindUrl = "http://capi.9eban.com/remind/add";
	private static String listRemindUrl = "http://capi.9eban.com/remind/list";
	private static String editRemindUrl = "http://capi.9eban.com/remind/edit";
	private static String delRemindUrl = "http://capi.9eban.com/remind/del";

	public static void main(String[] args) {
		FamilyReminderService service = new FamilyReminderService();
		/*RemindParam remindParam = new RemindParam();
		remindParam.setCode("100202859");
		remindParam.setType("1");
		remindParam.setContent("睡觉了啦1");
		remindParam.setRepeat("0");
		remindParam.setDate("2016-7-10");
		remindParam.setTime("12:50");
		// 修改时传递ID
*/	   // remindParam.setId("62528");
		// 添加
		//RemindResponse remindResponse = service.addRemind(remindParam);
		//System.out.println(remindResponse.getList().size()+"    "+remindResponse.getList().get(0).getContent());
		// 修改
		//RemindResponse remindResponse = service.editRemind(remindParam);
		//System.out.println(remindResponse.getList().size()+"    "+remindResponse.getList().get(0).getContent());
		// 删除
		RemindResponse remindResponse = service.delRemind("63254",
		 "100100187");
		System.out.println(remindResponse.getSuccess());
		// 获取所有
	  /* RemindResponse remindResponse = service.listRemind("100100187");
		System.out.println(remindResponse.getList().size());
		for (int i = 0; i < remindResponse.getList().size(); i++) {
			System.out.println("内容 ： "+remindResponse.getList().get(i).getContent());
			System.out.print("提醒时间 ："+DateUtil.format(DateUtil.TimeStamp2DateFomart(remindResponse.getList().get(i).getDate())));
			System.out.println(" "+DateUtil.parserTominute(remindResponse.getList().get(i).getTime()));
		}
	    System.out.println(DateUtil.TimeStamp2DateFomart(remindResponse.getList().get(0).getTime()));
		System.out.println(remindResponse.getSuccess());*/

	}

	/**
	 * 用户通过提交提醒相关的信息设定亲情提醒
	 */
	public RemindResponse addRemind(RemindParam remindParam) {
		String param = remindParam.toUrlString();
		HttpClientUtil httpClientUtil = new HttpClientUtil();
		String str = httpClientUtil.getHttp(addRemindUrl + param);
		logger.debug(remindParam.getCode()+" 设定亲情提醒  = " + CodeConverter.decodeUnicode2ZH(str));
		return JSONUtils.fromJson(CodeConverter.decodeUnicode2ZH(str),
				RemindResponse.class);
	}

	/**
	 * 用户通过设备编号以及提醒ID更新提醒信息
	 */
	public RemindResponse editRemind(RemindParam remindParam) {
		String param = remindParam.toUrlString();
		HttpClientUtil httpClientUtil = new HttpClientUtil();
		String str = httpClientUtil.getHttp(editRemindUrl + param);
		logger.info(remindParam.getCode()+" 更新提醒信息  = " + CodeConverter.decodeUnicode2ZH(str));
		return JSONUtils.fromJson(CodeConverter.decodeUnicode2ZH(str),
				RemindResponse.class);
	}

	/**
	 * 用户通过设备编号以及亲情提醒ID删除之前设置的提醒
	 */
	public RemindResponse delRemind(String id, String code) {
		HttpClientUtil httpClientUtil = new HttpClientUtil();
		String str = httpClientUtil.getHttp(delRemindUrl + "?id=" + id
				+ "&code=" + code);
		logger.info(code + " 删除提醒  = " + CodeConverter.decodeUnicode2ZH(str));
		return JSONUtils.fromJson(CodeConverter.decodeUnicode2ZH(str),
				RemindResponse.class);
	}

	/**
	 * 用户通过设备编号获取当前设备的所有亲情提醒
	 */
	public RemindResponse listRemind(String code) {
		HttpClientUtil httpClientUtil = new HttpClientUtil();
		String str = httpClientUtil.getHttp(listRemindUrl + "?code=" + code);
		logger.info(code + "所有亲情提醒  = " + CodeConverter.decodeUnicode2ZH(str));
		return JSONUtils.fromJson(CodeConverter.decodeUnicode2ZH(str),
				RemindResponse.class);
	}

}
