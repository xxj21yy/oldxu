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

/**
 * Created by cc on 2017/7/26.
 */
public class TestAudi {
    private static Logger logger = LoggerFactory.getLogger(TestAudi.class);
    int count = 0;//相等
    int downloadCnt = 0;
    int num = 0;
    Map<String, Integer> imgMap = new HashMap<>();

    @Test
    public void test() throws IOException, InterruptedException {

        StringBuilder stringBuilder = new StringBuilder();
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
        List<Future<String>> futList = new ArrayList<Future<String>>();
        stringBuilder.append("https://audi.7zap.com/cn/ca/");
        int jia = 1;
        String s = "https://audi.7zap.com/cat_scripts/models_get.php?lang=en&region=CA&_=" + jia;
        Document document = null;
        try {
            s = s.replace(" ", "");
            document = Jsoup.parse(HttpServiceUtil.doGet(s));
        } catch (Exception e) {
            logger.error(s, e);
        }
//        Elements select = document.select("div[class='table-responsive']");
//        Elements select3 = select.select("tr[onmouseout='this.classList.remove('model_select');']");
        Elements select3 = document.select("#collapse_5 > div > div > table > tbody > tr");
//        Elements select3 = document.select("tr[onmouseover='this.classList.add('model_select');']");
        //System.out.println(select3);
        for (Element element66 : select3) {
            String onclick = element66.attr("onclick");
            System.out.println("最初onclick：" + onclick);
            String groupId = null;
            String replace = null;
            if (onclick != null && !"".equals(onclick)) {
                String[] split = getValue(onclick);
                jia++;
                String s1 = "https://audi.7zap.com/cat_scripts/part_groups_get.php?lang=cn&region=ca&modification_date=" + split[3].trim() + "&modification_type=" + split[4].trim() + "&modification_name=" + split[2].trim() + "&model_name=" + split[0].trim() + "&vin=" + split[7].trim() + "&_=" + jia;
                String s2 = s1.replaceAll("'", "");
                System.out.println(s2);
                Document document1 = null;
                try {
                    s2 = s2.replace(" ", "");
                    document1 = Jsoup.parse(HttpServiceUtil.doGet(s2));
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }

                Elements select1 = document1.select("div[class='group col-xs-20 col-sm-11 col-md-9']");
                String s4 = null;
                for (Element element : select1) {
                    String onclick1 = element.attr("onclick");
                    String[] split1 = getValue(onclick1);
//                    String text = element.text();
//                    System.out.println(text);
                    //#cat_content > div > span发动机
                    //#cat_content > div > table > tbody > tr > td
                    jia++;

                    String s3 = "https://audi.7zap.com/cat_scripts/part_sub_groups_get.php?lang=cn&region=CA&modification_date=" + split1[3].trim() + "&modification_type=" + split1[4].trim() + "&modification_name=" + split1[2].trim() + "&model_url=" + split1[1].trim() + "&group_id=" + split1[7].trim() + "&model_name=" + split1[0].trim() + "&vin=" + split1[8].trim() + "&_=" + jia;
                    s4 = s3.replaceAll("'", "").trim();
                    groupId = split1[7].trim();
                    //System.out.println(s4);
                    //最终页面的获取
                    Document document2 = null;
                    try {
                        s4 = s4.replace(" ", "");
                        document2 = Jsoup.parse(HttpServiceUtil.doGet(s4));
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }

                    Elements select2 = document2.select("body > div");
                    String substring3 = null;
                    String fileName = null;
                    String str = null;
                    for (Element element77 : select2) {
                        String onclick77 = element77.attr("onclick");
//                        String text1 = element77.text();
//                        System.out.println(text1);
                        System.out.println("第三个onclick是：" + onclick77);
                        if (onclick77 != null && !"".equals(onclick77)) {
                            String[] split2 = getValue(onclick77);
                            String trim = split2[2];
                            fileName = trim.substring(trim.indexOf("'") + 1, trim.lastIndexOf("'")).trim();
                            jia++;
                            String s8 = "https://audi.7zap.com/cat_scripts/parts_get.php?lang=cn&region=CA&modification_date=" + split2[3].trim() + "&modification_type=" + split2[4].trim() + "&modification_name=" + split2[2].trim() + "&model_url=" + split2[1].trim() + "&group_id=" + groupId + "&model_name=" + split1[0].trim() + "&hg_ug=" + split2[8] + "&pnc=" + split2[9].trim() + "&vin=" + split2[10] + "&_=" + jia;
                            s8 = s8.replaceAll("'", "").trim();
                            System.out.println(s8);
                            futList.add(fixedThreadPool.submit(new TestAudi.Parser(s8, fileName)));
                        }
                    }
                }
            }
        }
        Thread.currentThread().sleep(5 * 24 * 60 * 60 * 1000);
        fixedThreadPool.shutdown();

    }

