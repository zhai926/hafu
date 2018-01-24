package com.hafu.repository.jpa;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hafu.entity.HfCode;

/**
 * 
 */
public interface HfCodeDao extends JpaSpecificationExecutor<HfCode>,
	PagingAndSortingRepository<HfCode, Long>{
	
	HfCode findOneByCodeAndDeleteFlag(String code,Integer deleteFlag);
	
}
