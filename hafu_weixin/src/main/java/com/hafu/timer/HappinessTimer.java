package com.hafu.timer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hafu.entity.HfBehaviorRecord;
import com.hafu.entity.HfDevice;
import com.hafu.entity.HfEncouragement;
import com.hafu.entity.HfHappiness;
import com.hafu.entity.HfVoice;
import com.hafu.service.BehaviorRecordService;
import com.hafu.service.EncouragementService;
import com.hafu.service.HfDeviceService;
import com.hafu.service.HfHappinessService;
import com.hafu.service.HfVoiceService;

/*@Component*/
public class HappinessTimer {
	// LOG日志	
	private static Logger log = LoggerFactory.getLogger(HappinessTimer.class);
	@Autowired
    private HfDeviceService hfDeviceService;
	@Autowired
	private HfHappinessService happinessService;
	@Autowired
    private HfVoiceService hfVoiceService;
	@Autowired
	private EncouragementService encouragementService;
	@Autowired 
	private BehaviorRecordService behaviorService;
	
	/*@Scheduled(cron = "0/30 * * * * ?")*/
	public void testTimer(){
		//log.debug("***ceshi定时");
		
		List<HfDevice> deviceList = hfDeviceService.getAllDevice();
//		log.debug("绑定设备的用户总数***"+deviceList.size());
		if(deviceList != null && deviceList.size() != 0){
			for(int i = 0; i< deviceList.size();i++){
				HfDevice hfDevice = deviceList.get(i);
				int score = getHappinessScore(hfDevice);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = new Date();
				String date_str = sdf.format(date);
//				log.debug("****日期字符串"+date_str);
				
				Long userId = hfDevice.getUserId();
				String code = hfDevice.getCode();
				String endTime = sdf2.format(date);
				List<HfHappiness> happinessList = happinessService.findHappiness(userId, date_str, code);
				if(happinessList == null || happinessList.size() == 0){
					HfHappiness happiness = new HfHappiness();
					happiness.setCode(hfDevice.getCode());
					happiness.setUserId(hfDevice.getUserId());
					happiness.setCreateDate(new Date());
					happiness.setEndDate(new Date());
					happiness.setHappiness(score);
					happiness.setCreateDay(date_str);
					happinessService.insertHappiness(happiness);
				}else{
//					log.debug("***进入更新数据库");
					HfHappiness happiness = happinessList.get(0);
					happiness.setEndDate(new Date());
					happiness.setHappiness(score);
					happinessService.updateHappiness(happiness);
				}
				
			}
			
		}
	}
	public int getHappinessScore(HfDevice hfDevice) {
		int base_score = 60;
		String code = hfDevice.getCode();
		Long currentId = hfDevice.getUserId();
		int care = 0;
		int encourage = 0;
		//获取关爱和鼓励的分数
//		log.debug("***定时器关爱和鼓励获取");
		Page<HfEncouragement> page=encouragementService.todayEncourageList(currentId, code, 1, 10);	
		
		if(page != null){
			List<HfEncouragement> hfEncouragement_list = page.getContent();
			if(hfEncouragement_list != null && hfEncouragement_list.size() != 0){
				for(int i=0;i<hfEncouragement_list.size();i++){
					HfEncouragement encouragement = hfEncouragement_list.get(i);
					if(encouragement.getEncouragementType() == 1){
						encourage = encourage + 1;
					}else if(encouragement.getEncouragementType() == 2){
						care = care + 1;
					}else{
						
					}
				}
			}
		}
//		log.debug("获取care***"+care);
//		log.debug("获取encourage***"+encourage);
		int encourage_score = care + encourage;
//		log.debug("***定时器鼓励分数**" + encourage_score);
		//获取5天佩戴情况
		log.debug("***定时器佩戴情况获取");
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
		
		List isWear_list = new ArrayList();
		List step_list = new ArrayList();
		int k = 0;
		long time=0l;
		String regEx="[^0-9]"; 
		Pattern p = Pattern.compile(regEx);   
		Matcher m = null;
		String step = "";
		int stepNum = 0;
		long wear_time = 0l;
		//之前5日的行为记录及佩戴状态
		for(int i=0;i<6;i++){
			//i=5时为今天的记录
			if(i == 5){
				time = now;//59秒
			}else{
				
				time = now - todayMilliSecond-1000 - i*everydayMilliSecond;//59秒
			}
			k = k+1;
			Date date = new Date(time);
			String dateStr = sdf1.format(date);
			String endDate = sdf2.format(date);
			List<HfBehaviorRecord> behavior_list = behaviorService.behaviorList(currentId , dateStr, code , endDate);
			
			//获取之前每日是否佩戴状态
			boolean isWear = false;
			if(behavior_list != null && behavior_list.size() != 0){
				for(int j=0;j<behavior_list.size();j++){
					HfBehaviorRecord hfBehaviorRecord = behavior_list.get(j);
					String behavType = hfBehaviorRecord.getBehaviorType();
					if(StringUtils.isNotEmpty(behavType)){
						if(behavType.contains("未佩戴")){
							continue;
						}else{
							isWear = true;
							break;
						}
					}
				}
			Map<String,Object> isWear_map = new HashMap<String,Object>();	
				isWear_map.put("day", k);
				isWear_map.put("isWear", isWear);
				isWear_list.add(isWear_map);
			}else{
				
			}
//			log.debug("***定时器今日步数及佩戴时间获取");
			//获取今日步数和佩戴时间
			if(i == 5){
				if(behavior_list != null && behavior_list.size() != 0){
					for(int j=0;j<behavior_list.size();j++){
						HfBehaviorRecord hfBehaviorRecord = behavior_list.get(j);
						String bh_type = hfBehaviorRecord.getBehaviorType();
						if(StringUtils.isNotEmpty(bh_type)){
							if(bh_type.contains("走") || bh_type.contains("步行")){
								m = p.matcher(bh_type);
								step = m.replaceAll("").trim();
								if(!StringUtils.isEmpty(step)){
									stepNum = stepNum + Integer.parseInt(step);
								}else{
									
								}
							}
							if(!bh_type.contains("未佩戴")){
								long duration = hfBehaviorRecord.getEndTime().getTime() - hfBehaviorRecord.getStartTime().getTime();
								wear_time = wear_time +duration;
							}
							
						}
				Map<String,Object> step_map = new HashMap<String,Object>();	
				step_map.put("day", k);
				step_map.put("stepNum", stepNum);
				step_list.add(step_map);
					}
				}
			
			}
			
	}
	//获取穿戴分数
//	log.debug("***定时器佩戴分数获取");
	int wear_score = 0;
	
	int wear_index = (int) (Math.random()*2);//0-1随机数
	int y = (int) Math.random()*6;
	for(int i=0; i< isWear_list.size(); i++){
		Map<String,Object> isWear_map = (Map<String, Object>) isWear_list.get(i);
//		log.debug("获取第*天的佩戴情况"+isWear_map.get("day"));
		int day = (Integer) isWear_map.get("day");
		boolean isWear = (Boolean) isWear_map.get("isWear");
		if(day == 1){
			if(isWear == true){
				wear_score = wear_score + 3*wear_index;
			}else{
				wear_score = wear_score -2*wear_index;
			}
		}else if(day == 2){
			if(isWear == true){
				wear_score = wear_score + 2;
			}else{
				wear_score = wear_score -wear_index;
			}
		}else if(day == 3){
			if(isWear == true){
				wear_score = wear_score + 2;
			}else{
				wear_score = wear_score -wear_index;
			}
		}else if(day == 4){
			if(isWear == true){
				wear_score = wear_score + 2;
			}else{
				wear_score = wear_score -wear_index;
			}
		}else if(day == 5){
			if(isWear == true){
				wear_score = wear_score + 2;
			}else{
				wear_score = wear_score -3 * y;
			}
		}else if(day == 6){
			if(isWear == true){
				wear_score = wear_score + 8;//今日佩戴+8分
			}else{
				wear_score = wear_score -3*wear_index;
			}
		}
		
	}
	
//	log.debug("***定时器佩戴分数**" + wear_score);
	if(wear_time > 6*60*60*1000 || wear_time == 6*60*60*1000){
		wear_score = wear_score + 10;//今日佩戴超过6小时加10分
	}
	
	//获取今日步数的分数
//	log.debug("***定时器今日步数分数获取");
	int step_score = 0;
	if(stepNum != 0){
		if(stepNum <1000){
			step_score = step_score + 7;
		}else{
			step_score = ((stepNum-1000)/1000)*2+7;
		}
	}
//	log.debug("***定时器今日步数分数**" + step_score);
	//获取语音的分数
//	log.debug("***定时器语音获取");
	int voice_score = 0;
	List<HfVoice> voiceList = hfVoiceService.getVoiceList(currentId, code);
	if(voiceList != null && voiceList.size() != 0){
		int size = voiceList.size();
		if(size == 1){
			voice_score = voice_score + 5;
		}else if(size == 2 || size == 3){
			voice_score = voice_score + 5 + (size - 1)*3;
		}else{
			voice_score = voice_score + 5 + 3 + 3 + size - 3;
		}
	}
//	log.debug("***定时器语音分数**" + voice_score);
	//获取综合分数
//	log.debug("***定时器综合分数获取");
	int score = base_score;
	score = base_score + wear_score + encourage_score + step_score + voice_score ;
//	log.debug("基本分"+base_score+"+穿戴分"+wear_score+"+鼓励分"+encourage_score+"+步数分"+step_score+"+语音分"+voice_score+"=总分"+score);
	if(score == 100 || score > 100){
		score = 100;
		score = (int) (score - Math.random()*6);//满分微调,减去0-5的随机整数
	}
//	log.debug("***定时器综合分数**" + score);
	return score;
}
}
