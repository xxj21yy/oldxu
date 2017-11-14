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
import java.util.Scanner;

/**
 * Created by cc on 2017/8/14.
 */
//通过二级网址
public class TestBMWforOthers {
    private static Logger logger = LoggerFactory.getLogger(TestBMWforOthers.class);


    //宝马与大众jsoup提取内容有差别
    public static void main(String args[]){
        int count = 0;
        int jia = 1;
        Scanner sc;
        String s3=null;
        while (true){
            sc=new Scanner(System.in);
            //获取用户输入的字符串
            System.out.print("请输入网址:");
            String s=sc.nextLine();
            System.out.println("你输入的网址为:"+s+" "+"\r\n"+"确定吗？Y/N");
            String torf=sc.nextLine();
            if ("y".equals(torf)||"Y".equals(torf)){
                s3=s;
                try {
                    Document document333 = connectUrl(s3);
                    String groupId = null;
                    String wheelname = "-";
                    String transmissionname = "-";
                    String yearname = "-";
                    String monthname = "-";
                    String regionname = "chn";

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

                        String s33 = "https://bmw.7zap.com/cat_scripts/get_part_subgroups.php?lang=zh&modification=" + split1[2] + "&modelname=" + split1[4] + "&year=" + yearname + "&month=" + monthname + "&wheel=" + wheelname + "&transmission=" + transmissionname + "&classic=VT&region=" + regionname + "&group=" + groupId + "&_=" + jia;
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
                                fileNameOne = split2[4];//大文件夹
                                fileName = split2[2];//子文件夹
                                jia++;
                                String s8 = "https://bmw.7zap.com/cat_scripts/get_parts.php?lang=zh&modification=" + split2[2] + "&modelname=" + split2[4] + "&year=" + yearname + "&month=" + monthname + "&wheel=" + wheelname + "&transmission=" + transmissionname + "&classic=VT&region=" + regionname + "&group=" + groupId + "&subgroup=" + split2[11] + "&_=" + jia;
                                s8 = s8.replaceAll("'", "").trim();
                                System.out.println(s8);
                                count++;
                                File dir = new File("F:\\BMW\\ceshiforPath\\" + fileNameOne + "\\" + fileName + "\\" + fileName + "-" + count + ".txt");
                                try {
                                    FileUtils.write(dir, s8, "UTF-8");
                                } catch (IOException e) {
                                    File dir2 = new File("F:\\BMW\\bugpath\\" + fileNameOne + "\\" + fileName + " - " + count + ".txt");
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else{
                System.out.print("输入有误，请重新输入!");
                continue;
            }
            break;
        }
    }

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



