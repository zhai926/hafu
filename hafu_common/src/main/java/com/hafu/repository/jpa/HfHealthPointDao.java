package com.hafu.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hafu.entity.HfHealthPoint;

/**
 * 健康评分dao
 *
 */
public interface HfHealthPointDao extends JpaSpecificationExecutor<HfHealthPoint>,
	PagingAndSortingRepository<HfHealthPoint, Long>{
	
	//最新评分
	List<HfHealthPoint> findByUserIdAndDeleteFlag(Long userId,Integer deleteFlag,Pageable page);
}
