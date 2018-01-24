package com.hafu.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hafu.modules.utils.BasicTypeUtils;
import org.hafu.modules.utils.VSFtpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.hafu.dto.UserDto;
import com.hafu.entity.HfConcernDevice;
import com.hafu.entity.HfDevice;
import com.hafu.entity.HfModuleSettings;
import com.hafu.entity.HfUser;
import com.hafu.service.IndexService;
import com.hafu.service.UserService;
import com.hafu.utils.AjaxResutObject;
import com.hafu.utils.JsonDateValueProcessor;
import com.hafu.utils.StaticVarible;
import com.hafu.utils.UpLoadFile;
import com.hafu.weixin.util.WeixinUtil;
/**
 * 个人设置
 */
@Controller
@RequestMapping("set")
public class UserSettingController {
	@Autowired
	private UserService userService;
	@Autowired
	private IndexService indexService;
	private static Logger log = LoggerFactory.getLogger(UserSettingController.class);
	public static final ResourceBundle BUNDLE = PropertyResourceBundle.getBundle("smsconfig");
	//头像路径
	public static final String HEADPIC=BasicTypeUtils.notNull(BUNDLE.getString("apache.picimg.relative"));
	
	
	
	
	
	/**
	 * 修改头像 
	 */
	@RequestMapping("avaterUpload")
	@ResponseBody
	public String avaterUpload(@RequestParam MultipartFile avater,HttpServletRequest req){
		try{
			UserDto cu = (UserDto)req.getSession().getAttribute("CurrentUser");
			Long currentUserId = cu.getId();
			if(avater != null){
				String valid = validateImage(avater);
				if(!valid.equals("")){
					return valid;
				}
				HfUser syuser = userService.getUser(currentUserId);
				String picName = "";
				CommonsMultipartFile cf = (CommonsMultipartFile) avater;
				DiskFileItem fi = (DiskFileItem) cf.getFileItem();
				File file = fi.getStoreLocation();
				if(StringUtils.isEmpty(syuser.getAvater())){
					String picFileFileName = avater.getOriginalFilename();
					int end = picFileFileName.lastIndexOf(".");
					String extension = picFileFileName.substring(end);
					picName =new Date().getTime()+"_"+ syuser.getId().toString() +extension;
				}else{
					picName = syuser.getAvater();
				}
				boolean flag = UpLoadFile.upLoadInputStream(HEADPIC,  new FileInputStream(file), picName,0);
				if(flag){
					syuser.setAvater(picName);
					userService.updateUser(syuser);
					cu.setAvater(picName);
					req.getSession().setAttribute("CurrentUser", cu);
					return picName;
				}else{
					return "error";
				}
			}else{
				return "noImg";
			}
		}catch(Exception e){
			e.printStackTrace();
			return "error";
		}
	}
	
