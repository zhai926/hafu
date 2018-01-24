package com.hafu.mybatis.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hafu.modules.utils.PublicProperty;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hafu.dto.UserDto;
import com.hafu.entity.HfCheckData;
import com.hafu.entity.HfExaminationCheckdata;
import com.hafu.repository.mybatis.HfCheckDataMybatisDao;
import com.hafu.repository.mybatis.UserMybatisDao;
import com.hafu.service.HfCheckDataService;
import com.hafu.service.HfExaminatonCheckdataService;

public class UserDaoTest extends SupportTest{

	@Resource
	private UserMybatisDao userMybatisDao;
	@Resource
	private HfCheckDataMybatisDao checkDataMybatisDao;
	@Autowired
	private HfExaminatonCheckdataService hfExaminatonCheckdataService;

	@Test
	public void getCard(){
		List<UserDto> list = userMybatisDao.getCardId();
		for (UserDto u:list) {
			System.out.println(u.getName());
			System.out.println(u.getCard());
		}
	}
	
	@Test
	public void insertBatchTest(){
		List<HfCheckData> list = new ArrayList<HfCheckData>();
		HfCheckData data1 = new HfCheckData();
		data1.setCode("111");
		list.add(data1);
		
		HfCheckData data2 = new HfCheckData();
		data2.setCode("111");
		list.add(data2);
		try {
			checkDataMybatisDao.insertBatch(list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void aaa(){
		hfExaminatonCheckdataService.handlerCheckData(new Long(52), "450221000000000316");
		/*Map<String, Object> map1  = new HashMap<String, Object>();
		map1.put("userId", new Long(52));
		map1.put("checkType", PublicProperty.healthData_XY);
		map1.put("beginTime", "2016-06-23 00:00:00");
		map1.put("endTime", "2016-06-23 23:59:59");
		HfCheckData d1 = checkDataMybatisDao.findByUserIdType(map1);
		Map<String, Object> map2  = new HashMap<String, Object>();
		map2.put("userId", new Long(52));
		map2.put("checkType", PublicProperty.healthData_XZ);
		map1.put("beginTime", "2016-06-23 00:00:00");
		map1.put("endTime", "2016-06-23 23:59:59");
		HfCheckData d2 = checkDataMybatisDao.findByUserIdType(map2);*/
	}
}
