package com.hafu.mybatis.test;

import java.sql.Timestamp;

import javax.annotation.Resource;

import org.junit.Test;

import com.hafu.entity.HfExamination;
import com.hafu.repository.mybatis.HfExaminationMybatisDao;


public class ExaminationTest extends SupportTest{

	@Resource
	private HfExaminationMybatisDao hfExaminationMybatisDao;
	
	@Test
	public void insert(){
		HfExamination hfExamination = new HfExamination();
		hfExamination.setCreateTime(new Timestamp(System.currentTimeMillis()));
		hfExamination.setStartDate(new Timestamp(System.currentTimeMillis()));
		hfExamination.setEndDate(new Timestamp(System.currentTimeMillis()));
		hfExamination.setPageSize(500);
		hfExamination.setStatus(0);
		hfExamination.setPageNo(1);
		System.out.println(hfExaminationMybatisDao.insert(hfExamination));
	}
	
	@Test
	public void update(){
		HfExamination hfExamination = new HfExamination();
		hfExamination.setCreateTime(new Timestamp(System.currentTimeMillis()));
		hfExamination.setStartDate(new Timestamp(System.currentTimeMillis()));
		hfExamination.setEndDate(new Timestamp(System.currentTimeMillis()));
		hfExamination.setPageSize(500);
		hfExamination.setStatus(1);
		hfExamination.setPageNo(1);
		hfExamination.setId(new Long(2));
		System.out.println(hfExaminationMybatisDao.updateByPrimaryKey(hfExamination));
	}
	
	@Test
	public void select(){
		System.out.println(hfExaminationMybatisDao.selectByLastTime().getId());
	}
}
