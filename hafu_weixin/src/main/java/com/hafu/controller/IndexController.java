package com.hafu.controller;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;

import com.hafu.dto.UserContainDeviceDto;
import com.hafu.dto.UserDto;
import com.hafu.entity.HfBehaviorRecord;
import com.hafu.entity.HfCheckData;
import com.hafu.entity.HfConcernUser;
import com.hafu.entity.HfDevice;
import com.hafu.entity.HfEncouragement;
import com.hafu.entity.HfHappiness;
import com.hafu.entity.HfHealthPoint;
import com.hafu.entity.HfHealthTarget;
import com.hafu.entity.HfModuleSettings;
import com.hafu.entity.HfSportData;
import com.hafu.entity.HfTrack;
import com.hafu.entity.HfUser;
import com.hafu.service.BehaviorRecordService;
import com.hafu.service.EncouragementService;
import com.hafu.service.HfCodeService;
import com.hafu.service.HfHappinessService;
import com.hafu.service.HfHealthPointService;
import com.hafu.service.HfHealthTargetService;
import com.hafu.service.HfTrackService;
import com.hafu.service.IndexService;
import com.hafu.service.UserService;
import com.hafu.timer.TimerUtilCollection;
import com.hafu.utils.AjaxResutObject;
import com.hafu.utils.JsonDateValueProcessor;
import com.hafu.utils.StaticVarible;
import com.hafu.utils.WeatherApi;
import com.hafu.weixin.WeixinCommon;
import com.hafu.weixin.menu.pojo.JsTicket;
import com.hafu.weixin.util.SignUtil;
/**
 * 首页相关controller 
 * @author zc
 *
 */
@Controller
@RequestMapping(value="index")
public class IndexController {
	private static Logger log = LoggerFactory.getLogger(IndexController.class);
	private ResourceBundle bundle=PropertyResourceBundle.getBundle("smsconfig");
	private String defaultLng = bundle.getString("defaultLng");
	private String defaultLat = bundle.getString("defaultLat");
	private String defaultPosition = bundle.getString("defaultPosition");
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
	
	@Resource
	private HfCodeService codeService;
	@Autowired
	private HfHappinessService happinessService;
	/**
	 * 首页数据查询 
	 */
	@RequestMapping(value="/index")
	public String welcome(HttpServletRequest req,Model model){
		//当前行为？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？ 
		//当前位置 ？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？
//		log.debug("进入indexController***");
		HfUser lg = (HfUser) req.getSession().getAttribute("LoginUser");
		Long userId = lg.getId();
//		log.debug("关注人ID***"+userId);
		UserDto cur = (UserDto) req.getSession().getAttribute("CurrentUser");
		//根据生日获取年龄
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			String age_str = "0";
			String birthday = "";
			String cur_birthday = cur.getBirthday();
			if(cur_birthday == null || cur_birthday.length() == 0){
				//根据身份证获取生日
				String card = cur.getCard().trim().replace(" ", "");
				if(card != null && card != ""){
					if(card.length() == 18){
						birthday = card.substring(6, 14);
					}else if(card.length() == 15){
						birthday = 19 + card.substring(6, 12);
					}
					String year_str = birthday.substring(0,4);
					String month_str = birthday.substring(4,6);
					String day_str = birthday.substring(6,8);
					String birthday_str = year_str +"-"+month_str +"-"+day_str;
					cur.setBirthday(birthday_str);
					birthday = cur.getBirthday();
					String[] arr = birthday.split("-");
					age_str = arr[0];
					int age = year - Integer.valueOf(age_str);
					if(age <0 || age >120){
						age = 0;
					}
					cur.setAge(age);
					req.getSession().setAttribute("CurrentUser", cur);
				}else{
					log.error("用户:"+cur.getId()+"身份证为空!");
				}
			}else{
				birthday = cur.getBirthday();
				if(!birthday.contains("-")){
					String year_str = birthday.substring(0,4);
					String month_str = birthday.substring(4,6);
					String day_str = birthday.substring(6,8);
					String birthday_str = year_str +"-"+month_str +"-"+day_str;
					cur.setBirthday(birthday_str);
				}
				birthday = cur.getBirthday();
				String[] arr = birthday.split("-");
				age_str = arr[0];
				int age = year - Integer.valueOf(age_str);
				if(age <0 || age >120){
					age = 0;
				}
				cur.setAge(age);
				req.getSession().setAttribute("CurrentUser", cur);
			}
		Long currentId = cur.getId();
//		log.debug("用户ID***"+currentId);
		//获取用户与最新关注人的关系
//		log.debug("关注人ID***"+ userId);
		HfConcernUser hfConcernUser = indexService.getConcernUser(currentId,userId);
		req.getSession().setAttribute("hfConcernUser", hfConcernUser);
		model.addAttribute("hfConcernUser", hfConcernUser);
//		log.debug("关注人与用户的关系***"+hfConcernUser.getRelations());
		
		String code = cur.getCode();
//		log.debug("用户的设备码***"+code);
		HfHealthPoint point = null;
		HfTrack track = null;
		if(code != null && !code.equals("")){
//			log.debug("此持有人已经绑定设备");
			point = pointService.lastHealthPoint(currentId,cur.getCode());//健康评分
//			log.debug("健康评分***"+point.toString());
//			log.debug("健康综合评分***"+point.getPoint());
			track = trackService.lastTrack(cur.getCode()); //当前轨迹
//			log.debug("当前轨迹***"+track.toString());
		}
		//设置默认坐标值
		if(track == null){
			track = new HfTrack();
			track.setLatitude(defaultLat);
			track.setLongitude(defaultLng);
			track.setPosition(defaultPosition);
		}
		model.addAttribute("point",point);
		model.addAttribute("track",track);
		//添加天气默认值
		WeatherApi api=null;
//		do{
			api=WeatherApi.getInstance(Double.valueOf(defaultLng), Double.valueOf(defaultLat));
			if(api.getWeather() == "" || api.getTemperature() == 0){
				api.setWeather("多云转晴");
				api.setTemperature(0);
			}
//		}while(api.getWeather() == "" || api.getTemperature() == 0);
//		log.debug("***weather:"+api.getWeather());
//		log.debug("***tmp:"+api.getTemperature());
		model.addAttribute("weather", api.getWeather());
		model.addAttribute("temperature", (int)api.getTemperature());
		//获取关爱和鼓励
		Page<HfEncouragement> page=encouragementService.todayEncourageList(currentId, code, 1, 10);	
		int care = 0;
		int encourage = 0;
		List<HfEncouragement> hfEncouragement_list = page.getContent();
		for(int i=0;i<hfEncouragement_list.size();i++){
			HfEncouragement encouragement = hfEncouragement_list.get(i);
			if(encouragement.getEncouragementType() == 1){
				encourage = encourage + 1;
			}else if(encouragement.getEncouragementType() == 2){
				care = care + 1;
			}else{
				
			}
		}
//		log.debug("获取care***"+care);
//		log.debug("获取encourage***"+encourage);
		model.addAttribute("care", care);
		model.addAttribute("encourage", encourage);
		//获取快乐指数
		
		String createDay = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		List<HfHappiness> happinessList = happinessService.findHappiness(currentId, createDay , code);
		int happiness_score = 0;
		if(happinessList != null && happinessList.size() != 0){
			int score = happinessList.get(0).getHappiness();
			if(score != 0){
				happiness_score = score;
			}
		}
		model.addAttribute("happy_score", happiness_score);
		
		HfSportData sport = indexService.indexLastSport(currentId); //运动数据
		model.addAttribute("sport",sport);
//		log.debug("目标运动数据***"+sport.toString());
		
		JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(java.util.Date.class,new JsonDateValueProcessor("yyyy-MM-dd"));
        
		List<HfCheckData> check = indexService.indexLastCheck(currentId); //检测数据
		model.addAttribute("check",JSONArray.fromObject(check,jsonConfig).toString());
//		log.debug("检测数据***"+check.toString());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		List<UserDto> bangDevice = indexService.deviceList(map); //首页已绑定设备（人）
		model.addAttribute("bangDevice",bangDevice);
		
		
		
		String appId =  WeixinCommon.APPID; //扫一扫
		String timestamp = Long.toString(System.currentTimeMillis() / 1000);
		String nonceStr = UUID.randomUUID().toString();
		JsTicket ticket = TimerUtilCollection.jsTicket;
		String string1 = "jsapi_ticket="+ticket.getTicket()+
				"&noncestr="+nonceStr+"&timestamp="+timestamp+
				"&url="+WeixinCommon.MENU_BASE+"/index/index";
		String signature = SignUtil.sha1Sign(string1);
		StringBuffer buff=new StringBuffer();
		if(StringUtils.isNotBlank(cur.getProvince())){
			buff.append(cur.getProvince());
		}
		if(StringUtils.isNotBlank(cur.getCity())){
			buff.append(cur.getCity());
		}
		if(StringUtils.isNotBlank(cur.getArea())){
			buff.append(cur.getArea());
		}
		if(StringUtils.isNotBlank(cur.getAddress())){
			buff.append(cur.getAddress());
		}
		model.addAttribute("address",buff.toString());
		model.addAttribute("appId",appId);
		model.addAttribute("timestamp",timestamp);
		model.addAttribute("nonceStr",nonceStr);
		model.addAttribute("signature",signature);
		//首页设置
		List<HfModuleSettings> setting = indexService.getModuleSettings(userId);
		model.addAttribute("setting",JSONArray.fromObject(setting,jsonConfig).toString());
		return "../index";
	}
	
	
	/**
	 * 注册持有人信息下一步 - 添加设备
	 */
	@RequestMapping("banding")
	public String banding(HttpServletRequest req,Model model){
		HfUser loginUser = (HfUser) req.getSession().getAttribute("LoginUser");
		UserDto holder = indexService.myBangHolder(loginUser.getId()).get(0);
		//先判断持有人是否已经绑定了设备 
		HfDevice holderDevice = indexService.currentUserDevice(holder.getDeviceId());
		if(holderDevice!=null){//有已绑定了设备直接跳首页，无需再次填写设备 
			return "redirect:/index/index";
		}
		model.addAttribute("ob",holder);
		//调取扫一扫所需
		String appId =  WeixinCommon.APPID;
		String timestamp = Long.toString(System.currentTimeMillis() / 1000);
		String nonceStr = UUID.randomUUID().toString();
		JsTicket ticket = TimerUtilCollection.jsTicket;
		String string1 = "jsapi_ticket="+ticket.getTicket()+
				"&noncestr="+nonceStr+"&timestamp="+timestamp+
				"&url="+WeixinCommon.MENU_BASE+"/index/banding";
		String signature = SignUtil.sha1Sign(string1);
		model.addAttribute("appId",appId);
		model.addAttribute("timestamp",timestamp);
		model.addAttribute("nonceStr",nonceStr);
		model.addAttribute("signature",signature);
		model.addAttribute("msg",req.getParameter("msg"));
		return "register_next";
	}
	
	
	/**
	 * 添加设备 
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="addDevice")
	@ResponseBody
	public String addDevice(HttpServletRequest req,Model model) throws UnsupportedEncodingException{
		AjaxResutObject ob = new AjaxResutObject();
		HfUser loginUser = (HfUser) req.getSession().getAttribute("LoginUser");
		String code = req.getParameter("code");
		String holderId = req.getParameter("holderId");
		if(StringUtils.isEmpty(code)){
			ob.setStatus(StaticVarible.ERROR);
			ob.setMsg("设备号必须输入！");
			return JSONObject.fromObject(ob).toString();
		}
		if(StringUtils.isEmpty(holderId)){
			ob.setStatus(StaticVarible.ERROR);
			ob.setMsg("非法请求！");
			return JSONObject.fromObject(ob).toString();
		}
		HfUser holder = userService.getUser(Long.parseLong(holderId));
		HfDevice device = new HfDevice();
		device.setCode(code);
		device.setCreateDate(new Date());
		device.setDeleteFlag(0);
		device.setUserId(Long.parseLong(holderId));
		String msg = indexService.addFirstDevice(loginUser,holder,device);
		if(!msg.equals("")){
			ob.setStatus(StaticVarible.ERROR);
			ob.setMsg(msg);
			return JSONObject.fromObject(ob).toString();
		}
		//保存最新绑定的设备到sesssion - currentUser 
		Map map = new HashMap();
		map.put("userId", loginUser.getId());
		UserDto current = userService.getLastBandingUser(map);
		req.getSession().setAttribute("CurrentUser", current);
		ob.setStatus(StaticVarible.SUCCESS);
		return JSONObject.fromObject(ob).toString();
		
	}
	
	/**
	 * 跳入修改持有人信息 页面
	 */
	/*@RequestMapping("toUpdateHolder")
	public String toUpdateHolder(HttpServletRequest req,Model model){
		HttpSession session = req.getSession();
		HfUser loginUser= (HfUser) session.getAttribute("LoginUser"); //当前被关注人 
		Map<String ,Object> map = new HashMap<String, Object>();
		UserDto cur = (UserDto) req.getSession().getAttribute("CurrentUser");
		Long id = cur.getId();
		map.put("userId", id);
		map.put("concernUserId", loginUser.getId());
		UserContainDeviceDto ob = indexService.getUserInfoById(map);
		model.addAttribute("ob",ob);
		return "update_user_info";
	}*/
	
