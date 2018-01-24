package com.hafu.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hafu.entity.HfHealthTarget;
import com.hafu.repository.jpa.HfHealthTargetDao;


@Service
public class HfHealthTargetService {
	
	@Resource
	private HfHealthTargetDao healthTargetDao;
	
	
	/**
	 * 获取日、周、月健康目标各一条
	 * @param userId
	 * @param code
	 * @return
	 */
	public List<HfHealthTarget> getAllHealthTarget(final Long userId,final String code){
		List<HfHealthTarget> result=new ArrayList<HfHealthTarget>();
		List<HfHealthTarget> dayResult=healthTargetDao.findAll(new Specification<HfHealthTarget>() {
			
			@Override
			public Predicate toPredicate(Root<HfHealthTarget> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> list=new ArrayList<Predicate>();
				list.add(cb.equal(root.get("userId"), userId));
				list.add(cb.equal(root.get("code"), code));
				list.add(cb.equal(root.get("dataType"), 0));
				list.add(cb.equal(root.get("deleteFlag"), 0));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		},new PageRequest(0, 1,Direction.DESC,"id")).getContent();
		result.addAll(dayResult);
		
		List<HfHealthTarget> weekResult=healthTargetDao.findAll(new Specification<HfHealthTarget>() {
			
			@Override
			public Predicate toPredicate(Root<HfHealthTarget> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> list=new ArrayList<Predicate>();
				list.add(cb.equal(root.get("userId"), userId));
				list.add(cb.equal(root.get("code"), code));
				list.add(cb.equal(root.get("dataType"), 1));
				list.add(cb.equal(root.get("deleteFlag"), 0));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		},new PageRequest(0, 1,Direction.DESC,"id")).getContent();
		result.addAll(weekResult);
		
		List<HfHealthTarget> monthResult=healthTargetDao.findAll(new Specification<HfHealthTarget>() {
			
			@Override
			public Predicate toPredicate(Root<HfHealthTarget> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> list=new ArrayList<Predicate>();
				list.add(cb.equal(root.get("userId"), userId));
				list.add(cb.equal(root.get("code"), code));
				list.add(cb.equal(root.get("dataType"), 2));
				list.add(cb.equal(root.get("deleteFlag"), 0));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		},new PageRequest(0, 1,Direction.DESC,"id")).getContent();
		result.addAll(monthResult);
		return result;
	}
	
	/**
	 * 更新健康目标
	 * @param target
	 * @return
	 */
	public HfHealthTarget saveHealthTarget(final HfHealthTarget target){
		if(target.getUserId()!=null&&StringUtils.isNotBlank(target.getCode())&&target.getDataType()!=null){
			if(new Integer(0).equals(target.getDeleteFlag())){
				List<HfHealthTarget> old=healthTargetDao.findAll(new Specification<HfHealthTarget>() {
					
					@Override
					public Predicate toPredicate(Root<HfHealthTarget> root,
							CriteriaQuery<?> query, CriteriaBuilder cb) {
						// TODO Auto-generated method stub
						List<Predicate> list=new ArrayList<Predicate>();
						list.add(cb.equal(root.get("userId"), target.getUserId()));
						list.add(cb.equal(root.get("code"), target.getCode()));
						list.add(cb.equal(root.get("dataType"), target.getDataType()));
						list.add(cb.equal(root.get("deleteFlag"), 0));
						return cb.and(list.toArray(new Predicate[list.size()]));
					}
				},new PageRequest(0, 1,Direction.DESC,"id")).getContent();
				if(old!=null&&old.size()>0){
					for(HfHealthTarget t:old){
						if(target.getSleepTime()!=null)t.setSleepTime(target.getSleepTime());
						if(target.getStepCount()!=null)t.setStepCount(target.getStepCount());
						if(target.getStaticCount()!=null)t.setStaticCount(target.getStaticCount());
						if(target.getTotalActivity()!=null)t.setTotalActivity(target.getTotalActivity());
						if(target.getLiveness()!=null)t.setLiveness(target.getLiveness());
						t.setUpdateDate(new Date());
						return healthTargetDao.save(t);
					}
				}else{
					target.setCreateDate(new Date());
					return healthTargetDao.save(target);
				}
			}else{
				target.setUpdateDate(new Date());
				return healthTargetDao.save(target);
			}
		}
		return null;
	}
	
	
	/**
	 * 根据用户id ,设备编号，类型获取日、周、月健康目标
	 * @param code
	 * @param userId
	 * @param dataType	日期类型（0：日，1：周，2：月）
	 * @return
	 */
	public HfHealthTarget getHealthTarget(final String code,final Long userId,final Integer dataType){
		List<HfHealthTarget> result=healthTargetDao.findAll(new Specification<HfHealthTarget>() {
			
			@Override
			public Predicate toPredicate(Root<HfHealthTarget> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> list=new ArrayList<Predicate>();
				list.add(cb.equal(root.get("userId"), userId));
				list.add(cb.equal(root.get("code"), code));
				list.add(cb.equal(root.get("dataType"), dataType));
				list.add(cb.equal(root.get("deleteFlag"), 0));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		},new PageRequest(0, 1,Direction.DESC,"id")).getContent();
		if(result!=null&&result.size()>0){
			return result.get(0);
		}else{
			return null;
		}
	}
}
