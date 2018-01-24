package com.hafu.eban.service;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.hafu.eban.entity.response.VoiceRespnse;
import com.hafu.eban.entity.response.VoiceRespnseList;
import com.hafu.eban.entity.response.VoiceResponse_voceList;
import com.hafu.eban.utils.CodeConverter;
import com.hafu.eban.utils.DateUtil;
import com.hafu.eban.utils.HttpClientUtil;
import com.hafu.eban.utils.JSONUtils;

/***
 * 语音信息接口详情
 * @author chang
 *
 */
public class VoiceService {
	private static Logger logger = Logger.getLogger(VoiceService.class);
	
	private static String voiceUrl = "http://capi.9eban.com/common/voice";
	private static String vmessageUrl = "http://capi.9eban.com/common/vmessage";
	private static String getVoiceUrl = "http://capi.9eban.com/common/getVoice";
	
	public static void main(String[] args) {
		VoiceService voice = new VoiceService();
		//上传文件
		//VoiceRespnse voiceResponse = voice.voice("100202859", "d://demo/2.amr");
		//上传文字消息
		//VoiceRespnse voiceResponse = voice.vmessage("100100356", "测试语音");
		//System.out.println(voiceResponse.getSuccess() +"  "+voiceResponse.getVoice());
		//下载语音
		VoiceRespnseList voiceRespnseList = voice.getVoice("100202859");
		List<VoiceResponse_voceList> voice1 = voiceRespnseList.getVoice();
		if(voice1 != null){
			for (int i = 0; i < voice1.size(); i++) {
				VoiceResponse_voceList voiceResponse_voceList = voiceRespnseList.getVoice().get(i);
				Date alertDate = DateUtil.TimeStamp2Date(voiceResponse_voceList.getDateline());
				System.out.println(alertDate);
			}
		}
	}
	
	
	/***
	 * 用户通过设备编号以及语音文件上传语音消息
	 */
	public VoiceRespnse voice(String code,String path){
		HttpClientUtil httpClientUtil = new HttpClientUtil();
		String str = httpClientUtil.uploadVoiceFile(path,voiceUrl+"?code="+code);
		logger.debug(code + " 上传语音消息-->文件  = "+CodeConverter.decodeUnicode2ZH(str));
		return JSONUtils.fromJson(CodeConverter.decodeUnicode2ZH(str), VoiceRespnse.class);
	}
	
	/***
	 * 用户通过设备编号以及文字信息上传文字信息
	 */
	public VoiceRespnse vmessage(String code,String message){
		HttpClientUtil httpClientUtil = new HttpClientUtil();
		String str = httpClientUtil.getHttp(vmessageUrl+"?code="+code+"&message="+message);
		logger.debug(code + " 上传语音消息-->文字  = "+CodeConverter.decodeUnicode2ZH(str));
		return JSONUtils.fromJson(CodeConverter.decodeUnicode2ZH(str), VoiceRespnse.class);
	}
	
	/***
	 * 用户通过设备编号以及文字信息上传文字信息
	 */
	public VoiceRespnseList getVoice(String code){
		HttpClientUtil httpClientUtil = new HttpClientUtil();
		String str = httpClientUtil.getHttp(getVoiceUrl+"?code="+code);
		logger.debug(code + " 下载语音信息  = "+CodeConverter.decodeUnicode2ZH(str));
		return JSONUtils.fromJson(CodeConverter.decodeUnicode2ZH(str), VoiceRespnseList.class);
	}
}
