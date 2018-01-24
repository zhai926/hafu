package com.hafu.quartz;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.hafu.controller.HealthReportTempMessage;
import com.hafu.dto.UserDto;
import com.hafu.eban.entity.response.DayInfoResponse;
import com.hafu.eban.entity.response.DayInfoResponse_Location;
import com.hafu.eban.entity.response.DayInfoResponse_Mask1;
import com.hafu.eban.entity.response.DayRadarResponse;
import com.hafu.eban.entity.response.DayRadarResponse_data;
import com.hafu.eban.entity.response.VoiceRespnseList;
import com.hafu.eban.entity.response.VoiceResponse_voceList;
import com.hafu.eban.service.OtherService;
import com.hafu.eban.service.VoiceService;
import com.hafu.eban.utils.DateUtil;
import com.hafu.eban.utils.PublicUtils;
import com.hafu.entity.HfBehaviorRecord;
import com.hafu.entity.HfDevice;
import com.hafu.entity.HfHealthPoint;
import com.hafu.entity.HfTrack;
import com.hafu.entity.HfUser;
import com.hafu.entity.HfVoice;
import com.hafu.repository.jpa.HfBehaviorRecordDao;
import com.hafu.repository.jpa.HfDeviceDao;
import com.hafu.repository.jpa.HfHealthPointDao;
import com.hafu.repository.jpa.HfTrackDao;
import com.hafu.repository.jpa.HfUserDao;
import com.hafu.repository.jpa.HfVoiceDao;
import com.hafu.repository.mybatis.UserMybatisDao;
import com.hafu.service.BehaviorRecordService;
import com.hafu.service.HfAlertService;
import com.hafu.service.HfHealthPointService;
import com.hafu.service.HfTrackService;
import com.hafu.utils.HttpTookit;

@Component
public class EbanQuartzTimer {

    private static Logger logger = Logger.getLogger(EbanQuartzTimer.class);

    @Autowired
    private HfDeviceDao hfDeviceDao;
    @Autowired
    private HfAlertService hfAlertService;
    @Autowired
    private BehaviorRecordService behaviorRecordService;
    @Autowired
    private HfBehaviorRecordDao hfBehaviorRecordDao;
    @Autowired
    private HfTrackDao hfTrackDao;
    @Autowired
    private HfTrackService hfTrackService;
    @Autowired
    private HfHealthPointDao hfHealthPointDao;
    @Autowired
    private HfHealthPointService hfHealthPointService;
    @Autowired
    private HfVoiceDao hfVoiceDao;
    @Autowired
    private UserMybatisDao userMybatisDao;
    @Autowired
    private HfUserDao userDao;

    public static String temp_message_url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

    public static String access_token_url = "http://wx.hafu365.com/auth/getToken?appid=APPID";

