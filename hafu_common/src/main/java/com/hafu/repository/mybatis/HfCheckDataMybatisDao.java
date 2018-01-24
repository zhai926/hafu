package com.hafu.repository.mybatis;

import java.util.List;
import java.util.Map;

import com.hafu.entity.HfCheckData;

/**
 * 检测数据
 * @author Administrator
 *
 */
@MyBatisRepository
public interface HfCheckDataMybatisDao {
	//首页最新的检测数据
	public List<HfCheckData> indexLastCheck(Map<String, Object> map);
	/**
	 * 添加检测数据
	 * @param HfCheckData
	 * @return
	 */
	public int insert(HfCheckData hfCheckData) throws Exception;
	/**
	 * 批量添加检测数据
	 * @param list
	 * @return
	 */
	public int insertBatch(List<HfCheckData> list) throws Exception;
	/**
	 * 批量修改检测数据
	 * @param list
	 * @return
	 */
	public int updateBatch(List<HfCheckData> list) throws Exception;
	/**
	 * 根据userId和checkType查询体检信息
	 * @param map
	 * @return
	 */
	public HfCheckData findByUserIdType(Map<String, Object> map);
	
}
