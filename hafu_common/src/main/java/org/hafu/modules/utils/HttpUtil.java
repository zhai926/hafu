package org.hafu.modules.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;



public class HttpUtil {	
		//获取服务器端数据 post方式
		public static String getContext(String url) throws Exception{
	    	StringBuilder sb = new StringBuilder();
	    	//获取网络客户端对象
	        HttpClient client = new DefaultHttpClient();
	       
	        client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
	        String realUrl = url;
	     // HttpPost连接对象
	       // HttpPost httpRequest = new HttpPost(realUrl);
	        
	        HttpGet httpRequest = new HttpGet(realUrl);	      
	        httpRequest.addHeader("Content-Type",
	                "application/x-www-form-urlencoded;charset=" + HTTP.UTF_8);
	        HttpResponse response = client.execute(httpRequest);
	        int statusCode = response.getStatusLine().getStatusCode();
	        if(statusCode==200)
	        {
	        	HttpEntity entity = response.getEntity();
		        if(entity != null)
		        {
		        	BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(),"UTF-8"),8192);
		        	String line = null;
		        	while((line = reader.readLine()) != null)
		        	{
		        		sb.append(line);
		        	}
		        	reader.close();
		        }
	        }
	    	return sb.toString();
	    }

}