    //整合取值onclick方法
    public static String[] getValue(String onclick) {
        String substring = onclick.substring(onclick.indexOf("(") + 1, onclick.indexOf(")"));
        String[] split = substring.split(",");
        String substring1 = null;
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            if (s.contains("'")) {
                substring1 = s.substring(s.indexOf("'") + 1, s.lastIndexOf("'")).trim();
            } else {
                substring1 = s.trim();
            }
            substring1 = substring1.replace("20%25", "+");
            substring1 = substring1.replace(" ", "20%");
        }
        return split;
    }


    public static void saveHtml(String filepath, String str) {

        try {
            OutputStreamWriter outs = new OutputStreamWriter(new FileOutputStream(filepath, true), "utf-8");
            outs.write(str);
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
                //生成url的txt
//                count++;
//                File dir = new File("F:\\Audi\\path1\\"+fileName+"-"+count+".txt");
//                FileUtils.write(dir,s,"UTF-8");

                //生成html
                num++;
                String filepath = "F:\\Audi\\html\\A3\\" + fileName + "-" + num + ".html";
                Document document0 = null;
                try {
                    s = s.replace(" ", "");
                    document0 = Jsoup.parse(HttpServiceUtil.doGet(s));
                } catch (Exception e) {
                    logger.error("网页出bug了" + fileName + "-" + count + "问题是：" + s + e.getMessage());
                }
                Elements select = document0.select("body > div > img");
                String src = select.attr("src");
                String imgSrc = "https://audi.7zap.com" + src;
                select.attr("src", imgSrc);
                saveHtml(filepath, document0.html());

            } catch (Exception e) {
                logger.error("生成html出bug了==" + fileName + "-" + count + "问题是：" + s + e.getMessage());
            }
            return null;
        }
    }


    @Test//出错的单独输出
    public void test2() {
        String s = "https://audi.7zap.com/cat_scripts/parts_get.php?lang=cn&region=CA&modification_date=2010&modification_type=635&modification_name=A8&model_url=audi+a8&group_id=9&model_name=AudiA8&hg_ug=911&pnc=911020&vin=&_=160979";
        String filepath = "F:\\Audi\\chucuo\\A8-158921.html";
        Document document0 = null;
        try {
            s = s.replace(" ", "");
            document0 = Jsoup.parse(HttpServiceUtil.doGet(s));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Elements select = document0.select("body > div > img");
        String src = select.attr("src");
        String imgSrc = "https://audi.7zap.com" + src;
        select.attr("src", imgSrc);
        saveHtml(filepath, document0.html());

    }

    @Test//单个车系解析html并导入到模板中
    public void importHtml() {
        File compNameFile = new File("F:\\Skoda\\data\\SUP.txt");
        Writer writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(compNameFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder stringBuilder = new StringBuilder();
        int num = 0;
        int count = 0;
        //递归文件夹下的所有文件
        File fileFile = new File("F:\\Skoda\\html\\SUP");
        String[] filelist = fileFile.list();
        for (int n = 0; n < filelist.length; n++) {
            File readfile = new File("F:\\Skoda\\html\\SUP" + "\\" + filelist[n]);
            System.out.println(readfile);
            if (!readfile.isDirectory()) {
                //继续读取文件
                String filePath = readfile.getAbsolutePath();
                String encoding = "GBK";
                File file = new File(filePath);
                try {
                    //获取配件数据
                    Document document = Jsoup.parse(file, "UTF-8");
                    num++;
                    Element select = document.select("body > div.Ctree > table > tbody").first();
                    if (select!=null && !"".equals(select)){
//                    for (Element element : select) {
                        Elements select1 = select.select("tr[class='one_part']");
                        stringBuilder = new StringBuilder("");
                        for (Element element1 : select1) {
                            String text = document.select("#breadcrumbs > li:nth-child(2) > a").first().text();
                            String[] split1 = text.split(" ");
                            String pinpai = split1[1];//获取品牌
                            stringBuilder.append("\"" + pinpai + "\"" + ",");

                            String text4 = document.select("#main_title > small").text();
                            String[] split = text4.split(" ");
                            String niankuan = split[1].trim();//年款
                            String chanyuandi = split[0].substring(1, split[0].length()).trim();//产源地
                            stringBuilder.append("\"" + chanyuandi + "\"" + ",");

                            String chexi = document.select("#breadcrumbs > li:nth-child(5)").first().text();
                            System.out.println(chexi);//获取车系名称
                            stringBuilder.append("\"" + chexi + "\"" + ",");
                            stringBuilder.append("\"" + niankuan + "\"" + ",");

                            String zhuzu = document.select("#breadcrumbs > li:nth-child(9) > a").first().text();//获取零部件主组
                            stringBuilder.append("\"" + zhuzu + "\"" + ",");
                            String zizu = document.select("#breadcrumbs > li:nth-child(11)").first().text();//获取零部件子组
                            stringBuilder.append("\"" + zizu + "\"" + ",");


                            //获取配件图
                            Element img = document.select("img[class='parts_image']").first();
                            String url = img.attr("src");
                            stringBuilder.append("\"" + url + "\"" + ",");
                            Elements select2 = element1.select("td");
                            for (int i = 0; i < select2.size(); i++) {
                                String get = select2.get(i).text();
                                get.replace("&nbsp;&nbsp", " ");
                                stringBuilder.append("\"" + get + "\"" + ",");
                                if (i >= 6) {
                                    Elements spans = select2.select("span");
                                    if (spans.size() > 0) {
                                        //获取适用性连接https://volkswagen.7zap.com/cat_scripts/get_analogs.php?lang=cn&detail=WHT004877&imagelink=/images/Tnrpics/WHT004877.jpg&showpic=info&_=1500623042332
                                        if (spans != null && !"".equals(spans)) {
                                            for (int a = 0; a < spans.size(); a++) {
                                                String onclick = spans.get(a).attr("onclick");
                                                String substring1 = onclick.substring(onclick.indexOf("(") + 1, onclick.lastIndexOf(")"));
                                                String s = substring1.trim().replaceAll("\"", "").replaceAll(" ", "");
                                                String[] split2 = s.split(",");
                                                StringBuilder stringBuilder1 = null;
                                                if (a == 0) {
                                                    count++;
                                                    String url4 = "https://skoda.7zap.com/cat_scripts/get_analogs.php?lang=" + split2[0] + "&detail=" + split2[1] + "&imagelink=" + split2[2] + "&showpic=" + split2[3] + "&_=" + count;
                                                    stringBuilder1 = new StringBuilder();
                                                    stringBuilder1.append("\"" + url4 + "\"" + ";");
                                                    if (spans.size() == 1) {
                                                        stringBuilder.append("\"" + stringBuilder1 + "\"" + "," + "\r\n");
                                                    } else {
                                                        stringBuilder.append("\"" + stringBuilder1 + "\"" + ",");
                                                    }
                                                }
                                                if (a == 1) {
                                                    count++;
                                                    //获得类似物连接
                                                    String url2 = "https://skoda.7zap.com/cat_scripts/get_analogs.php?lang=" + split2[0] + "&detail=" + split2[1] + "&imagelink=" + split2[2] + "&showpic=" + split2[3] + "&_=" + count;
                                                    stringBuilder1 = new StringBuilder();
                                                    stringBuilder1.append(url2 + ";");
                                                    if (spans.size() == 2) {
                                                        stringBuilder.append("\"" + stringBuilder1 + "\"" + "," + "\r\n");
                                                    } else {
                                                        stringBuilder.append("\"" + stringBuilder1 + "\"" + ",");
                                                    }
                                                }
                                                if (a > 1) {
                                                    //获得图片连接https://volkswagen.7zap.com/cat_scripts/get_analogs.php?lang=cn&detail=WHT004877&imagelink=/images/Tnrpics/WHT004877.jpg&showpic=photo&_=1500858172859
                                                    count++;
                                                    String imgUrl = split2[2];
                                                    if (imgUrl != null && !"".equals(imgUrl)) {
                                                        String url3 = "https://audi.7zap.com" + imgUrl;
                                                        stringBuilder1 = new StringBuilder();
                                                        stringBuilder1.append(url3 + ";");
                                                    }
                                                    if (spans.size() == 3) {
                                                        stringBuilder.append("\"" + stringBuilder1 + "\"" + "," + "\r\n");
                                                    } else {
                                                        stringBuilder.append("\"" + stringBuilder1 + "\"" + ",");
                                                    }
                                                }
                                            }
                                        }

                                    } else {
                                        stringBuilder.append("\r\n");
                                    }
                                }
                            }

                        }

                    }

//                    }
                    writer.write(stringBuilder.toString());
                    writer.flush();
                } catch (IOException e) {
                    File file1=new File("F:\\Skoda\\bugData\\"+num+".txt");
                    try {
                        FileUtils.write(file1, readfile.getName(), "UTF-8");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    logger.error(readfile+" - "+e.getMessage() + "这个--" + num + "--html出bug了--");
                }
            }
        }
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //下载图片
    @Test
    public void downloadPic() throws Exception {
        ExecutorService fixedThreadPool2 = Executors.newFixedThreadPool(5);
        Queue<Future<String>> futList = new ConcurrentLinkedQueue<Future<String>>();
        String filePath = "F:\\Audi\\audishuju.txt";
        Map<String, Integer> imgMap = new ConcurrentHashMap<>();
        File imgDir = new File("F:\\Audi\\downPic");
        for (File subDir : imgDir.listFiles()) {
            for (File file : subDir.listFiles()) {
                imgMap.put(subDir.getName() + "-" + file.getName(), 1);
            }
        }
        File file = new File(filePath);
        try {
            LineIterator lineIterator = FileUtils.lineIterator(file);
            while (lineIterator.hasNext()) {
                String line = lineIterator.nextLine();
                String[] split = line.split("\",\"");
                String s = split[6];
                String s2 = s.replaceAll("//", "/");
                String[] split1 = s2.split("/");
                String one = split1[4];
                String two = split1[5];
                System.out.println(count++);
                try {
                    if (imgMap.get(one + "-" + two) == null) {
                        futList.add(fixedThreadPool2.submit(new TestAudi.Parser1(one,two)));
                        imgMap.put(one + "-" + two, 1);
                    }
                } catch (Exception e) {
                    logger.error(line + "\t" + e.getMessage());
                    imgMap.put(one + "-" + two, 1);
                }
            }
            System.out.println("共有"+imgMap.size()+"个图片");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        Thread.currentThread().sleep(5 * 24 * 60 * 60 * 1000);
        fixedThreadPool2.shutdown();
    }

    class Parser1 implements Callable<String> {

        private String one;
        private String two;

        public Parser1( String one, String two) {

            this.one = one;
            this.two = two;
        }

        @Override
        public String call() throws Exception {
            try{
                System.out.println("在下载"+downloadCnt++);
                byte[] data = HttpServiceUtil.downloadPic("https://audi.7zap.com/images//Bilder/"+one+"/"+two);
                File dir = new File("F:\\Audi\\downPic\\" + one);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                File targetFile = new File(dir, two);
                FileUtils.writeByteArrayToFile(targetFile, data);
            }catch (Exception e){
                logger.error(e.getMessage());
            }
            return null;
        }
    }
}



