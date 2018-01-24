package com.hafu.repository.jpa;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hafu.entity.HfPrompt;

/**
 * 关爱提醒表dao
 *
 */
public interface HfPromptDao extends JpaSpecificationExecutor<HfPrompt>,
	PagingAndSortingRepository<HfPrompt, Long>{

}
