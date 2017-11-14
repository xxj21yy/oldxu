package com.example.citroen;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Created by Administrator on 2017/9/11.
 */
public class HttpUtil {

    public static String doGet(String url, HttpClientContext context) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        CloseableHttpResponse response = client.execute(get, context);
        String respCnt = EntityUtils.toString(response.getEntity());
        EntityUtils.consume(response.getEntity());
        return respCnt;

    }

    public static String doPost(String url, HttpClientContext context, List<NameValuePair> formParams) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpEntity entity1 = new UrlEncodedFormEntity(formParams, "utf-8");
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity1);
        CloseableHttpResponse response = client.execute(httpPost, context);
        if (response.getStatusLine().getStatusCode() == 302) {
            String locationUrl = response.getLastHeader("Location").getValue();
            return doGet(locationUrl, context);//跳转到重定向的url
        }
        String respCnt = EntityUtils.toString(response.getEntity());
        EntityUtils.consume(response.getEntity());
        return respCnt;
    }
}
