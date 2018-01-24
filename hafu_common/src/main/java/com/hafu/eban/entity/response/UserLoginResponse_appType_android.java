/**
 * 
 */
package com.hafu.eban.entity.response;

import java.io.Serializable;

/**
 * @author chang
 *
 */
public class UserLoginResponse_appType_android implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String version;	//版本
	private String url;		//下载连接
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

		
}
