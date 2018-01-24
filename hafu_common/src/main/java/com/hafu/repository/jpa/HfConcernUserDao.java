package com.hafu.repository.jpa;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hafu.entity.HfConcernUser;


/**
 * 添加人和被添加人 信息
 *
 */
public interface HfConcernUserDao extends JpaSpecificationExecutor<HfConcernUser>,
	PagingAndSortingRepository<HfConcernUser, Long>{
	
	/**
	 * 根据userid 和   concernUserid
	 */
	public HfConcernUser findOneByUserIdAndConcernUserId(Long userId,Long concernUserId);
}
