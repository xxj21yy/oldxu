/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.example.demo.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

/**
 * http service接口调用、解析工具类.
 *
 * @author v_gaozhengyang
 * @version 1.0
 * @since 2015年4月23日
 */
public class HttpServiceUtil {
    private static PoolingHttpClientConnectionManager connManager = null;


    static HttpRequestRetryHandler myRetryHandler;

    static {
//    	IpPoolHolder.init(DungProxyContext.create().setPoolEnabled(true));
        myRetryHandler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount >= 4) {
                    return false;
                }
                System.out.println("第 " + executionCount + " 次重试");
                try {
                    Thread.currentThread().sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
                if (idempotent) {
                    return true;
                }
                return true;
            }
        };
        connManager = new PoolingHttpClientConnectionManager();
        connManager.setMaxTotal(20);
        connManager.setDefaultMaxPerRoute(20);
//        HttpHost host = new HttpHost("http://www.cn357.com/notice_list", 80);
//        connManager.setMaxPerRoute(new HttpRoute(host), 10);
    }


    private static String handlerResponse(HttpResponse response) throws UnsupportedOperationException, IOException {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        StringBuilder bulider = new StringBuilder();
        try {
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                is = responseEntity.getContent();
                isr = new InputStreamReader(is, "utf-8");
                br = new BufferedReader(isr);
                String line = "";
                while ((line = br.readLine()) != null) {
                    bulider.append(line + "\n");
                }
            }
        } finally {
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (is != null) {
                is.close();
            }
        }
        return bulider.toString();
    }


    public static String doGet(String url) throws Exception {
        RequestConfig requestConfig = RequestConfig.custom().
                setSocketTimeout(50000).
                setConnectTimeout(50000).
                setConnectionRequestTimeout(50000).build();//设置请求和传输超时时间
        
//        CloseableHttpClient httpClient = HttpClients.custom().
//                setConnectionManager(connManager).
//                setRetryHandler(new DunProxyHttpRequestRetryHandler(myRetryHandler)).setRoutePlanner(new ProxyBindRoutPlanner()).build();
        CloseableHttpClient httpClient = HttpClients.custom().
                setConnectionManager(connManager).
                setRetryHandler(myRetryHandler).build();
        CloseableHttpResponse response = null;
        HttpGet httpget = new HttpGet(url);
        httpget.setConfig(requestConfig);

//        httpget.setHeader("X-Forwarded-For", getIPAddr());
        String result = null;
        try {
            response = httpClient.execute(httpget);
            int status = response.getStatusLine().getStatusCode();
            if (status != HttpStatus.SC_OK) {
                throw new Exception("服务器繁忙：" + status + "\t" + url);
            } else {
                result = handlerResponse(response);
            }
        } finally {
            if (httpget != null) {
                EntityUtils.consume(response.getEntity());
            }
        }
        return result;
    }

    public static byte[] downloadPic(String url) throws Exception {
        CloseableHttpClient httpClient = HttpClients.custom().
                setConnectionManager(connManager).
                setRetryHandler(myRetryHandler).build();
        CloseableHttpResponse response = null;
        HttpGet httpget = new HttpGet(url);
//        httpget.setHeader("Cookie", "CodeLanguePaysOI=zh_CN; PGAC59853388=10; DISPLAYDOCAC59853388=1; BIGipServerNEWAPVPROI_OIN_CITROEN.app~NEWAPVPROI_OIN_CITROEN_pool=529563658.20480.0000; PSACountry=CN; JSESSIONID=0000cm2oTFQIr2QPYSwruiECmSL:174i2j0ii; _pk_id.65.196e=706c2993d2c599f8.1504776405.44.1506417000.1506416981.; _pk_ses.65.196e=*; _pk_id.78.196e=738bdbc5e4179323.1504854299.24.1506417010.1506417004.; _pk_ses.78.196e=*");
//        httpget.setHeader("X-Forwarded-For", getIPAddr());
//        httpget.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
//        httpget.addHeader("Referer","http://service.citroen.com/docapv/affiche.do?ref=080001312010A&refaff=0800%2001%20312010A&idFct=FCT0015");
        byte[] result = null;
        try {
            response = httpClient.execute(httpget);
            int status = response.getStatusLine().getStatusCode();
            if (status != HttpStatus.SC_OK) {
                throw new Exception("服务器繁忙：" + status);
            } else {
                result = EntityUtils.toByteArray(response.getEntity());
            }
        } finally {
            if (httpget != null) {
                EntityUtils.consume(response.getEntity());
            }
        }
        return result;
    }
    
    public static byte[] downloadPic(String url, HttpClientContext context) throws Exception {
    	byte[] result = null;
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        CloseableHttpResponse response = client.execute(get, context);
        int status = response.getStatusLine().getStatusCode();
        if (status != HttpStatus.SC_OK) {
            throw new Exception("服务器繁忙：" + status);
        } else {
            result = EntityUtils.toByteArray(response.getEntity());
        }
        EntityUtils.consume(response.getEntity());
        return result;

    }

    /**
     * Shuts down this HTTP client object, releasing any resources that might be held open. This is an optional method,
     * and callers are not expected to call it, but can if they want to explicitly release any open resources. Once a
     * client has been shutdown, it cannot be used to make more requests.
     */
    public void shutdown() {
        this.connManager.shutdown();
    }

    /**
     * 内存回收时确保连接关闭.
     */
    @Override
    protected void finalize() throws Throwable {
        this.shutdown();
        super.finalize();
    }

    public static String getIPAddr() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append((int) (255 * Math.random())).append(".");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }
}
