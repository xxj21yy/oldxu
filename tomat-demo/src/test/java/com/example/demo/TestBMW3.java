package com.example.demo;

import com.example.demo.util.HttpServiceUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by cc on 2017/8/9.
 */
public class TestBMW3 {
    private static Logger logger = LoggerFactory.getLogger(TestBMW3.class);
    int count = 0;
    int downloadCnt = 0;
    int num = 0;

    //宝马与大众jsoup提取内容有差别
    @Test
    public void test() {

        int jia = 1;
        String s = "https://bmw.7zap.com/cat_scripts/get_models.php?lang=en&mark=bmw&classic=VT&_=" + jia;
        Document document = connectUrl(s);

        Elements select = document.select("div[class='table-responsive']");
//        Elements select3 = select.select("tr[onmouseout='this.classList.remove('model_select');']");
        Elements select3 = document.select("#collapse_1 > div > div > table > tbody > tr");
        for (Element element66 : select3) {
            String onclick = element66.attr("onclick");
            String groupId = null;
            String replace = null;
            if (onclick != null && !"".equals(onclick)) {
                String[] split = getValue(onclick);
                jia++;
                Document document333 = null;
                String s1 = "https://bmw.7zap.com/cat_scripts/get_model.php?lang=zh&modification=" + split[2] + "&modelname=" + split[4] + "&year=-&wheel=-&transmission=-&classic=" + split[3] + "&region=-&month=-&_=" + jia;
                String s2 = s1.replaceAll("'", "");
                Document document1 = connectUrl(s2);

                Elements select4 = document1.select("div.btn-group");
                String wheelname = "-";
                String transmissionname = "-";
                String yearname = "-";
                String monthname = "-";
                String regionname = "-";
                //获取wheelname
                Elements select1 = select4.select("#wheelbuttons");
                Elements select2 = select1.select("label");
                for (Element element1 : select2) {
                    wheelname = element1.select("input[name='wheelname']").attr("value");
                    String s38 = "https://bmw.7zap.com/cat_scripts/get_model.php?lang=en&modification=" + split[2] + "&modelname=" + split[4] + "&year=" + yearname + "&wheel=" + wheelname + "&transmission=" + transmissionname + "&classic=VT&region=" + regionname + "&month=" + monthname + "&_=" + jia;
                    Document document2 = connectUrl(s38);
                    Elements select8 = document2.select("div.btn-group");
                    if (select8 != null && !"".equals(select8) && select8.size() > 0) {
                        //获取transmissionname
                        Elements select5 = select8.select("#transmissionbuttons");
                        Elements label = select5.select("label");
                        for (Element element3 : label) {
                            String aClass = element3.attr("class");
                            if (!"btn btn-default disabled".equals(aClass)) {
                                transmissionname = element3.select("input[name='transmissionname']").attr("value");
                                String s48 = "https://bmw.7zap.com/cat_scripts/get_model.php?lang=en&modification=" + split[2] + "&modelname=" + split[4] + "&year=" + yearname + "&wheel=" + wheelname + "&transmission=" + transmissionname + "&classic=VT&region=" + regionname + "&month=" + monthname + "&_=" + jia;
                                Document document3 = connectUrl(s48);
                                Elements select9 = document3.select("div.btn-group");

                                //获取yearname
                                Elements select6 = select9.select("#yearbuttons");
                                Elements label1 = select6.select("label");
                                for (Element element5 : label1) {
                                    String aClass1 = element5.attr("class");
                                    if (!"btn btn-default disabled".equals(aClass1)) {
                                        yearname = element5.select("input[name='yearname']").attr("value");
                                        String s58 = "https://bmw.7zap.com/cat_scripts/get_model.php?lang=en&modification=" + split[2] + "&modelname=" + split[4] + "&year=" + yearname + "&wheel=" + wheelname + "&transmission=" + transmissionname + "&classic=VT&region=" + regionname + "&month=" + monthname + "&_=" + jia;
                                        Document document4 = connectUrl(s58);
                                        Elements select10 = document4.select("div.btn-group");

                                        //获取monthname
                                        Elements select7 = select10.select("#monthbuttons");
                                        Elements label2 = select7.select("label");
                                        for (Element element : label2) {
                                            String aClass2 = element.attr("class");
                                            if (!"btn btn-default disabled".equals(aClass2)) {
                                                monthname = element.select("input[name='monthname']").attr("value");
                                                String s68 = "https://bmw.7zap.com/cat_scripts/get_model.php?lang=en&modification=" + split[2] + "&modelname=" + split[4] + "&year=" + yearname + "&wheel=" + wheelname + "&transmission=" + transmissionname + "&classic=VT&region=" + regionname + "&month=" + monthname + "&_=" + jia;
                                                Document document5 = connectUrl(s68);
                                                Elements select11 = document5.select("div.btn-group");

                                                //获取regionname
                                                Elements select12 = select11.select("#regionbuttons");
                                                Elements label3 = select12.select("label");
                                                for (Element element2 : label3) {
                                                    String aClass3 = element2.attr("class");
                                                    if (!"btn btn-default disabled".equals(aClass3)) {
                                                        regionname = element2.select("input[name='regionname']").attr("value");

                                                        String s3 = "https://bmw.7zap.com/cat_scripts/get_part_groups.php?lang=zh&modification=" + split[2] + "&modelname=" + split[4] + "&year=" + yearname + "&month=" + monthname + "&wheel=" + wheelname + "&transmission=" + transmissionname + "&classic=VT&region=" + regionname + "&group=" + groupId + "&_=" + jia;
                                                        String s4 = s3.replaceAll("'", "");
                                                        document333 = connectUrl(s4);
                                                        //得到三级页面
                                                        Elements select88 = document333.select("div[class='panel-group  col-xs-48 col-sm-12 col-md-8 col-lg-8']");
                                                        String s44 = null;
                                                        for (Element element111 : select88) {
                                                            Elements select98 = element111.select("div[onmouseout=' this.className='panel panel-default';']");
                                                            String onclick1 = select98.attr("onclick");
                                                            String[] split1 = getValue2(onclick1);
                                                            jia++;
                                                            groupId = split1[10].trim();

                                                            String s33 = "https://bmw.7zap.com/cat_scripts/get_part_subgroups.php?lang=zh&modification=" + split[2] + "&modelname=" + split[4] + "&year=" + yearname + "&month=" + monthname + "&wheel=" + wheelname + "&transmission=" + transmissionname + "&classic=VT&region=" + regionname + "&group=" + groupId + "&_=" + jia;
                                                            s44 = s33.replaceAll("'", "").trim();
                                                            //最终页面的获取
                                                            Document document288 = connectUrl(s44);

                                                            Elements select22 = document288.select("body > div.row > div > div");
                                                            String substring3 = null;
                                                            String fileNameOne = null;
                                                            String fileName = null;
                                                            String str = null;
                                                            for (Element element77 : select22) {
                                                                String onclick77 = element77.attr("onclick");
                                                                if (onclick77 != null && !"".equals(onclick77)) {
                                                                    String[] split2 = getValue2(onclick77);
                                                                    fileNameOne=split2[4];//大文件夹
                                                                    fileName = split2[2];//子文件夹
                                                                    jia++;
                                                                    String s8 = "https://bmw.7zap.com/cat_scripts/get_parts.php?lang=zh&modification=" + split2[2] + "&modelname=" + split2[4] + "&year=" + yearname + "&month=" + monthname + "&wheel=" + wheelname + "&transmission=" + transmissionname + "&classic=VT&region=" + regionname + "&group=" + groupId + "&subgroup=" + split2[11] + "&_=" + jia;
                                                                    s8 = s8.replaceAll("'", "").trim();
                                                                    System.out.println(s8);
                                                                    count++;
                                                                    File dir = new File("F:\\BMW\\path\\" + fileNameOne+"\\"+fileName + "\\" + fileName + "-" + count + ".txt");
                                                                    try {
                                                                        FileUtils.write(dir, s8, "UTF-8");
                                                                    } catch (IOException e) {
                                                                        File dir2 = new File("F:\\Dazhong\\Europa\\bugpath\\" + fileNameOne+"\\" + fileName + " - " + count + ".txt");
                                                                        try {
                                                                            FileUtils.write(dir2, s8, "UTF-8");
                                                                        } catch (IOException e1) {
                                                                            logger.error("网址" + s8 + "出错了，问题是" + e1.getMessage());
                                                                        }
                                                                        logger.error("网址" + s8 + "出错了，问题是" + e.getMessage());
                                                                    }
                                                                }
                                                            }

                                                        }
                                                        //System.out.println(wheelname + " " + transmissionname + " " + yearname+" "+monthname+" "+regionname);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //HttpServiceUtil连接网址
    public static Document connectUrl(String url) {
        Document document = null;
        int cnt = 0;
        cnt++;
        if (url.contains(" ")) {
            url = url.replaceAll(" ", "");
        }
        try {
            document = Jsoup.parse(HttpServiceUtil.doGet(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return document;
    }

    //整合取值onclick方法
    public static String[] getValue(String onclick) {
        String substring = onclick.substring(onclick.indexOf("(") + 1, onclick.length() - 1);
        String[] split = substring.split("','");
        String substring1 = null;
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            if (s.contains("'")) {
                substring1 = s.substring(s.indexOf("'") + 1, s.length()).trim();
            } else {
                substring1 = s.trim();
            }
            split[i] = substring1;
        }
        return split;
    }

    //整合取值onclick方法
    public static String[] getValue2(String onclick) {
        String substring = onclick.substring(onclick.indexOf("(") + 1, onclick.length() - 1);
        String s2 = substring.replaceAll(",", "','");
        String[] split = s2.split("','");
        String substring1 = null;
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            if (s.contains("'")) {
                substring1 = s.substring(s.indexOf("'") + 1, s.length()).trim();
            } else {
                substring1 = s.trim();
            }
            split[i] = substring1.replaceAll("'", "").trim();
        }
        return split;
    }
}



