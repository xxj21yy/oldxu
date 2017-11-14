package com.example.demo;

import com.example.demo.util.HttpServiceUtil;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

public class DemoApplicationTests4 {


    static int counter = 0;


    @Test
    public void test01() throws Exception {
        List<String> lines = FileUtils.readLines(new File("C:\\notice_batch.txt"), "utf-8");
        final String template = "http://www.cn357.com/cvi.php?m=cvinotice&search=n&model=$1&lot=$2";

        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);

        for (String line : lines) {
            final String[] arr = line.split("\t");
            fixedThreadPool.submit(new Runnable() {
                @Override
                public void run() {
                    String html = null;
                    try {
                        html = HttpServiceUtil.doGet(template.replace("$1", arr[0].replace("\\ufeff", "")).replace("$2", arr[1]));
                        Document doc = Jsoup.parse(html);
                        String url = doc.select("body > div.cvMain > div.wrapper.clear > div.fl690.mt10 > div.noticeItem > a").attr("href");
                        System.out.println(arr[0] + "\t" + arr[1] + "\t" + url);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        Thread.currentThread().sleep(200000);
        fixedThreadPool.shutdown();
    }

    @Test
    public void test02() throws IOException, InterruptedException {
        File compNameFile = new File("c:\\noticeInfo-error.txt");
        Writer writer = new BufferedWriter(new FileWriter(compNameFile));
        compNameFile.delete();
        List<String> lines = FileUtils.readLines(new File("C:\\url-data.txt"), "utf-8");
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(50);
        List<Future<String>> futList = new ArrayList<Future<String>>();
        for (String line : lines) {
            String[] arr = line.split("\t");
            if (arr.length < 3) {
                System.out.println(line);
            }
            if (line.split("\t").length < 3) {
                System.out.println(line);
            } else {
                File f = new File("C:\\datas\\" + line.split("\t")[2].replaceAll("[/-]", ""));
                futList.add(fixedThreadPool.submit(new Parser(f)));
            }
        }
        for (Future<String> listFuture : futList) {
            try {
                writer.write(listFuture.get() + "\r\n");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        writer.flush();
        writer.close();
        fixedThreadPool.shutdown();
        System.out.println("操作结束");
    }

    class Parser implements Callable<String> {
        private File file;

        public Parser(File file) {
            this.file = file;
        }

        @Override
        public String call() throws Exception {
            StringBuilder sb = new StringBuilder();
            try {

//                System.out.println(counter++ + "\t" + new Date());

                Document doc = Jsoup.parse(file, "GBK");
                Element tbody = doc.select(".noticeAttr > tbody").get(0);
                int trCount = tbody.select(">tr").size();
                for (int i = 1; i <= 21; i++) {
                    Element ele01 = tbody.select(">tr:nth-child(" + i + ") > td:nth-child(2)").get(0);
                    Element ele02 = tbody.select(">tr:nth-child(" + i + ") > td:nth-child(4)").get(0);
                    sb.append("\"").append(ele01.text().replaceAll("\"", "")).append("\",");
                    sb.append("\"").append(ele02.text().replaceAll("\"", "")).append("\",");
                }
                if (trCount == 23) {
                    for (int i = 1; i <= 5; i++) {
                        Element ele = tbody.select(">tr:nth-child(22) > td > table > tbody > tr:nth-child(2) > td:nth-child(" + i + ")").get(0);
                        sb.append("\"").append(ele.text().replaceAll("\"", "")).append("\",");
                    }
                } else {
                    sb.append("\"").append("\",");
                    sb.append("\"").append("\",");
                    sb.append("\"").append("\",");
                    sb.append("\"").append("\",");
                    sb.append("\"").append("\",");
                }
                Element markEle = tbody.select(">tr:nth-child(" + trCount + ") > td:nth-child(2)").get(0);
                sb.append("\"").append(markEle.text().replaceAll("\"", "")).append("\"");
                if (sb.length() == 0) {
                    System.out.println(file.getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return sb.toString();
        }
    }
}
