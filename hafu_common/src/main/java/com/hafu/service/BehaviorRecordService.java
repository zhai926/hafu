package com.hafu.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.log4j.Logger;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hafu.entity.HfBehaviorRecord;
import com.hafu.repository.jpa.HfBehaviorRecordDao;
import com.hafu.repository.mybatis.HfBehaviorRecordMybatisDao;
/**
 * 行为记录service;
 * @author zc
 *
 */
@Service
public class BehaviorRecordService {
    
    private static Logger logger = Logger.getLogger(BehaviorRecordService.class);

	@Resource
	private HfBehaviorRecordDao behaviorDao;
	@Resource
	private HfBehaviorRecordMybatisDao behaviorMybatisDao;
	
	/**
	 * 获取行为列表
	 * @param userId
	 */
	public List<HfBehaviorRecord> behaviorList(Long userId,String createDate,String code,String endTime){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId",userId);
		param.put("createDate", createDate);
		param.put("code", code);
		param.put("endDate", endTime);
		/*param.put("fromPos",(pageNum-1)*pageSize);
		param.put("pageSize",pageSize);*/
		return behaviorMybatisDao.listBehavior(param);
	}
	/**
	 * 获取行为列表   历史行为
	 * @param userId
	 */
	public List<HfBehaviorRecord> historyBehaviorList(String code,Long userId){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId",userId);
		param.put("code", code);
		return behaviorMybatisDao.historyBehavior(param);
	}
	
	
	
	/**
	 * 获取当前 行为 （最新的一条行为）
	 */
	public HfBehaviorRecord lastBehavior(String createDate,String code,String endTime){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("createDate", createDate);
		param.put("code",code);
		param.put("endDate", endTime);
		return behaviorMybatisDao.lastBehavior(param);
	}

	/**
	 * 根据开始时间和日期获取行为记录
	 * @param id
	 * @return
	 */
	public List<HfBehaviorRecord> findOneyTime(final Date startTime,final String behaviorType,final String code) {
	    
		return behaviorDao.findAll(new Specification<HfBehaviorRecord>() {
			@Override
			public Predicate toPredicate(Root<HfBehaviorRecord> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal(root.get("code"), code));
				list.add(cb.equal(root.get("startTime"), startTime));
				//list.add(cb.equal(root.get("createDate"), date));
				list.add(cb.equal(root.get("behaviorType"), behaviorType));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		});
	}
}
