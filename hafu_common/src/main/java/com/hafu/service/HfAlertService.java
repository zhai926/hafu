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

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hafu.entity.HfAlert;
import com.hafu.entity.HfUserAlert;
import com.hafu.repository.jpa.HfAlterDao;
import com.hafu.repository.jpa.HfUserAlertDao;
import com.hafu.repository.mybatis.HfAlertMybatisDao;

@Service
public class HfAlertService {

	@Resource
	private HfAlterDao hfAlertDao;
	
	@Resource
	private HfUserAlertDao hfUserAlertDao;

	@Resource
	private HfAlertMybatisDao alertMybatisDao;

	public HfAlert getAlert(final Date alertDate, final String code) {
		return hfAlertDao.findOne(new Specification<HfAlert>() {
			@Override
			public Predicate toPredicate(Root<HfAlert> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal(root.get("alertDate"), alertDate));
				list.add(cb.equal(root.get("code"), code));
				// list.add(cb.equal(root.get("deleteFlag"), 0));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		});
	}

	public void save(HfAlert hfAlert) {
		hfAlertDao.save(hfAlert);
	}
	
	public HfAlert selectById(Integer id){
		return alertMybatisDao.selectById(id);
	}

	
	/**
	 * 
	 * @param code			code
	 * @param currentUserId	登陆用户id
	 * @param all			是否查询所有报警记录,true查询所有，false查询未读报警记录
	 * @return
	 */
	public List<HfAlert> listAlert(Long userId,String code, Long currentUserId,boolean all) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("code", code);
		param.put("currentUserId", currentUserId);
		param.put("userId", userId);
		List<Integer> list=new ArrayList<Integer>();
		list.add(0);
		if(all){
			list.add(1);
		}else{
			//未读记录只显示今天的报警记录
			Date date=new Date();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			try {
				date=sdf.parse(sdf.format(date));
				param.put("today", date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		param.put("status", list);
		return alertMybatisDao.listAlert(param);
	}
	
	
	public boolean readAlert(final Long alertId,final Long currentUserId){
		HfUserAlert userAlert=hfUserAlertDao.findOne(new Specification<HfUserAlert>() {

			@Override
			public Predicate toPredicate(Root<HfUserAlert> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				return cb.and(cb.equal(root.get("alertId"), alertId),
						cb.equal(root.get("concernUserId"), currentUserId));
			}
		});
		if(userAlert!=null){
			userAlert.setReadStatus(1);
			hfUserAlertDao.save(userAlert);
			return true;
		}else{
			HfAlert alert=hfAlertDao.findOne(new Specification<HfAlert>() {

				@Override
				public Predicate toPredicate(Root<HfAlert> root,
						CriteriaQuery<?> query, CriteriaBuilder cb) {
					// TODO Auto-generated method stub
					return cb.and(cb.equal(root.get("id"), alertId),
							cb.equal(root.get("deleteFlag"), 0));
				}
			});
			if(alert!=null){
				userAlert=new HfUserAlert();
				userAlert.setAlertId(alertId);
				userAlert.setCode(alert.getCode());
				userAlert.setConcernUserId(currentUserId);
				userAlert.setCreateDate(new Date());
				userAlert.setReadStatus(1);
				userAlert.setUserId(alert.getUserId());
				hfUserAlertDao.save(userAlert);
				return true;
			}
		}
		return false;
	}

}