	public static String validateImage(MultipartFile picFile){
		String error="";
		CommonsMultipartFile cf = (CommonsMultipartFile) picFile;
		DiskFileItem fi = (DiskFileItem) cf.getFileItem();
		File file = fi.getStoreLocation();
		if (file != null) {
			if(file.length() < 2000 * 1024) {
				Pattern p = Pattern .compile("^(([a-zA-Z]*)+\\/)+(bmp|png|gif|jpeg|jpg|x-png|pjpeg)$");
				Matcher m = p.matcher(picFile.getContentType());
				if (m.find()) {
					return error="";
				} else {
					error = "typeError";
				}
			} else {
				error = "tooLarge";
			}
		}else{
			error = "noImg";
		}
		return error;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 修改头像 
	 */
	@RequestMapping("avater")
	@ResponseBody
	public String avater(@RequestParam MultipartFile avater,HttpServletRequest req){
		try{
			UserDto cu = (UserDto)req.getSession().getAttribute("CurrentUser");
			Long currentUserId = cu.getId();
			if(avater != null){
				HfUser syuser = userService.getUser(currentUserId);
				String picName = "";
				CommonsMultipartFile cf = (CommonsMultipartFile) avater;
				DiskFileItem fi = (DiskFileItem) cf.getFileItem();
				File file = fi.getStoreLocation();
				if(StringUtils.isEmpty(syuser.getAvater())){
					String picFileFileName = avater.getOriginalFilename();
					int end = picFileFileName.lastIndexOf(".");
					String extension = picFileFileName.substring(end);
					picName =new Date().getTime()+"_"+ syuser.getId().toString() +extension;
				}else{
					picName = syuser.getAvater();
				}
				String result = uploadFile(file, picName,avater);
				if(!StringUtils.isEmpty(result) && !"typeError".equals(result) && !"tooLarge".equals(result)){
					syuser.setAvater(picName);
					userService.updateUser(syuser);
					cu.setAvater(picName);
					req.getSession().setAttribute("CurrentUser", cu);
				}
				return result;
			}else{
				return "noImg";
			}
		}catch(Exception e){
			e.printStackTrace();
			return "error";
		}
	}
	
	/**
	 * 异步请求下载微信服务器图片到本地服务器
	 */
	@RequestMapping("ajaxUpload")
	@ResponseBody
	public String ajaxUpload(HttpServletRequest req){
		AjaxResutObject ob = new AjaxResutObject();
		ByteArrayInputStream in = null;
		try{
			String serverId = req.getParameter("serverId");
			log.error("微信服务器图片id : "+ serverId+"地址");
			if(serverId == null){
				ob.setMsg("非法请求");
				ob.setStatus(StaticVarible.ERROR);
				return JSONObject.fromObject(ob).toString();
			}
			UserDto cu = (UserDto)req.getSession().getAttribute("CurrentUser");
			Long currentUserId = cu.getId();
			HfUser syuser = userService.getUser(currentUserId);
			String picName = "";
			AjaxResutObject ajax = WeixinUtil.downloadMedia(serverId);
			if(ajax.getObByte()==null){
				ob.setMsg("上传失败");
				ob.setStatus(StaticVarible.ERROR);
			}else{
				/*if(StringUtils.isEmpty(syuser.getAvater())){*/
					String extension = ajax.getMsg().substring(ajax.getMsg().indexOf("."));
					picName =new Date().getTime()+"_"+ syuser.getId().toString() +extension;
				/*}else{
					picName = syuser.getAvater();
				}*/
				in = new ByteArrayInputStream( ajax.getObByte());
				/*BufferedOutputStream bw = null;
				try {
					File f = new File("d:/a.jpg");
					if (!f.getParentFile().exists())
						f.getParentFile().mkdirs();
					bw = new BufferedOutputStream(new FileOutputStream("d:/a.jpg"));
					bw.write(ajax.getObByte());
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (bw != null)
							bw.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}*/
				boolean fl = UpLoadFile.upLoadInputStream(HEADPIC, in, picName,0);//VSFtpUtils.defaultPut(in, picName, "apache.picimg.relative", 224);
				if(fl){
					syuser.setAvater(picName);
					userService.updateUser(syuser);
					cu.setAvater(picName);
					req.getSession().setAttribute("CurrentUser", cu);
					ob.setMsg(picName);
					ob.setStatus(StaticVarible.SUCCESS);
				}else{
					ob.setMsg("上传失败");
					ob.setStatus(StaticVarible.ERROR);
				}
			}
			return JSONObject.fromObject(ob).toString();
		}catch(Exception e){
			e.printStackTrace();
			ob.setMsg("系统错误");
			ob.setStatus(StaticVarible.ERROR);
			return JSONObject.fromObject(ob).toString();
		}finally{
			try {
				in.close();
				in=null;
			} catch (IOException e) {
				in=null;
			}
		}
	}
	
	/**
	 * 上传图片
	 * @param file 要上传的文件
	 * @param picName 文件名
	 * @return
	 */
	public String uploadFile(File file, String picName,MultipartFile picFile) {
		String error="";
		if (file != null) {
			if(file.length() < 200 * 1024 ) {
				Pattern p = Pattern .compile("^(([a-zA-Z]*)+\\/)+(bmp|png|gif|jpeg|jpg|x-png|pjpeg)$");
				Matcher m = p.matcher(picFile.getContentType());
				if (m.find()) {
					uploadImage(file,picName,196);
					error = picName;
				} else {
					error = "typeError";
				}
			} else {
				error = "tooLarge";
			}
		}
		return error;
	}
	
	/**
	 * 上传图片接口
	 * @param file	要上传的文件
	 * @param picName 文件名
	 * @throws Exception
	 */
	public void uploadImage(File file,String picName,int gwidth) {						
		VSFtpUtils.defaultPut(file, picName, "apache.picimg.relative", gwidth);	//上传图片
	}
	
	/**
	 * 删除用户 
	 * 删除用户关系，如果该用户没有其他人关注则删除该用户，如果该用户的设备没有人绑定，删除该设备，并解绑
	 */
	@RequestMapping("deleteUser")
	public String deleteUser(HttpServletRequest req){
		HfUser loginUser = (HfUser) req.getSession().getAttribute("LoginUser");
		Long userId = Long.parseLong(req.getParameter("id")); //删除用户id
		try{
			userService.deleteUser(loginUser,userId);
		}catch(Exception e){
			e.printStackTrace();
			return "redirect:/index/device_user_list";
		}
		UserDto cur = (UserDto)req.getSession().getAttribute("CurrentUser");
		if(cur.getId().intValue()==userId.intValue()){
			req.getSession().removeAttribute("CurrentUser");
		}
		return "redirect:/index/device_user_list";
	}
	
	/**
	 * 1.判断 当前持有人 是否已经绑定过设备（一个持有人只能绑定一次）
	 * 2.异步获取该设备是否属于当前持有人
	 */
	/**
	 * @param req
	 * @param session
	 * @return
	 */
	@RequestMapping(value="isbelong")
	@ResponseBody
	public String isbelong(HttpServletRequest req,HttpSession session){
		HfUser loginUser = (HfUser) req.getSession().getAttribute("LoginUser");
		String code = req.getParameter("code");
		String holderId = req.getParameter("holderId");
		AjaxResutObject aj = new AjaxResutObject();
		if(StringUtils.isEmpty(code) || StringUtils.isEmpty(holderId)){
			aj.setStatus(StaticVarible.ERROR);
			aj.setMsg("非法请求");
		}else{
			//1.判断 当前持有人 是否已经绑定过设备（一个持有人只能绑定一次）
			Map<String, Object> bangFlag = indexService.isAgreeBand(loginUser,Long.parseLong(holderId),code);
			String flag= bangFlag.get("flag").toString();
			if(flag.equals("1")){
				aj.setMsg("section1_true");
				aj.setOb((HfDevice)bangFlag.get("ob"));
			}else if(flag.equals("2")){//当前人已经为当前持有人绑定了设备，不能在绑定新设备（但是如果是旧的设备，且不是当前人的仍然可以绑定（相当于绑定新的持有人））
				HfDevice de = indexService.getDeviceByCode(code);
				if(de!=null && (!de.getUserId().toString().equals(holderId))){
					aj.setMsg("false");
				}else{
					aj.setMsg("section1_false");
				}
			}else{
				//2.异步获取该设备是否属于当前持有人
				HfDevice de = indexService.getDeviceByCode(code);
				if(de==null){
					aj.setMsg("true");
				}else{
					if(de.getUserId().toString().equals(holderId)){
						aj.setMsg("true");
					}else{
						aj.setMsg("false");
					}
				}
			}
			aj.setStatus(StaticVarible.SUCCESS);
		}
		return JSONObject.fromObject(aj).toString();
	}
	
	@RequestMapping("module_setting")
	public String moduleSetting(HttpServletRequest req,Model model){
		HfUser loginUser = (HfUser) req.getSession().getAttribute("LoginUser");
		List<HfModuleSettings> setting = indexService.getModuleSettings(loginUser.getId());
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.util.Date.class,new JsonDateValueProcessor("yyyy-MM-dd"));
		model.addAttribute("setting",JSONArray.fromObject(setting,jsonConfig).toString());
		return "setting/module_setting";
	}
	@RequestMapping("save_module")
	@ResponseBody
	public String saveModule(HttpServletRequest req,@RequestParam("module[]")String[] module){
		HfUser lg = (HfUser) req.getSession().getAttribute("LoginUser");
		AjaxResutObject o = new AjaxResutObject();
		if(ArrayUtils.isEmpty(module)){
			o.setStatus("error");
		}else{
			indexService.saveModuleSetting(lg.getId(), module);
			o.setStatus("success");
		}
		return JSONObject.fromObject(o).toString();
	}
}
