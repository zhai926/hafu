package com.hafu.repository.jpa;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hafu.entity.HfTrack;

/**
 * 运动轨迹dao
 *
 */
public interface HfTrackDao extends JpaSpecificationExecutor<HfTrack>,
	PagingAndSortingRepository<HfTrack, Long>{
	
}
