package com.hafu.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hafu.modules.utils.PublicProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hafu.dto.UserDto;
import com.hafu.entity.HfBehaviorRecord;
import com.hafu.entity.HfCheckData;
import com.hafu.entity.HfHealthPoint;
import com.hafu.entity.HfTrack;
import com.hafu.service.BehaviorRecordService;
import com.hafu.service.HfCheckDataService;
import com.hafu.service.HfHealthPointService;
import com.hafu.service.HfSportDataService;
import com.hafu.service.HfTrackService;
import com.hafu.utils.AjaxResutObject;
import com.hafu.utils.StaticVarible;
import com.hafu.utils.WeatherApi;

@Controller
@RequestMapping(value="health")
public class HealthController {
	
	public static Logger logger=Logger.getLogger(HealthController.class);
	
	@Resource
	private HfHealthPointService healthPointService ;
	
	@Resource
	private HfCheckDataService checkDataService ;
	
	@Resource
	private HfSportDataService sportDataService; 
	
	@Resource
	private HfTrackService trackService;
	@Autowired 
	private BehaviorRecordService behaviorService;
	/**
	 * 最近一条健康目标
	 * @return
	 */
	@RequestMapping(value="lastHealthPoint")
	@ResponseBody
	public String lastHealthPoint(HttpSession session,Model model,
			HttpServletRequest req){
		UserDto current = (UserDto) req.getSession().getAttribute("CurrentUser"); 
		HfHealthPoint h=healthPointService.getLastHealthPoint(current.getId(), current.getCode());
		AjaxResutObject ro = new AjaxResutObject();
		ro.setStatus(StaticVarible.SUCCESS);
		ro.setOb(h);
		return JSONObject.fromObject(ro).toString();
	}
	
