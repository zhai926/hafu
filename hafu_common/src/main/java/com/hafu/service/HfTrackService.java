package com.hafu.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.springframework.util.CollectionUtils;

import com.hafu.entity.HfTrack;
import com.hafu.repository.jpa.HfTrackDao;
import com.hafu.repository.mybatis.HfTrackMybatisDao;

@Service
public class HfTrackService {

	@Resource
	private HfTrackDao trackDao;
	
	@Resource
	private HfTrackMybatisDao trackMybatisDao;

	/**
	 * 获取某一天的运动轨迹
	 * 
	 * @param userId
	 * @param date
	 * @return
	 */
	public List<HfTrack> getOneDayTrack(final Long userId,final String code,final Date date) {
		Page<HfTrack> page=trackDao.findAll(new Specification<HfTrack>() {

			@Override
			public Predicate toPredicate(Root<HfTrack> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> list = new ArrayList<Predicate>();
				//list.add(cb.equal(root.get("userId"), userId));
				list.add(cb.equal(root.get("code"),code));
				//list.add(cb.equal(root.get("deleteFlag"), 0));
				list.add(cb.greaterThanOrEqualTo(root.<Date> get("alertDate"), date));
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				calendar.add(Calendar.DATE, 1);
				list.add(cb.lessThan(root.<Date> get("alertDate"), calendar.getTime()));
				query.groupBy(root.get("position"));
				query.where(cb.and(list.toArray(new Predicate[list.size()])));
				return query.getRestriction();
			}
		},new PageRequest(0, 100, Direction.DESC, "alertDate"));
		if(page!=null){
			return page.getContent();
		}else{
			return null;
		}
	}
	
	

	/**
	 * 获取某一天的报警记录
	 * 
	 * @param userId
	 * @param date
	 * @return
	 */
	public List<HfTrack> getOneDayAlertTrack(final Long userId, final Date date) {
		return trackDao.findAll(new Specification<HfTrack>() {

			@Override
			public Predicate toPredicate(Root<HfTrack> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal(root.get("userId"), userId));
				//list.add(cb.equal(root.get("deleteFlag"), 0));
				list.add(cb.greaterThanOrEqualTo(root.<Date> get("createDate"), date));
				list.add(cb.greaterThanOrEqualTo(root.<Date> get("alertDate"), date));
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				calendar.add(Calendar.DATE, 1);
				list.add(cb.lessThanOrEqualTo(root.<Date> get("createDate"), calendar.getTime()));
				list.add(cb.lessThanOrEqualTo(root.<Date> get("alertDate"), calendar.getTime()));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		});
	}

	/**
	 * 保存轨迹记录
	 * @param track
	 * @return
	 */
	public HfTrack saveTrack(HfTrack track) {
		return trackDao.save(track);
	}
	
	/**
	 * 获取一条未删除的轨迹记录
	 * @param id
	 * @return
	 */
	public HfTrack findOne(final Long id) {
		return trackDao.findOne(new Specification<HfTrack>() {
			@Override
			public Predicate toPredicate(Root<HfTrack> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal(root.get("id"), id));
				//list.add(cb.equal(root.get("deleteFlag"), 0));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		});
	}

	
	/**
	 * 获取最新的轨迹
	 */
	public HfTrack lastTrack(final Long currentId){
		Page<HfTrack> li = trackDao.findAll(new Specification<HfTrack>() {
			@Override
			public Predicate toPredicate(Root<HfTrack> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal(root.get("userId"), currentId));
				//list.add(cb.equal(root.get("deleteFlag"), 0));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
			
		},new PageRequest(0,1,new Sort(Direction.DESC,"createDate")));
		if(li!=null && (!CollectionUtils.isEmpty(li.getContent()))){
			return li.getContent().get(0);
		}
		return null;
	}

	
	/**
	 * 根据开始时间和日期获取轨迹记录
	 * @param id
	 * @return
	 */
	public List<HfTrack> findOneyTime(final Date time,final Date date,final String code) {
		return trackDao.findAll(new Specification<HfTrack>() {
			@Override
			public Predicate toPredicate(Root<HfTrack> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> list = new ArrayList<Predicate>();
				//list.add(cb.equal(root.get("createDate"), date));
				list.add(cb.equal(root.get("alertDate"), time));
				list.add(cb.equal(root.get("code"), code));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		});
	}
	
	public HfTrack lastTrack(final String code){
		Page<HfTrack> li = trackDao.findAll(new Specification<HfTrack>() {
			@Override
			public Predicate toPredicate(Root<HfTrack> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {			
				/*List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal(root.get("code"), code));
				list.add(cb.equal(root.get("deleteFlag"), 0));				
				query.groupBy(root.get("position"));
				query.where(cb.and(list.toArray(new Predicate[list.size()])));
				return query.getRestriction();*/			
				
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				Date date=new Date();
				try {				
					date=sdf.parse(sdf.format(date));
				} catch (ParseException e) {					
					e.printStackTrace();
				}				
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal(root.get("code"), code));
				list.add(cb.equal(root.get("deleteFlag"), 0));				
				list.add(cb.greaterThanOrEqualTo(root.<Date> get("alertDate"), date));
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				calendar.add(Calendar.DATE, 1);
				list.add(cb.lessThan(root.<Date> get("alertDate"), calendar.getTime()));
				query.groupBy(root.get("position"));
				query.where(cb.and(list.toArray(new Predicate[list.size()])));
				return query.getRestriction();
			}
			
		},new PageRequest(0,1,Direction.DESC,"alertDate"));
		if(li!=null && (!CollectionUtils.isEmpty(li.getContent()))){
			return li.getContent().get(0);
		}
		return null;
	}
	
	
	public HfTrack lastTrack(final String code, final Date date){
		Page<HfTrack> li = trackDao.findAll(new Specification<HfTrack>() {
			@Override
			public Predicate toPredicate(Root<HfTrack> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {	
			
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal(root.get("code"), code));
				list.add(cb.equal(root.get("deleteFlag"), 0));				
				list.add(cb.greaterThanOrEqualTo(root.<Date> get("alertDate"), date));
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				calendar.add(Calendar.DATE, 1);
				list.add(cb.lessThan(root.<Date> get("alertDate"), calendar.getTime()));
				query.groupBy(root.get("position"));
				query.where(cb.and(list.toArray(new Predicate[list.size()])));
				return query.getRestriction();
			}
			
		},new PageRequest(0,1,Direction.DESC,"alertDate"));
		if(li!=null && (!CollectionUtils.isEmpty(li.getContent()))){
			return li.getContent().get(0);
		}
		return null;
	}
	
	public Page<HfTrack> getOnePage(int page,int size){
		return trackDao.findAll(new Specification<HfTrack>() {
			@Override
			public Predicate toPredicate(Root<HfTrack> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {				
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal(root.get("deleteFlag"), 0));
				query.groupBy(root.get("code"));
				query.where(cb.and(list.toArray(new Predicate[list.size()])));
				return query.getRestriction();
			}
			
		},new PageRequest(page-1,size,Direction.DESC,"alertDate"));
	}
	
	public int getTotalNumber(){
		return trackMybatisDao.getTrackTotalNumberGroupByCode();
	}

}
