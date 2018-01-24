package com.hafu.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.hafu.modules.utils.PublicProperty;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.hafu.dto.UserDto;
import com.hafu.eban.entity.response.BoundResponse;
import com.hafu.eban.service.BoundService;
import com.hafu.entity.HfConcernDevice;
import com.hafu.entity.HfConcernUser;
import com.hafu.entity.HfDevice;
import com.hafu.entity.HfModuleSettings;
import com.hafu.entity.HfUser;
import com.hafu.repository.jpa.HfAlterDao;
import com.hafu.repository.jpa.HfConcernDeviceDao;
import com.hafu.repository.jpa.HfConcernUserDao;
import com.hafu.repository.jpa.HfDeviceDao;
import com.hafu.repository.jpa.HfModuleSettingsDao;
import com.hafu.repository.jpa.HfUserDao;
import com.hafu.repository.mybatis.HfDeviceMybatisDao;
import com.hafu.repository.mybatis.UserMybatisDao;
/** 
 * 用户 
 */
@Service
public class UserService {
	
	@Resource
	private HfUserDao hfUserDao;
	@Resource
	private HfDeviceMybatisDao deviceMybatisDao;
	@Resource
	private UserMybatisDao userMybatisDao;
	@Resource
	private HfAlterDao alterDao;
	@Resource
	private HfDeviceDao deviceDao;
	@Resource
	private HfUserDao userDao;
	@Resource
	private HfConcernUserDao concernUserDao;
	@Resource
	private BoundService boundService;
	@Resource
	private HfExaminatonCheckdataService examinationSevice;
	@Resource
	private HfConcernDeviceDao concernDeviceDao;//
	@Resource
	private HfModuleSettingsDao settingDao;
	/**
	 * 根据 手机号 或者 用户名 获得 用户
	 */
	public HfUser getUserByNameOrPhone(String str){
		List<HfUser> user = hfUserDao.findByNameOrMobile(str,str);
		if(user != null && user.size()>0){
			return user.get(0);
		}
		return null;
	}
	
	/**
	 * 根据openId获取用户
	 * @param openId
	 * @return
	 */
	public HfUser getUserByOpenId(String openId) {
		return hfUserDao.findByOpenId(openId);
	}
	
