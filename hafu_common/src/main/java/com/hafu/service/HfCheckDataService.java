package com.hafu.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.hafu.modules.utils.PublicProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hafu.entity.HfCheckData;
import com.hafu.repository.jpa.HfCheckDataDao;
import com.hafu.repository.mybatis.HfCheckDataMybatisDao;

@Service
public class HfCheckDataService {

	@Resource
	private HfCheckDataDao checkDataDao;

	@Resource
	private HfCheckDataMybatisDao checkMybatisDao;

	@Resource
	private EntityManagerFactory entityManagerFactory;

	/**
	 * 保存一条用户检测数据
	 * 
	 * @param checkData
	 * @return
	 */
	public HfCheckData save(final HfCheckData checkData) {
		if(checkData.getUserId()==null||StringUtils.isBlank(checkData.getCode())||checkData.getCheckDate()==null){
			return null;
		}
		List<HfCheckData> old=checkDataDao.findAll(new Specification<HfCheckData>() {

			@Override
			public Predicate toPredicate(Root<HfCheckData> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> list=new ArrayList<Predicate>();
				list.add(cb.equal(root.get("userId"), checkData.getUserId()));
				//list.add(cb.equal(root.get("code"), checkData.getCode()));
				list.add(cb.equal(root.get("checkType"), checkData.getCheckType()));
				list.add(cb.equal(root.get("checkCategory"), checkData.getCheckCategory()));
				final Date date=checkData.getCheckDate();
				final Calendar c=Calendar.getInstance();
				c.setTime(date);
				c.add(Calendar.DATE, 1);
				list.add(cb.greaterThanOrEqualTo(root.<Date>get("checkDate"), date));
				list.add(cb.lessThan(root.<Date>get("checkDate"),c.getTime()));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		});
		
		if(old!=null&&old.size()>0){
			HfCheckData temp=old.get(0);
			temp.setCheckAddress(checkData.getCheckAddress());
			temp.setCheckCategory(checkData.getCheckCategory());
			temp.setCheckData(checkData.getCheckData());
			temp.setCheckType(checkData.getCheckType());
			temp.setDicItemName(checkData.getDicItemName());
			for(int i=1;i<old.size();i++){
				HfCheckData del=old.get(i);
				del.setDeleteFlag(1);
				checkDataDao.save(del);
			}
			return checkDataDao.save(temp);
		}else{
			return checkDataDao.save(checkData);
		}
	}

	/**
	 * 软删除某一条用户检测数据
	 * 
	 * @param id
	 *            数据id
	 * @param userId
	 *            用户id
	 * @return
	 */
	public HfCheckData delete(final Long id, final Long userId) {
		HfCheckData checkData = checkDataDao.findOne(new Specification<HfCheckData>() {

			@Override
			public Predicate toPredicate(Root<HfCheckData> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal(root.get("deleteFlag"), 0));
				list.add(cb.equal(root.get("userId"), userId));
				list.add(cb.equal(root.get("id"), id));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		});
		if (checkData != null) {
			checkData.setDeleteFlag(1);
			return checkDataDao.save(checkData);
		} else {
			return null;
		}
	}

	/**
	 * 获取一段时间内的某一项检测数据
	 * 
	 * @param userId
	 *            用户id
	 * @param checkCategory
	 *            检查分类，来自数据字典
	 * @param checkType
	 *            检查类型（分类下的子栏目,如健康数据：血压、血脂、血糖、尿检，引用数据字典）
	 * @param begin
	 *            开 始时间
	 * @param end
	 *            结束时间
	 * @param page
	 *            当前页
	 * @param size
	 *            每页显示数据条数
	 * @return
	 */
	public List<HfCheckData> getOnePageCheckData(final Long userId, final String checkCategory, final String checkType,
			final Date begin, final Date end) {
		return checkDataDao.findAll(new Specification<HfCheckData>() {

			@Override
			public Predicate toPredicate(Root<HfCheckData> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal(root.get("checkCategory"), checkCategory));
				list.add(cb.equal(root.get("checkType"), checkType));
				list.add(cb.equal(root.get("userId"), userId));
				list.add(cb.greaterThanOrEqualTo(root.<Date> get("checkDate"), begin));
				list.add(cb.lessThanOrEqualTo(root.<Date> get("checkDate"), end));
				list.add(cb.equal(root.get("deleteFlag"), 0));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		}, new Sort(Direction.DESC, "createDate"));
	}
	
	/**
	 * 获取一段时间内的某一分类下的所有检测项目
	 * @param userId		用户id
	 * @param code		设备编号
	 * @param checkCategory	检测分类
	 * @param begin			开始时间
	 * @param end			结束时间
	 * @return
	 */
	public List<HfCheckData> getOneCategoryCheckDate(final Long userId, final String code,
			final String checkCategory, final Date begin,
			final Date end) {
		return checkDataDao.findAll(new Specification<HfCheckData>() {

			@Override
			public Predicate toPredicate(Root<HfCheckData> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal(root.get("checkCategory"), checkCategory));
				list.add(cb.equal(root.get("userId"), userId));
				//list.add(cb.equal(root.get("code"), code));
				list.add(cb.greaterThanOrEqualTo(root.<Date> get("checkDate"), begin));
				Calendar calendar=Calendar.getInstance();
				calendar.setTime(end);
				calendar.add(Calendar.DATE, 1);
				list.add(cb.lessThanOrEqualTo(root.<Date> get("checkDate"), calendar.getTime()));
				list.add(cb.equal(root.get("deleteFlag"), 0));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		},new Sort(Direction.DESC,"checkDate"));
	}
	
	
	
	public List<HfCheckData> getCheckDate(final Long userId, final String code,
			 final Date begin,final Date end) {
		return checkDataDao.findAll(new Specification<HfCheckData>() {

			@Override
			public Predicate toPredicate(Root<HfCheckData> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal(root.get("userId"), userId));
				//list.add(cb.equal(root.get("code"), code));
				list.add(cb.greaterThanOrEqualTo(root.<Date> get("checkDate"), begin));
				Calendar calendar=Calendar.getInstance();
				calendar.setTime(end);
				calendar.add(Calendar.DATE, 1);
				list.add(cb.lessThanOrEqualTo(root.<Date> get("checkDate"), calendar.getTime()));
				list.add(cb.equal(root.get("deleteFlag"), 0));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		},new Sort(Direction.ASC,"checkDate"));
	}
	
	public Page<HfCheckData> getOnePageCheckData(final Long userId,final String code,
			final String checkCategory,final String checkType,final Date begin,
			final Date end,Integer page,Integer size){
		return checkDataDao.findAll(new Specification<HfCheckData>() {

			@Override
			public Predicate toPredicate(Root<HfCheckData> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> list=new ArrayList<Predicate>();
				list.add(cb.equal(root.get("userId"), userId));
				//list.add(cb.equal(root.get("code"), code));
				list.add(cb.equal(root.get("checkCategory"), checkCategory));
				list.add(cb.equal(root.get("checkType"), checkType));
				list.add(cb.equal(root.get("deleteFlag"), 0));
				list.add(cb.greaterThanOrEqualTo(root.<Date>get("checkDate"), begin));
				Calendar calendar=Calendar.getInstance();
				calendar.setTime(end);
				calendar.add(Calendar.DATE, 1);
				list.add(cb.lessThanOrEqualTo(root.<Date>get("checkDate"), calendar.getTime()));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		}, new PageRequest(page-1, size, Direction.ASC, "checkDate"));
	}

	/**
	 * 添加检测数据
	 * @param list
	 * @throws Exception
	 */
	public boolean insert(HfCheckData hfCheckData){
		Integer flag = 0;
		try {
			flag = checkMybatisDao.insert(hfCheckData);;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag>0 ? true : false;
	}
	/**
	 * 批量添加检测数据
	 * @param list
	 * @throws Exception
	 */
	public void insertBatch(List<HfCheckData> list) throws Exception{
		if(list!=null){
			if(list.size()>0){
				checkMybatisDao.insertBatch(list);
			}
		}
	}
	
	/**
	 * 批量修改检测数据
	 * @param list
	 * @return
	 */
	public void updateBatch(List<HfCheckData> list) throws Exception{
		if(list!=null){
			if(list.size()>0){
				checkMybatisDao.updateBatch(list);
			}
		}
	}
	/**
	 * 根据userId和checkType查询体检信息
	 * @param map
	 * @return
	 */
	public HfCheckData findByUserIdType(Long userId,String checkType,String beginTime,String endTime){
		Map<String, Object> params  = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("checkType", checkType);
		params.put("beginTime", beginTime);
		params.put("endTime", endTime);
		return checkMybatisDao.findByUserIdType(params);
	}
	
	public Float getOnePeriodScore(final String checkCategory,final Long userId,final String code,final Date begin,final int field,final int amount){
		List<HfCheckData> list=checkDataDao.findAll(new Specification<HfCheckData>() {

			@Override
			public Predicate toPredicate(Root<HfCheckData> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				List<Predicate> predicates=new ArrayList<Predicate>();
				predicates.add(cb.equal(root.get("checkCategory"), checkCategory));
				predicates.add(cb.equal(root.get("deleteFlag"), 0));
				predicates.add(cb.equal(root.get("userId"), userId));
				//predicates.add(cb.equal(root.get("code"), code));
				predicates.add(cb.greaterThanOrEqualTo(root.<Date>get("checkDate"), begin));
				Calendar cal=Calendar.getInstance();
				cal.setTime(begin);
				cal.add(field, amount);
				predicates.add(cb.lessThan(root.<Date>get("checkDate"), cal.getTime()));
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		});
		if(list==null||list.size()==0){
			return null;
		}else{
			Float score=null;
			int count=0;
			for(HfCheckData data:list){
				if(PublicProperty.bodyData.equals(checkCategory)){
					//身体
					if(score==null) score=0f;
					if(PublicProperty.bodyData_SM.equals(data.getCheckType())){
						count+=1;
						float d=Float.valueOf(data.getCheckData());
						if(d>=7&&d<=10){
							score+=100;
						}
					}else if(PublicProperty.bodyData_TN.equals(data.getCheckType())){
					}else if(PublicProperty.bodyData_XL.equals(data.getCheckType())){
						count+=1;
						float d=Float.valueOf(data.getCheckData());
						if(d>=60&&d<=100){
							score+=100;
						}
					}
				}else if(PublicProperty.environmentData.equals(checkCategory)){
					//环境
					if(score==null) score=0f;
					float d=Float.valueOf(data.getCheckData());
					if(PublicProperty.environmentData_SD.equals(data.getCheckType())){
						if(d<1){d=d*100;}
						if(d>=30&&d<=80){
							score+=100;
						}
						count+=1;
					}/*else if(PublicProperty.environmentData_ZY.equals(data.getCheckType())){
						if(d<=50){
							score+=100;
						}
						count+=1;
					}*/
				}else if(PublicProperty.healthData.equals(checkCategory)){
					if(score==null) score=0f;
					if(PublicProperty.healthData_XT.equals(data.getCheckType())){
						float d=Float.valueOf(data.getCheckData());
						if(d>=3.89&&d<=6.1){
							score+=100;
						}
						count+=1;
					}else if(PublicProperty.healthData_XY.equals(data.getCheckType())){
						String[] strs=data.getCheckData().split(",");
						if(strs.length>=2){
							float min=Float.valueOf(strs[0]);
							float max=Float.valueOf(strs[1]);
							if(min>max){
								float temp=min;
								min=max;
								max=temp;
							}
							if(min>=70&&min<=90){
								score+=50;
							}
							if(max>=110&&max<=148){
								score+=50;
							}
							count+=1;
						}
					}else if(PublicProperty.healthData_XZ.equals(data.getCheckType())){
						String[] strs=data.getCheckData().split(",");
						if(strs.length==4){
							float zdgc=Float.valueOf(strs[0]);
							float gysz=Float.valueOf(strs[1]);
							float gmd=Float.valueOf(strs[2]);
							float dmd=Float.valueOf(strs[3]);
							if(zdgc>=2.08&&zdgc<=5.17){
								score+=25;
							}
							if(gysz<=0.94){
								score+=25;
							}
							if(gmd>=4&&gmd<=10){
								score+=25;
							}
							if(dmd>=110&&dmd<=160){
								score+=25;
							}
							count+=1;
						}
					}
				}
			}
			if(score==null||count==0){
				return null;
			}else{
				return score/count;
			}
		}
	}
}
