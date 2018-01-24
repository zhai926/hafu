package com.hafu.repository.mybatis;

import java.util.List;
import java.util.Map;

import com.hafu.dto.UserDto;
import com.hafu.entity.HfConcernDevice;
import com.hafu.entity.HfDevice;

/**
 * 设备查询实现
 * @author Administrator
 *
 */
@MyBatisRepository
public interface HfDeviceMybatisDao {
	
	/**
	 * 我的设备列表 （以及设备所有人信息）
	 * 注意我 指的是关注人  （）
	 */
	public List<UserDto> myAttentitonDeviceList(Map map);
	
	
	/**
	 * 我的设备列表 
	 * 我指的是设备拥有者  
	 */
	public List<HfDevice> myDeviceList(Long userId);
	
	
	/**
	 * 查询该设备是否属于该人关注的人的设备
	 */
	public HfDevice findBydeviceIdAndAttenUser(Map map);
	
	/**
	 * 根据用户id获取用户信息和最新设备
	 */
	public UserDto userAndIsLastDevice(Map map);
	
	/**
	 * 根据code 获得设备和设备人信息
	 */
	public UserDto getDeviceAndUserByCode(String code);
	
	
	/**
	 * 根据关注人id  和  持有人id  查询 关注人和设备关系 
	 */
	public List<HfConcernDevice> getConcernDeviceFromUserDevice(Map map);
	
	/**
	 * 我的设备列表： 且设备与当前人有关注关系的设备
	 */
	public List<HfDevice> myDeviceListConcern(Map map);
	/**
	 * 获取所有用户信息和最新设备
	 */
	public List<HfDevice> findAllDevice();
	
	
}
