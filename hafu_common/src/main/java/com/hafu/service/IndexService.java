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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;

import com.hafu.dto.UserContainDeviceDto;
import com.hafu.dto.UserDto;
import com.hafu.eban.entity.BoundParam;
import com.hafu.eban.entity.response.BoundResponse;
import com.hafu.eban.entity.response.ContactsResponse;
import com.hafu.eban.entity.response.ContactsResponse_list;
import com.hafu.eban.service.BoundService;
import com.hafu.eban.service.ContactsService;
import com.hafu.entity.HfAlert;
import com.hafu.entity.HfCheckData;
import com.hafu.entity.HfConcernDevice;
import com.hafu.entity.HfConcernUser;
import com.hafu.entity.HfDevice;
import com.hafu.entity.HfDicItem;
import com.hafu.entity.HfModuleSettings;
import com.hafu.entity.HfSportData;
import com.hafu.entity.HfUser;

import com.hafu.repository.jpa.HfAlterDao;
import com.hafu.repository.jpa.HfConcernDeviceDao;
import com.hafu.repository.jpa.HfConcernUserDao;
import com.hafu.repository.jpa.HfDeviceDao;
import com.hafu.repository.jpa.HfDicItemDao;
import com.hafu.repository.jpa.HfModuleSettingsDao;
import com.hafu.repository.jpa.HfSportDataDao;
import com.hafu.repository.jpa.HfUserDao;

import com.hafu.repository.mybatis.HfCheckDataMybatisDao;
import com.hafu.repository.mybatis.HfDeviceMybatisDao;
import com.hafu.repository.mybatis.UserMybatisDao;

