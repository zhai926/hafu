package com.hafu.controller;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hafu.dto.UserDto;
import com.hafu.eban.entity.response.DayWeatherResponse;
import com.hafu.eban.entity.response.VoiceRespnse;
import com.hafu.eban.service.OtherService;
import com.hafu.eban.service.VoiceService;
import com.hafu.eban.utils.DateUtil;
import com.hafu.entity.HfEncouragement;
import com.hafu.entity.HfLove;
import com.hafu.entity.HfUser;
import com.hafu.entity.HfVoice;
import com.hafu.service.HfLoveService;
import com.hafu.service.HfVoiceService;
import com.hafu.service.UserService;
import com.hafu.timer.TimerUtilCollection;
import com.hafu.utils.AjaxResutObject;
import com.hafu.utils.HttpUtils;
import com.hafu.utils.StaticVarible;
import com.hafu.utils.UpLoadFile;

/***
 * 语音
 * 
 * @author chang
 *
 */
@Controller
@RequestMapping("/voice")
public class VoiceController {
	@Autowired
	private UserService userService;

    @Resource
    private HfVoiceService hfVoiceService;
    @Resource
    private HfLoveService hfLoveService;

    private static final String urlStr = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=TOKEN&type=voice";
    private static String fileName = "";

    private static Logger log = LoggerFactory.getLogger(VoiceController.class);

    @RequestMapping("voiceTest")
    public void voiceTest(HttpServletRequest req) {
     /*
        String serverId = uploadVoice(
        "http://www.z-smartlink.com/download/voice/2017-01/100100694/447957928.amr"
         );
         req.setAttribute("serverId", serverId); 
         return "/voice/voiceTest";*/
    }

    /**
     * 语音互动页面
     * 
     * @param req
     * @return
     */
    @RequestMapping("voice")
    public String voice(HttpServletRequest req,HttpSession session) {
    	log.debug("****进入语音页面");
        UserDto cur = (UserDto) req.getSession().getAttribute("CurrentUser");
        Long userId = cur.getId();//获取被关注人编号
        
        ServletContext servletContext= session.getServletContext(); // getSevletContext() 应用于整个 web app
		String remoteImage = servletContext.getInitParameter("remoteImage");//读取文件的路劲 
		
        HfUser lg = (HfUser) req.getSession().getAttribute("LoginUser");//手机用户
		Long concernuserid = lg.getId();  //获取手机用户的编号
		String Avater=lg.getAvater();//获取手机用户的头像地址
		if(Avater == null || Avater.equals("")){
			req.setAttribute("Avater", null);//存储手机用户的头像
		}else{
			req.setAttribute("Avater", remoteImage +"/" + Avater);//存储手机用户的头像
		}
		log.debug("关注人(手机)ID***"+concernuserid);
		log.debug("关注人(手机)的头像地址***"+Avater);
        
        List<HfVoice> voiceList = hfVoiceService.getVoiceList(userId, cur.getCode());
        List<HfUser> speakerList = new ArrayList<HfUser>();
        int notReadNum = hfVoiceService.getNotReadNum(cur.getCode(), userId);//获取未读的条数
        System.out.println("未读条数： " + notReadNum);
        for (int i = 0; i < voiceList.size(); i++) {
            HfVoice hfVoice = voiceList.get(i);
            Date date = hfVoice.getCreateDate();
            hfVoice.setTime(DateUtil.format(date, "MM-dd HH:mm"));
            if (hfVoice.getSource() == 1) {
                String serverId = hfVoice.getServerId();
                log.info("voice0 = " + hfVoice.getVoiceUrl());
                if (StringUtils.isBlank(serverId)) {
                    serverId = uploadVoice(hfVoice.getVoiceUrl());
                    hfVoice.setServerId(serverId);
                }
                log.info("voice : " + serverId);
                if (hfVoice.getDuration() == 100000) {
                    hfVoice.setDuration(getAmrDuration(new File(UpLoadFile.LOCALVOICEPATH + fileName)));
                }
            } else {
                if (hfVoice.getDuration() == 100000) {
                    hfVoice.setDuration(getAmrDuration(new File(hfVoice.getVoicePath())));
                }
            }
            if (hfVoice.getLength() == 0) {
                hfVoice.setLength((hfVoice.getDuration() / 2) + 3);
            }
            hfVoiceService.update(hfVoice);
            //用户speakerId为null问题
            HfUser voiceSpeaker = null;
            if(hfVoice.getVoiceSpeakerId() != null){
            	 voiceSpeaker = userService.getUser(hfVoice.getVoiceSpeakerId());
            }else{
            	voiceSpeaker = new HfUser();
            }
            speakerList.add(voiceSpeaker);
            
        }
        req.setAttribute("speakerList", speakerList);
        //req.setAttribute("Avater", Avater);//存储手机用户的头像
        req.setAttribute("voiceList", voiceList);
        log.debug("****进入语音返回页面");
        return "/voice/voice";
    }

