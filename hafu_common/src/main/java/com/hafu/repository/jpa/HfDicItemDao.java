package com.hafu.repository.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hafu.entity.HfDicItem;

/**
 * 数据字典子表
 */
public interface HfDicItemDao extends JpaSpecificationExecutor<HfDicItem>,
	PagingAndSortingRepository<HfDicItem, Long>{
	
	List<HfDicItem> findByDicCodeAndDeleteFlag(String code,Integer deleteFlag);
	
}