	@RequestMapping("toUpdateHolder/{id}")
	public String toUpdateHolder(HttpServletRequest req,Model model,@PathVariable("id")Long id){
		log.debug("进入页面");
		HttpSession session = req.getSession();
		HfUser loginUser= (HfUser) session.getAttribute("LoginUser"); //当前被关注人 
		Map<String ,Object> map = new HashMap<String, Object>();
		map.put("userId", id);
		map.put("concernUserId", loginUser.getId());
		UserContainDeviceDto ob = indexService.getUserInfoById(map);
		//根据生日获取年龄
					Calendar calendar = Calendar.getInstance();
					int year = calendar.get(Calendar.YEAR);
					String age_str = "0";
					String birthday = "";
					//log.debug("***birthday:"+ob.getBirthday());
					String ob_birthday = ob.getBirthday();
					if(ob_birthday == null || ob_birthday.length() ==0){
						//根据身份证获取生日
						String card = ob.getCard().trim().replace(" ", "");
						if(card != null && card != ""){
							if(card.length() == 18){
								birthday = card.substring(6, 14);
							}else if(card.length() == 15){
								birthday = 19 + card.substring(6, 12);
							}
							String year_str = birthday.substring(0,4);
							String month_str = birthday.substring(4,6);
							String day_str = birthday.substring(6,8);
							String birthday_str = year_str +"-"+month_str +"-"+day_str;
							ob.setBirthday(birthday_str);
							birthday = ob.getBirthday();
							String[] arr = birthday.split("-");
							age_str = arr[0];
							int age = year - Integer.valueOf(age_str);
							if(age <0 || age >120){
								age = 0;
							}
							ob.setAge(age);
						}else{
							log.error("用户:"+ob.getId()+"身份证为空!");
						}
					}else{
						birthday = ob.getBirthday();
						if(!birthday.contains("-")){
							String year_str = birthday.substring(0,4);
							String month_str = birthday.substring(4,6);
							String day_str = birthday.substring(6,8);
							String birthday_str = year_str +"-"+month_str +"-"+day_str;
							ob.setBirthday(birthday_str);
						}
						birthday = ob.getBirthday();
						String[] arr = birthday.split("-");
						age_str = arr[0];
						int age = year - Integer.valueOf(age_str);
						if(age <0 || age >120){
							age = 0;
						}
						ob.setAge(age);
					}
				log.debug("***birthday:"+ob.getBirthday());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		model.addAttribute("year", sdf.format(new Date()));
//		System.out.println("--------------------年---------------------："+year.format(new Date()));			
//		System.out.println("--------------------年龄---------------------："+ob.getAge());
		model.addAttribute("ob",ob);
		return "update_user_info";
	}
	
