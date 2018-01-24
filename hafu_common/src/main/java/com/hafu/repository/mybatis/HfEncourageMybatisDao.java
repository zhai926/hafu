package com.hafu.repository.mybatis;

import java.util.List;
import java.util.Map;

import com.hafu.entity.HfEncouragement;

/**
 * 鼓励
 *
 */
@MyBatisRepository
public interface HfEncourageMybatisDao {
	//鼓励列表
	public List<HfEncouragement> listEncourage(Map<String, Object> map);
	//获取今日鼓励列表
//	public List<HfEncouragement> todayListEncourage(HfEncouragement hfEncouragement);
	
}
