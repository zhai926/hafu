package com.hafu.repository.jpa;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hafu.entity.HfSportData;

/**
 * 运动数据dao
 *
 */
public interface HfSportDataDao extends JpaSpecificationExecutor<HfSportData>,
	PagingAndSortingRepository<HfSportData, Long>{

}
