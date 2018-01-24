package com.hafu.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hafu.entity.HfModuleSettings;

public interface HfModuleSettingsDao extends JpaSpecificationExecutor<HfModuleSettings>,
	PagingAndSortingRepository<HfModuleSettings, Long>{
	
	
	List<HfModuleSettings> findByConcernUserId(Long userId);

	HfModuleSettings findByConcernUserIdAndModuleCode(Long concernUserId,String moduleCode);
	
}
