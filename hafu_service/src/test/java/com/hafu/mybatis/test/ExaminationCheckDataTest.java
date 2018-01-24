package com.hafu.mybatis.test;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;

import com.hafu.entity.HfExaminationCheckdata;
import com.hafu.repository.mybatis.HfExaminationCheckdataMybatisDao;

public class ExaminationCheckDataTest extends SupportTest{

	@Resource
	private HfExaminationCheckdataMybatisDao hfExaminationCheckdataMybatisDao;
	
	@Test
	public void insert(){
		HfExaminationCheckdata checkdata = new HfExaminationCheckdata();
		checkdata.setAccessStatus(0);
		checkdata.setCardNo("431003199108214111");
		checkdata.setData("{}");
		checkdata.setDataFrom("http");
		checkdata.setExaminationId(new Long(2));
		checkdata.setName("dk");
		checkdata.setRecordTime(new Timestamp(System.currentTimeMillis()));
		checkdata.setUploadTime(new Timestamp(System.currentTimeMillis()));
		hfExaminationCheckdataMybatisDao.insert(checkdata);
	}
	@Test
	public void insertBatch(){
		HfExaminationCheckdata checkdata = new HfExaminationCheckdata();
		checkdata.setAccessStatus(0);
		checkdata.setCardNo("431003199108214111");
		checkdata.setData("{}");
		checkdata.setDataFrom("http");
		checkdata.setExaminationId(new Long(2));
		checkdata.setName("dk");
		checkdata.setRecordTime(new Timestamp(System.currentTimeMillis()));
		checkdata.setUploadTime(new Timestamp(System.currentTimeMillis()));
		List<HfExaminationCheckdata> list = new ArrayList<HfExaminationCheckdata>();
		list.add(checkdata);
		checkdata.setName("dk2");
		list.add(checkdata);
		try {
			hfExaminationCheckdataMybatisDao.insertBatch(list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void update(){
		HfExaminationCheckdata checkdata = new HfExaminationCheckdata();
		checkdata.setAccessStatus(0);
		checkdata.setCardNo("431003199108214111");
		checkdata.setData("{cardNo:\"431003199108214111\"}");
		checkdata.setDataFrom("http");
		checkdata.setExaminationId(new Long(2));
		checkdata.setName("dk1");
		checkdata.setRecordTime(new Timestamp(System.currentTimeMillis()));
		checkdata.setUploadTime(new Timestamp(System.currentTimeMillis()));
		checkdata.setId(new Long(1));
		hfExaminationCheckdataMybatisDao.updateByPrimaryKey(checkdata);
	}
	
	@Test
	public void updateBatch(){
		HfExaminationCheckdata checkdata = new HfExaminationCheckdata();
		checkdata.setAccessStatus(0);
		checkdata.setCardNo("431003199108214111");
		checkdata.setData("{cardNo:\"431003199108214111\"}");
		checkdata.setDataFrom("http");
		checkdata.setExaminationId(new Long(2));
		checkdata.setName("dk1");
		checkdata.setRecordTime(new Timestamp(System.currentTimeMillis()));
		checkdata.setUploadTime(new Timestamp(System.currentTimeMillis()));
		checkdata.setId(new Long(2));
		List<HfExaminationCheckdata> list = new ArrayList<HfExaminationCheckdata>();
		list.add(checkdata);
		try {
			hfExaminationCheckdataMybatisDao.updateBatch(list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void select(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cardNo","431003199108214111");
		map.put("accessStatus", 0);
		map.put("orderByClause", "upload_time asc");
		List<HfExaminationCheckdata> list = hfExaminationCheckdataMybatisDao.getList(map);
		System.out.println(list.get(0).getCardNo());
		System.out.println(list.get(0).getData());
	}
}
