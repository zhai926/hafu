package com.hafu.repository.jpa;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hafu.entity.HfVoice;

public interface HfVoiceDao extends JpaSpecificationExecutor<HfVoice>,
		PagingAndSortingRepository<HfVoice, Long>{

}
