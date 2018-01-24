package com.hafu.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Date;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang3.StringUtils;
import org.hafu.modules.utils.BasicTypeUtils;
import org.hafu.modules.utils.VSFtpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.hafu.dto.UserDto;
import com.hafu.entity.HfConcernDevice;
import com.hafu.entity.HfDevice;
import com.hafu.entity.HfUser;
import com.hafu.service.IndexService;
import com.hafu.service.UserService;
import com.hafu.timer.TimerUtilCollection;
import com.hafu.utils.AjaxResutObject;
import com.hafu.utils.StaticVarible;
import com.hafu.utils.UpLoadFile;
import com.hafu.weixin.SendMessageService;
import com.hafu.weixin.message.resquest.AlertTempMessage;
import com.hafu.weixin.message.resquest.AlertTempMessage.Data;
import com.hafu.weixin.message.resquest.AlertTempMessage.Data.First;
import com.hafu.weixin.message.resquest.AlertTempMessage.Data.Keyword1;
import com.hafu.weixin.message.resquest.AlertTempMessage.Data.Keyword2;
import com.hafu.weixin.message.resquest.AlertTempMessage.Data.Keyword3;
import com.hafu.weixin.message.resquest.AlertTempMessage.Data.Remark;
import com.hafu.weixin.util.WeixinUtil;
/**
 * 个人设置
 */
@Controller
@RequestMapping("sendMessage")
public class SendMessageController {
	@Autowired
	private SendMessageService sendService;
	@Autowired
	private IndexService indexService;
	
	
	
	/**
	 * 发送报警消息
	 * @param req
	 * @return
	 */
	@RequestMapping("alert")
	@ResponseBody
	public String alert(HttpServletRequest req){
		AjaxResutObject re = new AjaxResutObject();
		try{
			String alertId = req.getParameter("alertId");
			if( StringUtils.isEmpty(alertId) ){
				re.setStatus("error");
				re.setMsg("非法请求");
				return JSONObject.fromObject(re).toString();
			}
			boolean flag = sendService.sendAlertMessage(alertId);
			if(flag){
				re.setStatus("success");
				return JSONObject.fromObject(re).toString();
			}
			else {
				re.setStatus("error");
				re.setMsg("非法请求");
				return JSONObject.fromObject(re).toString();
			}
		}catch(Exception e){
			e.printStackTrace();
			re.setStatus("error");
			re.setMsg("处理异常");
			return JSONObject.fromObject(re).toString();
		}
	}
	
	
	
	/**
	 * 发送健康报告消息
	 * @param req
	 * @return
	 */
	@RequestMapping("healthReport")
	@ResponseBody
	public String healthReport(HttpServletRequest req){
		/*AjaxResutObject re = new AjaxResutObject();
		try{
			String healthPointId = req.getParameter("healthId");
			if( StringUtils.isEmpty(healthPointId) ){
				re.setStatus("error");
				re.setMsg("非法请求");
				return JSONObject.fromObject(re).toString();
			}
			boolean flag = sendService.sendHealthReportMessage(healthPointId);
			if(flag){
				re.setStatus("success");
				return JSONObject.fromObject(re).toString();
			}
			else {
				re.setStatus("error");
				re.setMsg("非法请求");
				return JSONObject.fromObject(re).toString();
			}
		}catch(Exception e){
			e.printStackTrace();
			re.setStatus("error");
			re.setMsg("处理异常");
			return JSONObject.fromObject(re).toString();
		}*/
		return "";
	}
	
}
