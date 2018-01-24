package com.hafu.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hafu.entity.HfDevice;
import com.hafu.entity.HfUser;
import com.hafu.service.BehaviorRecordService;
import com.hafu.service.EncouragementService;
import com.hafu.service.HfHealthPointService;
import com.hafu.service.HfHealthTargetService;
import com.hafu.service.HfTrackService;
import com.hafu.service.IndexService;
import com.hafu.service.UserService;
import com.hafu.timer.TimerUtilCollection;
import com.hafu.utils.AjaxResutObject;
import com.hafu.utils.StaticVarible;
import com.hafu.weixin.WeixinCommon;
import com.hafu.weixin.menu.pojo.JsTicket;
import com.hafu.weixin.util.SignUtil;
/**
 * 首次进入 添加使用人
 * @author zc
 *
 */
@Controller
@RequestMapping(value="register")
public class RegisterController {
	@Autowired
	private IndexService indexService;
	@Autowired 
	private BehaviorRecordService behaviorService;
	@Autowired
	private EncouragementService encouragementService;
	@Autowired
	private UserService userService;
	@Autowired
	private HfHealthPointService pointService;
	@Autowired
	private HfTrackService trackService;
	
	@Resource
	private HfHealthTargetService healthTargetService;
	
	// LOG日志	
		private static Logger log = LoggerFactory.getLogger(RegisterController.class);
	
	/**
	 * 进入欢迎页面（指注册设备持有人信息）
	 */
	@RequestMapping("towelcome")
	public String welcome(HttpServletRequest req,Model model){
		log.debug("进入欢迎页面");
		return "welcome";
	}
	
	/**
	 * 进入注册（指注册设备持有人信息）
	 */
	@RequestMapping("register")
	public String register(HttpServletRequest req,Model model){
		String appId =  WeixinCommon.APPID;
		String timestamp = Long.toString(System.currentTimeMillis() / 1000);
		String nonceStr = UUID.randomUUID().toString();
		JsTicket ticket = TimerUtilCollection.jsTicket;
		String string1 = "jsapi_ticket="+ticket.getTicket()+
				"&noncestr="+nonceStr+"&timestamp="+timestamp+
				"&url="+WeixinCommon.MENU_BASE+"/register/register";
		String signature = SignUtil.sha1Sign(string1);
		log.error("appId" + appId);
		log.error("timestamp" + timestamp);
		log.error("nonceStr" + nonceStr);
		log.error("ticket" + ticket);
		log.error("string1" + string1);
		log.error("signature" + signature);
		model.addAttribute("appId",appId);
		model.addAttribute("timestamp",timestamp);
		model.addAttribute("nonceStr",nonceStr);
		model.addAttribute("signature",signature);
		log.error("进入注册页面");
		return "register_info-1.1";
	}
	
	/**
.
	 * 保存持有人个人信息
	 */
	@RequestMapping("saveInfo")
	@ResponseBody
	public String saveInfo(HttpServletRequest req,Model model,@ModelAttribute("user")HfUser user,
			@ModelAttribute("relations")String relations){
		HfUser loginUser = (HfUser) req.getSession().getAttribute("LoginUser");
		String msg = "";
		String code = req.getParameter("code");
		AjaxResutObject ro = new AjaxResutObject();
		if(StringUtils.isEmpty(relations)){
			ro.setMsg("关系必须填写");
			ro.setStatus(StaticVarible.ERROR);
			return JSONObject.fromObject(ro).toString();
		}
		if(StringUtils.isEmpty(user.getRealName())){
			ro.setMsg("姓名必须填写");
			ro.setStatus(StaticVarible.ERROR);
			return JSONObject.fromObject(ro).toString();
		}
		if(StringUtils.isEmpty(user.getMobile())){
			ro.setMsg("电话必须填写");
			ro.setStatus(StaticVarible.ERROR);
			return JSONObject.fromObject(ro).toString();
		}
		if(StringUtils.isEmpty(user.getCard())){
			ro.setMsg("身份证号必须填写");
			ro.setStatus(StaticVarible.ERROR);
			return JSONObject.fromObject(ro).toString();
		}
		String birthday = user.getBirthday();
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int age = 0;
		String age_str = "0";
		if(birthday.contains("-")){
			String[] arr = birthday.split("-");
			age_str = arr[0];
		}else{
			age_str = birthday.substring(0, 4);
			String month_str = birthday.substring(4,6);
			String day_str = birthday.substring(6,8);
			String birthday_str = age_str +"-"+month_str +"-"+day_str;
			user.setBirthday(birthday_str);
		}
		age = year - Integer.valueOf(age_str);
		if(age <0 || age >120){
			ro.setMsg("生日有误,请修改生日!");
			ro.setStatus(StaticVarible.ERROR);
			return JSONObject.fromObject(ro).toString();
		}
		user.setAge(age);
		/*String card = user.getCard().trim().replace(" ", "");
		String birthday = "";
		int age = 0;
		if(card != null && card != ""){
			if(card.length() == 18){
				birthday = card.substring(6, 14);
			}else if(card.length() == 15){
				birthday = 19 + card.substring(6, 12);
			}
			if(birthday.length() == 8){
				int user_year = Integer.valueOf(birthday.substring(0, 4));
				int user_month = Integer.valueOf(birthday.substring(4,6));
				int user_day = Integer.valueOf(birthday.substring(6));
				Calendar calendar = Calendar.getInstance();
				
				int year = calendar.get(Calendar.YEAR);
				if(user_year > 1900 && user_year < year && user_month > -1 && user_month < 13 && user_day > -1 && user_day <32){
					age = year - user_year;
				}else{
					ro.setMsg("身份证号有误");
					ro.setStatus(StaticVarible.ERROR);
					return JSONObject.fromObject(ro).toString();
				}
			}
			user.setAge(age);
			user.setBirthday(birthday);
		}*/
		if(!"".equals(code) && code != null){
			HfDevice device = new HfDevice();
			device.setCode(code);
			device.setCreateDate(new Date());
			device.setDeleteFlag(0);
			msg = indexService.addHolder(user, loginUser, device, relations);
		} else {
			msg = indexService.addHolder(user,loginUser, relations);
		}
		if(msg == ""){ //绑定成功
			ro.setStatus(StaticVarible.SUCCESS);
		}else{
			ro.setStatus(StaticVarible.ERROR);
			ro.setMsg(msg);
		}
		return JSONObject.fromObject(ro).toString();
	}
	
}
