package com.hafu.eban.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.hafu.eban.entity.response.UserLoginResponse;

public class HttpClientUtil {
    private static Logger logger = Logger.getLogger(HttpClientUtil.class);

    private static String auth_key = "A511184DE45967A4705B40485BF2CC96A93BA8C7";// 授权的key（必填项）
    private static String loginUrl = "http://capi.9eban.com/user/login";
    private static String name = "6cbe7e898a9fad00";// "哈福网";
    private static String password = "e90fa9292e81b6deee01844f4dad8916";// "hafuwang";
    private static String token = "";

    public static void main(String[] args) {
        try {
            downloadFile("D:\\demo\\1.amr", "http://www.z-smartlink.com/download/voice/2016-05/100100356/861419005.amr");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HttpClientUtil() {
        token = userLogin();
    }

    public static String userLogin() {
        String token = "";
        String str = HttpClientUtil.getToken();
        str = CodeConverter.decodeUnicode2ZH(str);
        try {
            UserLoginResponse user = JSONUtils.fromJson(str, UserLoginResponse.class);
            if (user != null) {
                token = user.getToken();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return token;

    }

    public static String getToken() {
        String result = "";
        String url = loginUrl + "?name=" + name + "&password=" + password + "&header[token]=" + token + "&header[auth_key]=" + auth_key;
        result = baseHttpGet(url);
        return result;
    }

    public String getHttp(String uriAPI) {
        String result = "";
        String url = "";
        String tokenAuthkey = "header[token]=" + token + "&header[auth_key]=" + auth_key;
        if (uriAPI.indexOf("?") != -1) {
            url = uriAPI + "&";
        } else {
            url = uriAPI + "?";
        }
        url = url + tokenAuthkey;
        result = baseHttpGet(url);
        return result;
    }

    public static String baseHttpGet(String url) {
        String result = null;
        // 创建HttpClientBuilder
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // HttpClient
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();

        HttpGet httpRequst = new HttpGet(url);
        logger.debug(">>>>>>>>>>>>> request url :" + httpRequst.getRequestLine().getUri());
        try {
            // 执行get请求
            HttpResponse httpResponse = closeableHttpClient.execute(httpRequst);
            // 获取响应消息实体
            HttpEntity entity = httpResponse.getEntity();
            // 响应状态
            logger.debug(">>>>>>>>>>>>>status:" + httpResponse.getStatusLine());
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 判断响应实体是否为空
                if (entity != null) {
                    logger.debug("contentEncoding:" + entity.getContentEncoding());
                    result = EntityUtils.toString(entity);
                    logger.debug("response content:" + result);
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                // 关闭流并释放资源
                closeableHttpClient.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return result;
    }

    public static String baseHttpPost(String url) {
        String result = null;
        HttpPost httpPost = new HttpPost("http://tutor.sturgeon.mopaas.com/tutor/search");
        httpPost.setConfig(RequestConfig.DEFAULT);
        // 创建参数队列
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("searchText", "英语"));
        UrlEncodedFormEntity entity;
        try {
            entity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httpPost.setEntity(entity);
            result = baseHttpPost(httpPost);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return result;
    }

    public static String baseHttpPost(HttpPost httpPost) {
        String result = null;
        // 创建HttpClientBuilder
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // HttpClient
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        try {
            // post请求
            HttpResponse httpResponse = closeableHttpClient.execute(httpPost);
            // getEntity()
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                // 打印响应内容
                result = EntityUtils.toString(httpEntity, "UTF-8");
            }
            logger.debug(">>> Post result :" + result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                // 释放资源
                closeableHttpClient.close();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return result;
    }

    public String uploadHeadImg(String path, String uriAPI) {
        String result = "";
        HttpPost postRequst = new HttpPost(uriAPI);
        postRequst.setHeader("token", token);
        postRequst.setHeader("auth_key", auth_key);
        try {
            File upfile = new File(path);
            MultipartEntity entity3 = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
            entity3.addPart("avatar", new StringBody(upfile.getName()));
            entity3.addPart("avatar", new FileBody(upfile));
            postRequst.setEntity(entity3);
            result = baseHttpPost(postRequst);
        } catch (Exception e) {
            result = e.getMessage().toString();
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    public String uploadVoiceFile(String path, String uriAPI) {
        String result = "";
        uriAPI = uriAPI + "&header[token]=" + token + "&header[auth_key]=" + auth_key;
        HttpPost postRequst = new HttpPost(uriAPI);
        // httpRequst.setHeader("token", token);
        // httpRequst.setHeader("auth_key", auth_key);
        try {
            File upfile = new File(path);
            MultipartEntity entity3 = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
            entity3.addPart("voice", new StringBody(upfile.getName()));
            entity3.addPart("voice", new FileBody(upfile));
            postRequst.setEntity(entity3);
            result = baseHttpPost(postRequst);
        } catch (Exception e) {
            result = e.getMessage().toString();
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    public static String downloadFile(String path, String url) throws IOException {
        String fileName = "";
        try {
            HttpGet httpGet = getHttpGet(url, null, null);
            // 创建HttpClientBuilder
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            // HttpClient
            CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
            try {
                // post请求
                HttpResponse httpResponse = closeableHttpClient.execute(httpGet);

                if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    Header[] hs = httpResponse.getAllHeaders();
                    for (Header header : hs) {
                        if (header.getName().equals("Content-disposition")) {
                            fileName = header.getValue().substring(header.getValue().indexOf("filename") + 10, header.getValue().length() - 1);
                            break;
                        }
                    }
                    path = path + fileName;
                    byte[] result = EntityUtils.toByteArray(httpResponse.getEntity());
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
                    errorMsg.append(httpResponse.getStatusLine().getStatusCode());
                    errorMsg.append(httpResponse.getStatusLine().getReasonPhrase());
                    errorMsg.append(", Header: ");
                    Header[] headers = httpResponse.getAllHeaders();
                    for (Header header : headers) {
                        errorMsg.append(header.getName());
                        errorMsg.append(":");
                        errorMsg.append(header.getValue());
                    }
                }

            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            } finally {
                try {
                    closeableHttpClient.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return fileName;
    }

    private static HttpGet getHttpGet(String url, Map<String, String> params, String encode) {
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
                    logger.error(e.getMessage(), e);
                }
                flag = "&";
            }
        }
        HttpGet httpGet = new HttpGet(buf.toString());
        return httpGet;
    }
}
