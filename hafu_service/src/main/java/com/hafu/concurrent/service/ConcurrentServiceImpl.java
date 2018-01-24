package com.hafu.concurrent.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hafu.modules.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.hafu.entity.HfExamination;
import com.hafu.entity.HfExaminationCheckdata;
import com.hafu.examination.service.ExaminationService;
import com.hafu.examination.vo.CheckDataPage;
import com.hafu.examination.vo.CheckDataVo;
import com.hafu.service.HfExaminatonCheckdataService;
import com.hafu.service.HfExaminatonService;
import com.hafu.utils.JSONUtils;

public class ConcurrentServiceImpl implements ConcurrentService{

    private Logger logger = Logger.getLogger(this.getClass());
    @Autowired
	private ExaminationService examinationService;
	@Autowired
	private HfExaminatonService hfExaminatonService;
	@Autowired
	private HfExaminatonCheckdataService hfExaminatonCheckdataService;
    
	/***
	 * 线程同步一体机 每3分钟运行一次
	 */
	@Override
	public void concurrentExamination() throws Exception {
		logger.debug("********************执行数据同步Begin***********************");
		 try {
			 HfExamination examination = hfExaminatonService.selectByLastTime();
			 if(examination!=null){
				 String startDate = DateUtil.timestamp2String(examination.getStartDate(),DateUtil.FORMAT_DATETIME);
				 String endDate = DateUtil.timestamp2String(examination.getEndDate(), DateUtil.FORMAT_DATETIME);
				 if(examination.getStatus()==0){//上次未处理完成
					 //hfExaminatonCheckdataService.deleteByExaminationId(examination.getId());
					 List<CheckDataVo> dataVos= getList(startDate, endDate, examination.getPageNo(), examination.getPageSize(),new ArrayList<CheckDataVo>());
					 boolean f= handlerList(examination.getId(),dataVos);
					 if(f){
						 if(new Date().after(examination.getEndDate())){//结束时间在当前时间之前
							 HfExamination e = new HfExamination();
							 e.setId(examination.getId());
							 e.setStatus(1);
							 hfExaminatonService.updateByPrimaryKey(e);
						 }
					 }
				 }else{
					 //开始时间
					 Date dqDate = DateUtil.string2Date(endDate, DateUtil.FORMAT_DATETIME);
					 if(new Date().after(dqDate)){	//上次结束时间在当前时间之前
						//开始时间加1小时
						 String endTime = DateUtil.date2String(DateUtil.addHours(dqDate, 1), DateUtil.FORMAT_DATETIME);
						 
						 HfExamination e = new HfExamination();
						 e.setCreateTime(new Timestamp(System.currentTimeMillis()));
						 e.setEndDate(DateUtil.string2Timestamp(endTime, DateUtil.FORMAT_DATETIME));
						 e.setPageNo(examination.getPageNo());
						 e.setPageSize(examination.getPageSize());
						 e.setStartDate(DateUtil.string2Timestamp(endDate, DateUtil.FORMAT_DATETIME));
						 e.setStatus(0);
						 hfExaminatonService.insert(e);
						 
						 List<CheckDataVo> dataVos= getList(endDate, endTime, examination.getPageNo(), examination.getPageSize(),new ArrayList<CheckDataVo>());
						 boolean f= handlerList(e.getId(),dataVos);
						 if(f){
							 Date edate = DateUtil.addHours(dqDate, 1);
							 if(new Date().after(edate)){//结束时间在当前时间之前
								 e.setStatus(1);
								 hfExaminatonService.updateByPrimaryKey(e);
							 }
						 }
					 }
				 }
			 }
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		logger.debug("********************执行数据同步End***********************");
	}
    
	/**
	 * 获取一体机数据
	 * @param startDate
	 * @param endDate
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception 
	 */
	public List<CheckDataVo> getList(String startDate,String endDate,Integer pageNo,Integer pageSize,List<CheckDataVo> list) throws Exception{
		CheckDataPage page = examinationService.checkData("", startDate, endDate, pageNo,pageSize);
		if(page!=null){
			if(page.getTotalCount()>0){
				list.addAll(page.getResult());
				if(page.getTotalPages()>page.getPageNo()){
					getList(startDate, endDate, pageNo+1, pageSize,list);
				}
			}
		}
		return list;
	}
	
	/**
	 * 处理一体机数据
	 * @return
	 * @throws Exception 
	 */
	public boolean handlerList(Long examinationId,List<CheckDataVo> dataVos){
		boolean flag = false;
		List<HfExaminationCheckdata> addList = new ArrayList<HfExaminationCheckdata>();
		List<HfExaminationCheckdata> updateList = new ArrayList<HfExaminationCheckdata>();
		try {
			for(CheckDataVo v :dataVos){
				HfExaminationCheckdata c = hfExaminatonCheckdataService.getByDataId(v.getId());
				if(c!=null){
					c.setAccessStatus(0);
					c.setUploadTime(v.getUploadTime());
					c.setRecordTime(v.getRecordTime());
					c.setData(JSONUtils.xml2JSON(v.getData()));
					//updateList.add(c);
					hfExaminatonCheckdataService.updateByPrimaryKey(c);
				}else{
					HfExaminationCheckdata d = new HfExaminationCheckdata();
					d.setAccessStatus(0);
					d.setCardNo(v.getCardNo());
					d.setData(JSONUtils.xml2JSON(v.getData()));
					d.setDataFrom("http");
					d.setDataId(v.getId());
					d.setExaminationId(examinationId);
					d.setName(v.getName());
					d.setRecordTime(v.getRecordTime());
					d.setUploadTime(v.getUploadTime());
					//hfExaminatonCheckdataService.insert(d);
					addList.add(d);
				}
			}
			hfExaminatonCheckdataService.insertBatch(addList);
			hfExaminatonCheckdataService.updateBatch(updateList);
			flag = true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return flag;
	}
	
	public static void main(String[] args) {
		Date dqDate = DateUtil.string2Date("2016-06-17 15:00:00", DateUtil.FORMAT_DATETIME);
		System.out.println(new Date().after(dqDate));
	}
}
