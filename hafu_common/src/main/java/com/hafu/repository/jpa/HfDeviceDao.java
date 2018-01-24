package com.hafu.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hafu.entity.HfDevice;
/**
 * 设备表（外设表）dao
 */
public interface HfDeviceDao extends JpaSpecificationExecutor<HfDevice>,
	PagingAndSortingRepository<HfDevice, Long>{
	//根据code 获取
	public HfDevice findOneByCodeAndDeleteFlag(String code , Integer deleteFlag);
	
	//根据id获取 
	HfDevice findOneByIdAndDeleteFlag(Long id,Integer deleteFlag);
	
	//根据userId 获取记录 
	List<HfDevice> findByUserIdAndDeleteFlag(Long userId,Integer deleteFlag);
}