	/**
	 * 更新设备持有人个人信息
	 */
	/*@RequestMapping("updateInfo")
	@ResponseBody
	public String updateInfo(HttpServletRequest req,Model model,@ModelAttribute("user")HfUser user,
			@ModelAttribute HfHealthTarget healthTarget){
		HfUser loginUser = (HfUser) req.getSession().getAttribute("LoginUser");
		UserDto current = (UserDto) req.getSession()
				.getAttribute("CurrentUser");
		String msg = "";
		AjaxResutObject ro = new AjaxResutObject();
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
		msg = indexService.updateUserInfo(user,loginUser);
		if(msg == ""){
			if(current.getId().intValue()==user.getId().intValue()){
				current.setNickName(user.getNickName());
				current.setRealName(user.getRealName());
				current.setCard(user.getCard());
				current.setGender(user.getGender());
				current.setAge(user.getAge());
				current.setMobile(user.getMobile());
				current.setProvince(user.getProvince());
				current.setCity(user.getCity());
				current.setArea(user.getArea());
				current.setAddress(user.getAddress());
			}
			ro.setStatus(StaticVarible.SUCCESS);
		}else{
			ro.setStatus(StaticVarible.ERROR);
			ro.setMsg(msg);
		}
		if (healthTarget.getSleepTime() != null
				&& healthTarget.getStaticCount() != null
				&& healthTarget.getStepCount() != null) {
			healthTarget.setId(null);
			healthTarget.setDeleteFlag(0);
			healthTarget.setUserId(current.getId());
			healthTarget.setCode(current.getCode());
			healthTargetService.saveHealthTarget(healthTarget);
		}
		return JSONObject.fromObject(ro).toString();
	}*/
	@RequestMapping("updateInfo")
	@ResponseBody
	public String updateInfo(HttpServletRequest req,Model model,@ModelAttribute("user")HfUser user,
			@ModelAttribute AjaxResutObject ajaxOb){
		HfUser loginUser = (HfUser) req.getSession().getAttribute("LoginUser");
		UserDto current = (UserDto) req.getSession()
				.getAttribute("CurrentUser");
		String msg = "";
		AjaxResutObject ro = new AjaxResutObject();
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
		//log.debug("更新用户信息：用户生日"+user.getBirthday());
		msg = indexService.updateUserInfo(user,loginUser);
		if(msg == ""){
			if(current.getId().intValue()==user.getId().intValue()){
				current.setNickName(user.getNickName());
				current.setRealName(user.getRealName());
				current.setCard(user.getCard());
				current.setGender(user.getGender());
				current.setBirthday(user.getBirthday());
				current.setAge(user.getAge());
				current.setMobile(user.getMobile());
				current.setProvince(user.getProvince());
				current.setCity(user.getCity());
				current.setArea(user.getArea());
				current.setAddress(user.getAddress());
			}
			ro.setStatus(StaticVarible.SUCCESS);
		}else{
			ro.setStatus(StaticVarible.ERROR);
			ro.setMsg(msg);
		}
		String json = (String) ajaxOb.getOb();
		JSONArray array =  JSONArray.fromObject(json);
			for(int i = 0; i<array.size(); i++){
				JSONObject ob = (JSONObject) array.get(i);
				HfHealthTarget healthTarget = new HfHealthTarget();
				if(StringUtils.isEmpty(ob.getString("sleepTime"))){
					healthTarget.setSleepTime((double)0);
				}else{
					healthTarget.setSleepTime(Double.valueOf( ob.getString("sleepTime")));
				}
				if(StringUtils.isEmpty(ob.getString("stepCount"))){
					healthTarget.setStepCount(0);
				}else{
					healthTarget.setStepCount(Integer.valueOf(ob.getString("stepCount")));
				}
				if(StringUtils.isEmpty(ob.getString("staticCount"))){
					healthTarget.setStaticCount(0);
				}else{
					healthTarget.setStaticCount(Integer.valueOf(ob.getString("staticCount")));
				}
				healthTarget.setDataType(ob.getInt("dataType"));
				healthTarget.setDeleteFlag(0);
				healthTarget.setUserId(current.getId());
				healthTarget.setCode(current.getCode());
				healthTargetService.saveHealthTarget(healthTarget);
			}
		return JSONObject.fromObject(ro).toString();
	}
	
	/**
	 * 首页设备列表(通过c:import 导入)
	 */
	@RequestMapping("deviceList")
	public String deviceList(HttpServletRequest req,Model model){
		HfUser loginUser = (HfUser) req.getSession().getAttribute("LoginUser");
		Long userId = loginUser.getId();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		List<UserContainDeviceDto> list = indexService.deviceUserList(map);
		model.addAttribute("list",list);
		return "index/index_device_list";
	}
	
	/***
	 * 异步获取扫一扫需要的参数
	 */
	@RequestMapping("jsTicket")
	@ResponseBody
	public String jsTicket(HttpServletRequest req,Model model){
		String url = req.getParameter("url");
		//调取扫一扫所需
		String appId =  WeixinCommon.APPID;
		String timestamp = Long.toString(System.currentTimeMillis() / 1000);
		String nonceStr = UUID.randomUUID().toString();
		JsTicket ticket = TimerUtilCollection.jsTicket;
		String string1 = "jsapi_ticket="+ticket.getTicket()+
				"&noncestr="+nonceStr+"&timestamp="+timestamp+
				"&url="+url;
		String signature = SignUtil.sha1Sign(string1);
		JSONObject ob = new JSONObject();
		ob.put("appId",appId);
		ob.put("timestamp",timestamp);
		ob.put("nonceStr",nonceStr);
		ob.put("signature",signature);
		return ob.toString();
	}
	
