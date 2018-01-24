package com.hafu.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hafu.entity.HfUser;
import com.hafu.service.UserService;
import com.hafu.weixin.util.WeixinUtil;


/**
 * 账号控制类
 * @author zhang
 *
 */
@Controller
@Scope("session")
@RequestMapping(value = "/account")
public class AccountController {
	
	@Autowired
	private UserService userService;
	
	/**
	 * 跳转进入注册页面
	 * @return
	 */
	@RequestMapping(value="goRegister")
	public String doGoRegister() {
		
		return "account/register_page";
	}
	
	/**
	 * 注册功能
	 * @return
	 */
	@RequestMapping(value="register", method=RequestMethod.POST)
	public @ResponseBody String doRegister(HttpServletRequest request,HttpServletResponse response) {
		// 手机号
		String phone = request.getParameter("phone");
		// 密码
		String password = request.getParameter("password");
		// 验证码
		String code = request.getParameter("code");
		
		// 从session中取出code
		String ocode = "123456";// (String) request.getSession().getAttribute(phone+"code");
		// 验证码不正确
		if (!code.equals(ocode)) {
			return "code";
		}
		
		// 根据手机号获取用户
		HfUser user = userService.getUserByPhone(phone);
		// 用户已经存在
		if (user != null) {
			return "user";
		} else {
			user = new HfUser();
			user.setName("u"+phone);
			user.setMobile(phone);
			//user.setPassword(MD5.getMD5Str(password));
			try {
				user.setOpenId(WeixinUtil.getOpenId(request, response, 1));
			} catch (IOException e) {
				e.printStackTrace();
			}
			user.setCreateDate(new Date());
			// 存入数据库
			userService.saveUser(user);
			//保持登录状态 
			request.getSession().setAttribute("user", user);
			return "success";
		}
	}
	
	/**
     * 获取用户信息
     * @param request
     * @param response
     * @param kbn
     * @return
	 * @throws Exception 
     */
    private HfUser getUser(HttpServletRequest request, HttpServletResponse response, int kbn) throws Exception {
    	HfUser user = null;
    	
    	// 如果session中不存在user
    	if (request.getSession().getAttribute("user") == null) {
    		// 获取openID
    		String openId = WeixinUtil.getOpenId(request, response, kbn);
    		
    		if (StringUtils.isBlank(openId)) {
    			return null;
    		}
    		
    		// 根据openID去数据库中查询该用户
    		user = userService.getUserByOpenId(openId);
    		
    		// 设置session
    		request.getSession().setAttribute("user", user);
    	} else {
    		user = (HfUser) request.getSession().getAttribute("user");
    	}
    	
    	return user;
    }
    
}
