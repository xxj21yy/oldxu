package com.example.demo;

import com.example.demo.util.HttpServiceUtil;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class DemoApplicationTests {

    @Test
    public void contextLoads() throws IOException {
        String domain = "http://www.cn357.com/";
        Document doc = Jsoup.connect("http://www.cn357.com/notice_list").get();
        Element noticeList = doc.getElementById("noticeList");
        Elements notices = noticeList.children();
        for (Element notice : notices) {
            doc = Jsoup.connect(domain + notice.attr("href")).get();
            System.out.println(notice.attr("href") + "\t" + doc.select("#curHeader > div").text());
        }
    }

    @Test
    public void test02() throws IOException, InterruptedException {
        List<String> noticList = new ArrayList<String>();
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
        List<Future<List<String>>> futList = new ArrayList<Future<List<String>>>();

        List<String> lines = FileUtils.readLines(new File("C:\\batchno_count.txt"), "utf-8");
        for (String line : lines) {
            String[] arr = line.split(" ");
            String batchNo = arr[0];
            int count = Integer.parseInt(arr[1]);
            int pageCount = (count + 59) / 60;
            for (int i = 1; i <= pageCount; i++) {
                String url = "http://www.cn357.com/notice_" + batchNo + "_" + i;
                futList.add(fixedThreadPool.submit(new Crawler(url)));
            }
        }
        for (Future<List<String>> listFuture : futList) {
            try {
                noticList.addAll(listFuture.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        FileUtils.writeLines(new File("C:\\result.txt"), noticList);
        fixedThreadPool.shutdown();
    }

    class Crawler implements Callable<List<String>> {
        private String url;

        public Crawler(String url) {
            this.url = url;
        }

        @Override
        public List<String> call() throws Exception {
            System.out.println(url);
            String html = HttpServiceUtil.doGet(url);
            Document doc = Jsoup.parse(html, "gbk");
            Elements elements = doc.select("#noticeLot > div > span.m > a");
            List<String> urls = new ArrayList();
            for (Element element : elements) {
                urls.add(element.attr("href"));
            }
            return urls;
        }
    }
}
