package com.hafu.repository.mybatis;

import java.util.List;
import java.util.Map;

import com.hafu.entity.HfExaminationCheckdata;

@MyBatisRepository
public interface HfExaminationCheckdataMybatisDao {
    
	/**
	 * 根据查询计划id删除体检数据
	 * @param id
	 * @return
	 */
	public int deleteByExaminationId(Long id);
    /**
     * 添加
     * @param record
     * @return
     */
	public int insert(HfExaminationCheckdata record);

    /**
     * 修改
     * @param record
     * @return
     */
	public int updateByPrimaryKey(HfExaminationCheckdata record);
    /**
     * 根据数据id查询
     * @param id
     * @return
     */
    public HfExaminationCheckdata getByDataId(String id);
    /**
     * 根据条件查询集合
     * @param map
     * @return
     */
    public List<HfExaminationCheckdata> getList(Map<String, Object> map);
    /**
     * 根据条件查询数量
     * @param map
     * @return
     */
    public int countByExample(Map<String, Object> map);
    /**
     * 批量添加
     * @param list
     */
	public void insertBatch(List<HfExaminationCheckdata> list) throws Exception;
	/**
	 * 批量修改
	 * @param checkdata
	 */
	public void updateBatch(List<HfExaminationCheckdata> list) throws Exception;
}