package com.hafu.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hafu.entity.HfDevice;
import com.hafu.repository.jpa.HfDeviceDao;
import com.hafu.repository.mybatis.HfDeviceMybatisDao;

@Service
public class HfDeviceService {
	
	@Resource
	private HfDeviceDao hfDeviceDao;
	@Resource
	private HfDeviceMybatisDao deviceMybatisDao;
	
	public HfDevice getAlert(final String code){
		return hfDeviceDao.findOne(new Specification<HfDevice>() {
			@Override
			public Predicate toPredicate(Root<HfDevice> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal(root.get("code"), code));
				list.add(cb.equal(root.get("deleteFlag"), 0));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		});
	}

	public void save(HfDevice hfDevice) {
		hfDeviceDao.save(hfDevice);
	}
		public List<HfDevice> getAllDevice(){
		return deviceMybatisDao.findAllDevice();
	}
	
	
}
