package com.hafu.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hafu.dto.UserDto;
import com.hafu.entity.HfAlert;
import com.hafu.entity.HfUser;
import com.hafu.service.HfAlertService;
import com.hafu.utils.AjaxResutObject;
import com.hafu.utils.JsonDateValueProcessor;
import com.hafu.utils.StaticVarible;

/**
 * 报警服务
 *
 */
@Controller
@RequestMapping("alter")
public class AlterController {
	private static Logger log = LoggerFactory.getLogger(AlterController.class);
	/*@Autowired
	private IndexService indexService;*/
	
	@Resource
	private  HfAlertService alertService;
	
	/**
	 * 报警记录 
	 */
	@RequestMapping("user_alter_list")
	public String userAlterList(Model model,HttpServletRequest req){
		UserDto current = (UserDto) req.getSession().getAttribute("CurrentUser");
		HfUser login = (HfUser) req.getSession().getAttribute("LoginUser");
		Long userId = current.getId();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("code", current.getCode());
		List<HfAlert> list = alertService.listAlert(current.getId(),current.getCode(), login.getId(),true);//indexService.userAlertList(map);
		model.addAttribute("list",list);
		model.addAttribute("listNum",list.size());
		return "user_alter_list";
	}
	@RequestMapping("getAlter")
	@ResponseBody
	public String getAlterById(@Param(value="id")Integer id ){
		AjaxResutObject ob = new AjaxResutObject();
		HfAlert hfAlert = alertService.selectById(id);
		if(hfAlert != null){
			ob.setStatus(StaticVarible.SUCCESS);
			ob.setOb(hfAlert);
		}else{
			ob.setStatus(StaticVarible.ERROR);
		}
		JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(java.util.Date.class,new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
		return JSONObject.fromObject(ob,jsonConfig).toString();
	}
	/**
	 * 异步获取未读报警记录 
	 */
	@RequestMapping("noread")
	@ResponseBody
	public String noread(HttpServletRequest req){
		log.debug("***进入未读报警查询控制器");
		UserDto current = (UserDto) req.getSession().getAttribute("CurrentUser");
		HfUser login = (HfUser) req.getSession().getAttribute("LoginUser");
		Long userId = current.getId();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("code", current.getCode());
		map.put("readStatus", 0);
		List<HfAlert> alter =alertService.listAlert(current.getId(),current.getCode(), login.getId(),false); //indexService.noreadAlter(map);
		AjaxResutObject ob = new AjaxResutObject();
		ob.setOb((alter==null||alter.size()==0)?null:alter.get(0));
		ob.setStatus(StaticVarible.SUCCESS);
		JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(java.util.Date.class,new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
        log.debug("未读报警控制器返回数据***"+JSONObject.fromObject(ob,jsonConfig).toString());
		return JSONObject.fromObject(ob,jsonConfig).toString();
	}
	
	/**
	 * 异步修改报警为 已读
	 */
	@RequestMapping("read")
	@ResponseBody
	public String read(HttpServletRequest req){
		log.debug("***进入读取警报");
		AjaxResutObject ob = new AjaxResutObject();
		try{
			UserDto current = (UserDto) req.getSession().getAttribute("CurrentUser");
			HfUser login = (HfUser) req.getSession().getAttribute("LoginUser");
			Long userId = current.getId();
			Long alterId = Long.parseLong(req.getParameter("alterId"));
			//indexService.readAlter(userId,alterId,current.getCode());
			alertService.readAlert(alterId, login.getId());
			ob.setStatus(StaticVarible.SUCCESS);
			return JSONObject.fromObject(ob).toString();
		}catch(Exception e){
			e.printStackTrace();
			ob.setStatus(StaticVarible.ERROR);
			return JSONObject.fromObject(ob).toString();
		}
	}
}
