package com.hafu.quartz;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hafu.eban.entity.RemindParam;
import com.hafu.eban.entity.response.RemindResponse;
import com.hafu.eban.entity.response.RemindResponse_list;
import com.hafu.eban.service.FamilyReminderService;
import com.hafu.eban.utils.DateUtil;
import com.hafu.entity.HfPrompt;
import com.hafu.entity.HfPromptLog;
import com.hafu.service.HfPromptLogService;
import com.hafu.service.HfPromptService;

@Component
public class EbanPromptQuartzTimer {

	private static Logger logger = Logger.getLogger(EbanPromptQuartzTimer.class);

	@Autowired
	private HfPromptService hfPromptService;
	
	@Autowired
	private HfPromptLogService hfPromptLogService;
	
	/**
	 * 关爱提醒发送
	 */
	@Scheduled(cron = "0 */5 * * * ?")
	public void send() {
		FamilyReminderService service = new FamilyReminderService();
		logger.debug("关爱提醒发送start : "
				+ DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		
		List<HfPrompt> hfPromptList = hfPromptService.findAll();
		for (int i = 0; i < hfPromptList.size(); i++) {
			RemindParam remindParam = new RemindParam();
			RemindResponse remindResponse = null;
			HfPrompt hfPrompt = hfPromptList.get(i);
			//两个日期之间的天数
			int intDays = DateUtil.getBetweenDayNumber(new Date(),hfPrompt.getUpdateDate());
			if(intDays > 0){	//今天还未更新
				String coreExpression = hfPrompt.getCronExpression();
				String date = getDateOrTime(coreExpression, "1");
				String time = getDateOrTime(coreExpression, "2");
				remindParam.setCode(hfPrompt.getCode());
				remindParam.setContent(hfPrompt.getContent());
				remindParam.setDate(date);
				remindParam.setTime(time);
				remindParam.setType(hfPrompt.getPromptType()+"");
				remindParam.setRepeat(hfPrompt.getRepeatType()+"");
				if(hfPrompt.getRepeatType() == 0){	//不重复
					int flag = DateUtil.getBetweenDayNumber(new Date(),DateUtil.parse(date+" "+time));	
					if(flag >= 0){
						//提醒的日期还未到，但需要第一次提醒,创建的时间和更新的时间大于0 说明第一次添加
						if(DateUtil.getBetweenDayNumber(hfPrompt.getCreateDate(),hfPrompt.getUpdateDate()) > 0){
							remindResponse = service.addRemind(remindParam);
							hfPrompt.setUpdateDate(new Date());
						}
					}
				}else if(hfPrompt.getRepeatType() == 1){	//按天重复
					//提醒时间时候在当前时间之前
					int flag = DateUtil.getBetweenDayNumber(new Date(),DateUtil.parse(date+" "+time));
					//提醒的日期已到，需要每天提醒
					if(flag >= 0){
						//提醒时间已过
						if((DateUtil.parse(DateUtil.format(new Date())+" "+time).getTime()+300000) < (new Date().getTime()) ){
							remindParam.setDate(DateUtil.format(DateUtil.getDate(new Date(), 1)));
							remindResponse = service.addRemind(remindParam);
							hfPrompt.setUpdateDate(new Date());
						}
						
						
					}else{
						//提醒的日期还未到，但需要第一次提醒,创建的时间和更新的时间大于0 说明第一次添加
						if(DateUtil.getBetweenDayNumber(hfPrompt.getCreateDate(),hfPrompt.getUpdateDate()) > 0){
							remindResponse = service.addRemind(remindParam);
							hfPrompt.setUpdateDate(new Date());
						}
					}
				}else if(hfPrompt.getRepeatType() == 2){	//按周重复
					String weeks[] = hfPrompt.getLoopString().split(",");
					String week1[] = DateUtil.printWeekdays();
					String[] strs1 = getWeeks(weeks);
					for (int j = 0; j < strs1.length; j++) { // 获取未来七天内星期几的日期
						int w1 = toInt(strs1[j]);
						if( w1 == 7){
							w1 = 1;
						}else{
							w1 = w1+1;
						}
						String date1 = DateUtil.getDates(week1[0], week1[6], w1+"");
						//提醒日期是否在当前日期之前
						boolean flag = DateUtil.beforeCurrentDate(DateUtil.parse(date1));
						//提醒的日期已到，需要每天提醒
						if(!flag){
							///获得当前日期是星期几
							String week = DateUtil.getWeek(DateUtil.format(new Date()));
							//提前一天发送
							int w = toInt(weeks[j]) -1;
							if(w == 0){
								w = 7;
							}
							if(w == DateUtil.getWeekNum(week)){
								if((DateUtil.parse(DateUtil.format(new Date())+" "+time).getTime()+300000) < (new Date().getTime()) ){
									remindParam.setDate(DateUtil.format(DateUtil.getDate(new Date(), 1)));
									remindResponse = service.addRemind(remindParam);
									hfPrompt.setUpdateDate(new Date());
								}
								/*if( j == 0){
									remindParam.setDate(DateUtil.format(DateUtil.getDate(new Date(), 7)));
									remindResponse = service.addRemind(remindParam);
									hfPrompt.setUpdateDate(new Date());
								}else{
									
								}*/
							}
						}else{
							//提醒的日期还未到，但需要第一次提醒,创建的时间和更新的时间大于0 说明第一次添加
							if(DateUtil.getBetweenDayNumber(hfPrompt.getCreateDate(),hfPrompt.getUpdateDate()) > 0){
								remindParam.setDate(date1);
								remindResponse = service.addRemind(remindParam);
								hfPrompt.setUpdateDate(new Date());
							}
						}
					}
				}else if(hfPrompt.getRepeatType() == 3){	//按月重复
					int flag = DateUtil.getBetweenDayNumber(new Date(),DateUtil.parse(date+" "+time));	
					//提醒的日期已到，需要每天提醒
					if(flag >= 0){
						//更新时间和当前时间是否是同一个月
						if(DateUtil.countMonths(DateUtil.format(hfPrompt.getUpdateDate()), DateUtil.format(new Date())) > 1){
							remindParam.setDate(DateUtil.format(DateUtil.getAddDayNumber(new Date(), DateUtil.getDayOfMonth())));
							remindResponse = service.addRemind(remindParam);
							hfPrompt.setUpdateDate(new Date());
						}
					}else{
						//提醒的日期还未到，但需要第一次提醒,创建的时间和更新的时间大于0 说明第一次添加
						if(DateUtil.getBetweenDayNumber(hfPrompt.getCreateDate(),hfPrompt.getUpdateDate()) > 0){
							remindResponse = service.addRemind(remindParam);
							hfPrompt.setUpdateDate(new Date());
						}
					}
				}else if(hfPrompt.getRepeatType() == 4){	//按年重复
					int flag = DateUtil.getBetweenDayNumber(new Date(),DateUtil.parse(date+" "+time));	
					if(flag >= 0){
						//更新时间和当前时间是否大于一年
						if(DateUtil.countMonths(DateUtil.format(hfPrompt.getUpdateDate()), DateUtil.format(new Date())) > 12){
							remindParam.setDate(DateUtil.format(DateUtil.getAddDayNumber(new Date(), DateUtil.getDayOfMonth())));
							remindResponse = service.addRemind(remindParam);
							hfPrompt.setUpdateDate(new Date());
						}
					}else{
						//提醒的日期还未到，但需要第一次提醒,创建的时间和更新的时间大于0 说明第一次添加
						if(DateUtil.getBetweenDayNumber(hfPrompt.getCreateDate(),hfPrompt.getUpdateDate()) > 0){
							remindResponse = service.addRemind(remindParam);
							hfPrompt.setUpdateDate(new Date());
						}
					}
				}
				hfPromptService.save(hfPrompt);
				//保存到已发送列表里面
				if(remindResponse != null){
					if("1".equals(remindResponse.getSuccess()) ){
						for (int j = 0; j < remindResponse.getList().size(); j++) {
							RemindResponse_list remindResponse1 = remindResponse.getList().get(j);
							String id = remindResponse1.getId();
							HfPromptLog hfPromptLog = hfPromptLogService.findOne(hfPrompt.getCode(), id);
							if(hfPromptLog == null){
								hfPromptLog = new HfPromptLog();
								hfPromptLog.setCode(hfPrompt.getCode());
								hfPromptLog.setRepateType(remindResponse1.getRepeat());;
								hfPromptLog.setContent(remindResponse1.getContent());
								hfPromptLog.setCreateTime(new Date());
								hfPromptLog.setDeleteFlag(0);
								String expiredTime = DateUtil.format(DateUtil.TimeStamp2DateFomart(remindResponse1.getDate()))
										+" "+DateUtil.parserTominute(remindResponse1.getTime());
								hfPromptLog.setRemindDate(DateUtil.parse(expiredTime));
								hfPromptLog.setExpiredTime(DateUtil.parse(expiredTime).getTime()+300000);
								hfPromptLog.setPromptId(hfPrompt.getId());
								hfPromptLog.setRemindTime(DateUtil.parse(expiredTime).getTime());
								hfPromptLog.setUpdateTime(new Date());
								hfPromptLog.setEbanId(toInt(id));
								hfPromptLogService.save(hfPromptLog);
							}
						}
					}
				}
			}
		}
		logger.debug("关爱提醒发送end : "
				+ DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
	}

	/**
	 * 关爱提醒删除
	 */
	@Scheduled(cron = "0 */3 * * * ?")
	public void delete() {
		logger.debug("关爱提醒删除start : "
				+ DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		List<HfPromptLog> hfPromptLogList = hfPromptLogService.findAll();
		FamilyReminderService service = new FamilyReminderService();
		for (int i = 0; i < hfPromptLogList.size(); i++) {
			HfPromptLog hfPromptLog = hfPromptLogList.get(i);
			if(new Date().getTime() > hfPromptLog.getExpiredTime()){
				RemindResponse remindResponse = service.delRemind(hfPromptLog.getEbanId()+"", hfPromptLog.getCode());
				try{
					if("1".equals(remindResponse.getSuccess())){
						hfPromptLog.setDeleteFlag(1);
						hfPromptLog.setUpdateTime(new Date());
					}
				}catch(Exception e){
					hfPromptLog.setDeleteFlag(1);
					hfPromptLog.setUpdateTime(new Date());
				}
				hfPromptLogService.save(hfPromptLog);
			}
		}
		logger.debug("关爱提醒删除end : "
				+ DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
	}
	
	public static String getDateOrTime(String coreExpression,String type){
		String returnString = "";
		try{
			String[] strs = coreExpression.split(" ");
			if("1".equals(type)){	//日期
				returnString = strs[6]+"-"+strs[4]+"-"+strs[3];
			}else if("2".equals(type)){	//時間
				returnString = strs[2]+":"+strs[1];
			}
		}catch(Exception e){
			logger.error("getDateOrTime Error:"+e.getMessage());
		}
		return returnString;
	}

	public static Integer toInt(String param) {
		int p = 0;
		if (StringUtils.isNotBlank(param)) {
			p = Integer.parseInt(param);
		}
		return p;
	}
	
	public static void main(String[] args) {
		/*System.out.println(new Date().getTime());
		System.out.println(DateUtil.parse(DateUtil.format(new Date())+" 12:00:00").getTime());
		System.out.println(DateUtil.getBetweenDayNumber(new Date(),DateUtil.parse(DateUtil.format(new Date())+" 12:00:00")));*/
		
		String week[] = DateUtil.printWeekdays();
		String weeks[] = new String[] { "1", "2", "3", "4", "5" };
		String[] strs1 = new String[weeks.length];
		int[] a = new int[weeks.length];
		for (int i = 0; i < weeks.length; i++) {
			int w = toInt(weeks[i]) - 1;
			if (w == 0) {
				w = 7;
			}
			a[i] = w;
		}
		Arrays.sort(a); // 进行排序
		for (int i = 0; i < a.length; i++) {
			strs1[i] = a[i]+"";
		}

		for (int j = 0; j < strs1.length; j++) { // 获取未来七天内星期几的日期
			int w = toInt(strs1[j]);
			System.out.println(w);
			if( w == 7){
				w = 1;
			}else{
				w = w+1;
			}
			String date1 = DateUtil.getDates(week[0], week[6], w+"");
			System.out.println(date1);
		}
	}
	
	public static int countMonths(String date1,String date2,String pattern){
        SimpleDateFormat sdf=new SimpleDateFormat(pattern);
        Calendar c1=Calendar.getInstance();
        Calendar c2=Calendar.getInstance();
        try {
			c1.setTime(sdf.parse(date1));
			c2.setTime(sdf.parse(date2));
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
        int year =c2.get(Calendar.YEAR)-c1.get(Calendar.YEAR);
        
        //开始日期若小月结束日期
        if(year<0){
            year=-year;
            return year*12+c1.get(Calendar.MONTH)-c2.get(Calendar.MONTH);
        }
       
        return year*12+c2.get(Calendar.MONTH)-c1.get(Calendar.MONTH);
    }
	
	public String[] getWeeks(String[] weeks){
		String[] strs1 = new String[weeks.length];
		int[] a = new int[weeks.length];
		for (int k = 0; k < weeks.length; k++) {
			int w = toInt(weeks[k]) - 1;
			if (w == 0) {
				w = 7;
			}
			a[k] = w;
		}
		Arrays.sort(a); // 进行排序
		for (int l = 0; l < a.length; l++) {
			strs1[l] = a[l]+"";
		}
		return strs1;
	}

}
