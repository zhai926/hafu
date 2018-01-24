package com.hafu.service;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hafu.entity.HfCode;
import com.hafu.repository.jpa.HfCodeDao;

@Service
public class HfCodeService {

	@Resource
	private HfCodeDao codeDao;
	
	public void saveCode(String code){
		try{
			HfCode co = codeDao.findOneByCodeAndDeleteFlag(code, 0);
			if(co==null){
				HfCode hc = new HfCode();
				hc.setCode(code);
				hc.setIsUsed(1);
				hc.setCreateDate(new Date());
				hc.setDeleteFlag(0);
				codeDao.save(hc);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
