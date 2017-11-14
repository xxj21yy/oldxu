package com.example.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by cc on 2017/6/26.
 */
public class TestJsoup {
    @Test
    public void testJsoup() throws IOException {
        //Document doc = Jsoup.connect("http://www.cn357.com/notice").get();
        String xingHao="HFJ7110CE4";
        String searchXingHao=xingHao;
        Document document = Jsoup.connect("http://www.cn357.com/cvi.php?m=cvinotice&search=n&model="+searchXingHao).get();
        //String title1 = document.title();
        Elements noticeItem = document.getElementsByClass("noticeItem");
        String href = noticeItem.select("a[href]").get(0).attr("href");//带有href属性的a元素
        System.out.println(href);
        Document document2 = Jsoup.connect("http://www.cn357.com"+href).get();
        //String s = document2.baseUri();
        //Element noticeImage = document2.getElementById("noticeImage");
        //Elements select = noticeItem.select("img[src]");
        Elements imgEles = document2.select("#noticeImage > div > img");
        for (Element imgEle : imgEles) {
            System.out.println(imgEle.attr("src"));
        }

    }
}
