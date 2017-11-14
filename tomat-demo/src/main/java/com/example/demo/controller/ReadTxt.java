package com.example.demo.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by cc on 2017/6/26.
 */
public class ReadTxt{
    public static void main(String argv[]){
        String filePath = "F:\\公告目录--图形.txt";
//      "res/";
        try {
            //String filePath = "F:\\公告目录--图形2.txt";
            String encoding="GBK";
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                //写入
                File outFile = new File("F:\\新.txt");
                OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(outFile), encoding);
                BufferedWriter out = new BufferedWriter(write);

                String lineTxt = null;
                    while((lineTxt = bufferedReader.readLine()) != null){
                        String searchXingHao=lineTxt;
                    System.out.println(lineTxt);
                    out.write(lineTxt+"\r\n");
                    //String str = "sdfs#d";
                    Document document;
                    if (searchXingHao.contains("(")){
                        String substring1 = searchXingHao.substring(0, searchXingHao.indexOf("("));
                        document = Jsoup.connect("http://www.cn357.com/cvi.php?m=cvinotice&search=n&model="+substring1).get();
                    }else {
                        document = Jsoup.connect("http://www.cn357.com/cvi.php?m=cvinotice&search=n&model="+searchXingHao).get();
                    }
                    Elements noticeItem = document.getElementsByClass("noticeItem");
                    String href = noticeItem.select("a[href]").get(0).attr("href");//带有href属性的a元素
                    Document document2;
                    if (href.contains("(")){
                        String substring = href.substring(0, href.indexOf("("));
                        document2 = Jsoup.connect("http://www.cn357.com"+substring).get();
                    }else if(href.contains(".")){
                        String substring = href.substring(0, href.indexOf("."));
                        document2 = Jsoup.connect("http://www.cn357.com"+substring).get();
                    }else {
                        document2 = Jsoup.connect("http://www.cn357.com"+href).get();
                    }
                    Elements imgEles = document2.select("#noticeImage > div > img");
                    for (Element imgEle : imgEles) {
                        System.out.println(imgEle.attr("src"));
                        String src = imgEle.attr("src");
                        out.write(src+"\r\n");
                        out.flush();
                    }

                }
                read.close();
                write.close();

            }else{
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        //readTxtFile(filePath);
    }





    public void readText(String xingHao) throws IOException {
        String filePath = "F:\\公告目录--图形2.txt";
        String encoding = "GBK";
        File file = new File(filePath);
        if (file.isFile() && file.exists()) { //判断文件是否存在
            InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file), encoding);//考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                String searchXingHao = xingHao;
                Document document;
                if (searchXingHao.contains("(")) {
                    String substring1 = searchXingHao.substring(0, searchXingHao.indexOf("("));
                    document = Jsoup.connect("http://www.cn357.com/cvi.php?m=cvinotice&search=n&model=" + substring1).get();
                } else {
                    document = Jsoup.connect("http://www.cn357.com/cvi.php?m=cvinotice&search=n&model=" + searchXingHao).get();
                }
                Elements noticeItem = document.getElementsByClass("noticeItem");
                String href = noticeItem.select("a[href]").get(0).attr("href");//带有href属性的a元素
                Document document2;
                if (href.contains("(")) {
                    String substring = href.substring(0, href.indexOf("("));
                    document2 = Jsoup.connect("http://www.cn357.com" + substring).get();
                } else if (href.contains(".")) {
                    String substring = href.substring(0, href.indexOf("."));
                    document2 = Jsoup.connect("http://www.cn357.com" + substring).get();
                } else {
                    document2 = Jsoup.connect("http://www.cn357.com" + href).get();
                }
                Elements imgEles = document2.select("#noticeImage > div > img");
                for (Element imgEle : imgEles) {
                    String src = imgEle.attr("src");
                    System.out.println(lineTxt + ":" + imgEle.attr("src"));
                }
            }
        }
    }
    //jsoup
    public void testJsoup(String xingHao) throws IOException {
        String searchXingHao=xingHao;
        Document document = Jsoup.connect("http://www.cn357.com/cvi.php?m=cvinotice&search=n&model="+searchXingHao).get();
        Elements noticeItem = document.getElementsByClass("noticeItem");
        String href = noticeItem.select("a[href]").get(0).attr("href");//带有href属性的a元素
        Document document2 = Jsoup.connect("http://www.cn357.com"+href).get();
        Elements imgEles = document2.select("#noticeImage > div > img");
        for (Element imgEle : imgEles) {
            System.out.println(imgEle.attr("src"));
        }

    }

}
