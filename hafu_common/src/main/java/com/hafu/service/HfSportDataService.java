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

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hafu.entity.HfHealthTarget;
import com.hafu.entity.HfSportData;
import com.hafu.repository.jpa.HfHealthTargetDao;
import com.hafu.repository.jpa.HfSportDataDao;

@Service
public class HfSportDataService {

	@Resource
	private HfSportDataDao sportDataDao;
	@Resource
	private HfHealthTargetDao healthTargetDao;

	public HfSportData findOne(final Long id) {
		return sportDataDao.findOne(new Specification<HfSportData>() {

			@Override
			public Predicate toPredicate(Root<HfSportData> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal(root.get("id"), id));
				list.add(cb.equal(root.get("deleteFlag"), 0));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		});
	}

	public HfSportData saveSportData(HfSportData data) {
		return sportDataDao.save(data);
	}
	
	
	/***
	 * 获取用户某一段时间内的运动数据
	 * @param userId
	 * @param code
	 * @param begin
	 * @param end
	 * @return
	 */
	public List<HfSportData> getOnePeriodSportData(final Long userId,final String code, final Date begin, final Date end) {
		return sportDataDao.findAll(new Specification<HfSportData>() {

			@Override
			public Predicate toPredicate(Root<HfSportData> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal(root.get("userId"), userId));
				list.add(cb.equal(root.get("code"), code));
				list.add(cb.equal(root.get("deleteFlag"), 0));
				list.add(cb.greaterThanOrEqualTo(root.<Date> get("sportDate"), begin));
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(end);
				calendar.add(Calendar.DATE, 1);
				list.add(cb.lessThanOrEqualTo(root.<Date> get("sportDate"), calendar.getTime()));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		}, new Sort(Direction.ASC, "sportDate"));
	}

	
	public Float getOnePeriodScore(final Long userId,final String code,final Date begin,final int field,final int amount){
		Calendar c=Calendar.getInstance();
		c.setTime(begin);
		c.add(field, amount);
		final Date end=c.getTime();
		List<HfSportData> list=sportDataDao.findAll(new Specification<HfSportData>() {

			@Override
			public Predicate toPredicate(Root<HfSportData> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> list = new ArrayList<Predicate>();
				//list.add(cb.equal(root.get("userId"), userId));
				list.add(cb.equal(root.get("code"), code));
				list.add(cb.equal(root.get("deleteFlag"), 0));
				list.add(cb.greaterThanOrEqualTo(root.<Date> get("sportDate"), begin));
				list.add(cb.lessThanOrEqualTo(root.<Date> get("sportDate"), end));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		});
		HfHealthTarget target=healthTargetDao.findTop1ByUserIdAndCodeAndDeleteFlagAndDataTypeOrderByIdDesc(userId, code, 0, 1);
		if(list==null||list.size()==0){
			return null;
		}else{
			float score=0;
			int tar=4000;
			for(HfSportData sport:list){
				if(target!=null&&target.getStepCount()!=null){
					tar=target.getStepCount();
				}
				if(sport.getStepNumber()>tar){
					score+=100;
				}else {
					score+=((sport.getStepNumber()*1.0)/tar)*100;
				}
			}
			return score/list.size();
		}
	}
}
