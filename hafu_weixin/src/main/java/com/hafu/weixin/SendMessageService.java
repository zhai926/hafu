package com.hafu.weixin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hafu.modules.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.hafu.controller.SendMessageController;
import com.hafu.dto.UserDto;
import com.hafu.entity.HfAlert;
import com.hafu.entity.HfConcernUser;
import com.hafu.entity.HfHealthPoint;
import com.hafu.entity.HfUser;
import com.hafu.entity.HfUserAlert;
import com.hafu.repository.jpa.HfAlterDao;
import com.hafu.repository.jpa.HfConcernUserDao;
import com.hafu.repository.jpa.HfHealthPointDao;
import com.hafu.repository.jpa.HfUserAlertDao;
import com.hafu.repository.jpa.HfUserDao;
import com.hafu.repository.mybatis.UserMybatisDao;
import com.hafu.timer.TimerUtilCollection;
import com.hafu.weixin.message.resquest.AlertTempMessage;
import com.hafu.weixin.message.resquest.HealthReportTempMessage;
import com.hafu.weixin.util.WeixinUtil;

@Service
public class SendMessageService {
	
	private static Logger log = LoggerFactory.getLogger(SendMessageController.class);
	public static String temp_message_url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
	
	@Autowired
	private HfAlterDao alterDao;
	@Autowired
	private UserMybatisDao userMybatisDao;
	@Autowired
	private HfUserAlertDao userAlertDao;
	@Autowired
	private HfConcernUserDao concernUserDao;
	@Autowired
	private HfUserDao userDao;
	@Autowired
	private HfHealthPointDao healthPointDao;
	
