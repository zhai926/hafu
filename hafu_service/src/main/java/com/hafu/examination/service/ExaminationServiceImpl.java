package com.hafu.examination.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hafu.eban.utils.DateUtil;
import com.hafu.examination.vo.CheckData;
import com.hafu.examination.vo.CheckDataPage;
import com.hafu.examination.vo.CheckDataVo;
import com.hafu.examination.vo.ResponseExamination;
import com.hafu.utils.HttpTookit;
import com.hafu.utils.JSONUtils;
import com.hafu.utils.MD5;

@Component("examinationService")
public class ExaminationServiceImpl implements ExaminationService{

	@Value("#{contextProperties['requestor']}")
	private String requestor = "SHJS";			//厂商标识
	private String merchantId = "yuhetong";		//厂商代号
	//private String URL= "http://103.20.248.39:8080/imms-webs/data/checkData";
	@Value("#{contextProperties['ExaminationURL']}")
	private String ExaminationURL= "http://172.168.22.105:9001/imms-webs/data/checkData";
	private String URL2= "http://172.168.22.105:8080/imms-web/data/checkData";
	
	@Override
	public CheckDataPage checkData(String id,String startDate,String endDate,
			Integer pageNo,Integer pageSize) throws Exception {
		String time = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
		String key = MD5.MD5Encode(requestor+time);
		Map<String, String> filter = new HashMap<String, String>();
		filter.put("id", id);
		filter.put("startDate", startDate);
		filter.put("endDate", endDate);
		filter.put("pageNo", pageNo.toString());
		filter.put("pageSize", pageSize.toString());
		filter.put("key", key);
		filter.put("time", time);
		String reslut = HttpTookit.doPost(ExaminationURL, filter, "utf-8", false);
		CheckDataPage page = null;
		if(StringUtils.isNotBlank(reslut)){
			page = JSONUtils.fromJson(reslut, CheckDataPage.class);
		}
		return page;
	}
	
	@Override
	public ResponseExamination checkData2(String id, String startDate, String endDate,
			Integer page) throws Exception {
		String time = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
		String key = MD5.MD5Encode(merchantId+time);
		String urls=URL2+"?id="+id+"&page="+page+"&requestor="+requestor+"&key="+key+"&time="+time+"&startTime="+startDate+"&endTime="+endDate;
		urls= urls.replaceAll(" ", "%20");
		//System.out.println(urls);
		String reslut = HttpTookit.doGet(urls, null, "utf-8", false);
		System.out.println(reslut);
		ResponseExamination examination = JSONUtils.fromJson(reslut, ResponseExamination.class);
		return examination;
	}

	public static void main(String[] args) throws Exception {
		ExaminationService examinationService = new ExaminationServiceImpl();
		CheckDataPage page = examinationService.checkData("", "2016-05-25 00:00:00", "2016-06-31 00:00:00", 1,20);
		System.out.println(page.getTotalCount());
		for(CheckDataVo v:page.getResult()){
			System.out.println(v.getCardNo());
			System.out.println(v.getData());
			String json = JSONUtils.xml2JSON(v.getData());
			System.out.println(json);
			CheckData checkData = JSONUtils.fromJson(json, CheckData.class);
			System.out.println(checkData.toString());
		}
		
		//examinationService.checkData2("", "2016-05-25 00:00:00", "2016-05-26 00:00:00", 1);
	}
	
	
	
}
