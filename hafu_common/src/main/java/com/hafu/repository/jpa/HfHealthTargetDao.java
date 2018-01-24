package com.hafu.repository.jpa;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hafu.entity.HfHealthTarget;

/**
 * 健康目标dao
 *
 */
public interface HfHealthTargetDao extends JpaSpecificationExecutor<HfHealthTarget>,
	PagingAndSortingRepository<HfHealthTarget, Long>{
	
	public HfHealthTarget findTop1ByUserIdAndCodeAndDeleteFlagAndDataTypeOrderByIdDesc(Long userId,String code,Integer deleteFlag,Integer dataType);
}
