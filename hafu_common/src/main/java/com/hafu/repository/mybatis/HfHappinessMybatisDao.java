package com.hafu.repository.mybatis;

import java.util.List;
import java.util.Map;

import com.hafu.entity.HfHappiness;


@MyBatisRepository
public interface HfHappinessMybatisDao {
      /**
       * 查询所有快乐指数（可以带条件）
       * @return
       */
	  public List<HfHappiness> findHappiness(Map<String, Object> map);
	  /**
	   * 保存到数据库
	 * @return 
	   */
	  public void insertHappiness(HfHappiness happiness);
	  /**
	   * 修改数据
	   * @param happiness
	   */
	  public void updateHappiness(HfHappiness happiness);
	  /**
	   * 获取快乐指数表
	   * @param map
	   * @return
	   */
	 /* public List<HfHappiness> listHappiness(Map<String, Object> map);*/
}