    /**
     * 根据语音id 手机用户id等修改语音读取的状态
     * */
    @RequestMapping("updateRead")
    @ResponseBody
    public String updateRead(HttpServletRequest req, Long id) {
    	HfUser lg = (HfUser) req.getSession().getAttribute("LoginUser");//手机用户
		Long concernuserid = lg.getId();
		log.debug("关注人(手机)ID***"+concernuserid);		
        AjaxResutObject ro = new AjaxResutObject();
        hfVoiceService.updateReadStatus(id,concernuserid);
        ro.setStatus(StaticVarible.SUCCESS);
        ro.setMsg("操作成功");
        return JSONObject.fromObject(ro).toString();
    }

    
    
    @RequestMapping("getVoiceNum")
    @ResponseBody
    public String getVoiceNum(HttpServletRequest req) {
    	log.debug("****进入获取未读语音条数");
        UserDto cur = (UserDto) req.getSession().getAttribute("CurrentUser");
        Long userId = cur.getId();
        AjaxResutObject ro = new AjaxResutObject();
        try {
            int notReadNum = hfVoiceService.getNotReadNum(cur.getCode(), userId);
            ro.setStatus(StaticVarible.SUCCESS);
            ro.setOb(notReadNum);
        } catch (Exception e) {
            ro.setStatus(StaticVarible.ERROR);
            ro.setMsg("操作失败");
        }
        log.debug("****获取未读语音条数："+JSONObject.fromObject(ro).toString());
        return JSONObject.fromObject(ro).toString();
      
    }

    /**
     * 保存语音互动消息
     */
    @RequestMapping("saveVoice")
    @ResponseBody
    public String saveInfo(HttpServletRequest req, String voiceMsg, String serverId, String type) {
        VoiceService voice = new VoiceService();
        String fileName = "";
        String voiceUrl = "";
        String filePath = "";
        UserDto cur = (UserDto) req.getSession().getAttribute("CurrentUser");//E用户
        Long userId = cur.getId();
        log.debug("E用户(设备)ID***"+userId);
    	HfUser lg = (HfUser) req.getSession().getAttribute("LoginUser");//手机用户
		Long voiceSpeakerId = lg.getId();
		log.debug("关注人(手机/说话人)ID***"+voiceSpeakerId);
        log.info("saveVoice : " + voiceMsg + "   " + serverId);
        if (!"none".equals(serverId)) {
            try {
                fileName = HttpUtils.downloadFile(UpLoadFile.LOCALVOICEPATH, "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=" + TimerUtilCollection.accessToken.getToken()
                        + "&media_id=" + serverId + "");
                log.info(" fileName = " + fileName);
                filePath = UpLoadFile.LOCALVOICEPATH + fileName;
                voiceUrl = getFtpUrl(fileName, filePath);
            } catch (IOException e) {
                log.error(e.getMessage(),e);
            }
        }
        log.info("saveVoice : " + filePath + "   voiceUrl=" + voiceUrl + "   fileName = " + fileName);
        Integer duration = getAmrDuration(new File(filePath));
        AjaxResutObject ro = new AjaxResutObject();
        boolean flag = hfVoiceService.saveVoice(serverId, voiceMsg, userId,voiceSpeakerId, cur.getCode(), filePath, duration, voiceUrl);
        if (flag) {
            VoiceRespnse voiceRespnse = null;
            if ("1".equals(type)) {
                voiceRespnse = voice.vmessage(cur.getCode(), voiceMsg);
            } else {
                voiceRespnse = voice.voice(cur.getCode(), filePath);
            }

            log.info("语音发送结果 ： " + voiceRespnse.getVoice() + "  type:" + type + "   path = " + filePath + "  result:" + voiceRespnse.getSuccess());
            ro.setStatus(StaticVarible.SUCCESS);
            if (!"none".equals(serverId)) {
                ro.setMsg(duration + "");
                ro.setOb(UUID.randomUUID() + "");
            }
        } else {
            ro.setStatus(StaticVarible.ERROR);
            ro.setMsg("操作失败");
        }
     return JSONObject.fromObject(ro).toString();
     
    }

    
    
