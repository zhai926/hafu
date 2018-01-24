package com.hafu.eban.service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.hafu.eban.entity.response.ContactsResponse;
import com.hafu.eban.utils.CodeConverter;
import com.hafu.eban.utils.HttpClientUtil;
import com.hafu.eban.utils.JSONUtils;

/***
 * 紧急联系人
 * @author chang
 *
 */
@Service
public class ContactsService {
	private static Logger logger = Logger.getLogger(ContactsService.class);

	private static String getContactsUrl = "http://capi.9eban.com/relation/list";	//获取列表
	private static String addContactsUrl = "http://capi.9eban.com/relation/add";	//添加
	private static String editContactsUrl = "http://capi.9eban.com/relation/edit";	//编辑
	private static String delContactsUrl = "http://capi.9eban.com/relation/del";	//删除
	
	public static void main(String[] args) {
		ContactsService contactsService = new ContactsService();
		ContactsResponse c1 = contactsService.getContacts("100101947");
		System.out.println(c1.getSuccess());
		//ContactsResponse c2 = contactsService.addContacts("100101947","时志多","15618385919");
		//System.out.println(c2.getSuccess());
		//ContactsResponse c3 = contactsService.editContact("100100406",7689,"时","15618385919");
		//System.out.println(c3.getSuccess());
		/*ContactsResponse c4 = contactsService.delContact("100202859", 7553);
		System.out.println(c4.getSuccess());*/
	}
	
	public ContactsResponse getContacts(String code){
		HttpClientUtil httpClientUtil = new HttpClientUtil();
		String str = httpClientUtil.getHttp(getContactsUrl + "?code=" +code);
		logger.info("当前设备 "+code+" 相关信息  = " + CodeConverter.decodeUnicode2ZH(str));
		return JSONUtils.fromJson(CodeConverter.decodeUnicode2ZH(str), ContactsResponse.class);
	}
	
	
	public ContactsResponse addContacts(String code,String name,String mobile){
		HttpClientUtil httpClientUtil = new HttpClientUtil();
		String str = httpClientUtil.getHttp(addContactsUrl + "?code=" +code+"&name="+name+"&mobile="+mobile);
		logger.info("当前设备 "+code+" 相关信息  = " + CodeConverter.decodeUnicode2ZH(str));
		return JSONUtils.fromJson(CodeConverter.decodeUnicode2ZH(str), ContactsResponse.class);
	}
	
	public ContactsResponse editContact(String code,Integer id,String name,String mobile){
		HttpClientUtil httpClientUtil = new HttpClientUtil();
		String str = httpClientUtil.getHttp(editContactsUrl + "?code=" +code+"&id="+id+"&name="+name+"&mobile="+mobile);
		logger.info("当前设备 "+code+" 相关信息  = " + CodeConverter.decodeUnicode2ZH(str));
		return JSONUtils.fromJson(CodeConverter.decodeUnicode2ZH(str), ContactsResponse.class);
	}
	
	public ContactsResponse delContact(String code,Integer id){
		HttpClientUtil httpClientUtil = new HttpClientUtil();
		String str = httpClientUtil.getHttp(delContactsUrl + "?code=" +code+"&id="+id);
		logger.info("当前设备 "+code+" 相关信息  = " + CodeConverter.decodeUnicode2ZH(str));
		return JSONUtils.fromJson(CodeConverter.decodeUnicode2ZH(str), ContactsResponse.class);
	}
}
