package com.hafu.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class HttpTookit{
    
    private static Logger logger = Logger.getLogger(HttpTookit.class);
    

    /**
     * 执行一个HTTP GET请求，返回请求响应的HTML
     *
     * @param url                 请求的URL地址
     * @param queryString 请求的查询参数,可以为null
     * @param charset         字符集
     * @param pretty            是否美化
     * @return 返回请求响应的HTML
     */
    public static String doGet(String url, String queryString, String charset, boolean pretty) {
        StringBuffer result = new StringBuffer();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet(url);
            //设置请求和传输超时时间
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000).build();
            httpGet.setConfig(requestConfig);
            CloseableHttpResponse response1 = httpclient.execute(httpGet);
            try {
                HttpEntity entity = response1.getEntity();
                BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), charset));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (pretty)
                        result.append(line).append(System.getProperty("line.separator"));
                    else
                        result.append(line);
                }
            } finally {
                response1.close();
            }
        } catch (Exception e) {
            logger.error("执行HTTP Get请求" + url + "时，发生异常！", e);
        }finally{
            try {
                httpclient.close();
            } catch (IOException e) {
                logger.error("执行HTTP Get请求" + url + "时，发生异常！", e);
            }
        }
        return result.toString();
    }

    /**
     * 执行一个HTTP POST请求，返回请求响应的HTML
     *
     * @param url         请求的URL地址
     * @param params    请求的查询参数,可以为null
     * @param charset 字符集
     * @param pretty    是否美化
     * @return 返回请求响应的HTML
     */
    public static String doPost(String url, Map<String, String> params, String charset, boolean pretty) {
        StringBuffer result = new StringBuffer();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            //设置请求和传输超时时间 3秒
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(15000).build();
            httpPost.setConfig(requestConfig);
            // 设置Http Post数据
            if (params != null) {
                List <NameValuePair> nvps = new ArrayList <NameValuePair>();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    nvps.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
                }
                httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            }
            CloseableHttpResponse response2 = httpclient.execute(httpPost);
            try {
                if (response2.getStatusLine().getStatusCode()== HttpStatus.SC_OK) {
                    HttpEntity entity = response2.getEntity();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(),charset));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (pretty)
                            result.append(line).append(System.getProperty("line.separator"));
                        else
                            result.append(line);
                    }
                    EntityUtils.consume(entity);
                }
            } finally {
                response2.close();
            }
        } catch (Exception e) {
            //logger.error("执行HTTP POST请求" + url + "时，发生异常！", e);
        }finally{
            try {
                httpclient.close();
            } catch (IOException e) {
                logger.error("执行HTTP POST请求" + url + "时，发生异常！", e);
            }
        }
        return result.toString();
    }
    
    
    /**
	 * 发起http请求并获取结果
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * @param outputStr
	 *            提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject doHttpRequest(String requestUrl,
			String requestMethod, String outputStr ) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url
					.openConnection();

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod)) {
				httpUrlConn.connect();
			}
             
			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			if(StringUtils.isEmpty(buffer.toString()))
				jsonObject = null;
			else
				jsonObject = JSONObject.fromObject(buffer.toString());
		} catch (ConnectException ce) {
			ce.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	/**
	 * 返回string 
	 * @param requestUrl
	 * @param requestMethod
	 * @param outputStr
	 * @return
	 */
	public static String doHttpRequestString(String requestUrl,
			String requestMethod, String outputStr ) {
		
		String jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url
					.openConnection();

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod)) {
				httpUrlConn.connect();
			}
             
			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = buffer.toString();
		} catch (ConnectException ce) {
			ce.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	
    public static void main(String[] args) {
        /*try {
            System.out.println(new Date());
            for(int i=0;i<10;i++){
                String result = doPost("http://180.153.86.68:9013/refund", null, "utf-8", false);
                System.out.println(result);
            }
            System.out.println(new Date());
            System.out.println(System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    	
    	String result = doPost("http://wx.hafu365.com/auth/admin?p=aHR0cDovL2JhaWR1LmNvbT9hcHBpZD13eGFlMjU5ZDM2NGJlZTUyMjE=", null,"utf-8", false);
    	System.out.println(result);
    }
}
