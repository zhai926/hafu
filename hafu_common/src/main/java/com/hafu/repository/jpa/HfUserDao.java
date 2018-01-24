package com.hafu.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hafu.entity.HfUser;

/**
 * 用户dao
 *
 */
public interface HfUserDao extends JpaSpecificationExecutor<HfUser>,
	PagingAndSortingRepository<HfUser, Long>{
	/**
	 * 根据用户名 或者手机号 获得用户 
	 */
	public List<HfUser> findByNameOrMobile(String str,String str1);
	
	/**
	 * 根据电话获得用户
	 */
	public List<HfUser> findByMobile(String phone);
	/**
	 * 根据电话并且id不等于获得
	 * @return
	 */
	public List<HfUser> findByMobileAndIdNot(String mobilePhone,Long id);
	/**
	 * 根据openid 获得用户
	 * @param openId
	 * @return
	 */
	HfUser findByOpenId(String openId);
	
	/**
	 * 根据 card 获取持有人信息
	 */
	HfUser findOneByCardAndDeleteFlag(String card,Integer deleteFlag);
	
	/**
	 * 根据昵称查找用户，临时
	 */
	HfUser findOneByNickName(String nickName);
}