    /**
     * 每隔1分钟执行一次
     */
    @Scheduled(cron = "0 */3 * * * ?")
    public void alert() {
        logger.debug("语音start : " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        try {
            Iterable<HfDevice> hfDevice = hfDeviceDao.findAll();
            Iterator<HfDevice> hfDevicelist = hfDevice.iterator();
            VoiceService voiceSerice = new VoiceService();
            while (hfDevicelist.hasNext()) {
                HfDevice hfDeviceBean = hfDevicelist.next();
                String code = hfDeviceBean.getCode();
                Long userId = hfDeviceBean.getUserId();
                if (StringUtils.isNotBlank(code)) {
                    VoiceRespnseList voiceRespnseList = voiceSerice.getVoice(code);
                    List<VoiceResponse_voceList> voice1 = voiceRespnseList.getVoice();
                    if (voice1 != null) {
                        for (int i = 0; i < voice1.size(); i++) {
                            VoiceResponse_voceList voiceResponse_voceList = voiceRespnseList.getVoice().get(i);
                            Date alertDate = DateUtil.TimeStamp2Date(voiceResponse_voceList.getDateline());
                            HfVoice hfVoice = new HfVoice();
                            hfVoice.setCode(code);
                            hfVoice.setCreateDate(new Date());
                            hfVoice.setUserId(userId);
                            hfVoice.setDeleteFlag(0);
                            hfVoice.setSource(1);
                            hfVoice.setReceiveDate(alertDate);
                            hfVoice.setVoiceUrl(voiceResponse_voceList.getPath());
                            hfVoice.setIsRead(0);
                            hfVoice.setDuration(100000);
                            hfVoice.setLength(0);
                            hfVoice.setVoiceSpeakerId(userId);
                            hfVoiceDao.save(hfVoice);
                            // 发送语音消息
                            sendHealthReportMessage(hfVoice);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.debug("语音Error : " + e.getMessage());
        }
        logger.debug("语音end : " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 每隔1分钟执行一次
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void behaviorRecord() {
        logger.debug("行动轨迹start : " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        try {
            Iterable<HfDevice> hfDevice = hfDeviceDao.findAll();
            Iterator<HfDevice> hfDevicelist = hfDevice.iterator();
            OtherService otherService = new OtherService();
            while (hfDevicelist.hasNext()) {
                HfDevice hfDeviceBean = hfDevicelist.next();
                String code = hfDeviceBean.getCode();
                Long userId = hfDeviceBean.getUserId();
                if (StringUtils.isNotBlank(code)) {
                    oneDevicebehaviorRecord(otherService,DateUtil.format(new Date(), "yyyy-MM-dd"),code,userId);
                }
            }
        } catch (Exception e) {
            logger.error("行动轨迹Error : " + e.getMessage(), e);
        }
        logger.debug("行动轨迹end : " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
    }

    public void oneDevicebehaviorRecord(OtherService otherService,String dateStr,String code,Long userId){

        try {
            DayInfoResponse dayInfoResponse = otherService.dayInfo(code, dateStr);
            logger.debug("日报信息 ： " + code + "=" + JSONObject.fromObject(dayInfoResponse));
            if (dayInfoResponse != null) {
                if (1 == dayInfoResponse.getSuccess()) {
                    String location = dayInfoResponse.getDay_info().getLocation();
                    //行动轨迹
                    if (location != null && location.length() > 0) {
                        logger.debug("行动轨迹 ： " + code + "=" + location);
                        List<DayInfoResponse_Location> locationList = PublicUtils.getDayInfoResponse_Location(location);
                        for (int i = 0; i < locationList.size(); i++) {
                            Date date = DateUtil.parse(dateStr + " " + locationList.get(i).getTime());
                            List<HfTrack> hfTrack1 = hfTrackService.findOneyTime(date, null, code);
                            if (hfTrack1.size() <= 0) {
                                HfTrack hfTrac = new HfTrack();
                                hfTrac.setCode(code);
                                hfTrac.setUserId(userId);
                                hfTrac.setLongitude(locationList.get(i).getLongitude());
                                hfTrac.setLatitude(locationList.get(i).getLatitude());
                                hfTrac.setPosition(locationList.get(i).getLocation());
                                hfTrac.setRadius(locationList.get(i).getAccuracy());
                                hfTrac.setCoordinate(locationList.get(i).getCoordinate());
                                hfTrac.setAlertDate(date);
                                hfTrac.setCreateDate(new Date());
                                hfTrac.setDeleteFlag(0);
                                hfTrac.setUpdateDate(new Date());
                                hfTrackDao.save(hfTrac);
                            } else {
                                HfTrack hfTrac = hfTrack1.get(hfTrack1.size() - 1);
                                hfTrac.setCode(code);
                                hfTrac.setUserId(userId);
                                hfTrac.setUpdateDate(new Date());
                                hfTrackDao.save(hfTrac);
                            }
                        }
                    } else {
                        logger.info(">>>>>>>>>>>>>>> Location is null !");
                    }

                    // 行为记录。目前问题 重复数据的问题
                    String mask1 = dayInfoResponse.getDay_info().getMask1();
                    logger.debug("行为记录 ： " + code + " " + mask1);
                    if (mask1 != null && mask1.length() > 0) {
                        List<DayInfoResponse_Mask1> list = PublicUtils.getDayInfoResponse_Mask1(mask1);
                        for (int i = 0; i < list.size(); i++) {
                            HfBehaviorRecord hfBehaviorRecord = new HfBehaviorRecord();
                            Date startTime = DateUtil.parse(dateStr + " " + list.get(i).getStartTime());
                            Date endTime = DateUtil.parse(dateStr + " " + list.get(i).getEndTime());

                            String behaviorType = list.get(i).getBehaviorType();
                            if (behaviorType.length() > 1) {
                                if (",".equals(behaviorType.substring(0, 1))) {
                                    behaviorType = behaviorType.substring(1, behaviorType.length());
                                }
                                if (",".equals(behaviorType.substring(behaviorType.length() - 1, behaviorType.length()))) {
                                    behaviorType = behaviorType.substring(0, behaviorType.length() - 1);
                                }
                            }
                            List<HfBehaviorRecord> hfBehaviorRecordBean = behaviorRecordService.findOneyTime(startTime, behaviorType, code);
                            if (hfBehaviorRecordBean.size() <= 0) {
                                hfBehaviorRecord.setBehaviorType(behaviorType);
                                hfBehaviorRecord.setCode(code);
                                hfBehaviorRecord.setUserId(userId);
                                hfBehaviorRecord.setCreateDate(new Date());
                                hfBehaviorRecord.setStartTime(startTime);
                                hfBehaviorRecord.setEndTime(endTime);
                                hfBehaviorRecord.setDeleteFlag(0);
                                hfBehaviorRecord.setUpdateDate(new Date());
                                hfBehaviorRecord.setBhType(list.get(i).getAttribute());
                                logger.debug(code + "  行动记录开始时间 : " + dateStr + " " + list.get(i).getStartTime());
                            } else {
                                hfBehaviorRecord = hfBehaviorRecordBean.get(hfBehaviorRecordBean.size() - 1);
                                hfBehaviorRecord.setCode(code);
                                hfBehaviorRecord.setUserId(userId);
                                hfBehaviorRecord.setBhType(list.get(i).getAttribute());
                                hfBehaviorRecord.setUpdateDate(new Date());
                            }
                            hfBehaviorRecordDao.save(hfBehaviorRecord);
                        }
                    } else {
                        logger.info(">>>>>>>>>>>> location mask is null !");
                    }
               }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    
    }
    /**
     * 每天凌晨1点执行一次
     */
    /* @Scheduled(cron="0 59 23 * * ?") */
    @Scheduled(cron = "0 */10 * * * ?")
    public void dayRadar() {
        logger.debug("健康数据start : " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        try {
            Iterable<HfDevice> hfDevice = hfDeviceDao.findAll();
            Iterator<HfDevice> hfDevicelist = hfDevice.iterator();
            OtherService otherService = new OtherService();
            while (hfDevicelist.hasNext()) {
                HfDevice hfDeviceBean = hfDevicelist.next();
                String code = hfDeviceBean.getCode();
                Long userId = hfDeviceBean.getUserId();
                String time = DateUtil.format(new Date(), "yyyy-MM-dd");
                if (StringUtils.isNotBlank(code)) {
                    DayRadarResponse dayRadarResponse = otherService.dayRadar(code, time);
                    logger.debug("健康 ： " + code + " " + JSONObject.fromObject(dayRadarResponse));
                    if (dayRadarResponse != null) {

                        String time1 = DateUtil.format(DateUtil.getYesterdayDate(new Date()), "yyyy-MM-dd");
                        DayInfoResponse dayInfoResponse1 = otherService.dayInfo(code, time1);
                        if (dayInfoResponse1 != null) {
                            HfHealthPoint hfHealthPoint1 = hfHealthPointService.findOne(code, DateUtil.parse(time1 + " 00:00:00"), DateUtil.parse(time1 + " 23:59:59"));
                            if (hfHealthPoint1 != null) {
                                hfHealthPoint1.setPoint(toInt(dayInfoResponse1.getDay_info().getPoint_today()));
                                hfHealthPoint1.setActivity(toInt(dayInfoResponse1.getDay_info().getValid_wear()) / 60);// 佩戴有效时长
                                hfHealthPointService.save(hfHealthPoint1);
                            }
                        }

                        // DayRadarResponse_rest rest =
                        // dayRadarResponse.getRest();
                        HfHealthPoint hfHealthPoint = hfHealthPointService.findOne(code, DateUtil.parse(time + " 00:00:00"), DateUtil.parse(time + " 23:59:59"));
                        DayRadarResponse_data data = dayRadarResponse.getData();
                        if (hfHealthPoint == null) {
                            hfHealthPoint = new HfHealthPoint();
                            hfHealthPoint.setCreateDate(new Date());
                        }
                        hfHealthPoint.setCode(code);
                        hfHealthPoint.setUserId(userId);
                        hfHealthPoint.setStepCount(toInt(data.getStep_sum()));
                        hfHealthPoint.setStepLength(toDouble(0 + ""));
                        hfHealthPoint.setStepSpeed(toInt(data.getStep_freq()) / 60);
                        hfHealthPoint.setStaticCount(toInt(data.getSit_long()));
                        hfHealthPoint.setLiveness(toDouble(data.getLiveness()));
                        hfHealthPoint.setFatigue(0);
                        hfHealthPoint.setRest(toInt(data.getSleep_time()) / 60);// 暂时存储睡眠时长
                        hfHealthPoint.setActivity(0);
                        hfHealthPoint.setPoint(0);
                        hfHealthPoint.setDateType(0);
                        hfHealthPoint.setUpdateDate(new Date());
                        hfHealthPoint.setDeleteFlag(0);
                        hfHealthPointService.save(hfHealthPoint);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("健康数据Error : " + e.getMessage(),e);
        }
        logger.debug("健康数据end : " + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
    }

    public void sendHealthReportMessage(HfVoice hfVoice) {
        String requestUrl = access_token_url.replace("APPID", "wxae259d364bee5221");
        String jsonObject = HttpTookit.doHttpRequestString(requestUrl, "GET", null);
        String url = temp_message_url.replace("ACCESS_TOKEN", jsonObject);
        List<UserDto> list = userMybatisDao.getMyConcernUser(hfVoice.getUserId());
        HfUser hf = userDao.findOne(hfVoice.getUserId());
        logger.info("发送语音消息. token : " + jsonObject + " list Size :" + list.size());
        if (!CollectionUtils.isEmpty(list)) {
            boolean flag = true;
            for (UserDto u : list) {
                HealthReportTempMessage m = new HealthReportTempMessage();
                m.setTouser(u.getOpenId());
                HealthReportTempMessage.Data d = m.new Data();
                String nickName = null;
                if (StringUtils.isNotEmpty(u.getNickName())) {
                    nickName = u.getNickName();
                } else {
                    nickName = hf.getRealName();
                }
                d.setFirst(d.new First("您的家人  " + nickName + " 发送了一条语音消息"));
                d.setKeyword1(d.new Keyword1(""));
                d.setKeyword2(d.new Keyword2(DateUtil.date2String(hfVoice.getCreateDate(), "yyyy年MM月dd日 HH:mm")));
                d.setRemark(d.new Remark("可点击下方“福心关爱”进入查看更多》"));
                m.setData(d);
                JSONObject o = JSONObject.fromObject(m);
                JSONObject outputStr = JSONObject.fromObject(o.toString());
                JSONObject res = HttpTookit.doHttpRequest(url, "POST", outputStr.toString());
                logger.info("发送" + u.getOpenId() + "语音消息内容 ：" + outputStr.toString());
                if (!res.getString("errcode").equals("0")) {
                    logger.error("发送" + u.getOpenId() + "语音消息失败：" + res.toString());
                    flag = false;
                }
            }
            if (!flag) {
                logger.error("发送语音消息失败：出现错误，请查看以上日志");
            }
        }
    }

    public Integer toInt(String param) {
        int p = 0;
        if (StringUtils.isNotBlank(param)) {
            p = Integer.parseInt(param);
        }
        return p;
    }

    public Double toDouble(String param) {
        double p = 0;
        if (StringUtils.isNotBlank(param)) {
            p = Double.parseDouble(param);
        }
        return p;
    }
}