	/**
	 * 根据地理位置获取天气信息
	 * @param lng
	 * @param lat
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getWeather",method=RequestMethod.POST)
	public String getWeather(@RequestParam(value="lng")String lng,@RequestParam(value="lat")String lat){
//		log.debug("经纬度****"+lng+":"+lat);
		AjaxResutObject ro = new AjaxResutObject();
		try{
			if(StringUtils.isEmpty(lng) || StringUtils.isEmpty(lat)){
				ro.setMsg("参数错误");
				ro.setStatus(StaticVarible.ERROR);
				return JSONObject.fromObject(ro).toString();
			}
			//获取天气10次，获取不到停止
			WeatherApi api=null;
			int i = 0;
			do{
				i = i + 1;
//				log.debug("获取天气第**"+i+"**次");
				if(i == 11 ){
					ro.setStatus(StaticVarible.ERROR);
					ro.setMsg("天气刷新失败，网络真不给力！");
					ro.setOb(api);
					log.debug("天气回调:****"+JSONObject.fromObject(ro).toString());
					return JSONObject.fromObject(ro).toString();
				}
				api=WeatherApi.getInstance(Double.valueOf(defaultLng), Double.valueOf(defaultLat));
				
			}while(api.getWeather() == "" || api.getTemperature() == 0);
			ro.setStatus(StaticVarible.SUCCESS);
			ro.setOb(api);
//			log.debug("天气回调:****"+JSONObject.fromObject(ro).toString());
			return JSONObject.fromObject(ro).toString();
		}catch(Exception e){
			e.printStackTrace();
			ro.setStatus(StaticVarible.ERROR);
			return JSONObject.fromObject(ro).toString();
		}
	}
	
	
	
	/**
	 * 异步添加设备
	 */
	@RequestMapping(value="ajaxAddDevice")
	@ResponseBody
	public String ajaxAddDevice(HttpServletRequest req,Model model){
//		log.debug("***进入异步添加设备***");
		AjaxResutObject ro = new AjaxResutObject();
		HfUser loginUser = (HfUser) req.getSession().getAttribute("LoginUser");
		String code = req.getParameter("code");
		String holderId = req.getParameter("holderId");
		if(StringUtils.isEmpty(code)){
			ro.setMsg("参数错误");
			ro.setStatus(StaticVarible.ERROR);
			return JSONObject.fromObject(ro).toString();
		}
		if(StringUtils.isEmpty(holderId)){
			ro.setMsg("参数错误");
			ro.setStatus(StaticVarible.ERROR);
			return JSONObject.fromObject(ro).toString();
		}
		HfUser holder = userService.getUser(Long.parseLong(holderId));
		HfDevice device = new HfDevice();
		device.setCode(code);
		device.setCreateDate(new Date());
		device.setDeleteFlag(0);
		device.setUserId(Long.parseLong(holderId));
		String msg = indexService.addDevice(loginUser,holder,device);
		if(!msg.equals("")){
			ro.setMsg(msg);
			ro.setStatus(StaticVarible.ERROR);
			return JSONObject.fromObject(ro).toString();
		}
		codeService.saveCode(device.getCode());//统计
		//保存最新绑定的设备到sesssion - currentUser 
		UserDto current = userService.getUserAndDevice(device.getUserId(), loginUser.getId());//indexService.getDeviceAndUserByCode(device.getCode());
		req.getSession().setAttribute("CurrentUser", current);
		ro.setMsg("绑定成功");
		ro.setStatus(StaticVarible.SUCCESS);
		ro.setOb(device);
		return JSONObject.fromObject(ro).toString();
		
	}
	
	
	
	
	/**
	 * 异步获取首页 轨迹
	 */
	@RequestMapping("indexTrack")
	@ResponseBody
	public String indexTrack(HttpServletRequest req,Model model){
		AjaxResutObject ob = new AjaxResutObject();
		UserDto cur = (UserDto) req.getSession().getAttribute("CurrentUser");
		Long currentId = cur.getId();
		HfTrack track = trackService.lastTrack(cur.getCode()); //当前轨迹
		if(track!=null){
			ob.setStatus(StaticVarible.SUCCESS);
			ob.setMsg("查询成功");
			ob.setOb(track);
		}else{
			ob.setStatus(StaticVarible.ERROR);
			ob.setMsg("暂无记录");
		}
		return JSONObject.fromObject(ob).toString();
	}
	@RequestMapping("getBaseInfo")
	@ResponseBody
	public String getBaseInfo(HttpSession session){
		AjaxResutObject ob = new AjaxResutObject();
		Map<String,Object> map = new HashMap<String,Object>();
		ServletContext servletContext= session.getServletContext();
		String remoteImage = servletContext.getInitParameter("remoteImage");
//		log.debug("****获取初始化参数，图片服务器路径");
		UserDto cur = (UserDto) session.getAttribute("CurrentUser");
		HfUser user = userService.getUser(cur.getId());
//		log.debug("用户ID："+cur.getId());
//		log.debug("获取的用户："+user.toString());
		map.put("user", user);
		String name = cur.getName();
		String nickName = cur.getNickName();
		if(name != null && name != ""){
			map.put("name", name);
		}else if(nickName != null && nickName != ""){
			map.put("name", nickName);
		}else{
			map.put("name", null);
		}
		String gender = cur.getGender();
		map.put("sex", gender);
		String avater = cur.getAvater();
		if(avater == null || avater.equals("")){
			map.put("avater", null);
		}else{
			map.put("avater", remoteImage +"/" + avater);
		}
		
//		log.debug("****获取头像完整路径");
		HfConcernUser hfConcernUser = (HfConcernUser) session.getAttribute("hfConcernUser");
		String relations = hfConcernUser.getRelations();
		if(relations == null || relations.equals("")){
			relations = "亲人";
		}
		map.put("relations", relations);
		ob.setOb(map);
//		log.debug("****信息弹框返回数据***"+JSONObject.fromObject(ob).toString());
		return JSONObject.fromObject(ob).toString();
	}
	/**
	 * 异步获取首页运动弹窗数据
	 * @param req
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping("getStepPageData")
	@ResponseBody
	public String getStepPageData(HttpServletRequest req,HttpSession session,Model model){
//		log.debug("****进入步数弹框信息获取");
		AjaxResutObject ob = new AjaxResutObject();
		Map<String,Object> map = new HashMap<String,Object>();
		ServletContext servletContext=req.getSession().getServletContext();
		String remoteImage = servletContext.getInitParameter("remoteImage");
//		log.debug("****获取初始化参数，图片服务器路径");
		//WebApplicationContext webApplicationContext = (WebApplicationContext)servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		UserDto cur = (UserDto) session.getAttribute("CurrentUser");
		String gender = cur.getGender();
		map.put("sex", gender);
		String avater = cur.getAvater();
		if(avater == null || avater.equals("")){
			map.put("avater", null);
		}else{
			map.put("avater", remoteImage +"/" + avater);
		}
		
//		log.debug("****获取头像完整路径");
		HfConcernUser hfConcernUser = (HfConcernUser) session.getAttribute("hfConcernUser");
		String nickName = hfConcernUser.getNickName();
		if(nickName == null || nickName.length() == 0){
			nickName = cur.getRealName();
		}
		map.put("nickName", nickName);
		//获取今日步数
		String code = cur.getCode();
		Long currentId = cur.getId();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date dateL=new Date();
		String dateStr = sdf.format(dateL);
		String endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateL);
		List<HfBehaviorRecord> behavior_list = behaviorService.behaviorList(currentId , dateStr, code , endDate);
		String regEx="[^0-9]"; 
		Pattern p = Pattern.compile(regEx);   
		Matcher m = null;
		String step = "";
		int stepNum = 0;
		if(behavior_list != null && behavior_list.size() != 0){
			for(int i=0;i<behavior_list.size();i++){
				HfBehaviorRecord bh = behavior_list.get(i);
//				log.debug("行为列表:"+i+"***"+JSONObject.fromObject(bh).toString());
				String bh_type = bh.getBehaviorType();
				if(StringUtils.isNotEmpty(bh_type)){
					if(bh_type.contains("走") || bh_type.contains("步行")){
						m = p.matcher(bh_type);
						step = m.replaceAll("").trim();
						if(!StringUtils.isEmpty(step)){
							stepNum = stepNum + Integer.parseInt(step);
						}else{
							
						}
						
					}
					
				}
			}
			
		}else{
			
		}
		map.put("todayStep", stepNum);
		ob.setOb(map);
		String score = "";
		if(stepNum<100){
			score = "5%";
		}else if(stepNum>99 && stepNum<1000){
			score = "10%";
		}else if(stepNum>999 && stepNum<2000){
			score = "30%";
		}else if(stepNum>1999 && stepNum<3000){
			score = "50%";
		}else if(stepNum>2999 && stepNum<5000){
			score = "70%";
		}else if(stepNum>4999 && stepNum<6000){
			score = "90%";
		}else if(stepNum>5999 && stepNum<6500){
			score = "95%";
		}else{
			score = "100%";
		}
		map.put("score", score);
//		log.debug("****首页步数弹框返回数据***"+JSONObject.fromObject(ob).toString());
		return JSONObject.fromObject(ob).toString();
	}
	/**
	 * 获取快乐指数笑脸弹窗页面数据
	 * @return
	 */
	@RequestMapping("happyPageData")
	@ResponseBody
	public String getHappyPageData(HttpSession session,Model model){
		UserDto cur = (UserDto) session.getAttribute("CurrentUser");
//		log.debug("****进入一周快乐指数笑脸弹窗数据回调");
		AjaxResutObject ob = new AjaxResutObject();
		String createDay_today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		Map<String,Object> map = new HashMap<String, Object>();
		//获取今日的快乐指数
		List<HfHappiness> happinessList = happinessService.findHappiness(cur.getId(), createDay_today  , cur.getCode());
		int today_happy_score = 0;
		if(happinessList != null && happinessList.size() != 0){
			today_happy_score = happinessList.get(0).getHappiness();
		}
		map.put("today_happy_score", today_happy_score);
		int h = today_happy_score/10;
		String msg = "";
		//根据快乐指数判断  被关注人的心情   划分快乐等级
	       switch (h) {
		    case 10:
		    	msg = "非常快乐,感觉今天是一生中最快乐的时光!"; 
			 break;
		    case 9:
		    	msg = "很快乐,有点心旷神怡的感觉！"; 
			 break;
		    case 8:
		    	msg = "快乐,情绪高昂 ！"; 
			 break;
		    case 7:
		    	msg = "中度快乐,感觉还不错，心情愉悦 。"; 
			 break;
		    case 6:
		    	msg = "有点开心,比一般人快乐一点。"; 
			 break;
		    case 5:
		    	msg = "持平,心情一般般,感觉不特别幸福也不特别不快乐。"; 
			 break;
		    case 4:
		    	msg = "有点不快乐。"; 
			 break;
		    case 3:
		    	msg = "中度不快乐,感觉心情有点低落。"; 
			 break;
		    case 2:
		    	msg = "不快乐,感觉心情不好，提不起劲。"; 
			 break;
		    case 1:
		    	msg = "很不快乐，感觉到抑郁，沉闷。"; 
			 break;
		    case 0:
		    	msg = "非常不快乐,心情很低落。"; 
			 break;
		    default:
		    	msg = "暂无记载"; 
			break;
		}
	    ob.setMsg(msg);
		HfConcernUser hfConcernUser = (HfConcernUser) session.getAttribute("hfConcernUser");
		String nickName = hfConcernUser.getNickName();
		if(nickName == null || nickName.length() == 0){
			nickName = cur.getRealName();
		}
		map.put("nickName", nickName);
		ob.setOb(map);
		return JSONObject.fromObject(ob).toString();
	}
	/**
	 * 获取一周快乐指数
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping("happyScore")
	@ResponseBody
	public String getWeekHappy(HttpSession session,Model model){
//		log.debug("****进入一周快乐指数回调");
		AjaxResutObject ob = new AjaxResutObject();
		//获取行为列表
				UserDto cur = (UserDto) session.getAttribute("CurrentUser");
				String code = cur.getCode();
				Long currentId = cur.getId();
				Calendar cal = Calendar.getInstance();
				int hours = cal.get(Calendar.HOUR_OF_DAY);
				int minutes = cal.get(Calendar.MINUTE);
				int seconds = cal.get(Calendar.SECOND);
				int milliSecond = cal.get(Calendar.MILLISECOND);
				long todayMilliSecond = ((hours*60+minutes)*60+seconds)*1000+milliSecond;
				long everydayMilliSecond = 24*60*60*1000;
				long now = System.currentTimeMillis();
				SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd");
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
				List happyList = new ArrayList();
				List dateList = new ArrayList();
				for(int i=0;i<6;i++){
					long time = now - todayMilliSecond-1000 - i*everydayMilliSecond;//59秒
					Date date = new Date(time);
					String createDay = sdf2.format(date);
					String happyDate = sdf1.format(date);
					List<HfHappiness> happinessList = happinessService.findHappiness(currentId, createDay , code);
					int happiness_score = 0;
					if(happinessList != null && happinessList.size() != 0){
						happiness_score = happinessList.get(0).getHappiness();
					}
					happyList.add(happiness_score);
					dateList.add(happyDate);
				}
				List list_happy = new ArrayList();
				for(int k=5;k>-1;k--){
					list_happy.add(happyList.get(k));
				}
				List list_date = new ArrayList();
				for(int k=5;k>-1;k--){
					list_date.add(dateList.get(k));
				}
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("week_happy", list_happy);
				map.put("date_happy", list_date);
				ob.setOb(map);
				String createDay_today = sdf2.format(new Date());
				//获取今日的快乐指数
				List<HfHappiness> happinessList = happinessService.findHappiness(currentId, createDay_today  , code);
				int today_happy_score = 0;
				if(happinessList != null && happinessList.size() != 0){
					today_happy_score = happinessList.get(0).getHappiness();
				}
				int n = today_happy_score/10;
				int m = (int) (n * 10 + Math.random()*11);
				ob.setMsg(m + "%");
				
				/*//获取快乐指数页面数据
				Map<String,Object> map2 = new HashMap<String,Object>();
				ServletContext servletContext=session.getServletContext();
				String remoteImage = servletContext.getInitParameter("remoteImage");
				log.debug("****获取初始化参数，图片服务器路径");
				String gender = cur.getGender();
				map2.put("sex", gender);
				String avater = cur.getAvater();
				if(avater == null || avater.equals("")){
					map2.put("avater", null);
				}else{
					map2.put("avater", remoteImage +"/" + avater);
				}
				
