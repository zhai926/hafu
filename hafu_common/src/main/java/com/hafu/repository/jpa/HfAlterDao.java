package com.hafu.repository.jpa;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hafu.entity.HfAlert;

/**
 * 报警表实现dao
 */
public interface HfAlterDao extends JpaSpecificationExecutor<HfAlert>,
	PagingAndSortingRepository<HfAlert, Long>{
	
	/**
	 * 最新的抱紧记录
	 */
	List<HfAlert> findByuserIdAndDeleteFlag(Long userId,Integer deleteFlag,Pageable page);
	
}
