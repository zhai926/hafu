package com.hafu.service;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hafu.entity.HfExamination;
import com.hafu.repository.mybatis.HfExaminationMybatisDao;

@Service
public class HfExaminatonService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private HfExaminationMybatisDao hfExaminationMybatisDao;
	
	/**
     * 根据添加时间查询最后处理时间
     * @param id
     * @return
     */
	public HfExamination selectByLastTime(){
		return hfExaminationMybatisDao.selectByLastTime();
    }

    /**
     * 添加
     * @param record
     * @return
     */
	public boolean insert(HfExamination record){
		Integer flag = 0;
		try {
			flag = hfExaminationMybatisDao.insert(record);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return flag > 0 ? true : false;
    }

    /**
     * 根据id修改
     * @param record
     * @return
     */
	public boolean updateByPrimaryKey(HfExamination record){
		Integer flag = 0;
		try {
			flag = hfExaminationMybatisDao.updateByPrimaryKey(record);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return flag > 0 ? true : false;
    }
}