				log.debug("****获取头像完整路径");
				HfConcernUser hfConcernUser = (HfConcernUser) session.getAttribute("hfConcernUser");
				String relations = hfConcernUser.getRelations();
				if(relations == null || relations.equals("")){
					relations = "亲人";
				}
				ob.setMsg("relations");
				map2.put("relations", relations);
				List dataList = new ArrayList();
				dataList.add(map2);
				ob.setList(dataList);*/
//				log.debug("***快乐指数返回数据***"+JSONObject.fromObject(ob).toString());
		return JSONObject.fromObject(ob).toString();
	}
	/**
	 * 异步获取一周步数
	 * @return
	 */
	@RequestMapping("weekStep")
	@ResponseBody
	public String getWeekStep(HttpSession session,Model model){
//		log.debug("behavior:****进入一周步数回调");
		AjaxResutObject ob = new AjaxResutObject();
		//获取行为列表
		UserDto cur = (UserDto) session.getAttribute("CurrentUser");
		String code = cur.getCode();
		Long currentId = cur.getId();
		Calendar cal = Calendar.getInstance();
		int hours = cal.get(Calendar.HOUR_OF_DAY);
		int minutes = cal.get(Calendar.MINUTE);
		int seconds = cal.get(Calendar.SECOND);
		int milliSecond = cal.get(Calendar.MILLISECOND);
		long todayMilliSecond = ((hours*60+minutes)*60+seconds)*1000+milliSecond;
		long everydayMilliSecond = 24*60*60*1000;
		long now = System.currentTimeMillis();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		List list = new ArrayList();
		for(int i=0;i<6;i++){
			long time = now - todayMilliSecond-1000 - i*everydayMilliSecond;//59秒
			Date date = new Date(time);
			String dateStr = new SimpleDateFormat("MM-dd").format(date);
			String startDate = sdf1.format(date);
			String endDate = sdf2.format(date);
			List<HfBehaviorRecord> behavior_list = behaviorService.behaviorList(currentId , startDate, code , endDate);
			System.out.println(dateStr);
			System.out.println(endDate);
			String regEx="[^0-9]"; 
			Pattern p = Pattern.compile(regEx);   
			Matcher m = null;
			String step = "";
			int stepNum = 0;
			if(behavior_list != null && behavior_list.size() != 0){
				for(int j=0;j<behavior_list.size();j++){
					HfBehaviorRecord bh = behavior_list.get(j);
					String bh_type = bh.getBehaviorType();
					if(StringUtils.isNotEmpty(bh_type)){
						if(bh_type.contains("走") || bh_type.contains("步行")){
							m = p.matcher(bh_type);
							step = m.replaceAll("").trim();
							if(!StringUtils.isEmpty(step)){
								stepNum = stepNum + Integer.parseInt(step);
							}else{
								
							}
							
						}
						
					}
				}
				
			}else{
				
			}
			Map<String,String> map = new HashMap<String,String>();
			map.put("stepDate", dateStr);
			map.put("stepData", String.valueOf(stepNum));
			list.add(map);
		}
		List list2 = new ArrayList();
		for(int k=5;k>-1;k--){
			list2.add(list.get(k));
		}
		ob.setOb(list2);
//		log.debug("****一周步数返回数据"+JSONObject.fromObject(ob).toString());
		return JSONObject.fromObject(ob).toString();
	}
	/**
	 * 异步获取今日步数
	 * @return
	 */
	@RequestMapping("dayStep")
	@ResponseBody
	public String getDayStep(HttpSession session,Model model){
		log.debug("behavior:****进入步数回调");
		AjaxResutObject ob = new AjaxResutObject();
		//获取行为列表
		UserDto cur = (UserDto) session.getAttribute("CurrentUser");
		String code = cur.getCode();
		Long currentId = cur.getId();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date dateL=new Date();
		String dateStr = sdf.format(dateL);
		String endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateL);
		List<HfBehaviorRecord> behavior_list = behaviorService.behaviorList(currentId , dateStr, code , endDate);
		String regEx="[^0-9]"; 
		Pattern p = Pattern.compile(regEx);   
		Matcher m = null;
		String step = "";
		int stepNum = 0;
		if(behavior_list != null && behavior_list.size() != 0){
			for(int i=0;i<behavior_list.size();i++){
				HfBehaviorRecord bh = behavior_list.get(i);
				//log.debug("行为列表:"+i+"***"+JSONObject.fromObject(bh).toString());
				String bh_type = bh.getBehaviorType();
				if(StringUtils.isNotEmpty(bh_type)){
					if(bh_type.contains("走") || bh_type.contains("步行")){
						m = p.matcher(bh_type);
						step = m.replaceAll("").trim();
						if(!StringUtils.isEmpty(step)){
							stepNum = stepNum + Integer.parseInt(step);
						}else{
							
						}
						
					}
					
				}
			}
			ob.setStatus(StaticVarible.SUCCESS);
			ob.setMsg(String.valueOf(stepNum));
			ob.setOb(stepNum);
		}else{
			ob.setStatus(StaticVarible.ERROR);
			ob.setMsg("暂无记录");
		}
		
