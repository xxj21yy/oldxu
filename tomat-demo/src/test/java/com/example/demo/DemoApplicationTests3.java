package com.example.demo;

import com.example.demo.util.HttpServiceUtil;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DemoApplicationTests3 {


    static int counter = 0;


    @Test
    public void test01() throws IOException, InterruptedException {
        File file = new File("C:\\datas\\notice46731_BJ1020V3JB4");
        Document doc = Jsoup.parse(file, "GBK");
        Element compName = doc.select(".noticeAttr > tbody > tr:nth-child(17) > td:nth-child(4)").get(0);
        System.out.println(compName.text());
    }

    @Test
    public void test02() throws IOException, InterruptedException {
        File compNameFile = new File("c:\\noticeInfo.txt");
        Writer writer = new BufferedWriter(new FileWriter(new File("c:\\noticeInfo.txt")));
        compNameFile.delete();
        File[] files = new File("C:\\datas").listFiles();
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(50);
        List<Future<String>> futList = new ArrayList<Future<String>>();
        for (File f : files) {
            futList.add(fixedThreadPool.submit(new Parser(f)));
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

                System.out.println(counter++ + "\t" + new Date());

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
            } catch (Exception e) {
                e.printStackTrace();
            }
            return sb.toString();
        }
    }
}
