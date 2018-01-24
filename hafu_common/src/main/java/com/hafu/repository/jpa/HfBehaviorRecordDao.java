package com.hafu.repository.jpa;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hafu.entity.HfBehaviorRecord;

/**
 * 行为记录dao
 */
public interface HfBehaviorRecordDao extends JpaSpecificationExecutor<HfBehaviorRecord>,
	PagingAndSortingRepository<HfBehaviorRecord, Long>{
	
}
