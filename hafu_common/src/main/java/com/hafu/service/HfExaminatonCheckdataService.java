package com.hafu.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hafu.modules.utils.DateUtil;
import org.hafu.modules.utils.PublicProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hafu.dto.CheckDataDto;
import com.hafu.eban.utils.JSONUtils;
import com.hafu.entity.HfCheckData;
import com.hafu.entity.HfExaminationCheckdata;
import com.hafu.repository.mybatis.HfExaminationCheckdataMybatisDao;

@Service
public class HfExaminatonCheckdataService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private HfExaminationCheckdataMybatisDao hfExaminationCheckdataMybatisDao;
	@Autowired
	private HfCheckDataService hfCheckDataService;
	/**
	 * 根据查询计划id删除体检数据
	 * @param id
	 * @return
	 */
	public boolean deleteByExaminationId(Long id){
		Integer flag = 0;
		try {
			flag = hfExaminationCheckdataMybatisDao.deleteByExaminationId(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return flag > 0 ? true : false;
	}
    /**
     * 添加
     * @param record
     * @return
     */
	public boolean insert(HfExaminationCheckdata record){
		Integer flag = 0;
		try {
			flag = hfExaminationCheckdataMybatisDao.insert(record);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return flag > 0 ? true : false;
	}

    /**
     * 修改
     * @param record
     * @return
     */
	public boolean updateByPrimaryKey(HfExaminationCheckdata record){
		Integer flag = 0;
		try {
			flag = hfExaminationCheckdataMybatisDao.updateByPrimaryKey(record);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return flag > 0 ? true : false;
	}
    /**
     * 根据数据id查询
     * @param id
     * @return
     */
    public HfExaminationCheckdata getByDataId(String id){
    	return hfExaminationCheckdataMybatisDao.getByDataId(id);
    }
    /**
     * 根据条件查询
     * @param cardNo 身份证
     * @param accessStatus 同步状态
     * @param orderByClause 排序
     * @return
     */
    public List<HfExaminationCheckdata> getList(Map<String, Object> map){
    	return hfExaminationCheckdataMybatisDao.getList(map);
    }
    /**
     * 根据条件查询数量
     * @param cardNo 身份证
     * @param accessStatus 同步状态
     * @param orderByClause 排序
     * @return
     */
    public int countByExample(Map<String, Object> map){
    	return hfExaminationCheckdataMybatisDao.countByExample(map);
    }
    
    /**
     * 批量添加
     * @param list
     */
	public void insertBatch(List<HfExaminationCheckdata> list) throws Exception{
		if(list!=null){
			if(list.size()>0){
				hfExaminationCheckdataMybatisDao.insertBatch(list);
			}
		}
	}
	/**
	 * 批量修改
	 * @param checkdata
	 */
	public void updateBatch(List<HfExaminationCheckdata> list) throws Exception{
		if(list!=null){
			if(list.size()>0){
				hfExaminationCheckdataMybatisDao.updateBatch(list);
			}
		}
	}

	/**
	 * 实时处理体检数据
	 * ********用户填写身份证信息调用********
	 * @param userId 用户id
	 * @param cardNo 身份编号
	 * @return
	 */
	public boolean handlerCheckData(Long userId,String cardNo){
		boolean flag = false;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("cardNo", cardNo);
			map.put("accessStatus", 0);
			map.put("orderByClause", "upload_time asc");
			List<HfExaminationCheckdata> List = getList(map);
			//List<HfCheckData> addDataList = new ArrayList<HfCheckData>();
			List<HfCheckData> updateDataList = new ArrayList<HfCheckData>();
			List<HfExaminationCheckdata> updateList = new ArrayList<HfExaminationCheckdata>();
			if(List.size()>0){
				for(HfExaminationCheckdata checkdata:List){
					CheckDataDto dataDto = JSONUtils.fromJson(checkdata.getData(), CheckDataDto.class);
					/**
					 * 1.血压
					 * 舒张压,收缩压,平均压
					 */
					if(dataDto.getSBP()!=null&&dataDto.getDBP()!=null&&dataDto.getMBP()!=null){
						HfCheckData data = new HfCheckData();
						data.setCheckCategory(PublicProperty.healthData);
						data.setCheckData(dataDto.getDBP()+","+dataDto.getSBP()+","+dataDto.getMBP());
						data.setCheckDate(new Date(checkdata.getRecordTime().getTime()));
						data.setCreateDate(new Date(checkdata.getUploadTime().getTime()));
						data.setDeleteFlag(0);
						data.setCheckType(PublicProperty.healthData_XY);
						data.setUserId(userId);
						String beginTime = DateUtil.date2String(new Date(checkdata.getRecordTime().getTime()), "yyyy-MM-dd")+" 00:00:00";
						String endTime = DateUtil.date2String(new Date(checkdata.getRecordTime().getTime()), "yyyy-MM-dd")+" 23:59:59";
						HfCheckData d1 = hfCheckDataService.findByUserIdType(userId,PublicProperty.healthData_XY,beginTime,endTime);
						if(d1==null){
							hfCheckDataService.insert(data);
						}else{
							data.setId(d1.getId());
							updateDataList.add(data);
						}
						
					}
					/**
					 * 2.血常规
					 * 总胆固醇 血氧 白细胞 血红蛋白
					 */
					if(dataDto.getXZZDGC()!=null||dataDto.getSPO2()!=null||dataDto.getASSBXB()!=null||dataDto.getASSXHDB()!=null){
						HfCheckData data2 = new HfCheckData();
						data2.setCheckCategory(PublicProperty.healthData);
						String zdgc = dataDto.getXZZDGC()==null?"":dataDto.getXZZDGC();
						String xy = dataDto.getSPO2()==null?"":dataDto.getSPO2();
						String bxb = dataDto.getASSBXB()==null?"":dataDto.getASSBXB();
						String xhdb = dataDto.getASSXHDB()==null?"":dataDto.getASSXHDB();
						data2.setCheckData(zdgc+","+xy+","+bxb+","+xhdb);
						data2.setCheckDate(new Date(checkdata.getRecordTime().getTime()));
						data2.setCreateDate(new Date(checkdata.getUploadTime().getTime()));
						data2.setDeleteFlag(0);
						data2.setCheckType(PublicProperty.healthData_XZ);
						data2.setUserId(userId);
						String beginTime = DateUtil.date2String(new Date(checkdata.getRecordTime().getTime()), "yyyy-MM-dd")+" 00:00:00";
						String endTime = DateUtil.date2String(new Date(checkdata.getRecordTime().getTime()), "yyyy-MM-dd")+" 23:59:59";
						HfCheckData d2 = hfCheckDataService.findByUserIdType(userId,PublicProperty.healthData_XZ,beginTime,endTime);
						if(d2==null){
							hfCheckDataService.insert(data2);
						}else{
							data2.setId(d2.getId());
							updateDataList.add(data2);
						}
					}
					/**
					 * 3.血糖
					 */
					if(dataDto.getGlu()!=null){
						HfCheckData data = new HfCheckData();
						data.setCheckCategory(PublicProperty.healthData);
						data.setCheckData(dataDto.getGlu());
						data.setCheckDate(new Date(checkdata.getRecordTime().getTime()));
						data.setCreateDate(new Date(checkdata.getUploadTime().getTime()));
						data.setDeleteFlag(0);
						data.setCheckType(PublicProperty.healthData_XT);
						data.setUserId(userId);
						String beginTime = DateUtil.date2String(new Date(checkdata.getRecordTime().getTime()), "yyyy-MM-dd")+" 00:00:00";
						String endTime = DateUtil.date2String(new Date(checkdata.getRecordTime().getTime()), "yyyy-MM-dd")+" 23:59:59";
						HfCheckData d3 = hfCheckDataService.findByUserIdType(userId,PublicProperty.healthData_XT,beginTime,endTime);
						if(d3==null){
							hfCheckDataService.insert(data);
						}else{
							data.setId(d3.getId());
							updateDataList.add(data);
						}
					}
					/**
					 * 4.心率
					 */
					if(dataDto.getPR()!=null){
						HfCheckData data = new HfCheckData();
						data.setCheckCategory(PublicProperty.bodyData);
						data.setCheckData(dataDto.getPR());
						data.setCheckDate(new Date(checkdata.getRecordTime().getTime()));
						data.setCreateDate(new Date(checkdata.getUploadTime().getTime()));
						data.setDeleteFlag(0);
						data.setCheckType(PublicProperty.bodyData_XL);
						data.setUserId(userId);
						String beginTime = DateUtil.date2String(new Date(checkdata.getRecordTime().getTime()), "yyyy-MM-dd")+" 00:00:00";
						String endTime = DateUtil.date2String(new Date(checkdata.getRecordTime().getTime()), "yyyy-MM-dd")+" 23:59:59";
						HfCheckData d4 = hfCheckDataService.findByUserIdType(userId,PublicProperty.bodyData_XL,beginTime,endTime);
						if(d4==null){
							hfCheckDataService.insert(data);
						}else{
							data.setId(d4.getId());
							updateDataList.add(data);
						}
					}
					/**
					 * 5.尿检
					 * TODO 未确认数据
					 */
					HfCheckData data = new HfCheckData();
					data.setCheckCategory(PublicProperty.healthData);
					data.setCheckData("正常");
					data.setCheckDate(new Date(checkdata.getRecordTime().getTime()));
					data.setCreateDate(new Date(checkdata.getUploadTime().getTime()));
					data.setDeleteFlag(0);
					data.setCheckType(PublicProperty.healthData_NJ);
					data.setUserId(userId);
					String beginTime = DateUtil.date2String(new Date(checkdata.getRecordTime().getTime()), "yyyy-MM-dd")+" 00:00:00";
					String endTime = DateUtil.date2String(new Date(checkdata.getRecordTime().getTime()), "yyyy-MM-dd")+" 23:59:59";
					HfCheckData d5 = hfCheckDataService.findByUserIdType(userId,PublicProperty.healthData_NJ,beginTime,endTime);
					if(d5==null){
						hfCheckDataService.insert(data);
					}else{
						data.setId(d5.getId());
						updateDataList.add(data);
					}
					
					/**
					 * 处理完成的数据
					 */
					HfExaminationCheckdata checkdatas = new HfExaminationCheckdata();
					checkdatas.setAccessStatus(1);
					checkdatas.setId(checkdata.getId());
					updateList.add(checkdatas);
				}
				//hfCheckDataService.insertBatch(addDataList);
				hfCheckDataService.updateBatch(updateDataList);
				updateBatch(updateList);
			}
			flag = true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return flag;
	}
}
