package com.example.demo.controller;

import com.example.demo.util.HttpServiceUtil;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.ws.RequestWrapper;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2017/5/26 0026.
 */
@Controller
public class IndexController {


    static AtomicInteger counter = new AtomicInteger(614380);

    @RequestMapping("/")
    @ResponseBody
    public String index() {
        return new Date().toString();
    }

    @RequestMapping("/start")
    @ResponseBody
    public void start() throws IOException {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
        List<Future<String[]>> futList = new ArrayList<Future<String[]>>();
        File[] files = new File("/data/notice").listFiles();
        Map<String, Boolean> existedMap = new HashMap<String, Boolean>();
        for (File file : files) {
            existedMap.put(file.getName(), true);
        }
        List<String> lines = FileUtils.readLines(new File("/data/result.txt"), "utf-8");
        for (String line : lines) {
            if (existedMap.get(line) == null) {
                futList.add(fixedThreadPool.submit(new Crawler(line)));
            } else {
                counter.decrementAndGet();
            }
        }
        for (Future<String[]> listFuture : futList) {
            try {
                String[] arr = listFuture.get();
                FileUtils.write(new File("/data/notice/" + arr[0]), arr[1], "gbk");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        fixedThreadPool.shutdown();
    }

    class Crawler implements Callable<String[]> {
        private String url;

        public Crawler(String url) {
            this.url = url;
        }

        @Override
        public String[] call() throws Exception {
            System.out.println(counter.decrementAndGet()+"\t"+new Date());
            String html = HttpServiceUtil.doGet("http://www.cn357.com/" + url);
            Document doc = Jsoup.parse(html, "gbk");
            Elements elements = doc.select("#noticeModel > div.cvMain > div.wrapper.clear > div.fl690.mt10 > table");
            if (elements.size() > 0) {
                return new String[]{url, elements.get(0).outerHtml()};
            }

            return new String[]{url, ""};
        }
    }
}
