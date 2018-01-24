package com.hafu.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

import com.hafu.entity.HfHealthPoint;
import com.hafu.entity.HfHealthTarget;
import com.hafu.repository.jpa.HfHealthPointDao;
import com.hafu.repository.jpa.HfHealthTargetDao;

@Service
public class HfHealthPointService {

	@Resource
	private HfHealthPointDao hfHealthPointDao;
	
	@Resource
	private HfHealthTargetDao healthTargetDao;
	
	/**
	 * 获取最近一条健康
	 */
	public HfHealthPoint getLastHealthPoint(final Long userId, final String code){
		Page<HfHealthPoint> list = hfHealthPointDao.findAll(new Specification<HfHealthPoint>() {
			@Override
			public Predicate toPredicate(Root<HfHealthPoint> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal(root.get("deleteFlag"), 0));
				//list.add(cb.equal(root.get("code"), code));
				list.add(cb.equal(root.get("userId"), userId));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		}, new PageRequest(0, 1,new  Sort(Direction.DESC, "createDate")));
		if(list!=null && list.getContent()!=null && list.getContent().size()!=0){
			return list.getContent().get(0);
		}
		return null;
	}
	
	/**
	 * 获取某一段时间的健康评分数据
	 * 
	 * @param begin
	 * @param end
	 * @param userId
	 * @param code
	 *            设备号
	 * @return
	 */
	public List<HfHealthPoint> getHfHealthPoint(final Date begin,
			final Date end, final Long userId, final String code) {
		return hfHealthPointDao.findAll(new Specification<HfHealthPoint>() {

			@Override
			public Predicate toPredicate(Root<HfHealthPoint> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal(root.get("deleteFlag"), 0));
				//list.add(cb.equal(root.get("code"), code));
				list.add(cb.equal(root.get("userId"), userId));
				list.add(cb.greaterThanOrEqualTo(root.<Date> get("createDate"),
						begin));
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(end);
				calendar.add(Calendar.DATE, 1);
				list.add(cb.lessThanOrEqualTo(root.<Date> get("createDate"),
						calendar.getTime()));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		}, new Sort(Direction.DESC, "createDate"));
	}

	/**
	 * 保存一条健康评分数据
	 * 
	 * @param healthPoint
	 * @return
	 */
	public HfHealthPoint save(HfHealthPoint healthPoint) {
		return hfHealthPointDao.save(healthPoint);
	}

	/***
	 * 根据编码和时间获取当天的数据
	 * 
	 * @param code
	 * @param Date
	 * @return
	 */
	public HfHealthPoint findOne(final String code, final Date begin,
			final Date end) {
		return hfHealthPointDao.findOne(new Specification<HfHealthPoint>() {

			@Override
			public Predicate toPredicate(Root<HfHealthPoint> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal(root.get("deleteFlag"), 0));
				list.add(cb.equal(root.get("code"), code));
				list.add(cb.greaterThanOrEqualTo(root.<Date> get("createDate"),
						begin));
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(end);
				list.add(cb.lessThanOrEqualTo(root.<Date> get("createDate"),
						calendar.getTime()));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		});
	}

	/**
	 * 删除某一用户下的健康评分数据
	 * 
	 * @param userId
	 * @param id
	 * @return
	 */
	public HfHealthPoint delete(final Long userId, final Long id) {
		HfHealthPoint healthPoint = hfHealthPointDao
				.findOne(new Specification<HfHealthPoint>() {

					@Override
					public Predicate toPredicate(Root<HfHealthPoint> root,
							CriteriaQuery<?> query, CriteriaBuilder cb) {
						// TODO Auto-generated method stub
						List<Predicate> list = new ArrayList<Predicate>();
						list.add(cb.equal(root.get("deleteFlag"), 0));
						list.add(cb.equal(root.get("id"), id));
						list.add(cb.equal(root.get("userId"), userId));
						return cb.and(list.toArray(new Predicate[list.size()]));
					}
				});
		if (healthPoint != null) {
			healthPoint.setDeleteFlag(1);
			return hfHealthPointDao.save(healthPoint);
		} else {
			return null;
		}
	}

	/**
	 * 获取最新的评分记录
	 */
	public HfHealthPoint lastHealthPoint(final Long currentId,final String code) {
		Page<HfHealthPoint> list = hfHealthPointDao.findAll(new Specification<HfHealthPoint>() {
			@Override
			public Predicate toPredicate(Root<HfHealthPoint> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal(root.get("deleteFlag"), 0));
				list.add(cb.equal(root.get("code"), code));
				list.add(cb.equal(root.get("userId"), currentId));
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				list.add(cb.lessThan(root.<Date> get("createDate"),
						calendar.getTime()));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		}, new PageRequest(0, 1,new  Sort(Direction.DESC, "createDate")));
		if(list!=null && list.getContent()!=null && list.getContent().size()!=0){
			return list.getContent().get(0);
		}
		return null;
		/*List<HfHealthPoint> li = hfHealthPointDao.findByUserIdAndDeleteFlag(
				currentId, 0, new PageRequest(0, 1, new Sort(Direction.DESC,
						"createDate")));
		if (CollectionUtils.isEmpty(li)) {
			return null;
		}
		return li.get(0);*/
	}
	
	
	
	public Float getOnePeriodScore(final Long userId,final String code,final Date begin,final int field,final int amount,final Integer dateType){
		Calendar c=Calendar.getInstance();
		c.setTime(begin);
		c.add(field, amount);
		final Date end=c.getTime();
		List<HfHealthPoint> list=hfHealthPointDao.findAll(new Specification<HfHealthPoint>() {

			@Override
			public Predicate toPredicate(Root<HfHealthPoint> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal(root.get("userId"), userId));
				list.add(cb.equal(root.get("code"), code));
				list.add(cb.equal(root.get("deleteFlag"), 0));
				//list.add(cb.equal(root.get("dateType"), dateType));
				list.add(cb.greaterThanOrEqualTo(root.<Date> get("createDate"), begin));
				list.add(cb.lessThanOrEqualTo(root.<Date> get("createDate"), end));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		});
		HfHealthTarget target=healthTargetDao.findTop1ByUserIdAndCodeAndDeleteFlagAndDataTypeOrderByIdDesc(userId, code, 0, dateType);
		if(list==null||list.size()==0){
			return null;
		}else{
			float score=0;
			int days=1;
			switch(dateType){
				case 1:days=1;break;
				case 2:days=7;break;
				case 3:days=30;break;
				default :days=1;break;
			}
			int tar=4000*days;
			Float stepCount=0f;
			int total=0;
			if(list==null||list.size()==0){
				return null;
			}else{
				for(HfHealthPoint point:list){
					if(point.getStepCount()!=null){
						stepCount+=point.getStepCount();
						total+=1;
					}
				}
			}
			if(total==0){
				return null;
			}
			if(target!=null&&target.getStepCount()!=null){
				tar=target.getStepCount();
			}
			tar=tar*list.size()/days;
			if(stepCount>tar){
				score+=100;
			}else{
				score+=(int)((stepCount*100.0)/tar);
			}
			return score;
		}
	}
	
	
}
