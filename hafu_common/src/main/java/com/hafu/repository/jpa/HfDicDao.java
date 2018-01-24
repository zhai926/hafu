package com.hafu.repository.jpa;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hafu.entity.HfDic;
/**
 * 数据字典dao
 */
public interface HfDicDao extends JpaSpecificationExecutor<HfDic>,
	PagingAndSortingRepository<HfDic, Long>{

}
