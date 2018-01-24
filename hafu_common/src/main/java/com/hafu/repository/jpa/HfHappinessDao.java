package com.hafu.repository.jpa;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hafu.entity.HfBehaviorRecord;
import com.hafu.entity.HfHappiness;

public interface HfHappinessDao extends JpaSpecificationExecutor<HfHappiness>,
PagingAndSortingRepository<HfHappiness, Long> {

}