    /**
     * 保存语音互动消息
     */
    @RequestMapping("/sentVoice")
    @ResponseBody
    public String sentVoice(HttpServletRequest req, String voiceMsg, String serverId, String type) {
        VoiceService voice = new VoiceService();
        String fileName = "";
        String voiceUrl = "";
        String filePath = "";
        UserDto cur = (UserDto) req.getSession().getAttribute("CurrentUser");
        Long userId = cur.getId();
        log.debug("E用户(设备)ID***"+userId);
    	HfUser lg = (HfUser) req.getSession().getAttribute("LoginUser");//手机用户
		Long voiceSpeakerId = lg.getId();
		log.debug("关注人(手机)ID***"+voiceSpeakerId);
        
        log.info("saveVoice : " + voiceMsg + "   " + serverId);
        if (!"none".equals(serverId)) {
            try {
                fileName = HttpUtils.downloadFile(UpLoadFile.LOCALVOICEPATH, "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=" + TimerUtilCollection.accessToken.getToken()
                        + "&media_id=" + serverId + "");
                log.info(" fileName = " + fileName);
                filePath = UpLoadFile.LOCALVOICEPATH + fileName;
                voiceUrl = getFtpUrl(fileName, filePath);
            } catch (IOException e) {
                log.error(e.getMessage(),e);
            }
        }
        log.info("saveVoice : " + filePath + "   voiceUrl=" + voiceUrl + "   fileName = " + fileName);
        Integer duration = getAmrDuration(new File(filePath));
        AjaxResutObject ro = new AjaxResutObject();
        boolean flag = hfVoiceService.saveVoice(serverId, voiceMsg, userId,voiceSpeakerId, cur.getCode(), filePath, duration, voiceUrl);
        if (flag) {
            VoiceRespnse voiceRespnse = null;
            if ("1".equals(type)) {
                voiceRespnse = voice.vmessage(cur.getCode(), voiceMsg);
            } else {
                voiceRespnse = voice.voice(cur.getCode(), filePath);
            }

            log.info("语音发送结果 ： " + voiceRespnse.getVoice() + "  type:" + type + "   path = " + filePath + "  result:" + voiceRespnse.getSuccess());
            ro.setStatus(StaticVarible.SUCCESS);
            if (!"none".equals(serverId)) {
                ro.setMsg(duration + "");
                ro.setOb(UUID.randomUUID() + "");
            }
        } else {
            ro.setStatus(StaticVarible.ERROR);
            ro.setMsg("操作失败");
        }
        return JSONObject.fromObject(ro).toString();
     
    }
    
