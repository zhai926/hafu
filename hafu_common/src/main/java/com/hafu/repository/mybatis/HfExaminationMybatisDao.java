package com.hafu.repository.mybatis;

import com.hafu.entity.HfExamination;

@MyBatisRepository
public interface HfExaminationMybatisDao {

    /**
     * 根据添加时间查询最后处理时间
     * @param id
     * @return
     */
	public HfExamination selectByLastTime();

    /**
     * 添加
     * @param record
     * @return
     */
	public int insert(HfExamination record);

    /**
     * 根据id修改
     * @param record
     * @return
     */
	public int updateByPrimaryKey(HfExamination record);

}