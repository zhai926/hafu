package com.hafu.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hafu.dto.UserDto;
import com.hafu.eban.entity.RemindParam;
import com.hafu.eban.entity.response.DayWeatherResponse;
import com.hafu.eban.service.OtherService;
import com.hafu.entity.HfEncouragement;
import com.hafu.entity.HfPrompt;
import com.hafu.entity.HfUser;
import com.hafu.service.EncouragementService;
import com.hafu.service.HfPromptService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "prompt")
public class PromptController {
	
	@Resource
	private EncouragementService encouragementService;
	
	@Resource
	private HfPromptService promptService;
	
	@ResponseBody
	@RequestMapping(value = "addPrompt")
	public String addPrompt(HttpServletRequest req,@ModelAttribute RemindParam remindParam,
			@RequestParam(value="week[]",required=false)int[] week) {
		UserDto current = (UserDto) req.getSession()
				.getAttribute("CurrentUser");
		JSONObject obj = new JSONObject();
		//FamilyReminderService service = new FamilyReminderService();
		//SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String cron="0 %s %s %s %s %s %s";//分、时、日、月、周几、年
		String success="0";
		if(StringUtils.isNotBlank(current.getCode())&&current.getId()!=null){
			Matcher matcher=Pattern.compile("(?<hour>\\d+):(?<minute>\\d+)").matcher(remindParam.getTime());
			if(matcher.find()){
				Integer hour=Integer.valueOf(matcher.group("hour"));
				Integer minute=Integer.valueOf(matcher.group("minute"));
				HfPrompt prompt=null;
				if(StringUtils.isNotBlank(remindParam.getId())){
					prompt=promptService.findOne(Long.valueOf(remindParam.getId()), current.getCode());
				}else{
					prompt=new HfPrompt();
				}
				prompt.setCode(current.getCode());
				prompt.setContent(remindParam.getContent());
				Calendar c=Calendar.getInstance();
				c.setTime(new Date());
				c.add(Calendar.DATE, -1);
				if(prompt.getId()!=null){
					prompt.setUpdateDate(c.getTime());
				}else{
					prompt.setCreateDate(new Date());
					prompt.setUpdateDate(c.getTime());	
				}
				prompt.setDeleteFlag(0);
				prompt.setHour(hour);
				prompt.setMinute(minute);
				prompt.setUserId(current.getId());
				prompt.setPromptType(Integer.valueOf(remindParam.getType()));
				prompt.setIsPrompt(0);
				if(week==null){
					/*remindParam.setDate(sdf.format(new Date()));
					RemindResponse response=service.addRemind(remindParam);
					success=response.getSuccess();*/
					Calendar calendar=Calendar.getInstance();
					prompt.setCronExpression(String.format(cron, minute,hour,calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.MONTH)+1,"*",calendar.get(Calendar.YEAR)));
					prompt.setRepeatType(0);
					promptService.save(prompt);
					success="1";
				}else if(week.length>0){
					//Calendar calendar=Calendar.getInstance();
					prompt.setRepeatType(2);
					StringBuffer buff=new StringBuffer();
					for(int i=0;i<week.length;i++){
						buff.append(week[i]+1);
						if(i!=week.length-1){
							buff.append(",");
						}
						/*int value=0;
						if(week[i]!=6) value=week[i]+2;
						else value=1;
						calendar.set(Calendar.DAY_OF_WEEK, value);
						remindParam.setDate(sdf.format(calendar.getTime()));
						RemindResponse response=service.addRemind(remindParam);
						success=response.getSuccess();*/
					}
					prompt.setCronExpression(String.format(cron, minute,hour,"*","*",buff.toString(),"*"));
					prompt.setLoopString(buff.toString());
					promptService.save(prompt);
					success="1";
				}
			}
		}
		obj.put("success", success);
		return obj.toString();
	}

	@RequestMapping(value = "toAddPrompt")
	public String toAddPrompt(@RequestParam(value="id",required=false)Long id,
			HttpServletRequest req,Model model) {
		if(id!=null){
			UserDto current = (UserDto) req.getSession()
					.getAttribute("CurrentUser");
			HfPrompt prompt=promptService.findOne(id, current.getCode());
			if(prompt!=null){
				model.addAttribute("prompt", prompt);
			}
		}
		return "/prompt/addPrompt";
	}

	@RequestMapping(value = "promptList")
	public String promptList(HttpServletRequest req,Model model) {
		UserDto current = (UserDto) req.getSession()
				.getAttribute("CurrentUser");
		/*FamilyReminderService service = new FamilyReminderService();
		RemindResponse response = service.listRemind(current.getCode());
		List<RemindResponse_list> list = response.getList();*/
		List<HfPrompt> list=promptService.getPromptByCode(current.getCode());
		model.addAttribute("list", list);
		return "/prompt/promptList";
	}
	
	@ResponseBody
	@RequestMapping(value="delePrompt")
	public String delePrompt(@RequestParam(value="id")Long id,
			HttpServletRequest req){
		UserDto current = (UserDto) req.getSession()
				.getAttribute("CurrentUser");
		/*FamilyReminderService service = new FamilyReminderService();
		RemindResponse response=service.delRemind(String.valueOf(id), current.getCode());
		JSONObject obj=JSONObject.fromObject(response);*/
		HfPrompt prompt=promptService.deletePrompt(id, current.getCode());
		JSONObject obj=new JSONObject();
		obj.put("success", prompt!=null?1:0);
		return obj.toString();
	}
	
	@ResponseBody
	@RequestMapping(value="editPrompt")
	public String editPrompt(@ModelAttribute RemindParam remind,
			HttpServletRequest req){
		UserDto current = (UserDto) req.getSession()
				.getAttribute("CurrentUser");
		String success="0";
		/*remind.setCode(current.getCode());
		FamilyReminderService service = new FamilyReminderService();
		RemindResponse response=service.editRemind(remind);
		JSONObject obj=JSONObject.fromObject(response);*/
		HfPrompt prompt=promptService.findOne(Long.valueOf(remind.getId()), current.getCode());
		if(prompt!=null){
			prompt.setIsPrompt(Integer.valueOf(remind.getRepeat()));
			prompt.setUpdateDate(new Date());
			promptService.save(prompt);
			success="1";
		}
		JSONObject obj=new JSONObject();
		obj.put("success", success);
		return obj.toString();
	}
	
	@RequestMapping(value="/encourageList")
	public String encourageList(HttpServletRequest req,Model model){
		UserDto current = (UserDto) req.getSession()
				.getAttribute("CurrentUser");
		Page<HfEncouragement> page=encouragementService.encourageList(current.getId(), current.getCode(), 1, 10);
		model.addAttribute("list", page.getContent());
		return "/prompt/encourageList";
	}
	
	@ResponseBody
	@RequestMapping(value="addEncourage")
	public String addEncourage(@ModelAttribute HfEncouragement encouragement,
			HttpServletRequest req){
		UserDto current = (UserDto) req.getSession()
				.getAttribute("CurrentUser");
		HfUser loginUser=(HfUser) req.getSession().getAttribute("LoginUser");
		encouragement.setCode(current.getCode());
		OtherService other=new OtherService();
		DayWeatherResponse response=other.healthEncourage(current.getCode(), encouragement.getEncouragementType());
		JSONObject obj=new JSONObject();
		if("1".equals(response.getSuccess())){
			encouragement.setSendUserId(loginUser.getId());
			encouragement.setCreateDate(new Date());
			encouragement.setDeleteFlag(0);
			encouragement.setReceiveUserId(current.getId());
			encouragement.setSendDate(new Date());
			encouragement.setSendStatus(0);
			encouragementService.save(encouragement);
		}
		obj.put("success", response.getSuccess());
		return obj.toString();
	}

}
