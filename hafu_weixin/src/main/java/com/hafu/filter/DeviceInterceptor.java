package com.hafu.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.hafu.controller.RegisterController;
import com.hafu.dto.UserDto;
import com.hafu.entity.HfUser;
import com.hafu.service.IndexService;
import com.hafu.service.UserService;
/**
 * 登陆 拦截器 (默认进入公共号必须静默登陆)
 * @author zc
 *
 */
public class DeviceInterceptor  extends HandlerInterceptorAdapter{
	
	private static Logger log = LoggerFactory.getLogger(DeviceInterceptor.class);
	
	@Autowired
	private UserService userService;
	@Autowired
	private IndexService indexService;
	/**
	 * 公共查询是否已经绑定设备， 用户初始化
	 * @throws IOException 
	 */
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
		try {
			log.debug("进入拦截器方法preHandle");
			HfUser loginUser = (HfUser)req.getSession().getAttribute("LoginUser");
			UserDto cu = (UserDto) req.getSession().getAttribute("CurrentUser");
			if(cu==null){
				Map<String, Object> map = new HashMap<String, Object>(); //判断是否已经绑定过
				map.put("userId", loginUser.getId());
				UserDto curUser = userService.getLastBandingUser(map);
				req.getSession().setAttribute("CurrentUser", curUser);
				if(curUser==null){ //无绑定设备（可能有关注人）
					List<UserDto> hl = indexService.myBangHolder(loginUser.getId()); //判断是否有绑定人
					if(hl==null || hl.size()==0){ //无关注人跳入注册页
						log.debug("准备进入欢迎页面");
						log.debug("req.getPathInfo() = " + req.getPathInfo());
						res.sendRedirect("/register/towelcome");
//						res.sendRedirect("/register/register");
						return false;
					}else{ //有关注人返回true
						req.getSession().setAttribute("CurrentUser", hl.get(0));//注意该持有人没有设备
						return true;
					}
				}else{
					return true;
				}
			}else{
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			req.setAttribute("msg","系统错误！");
			res.sendRedirect("/index/error");
			return false;
		}	
	}
}
