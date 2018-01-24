package com.hafu.repository.mybatis;

import java.util.List;
import java.util.Map;

import com.hafu.dto.UserContainDeviceDto;
import com.hafu.dto.UserDto;
import com.hafu.entity.HfUser;
@MyBatisRepository
public interface UserMybatisDao {
	/**
	 * 所有用户
	 */
	public List<HfUser> listAll();
	
	/**
	 * 首页我绑定的设备所有人
	 */
	public List<UserDto> mybangUser(Map map);
	
	/**
	 * 使用人列表(按照关注的人查询)
	 */
	public List<UserContainDeviceDto> deviceUserList(Map map);
	
	/**
	 * 查询用户信息包含设备 
	 */
	public UserContainDeviceDto getUserInfoById(Map map);
	
	/**
	 * 我绑定的持有人 （不包含设备）
	 */
	public List<UserDto> mybangHolder(Map map);
	/**
	 * 获取所有身份证不为空的用户
	 * @return
	 */
	public List<UserDto> getCardId();
	
	public List<UserDto> getWeixinUserConcernUser(String code);
	
	/**
	 * 查找我的关注人 
	 */
	public List<UserDto> getMyConcernUser(Long userId);
	
}