@Service
public class IndexService {
	@Resource
	private HfCheckDataMybatisDao checkMybatisDao;
	@Resource
	private HfSportDataDao sportDataDao;
	@Resource
	private UserMybatisDao userMybatisDao;
	@Resource
	private HfDeviceMybatisDao deviceMybatisDao;
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
	private ContactsService contactsService;
	@Resource
	private HfModuleSettingsDao settingDao;
	@Resource
	private HfDicItemDao dicItemDao;
	/**
	 * 首页头像用户与最新关注人的关系
	 * @return
	 */
	public HfConcernUser getConcernUser(Long userId,Long concernUserId){
		return concernUserDao.findOneByUserIdAndConcernUserId(userId, concernUserId);
	}
	/**
	 * 首页最新的运动数据 
	 */
	public HfSportData indexLastSport(final Long userId){
		Page<HfSportData> data = sportDataDao.findAll(new Specification<HfSportData>() {
			@Override
			public Predicate toPredicate(Root<HfSportData> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				list.add(cb.equal(root.get("userId"), userId));
				list.add(cb.equal(root.get("deleteFlag"), 0));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		},new PageRequest(0, 1, new Sort(Direction.DESC,"sportDate")));
		if(data!=null && data.getTotalElements()>0){
			return data.getContent().get(0);
		}
		return null;
	}
	
	/**
	 * 首页最新的检测数据(健康书籍，身体数据 和环境数据)
	 */
	public List<HfCheckData> indexLastCheck(Long userId){
		Map<String,Object> map = new HashMap<String, Object>();		
		/*SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date dateL=new Date();
		String dateStr = sdf.format(dateL);
		map.put("checkDate", dateStr);*/
		map.put("userId", userId);			
		List<HfCheckData> data = checkMybatisDao.indexLastCheck(map);
		return data;
	}
	
	/**
	 * 首页 我 绑定的人 (必须保护设备)
	 */
	public List<UserDto> indexBingDevice(Long userId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		List<UserDto> l = userMybatisDao.mybangUser(map);
		return l;
	}
	
	/**
	 * 首页 我 绑定持有人
	 */
	public List<UserDto> myBangHolder(Long userId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		List<UserDto> l = userMybatisDao.mybangHolder(map);
		return l;
	}
	
	/**
	 * 设备列表 
	 */
	public List<UserDto> deviceList(Map map){
		return deviceMybatisDao.myAttentitonDeviceList(map);
	}
	
	/**
	 * 使用人列表 
	 */
	public List<UserContainDeviceDto> deviceUserList(Map map){
		return userMybatisDao.deviceUserList(map);
	}
	
	/**
	 * 报警记录
	 * @param userId
	 */
	public List<HfAlert> userAlertList(final Map map){
		return alterDao.findAll(new Specification<HfAlert>() {
			@Override
			public Predicate toPredicate(Root<HfAlert> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				//list.add(cb.equal(root.get("userId"), map.get("userId")));
				list.add(cb.equal(root.get("code"), map.get("code")));
				list.add(cb.equal(root.get("deleteFlag"), 0));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		});
	}
	
	/**
	 * 当前人 设备 
	 */
	public HfDevice currentUserDevice(Long currentUserId){
		List<HfDevice> list = deviceMybatisDao.myDeviceList(currentUserId);
		if(!CollectionUtils.isEmpty(list)){
			return list.get(0);
		}
		return null;
	}
	
	
	/**
	 * 删除设备 
	 * 查询该设备是否属于该人关注的人的设备
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public boolean deleteDevice(Long deviceId,Long concernUserId){
		Map<String, Object> map = new HashMap<String, Object>();
		//map.put("deviceId", deviceId);
		//map.put("concernUserId", concernUserId);
		//根据deviceid获取设备
		final HfDevice d = deviceDao.findOneByIdAndDeleteFlag(deviceId,0);
		if(d!=null){
			//获取关注人和设备关系
			HfConcernDevice cd = concernDeviceDao.findOneByConcernUserIdAndCode(concernUserId, d.getCode());
			if(cd!=null){
				//删除关注人和设备关系
				concernDeviceDao.delete(cd);
				//查询该code是否还与其他人有关联关系，如果有，不操作，如果没有了，需要把该设备与第三方解绑 
				Long count = concernDeviceDao.count(new Specification<HfConcernDevice>() {
					@Override
					public Predicate toPredicate(Root<HfConcernDevice> root,
							CriteriaQuery<?> query, CriteriaBuilder cb) {
						query.where(cb.equal(root.get("code"), d.getCode()));
						return query.getRestriction();
					}
				});
				if(count==null || count==0){
					BoundResponse req = boundService.delBound(d.getCode());
					deviceDao.delete(deviceId);
					/*if(req!=null && req.getSuccess()!=null && req.getSuccess().equals("1")){
						deviceDao.delete(deviceId);
					}else{
						return false;
					}*/
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 新增持有人信息
	 * (根据新版注册页面做的功能新增，新版注册页面添加了设备绑定，如果用户输入了设备号就调用此方法。)
	 * @param user
	 * @param loginUser
	 * @param device
	 * @author 段鬻
	 * 2016年11月22日
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public String addHolder(HfUser user , HfUser loginUser, HfDevice device, String relations){
		try{
			//根据card 判断持有人是否已经存在 
			HfUser cardUser = userDao.findOneByCardAndDeleteFlag(user.getCard(),0);
			if(cardUser==null){
				user.setDeleteFlag(0);
				user.setCreateDate(new Date());
				userDao.save(user); //保存持有人
				examinationSevice.handlerCheckData(user.getId(), user.getCard());
				
				//新增用户关系
				HfConcernUser ud = new HfConcernUser();
				ud.setCreateDate(new Date());
				ud.setUserId(user.getId());
				ud.setConcernUserId(loginUser.getId());
				ud.setRelations(relations);
				ud.setNickName(user.getNickName());
				concernUserDao.save(ud);
				device.setUserId(user.getId());
				addFirstDevice(loginUser, user, device);
			}else{ //身份证存在，覆盖原有信息
				//需要先判断该人是否已经关注过该人 （按住身份证判断）
				HfConcernUser oldCu = concernUserDao.findOneByUserIdAndConcernUserId(cardUser.getId(),loginUser.getId());
				if(oldCu!=null){
					return "您已经关注过该身份证，请不要重复添加！";
				}
				
				cardUser.setRealName(user.getRealName());
				cardUser.setGender(user.getGender());
				cardUser.setAge(user.getAge());
				cardUser.setBirthday(user.getBirthday());
				cardUser.setMobile(user.getMobile());
				cardUser.setProvince(user.getProvince());
				cardUser.setCity(user.getCity());
				cardUser.setArea(user.getArea());
				cardUser.setAddress(user.getAddress());
				cardUser.setContactPerson(user.getContactPerson());
				userDao.save(cardUser);
				//查找该微信用户是否已经和该设备持有人建立过关系hf_concern_user
//				if(oldCu==null){此处判断多此一举故注释掉
					//新增用户关系
					HfConcernUser ud = new HfConcernUser();
					ud.setCreateDate(new Date());
					ud.setUserId(cardUser.getId());
					ud.setConcernUserId(loginUser.getId());
					ud.setRelations(relations);
					ud.setNickName(user.getNickName());
					concernUserDao.save(ud);
					device.setUserId(user.getId());
					addFirstDevice(loginUser, cardUser, device);
//				}
			}
		}catch(Exception e){
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return "操作失败！";
		}
		return "";
	}
	
	/**
	 * 新增持有人信息 
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public String addHolder(HfUser user , HfUser loginUser, String relations){
		try{
			//根据card 判断持有人是否已经存在 
			HfUser cardUser = userDao.findOneByCardAndDeleteFlag(user.getCard(),0);
			if(cardUser==null){
				user.setDeleteFlag(0);
				user.setCreateDate(new Date());
				userDao.save(user); //保存持有人
				examinationSevice.handlerCheckData(user.getId(), user.getCard());
				//新增用户关系
				HfConcernUser ud = new HfConcernUser();
				ud.setCreateDate(new Date());
				ud.setUserId(user.getId());
				ud.setConcernUserId(loginUser.getId());
				ud.setRelations(relations);
				ud.setNickName(user.getNickName());
				concernUserDao.save(ud);
			}else{ //身份证存在，覆盖原有信息
				//需要先判断该人是否已经关注过该人 （按住身份证判断）
				HfConcernUser oldCu = concernUserDao.findOneByUserIdAndConcernUserId(cardUser.getId(),loginUser.getId());
				if(oldCu!=null){
					return "您已经关注过该身份证，请不要重复添加！";
				}
				
				cardUser.setRealName(user.getRealName());
				cardUser.setGender(user.getGender());
				cardUser.setAge(user.getAge());
				cardUser.setBirthday(user.getBirthday());
				cardUser.setMobile(user.getMobile());
				cardUser.setProvince(user.getProvince());
				cardUser.setCity(user.getCity());
				cardUser.setArea(user.getArea());
				cardUser.setAddress(user.getAddress());
				cardUser.setContactPerson(user.getContactPerson());
				userDao.save(cardUser);
				//查找该微信用户是否已经和该设备持有人建立过关系hf_concern_user
				if(oldCu==null){
					//新增用户关系
					HfConcernUser ud = new HfConcernUser();
					ud.setCreateDate(new Date());
					ud.setUserId(cardUser.getId());
					ud.setConcernUserId(loginUser.getId());
					ud.setRelations(relations);
					ud.setNickName(user.getNickName());
					concernUserDao.save(ud);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return "操作失败！";
		}
		return "";
	}
	/**
	 * 首次添加设备（临时实现后面要改）
	 * @param login
	 * @param holder
	 * @param device
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public String addFirstDevice(HfUser login,HfUser holder,HfDevice device){
		try{
			//根据code判断设备是否已经存在 
			HfDevice isde = deviceDao.findOneByCodeAndDeleteFlag(device.getCode(), 0);
			if(isde!=null){
				//设备号已经存在 -- 查找该设备号持有人 ，使微信用户和原持有人绑定,并和设备建立关系
				HfUser nowHolder = userDao.findOne(isde.getUserId());
				//nowHolder.setCard(holder.getCard());
				//userDao.save(nowHolder); //修改原
				//HfConcernUser oldUd = concernUserDao.findOneByUserIdAndConcernUserId(holder.getId(),login.getId()); //修改绑定
				//oldUd.setUserId(nowHolder.getId());
				//concernUserDao.save(oldUd);
				//userDao.delete(holder.getId());//删除现在人
				//查找该微信用户是否已经和该设备持有人建立过关系hf_concern_user
				HfConcernUser oldCu = concernUserDao.findOneByUserIdAndConcernUserId(nowHolder.getId(),login.getId());
				if(oldCu==null){//未建立关系时才新建关系
					HfConcernUser newUd = new HfConcernUser(); 
					newUd.setConcernUserId(login.getId());
					newUd.setUserId(nowHolder.getId());
					newUd.setCreateDate(new Date());
					concernUserDao.save(newUd);//保存新的用户关系
				}
				HfConcernDevice newCd = new HfConcernDevice();
				newCd.setCode(device.getCode());
				newCd.setConcernUserId(login.getId());
				newCd.setCreateDate(new Date());
				concernDeviceDao.save(newCd); //保存新的绑定设备关系
				return "";
			}else{
				//数据库不存在该设备号
				BoundParam boundParam = new BoundParam();
				boundParam.setMobile(holder.getMobile());
				boundParam.setGender(holder.getGender());
				boundParam.setName(holder.getRealName());
				boundParam.setCode(device.getCode());
				BoundResponse boundResponse = boundService.bound(boundParam); //去第三方绑定设备
				if(boundResponse.getSuccess().equals("1")){ //第三方绑定成功
					deviceDao.save(device);//保存设备 
					addThirdContact(device.getCode(), holder.getContactPerson());
					/*HfConcernUser newUd = new HfConcernUser(); 
					newUd.setConcernUserId(login.getId());
					newUd.setUserId(holder.getId());
					newUd.setCreateDate(new Date());
					concernUserDao.save(newUd);//保存新的用户关系
*/					HfConcernDevice newCd = new HfConcernDevice();
					newCd.setCode(device.getCode());
					newCd.setConcernUserId(login.getId());
					newCd.setCreateDate(new Date());
					concernDeviceDao.save(newCd); //保存新的绑定设备关系
				}else{//绑定不成功有两种情况 ：1.设备在第三方已绑定（我们表里并不存在）， 2.设备不存在
					String msg = boundResponse.getMessage();
					if(StringUtils.isNotEmpty(msg) && "该设备号已被绑定".equals(msg)){ //设备在第三方已绑定（我们表里并不存在）
						BoundParam bpedit = new BoundParam();
						bpedit.setMobile(holder.getMobile());
						bpedit.setGender(holder.getGender());
						bpedit.setName(holder.getRealName());
						bpedit.setNickname(holder.getRealName());
						bpedit.setCode(device.getCode());
						BoundResponse editres = boundService.editBound(bpedit); //更新第三方设备信息
						if("1".equals(editres.getSuccess())){ //修改第三方成功
							deviceDao.save(device); //直接新建设备关系
							/*HfConcernUser newUd = new HfConcernUser(); 
							newUd.setConcernUserId(login.getId());
							newUd.setUserId(holder.getId());
							newUd.setCreateDate(new Date());
							concernUserDao.save(newUd);//保存新的用户关系
*/							HfConcernDevice newCd = new HfConcernDevice();
							newCd.setCode(device.getCode());
							newCd.setConcernUserId(login.getId());
							newCd.setCreateDate(new Date());
							concernDeviceDao.save(newCd); //保存新的绑定设备关系
							return "";
						}else{
							return "系统错误，请稍后再试!";
						}
					}else{
						return boundResponse.getMessage();
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return "操作失败！";
		}
		return "";
	}
	
	/**
	 * 非首次添加新增设备
	 * 判断当前用户是否已经绑定过该设备 
	 * 其他与首次绑定类似
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public String addDevice(HfUser login,HfUser holder,HfDevice device){
		try{
			HfConcernDevice iscd = concernDeviceDao.findOneByConcernUserIdAndCode(login.getId(),device.getCode());
			if(iscd != null){//微信用户绑定设备 ，无需再绑定 
				return "您已经绑定过该设备，请不要重复绑定！";
			}
			//根据code判断设备是否已经存在 
			HfDevice isde = deviceDao.findOneByCodeAndDeleteFlag(device.getCode(), 0);
			if(isde!=null){
				//更新该设备用于前台展示（因为我返回的device）
				device.setId(isde.getId()); 
				device.setCreateDate(isde.getCreateDate());
				device.setUserId(isde.getUserId());
				//设备号已经存在 -- 查找该设备号持有人 ，使微信用户和原持有人绑定（以前未存在绑定关系时才做此操作）,并和设备建立关系
				HfUser nowHolder = userDao.findOne(isde.getUserId());
				//nowHolder.setCard(holder.getCard());
				//userDao.save(nowHolder); //修改原
				//HfConcernUser oldUd = concernUserDao.findOneByUserIdAndConcernUserId(holder.getId(),login.getId()); //修改绑定
				//oldUd.setUserId(nowHolder.getId());
				//concernUserDao.save(oldUd);
				//userDao.delete(holder.getId());//删除现在人
				//查找该微信用户是否已经和该设备持有人建立过关系hf_concern_user
				HfConcernUser oldCu = concernUserDao.findOneByUserIdAndConcernUserId(nowHolder.getId(),login.getId());
				if(oldCu==null){//未建立关系时才新建关系
					HfConcernUser newUd = new HfConcernUser(); 
					newUd.setConcernUserId(login.getId());
					newUd.setUserId(nowHolder.getId());
					newUd.setCreateDate(new Date());
					concernUserDao.save(newUd);//保存新的用户关系
				}
				HfConcernDevice newCd = new HfConcernDevice();
				newCd.setCode(device.getCode());
				newCd.setConcernUserId(login.getId());
				newCd.setCreateDate(new Date());
				concernDeviceDao.save(newCd); //保存新的绑定设备关系
				return "";
			}else{
				//数据库不存在该设备号
				BoundParam boundParam = new BoundParam();
				boundParam.setMobile(holder.getMobile());
				boundParam.setGender(holder.getGender());
				boundParam.setName(holder.getRealName());
				boundParam.setCode(device.getCode());
				BoundResponse boundResponse = boundService.bound(boundParam); //去第三方绑定设备
				if(boundResponse.getSuccess().equals("1")){ //第三方绑定成功
					deviceDao.save(device);//保存设备 
					addThirdContact(device.getCode(), holder.getContactPerson());
					/*HfConcernUser newUd = new HfConcernUser(); 
					newUd.setConcernUserId(login.getId());
					newUd.setUserId(holder.getId());
					newUd.setCreateDate(new Date());
					concernUserDao.save(newUd);//保存新的用户关系
*/					HfConcernDevice newCd = new HfConcernDevice();
					newCd.setCode(device.getCode());
					newCd.setConcernUserId(login.getId());
					newCd.setCreateDate(new Date());
					concernDeviceDao.save(newCd); //保存新的绑定设备关系
				}else{//绑定不成功有两种情况 ：1.设备在第三方已绑定（我们表里并不存在）， 2.设备不存在
					String msg = boundResponse.getMessage();
					if(StringUtils.isNotEmpty(msg) && "该设备号已被绑定".equals(msg)){ //设备在第三方已绑定（我们表里并不存在）
						BoundParam bpedit = new BoundParam();
						bpedit.setMobile(holder.getMobile());
						bpedit.setGender(holder.getGender());
						bpedit.setName(holder.getRealName());
						bpedit.setNickname(holder.getRealName());
						bpedit.setCode(device.getCode());
						BoundResponse editres = boundService.editBound(bpedit); //更新第三方设备信息
						if("1".equals(editres.getSuccess())){ //修改第三方成功
							deviceDao.save(device); //直接新建设备关系
							/*HfConcernUser newUd = new HfConcernUser(); 
							newUd.setConcernUserId(login.getId());
							newUd.setUserId(holder.getId());
							newUd.setCreateDate(new Date());
							concernUserDao.save(newUd);//保存新的用户关系
*/							HfConcernDevice newCd = new HfConcernDevice();
							newCd.setCode(device.getCode());
							newCd.setConcernUserId(login.getId());
							newCd.setCreateDate(new Date());
							concernDeviceDao.save(newCd); //保存新的绑定设备关系
							return "";
						}else{
							return "系统错误，请稍后再试!";
						}
					}else{
						return boundResponse.getMessage();
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return "系统错误！";
		}
		return "";
	}
	/**
	 * 修改设备持有人信息
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public String updateUserInfo(HfUser user,HfUser loginUser){
		try{
			//修改身份证判断
			HfUser cardUser = userDao.findOneByCardAndDeleteFlag(user.getCard(), 0);
			if(cardUser!=null && cardUser.getId().intValue()!=user.getId().intValue()){
				return "身份证号已被其他人使用！";
			}else{
				HfUser u = userDao.findOne(user.getId()); //数据库
				u.setNickName(user.getNickName());
				u.setRealName(user.getRealName());
				u.setCard(user.getCard());
				u.setGender(user.getGender());
				u.setBirthday(user.getBirthday());
				u.setAge(user.getAge());
				u.setMobile(user.getMobile());
				u.setProvince(user.getProvince());
				u.setCity(user.getCity());
				u.setArea(user.getArea());
				u.setAddress(user.getAddress());
				u.setContactPerson(user.getContactPerson());
				userDao.save(u); 
				HfConcernUser cu  = concernUserDao.findOneByUserIdAndConcernUserId(u.getId(), loginUser.getId());
				cu.setNickName(u.getNickName());
				concernUserDao.save(cu);
				//更新第三方设备绑定和联系人绑定 
				//查询该成所有设备
				List<HfDevice> dl = deviceMybatisDao.myDeviceList(u.getId());
				if(!CollectionUtils.isEmpty(dl)){
					for(HfDevice d : dl){
						//更新三方绑定
						BoundParam boundParam = new BoundParam();
						boundParam.setMobile(u.getMobile());
						boundParam.setGender(u.getGender());
						boundParam.setName(u.getRealName());
						boundParam.setCode(d.getCode());
						boundService.editBound(boundParam);
						//更新三方紧急人
						addThirdContact(d.getCode(), u.getContactPerson());
					}
				}
				
				
			}
		}catch(Exception e){
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return "操作失败！";
		}
		return "";
	}
	/**
	 * 查询某人的最近的一条报警记录
	 */
	public HfAlert lastAlert(Long userId){
		List<HfAlert> al = alterDao.findByuserIdAndDeleteFlag(userId,0,new PageRequest(1, 1,new Sort(Direction.DESC, "alertDate")));
		if(CollectionUtils.isEmpty(al)){
			return null;
		}
		return al.get(0);
	}
	
