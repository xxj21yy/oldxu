package com.example.demo;

import com.example.demo.util.HttpServiceUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by cc on 2017/7/3.
 */
public class TestPassat22 {
    private static Logger logger = LoggerFactory.getLogger(TestPassat22.class);
    int count = 38260;//相等
    @Test
    public void test() throws IOException, InterruptedException {

        StringBuilder stringBuilder = new StringBuilder();
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(7);
        List<Future<String>> futList = new ArrayList<Future<String>>();
        stringBuilder.append("https://volkswagen.7zap.com/cn/svw/");//中国SVW
        //stringBuilder.append("https://volkswagen.7zap.com/cn/cn/");//中国FAW
        int jia = 39337;//-2
//        String s = "https://volkswagen.7zap.com/cat_scripts/models_get.php?lang=cn&region=SVW&_=" + jia;
        String s = "https://volkswagen.7zap.com/cat_scripts/models_get.php?lang=cn&region=CN&_=" + jia;
//        Document document = Jsoup.connect(s).get();
        Document document=null;
        try {
            if (s.contains(" ")){
                s = s.replace(" ", "");
            }
            document = Jsoup.parse(HttpServiceUtil.doGet(s));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
//        Elements select = document.select("div[class='table-responsive']");
//        Elements select3 = select.select("tr[onmouseout='this.classList.remove('model_select');']");
//        Elements select = document.select("#collapse_7 > div > div > table > tbody > tr:nth-child(27)");
        Elements select3 = document.select("#collapse_8 > div > div > table > tbody > tr");
        for (Element element66 : select3) {
            String onclick = element66.attr("onclick");
            System.out.println("最初onclick：" + onclick);
            if (onclick != null && !"".equals(onclick)) {
                String substring = onclick.substring(onclick.indexOf("(") + 1, onclick.indexOf(")"));
                String[] split = substring.split(",");
                String groupId = null;
                String replace = null;
                for (int i = 0; i < split.length; i++) {
                    String s1 = split[i];
                    String substring1 = s1.substring(1, s1.lastIndexOf("'"));
                    if (substring1.contains(" ")) {
                        replace = substring1.replace(" ", "20%");
                        //System.out.println(replace+" :有空格变成20%");
                    }
                    System.out.println(substring1);
                }
                jia++;
//                String s1 = "https://volkswagen.7zap.com/cat_scripts/part_groups_get.php?lang=cn&region=SVW&modification_date=" + split[3].trim() + "&modification_type=" + split[4].trim() + "&modification_name=" + split[2].trim() + "&model_name=" + split[0].trim() + "&vin=" + split[7].trim() + "&_=" + jia;
                String s1 = "https://volkswagen.7zap.com/cat_scripts/part_groups_get.php?lang=cn&region=CN&&modification_date=" + split[3].trim() + "&modification_type=" + split[4].trim() + "&modification_name=" + split[2].trim() + "&model_name=" + split[0].trim() + "&vin=" + split[7].trim() + "&_=" + jia;
                String s2 = s1.replaceAll("'", "");
                System.out.println(s2);
                //Document document1 = Jsoup.connect(s2).get();
                Document document1=null;
                try {
                    if (s2.contains(" ")){
                        s2 = s2.replace(" ", "");
                    }
                    document1 = Jsoup.parse(HttpServiceUtil.doGet(s2));
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(e.getMessage());
                }

                Elements select1 = document1.select("div[class='group col-xs-20 col-sm-11 col-md-9']");
                String s4 = null;
                for (Element element : select1) {
                    String onclick1 = element.attr("onclick");
                    String substring1 = onclick1.substring(onclick1.indexOf("(") + 1, onclick1.indexOf(")"));
                    String[] split1 = substring1.split(",");
                    String substring2 = null;
                    for (int i = 0; i < split1.length; i++) {
                        String s3 = split1[i];
                        if (s3.contains("'")) {
                            substring2 = s3.substring(s3.indexOf("'") + 1, s3.lastIndexOf("'")).trim();
                        } else {
                            substring2 = s3.trim();
                        }
                        if (substring2.contains("20%25")) {
                            String replace1 = substring2.replace("20%25", "+");
                            substring2 = replace1;
                        }
                    }
                    jia++;
//                    String s3 = "https://volkswagen.7zap.com/cat_scripts/part_sub_groups_get.php?lang=cn&region=SVW&modification_date=" + split1[3].trim() + "&modification_type=" + split1[4].trim() + "&modification_name=" + split1[2].trim() + "&model_url=" + split1[1].trim() + "&group_id=" + split1[7].trim() + "&model_name=" + split1[0].trim() + "&vin=" + split1[8].trim() + "&_=" + jia;
                    String s3 = "https://volkswagen.7zap.com/cat_scripts/part_sub_groups_get.php?lang=cn&region=CN&modification_date=" + split1[3].trim() + "&modification_type=" + split1[4].trim() + "&modification_name=" + split1[2].trim() + "&model_url=" + split1[1].trim() + "&group_id=" + split1[7].trim() + "&model_name=" + split1[0].trim() + "&vin=" + split1[8].trim() + "&_=" + jia;
                    s4 = s3.replaceAll("'", "").trim();
                    groupId = split1[7].trim();
                    System.out.println(s4);
                    //最终页面的获取
                    //Document document2 = Jsoup.connect(s4).get();
                    Document document2=null;
                    try {
                        if (s4.contains(" ")){
                            s4 = s4.replace(" ", "");
                        }
                        document2 = Jsoup.parse(HttpServiceUtil.doGet(s4));
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error(e.getMessage());
                    }

                    Elements select2 = document2.select("body > div");
                    //System.out.println("查看第二步onclick："+select2);
                    String substring3 = null;
                    String fileName = null;
                    String str=null;
                    for (Element element77 : select2) {
                        String onclick77 = element77.attr("onclick");
                        System.out.println("第三个onclick是：" + onclick77);
                        if (onclick77 != null && !"".equals(onclick77)) {
                            String substring7 = onclick77.substring(onclick77.indexOf("(") + 1, onclick77.indexOf(")"));
                            String[] split7 = substring7.split(",");
                            for (int i = 0; i < split7.length; i++) {
                                String s7 = split7[i];
                                String trim = split7[2];
                                fileName = trim.substring(trim.indexOf("'") + 1, trim.lastIndexOf("'")).trim();
                                //System.out.println("第三个split1是："+s7);
                                if (s7.contains("'")) {
                                    substring3 = s7.substring(s7.indexOf("'") + 1, s7.lastIndexOf("'")).trim();
                                } else {
                                    substring3 = s7.trim();
                                }
                                if (s7.contains(" ")) {
                                    replace = substring3.replace(" ", "20%");
                                }
                            }
                            jia++;
//                            String s8 = "https://volkswagen.7zap.com/cat_scripts/parts_get.php?lang=cn&region=SVW&modification_date=" + split7[3].trim() + "&modification_type=" + split7[4].trim() + "&modification_name=" + split7[2].trim() + "&model_url=" + split7[1].trim() + "&group_id=" + groupId + "&model_name=" + replace.trim() + "&hg_ug=" + split7[8] + "&pnc=" + split7[9].trim() + "&vin=" + split7[10] + "&_=" + jia;
                            String s8 = "https://volkswagen.7zap.com/cat_scripts/parts_get.php?lang=cn&region=CN&modification_date=" + split7[3].trim() + "&modification_type=" + split7[4].trim() + "&modification_name=" + split7[2].trim() + "&model_url=" + split7[1].trim() + "&group_id=" + groupId + "&model_name=" + replace.trim() + "&hg_ug=" + split7[8] + "&pnc=" + split7[9].trim() + "&vin=" + split7[10] + "&_=" + jia;
                            s8 = s8.replaceAll("'", "").trim();
                            System.out.println(s8);
                            futList.add(fixedThreadPool.submit(new Parser(s8,fileName)));
                        }
                    }
                }
            }
        }
        Thread.currentThread().sleep(5*24*60*60*1000);
        fixedThreadPool.shutdown();

    }

    public static void saveHtml(String filepath, String str) {

        try {
            OutputStreamWriter outs = new OutputStreamWriter(new FileOutputStream(filepath, true), "utf-8");
            outs.write(str);
            //System.out.print(str);
            outs.close();
        } catch (IOException e) {
            System.out.println("Error at save html...");
            e.printStackTrace();
        }
    }

    public static String InputStream2String(InputStream in_st, String charset) throws IOException {
        BufferedReader buff = new BufferedReader(new InputStreamReader(in_st, charset));
        StringBuffer res = new StringBuffer();
        String line = "";
        while ((line = buff.readLine()) != null) {
            res.append(line);
        }
        return res.toString();
    }

    class Parser implements Callable<String> {
        private String s;
        private String fileName;

        public Parser(String s, String fileName) {
            this.s = s;
            this.fileName = fileName;
        }

        @Override
        public String call() throws Exception {

            try {
                count++;
                String filepath="F:\\otherHtml\\"+fileName+"-"+count+".html";
                //Document document0 = Jsoup.connect(s).timeout(10000).get();
                Document document0=null;
                try {
                    if (s.contains(" ")){
                        s = s.replace(" ", "");
                    }
                    document0 = Jsoup.parse(HttpServiceUtil.doGet(s));
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("小catch"+fileName+"-"+count+"问题是："+e.getMessage());
                }
                Elements select = document0.select("body > div > img");
                String src = select.attr("src");
                String imgSrc="https://volkswagen.7zap.com"+src;
                select.attr("src", imgSrc);
                saveHtml(filepath,document0.html());
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("大catch"+fileName+"-"+count+"问题是："+e.getMessage());
            }
            return null;
        }
    }

//    @Test//出错的单独输出
//    public void test2(){
//        String s="https://volkswagen.7zap.com/cat_scripts/parts_get.php?lang=cn&region=CN&modification_date=1947&modification_type=198&modification_name=EL&model_url=elekt+verbind-elem&group_id=0&model_name=CN&hg_ug=035&pnc=35020&vin=&_=9375";
//        String filepath="F:\\writeToHtml\\bucuo\\EL-9070.html";
//        Document document0=null;
//        try {
//            if (s.contains(" ")){
//                s = s.replace(" ", "");
//            }
//            document0 = Jsoup.parse(HttpServiceUtil.doGet(s));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Elements select = document0.select("body > div > img");
//        String src = select.attr("src");
//        String imgSrc="https://volkswagen.7zap.com"+src;
//        select.attr("src", imgSrc);
//        saveHtml(filepath,document0.html());
//
//    }


    @Test//解析html并导入到模板中
    public void importHtml() throws IOException {
        File compNameFile = new File("F:\\importResult.txt");
        Writer writer = new BufferedWriter(new FileWriter(compNameFile));
        StringBuilder stringBuilder=new StringBuilder();

        int count=0;


        //获取配件数据
        Document document = Jsoup.connect("https://volkswagen.7zap.com/cat_scripts/parts_get.php?lang=cn&region=SVW&modification_date=1986&modification_type=440&modification_name=EL&model_url=electr+connect+elements&group_id=0&model_name=SVW&hg_ug=035&pnc=35020&vin=&_=3").get();
        Elements select = document.select("body > div.Ctree > table > tbody ");
        for (Element element : select) {

            Elements select1 = element.select("tr[class='one_part']");
            for (Element element1 : select1) {
                String text =document.select("#breadcrumbs > li:nth-child(2) > a").text();
                String[] split1 = text.split(" ");
                String pinpai = split1[1];
                stringBuilder.append("\""+pinpai+"\""+",");
                System.out.println(pinpai);//获取品牌

                String text4 = document.select("#main_title > small").text();
                String[] split = text4.split(" ");
                String niankuan = split[1].trim();
                System.out.println(split[1].trim());//年款
                String chanyuandi = split[0].substring(1, split[0].length()).trim();
                System.out.println(chanyuandi);//产源地
                stringBuilder.append("\""+chanyuandi+"\""+",");

                String chexi = document.select("#breadcrumbs > li:nth-child(5)").text();
                System.out.println(chexi);//获取车系名称
                stringBuilder.append("\""+chexi+"\""+",");
                stringBuilder.append("\""+niankuan+"\""+",");

                String zhuzu = document.select("#breadcrumbs > li:nth-child(9) > a").text();
                System.out.println(zhuzu);//获取零部件主组
                stringBuilder.append("\""+zhuzu+"\""+",");
                String zizu = document.select("body > div.Ctree > table > tbody > tr:nth-child(2) > td:nth-child(3)").text();
                System.out.println(zizu);//获取零部件子组
                stringBuilder.append("\""+zizu+"\""+",");


                //获取配件图
                Elements imgs = document.select("img[class='parts_image']");
                for (Element img : imgs) {
                    String peijiantu = img.attr("src");
                    String url="https://volkswagen.7zap.com"+peijiantu;
                    System.out.println(url);
                    stringBuilder.append("\""+url+"\""+",");
                }
                Elements select2 = element1.select("td");
                for (int i = 0; i < select2.size(); i++) {
                    String get= select2.get(i).text();
                    if (get.contains("&nbsp;&nbsp;")){
                        get.replace("&nbsp;&nbsp"," ");
                    }
                    System.out.println(get);
                    stringBuilder.append("\""+get+"\""+",");
                    if (i>=6){
                        Elements spans = select2.select("span");
                        if (spans.size()>0){
                            //获取适用性连接https://volkswagen.7zap.com/cat_scripts/get_analogs.php?lang=cn&detail=WHT004877&imagelink=/images/Tnrpics/WHT004877.jpg&showpic=info&_=1500623042332
                            if (spans!=null && !"".equals(spans)){
                                for (int a = 0; a < spans.size(); a++) {
                                        String onclick = spans.get(a).attr("onclick");
                                        String substring1 = onclick.substring(onclick.indexOf("(") + 1, onclick.lastIndexOf(")"));
                                        String s = substring1.trim().replaceAll("\"", "").replaceAll(" ", "");
                                        System.out.println(s);
                                        String[] split2 = s.split(",");
                                        StringBuilder stringBuilder1=null;
                                    if (a==0){
                                        count++;
                                        String url="https://volkswagen.7zap.com/cat_scripts/get_analogs.php?lang="+split2[0]+"&detail="+split2[1]+"&imagelink="+split2[2]+"&showpic="+split2[3]+"&_="+count;
                                        System.out.println("适用性："+url);
                                        stringBuilder1=new StringBuilder();
                                        stringBuilder1.append("\""+url+"\""+";");
                                        if (spans.size()==1){
                                            stringBuilder.append("\""+stringBuilder1+"\""+","+"\r\n");
                                        }else{
                                            stringBuilder.append("\""+stringBuilder1+"\""+",");
                                        }
                                    }
                                    if(a==1){
                                        count++;
                                        String url="https://volkswagen.7zap.com/cat_scripts/get_analogs.php?lang="+split2[0]+"&detail="+split2[1]+"&imagelink="+split2[2]+"&showpic="+split2[3]+"&_="+count;
                                        System.out.println("类似物："+url);
                                        stringBuilder1=new StringBuilder();
                                        stringBuilder1.append(url+";");
                                        if (spans.size()==2){
                                            stringBuilder.append("\""+stringBuilder1+"\""+","+"\r\n");
                                        }else{
                                            stringBuilder.append("\""+stringBuilder1+"\""+",");
                                        }
                                    }
                                    if(a>1){
                                        //获得图片连接https://volkswagen.7zap.com/cat_scripts/get_analogs.php?lang=cn&detail=WHT004877&imagelink=/images/Tnrpics/WHT004877.jpg&showpic=photo&_=1500858172859
                                        count++;
                                        String imgUrl=split2[2];
                                        if (imgUrl!=null && !"".equals(imgUrl)){
                                            String url="https://volkswagen.7zap.com"+imgUrl;
                                            System.out.println("图片地址："+url);
                                            stringBuilder1=new StringBuilder();
                                            stringBuilder1.append(url+";");
                                        }
                                        if (spans.size()==3){
                                            stringBuilder.append("\""+stringBuilder1+"\""+","+"\r\n");
                                        }else{
                                            stringBuilder.append("\""+stringBuilder1+"\""+",");
                                        }
                                    }
                                }
                            }

                        }else{
                            stringBuilder.append("\r\n");
                        }
                    }
                }

            }

            writer.write(stringBuilder.toString());
            writer.flush();
            writer.close();
        }
    }
}

