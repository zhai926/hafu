package com.hafu.repository.mybatis;

import java.util.List;
import java.util.Map;

import com.hafu.entity.HfPrompt;

@MyBatisRepository
public interface HfPromptMybatisDao {
	public List<HfPrompt> listPrompt(Map<String, Object> params);
}
