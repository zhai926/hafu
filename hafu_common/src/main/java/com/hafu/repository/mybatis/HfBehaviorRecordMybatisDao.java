package com.hafu.repository.mybatis;

import java.util.List;
import java.util.Map;

import com.hafu.entity.HfBehaviorRecord;

/**
 * 行为记录mybatisdao
 *
 */
@MyBatisRepository
public interface HfBehaviorRecordMybatisDao {
	//行为记录列表
	public List<HfBehaviorRecord> listBehavior(Map<String, Object> map);

	//最新行为
	public HfBehaviorRecord lastBehavior(Map<String,Object> map);
	
	
	//行为记录列表  历史记录列表  
    public List<HfBehaviorRecord> historyBehavior(Map<String, Object> map);

}
