package com.hafu.repository.jpa;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hafu.entity.HfEncouragement;

/**
 * 鼓励表dao
 */
public interface HfEncouragementDao extends JpaSpecificationExecutor<HfEncouragement>,
	PagingAndSortingRepository<HfEncouragement, Long>{

}
