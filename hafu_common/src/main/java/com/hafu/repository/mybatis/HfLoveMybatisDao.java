package com.hafu.repository.mybatis;

import java.util.Map;

import com.hafu.entity.HfLove;


@MyBatisRepository
public interface HfLoveMybatisDao {
      /**
       * 根据关爱类型编号查询关爱信息
       * @return
       */
	 public HfLove getLove(Integer loveType);
	/*  public  HfLove findhfLove(Map<String,Object> map);*/
	
	  /**
	   * 保存到数据库
	 * @return 
	   */
	  public void inserthfLove(HfLove hfLove );
	  
}
