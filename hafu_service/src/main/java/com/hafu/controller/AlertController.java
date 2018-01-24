package com.hafu.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hafu.dto.UserDto;
import com.hafu.eban.utils.CodeConverter;
import com.hafu.eban.utils.DateUtil;
import com.hafu.eban.utils.JSONUtils;
import com.hafu.entity.HfAlert;
import com.hafu.entity.HfDevice;
import com.hafu.entity.HfUser;
import com.hafu.repository.jpa.HfUserDao;
import com.hafu.repository.mybatis.UserMybatisDao;
import com.hafu.service.HfAlertService;
import com.hafu.service.HfDeviceService;
import com.hafu.utils.HttpTookit;

/**
 * 报警信息推送接收类
 * 
 * @author zhang
 *
 */
@Controller
@RequestMapping(value = "/alert")
public class AlertController {

	private Logger log = Logger.getLogger(AlertController.class);

	@Resource
	private HfAlertService hfAlertService;
	@Resource
	private HfDeviceService hfDeviceService;
	
	/**
	 * 推送接收
	 * 
	 * @return
	 */
	@RequestMapping(value = "alert")
	public void alert(HttpServletRequest request, HttpServletResponse response) {
		try {
			String msgXml = IOUtils.toString(request.getInputStream(), "utf-8");
			log.info("报警内容：" + msgXml);
			if (StringUtils.isNotBlank(msgXml)) {
				msgXml = CodeConverter.decodeUnicode2ZH(msgXml);
				AlertBean alertBean = JSONUtils
						.fromJson(CodeConverter.decodeUnicode2ZH(msgXml),
								AlertBean.class);
				if (alertBean != null) {
					HfDevice hfDevice = hfDeviceService.getAlert(alertBean
							.getCode());
					if (hfDevice != null) {
						Date alertDate = DateUtil
								.TimeStamp2DateFomart(alertBean.getTime());
						HfAlert hfAlert = new HfAlert();
						hfAlert.setAlertDate(alertDate);
						hfAlert.setAlertType(alertBean.getType());
						hfAlert.setCode(alertBean.getCode());
						hfAlert.setContent("");
						hfAlert.setCreateDate(new Date());
						hfAlert.setDeleteFlag(0);
						hfAlert.setLatitude(alertBean.getLati());
						hfAlert.setLongitude(alertBean.getLang());
						hfAlert.setNickName("");
						hfAlert.setPosition(alertBean.getPosition());
						hfAlert.setUserId(hfDevice.getUserId());
						hfAlert.setReadStatus(0);
						hfAlertService.save(hfAlert);
					}
				}
			}
			PrintWriter out = response.getWriter();
			out.write("success");
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
