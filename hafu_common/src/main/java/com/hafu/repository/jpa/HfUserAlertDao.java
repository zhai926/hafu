package com.hafu.repository.jpa;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hafu.entity.HfUserAlert;

public interface HfUserAlertDao
		extends PagingAndSortingRepository<HfUserAlert, Long>, JpaSpecificationExecutor<HfUserAlert> {

}
