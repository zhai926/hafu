package com.hafu.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

	public static void main(String[] args) {
		/*
		 * String token =
		 * "JM6Ua7llD-SS553sJv6qxXgrc7iQ4j7e629uEDYK2xbG-rbeNy_OSxvTBsYXOECT-1zVnkIL6oGwsGCoJdHFGHC4TxqDQQCSElUcSZ7rg1NYJiPSnRJcr3tCfW_cSPq5BZHfAJAPNV"
		 * ; try { String a = downloadFile("D:\\demo\\1.amr",
		 * "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=" +
		 * token +
		 * "&media_id=T3gMLicJK0qk9HOT44NUhKVuYCJwZUqg48aGngkzJYBWoyL_4tROiPx-_5MgYm6d"
		 * ); System.out.println(a); } catch (IOException e) {
		 * logger.error(e.getMessage(),e);; }
		 */

		String filepath = "D:\\demo\\1.amr";
		String urlStr = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=WwUeC975bnPXrtbF2zIfNh7ijEI6HQzs122BI12pct6CHfeODeqx9vauY7J0AjLWcuZR21pR-xl1p3MBSJ-tiFD5EnkDJPc6n0LVt1Qbgcm1tgb0uG21cHfjgFLZbk3KHRDeADADNZ&type=voice";
		Map<String, String> textMap = new HashMap<String, String>();
		textMap.put("media", "447957928.amr");
		Map<String, String> fileMap = new HashMap<String, String>();
		fileMap.put("userfile", filepath);
		String ret = formUpload(urlStr, textMap, fileMap);
		System.out.println(ret);

	}

	/**
	 * 上传语音
	 * 
	 * @param urlStr
	 * @param textMap
	 * @param fileMap
	 * @return
	 */
	public static String formUpload(String urlStr, Map<String, String> textMap,
			Map<String, String> fileMap) {
		String res = "";
		HttpURLConnection conn = null;
		String BOUNDARY = "---------------------------123821742118716"; // boundary就是request头和上传文件内容的分隔符
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + BOUNDARY);

			OutputStream out = new DataOutputStream(conn.getOutputStream());
			// text
			if (textMap != null) {
				StringBuffer strBuf = new StringBuffer();
				Iterator iter = textMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					strBuf.append("\r\n").append("--").append(BOUNDARY)
							.append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\""
							+ inputName + "\"\r\n\r\n");
					strBuf.append(inputValue);
				}
				out.write(strBuf.toString().getBytes());
			}

			// file
			if (fileMap != null) {
				Iterator iter = fileMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					File file = new File(inputValue);
					String filename = file.getName();
					String contentType = new MimetypesFileTypeMap()
							.getContentType(file);
					if (filename.endsWith(".png")) {
						contentType = "image/png";
					}
					if (contentType == null || contentType.equals("")) {
						contentType = "application/octet-stream";
					}

					StringBuffer strBuf = new StringBuffer();
					strBuf.append("\r\n").append("--").append(BOUNDARY)
							.append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\""
							+ inputName + "\"; filename=\"" + filename
							+ "\"\r\n");
					strBuf.append("Content-Type:" + contentType + "\r\n\r\n");

					out.write(strBuf.toString().getBytes());

					DataInputStream in = new DataInputStream(
							new FileInputStream(file));
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					in.close();
				}
			}

			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();

			// 读取返回数据
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line).append("\n");
			}
			res = strBuf.toString();
			reader.close();
			reader = null;
		} catch (Exception e) {
			logger.info("发送POST请求出错。" + urlStr);
			logger.error(e.getMessage(),e);
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return res;
	}

	/***
	 * 下载语音
	 * 
	 * @param path
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String downloadFile(String path, String url)
			throws IOException {
		HttpClient client = null;
		String fileName = "";
		try {
			client = new DefaultHttpClient();
			HttpGet httpGet = getHttpGet(url, null, null);
			HttpResponse response = client.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				Header[] hs = response.getAllHeaders();
				for (Header header : hs) {
					if (header.getName().equals("Content-disposition")) {
						fileName = header.getValue().substring(
								header.getValue().indexOf("filename") + 10,
								header.getValue().length() - 1);
						break;
					}
				}
				path = path + fileName;
				byte[] result = EntityUtils.toByteArray(response.getEntity());
				BufferedOutputStream bw = null;
				try {
					File f = new File(path);
					if (!f.getParentFile().exists())
						f.getParentFile().mkdirs();
					bw = new BufferedOutputStream(new FileOutputStream(path));
					bw.write(result);
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
				} finally {
					try {
						if (bw != null)
							bw.close();
					} catch (Exception e) {
						logger.error(e.getMessage(),e);
					}
				}
			} else {
				StringBuffer errorMsg = new StringBuffer();
				errorMsg.append("httpStatus:");
				errorMsg.append(response.getStatusLine().getStatusCode());
				errorMsg.append(response.getStatusLine().getReasonPhrase());
				errorMsg.append(", Header: ");
				Header[] headers = response.getAllHeaders();
				for (Header header : headers) {
					errorMsg.append(header.getName());
					errorMsg.append(":");
					errorMsg.append(header.getValue());
				}
			}
		} catch (ClientProtocolException e) {
			logger.error(e.getMessage(),e);;
		} catch (IOException e) {
			logger.error(e.getMessage(),e);;
		} finally {
			try {
				client.getConnectionManager().shutdown();
			} catch (Exception e) {
				logger.error(e.getMessage(),e);;
			}
		}
		return fileName;
	}

	private static HttpGet getHttpGet(String url, Map<String, String> params,
			String encode) {
		StringBuffer buf = new StringBuffer(url);
		if (params != null) {
			String flag = (url.indexOf('?') == -1) ? "?" : "&";
			for (String name : params.keySet()) {
				buf.append(flag);
				buf.append(name);
				buf.append("=");
				try {
					String param = params.get(name);
					if (param == null) {
						param = "";
					}
					buf.append(URLEncoder.encode(param, encode));
				} catch (UnsupportedEncodingException e) {
					logger.error(e.getMessage(),e);;
				}
				flag = "&";
			}
		}
		HttpGet httpGet = new HttpGet(buf.toString());
		return httpGet;
	}

}
