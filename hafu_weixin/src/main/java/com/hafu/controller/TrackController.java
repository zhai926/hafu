package com.hafu.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hafu.dto.UserDto;
import com.hafu.entity.HfTrack;
import com.hafu.service.HfTrackService;
import com.hafu.service.UserService;

@Controller
@RequestMapping(value="track")
public class TrackController {
	private ResourceBundle bundle=PropertyResourceBundle.getBundle("smsconfig");
	private String defaultLng = bundle.getString("defaultLng");
	private String defaultLat = bundle.getString("defaultLat");
	@Resource
	private HfTrackService trackService;
	
	@Resource
	private UserService userService;
	
	
	/**
	 * 定位
	 * @param session
	 * @param model
	 * @param req
	 * @param address
	 * @return
	 */
	@RequestMapping(value="position")
	public String position(HttpSession session,
			Model model,HttpServletRequest req){
		UserDto current = (UserDto) req.getSession().getAttribute("CurrentUser"); 
		String code=current.getCode();
		if(current.getId()!=null){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			Date date=new Date();
			try {
				date=sdf.parse(sdf.format(date));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<HfTrack> trackList=trackService.getOneDayTrack(current.getId(), code,date);
			model.addAttribute("trackList", JSONArray.fromObject(trackList));
		}
		HfTrack last=trackService.lastTrack(code);
		model.addAttribute("last", last);
		return "/track/position";
	}
	/**
	 * 定位
	 * @param session
	 * @param model
	 * @param req
	 * @param address
	 * @return
	 */
	@RequestMapping(value="userPosition")
	public String userPosition(HttpSession session,
			Model model,HttpServletRequest req){
		UserDto current = (UserDto) req.getSession().getAttribute("CurrentUser"); 
		String code=current.getCode();
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(current.getId()!=null){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			Date date=new Date();
			try {
				date=sdf.parse(sdf.format(date));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<HfTrack> trackList=trackService.getOneDayTrack(current.getId(), code,date);
			if(trackList==null || trackList.size() == 0){
				trackList = new ArrayList<HfTrack>();
				HfTrack last = new HfTrack();
				last.setLatitude(defaultLat);
				last.setLongitude(defaultLng);
				try {
					last.setAlertDate(sdf2.parse(sdf2.format(new Date())));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				trackList.add(last);
			}
			model.addAttribute("trackList", JSONArray.fromObject(trackList));
		}
		HfTrack last=trackService.lastTrack(code);
		//设置默认经纬度
				if(last == null){
					last = new HfTrack();
					last.setLatitude(defaultLat);
					last.setLongitude(defaultLng);
					try {
						last.setAlertDate(sdf2.parse(sdf2.format(new Date())));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		model.addAttribute("last", last);
		return "/track/userPosition";
	}
	/*@RequestMapping(value="userPosition")
	public String userPosition(HttpSession session,
			Model model,HttpServletRequest req){
		UserDto current = (UserDto) req.getSession().getAttribute("CurrentUser"); 
		String code=current.getCode();
		if(current.getId()!=null){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			Date date=new Date();
			try {
				date=sdf.parse(sdf.format(date));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<HfTrack> trackList=trackService.getOneDayTrack(current.getId(), code,date);
			model.addAttribute("trackList", JSONArray.fromObject(trackList));
		}
		HfTrack last=trackService.lastTrack(code);
		//设置默认经纬度
		if(last == null){
			last = new HfTrack();
			last.setLatitude(defaultLat);
			last.setLongitude(defaultLng);
		}
		model.addAttribute("last", last);
		return "/track/userPosition";
	}*/
	
	
	/**
	 * 
	 * @param session
	 * @param model
	 * @param req
	 * @param address
	 * @param dateStr
	 * @return
	 */
	@RequestMapping(value="track")
	public String track(HttpSession session,
			Model model,HttpServletRequest req,
			@RequestParam(value="dateStr",required=false)String dateStr){
		UserDto current = (UserDto) req.getSession().getAttribute("CurrentUser");
		String code=current.getCode();
		Date date=new Date();
		if(current.getId()!=null){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			if(StringUtils.isNotBlank(dateStr)){ //字符串是否不为空且长度不为0且不由空白符(whitespace)构成
				try {
					date=sdf.parse(dateStr);
				} catch (ParseException e) { 
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				try {
					date=sdf.parse(sdf.format(date));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			List<HfTrack> trackList=trackService.getOneDayTrack(current.getId(), code,date);
			model.addAttribute("trackList", JSONArray.fromObject(trackList));
			model.addAttribute("dateStr", sdf.format(date));
		}
		HfTrack last=trackService.lastTrack(code,date);
		model.addAttribute("last", last);
		return "/track/track";
	}
	
	public void getTimeImage(HttpServletResponse response){
		int WIDTH=52,HEIGHT=23;
		// 1. 在 内存 中 构建 出 一张 图片 ,
        BufferedImage bf = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        //此处 要 墙转, 发现 Graphics 的 方法 不够 用, 但是 实际上返回的 是 一个 Graphics2D , 而 Graphics2D 里 有 旋转 的方法 
        Graphics2D graphics = (Graphics2D) bf.getGraphics();
        // 2.填充矩形 颜色 
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);  
        //3. 画 一个 蓝色 的 边框 
        graphics.setColor(Color.BLUE);
        // 设置边框 实际上是 画  矩形 
        graphics.drawRect(0, 0, WIDTH-1, HEIGHT-1);
        // 4.  画 一些 字符 到 这个图片上 
        graphics.setColor(Color.RED);
        graphics.setFont(new Font("宋体",Font.BOLD,15));
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
        graphics.drawString(sdf.format(new Date()), 4, 16);
        graphics.setColor(new Color(255, 0, 0, 128));
        graphics.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);  
        graphics.drawRect(1, 1, WIDTH - 1, HEIGHT - 1);  
        graphics.drawRect(0, 0, WIDTH - 2, HEIGHT - 2);  
        
        // 释放 资源 
        graphics.dispose();  // 这个 方法 调用之后,  graphics 对象 就不可以再 用了
        
        
//         将这个 图片  能够 与 response.getOutputStream相关联, 也就是 写到 getOutputStream() 里去 .
        // png, jpg, gif , bmp
        try {
			ImageIO.write(bf, "jpg", response.getOutputStream());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
