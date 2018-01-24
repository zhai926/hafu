package com.hafu.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hafu.entity.HfBehaviorRecord;
import com.hafu.entity.HfEncouragement;
import com.hafu.repository.jpa.HfBehaviorRecordDao;
import com.hafu.repository.jpa.HfEncouragementDao;
import com.hafu.repository.mybatis.HfBehaviorRecordMybatisDao;
import com.hafu.repository.mybatis.HfEncourageMybatisDao;
/**
 * 鼓励
 *
 */
@Service
public class EncouragementService {
	@Resource
	private HfEncourageMybatisDao encourageMybatisDao;
	
	@Resource
	private HfEncouragementDao encouragementDao;
	
	/**
	 * 获取鼓励列表
	 * @param userId
	 */
	public List<HfEncouragement> encourageList(Long userId,Integer pageNum,Integer pageSize){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId",userId);
		param.put("fromPos",(pageNum-1)*pageSize);
		param.put("pageSize",pageSize);
		return encourageMybatisDao.listEncourage(param);
	}
	/**
	 * 获取今日鼓励列表
	 * @param userId
	 */
	public Page<HfEncouragement> todayEncourageList(final Long userId,final String code,Integer pageNum,Integer pageSize){
		
        return encouragementDao.findAll(new Specification<HfEncouragement>() {
			
			@Override
			public Predicate toPredicate(Root<HfEncouragement> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
				String createDate_str = format2.format(new Date());
				Date createDate = null;
				try {
					createDate = format2.parse(createDate_str);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				List<Predicate> list=new ArrayList<Predicate>();
				list.add(cb.equal(root.get("receiveUserId"), userId));
				list.add(cb.equal(root.get("code"), code));
				list.add(cb.equal(root.get("sendStatus"), 0));
				list.add(cb.equal(root.get("deleteFlag"), 0));	
				list.add(cb.equal(root.get("createDate"), createDate));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		}, new PageRequest(pageNum-1, pageSize,Direction.DESC,"sendDate"));
	}
	
	
	
	public Page<HfEncouragement> encourageList(final Long userId,final String code,Integer pageNum,Integer pageSize){
		return encouragementDao.findAll(new Specification<HfEncouragement>() {
			
			@Override
			public Predicate toPredicate(Root<HfEncouragement> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> list=new ArrayList<Predicate>();
				list.add(cb.equal(root.get("receiveUserId"), userId));
				list.add(cb.equal(root.get("code"), code));
				list.add(cb.equal(root.get("sendStatus"), 0));
				list.add(cb.equal(root.get("deleteFlag"), 0));	
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		}, new PageRequest(pageNum-1, pageSize,Direction.DESC,"sendDate"));
	}
	
	public HfEncouragement save(HfEncouragement encouragement){
		return encouragementDao.save(encouragement);
	}
	
}