//		log.debug("步数返回信息***"+JSONObject.fromObject(ob).toString());		
		return JSONObject.fromObject(ob).toString();
	}
	/**
	 * 获取7天运动数据及日期（动态）
	 * @param index
	 * @param session
	 * @return
	 */
    @RequestMapping("weekSport")
    @ResponseBody
	public String getWeekSport(@RequestParam(value="index")Integer index,HttpSession session){
//    	log.debug("***今日获取7天运动数据及日期");
//    	log.debug("***接收到的参数index***"+index);
    	AjaxResutObject ob = new AjaxResutObject();
		//获取行为列表
		UserDto cur = (UserDto) session.getAttribute("CurrentUser");
		String code = cur.getCode();
		Long currentId = cur.getId();
		Calendar cal = Calendar.getInstance();
		int hours = cal.get(Calendar.HOUR_OF_DAY);
		int minutes = cal.get(Calendar.MINUTE);
		int seconds = cal.get(Calendar.SECOND);
		int milliSecond = cal.get(Calendar.MILLISECOND);
		long todayMilliSecond = ((hours*60+minutes)*60+seconds)*1000+milliSecond;
		long everydayMilliSecond = 24*60*60*1000;
		long now = System.currentTimeMillis();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		List list = new ArrayList();
		for(int i=-1;i<6;i++){
			long time = 0l;
			if(index == 0){
				if(i == -1 ){
					time = now;
				}else{
					time = now - todayMilliSecond-1000 - i*everydayMilliSecond;//59秒
				}
			}else{
				time = now - todayMilliSecond-1000 - (i + index)*everydayMilliSecond;
			}
			Date date = new Date(time);
			String dateStr = new SimpleDateFormat("MM-dd").format(date);
			String startDate = sdf1.format(date);
			String endDate = sdf2.format(date);
			List<HfBehaviorRecord> behavior_list = behaviorService.behaviorList(currentId , startDate, code , endDate);
			System.out.println(dateStr);
			System.out.println(endDate);
			String regEx="[^0-9]"; 
			Pattern p = Pattern.compile(regEx);   
			Matcher m = null;
			String step = "";
			int stepNum = 0;
			if(behavior_list != null && behavior_list.size() != 0){
				for(int j=0;j<behavior_list.size();j++){
					HfBehaviorRecord bh = behavior_list.get(j);
					String bh_type = bh.getBehaviorType();
					if(StringUtils.isNotEmpty(bh_type)){
						if(bh_type.contains("走") || bh_type.contains("步行")){
							m = p.matcher(bh_type);
							step = m.replaceAll("").trim();
							if(step != "" || step != null){
								stepNum = stepNum + Integer.parseInt(step);
							}else{
								
							}
							
						}
						
					}
				}
				
			}else{
				
			}
			Map<String,String> map = new HashMap<String,String>();
			map.put("sportDate", dateStr);
			map.put("sportData", String.valueOf(stepNum));
			list.add(map);
		}
		
		ob.setOb(list);
//		log.debug("***周运动数据返回"+JSONObject.fromObject(ob).toString());
    	return JSONObject.fromObject(ob).toString();
	}
    @RequestMapping("sportData")
    @ResponseBody
    public String getSportData(HttpSession session){
    	AjaxResutObject ob = new AjaxResutObject();
		//获取行为列表
		UserDto cur = (UserDto) session.getAttribute("CurrentUser");
		String code = cur.getCode();
		Long currentId = cur.getId();
		Calendar cal = Calendar.getInstance();
		int hours = cal.get(Calendar.HOUR_OF_DAY);
		int minutes = cal.get(Calendar.MINUTE);
		int seconds = cal.get(Calendar.SECOND);
		int milliSecond = cal.get(Calendar.MILLISECOND);
		long todayMilliSecond = ((hours*60+minutes)*60+seconds)*1000+milliSecond;
		long everydayMilliSecond = 24*60*60*1000;
		long now = System.currentTimeMillis();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int todaySteps = 0;
		int weekSteps = 0;
		int monthSteps = 0;
		int stepNum = 0;
		List list = new ArrayList();
		for(int i=-1;i<29;i++){
			long time = 0l;
				if(i == -1 ){
					time = now;
				}else{
					time = now - todayMilliSecond-1000 - i*everydayMilliSecond;//59秒
				}
			Date date = new Date(time);
			String startDate = sdf1.format(date);
			String endDate = sdf2.format(date);
			List<HfBehaviorRecord> behavior_list = behaviorService.behaviorList(currentId , startDate, code , endDate);
			System.out.println(endDate);
			String regEx="[^0-9]"; 
			Pattern p = Pattern.compile(regEx);   
			Matcher m = null;
			String step = "";
			
			if(behavior_list != null && behavior_list.size() != 0){
				for(int j=0;j<behavior_list.size();j++){
					HfBehaviorRecord bh = behavior_list.get(j);
					String bh_type = bh.getBehaviorType();
					if(StringUtils.isNotEmpty(bh_type)){
						if(bh_type.contains("走") || bh_type.contains("步行")){
							m = p.matcher(bh_type);
							step = m.replaceAll("").trim();
							if(step != "" || step != null){
								stepNum = stepNum + Integer.parseInt(step);
							}else{
								
							}
							
						}
						
					}
				}
				
			}else{
				
			}
			if(i == -1){
				todaySteps = stepNum;
			}else if(i == 5){
				weekSteps = stepNum;
			}else if(i == 28){
				monthSteps = stepNum;
			}
			
		}
		String score = "";
		if(todaySteps<100){
			score = "5%";
		}else if(todaySteps>99 && todaySteps<1000){
			score = "10%";
		}else if(todaySteps>999 && todaySteps<2000){
			score = "30%";
		}else if(todaySteps>1999 && todaySteps<3000){
			score = "50%";
		}else if(todaySteps>2999 && todaySteps<5000){
			score = "70%";
		}else if(todaySteps>4999 && todaySteps<6000){
			score = "90%";
		}else if(todaySteps>5999 && todaySteps<6500){
			score = "95%";
		}else{
			score = "100%";
		}
		Map<String,String> map = new HashMap<String,String>();
		map.put("todaySteps", String.valueOf(todaySteps));
		map.put("weekAve", String.valueOf(weekSteps/7));
		map.put("monthAve", String.valueOf(monthSteps/30));
		map.put("score", score);
		ob.setOb(map);
		log.debug("***运动页面数据返回"+JSONObject.fromObject(ob).toString());
    	return JSONObject.fromObject(ob).toString();
    }
	
	
	/**
	 * 异步获取行为记录 列表 
	 * @throws ParseException 
	 */
	@RequestMapping("behaviorList")
	@ResponseBody
	public String behaviorlist(Model model,HttpServletRequest req
		) throws ParseException{
		AjaxResutObject ob = new AjaxResutObject();
		UserDto current = (UserDto) req.getSession().getAttribute("CurrentUser");
		Long userId = current.getId();
		String code = current.getCode();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date dateL=new Date();
		String dateStr = sdf.format(dateL);
		String endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateL);
		model.addAttribute("dateStr",dateStr);
		List<HfBehaviorRecord> behaviorList = behaviorService.behaviorList(userId,dateStr,code,endDate);
		if(behaviorList != null && behaviorList.size() != 0){
			ob.setStatus(StaticVarible.SUCCESS);
			ob.setOb(behaviorList);
		}else{
			ob.setStatus(StaticVarible.ERROR);
			ob.setMsg("暂无记录");
		}
//		log.debug("步数返回信息***"+JSONObject.fromObject(ob).toString());		
		return JSONObject.fromObject(ob).toString();
	}
	/**
	 * 异步获取昨日行为比例
	 * @param session
	 * @return
	 */
	@RequestMapping("getYesterdayBehavior")
	@ResponseBody
	public String getYesterdayBehavior(HttpSession session){
		AjaxResutObject ob = new AjaxResutObject();
		UserDto cur = (UserDto) session.getAttribute("CurrentUser");
		String code = cur.getCode();
		Long currentId = cur.getId();
		Calendar cal = Calendar.getInstance();
		int hours = cal.get(Calendar.HOUR_OF_DAY);
		int minutes = cal.get(Calendar.MINUTE);
		int seconds = cal.get(Calendar.SECOND);
		int milliSecond = cal.get(Calendar.MILLISECOND);
		long todayMilliSecond = ((hours*60+minutes)*60+seconds)*1000+milliSecond;
		long everydayMilliSecond = 24*60*60*1000;
		long now = System.currentTimeMillis();
		SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long time = now - todayMilliSecond-1000;//23:59:59:000毫秒
		Date date = new Date(time);
		String dateStr = sdf1.format(date);
		//将日期和星期传到首页
		//{value:1000, name:'scole', selected:true}
		Map<String ,Object> map_date = new HashMap<String, Object>();
		map_date.put("value", 1000);
		map_date.put("name", dateStr);
		map_date.put("selected", true);
		String weekday = "";
		int dayOfWeek = date.getDay();
		if(dayOfWeek == 1){
			weekday = "星期一";
		}else if(dayOfWeek == 2){
			weekday = "星期二";
		}else if(dayOfWeek == 3){
			weekday = "星期三";
		}else if(dayOfWeek == 4){
			weekday = "星期四";
		}else if(dayOfWeek == 5){
			weekday = "星期五";
		}else if(dayOfWeek == 6){
			weekday = "星期六";
		}else if(dayOfWeek == 0){
			weekday = "星期日";
		}else{
			weekday = "/";
		}
		Map<String ,Object> map_weekday = new HashMap<String, Object>();
		map_weekday.put("value", 1000);
		map_weekday.put("name", weekday);
		map_weekday.put("selected", true);
		String endDate = sdf2.format(date);
		List list_time = new ArrayList();
		list_time.add(map_date);
		list_time.add(map_weekday);
		ob.setOb(list_time);
		List<HfBehaviorRecord> yesterday_behav_list = behaviorService.behaviorList(currentId , dateStr, code , endDate);
		long noWear_time = 0l;
		long rest_time =0l;
		long sport_time =0l;
		long busy_time =0l;
		for(int i = 0;i<yesterday_behav_list.size();i++){
			HfBehaviorRecord hfBehaviorRecord = yesterday_behav_list.get(i);
			String behav_type = hfBehaviorRecord.getBehaviorType();
			long duration = hfBehaviorRecord.getEndTime().getTime() - hfBehaviorRecord.getStartTime().getTime();
			if(behav_type.contains("未佩戴")){
				noWear_time = noWear_time + duration;
			}else if((behav_type.contains("坐") && !behav_type.contains("坐车"))|| behav_type.contains("卧") || behav_type.contains("依靠") || behav_type.contains("睡觉") ||behav_type.contains("看电视") || behav_type.contains("躺着")){
				rest_time = rest_time + duration;
			}else if(behav_type.contains("走") || behav_type.contains("跑") || behav_type.contains("逛街") || behav_type.contains("坐车") || behav_type.contains("散步") || behav_type.contains("步行") || behav_type.contains("乘车")){
				sport_time = sport_time + duration;
			}else if(behav_type.contains("吃饭") || behav_type.contains("做饭") || behav_type.contains("摔倒") || behav_type.contains("买菜") || behav_type.contains("做家务") || behav_type.contains("出门") || behav_type.contains("进门")){
				busy_time = busy_time +duration;
			}else{
				
			}
		}
		List list = new ArrayList();
		Map<String ,Object> map_noWear = new HashMap<String, Object>();
		map_noWear.put("value", noWear_time);
		map_noWear.put("name", "未佩戴");
		list.add(map_noWear);
		Map<String ,Object> map_rest = new HashMap<String, Object>();
		map_rest.put("value", rest_time);
		map_rest.put("name", "休息");
		list.add(map_rest);
		Map<String ,Object> map_sport = new HashMap<String, Object>();
		map_sport.put("value", sport_time);
		map_sport.put("name", "运动");
		list.add(map_sport);
		Map<String ,Object> map_busy = new HashMap<String, Object>();
		map_busy.put("value", busy_time);
		map_busy.put("name", "忙碌");
		list.add(map_busy);
		ob.setList(list);
//		log.debug("***异步获取昨日行为比例返回数据：***"+JSONObject.fromObject(ob).toString());
		return JSONObject.fromObject(ob).toString();
	}
	/**
	 * 异步获取首页 佩戴状态 
	 */
	@RequestMapping("indexAdorn")
	@ResponseBody
	public String indexAdorn(HttpServletRequest req,Model model){
//		log.debug("behavior:****进入佩戴回调");
		AjaxResutObject ob = new AjaxResutObject();
		UserDto cur = (UserDto) req.getSession().getAttribute("CurrentUser");
		Long currentId = cur.getId();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date dateL=new Date();
		String dateStr = sdf.format(dateL);
		String endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateL);
		
		HfBehaviorRecord behavior = behaviorService.lastBehavior(dateStr,cur.getCode(),endDate); //当前行为
