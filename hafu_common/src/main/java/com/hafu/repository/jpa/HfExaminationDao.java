package com.hafu.repository.jpa;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.hafu.entity.HfExamination;

public interface HfExaminationDao extends JpaSpecificationExecutor<HfExamination>,
PagingAndSortingRepository<HfExamination, Long>{
}
