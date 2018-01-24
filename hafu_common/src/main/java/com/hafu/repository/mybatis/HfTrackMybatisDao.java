package com.hafu.repository.mybatis;

import java.util.Date;

import com.hafu.entity.HfTrack;

/**
 * 运动轨迹dao
 *
 */
@MyBatisRepository
public interface HfTrackMybatisDao {
	//鼓励列表
	//public HfTrack findHfTrack(String time,Date date,String code);
	
	
	public int getTrackTotalNumberGroupByCode();
	
}
