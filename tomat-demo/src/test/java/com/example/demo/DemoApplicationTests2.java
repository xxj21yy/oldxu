package com.example.demo;

import com.example.demo.util.HttpServiceUtil;
import org.apache.commons.io.FileUtils;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DemoApplicationTests2 {


    static AtomicInteger counter = new AtomicInteger(614380);

    @Test
    public void test02() throws IOException, InterruptedException {

        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(20);
        List<Future<String>> futList = new ArrayList<Future<String>>();
        File[] files = new File("C:\\datas").listFiles();
        Map<String, Boolean> existedMap = new HashMap<String, Boolean>();
        for (File file : files) {
            existedMap.put(file.getName(), true);
        }
        List<String> lines = FileUtils.readLines(new File("C:\\result.txt"), "utf-8");
        for (String line : lines) {
            if (existedMap.get(line) == null) {
                futList.add(fixedThreadPool.submit(new Crawler(line)));
            } else {
                counter.decrementAndGet();
            }
        }

        for (Future<String> listFuture : futList) {
            try {
                listFuture.get();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        fixedThreadPool.shutdown();
    }

    class Crawler implements Callable<String> {
        private String url;

        public Crawler(String url) {
            this.url = url;
        }

        @Override
        public String call() throws Exception {
            System.out.println(counter.decrementAndGet());
            String html = HttpServiceUtil.doGet("http://www.cn357.com/" + url);
            FileUtils.write(new File("C:\\datas\\" + url), html, "gbk");
            return null;
        }
    }
}
