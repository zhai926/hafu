package com.hafu.examination.service;

import com.hafu.examination.vo.CheckDataPage;
import com.hafu.examination.vo.ResponseExamination;

/**
 * 一体机http查询接口调用
 * @author Darker
 *
 */
public interface ExaminationService {

	/**
	 * 体检信息查询--自定义
	 * @param id 身份证 该值不为空时时间可为空
	 * @param startDate	>= 体检起始日期，选填YYYY-MM-DD HH:mm:ss
	 * @param endDate <体检截止日期，选填YYYY-MM-DD HH:mm:ss
	 * @param pageNo 查询页数
	 * @param pageSize 每页数量
	 * @return 
	 * @throws Exception 
	 */
	public CheckDataPage checkData(String id,String startDate,String endDate,Integer pageNo,Integer pageSize) throws Exception;
	
	/**
	 * 体检信息查询--原接口
	 * @param id 身份证
	 * @param startDate	体检起始日期，选填YYYY-MM-DD HH:mm:ss
	 * @param endDate 体检截止日期，选填YYYY-MM-DD HH:mm:ss
	 * @param page 页码 每页最多500条
	 * @return 
	 * @throws Exception 
	 */
	public ResponseExamination checkData2(String id,String startDate,String endDate,Integer page) throws Exception;
}