//		log.debug("behavior:****"+behavior.toString());
		
		if(behavior!=null){
			String bt = behavior.getBehaviorType();
			if(StringUtils.isNotEmpty(bt)){
				if(bt.indexOf(",")!=-1){
					String[] bt1 = StringUtils.split(bt,",");
					if(bt.contains("走")){
						ob.setMsg("走路");
					}else if(bt.contains("步行")){
						ob.setMsg("步行");
					}else{
						ob.setMsg(bt1[1]);
					}
					ob.setStatus(StaticVarible.SUCCESS);
					ob.setOb(behavior);
				}else{
					if(bt.contains("走")){
						ob.setMsg("走路");
					}else if(bt.contains("步行")){
						ob.setMsg("步行");
					}else{
						ob.setMsg(bt);
					}
					ob.setStatus(StaticVarible.SUCCESS);
//					ob.setMsg(bt);
					ob.setOb(behavior);
				}
			}else{
				ob.setStatus(StaticVarible.SUCCESS);
				ob.setMsg("暂无记录");
				ob.setOb(behavior);
			}
			
		}else{
			ob.setStatus(StaticVarible.ERROR);
			ob.setMsg("暂无记录");
		}
//		log.debug("获取佩戴回调信息***"+JSONObject.fromObject(ob).toString());
		return JSONObject.fromObject(ob).toString();
	}
	
	
	
	
	
	
	
	/**
	 * 切换持有人
	 */
	@RequestMapping("convertDevice")
	public String convertDevice(HttpServletRequest req,Model model){
//		log.debug("进入切换持有人方法***");
		String userId = req.getParameter("userId");
//		log.debug("进入切换持有人方法***携带参数userId***"+userId);
		if(StringUtils.isEmpty(userId)){
			model.addAttribute("msg","非法请求");
			return "redirect：/index/error";
		}
		HfUser loginUser = (HfUser) req.getSession().getAttribute("LoginUser");
		UserDto d = userService.getUserAndDevice(Long.parseLong(userId),loginUser.getId());
		req.getSession().setAttribute("CurrentUser", d);
		return "redirect:/index/index";
	}
	
	
	
	/**
	 * 根据时间异步刷选 行为记录 列表 
	 * @throws ParseException 
	 */
	@RequestMapping("be_list")
	@ResponseBody
	public String beList(Model model,HttpServletRequest req,
			@RequestParam(value="dateStr",required=false) String  dateStr) throws ParseException{
		//Integer pageNum = Integer.parseInt(req.getParameter("pageNum"));
		UserDto current = (UserDto) req.getSession().getAttribute("CurrentUser");
		Long userId = current.getId();
		String code = current.getCode();
		String endDate = "";
		AjaxResutObject ob = new AjaxResutObject();
		if(StringUtils.isEmpty(dateStr)){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			Date date=new Date();
			dateStr = sdf.format(date);
			endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		}else{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			Date date=new Date();
			if(sdf.format(date).equals(dateStr)){ //选择了当天，则是当前时间
				endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
			}else{ //非当天，这是24点
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(dateStr));
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
			}
		}
		model.addAttribute("dateStr",dateStr);
		List<HfBehaviorRecord> behaviorList = behaviorService.behaviorList(userId,dateStr,code,endDate);
		if(behaviorList != null && behaviorList.size() != 0){
			ob.setStatus(StaticVarible.SUCCESS);
			ob.setOb(behaviorList);
		}else{
			ob.setStatus(StaticVarible.ERROR);
			ob.setMsg("暂无记录");
		}
		log.debug("步数返回信息***"+JSONObject.fromObject(ob).toString());		
		return JSONObject.fromObject(ob).toString();
	}

	/**
	 * 行为记录 列表 
	 * @throws ParseException 
	 */
	@RequestMapping("behavior_list")
	public String behaviorList(Model model,HttpServletRequest req,
			@RequestParam(value="dateStr",required=false) String  dateStr) throws ParseException{
		//Integer pageNum = Integer.parseInt(req.getParameter("pageNum"));
		UserDto current = (UserDto) req.getSession().getAttribute("CurrentUser");
		Long userId = current.getId();
		String code = current.getCode();
		String endDate = "";
		if(StringUtils.isEmpty(dateStr)){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			Date date=new Date();
			dateStr = sdf.format(date);
			endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		}else{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			Date date=new Date();
			if(sdf.format(date).equals(dateStr)){ //选择了当天，则是当前时间
				endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
			}else{ //非当天，这是24点
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(dateStr));
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
			}
		}
		model.addAttribute("dateStr",dateStr);
		List<HfBehaviorRecord> list = behaviorService.behaviorList(userId,dateStr,code,endDate);
		model.addAttribute("list",list);
		return "behavior_list";
	}
	
	
	/**
	 * 我的设置 - 设备列表 -
	 * 描述:我的所有设备及每个设备对应的人
	 */
	@RequestMapping("device_list")
	public String deviceList(Model model,HttpServletRequest req){
		HfUser lg = (HfUser) req.getSession().getAttribute("LoginUser");
		Long userId = lg.getId();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		List<UserDto> list = indexService.deviceList(map);
		model.addAttribute("list",list);
		return "setting/device_list";
	}
	
	/**
	 * 使用人列表 （我关注的人）
	 */
	@RequestMapping("device_user_list")
	public String deviceUserList(Model model,HttpServletRequest req){
		HfUser loginUser = (HfUser) req.getSession().getAttribute("LoginUser");
		Long userId = loginUser.getId();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		List<UserContainDeviceDto> list = indexService.deviceUserList(map);
		for(int i=0;i<list.size(); i++){
			UserContainDeviceDto user = list.get(i);
			//根据生日获取年龄
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			String age_str = "0";
			String birthday = "";
			String user_birthday = user.getBirthday();
			if(user_birthday == null || user_birthday.length() == 0){
				//根据身份证获取生日
				String card = user.getCard().trim().replace(" ", "");
				if(card != null && card != ""){
					if(card.length() == 18){
						birthday = card.substring(6, 14);
					}else if(card.length() == 15){
						birthday = 19 + card.substring(6, 12);
					}
					String year_str = birthday.substring(0,4);
					String month_str = birthday.substring(4,6);
					String day_str = birthday.substring(6,8);
					String birthday_str = year_str +"-"+month_str +"-"+day_str;
					user.setBirthday(birthday_str);
					birthday = user.getBirthday();
					String[] arr = birthday.split("-");
					age_str = arr[0];
					int age = year - Integer.valueOf(age_str);
					if(age <0 || age >120){
						age = 0;
					}
					user.setAge(age);
				}else{
					log.error("用户:"+user.getId()+"身份证为空!");
				}
			}else{
				birthday = user.getBirthday();
				if(!birthday.contains("-")){
					String year_str = birthday.substring(0,4);
					String month_str = birthday.substring(4,6);
					String day_str = birthday.substring(6,8);
					String birthday_str = year_str +"-"+month_str +"-"+day_str;
					user.setBirthday(birthday_str);
				}
				birthday = user.getBirthday();
				String[] arr = birthday.split("-");
				age_str = arr[0];
				int age = year - Integer.valueOf(age_str);
				if(age <0 || age >120){
					age = 0;
				}
				user.setAge(age);
			}
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
						age = 0;
						birthday = "";
					}
				}
				user.setAge(age);
				user.setBirthday(birthday);
			}*/
		}
		SimpleDateFormat year = new SimpleDateFormat("yyyy");
		model.addAttribute("list",list);
		model.addAttribute("year", year.format(new Date()));
		return "setting/device_user_list";
	}
	
	
	/**
	 * 鼓励一下 列表
	 */
	@RequestMapping("encourage_list")
	public String encourageList(Model model,HttpServletRequest req){
		Integer pageNum = Integer.parseInt(req.getParameter("pageNum"));
		HfUser lg = (HfUser) req.getSession().getAttribute("LoginUser");
		Long userId = lg.getId();
		List<HfEncouragement> list = encouragementService.encourageList(userId, pageNum, 10);
		model.addAttribute("list",list);
		return "encourage_list";
	}
	
	/**
	 * 保存鼓励
	 * @param session
	 */
	@RequestMapping("saveEncourage")
	@ResponseBody
	public void saveEncourage(HttpSession session,@Param(value="type") int type){
		log.debug("进入indexController***saveEncourage方法");
		HfUser lg = (HfUser) session.getAttribute("LoginUser");
		Long userId = lg.getId();
		log.debug("关注人ID***"+userId);
		UserDto cur = (UserDto) session.getAttribute("CurrentUser");
		Long currentId = cur.getId();
		log.debug("用户ID***"+currentId);
		String code = cur.getCode();
		log.debug("用户的设备码***"+code);
		HfEncouragement encouragement = new HfEncouragement();
		
		encouragement.setSendUserId(userId);
		encouragement.setReceiveUserId(currentId);
		encouragement.setCode(code);
		encouragement.setEncouragementType(type);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sendDate_str = format.format(new Date());
		Date sendDate = null;
		try {
			sendDate = format.parse(sendDate_str);
		} catch (ParseException e) {
			e.printStackTrace();
			log.error("格式转换错误!");
			
		}
		encouragement.setSendDate(sendDate);
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		String createDate_str = format2.format(new Date());
		Date createDate = null;
		try {
			createDate = format2.parse(createDate_str);
		} catch (ParseException e) {
			e.printStackTrace();
			log.error("格式转换错误!");
		}
		encouragement.setCreateDate(createDate);
		encouragement.setDeleteFlag(0);
		encouragement.setSendStatus(0);
		HfEncouragement encouragement_save = encouragementService.save(encouragement);
		
	}
	
	
	

	
	/**
	 * 安全情况下  - 点击头像弹出 个人信息(被关注人的信息)，拍照功能
	 */
