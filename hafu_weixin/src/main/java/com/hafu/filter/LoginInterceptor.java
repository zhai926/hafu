package com.hafu.filter;

import com.hafu.entity.HfUser;
import com.hafu.service.IndexService;
import com.hafu.service.UserService;
import com.hafu.weixin.util.WeixinUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
/**
 * 登陆 拦截器 (默认进入公共号必须静默登陆)
 * @author zc
 *
 */
public class LoginInterceptor  extends HandlerInterceptorAdapter{
	
	@Autowired
	private UserService userService;
	@Autowired
	private IndexService indexService;
	private static Logger log = LoggerFactory.getLogger(LoginInterceptor.class);

	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
		    throws Exception {
		try{
			if(handler.getClass().isAssignableFrom(HandlerMethod.class)){//如果是spring mvc 方法
				HfUser user = null;
				String kbn = request.getParameter("kbn");
		    	if (request.getSession().getAttribute("LoginUser") == null) { //未登录直接获取openid
		    		// 获取openID
		    		//String openId = "oA3IusyKpZCr2SGDuze6DKMPNWLg";//暂时WeixinUtil.getOpenId(request, response, StringUtils.isNotEmpty(kbn)?Integer.parseInt(kbn):1);
		    		String openId = WeixinUtil.getOpenId(request, response, StringUtils.isNotEmpty(kbn)?Integer.parseInt(kbn):1);
						System.out.println("获取的 openId 是:------------------------"+openId);
						if (StringUtils.isBlank(openId)) {//无法获取openId跳入错误页面
		    			return false;
		    		}
		    		// 根据openID去数据库中查询该用户
		    		user = userService.getUserByOpenId(openId); //使用微信认证直接登陆
		    		if(user==null){//用户不存在，静默登陆
		    			JSONObject uo = WeixinUtil.getWeixinUserInfo(openId); //获取微信用户信息
		    			if(uo==null){
		    				request.setAttribute("msg","系统错误！");
			    			response.sendRedirect("/index/error");
			    			return false;
		    			}else{		    					
		    					user = new HfUser();
		    			    	user.setAvater(cts(uo.get("headimgurl")));
		    			    	user.setCity(cts(uo.get("city")));
		    			    	user.setCountry(cts(uo.get("country")));
		    			    	user.setCreateDate(new Date());
		    			    	user.setDeleteFlag(0);
		    			    	Object gen = uo.get("sex");
		    			    	String gender = "";
		    			    	if(gen==null || Integer.parseInt(gen.toString())==0){
		    			    		gender ="";
		    			    	}else if( Integer.parseInt(gen.toString())==1){
		    			    		gender ="男";
		    			    	}else{
		    			    		gender ="女";
		    			    	}
		    			    	user.setGender(gender);
		    			    	//user.setNickName(cts(uo.get("nickname")));
		    			    	user.setNickName("");
		    			    	user.setOpenId(cts(uo.get("openid")));
		    			    	user.setProvince(cts(uo.get("province")));
			    				HfUser loginUser = userService.newUser(user);
			    				request.getSession().setAttribute("LoginUser", loginUser);
			    				return true;    			    	
		    			}
		    		}else{
		    			// 设置session
			    		request.getSession().setAttribute("LoginUser", user);
			    		return true;
		    		}
		    		
		    	} else {
		    		//已登录仍需判断是否绑定过设备
		    		return true;
		    	}
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
			request.setAttribute("msg","系统错误！");
			response.sendRedirect("/index/error");
			return false;
		}
		
	}
	
	
	
	public static String cts(Object o){
		if(o==null){
			return null;
		}
		return o.toString();
	}
}
