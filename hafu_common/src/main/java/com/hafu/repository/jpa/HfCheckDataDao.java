package com.hafu.repository.jpa;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hafu.entity.HfCheckData;
/**
 * 检测数据表,包括（健康数据，身体数据，环境数据）
 */
public interface HfCheckDataDao extends JpaSpecificationExecutor<HfCheckData>,
	PagingAndSortingRepository<HfCheckData, Long>{
	
}
