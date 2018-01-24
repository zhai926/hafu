package com.hafu.repository.mybatis;

import java.util.List;
import java.util.Map;

import com.hafu.entity.HfAlert;

@MyBatisRepository
public interface HfAlertMybatisDao {

	public List<HfAlert> listAlert(Map<String, Object> param);
	public HfAlert selectById(Integer id);
}
