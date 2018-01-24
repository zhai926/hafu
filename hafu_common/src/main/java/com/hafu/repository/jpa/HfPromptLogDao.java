package com.hafu.repository.jpa;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hafu.entity.HfPromptLog;

/**
 * 
	* <p>Title: HfPropmtLogDao</p>
	* <p>Description: 关爱提醒操作</p>
	* <p>Company: 嘉善网络</p>
	* @author 时志多
	* @date 2016年7月6日上午10:27:08
 */
public interface HfPromptLogDao extends JpaSpecificationExecutor<HfPromptLog>,
		PagingAndSortingRepository<HfPromptLog, Long> {

}
