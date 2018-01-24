package com.hafu.quartz;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hafu.dto.UserDto;
import com.hafu.repository.mybatis.UserMybatisDao;
import com.hafu.service.HfExaminatonCheckdataService;

@Component("examinationQuartzTimer")
public class ExaminationQuartzTimer {

	private static Logger logger = Logger.getLogger(ExaminationQuartzTimer.class);
	@Autowired
	private HfExaminatonCheckdataService hfExaminatonCheckdataService;
	@Resource
	private UserMybatisDao userMybatisDao;
	
	/**
	 * 每隔30分钟执行一次
	 */
	@Scheduled(cron = "0 */3 * * * ?")
	public void handlerExamination() {
		logger.debug("********************执行体检数据同步Begin***********************");
		List<UserDto> list = userMybatisDao.getCardId();
		if (list != null) {
			if (list.size() > 0) {
				for(UserDto dto:list){
					hfExaminatonCheckdataService.handlerCheckData(dto.getId(), dto.getCard());
				}
			}
		}
		logger.debug("********************执行体检数据同步end***********************");
	}
}