/*	@RequestMapping(value="be_attention_userinfo")
	public String beAttentionUserInfo(Model model,HttpServletRequest req){
		HttpSession session = req.getSession();
		HfUser currentUser= (HfUser) session.getAttribute("currentUser"); //当前被关注人 
		HfDevice device = indexService.currentUserDevice(currentUser.getId());
		model.addAttribute("device",device);
		model.addAttribute("currentUser",currentUser);
		return "be_attention_userinfo";
	}*/
	/**
	 * 安全情况下  - 点击头像弹出 个人信息(被关注人的信息)，拍照功能
	 */
	@RequestMapping(value="be_attention_userinfo")
	public String beAttentionUserInfo(Model model,HttpServletRequest req){
		HttpSession session = req.getSession();
		HfUser currentUser= (HfUser) session.getAttribute("currentUser"); //当前被关注人 
		HfDevice device = indexService.currentUserDevice(currentUser.getId());
		model.addAttribute("device",device);
		model.addAttribute("currentUser",currentUser);
		return "index/index_outLayer";
	}
	
	
	/**
	 * 删除设备 
	 */
	@RequestMapping(value="deleteDevice")
	@ResponseBody
	public String deleteDevice(Model model,HttpServletRequest req){
		HttpSession session = req.getSession();
		HfUser loginUser= (HfUser) session.getAttribute("LoginUser"); //当前被关注人 
		Long deviceId = Long.parseLong(req.getParameter("deviceId"));
		boolean flag = indexService.deleteDevice(deviceId,loginUser.getId());
		if(flag){
			UserDto cur = (UserDto) session.getAttribute("CurrentUser");
			if(cur.getDeviceId().intValue() == deviceId){
				session.removeAttribute("CurrentUser");
			}
			return "success";
		}
		return "fail";
	}
	
	
	
	
	
	
	
	
	/**
	 * 新增设备 跳入填写基本信息页面(设备号一存在，直接跳入首页)
	 * @return
	 */
	@RequestMapping("user_info")
	public String userInfo(HttpServletRequest req,Model model){
		HfUser loginUser = (HfUser) req.getSession().getAttribute("LoginUser");
		String decode = req.getParameter("deviceCode");
		if(StringUtils.isEmpty(decode)){
			req.setAttribute("msg", "非法请求");
			return "redirect:/index/error";
		}
		HfDevice d = indexService.getDeviceByCode(decode);
		if(d!=null){ //设备存在
			UserDto cur = userService.getUserAndDevice(d.getUserId(),loginUser.getId()); //获得用户及其最新设备
			req.getSession().setAttribute("CurrentUser", cur);
			indexService.bindingUserAndUser(loginUser,cur);
			return "redirect:index/index";
		}
		model.addAttribute("deviceCode",decode);
		return "user_info";
	}
	

	
	
	/**
	 * 错误页面
	 */
	@RequestMapping("error")
	public String error(HttpServletRequest req,Model model){
		AjaxResutObject ro = new AjaxResutObject();
		ro.setMsg(req.getAttribute("msg")==null?"":req.getAttribute("msg").toString());
		model.addAttribute(ro);
		return "error";
	}
	
	@ResponseBody
	@RequestMapping(value="getHealthTarget")
	public String getHealthTarget(HttpSession session, HttpServletRequest req) {
		UserDto current =(UserDto) req.getSession().getAttribute("CurrentUser");
		List<HfHealthTarget> list = healthTargetService.getAllHealthTarget(
				current.getId(), current.getCode());
		JSONArray array = JSONArray.fromObject(list);
		return array.toString();
	}
	
	
	/**
	 * 注册修改持有人 保存
	 */
	@RequestMapping("registerUpdateSave")
	@ResponseBody
	public String registerUpdateSave(HttpServletRequest req,Model model,@ModelAttribute("user")HfUser user){
		HfUser loginUser = (HfUser) req.getSession().getAttribute("LoginUser");
		String msg = "";
		AjaxResutObject ro = new AjaxResutObject();
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
		msg = indexService.updateUserInfo(user,loginUser);
		if(msg == ""){ //绑定成功
			ro.setStatus(StaticVarible.SUCCESS);
		}else{
			ro.setStatus(StaticVarible.ERROR);
			ro.setMsg(msg);
		}
		return JSONObject.fromObject(ro).toString();
	}
	
	
	/**
	 *跳入 注册 修改持有人信息。
	 */
	@RequestMapping("registerUpdate")
	public String registerUpdate(HttpServletRequest req,Model model){
		HfUser loginUser = (HfUser) req.getSession().getAttribute("LoginUser");
		UserDto holder = indexService.myBangHolder(loginUser.getId()).get(0);
		model.addAttribute("user",holder);
		return "register_info_update";
	}
	
	
	@RequestMapping(value="toAddUser")
	public String toAddUser(){
		return "/setting/addUser";
	}
	
}
