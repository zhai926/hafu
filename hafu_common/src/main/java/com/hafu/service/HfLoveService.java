package com.hafu.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.hafu.entity.HfLove;
import com.hafu.repository.mybatis.HfLoveMybatisDao;

/** 
 * 关爱类型
 */
@Service
public class HfLoveService {
	
	@Resource
	private HfLoveMybatisDao hfLoveMybatisDao;
	
	 /**
     * 根据关爱类型编号查询关爱信息
     * @return
     */
//	public  HfLove findhfLove(long loveType){
//		Map<String, Object> param = new HashMap<String, Object>();
//		param.put("loveType", loveType);
//		return hfLoveMybatisDao.findhfLove(param);
//		
//	}
	 
	public HfLove getlove(Integer loveType){
		return hfLoveMybatisDao.getLove(loveType);	
	}
	  /**
	   * 保存到数据库
	 * @return 
	   */
	  public void inserthfLove(HfLove hfLove ){
		  hfLoveMybatisDao.inserthfLove(hfLove);
	  }
	  
	
}
