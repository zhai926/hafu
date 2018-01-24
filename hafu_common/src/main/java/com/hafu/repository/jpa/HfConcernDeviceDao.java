package com.hafu.repository.jpa;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hafu.entity.HfConcernDevice;


/**
 * 添加人和被添加人 信息
 *
 */
public interface HfConcernDeviceDao extends JpaSpecificationExecutor<HfConcernDevice>,
	PagingAndSortingRepository<HfConcernDevice, Long>{
	
	//根据关注人 和 code 获取记录 
	HfConcernDevice findOneByConcernUserIdAndCode(Long concernUserId,String code);
	
}
