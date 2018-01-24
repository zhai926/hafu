package com.hafu.eban.service;

import java.io.FileInputStream;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.hafu.eban.entity.BoundParam;
import com.hafu.eban.entity.response.BoundResponse;
import com.hafu.eban.utils.CodeConverter;
import com.hafu.eban.utils.DateUtil;
import com.hafu.eban.utils.HttpClientUtil;
import com.hafu.eban.utils.JSONUtils;

/***
 * 设备信息相关
 * 
 * @author chang
 *
 */
@Service
public class BoundService {
	private static Logger logger = Logger.getLogger(BoundService.class);
	
	private static String boundUrl = "http://capi.9eban.com/common/bound";
	private static String myFollowUrl = "http://capi.9eban.com/userFollow/myFollow";
	private static String editBoundUrl = "http://capi.9eban.com/common/editBound";
	private static String delBoundUrl = "http://capi.9eban.com/common/delBound";

	public static void main(String[] args) {
		BoundService bound = new BoundService();
		// 绑定
		/*BoundParam boundParam = new BoundParam();
		boundParam.setBirthday("0919");
		boundParam.setCasehistory("无");
		boundParam.setMobile("13587733456");
		boundParam.setWeight("160");
		boundParam.setGender("男");
		boundParam.setHeight("190");
		boundParam.setName("蔡一航");
		boundParam.setNickname("蔡一航");
		boundParam.setCode("100101947");
		BoundResponse boundResponse = bound.bound(boundParam);
		System.out.println(boundResponse.getSuccess().equals("1"));*/
		//获取所有
		BoundResponse boundResponse = bound.myFollow("100101947");
		for (int i = 0; i < boundResponse.getMyFollow().size(); i++) {
			System.out.println(boundResponse.getMyFollow().get(i).getCode());
		}
		//修改
		//BoundResponse boundResponse = bound.editBound(boundParam);
		//System.out.println(boundResponse.getMessage());
		//删除
		//BoundResponse boundResponse = bound.delBound("100202951");
		//System.out.println(boundResponse.getMessage());
		//CodeAvatarResponse codeAvatarResponse = bound.codeAvatar
		//		("C:\\Users\\chang\\Pictures\\Saved Pictures\\160330135414-1.jpg","100100356");
		//System.out.println(codeAvatarResponse.getAvatar());
	}

	/***
	 * 用户通过提交绑定设备编号以及相关信息绑定当前设备
	 */
	public BoundResponse bound(BoundParam boundParam) {
		String param = boundParam.toUrlString();
		HttpClientUtil httpClientUtil = new HttpClientUtil();
		String str = httpClientUtil.getHttp(boundUrl + param);
		System.out.println(boundParam.getCode()+ " 绑定当前设备  = " + CodeConverter.decodeUnicode2ZH(str));
		logger.debug(boundParam.getCode()+ " 绑定当前设备  = " + CodeConverter.decodeUnicode2ZH(str));
		return JSONUtils.fromJson(CodeConverter.decodeUnicode2ZH(str), BoundResponse.class);
	}

	/***
	 * 用户通过设备编号获取当前设备相关信息
	 */
	public BoundResponse myFollow(String code) {
		HttpClientUtil httpClientUtil = new HttpClientUtil();
		String str = httpClientUtil.getHttp(myFollowUrl + "?code=" + code);
		logger.info("时间   "
				+ DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss")
				+ "  当前设备 "+code+" 相关信息  = " + CodeConverter.decodeUnicode2ZH(str));
		return JSONUtils.fromJson(CodeConverter.decodeUnicode2ZH(str), BoundResponse.class);
	}

	/***
	 * 用户通过设备编号以及新的设备信息更新之前绑定的设备信息
	 */
	public BoundResponse editBound(BoundParam boundParam) {
		String param = boundParam.toUrlString();
		HttpClientUtil httpClientUtil = new HttpClientUtil();
		String str = httpClientUtil.getHttp(editBoundUrl + param);
		logger.debug(boundParam.getCode() + " 更新当前设备相关信息  = "
				+ CodeConverter.decodeUnicode2ZH(str));
		return JSONUtils.fromJson(CodeConverter.decodeUnicode2ZH(str), BoundResponse.class);
	}

	/***
	 * 用户通过设备编号删除绑定设备
	 */
	public BoundResponse delBound(String code) {
		HttpClientUtil httpClientUtil = new HttpClientUtil();
		String str = httpClientUtil.getHttp(delBoundUrl + "?code=" + code);
		logger.debug(code + " 删除绑当前设备相关信息   = "
				+ CodeConverter.decodeUnicode2ZH(str));
		return JSONUtils.fromJson(CodeConverter.decodeUnicode2ZH(str), BoundResponse.class);
	}

	
	public static byte[] getImgByte(String path) {
		FileInputStream fis = null;
		byte[] buffer = new byte[100];
		int temp = 0;
		try {
			fis = new FileInputStream(path);
			while (true) {
				temp = fis.read(buffer, 0, buffer.length);
				if (temp == -1) {
					break;
				}
			}
			System.out.println(buffer);
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				fis.close();
			} catch (Exception e2) {
				System.out.println(e2);
			}
		}
		return buffer;

	}
	
}
