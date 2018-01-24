package com.hafu.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hafu.entity.HfBehaviorRecord;
import com.hafu.entity.HfHappiness;
import com.hafu.repository.jpa.HfBehaviorRecordDao;
import com.hafu.repository.jpa.HfCodeDao;
import com.hafu.repository.jpa.HfVoiceDao;
import com.hafu.repository.mybatis.HfBehaviorRecordMybatisDao;
import com.hafu.repository.mybatis.HfHappinessMybatisDao;

/** 
 * 快乐指数
 */
@Service
public class HfHappinessService {
	@Resource
	private HfHappinessMybatisDao happinessMybatisDao;
	@Resource
	private HfCodeDao codeDao;
	@Resource
	private HfBehaviorRecordDao behaviorDao;
	@Resource
	private HfBehaviorRecordMybatisDao behaviorMybatisDao; 
	@Resource
	private HfVoiceDao voiceDao;
	private  int socre=60;//基础分
    //产生随机数
	int random1 = new Random().nextInt();
	
	 /**
	  * 查询快乐指数
	  * @param userId
	  * @param createDate
	  * @param code
	  * @param endTime
	  * @return
	  */
	  public List<HfHappiness> findHappiness(Long userId,String createDay,String code) {
		  Map<String, Object> param = new HashMap<String, Object>();
			param.put("userId",userId);
			param.put("code", code);
			param.put("createDay", createDay);
		return happinessMybatisDao.findHappiness(param);
	   }
	  /**
	   * 保存到数据库
	   */
	  public void insertHappiness(HfHappiness happiness) { 
		happinessMybatisDao.insertHappiness(happiness);  
	} 
	  /**
	   * 修改数据库
	   * @param happiness
	   */ 
	  public void updateHappiness(HfHappiness happiness){
		  happinessMybatisDao.updateHappiness(happiness);
	  }
 
	  /**
	   * 获取快乐指数列表信息
	   * @param userId
	   * @param createDate
	   * @param code
	   * @param endTime
	   * @return
	   */
/*	  public List<HfHappiness> HfHappinessList(Long userId,String createDate,String code,String endTime){
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("userId",userId);
			param.put("createDate", createDate);
			param.put("code", code);
			param.put("endDate", endTime);
			return happinessMybatisDao.listHappiness(param);
		}*/
}