	/**
	 * 根据id  获得用户
	 * @param id
	 * @return
	 */
	public HfUser getUser(Long id){
		HfUser user=hfUserDao.findOne(id);
		return user;
	}
	

	
	/**
	 * 保存用户 信息 ， 微信用户
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public synchronized HfUser newUser(HfUser m){
		HfUser o = hfUserDao.findByOpenId(m.getOpenId());
		if(o!=null){
			return o;
		}
		hfUserDao.save(m);
		HfModuleSettings m1 = new HfModuleSettings();
		m1.setConcernUserId(m.getId());
		m1.setModuleCode(PublicProperty.INDEX_MODULE.get("INDEX_XWJL"));
		m1.setCreateDate(new Date());
		settingDao.save(m1);
		HfModuleSettings m2 = new HfModuleSettings();
		m2.setConcernUserId(m.getId());
		m2.setModuleCode(PublicProperty.INDEX_MODULE.get("INDEX_YDGJ"));
		m2.setCreateDate(new Date());
		settingDao.save(m2);
		HfModuleSettings m3 = new HfModuleSettings();
		m3.setConcernUserId(m.getId());
		m3.setModuleCode(PublicProperty.INDEX_MODULE.get("INDEX_YDSJ"));
		m3.setCreateDate(new Date());
		settingDao.save(m3);
		HfModuleSettings m4 = new HfModuleSettings();
		m4.setConcernUserId(m.getId());
		m4.setModuleCode(PublicProperty.INDEX_MODULE.get("INDEX_HJSJ"));
		m4.setCreateDate(new Date());
		settingDao.save(m4);
		return m;
	}
	
	
	/**
	 * 保存用户 信息 ， 非微信用户
	 * true,保存成功
	 * false,已存在
	 */
	public synchronized boolean saveUser(HfUser m){
		List<HfUser> user = hfUserDao.findByNameOrMobile(m.getMobile(),m.getMobile());
		if(user==null || user.size()==0){
			 hfUserDao.save(m);
			 return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 更新用户信息，
	 * true,保存成功
	 * false,已存在
	 */
	public boolean updateUser(HfUser m){
		 hfUserDao.save(m);
		 return true;
	}
	
	/**
	 * 验证手机号是否存在
	 * @param phone
	 * @return
	 */
	public HfUser getUserByPhone(String phone){
		List<HfUser> list =  hfUserDao.findByMobile(phone);
		if(list != null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	public boolean modifyUserInformation(HfUser user,Long userId){
		HfUser temp=getUser(userId);
		if(temp==null||user==null){
			//用户查不到
			return false;
		}else{
			boolean flag1=validatePhone(user.getMobile(), userId);
			if(flag1){
				return false;
			}else{
				boolean flag=false;
				if(!user.getMobile().equals(temp.getMobile())){
					if(user.getMobile()!=null){
						flag=true;
						temp.setMobile(user.getMobile());
					}
				}
				if(user.getAvater()!=null){
					flag=true;
					temp.setAvater(user.getAvater());
				}
				if(StringUtils.isNotBlank(user.getNickName())){
					flag=true;
					temp.setNickName(user.getNickName());
				}
				if(flag)hfUserDao.save(temp);
				return true;
			}
		}
		
	}
	
	private boolean validatePhone(String mobilePhone,Long userId){
		List<HfUser> list=hfUserDao.findByMobileAndIdNot(mobilePhone,userId);
		if(list!=null&&!list.isEmpty()){
			return true;
		}
		return false;
	}
	
	/**
	 * 根据userId 获取该用户最新绑定的人 (包含最新设备)
	 */
	public UserDto getLastBandingUser(Map map){
		List<UserDto> list = deviceMybatisDao.myAttentitonDeviceList(map);
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		return list.get(0);
	}
	
	/**
	 * 根据userId 获取用户信息和最新设备
	 */
	public UserDto getUserAndDevice(Long userid,Long concernId){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("userId", userid);
		map.put("concernUserId", concernId);
		UserDto u = deviceMybatisDao.userAndIsLastDevice(map);
		return u;
	}
	
	/**
	 * 根据关注人id  和  持有人id  查询 关注人和设备关系 
	 */
	public List<HfConcernDevice> getConcernDeviceFromUserDevice(Long concernUserId,Long userId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("concernUserId", concernUserId);
		map.put("userId", userId);
		return deviceMybatisDao.getConcernDeviceFromUserDevice(map);
	}
	
	/**
	 * 删除 用户 
	 */
	public boolean deleteUser(HfUser loginUser,final Long userId){
		Long concernUserId = loginUser.getId();
		//根据关注人和被关注人查询  关注人和code关系 
		List<HfConcernDevice> listCd = getConcernDeviceFromUserDevice(loginUser.getId(),userId);
		if(listCd==null || listCd.size()==0){//无设备关联关系
			HfConcernUser cu = concernUserDao.findOneByUserIdAndConcernUserId(userId, concernUserId);
			if(cu!=null){
				concernUserDao.delete(cu); //删除关注关系
				//查询该人是否还有其他关注关系
				Long count = concernUserDao.count(new Specification<HfConcernUser>() {
					@Override
					public Predicate toPredicate(Root<HfConcernUser> root,
							CriteriaQuery<?> query, CriteriaBuilder cb) {
						query.where(cb.equal(root.get("userId"), userId));
						return query.getRestriction();
					}
				});
				if(count==null || count==0){ //无关注关系删除该用户
					userDao.delete(userId);
				}
			}
		}else{//有设备关联关系
			for(HfConcernDevice d:listCd){
				final String code = d.getCode();
				concernDeviceDao.delete(d.getId()); //删除微信设备绑定 
				//查询该设备是否还有其他人关注
				Long count = concernDeviceDao.count(new Specification<HfConcernDevice>() {
					@Override
					public Predicate toPredicate(Root<HfConcernDevice> root,
							CriteriaQuery<?> query, CriteriaBuilder cb) {
						query.where(cb.equal(root.get("code"), code));
						return query.getRestriction();
					}
				});
				if(count==null || count==0){ //无人关注此设备
					HfDevice now = deviceDao.findOneByCodeAndDeleteFlag(code, 0);
					deviceDao.delete(now);//删除设备
					BoundResponse req = boundService.delBound(d.getCode());//删除设备并与第三方解绑
				}
			}
			HfConcernUser cu = concernUserDao.findOneByUserIdAndConcernUserId(userId, concernUserId);
			if(cu!=null){
				concernUserDao.delete(cu); //删除关注关系
				//查询该人是否还有其他关注关系
				Long count = concernUserDao.count(new Specification<HfConcernUser>() {
					@Override
					public Predicate toPredicate(Root<HfConcernUser> root,
							CriteriaQuery<?> query, CriteriaBuilder cb) {
						query.where(cb.equal(root.get("userId"), userId));
						return query.getRestriction();
					}
				});
				if(count==null || count==0){ //无关注关系删除该用户
					userDao.delete(userId);
				}
			}
		}
		return true;
	}
	
	
	/**
	 * 根据nickName 查询
	 */
	public HfUser getUserByNickName(String nickName){
		return userDao.findOneByNickName(nickName);
	}
	
}
