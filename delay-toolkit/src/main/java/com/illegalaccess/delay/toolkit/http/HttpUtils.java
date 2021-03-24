package com.illegalaccess.delay.toolkit.http;

import com.illegalaccess.delay.toolkit.json.JsonTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;


public class HttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    public enum HTTP_TYPE {
        GET, POST

    }

    private HttpUtils() {
        throw new IllegalStateException("can not construct HttpUtils");
    }

    public static final String httpGet(String url, String params, Map<String, String> header) {
        StringBuilder sb = new StringBuilder();
        if (url.endsWith("?")) {
            sb.append(url).append(params);
        } else {
            sb.append(url).append("?").append(params);
        }
        return doAccess(sb.toString(), HTTP_TYPE.GET, null, header, null);
    }

    public static final <T> String httpPost(String url, T obj, Map<String, String> header) {
        String data = JsonTool.toJsonString(obj);
        return doAccess(url, HTTP_TYPE.POST, data, header, null);
    }

    public static final void asyncHttpGet(String url, String params, Map<String, String> header, HttpCallback httpCallback) {
        StringBuilder sb = new StringBuilder();
        if (url.endsWith("?")) {
            sb.append(url).append(params);
        } else {
            sb.append(url).append("?").append(params);
        }
        new Thread(() -> doAccess(sb.toString(), HTTP_TYPE.GET, null, header, httpCallback)).start();
    }

    public static final <T> String asyncHttpPost(String url, T obj, Map<String, String> header, HttpCallback httpCallback) {
        String data = JsonTool.toJsonString(obj);
        new Thread(() -> doAccess(url, HTTP_TYPE.POST, data, header, null)).start();
        return null;
    }

    private static final String doAccess(String urlString, HTTP_TYPE httpType, String data, Map<String, String> header, HttpCallback httpCallback) {
        URL url;
        HttpURLConnection httpURLConnection = null;
        String response = "";
        boolean success = true;
        try {
            // 根据URL地址创建URL对象
            url = new URL(urlString);
            // 获取HttpURLConnection对象
            httpURLConnection = (HttpURLConnection) url.openConnection();

            // 设置连接超时
            httpURLConnection.setConnectTimeout(10000);
            // 设置读取超时
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setRequestProperty("Accept-Language", "zh-CN");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            httpURLConnection.setDoInput(true); // 向连接中写入数据
            httpURLConnection.setDoOutput(true); // 从连接中读取数据
            httpURLConnection.setUseCaches(false); // 禁止缓存

            if (header != null && !header.isEmpty()) {
                HttpURLConnection finalHttpURLConnection = httpURLConnection;
                header.forEach((k, v) -> {
                    finalHttpURLConnection.setRequestProperty(k, v);
                });
            }
            switch (httpType) {
                case GET:
                    httpURLConnection.setRequestMethod("GET");// 设置请求类型为
                    break;
                case POST:
                    httpURLConnection.setRequestMethod("POST");// 设置请求类型为
                    if (data != null && !"".equals(data)) {
                        DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream()); // 获取输出流
                        out.write(data.getBytes("utf-8"));// 将要传递的数据写入数据输出流,不要使用out.writeBytes(param); 否则中文时会出错
                        out.flush(); // 输出缓存
                        out.close(); // 关闭数据输出流
                    }

                    break;
                default:
                    break;
            }

            // 响应码为200表示成功，否则失败。
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                logger.error("请求:{}失败", urlString);
                success = false;
            }
            // 获取网络的输入流
            InputStream is = httpURLConnection.getInputStream();
            // 读取输入流中的数据
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int len = -1;
            while ((len = bis.read(bytes)) != -1) {
                baos.write(bytes, 0, len);
            }
            bis.close();
            is.close();
            // 响应的数据
            response= baos.toString("utf-8");
            logger.info("resp:{} from:{}", response, urlString);

        } catch (MalformedURLException e) {
            logger.error("access exception", e);
            e.printStackTrace();
            if (httpCallback != null) {
                // 回调onError()方法
                httpCallback.onError("MalformedURLException", e);
            }
        } catch (IOException e) {
            logger.error("access exception", e);
            e.printStackTrace();
            if (httpCallback != null) {
                // 回调onError()方法
                httpCallback.onError("IOException", e);
            }
        } finally {
            if (httpURLConnection != null) {
                // 释放资源
                httpURLConnection.disconnect();
            }
        }
        if (httpCallback != null) {
            // 回调onFinish()方法
            if (success) {
                httpCallback.onSuccess(response);
            } else {
                httpCallback.onFail(response);
            }
        }
        return response;
    }

}
