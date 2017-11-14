package com.example.demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by cc on 2017/6/29.
 */
public class ThreadTestCCLocal {
    private static Logger logger = LoggerFactory.getLogger(ThreadTestCC.class);
    static int counter = 0;
    //多线程
    @Test
    public void testLocal() throws IOException, InterruptedException {
        File compNameFile = new File("F:\\新 - 本地网址.txt");
        Writer writer = new BufferedWriter(new FileWriter(compNameFile));
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(50);
        List<Future<String>> futList = new ArrayList<Future<String>>();
        //递归文件夹下的所有文件
        File fileFile = new File("F:\\LocalPa");
        String[] filelist = fileFile.list();
        for (int i = 0; i < filelist.length; i++) {
            File readfile = new File("F:\\LocalPa" + "\\" + filelist[i]);
            if (!readfile.isDirectory()) {
                String s = readfile.getAbsolutePath();
                //继续读取文件
                String filePath = s;
                String encoding = "GBK";
                File file=new File(filePath);
                if(file.isFile() && file.exists()) { //判断文件是否存在
                    InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);//考虑到编码格式
                    BufferedReader bufferedReader = new BufferedReader(read);
                    String lineTxt = null;
                    while ((lineTxt = bufferedReader.readLine()) != null) {
                        String searchXingHao = lineTxt;
                        futList.add(fixedThreadPool.submit(new ThreadTestCCLocal.Parser(s)));
                    }


                    for (Future<String> listFuture : futList) {
                        try {
                            writer.write(listFuture.get() + "\r\n");
                        }catch (Exception e) {
                            System.out.println("出现异常");
                            e.printStackTrace();
                        }
                    }
                    writer.flush();
                    writer.close();
                    fixedThreadPool.shutdown();
                    System.out.println("线程关闭，操作结束");
                }
            }
        }
    }

    class Parser implements Callable<String> {
        private String s;

        public Parser(String s) {
            this.s = s;
        }

        @Override
        public String call() throws Exception {
            StringBuilder sb = new StringBuilder();
            String src=null;
            String text=null;
            try {
                File input = new File(s);
                Document doc = Jsoup.parse(input, "GBK", "");
                Elements noticeItem = doc.getElementsByClass("noticeAttr mt5");
                text = noticeItem.select("[class='c']").text();
                System.out.println("公告型号:"+text);
                Elements imgEles = doc.select("#noticeImage > div > img");
                if (imgEles!=null&&!"".equals(imgEles)){
                    for (Element imgEle : imgEles) {
                        System.out.println(text+":"+imgEle.attr("src"));
                        sb.append(imgEle.attr("src")).append(";");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(text+"/"+e.getMessage());
            }
            return sb.toString();
        }
    }


    /*@Test
    public void testLocal2() throws IOException {
        File input = new File("F://LocalPa/notice10000_HY4160F8");
        Document doc = Jsoup.parse(input, "GBK", "");
        //System.out.println(doc);
        //Elements imgEles = doc.select("#noticeImage > div > img");
        Elements noticeItem = doc.getElementsByClass("noticeAttr mt5");
        String text = noticeItem.select("[class='c']").text();
        System.out.println("公告型号:"+text);
        Elements imgEles = doc.select("#noticeImage > div > img");
        if (imgEles!=null&&!"".equals(imgEles)){
            for (Element imgEle : imgEles) {
                String src = imgEle.attr("src");
                System.out.println("网址:"+src);
            }
        }
    }*/
}