	public boolean sendAlertMessage(String alertId){
		HfAlert alert = alterDao.findOne(Long.parseLong(alertId));
		if(alert!=null){
			String url = temp_message_url.replace("ACCESS_TOKEN", TimerUtilCollection.accessToken.getToken());
			//code 查找微信关注人和对应持有人昵称
			List<UserDto> list = userMybatisDao.getWeixinUserConcernUser(alert.getCode());
			HfUser hf = userDao.findOne(alert.getUserId());
			if(!CollectionUtils.isEmpty(list)){
				boolean flag = true;
				for(UserDto u : list){
					HfUserAlert ua = new HfUserAlert();
					ua.setUserId(alert.getUserId());
					ua.setConcernUserId(u.getId());
					ua.setCode(alert.getCode());
					ua.setAlertId(alert.getId());
					ua.setReadStatus(0);
					ua.setCreateDate(new Date());
					userAlertDao.save(ua); //保存关系
					
					AlertTempMessage m = new AlertTempMessage();
					m.setTouser(u.getOpenId());
					AlertTempMessage.Data d = m.new Data();
					HfConcernUser cu = concernUserDao.findOneByUserIdAndConcernUserId(alert.getUserId(), u.getId());
					String nickName = null;
					if(cu!=null && StringUtils.isNotEmpty(cu.getNickName())){
						nickName = cu.getNickName();
					}else{
						nickName = hf.getRealName();
					}
					d.setFirst(d.new First("您的家人"+nickName+" 报警,请尽快确认！"));
					d.setKeyword1(d.new Keyword1(alert.getCode()));
					d.setKeyword2(d.new Keyword2(DateUtil.date2String(alert.getAlertDate(),"yyyy年MM月dd日 HH:mm")));
					d.setKeyword3(d.new Keyword3(alert.getAlertType()==1?"自动报警":"手动报警"));
					d.setKeyword4(d.new Keyword4(alert.getPosition()));
					d.setRemark(d.new Remark("请您尽快与家人联系！"));
					m.setData(d);
					JSONObject o = JSONObject.fromObject(m);
					JSONObject outputStr = JSONObject.fromObject(o.toString());
					JSONObject res = WeixinUtil.doHttpRequest(url, "POST", outputStr.toString());
					if(!res.getString("errcode").equals("0")){
						log.error("发送报警消息失败："+res.toString());
						flag = false;
						//return false;
					}
				}
				if(!flag){
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 发送昨日健康报告
	 */
	@Scheduled(cron="0 0 10 * * ?")
	public void sendHealthReportMessage(){
		List<HfHealthPoint> hlist = healthPointDao.findAll(new Specification<HfHealthPoint>() {
			@Override
			public Predicate toPredicate(Root<HfHealthPoint> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal(root.get("deleteFlag"), 0));
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				list.add(cb.lessThan(root.<Date> get("createDate"),calendar.getTime()));
				Calendar calendar2 = Calendar.getInstance();
				calendar2.setTime(calendar.getTime());
				calendar2.add(Calendar.DATE, -1);
				list.add(cb.greaterThanOrEqualTo(root.<Date> get("createDate"),calendar2.getTime()));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		});
		if(hlist!=null  && hlist.size()!=0){
			for(HfHealthPoint point : hlist){
				if(point!=null){
					String url = temp_message_url.replace("ACCESS_TOKEN", TimerUtilCollection.accessToken.getToken());
					//code 查找微信关注人和对应持有人昵称
					List<UserDto> list = userMybatisDao.getMyConcernUser(point.getUserId());
					HfUser hf = userDao.findOne(point.getUserId());
					if(!CollectionUtils.isEmpty(list)){
						boolean flag = true;
						for(UserDto u : list){
							HealthReportTempMessage m = new HealthReportTempMessage();
							m.setTouser(u.getOpenId());
							HealthReportTempMessage.Data d = m.new Data();
							String nickName = null;
							if(StringUtils.isNotEmpty(u.getNickName())){
								nickName = u.getNickName();
							}else{
								nickName = hf.getRealName();
							}
							d.setFirst(d.new First("您的家人  "+nickName+" 昨日健康报告"));
							d.setKeyword1(d.new Keyword1(DateUtil.date2String(point.getCreateDate(),"yyyy年MM月dd日 HH:mm")));
							d.setKeyword2(d.new Keyword2("健康评分"+point.getPoint()+"分"));
							d.setRemark(d.new Remark("可点击下方“福心关爱”进入查看更多》"));
							m.setData(d);
							JSONObject o = JSONObject.fromObject(m);
							JSONObject outputStr = JSONObject.fromObject(o.toString());
							JSONObject res = WeixinUtil.doHttpRequest(url, "POST", outputStr.toString());
							if(!res.getString("errcode").equals("0")){
								log.error("发送"+u.getOpenId()+"健康报告消息失败："+res.toString());
								flag = false;
								//return false;
							}
						}
						if(!flag){
							log.error("发送健康报告消息失败：出现错误，请查看以上日志");
						}
					}
				}
			}
		}
	}
	
	/*public static void main(String[] args){
		AlertTempMessage m = new AlertTempMessage();
		m.setTouser("oA3IusxW3V4lO_-Qb8n1pPlMXcc0");
		AlertTempMessage.Data d = m.new Data();
		d.setFirst(d.new First("恭喜你购买成功"));
		d.setKeyword1(d.new Keyword1("巧克力"));
		d.setKeyword2(d.new Keyword2("20元"));
		d.setKeyword3(d.new Keyword3("2014年3月6日"));
		d.setRemark(d.new Remark("欢迎下次光临"));
		m.setData(d);
		JSONObject o = JSONObject.fromObject(m);
		System.out.println(o.toString());
		String url = temp_message_url.replace("ACCESS_TOKEN", "GBPQijJkzJTNIdxQdkqMlU8xEMCNF45ZcmBv_0sr4r4Ob09fDBXMSCCtb2DpSVgWIeAEtnjKbaI28h4FNINjvw3ZUn9jGNu74eNjD8XXjXEDLYgAEAOWJ");
		JSONObject outputStr = JSONObject.fromObject(o.toString());
		JSONObject res = WeixinUtil.doHttpRequest(url, "POST", outputStr.toString());
		if(!res.getString("errcode").equals("0")){
			log.error("发送报警消息失败："+res.toString());
		}
		System.out.println(res.toString());
	}*/
	
}
