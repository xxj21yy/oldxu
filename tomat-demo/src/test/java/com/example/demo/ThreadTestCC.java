package com.example.demo;

import com.example.demo.util.HttpServiceUtil;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by cc on 2017/6/27.
 */
public class ThreadTestCC {
    private static Logger logger = LoggerFactory.getLogger(ThreadTestCC.class);
    static int counter = 0;
    //多线程
    @Test
    public void test() throws IOException, InterruptedException {
        File compNameFile = new File("F:\\新 - 副本.txt");
        Writer writer = new BufferedWriter(new FileWriter(compNameFile));
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(50);
        List<Future<String>> futList = new ArrayList<Future<String>>();
        String filePath = "F:\\公告目录--图形.txt";
        String encoding="GBK";
        File file=new File(filePath);
        if(file.isFile() && file.exists()) { //判断文件是否存在
            InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file), encoding);//考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                String searchXingHao = lineTxt;
                futList.add(fixedThreadPool.submit(new ThreadTestCC.Parser(searchXingHao)));
            }


        for (Future<String> listFuture : futList) {
            try {
                //String obj = listFuture.get(1000 * 3, TimeUnit.MILLISECONDS); //任务处理超时时间设为 1 秒
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
    class Parser implements Callable<String> {
        private String xingHao;

        public Parser(String xingHao) {
            this.xingHao = xingHao;
        }


        @Override
        public String call() throws Exception {
            StringBuilder sb = new StringBuilder();
            String src=null;
            try {
                Document document;
                if (xingHao!=null&&!"".equals(xingHao)){
                    if (xingHao.contains("(")) {
                        String substring1 = xingHao.substring(0, xingHao.indexOf("("));
                        document = Jsoup.connect("http://www.cn357.com/cvi.php?m=cvinotice&search=n&model=" + substring1).timeout(10000).get();
                    } else {
                        document = Jsoup.connect("http://www.cn357.com/cvi.php?m=cvinotice&search=n&model=" + xingHao).timeout(10000).get();
                    }
                    if(document!=null&&!"".equals(document)){
                        Elements noticeItem = document.getElementsByClass("noticeItem");
                        if (noticeItem!=null&&!"".equals(noticeItem)){
                            String href = noticeItem.select("a[href]").get(0).attr("href");//带有href属性的a元素
                            Document document2;
                            if(href!=null&&!"".equals(href)){
                                if (href.contains("(")) {
                                    String substring = href.substring(0, href.indexOf("("));
                                    document2 = Jsoup.connect("http://www.cn357.com" + substring).timeout(10000).get();
                                } else if (href.contains(".")) {
                                    String substring = href.substring(0, href.indexOf("."));
                                    document2 = Jsoup.connect("http://www.cn357.com" + substring).timeout(10000).get();
                                } else {
                                    document2 = Jsoup.connect("http://www.cn357.com" + href).timeout(10000).get();
                                }
                                Elements imgEles = document2.select("#noticeImage > div > img");
                                if (imgEles!=null&&!"".equals(imgEles)){
                                    src=xingHao+":";
                                    sb.append(src);
                                    for (Element imgEle : imgEles) {
                                        System.out.println(xingHao+":"+imgEle.attr("src"));
                                        sb.append(imgEle.attr("src")).append(";");
                                    }
                                }
                            }
                        }
                    }else{
                        System.out.println("该页搜索不到");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(xingHao+"/"+e.getMessage());
            }
            return sb.toString();
        }
    }
}