	@RequestMapping(value="healthPoint")
	public String healthPoint(HttpSession session,Model model,
			@RequestParam(value="date",required=false) String endDate,
			HttpServletRequest req){
		UserDto current = (UserDto) req.getSession().getAttribute("CurrentUser"); 
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
		try {
			if(StringUtils.isNotBlank(endDate)){
				date=sdf.parse(endDate);
			}else{
				date=sdf.parse(sdf.format(date));
				date.setTime(date.getTime()-24*60*60*1000);
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<HfHealthPoint> list=healthPointService.getHfHealthPoint(date, date, current.getId(), current.getCode());
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		List<HfHealthPoint> yesterday=healthPointService.getHfHealthPoint(c.getTime(), c.getTime(), current.getId(), current.getCode());
		model.addAttribute("list", list);
		if(yesterday!=null&&yesterday.size()>0){
			model.addAttribute("yesterday", yesterday.get(0));
		}
		model.addAttribute("date", sdf.format(date));
		return "/health/healthPoint";
	}
	
    /**
     * 获取步数
     */
	@RequestMapping(value="getStep")
	public String getStep(HttpSession session,Model model,
			@RequestParam(value="date",required=false) String dateStr,
			HttpServletRequest req) throws ParseException{
		UserDto current = (UserDto) req.getSession().getAttribute("CurrentUser"); 
		String code = current.getCode();
		Long currentId = current.getId();
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
		List<HfBehaviorRecord> behavior_list = behaviorService.behaviorList(currentId ,dateStr, code , endDate);
		String regEx="[^0-9]"; 
		Pattern p = Pattern.compile(regEx);   
		Matcher m = null;
		String step = "";
		int stepNum = 0;
		if(behavior_list != null && behavior_list.size() != 0){
			for(int i=0;i<behavior_list.size();i++){
				HfBehaviorRecord bh = behavior_list.get(i);
				String bh_type = bh.getBehaviorType();
				if(StringUtils.isNotEmpty(bh_type)){
					if(bh_type.contains("走") || bh_type.contains("步行")){
						m = p.matcher(bh_type);
						step = m.replaceAll("").trim();
						if(step != "" || step != null){
							stepNum = stepNum + Integer.parseInt(step);
						}	
					}
					
				}
			}
		}
		model.addAttribute("SposrtstepNum", stepNum);
		return "/health/sportData";
	}
	
	
	/**
	 * 异步获取行为比例
	 * @param session
	 * @return
	 */
	@SuppressWarnings({ "deprecation", "unused", "rawtypes", "unchecked" })
	@RequestMapping("getdayBehavior")
	@ResponseBody
	public String getdayBehavior(HttpSession session,Model model,HttpServletRequest req,
			@RequestParam(value="dateStr",required=false) String dateStr){
		logger.debug("***进入行为识别页面获取");
		logger.debug("***参数dateStr"+dateStr);
		AjaxResutObject ob = new AjaxResutObject();
		UserDto cur = (UserDto) session.getAttribute("CurrentUser");
		String code = cur.getCode();
		Long currentId = cur.getId();
		String endDate = dateStr +" 23:59:59:000";
		String startDate = dateStr;
		SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 String dateTR="";
		 int dayOfWeek = 0;
		/* String dateTR = dateStr.substring(5);*/
		try {
			Date datestr=sdf.parse(dateStr);
		    dateTR = sdf1.format(datestr);
		    dayOfWeek = datestr.getDay();//获取星期几
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Map<String ,Object> map_date = new HashMap<String, Object>();
		//将日期和星期传到首页
		map_date.put("name", dateTR);
		map_date.put("value", 1000);
		map_date.put("selected", true);
		String weekday = "";
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
		List list_time = new ArrayList();
		list_time.add(map_date);
		list_time.add(map_weekday);
		ob.setOb(list_time);
		List<HfBehaviorRecord> day_behav_list = behaviorService.behaviorList(currentId , startDate, code , endDate);
		long noWear_time = 0l;
		long rest_time =0l;
		long sport_time =0l;
		long busy_time =0l;
		for(int i = 0;i<day_behav_list.size();i++){
			HfBehaviorRecord hfBehaviorRecord =day_behav_list.get(i);
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
		logger.debug("行为识别页面返回数据***"+JSONObject.fromObject(ob).toString());
		return JSONObject.fromObject(ob).toString();
	}

	@RequestMapping(value="sportData")
	public String sportData(@RequestParam(value="type",defaultValue="1")Integer  type,Model model,
			HttpSession session){
		model.addAttribute("type", type);
		return "/health/sportData";
	}
	
	@RequestMapping(value="healthData")
	public String healthData(@RequestParam(value="type",defaultValue="1")Integer type,HttpSession session,
			Model model,HttpServletRequest req){
		UserDto current = (UserDto) req.getSession().getAttribute("CurrentUser"); 
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String checkType=null;
		Date end=null;
		try {
			if(type.equals(1)){
				checkType="JKSJ_XY";
			}else if(type.equals(2)){
				checkType="JKSJ_XZ";
			}else if(type.equals(3)){
				checkType="JKSJ_XT";
			}else{
				checkType="JKSJ_NJ";
				type=4;
			}
			end=sdf.parse(sdf.format(new Date()));
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(end);
			calendar.add(Calendar.DATE, -6);
			List<HfCheckData> list=checkDataService.getOnePageCheckData(current.getId(),current.getCode(), "JKSJ", checkType, calendar.getTime(), end,1,1).getContent();
			if(list.size()>0){
				model.addAttribute("checkData", list.get(0));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("type", type);
		return "/health/healthData";
	}
	
	@RequestMapping(value="bodyData")
	public String bodyData(@RequestParam(value="type",defaultValue="1")Integer type,
			HttpSession session,Model model,HttpServletRequest req){
		UserDto current = (UserDto) req.getSession().getAttribute("CurrentUser"); 
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String checkType=null;
		Date end=null;
		try {
			if(type.equals(1)){
				checkType="STSJ_XL";
			}else if(type.equals(2)){
				checkType="STSJ_SM";
			}else if(type.equals(3)){
				checkType="STSJ_TN";
				type=3;
			}
			end=sdf.parse(sdf.format(new Date()));
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(end);
			calendar.add(Calendar.DATE, -6);
			List<HfCheckData> list=checkDataService.getOnePageCheckData(current.getId(),current.getCode(), PublicProperty.bodyData, checkType, calendar.getTime(), end,1,1).getContent();
			if(list.size()>0){
				model.addAttribute("checkData", list.get(0));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("type", type);
		return "/health/bodyData";
	}
	
	@RequestMapping(value="environmentData")
	public String environmentData(@RequestParam(value="type",defaultValue="1")Integer type,
			HttpSession session,Model model,HttpServletRequest req){
		UserDto current = (UserDto) req.getSession().getAttribute("CurrentUser"); 
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String checkType=null;
		Date end=null;
		try {
			if(type.equals(1)){
				checkType="HJSJ_SD";
			}else if(type.equals(2)){
				checkType="HJSJ_SD";
			}else{
				checkType="HJSJ_SD";
				type=3;
			}
			end=sdf.parse(sdf.format(new Date()));
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(end);
			calendar.add(Calendar.DATE, -6);
			List<HfCheckData> list=checkDataService.getOnePageCheckData(current.getId(),current.getCode(), "HJSJ", checkType, calendar.getTime(), end,1,1).getContent();
			if(list.size()>0){
				model.addAttribute("checkData", list.get(0));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*List<Date> labels=new ArrayList<Date>();
		while(!calendar.getTime().after(end)){
			labels.add(calendar.getTime());
			calendar.add(Calendar.DATE, 1);
		}
		List<HfCheckData> list=checkDataService.getCheckDate(1l,code, begin, end);*/
		model.addAttribute("type", type);
		return "/health/envireomentData";
	}
	
	
	@RequestMapping(value="toAddCheckData")
	public String toAddCheckData(@RequestParam(value="type",defaultValue="XT")String type){
		String view=null;
		if("XT".equals(type)){
			view="/health/add/addXT";
		}else if("XY".equals(type)){
			view="/health/add/addXY";
		}else if("XZ".equals(type)){
			view="/health/add/addXZ";
		}else if("XL".equals(type)){
			view="/health/add/addXL";
		}
		return view;
	}
	
	@ResponseBody
	@RequestMapping(value="addCheckData")
	public String addCheckData(@ModelAttribute HfCheckData checkData,
			HttpServletRequest req){
		UserDto current =(UserDto) req.getSession().getAttribute("CurrentUser"); 
		JSONObject obj=new JSONObject();
		boolean success=false;
		if(checkData.getCheckDate()!=null&&!checkData.getCheckDate().after(new Date())){
			if(checkData.getCheckType().equals("JKSJ_XY")||checkData.getCheckType().equals("JKSJ_XZ")||
					checkData.getCheckType().equals("JKSJ_XT")||checkData.getCheckType().equals("STSJ_XL")){
				if(StringUtils.isNotBlank(checkData.getCheckAddress())&&StringUtils.isNotBlank(checkData.getCheckData())){
					if(checkData.getCheckType().equals("STSJ_XL")){
						checkData.setCheckCategory(PublicProperty.bodyData);
					}else{
						checkData.setCheckCategory(PublicProperty.healthData);
					}
					checkData.setCreateDate(new Date());
					checkData.setDeleteFlag(0);
					checkData.setUserId(current.getId());
					checkData.setCode(current.getCode());
					checkDataService.save(checkData);
					success=true;
				}
			}
		}
		obj.put("success", success);
		return obj.toString();
	}
	
	@RequestMapping(value="dataCompare")
	public String dataCompare(@RequestParam(value="type",defaultValue="1")Integer type,
			Model model){
		model.addAttribute("type", type);
		return "/health/dataCompare";
	}
	
	/**
	 * 获取健康数据
	 * @param unit	时间单位	1 天	2 月	3 年
	 * @param value	数值
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getHelthPoint")
	public String getHelthPoint(@RequestParam(value="unit",defaultValue="1")Integer unit,
			@RequestParam(value="value",defaultValue="6")Integer value,
			@RequestParam(value="date",required=false)String endDate,
			HttpServletRequest req){
		UserDto current = (UserDto) req.getSession().getAttribute("CurrentUser"); 
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		
		Date end=null;
		if(StringUtils.isNotBlank(endDate)){
			try {
				end=sdf.parse(endDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				end=new Date();
				e.printStackTrace();
			}
		}else{
			end=new Date();
		}
		Date begin=new Date();
		Calendar calendar=Calendar.getInstance();
		try {
			end=sdf.parse(sdf.format(end));
			calendar.setTime(end);
			if(unit.equals(1)){
				calendar.add(Calendar.DATE, -value);
			}else if(unit.equals(2)){
				calendar.add(Calendar.MONTH, -value);
			}else if(unit.equals(3)){
				calendar.add(Calendar.YEAR, -value);
			}
			begin=calendar.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Date> labels=new ArrayList<Date>();
		while(!calendar.getTime().after(end)){
			labels.add(calendar.getTime());
			calendar.add(Calendar.DATE, 1);
		}
		List<HfCheckData> list=checkDataService.getCheckDate(current.getId(),current.getCode(), begin, end);
		List<HfHealthPoint>  sportData=healthPointService.getHfHealthPoint(begin, end, current.getId(), current.getCode());
		//List<HfSportData> sportData=sportDataService.getOnePeriodSportData(current.getId(),current.getCode(), begin, end);
		Map<String,Map<String, Map<Date, Object>>> map=new HashMap<String, Map<String,Map<Date, Object>>>();
		for(HfCheckData data:list){
			String checkType=data.getCheckType();
			if(!map.containsKey(checkType)){
				Map<String, Map<Date, Object>> temp=new HashMap<String, Map<Date, Object>>();
				map.put(checkType, temp);
				Map<String, Map<Date, Object>> temp1=new HashMap<String, Map<Date, Object>>();
				map.put(checkType+"address", temp1);
				//addressMap.put(checkType, addressList);
			}
			Map<String, Map<Date, Object>> t=map.get(checkType+"address");
			if(!t.containsKey(checkType+"address")){
				Map<Date, Object> temp=new HashMap<Date, Object>();
				t.put(checkType+"address", temp);
			}
			Map<Date, Object> d=t.get(checkType+"address");
			d.put(data.getCheckDate(), data.getCheckAddress());
			String checkData=data.getCheckData();
			if(checkType.equals("JKSJ_XY")){
				String[] array=checkData.split(",");
				Integer szy=null;
				if(array.length>=1&&StringUtils.isNotBlank(array[0])){
					szy=Math.round(Float.valueOf(array[0]));
				}
				Integer ssy=null;
				if(array.length>=2&&StringUtils.isNotBlank(array[1])){
					ssy=Math.round(Float.valueOf(array[1]));
				}
				Integer avg=null;
				if(array.length>=3&&StringUtils.isNotBlank(array[2])){
					avg=Math.round(Float.valueOf(array[2]));
				}else{
					if(szy!=null&&ssy!=null) avg=(szy+ssy)/2;
					else avg=null;
				}
				if(!map.get(checkType).containsKey("舒张压")){
					Map<Date, Object> temp=new HashMap<Date, Object>();
					Map<Date, Object> temp2=new HashMap<Date, Object>();
					Map<Date, Object> temp3=new HashMap<Date, Object>();
					map.get(checkType).put("舒张压", temp);
					map.get(checkType).put("收缩压", temp2);
					map.get(checkType).put("血压", temp3);
				}
				if(!map.get(checkType).get("舒张压").containsKey(data.getCheckDate())){
					map.get(checkType).get("舒张压").put(data.getCheckDate(),szy);
					map.get(checkType).get("收缩压").put(data.getCheckDate(),ssy);
					map.get(checkType).get("血压").put(data.getCheckDate(),avg);
				}
			}else if(checkType.equals("JKSJ_XZ")){
				String[] array=checkData.split(",",4);
				Integer zdgc=null;
				if(StringUtils.isNotBlank(array[0])){
					zdgc=Math.round(Float.valueOf(array[0]));
				}
				Integer gysz=null;
				if(StringUtils.isNotBlank(array[1])){
					gysz=Math.round(Float.valueOf(array[1]));
				}
				Integer gmdzdb=null;
				if(StringUtils.isNotBlank(array[2])){
					gmdzdb=Math.round(Float.valueOf(array[2]));
				}
				Integer dmdzdb=null;
				if(StringUtils.isNotBlank(array[3])){
					dmdzdb=Math.round(Float.valueOf(array[3]));
				}
				if(!map.get(checkType).containsKey("总胆固醇")){
					Map<Date, Object> temp=new HashMap<Date, Object>();
					Map<Date, Object> temp2=new HashMap<Date, Object>();
					Map<Date, Object> temp3=new HashMap<Date, Object>();
					Map<Date, Object> temp4=new HashMap<Date, Object>();
					map.get(checkType).put("总胆固醇", temp);
					map.get(checkType).put("血氧", temp2);
					map.get(checkType).put("白细胞", temp3);
					map.get(checkType).put("血红蛋白", temp4);
				}
				if(!map.get(checkType).get("总胆固醇").containsKey(data.getCheckDate())){
					map.get(checkType).get("总胆固醇").put(data.getCheckDate(),zdgc);
					map.get(checkType).get("血氧").put(data.getCheckDate(),gysz);
					map.get(checkType).get("白细胞").put(data.getCheckDate(),gmdzdb);
					map.get(checkType).get("血红蛋白").put(data.getCheckDate(),dmdzdb);
				}
			}else if(checkType.equals("JKSJ_XT")){
				if(!map.get(checkType).containsKey("血糖")){
					Map<Date, Object> temp=new HashMap<Date, Object>();
					map.get(checkType).put("血糖", temp);
				}
				if(!map.get(checkType).get("血糖").containsKey(data.getCheckDate())){
					map.get(checkType).get("血糖").put(data.getCheckDate(),Float.valueOf(checkData));
				}
			}else if(checkType.equals("JKSJ_NJ")){
				/*float xt=Float.valueOf(checkData);
				if(!map.get(checkType).containsKey("尿检")){
					Map<Date, Float> temp=new HashMap<Date, Float>();
					map.get(checkType).put("尿检", temp);
				}
				if(!map.get(checkType).get("尿检").containsKey(data.getCheckDate())){
					map.get(checkType).get("尿检").put(data.getCheckDate(),xt);
				}*/
			}else if(checkType.equals("STSJ_XL")){
				if(!map.get(checkType).containsKey("心率")){
					Map<Date, Object> temp=new HashMap<Date, Object>();
					map.get(checkType).put("心率", temp);
				}
				if(!map.get(checkType).get("心率").containsKey(data.getCheckDate())){
					map.get(checkType).get("心率").put(data.getCheckDate(),Float.valueOf(checkData));
				}
			}else if(checkType.equals("STSJ_SM")){
				if(!map.get(checkType).containsKey("睡眠")){
					Map<Date, Object> temp=new HashMap<Date, Object>();
					map.get(checkType).put("睡眠", temp);
				}
				if(!map.get(checkType).get("睡眠").containsKey(data.getCheckDate())){
					map.get(checkType).get("睡眠").put(data.getCheckDate(),Float.valueOf(checkData));
				}
			}else if(checkType.equals("STSJ_TN")){
				String[] array=checkData.split(",",2);
				Integer srrl=null;
				if(StringUtils.isNotBlank(array[0])){
					srrl=Math.round(Float.valueOf(array[0]));
				}
				Integer xhkll=null;
				if(StringUtils.isNotBlank(array[1])){
					xhkll=Math.round(Float.valueOf(array[1]));
				}
				if(!map.get(checkType).containsKey("摄入热量")){
					Map<Date, Object> temp=new HashMap<Date, Object>();
					Map<Date, Object> temp2=new HashMap<Date, Object>();
					map.get(checkType).put("摄入热量", temp);
					map.get(checkType).put("消耗卡路里", temp2);
				}
				if(!map.get(checkType).get("摄入热量").containsKey(data.getCheckDate())){
					map.get(checkType).get("摄入热量").put(data.getCheckDate(),srrl);
					map.get(checkType).get("消耗卡路里").put(data.getCheckDate(),xhkll);
				}
			}else if(checkType.equals("HJSJ_SD")){
				if(!map.get(checkType).containsKey("湿度")){
					Map<Date, Object> temp=new HashMap<Date, Object>();
					map.get(checkType).put("湿度", temp);
				}
				if(!map.get(checkType).get("湿度").containsKey(data.getCheckDate())){
					map.get(checkType).get("湿度").put(data.getCheckDate(),Float.valueOf(checkData));
				}
			}else if(checkType.equals("HJSJ_ZY")){
				if(!map.get(checkType).containsKey("风力")){
					Map<Date, Object> temp=new HashMap<Date, Object>();
					map.get(checkType).put("风力", temp);
				}
				if(!map.get(checkType).get("风力").containsKey(data.getCheckDate())){
					map.get(checkType).get("风力").put(data.getCheckDate(),Float.valueOf(checkData));
				}
			}else if(checkType.equals("HJSJ_KQZL")){
				if(!map.get(checkType).containsKey("空气质量")){
					Map<Date, Object> temp=new HashMap<Date, Object>();
					map.get(checkType).put("空气质量", temp);
				}
				if(!map.get(checkType).get("空气质量").containsKey(data.getCheckDate())){
					map.get(checkType).get("空气质量").put(data.getCheckDate(),Float.valueOf(checkData));
				}
			}
		}
		map.put("YDSJ", new HashMap<String, Map<Date,Object>>());
		Map<String, Map<Date, Object>> temp=map.get("YDSJ");
		temp.put("运动量", new HashMap<Date, Object>());
		temp.put("活动次数", new HashMap<Date, Object>());
		calendar=Calendar.getInstance();
		for(HfHealthPoint data:sportData){
			Date d=data.getCreateDate();
			try {
				d=sdf.parse(sdf.format(d));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(!temp.get("运动量").containsKey(d)){
				temp.get("运动量").put(d, data.getStepCount());
				temp.get("活动次数").put(d, data.getLiveness());
			}
		}
		Map<String, List<Object>> result=new HashMap<String, List<Object>>();
		Iterator<Entry<String, Map<String, Map<Date, Object>>>> it=map.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, Map<String, Map<Date, Object>>> entry=it.next();
			Iterator<Entry<String, Map<Date, Object>>> ite=entry.getValue().entrySet().iterator();
			while(ite.hasNext()){
				Entry<String, Map<Date, Object>> next=ite.next();
				String label=next.getKey();
				Map<Date, Object> re=next.getValue();
				List<Object> floatList=new ArrayList<Object>();
				for(Date d:labels){
					floatList.add(re.get(d));
				}
				result.put(label, floatList);
			}
			
		}
		JSONObject obj=JSONObject.fromObject(result);
		obj.put("date", JSONArray.fromObject(labels));
		return obj.toString();
	}
	
	//综合、运动、健康、身体、环境 
	@ResponseBody
	@RequestMapping(value="getCompareData")
	public String getCompareData(HttpServletRequest req){
		UserDto current = (UserDto) req.getSession().getAttribute("CurrentUser"); 
		
		logger.fatal("获取对比数据，code:"+current.getCode()+"\tuserId:"+current.getId());
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal=Calendar.getInstance();
		Date today=new Date();//sdf.format(cal.getTime());
		try {
			today=sdf.parse(sdf.format(cal.getTime()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, List<Object>> map=new HashMap<String, List<Object>>();
		List<String> type=new ArrayList<String>();
		type.add(PublicProperty.bodyData);
		type.add(PublicProperty.healthData);
		type.add(PublicProperty.environmentData);
		for(String category:type){
			List<Object> data=new ArrayList<Object>();
			for(int i=5;i>0;i--){
				cal.setTime(today);
				cal.add(Calendar.DATE, -i);
				Float score=checkDataService.getOnePeriodScore(category, current.getId(), current.getCode(), cal.getTime(),Calendar.DATE,1);
				data.add(score);
			}
			map.put(category+"_day", data);
		}
		for(String category:type){
			List<Object> data=new ArrayList<Object>();
			for(int i=5;i>0;i--){
				cal.setTime(today);
				cal.add(Calendar.DATE, -i*7);
				Float score=checkDataService.getOnePeriodScore(category, current.getId(), current.getCode(), cal.getTime(),Calendar.DATE,7);
				data.add(score);
			}
			map.put(category+"_week", data);
		}
		for(String category:type){
			List<Object> data=new ArrayList<Object>();
			for(int i=5;i>0;i--){
				cal.setTime(today);
				cal.add(Calendar.MONTH, -i);
				Float score=checkDataService.getOnePeriodScore(category, current.getId(), current.getCode(), cal.getTime(),Calendar.MONTH,1);
				data.add(score);
			}
			map.put(category+"_month", data);
		}
		List<Object> day=new ArrayList<Object>();
		List<Object> dayTime=new ArrayList<Object>();
		for(int i=5;i>0;i--){
			cal.setTime(today);
			cal.add(Calendar.DATE, -i);
			dayTime.add(cal.getTime());
			Float score=healthPointService.getOnePeriodScore(current.getId(), current.getCode(), cal.getTime(), Calendar.DATE,1,0);
			day.add(score);
		}
		map.put("day", dayTime);
		map.put("sport_day", day);
		List<Object> week=new ArrayList<Object>();
		List<Object> weekTime=new ArrayList<Object>();
		for(int i=5;i>0;i--){
			cal.setTime(today);
			cal.add(Calendar.DATE, -i*7);
			weekTime.add(cal.getTime());
			Float score=healthPointService.getOnePeriodScore(current.getId(), current.getCode(), cal.getTime(), Calendar.DATE,7,1);
			week.add(score);
		}
		map.put("sport_week", week);
		map.put("week", weekTime);
		List<Object> month=new ArrayList<Object>();
		List<Object> monthTime=new ArrayList<Object>();
		for(int i=5;i>0;i--){
			cal.setTime(today);
			cal.add(Calendar.MONTH, -i);
			monthTime.add(cal.getTime());
			Float score=healthPointService.getOnePeriodScore(current.getId(), current.getCode(), cal.getTime(), Calendar.MONTH,1,2);
			month.add(score);
		}
		map.put("sport_month", month);
		map.put("month", monthTime);
		JSONObject obj=JSONObject.fromObject(map);
		return obj.toString();
	}
	
	
	/**
	 * 定时更新环境数据
	 */
	@Scheduled(cron="0 0 12 * * ?")
	public void saveEnvironment(){
		int totalNumber=trackService.getTotalNumber();
		int totalPage=totalNumber%10==0?totalNumber/10:(totalNumber/10+1);
		for(int i=1;i<=totalPage;i++){
			Page<HfTrack> page=trackService.getOnePage(i, 10);
			if(page!=null){
				List<HfTrack> list=page.getContent();
				for(HfTrack track:list){
					if(StringUtils.isNotBlank(track.getLatitude())&&StringUtils.isNotBlank(track.getLongitude())){
						WeatherApi api=WeatherApi.getInstance(Double.valueOf(121), Double.valueOf(31));
						HfCheckData checkData=new HfCheckData();
						checkData.setCheckCategory(PublicProperty.environmentData);
						checkData.setCheckData(api.getHumidity().replaceAll("%", ""));
						checkData.setCheckDate(new Date());
						checkData.setCheckType(PublicProperty.environmentData_SD);
						checkData.setCode(track.getCode());
						checkData.setCreateDate(new Date());
						checkData.setDeleteFlag(0);
						checkData.setUserId(track.getUserId());
						checkData.setCheckAddress(track.getPosition());
						checkDataService.save(checkData);
						HfCheckData checkData1=new HfCheckData();
						checkData1.setCheckCategory(PublicProperty.environmentData);
						checkData1.setCheckData(api.getWindPower().replaceAll("级", ""));
						checkData1.setCheckDate(new Date());
						checkData1.setCheckType(PublicProperty.environmentData_ZY);
						checkData1.setCode(track.getCode());
						checkData1.setCreateDate(new Date());
						checkData1.setDeleteFlag(0);
						checkData1.setUserId(track.getUserId());
						checkData1.setCheckAddress(track.getPosition());
						checkDataService.save(checkData1);
					}
				}
			}
		}
	}
	
}