    public static void main(String[] args) {
        try {
            // HttpUtils.downloadFile("d://demo/2.amr","http://pic1.hafu365.com/voicePath/M3jRSUXscrZtUD-xQP5MCekmxJZKlrcWIphq1CKY5DHR7WhjNsomICIgDwtxk-31.amr");
            System.out.println(getAmrDuration(new File("d://2.amr")));
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
    }

    /***
     * 上传微信服务器获得serverId
     * 
     * @param voiceUrl
     * @return
     */
    public static String uploadVoice(String voiceUrl) {
        String serverId = "";
        fileName = UUID.randomUUID().toString() + "." + voiceUrl.substring(voiceUrl.lastIndexOf(".") + 1, voiceUrl.length());
        try {
            String filePath = UpLoadFile.LOCALVOICEPATH + fileName;
            HttpUtils.downloadFile(filePath, voiceUrl);
            String urlReal = urlStr.replace("TOKEN", TimerUtilCollection.accessToken.getToken());
            log.debug(">>>>>>>>>>>>>>>>> upload voice taken & url is :" + urlReal);
            Map<String, String> textMap = new HashMap<String, String>();
            textMap.put("media", fileName);
            Map<String, String> fileMap = new HashMap<String, String>();
            fileMap.put("userfile", filePath);
            String ret = HttpUtils.formUpload(urlReal, textMap, fileMap);
            log.debug(">>>>>>>>>>>>>>>>>> upload result json : " + ret);
            JSONObject object = JSONObject.fromObject(ret);
            serverId = (String) object.get("media_id");
            getFtpUrl(fileName, filePath);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
        return serverId;
    }

    /***
     * 上传到ftp文件服务器
     * 
     * @param fileName
     * @param filePath
     * @return
     */
    public static String getFtpUrl(String fileName, String filePath) {
        return UpLoadFile.upLoadVoiceFile(filePath, fileName);
    }

    /***
     * 获取语音时长
     * 
     * @param file
     * @return
     */
    public static Integer getAmrDuration(File file) {
        int a = 0;
        long duration = -1;
        int[] packedSize = { 12, 13, 15, 17, 19, 20, 26, 31, 5, 0, 0, 0, 0, 0, 0, 0 };
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
            long length = file.length();// 文件的长度
            int pos = 6;// 设置初始位置
            int frameCount = 0;// 初始帧数
            int packedPos = -1;

            byte[] datas = new byte[1];// 初始数据值
            while (pos <= length) {
                randomAccessFile.seek(pos);
                if (randomAccessFile.read(datas, 0, 1) != 1) {
                    duration = length > 0 ? ((length - 6) / 650) : 0;
                    break;
                }
                packedPos = (datas[0] >> 3) & 0x0F;
                pos += packedSize[packedPos] + 1;
                frameCount++;
            }
            duration += frameCount * 20;// 帧数*20
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
                }
            }
        }
        a = (int) Math.rint(duration / 1000);
        if (a == 0) {
            a = 1;
        }
        return a;
    }
    

  
	  @RequestMapping("sendWord")
	  @ResponseBody
	public String sendWord(@RequestParam(value="LoveType",required=false) String LoveType,
			HttpServletRequest req){
    	AjaxResutObject ob = new AjaxResutObject();
    	int Lovetype=Integer.parseInt(LoveType);
        HfLove hfLove = hfLoveService.getlove(Lovetype);//根据关爱类型获取关爱对象
    	//判断
       if (hfLove !=null) {
    		String content=hfLove.getContent();//获取到当前的内容
    		ob.setStatus(StaticVarible.SUCCESS);
    		ob.setOb(content);
		}else{
			ob.setStatus(StaticVarible.ERROR);
			ob.setMsg("暂无记录");
		}
    	log.debug("打印的数据是:-------------"+JSONObject.fromObject(ob).toString());
    	return JSONObject.fromObject(ob).toString();
	}
    
    
    
	
    
  
}
