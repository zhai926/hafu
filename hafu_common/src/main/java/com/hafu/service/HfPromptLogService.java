package com.hafu.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hafu.entity.HfPromptLog;
import com.hafu.repository.jpa.HfPromptLogDao;

/**
 * 
 * <p>Title: HfPropmtLogService</p>
 * <p>Description: 关爱提醒操作</p>
 * <p>Company: 嘉善网络 </p>
 * @author 时志多
 * @date 2016年7月6日上午10:24:41
 */
@Service
public class HfPromptLogService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	private HfPromptLogDao hfPropmtLogDao;

	/**
	 * 获取所有已发送的提醒信息
	 * 
	 * @return
	 */
	public List<HfPromptLog> findAll() {
		return hfPropmtLogDao.findAll(new Specification<HfPromptLog>() {
			@Override
			public Predicate toPredicate(Root<HfPromptLog> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal(root.get("deleteFlag"), 0));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		});
	}
	
	/**
	 * 保存（修改）
	 * @param hfPropmtLog
	 * @return
	 */
	public HfPromptLog save(HfPromptLog hfPropmtLog){
		return hfPropmtLogDao.save(hfPropmtLog);
	}

	public HfPromptLog findOne(final String code,final String id){
		return hfPropmtLogDao.findOne(new Specification<HfPromptLog>() {
			@Override
			public Predicate toPredicate(Root<HfPromptLog> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal(root.get("ebanId"), id));
				list.add(cb.equal(root.get("code"), code));
				list.add(cb.equal(root.get("deleteFlag"), 0));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		});
	}
}
