package com.hafu.repository.jpa;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hafu.entity.HfExaminationCheckdata;

public interface HfExaminationCheckdataDao extends JpaSpecificationExecutor<HfExaminationCheckdata>,
PagingAndSortingRepository<HfExaminationCheckdata, Long>{

}