	/**
	 * 根据id 查询用户信息包含用户设备 
	 */
	public UserContainDeviceDto getUserInfoById(Map map){
		return userMybatisDao.getUserInfoById(map);
	}
	
	/**
	 * 根据code获得设备 
	 */
	public HfDevice getDeviceByCode(String code){
		return deviceDao.findOneByCodeAndDeleteFlag(code,0);
	}
	
	
	/**
	 * 绑定用户关系
	 */
	public void bindingUserAndUser(HfUser lu , UserDto cu){
		HfConcernUser ud = new HfConcernUser();
		ud.setUserId(cu.getId());
		ud.setConcernUserId(lu.getId());
		ud.setCreateDate(new Date());
		concernUserDao.save(ud);
	}
	/**
	 * 根据code 获得设备和设备持有人信息
	 */
	public UserDto getDeviceAndUserByCode(String code){
		return deviceMybatisDao.getDeviceAndUserByCode(code);
	}
	/**
	 * 读取报警记录
	 */
	public void readAlter(final Long userId,final Long alterId,final String code){
		HfAlert alter = alterDao.findOne(new Specification<HfAlert>() {
			@Override
			public Predicate toPredicate(Root<HfAlert> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				//list.add(cb.equal(root.get("userId"), userId));
				list.add(cb.equal(root.get("code"), code));
				list.add(cb.equal(root.get("deleteFlag"), 0));
				list.add(cb.equal(root.get("id"), alterId));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		});
		if(alter!=null){
			alter.setReadStatus(1);
			alterDao.save(alter);
		}
	}
	
	/**
	 * 获取最新的未读报警记录
	 */
	public HfAlert noreadAlter(final Map map){
		Page<HfAlert> list=  alterDao.findAll(new Specification<HfAlert>() {
			@Override
			public Predicate toPredicate(Root<HfAlert> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				//list.add(cb.equal(root.get("userId"), map.get("userId")));
				list.add(cb.equal(root.get("code"), map.get("code")));
				list.add(cb.equal(root.get("readStatus"), map.get("readStatus")));
				list.add(cb.equal(root.get("deleteFlag"), 0));
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		},new PageRequest(0, 1,new Sort(Direction.DESC,"alertDate")));
		if(list!=null && list.getContent()!=null && list.getContent().size()>0){
			return list.getContent().get(0);
		}
		return null;
	}
	
	/**
	 * 第三方 添加紧急联系人 
	 */
	private void addThirdContact(String code,String contact){
		ContactsResponse res = contactsService.getContacts(code);
		if(StringUtils.isNotEmpty(contact)){
			if(res.getSuccess()!=null && res.getSuccess().intValue()==1){
				List<ContactsResponse_list> li = res.getList();
				if(!CollectionUtils.isEmpty(li)){
					JSONArray ar = JSONArray.fromObject(contact);
					for(ContactsResponse_list no:li){
						boolean fl = false;
						String name = null;
						String phone = null;
						for(int i=0;i<ar.size();i++){
							JSONObject o = (JSONObject) ar.get(i);
							name = o.getString("name");
							phone = o.getString("phone");
							if(phone.equals(no.getMobile())){
								fl = true;
								break;
							}
						}
						if(!fl){//本地没找到，删除绑定
							contactsService.delContact(code, no.getId());
						}else{ //本地找到，做修改
							contactsService.editContact(code, no.getId(), name, phone);
						}
					}
					for(int i=0;i<ar.size();i++){
						JSONObject o = (JSONObject) ar.get(i);
						boolean fl = false;
						String name = o.getString("name");
						String phone = o.getString("phone");
						for(ContactsResponse_list no:li){
							if(phone.equals(no.getMobile())){
								fl = true;
								break;
							}
						}
						if(!fl){ //远程没找到，添加
							contactsService.addContacts(code, name, phone);
						}
					}
				}else{
					JSONArray ar = JSONArray.fromObject(contact);
					for(int i=0;i<ar.size();i++){
						JSONObject o = (JSONObject) ar.get(i);
						String name = o.getString("name");
						String phone = o.getString("phone");
						contactsService.addContacts(code, name, phone);
					}
				}
			}
		}
		
	}
	
	/**
	 * 是否可以为当前持有人绑定 code 
	 */
	public Map<String, Object> isAgreeBand(HfUser login , Long holderId,String code){
		Map<String, Object> res = new HashMap<String, Object>();
		List<HfDevice> de_1 = deviceDao.findByUserIdAndDeleteFlag(holderId,0);
		if(!CollectionUtils.isEmpty(de_1)){
			//当前持有人有绑定设备，要判定 当前是是否和这个绑定的设备是否有关联关系，如果有，则判定当前持有人已经绑定过设备，否则无
			HfConcernDevice cd = concernDeviceDao.findOneByConcernUserIdAndCode(login.getId(), de_1.get(0).getCode());
			if(cd==null){//无关注，说明当前微信用户还没有为当前持有人绑定设备,但是别的微信用户为当前持有人绑定过，提示同步
				res.put("flag", "1");
				res.put("ob",de_1.get(0));
			}else{ //已绑定了设备 ，不可以绑定多个
				res.put("flag", "2"); 
			}
		}else{//持有人无设备
			res.put("flag", "0"); 
		}
		return res;
	}
	
	/**
	 * 获取 首页设置
	 * @param concernUserId
	 */
	public List<HfModuleSettings> getModuleSettings(Long concernUserId){
		return settingDao.findByConcernUserId(concernUserId);
	}
	
	/**
	 * 获取数据显示字典
	 */
	public List<HfDicItem> getDicItemByDicCode(String dicCode){
		return dicItemDao.findByDicCodeAndDeleteFlag(dicCode, 0);
	}
	
	/**
	 * 保存 首页显示设置
	 */
	@Transactional(propagation=Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
	public void saveModuleSetting(Long concernUserId,String[] module){
		List<HfDicItem> item = dicItemDao.findByDicCodeAndDeleteFlag("INDEX_MODULE", 0);
		if(item!=null && item.size()>0){
			for(HfDicItem i : item){
				boolean flag = false;
				for(String m : module){
					if(m.equals(i.getDicItemCode())){
						flag=true;//确认合法
						break;
					}
				}
				HfModuleSettings del = settingDao.findByConcernUserIdAndModuleCode(concernUserId,i.getDicItemCode());
				if(!flag){ //设置了不显示,执行删除
					if(del!=null){
						settingDao.delete(del);
					}
				}else{
					if(del==null){
						del = new HfModuleSettings();
						del.setConcernUserId(concernUserId);
						del.setModuleCode(i.getDicItemCode());
						del.setCreateDate(new Date());
						settingDao.save(del);
					}else{
						del.setCreateDate(new Date());
						settingDao.save(del);
					}
				}
			}
		}
	}
	
}
