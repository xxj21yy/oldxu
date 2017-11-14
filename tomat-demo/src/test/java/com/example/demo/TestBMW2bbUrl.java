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
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by cc on 2017/8/14.
 */
//通过一级网址
public class TestBMW2bbUrl {
    private static Logger logger = LoggerFactory.getLogger(TestBMW2bbUrl.class);
    //    int count = 0;
    int downloadCnt = 0;
    int num = 0;
    AtomicInteger count = new AtomicInteger();

    //宝马与大众jsoup提取内容有差别
    @Test
    public void test() {

        int jia = 1;
        String s = "https://bmw.7zap.com/cat_scripts/get_models.php?lang=en&mark=bmw&classic=VT&_=" + jia;
        Document document = connectUrl(s);

        Elements select3 = document.select("#collapse_49 > div > div > table > tbody > tr:nth-child(4)");
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
                                                                    fileNameOne = split2[4];//大文件夹
                                                                    fileName = split2[2];//子文件夹
                                                                    jia++;
                                                                    String s8 = "https://bmw.7zap.com/cat_scripts/get_parts.php?lang=zh&modification=" + split2[2] + "&modelname=" + split2[4] + "&year=" + yearname + "&month=" + monthname + "&wheel=" + wheelname + "&transmission=" + transmissionname + "&classic=VT&region=" + regionname + "&group=" + groupId + "&subgroup=" + split2[11] + "&_=" + jia;
                                                                    s8 = s8.replaceAll("'", "").trim();
                                                                    System.out.println(s8);
//                                                                    count++;
                                                                    File dir = new File("F:\\BMW\\chnPath\\" + fileNameOne + "\\" + fileName + "\\" + fileName + "-" + count + ".txt");
                                                                    try {
                                                                        FileUtils.write(dir, s8, "UTF-8");
                                                                    } catch (IOException e) {
                                                                        File dir2 = new File("F:\\Dazhong\\Europa\\bugpath\\" + fileNameOne + "\\" + fileName + " - " + count + ".txt");
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

    public static Document connectUrl(String url) {
        Document document = null;
        int cnt = 0;
        cnt++;
        if (url.contains(" ")) {
            url = url.replaceAll(" ", "");
        }
        try {
//            document = Jsoup.connect(url).proxy("127.0.0.1",51616).get();
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

    public static void saveHtml(String filepath, String str) {

        try {
            OutputStreamWriter outs = new OutputStreamWriter(new FileOutputStream(filepath, true), "utf-8");
            outs.write(str);
            outs.close();
        } catch (Exception e) {
            System.out.println("Error at save html...");
            e.printStackTrace();
        }
    }

    public static void savePic(String filepath, String str) {

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

    @Test//生成html
    public void html() {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
        CompletionService<String> execcomp = new ExecutorCompletionService<String>(fixedThreadPool);
        String fileName = null;

        File file288 = new File("F:\\BMW\\html");
        String[] list188 = file288.list();
        for (int j88 = 0; j88 < list188.length; j88++) {
            File file88 = new File("F:\\BMW\\html\\" + list188[j88]);
            String[] files88 = file88.list();
            for (int i88 = 0; i88 < files88.length; i88++) {
                File readfile88 = new File("F:\\BMW\\html\\" + list188[j88] + "\\" + files88[i88]);
                String[] list88 = readfile88.list();
                for (int i188 = 0; i188 < list88.length; i188++) {
                    File file188 = new File("F:\\BMW\\html\\" + list188[j88] + "\\" + files88[i88] + "\\" + list88[i188]);
                    String substring = list88[i188].substring(0, list88[i188].lastIndexOf(".html"));
                    if (file188.exists() && file188.isFile()) {
                        long length = file188.length();
                        System.out.println(file188 + "小于2kb ：" + length);
                        if (length < 2048) {
                            try {
                                File file1 = new File("E:\\BMW\\path\\" + list188[j88] + "\\" + files88[i88] + "\\" + substring + ".txt");
                                fileName = list88[i188];
                                execcomp.submit(new TestBMW2bbUrl.Parser2(file1, fileName));
//                                //生成html
//                                File file2 = new File("E:\\BMW\\path");
//                                String[] list1 = file2.list();
//                                for (int j = 0; j < list1.length; j++) {
//                                    File file = new File("E:\\BMW\\path\\" + list1[j]);
//                                    String[] files = file.list();
//                                    for (int i = 0; i < files.length; i++) {
//                                        File readfile = new File("E:\\BMW\\path\\" + list1[j] + "\\" + files[i]);
//                                        String[] list = readfile.list();
//                                        for (int i1 = 0; i1 < list.length; i1++) {
//                                            File file1 = new File("E:\\BMW\\path\\" + list1[j] + "\\" + files[i] + "\\" + list[i1]);
//                                            fileName = list[i1];
//                                            execcomp.submit(new TestBMW2bbUrl.Parser2(file1, fileName));
//                                        }
//                                    }
//                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            System.out.println(file188 + "大于1kb");
                        }
                    }
                }
            }
        }

        try {
            Thread.currentThread().sleep(5 * 24 * 60 * 60 * 1000);
        } catch (InterruptedException e) {
            File dir2 = new File("F:\\BMW\\bugHtml\\" + fileName + " - " + count.get() + ".txt");
            try {
                FileUtils.write(dir2, fileName, "UTF-8");
            } catch (IOException e1) {
                logger.error(e.getMessage());
            }
            logger.error(fileName + " - " + count.get() + "未写入，原因是：" + e.getMessage());
        }
        fixedThreadPool.shutdown();
    }

    class Parser2 implements Callable<String> {
        private File file;
        private String fileName;

        public Parser2(File file, String fileName) {
            this.file = file;
            this.fileName = fileName;
        }

        @Override
        public String call() {
            String[] split = file.getAbsolutePath().split("\\\\");
            String first = split[2];
            String middle = split[3];
            String number = fileName.substring(fileName.indexOf("-") + 1, fileName.lastIndexOf("."));
            String name = fileName.substring(0, fileName.lastIndexOf("-"));
            InputStreamReader read = null;//考虑到编码格式
            try {
                read = new InputStreamReader(new FileInputStream(file), "GBK");
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    String url = lineTxt;
                    String filepath = "F:\\BMW\\html\\" + middle + "\\" + name + "\\" + name + "-" + number + ".html";
                    File dir = new File("F:\\BMW\\html\\" + middle + "\\" + name);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    File targetFile = new File(dir, name + "-" + number + ".html");
                    String path = targetFile.getAbsolutePath();
                    System.out.println(name + "-" + number + ":" + url);
                    try {
                        Document document0 = null;
                        url = url.replace(" ", "");
                        document0 = Jsoup.parse(HttpServiceUtil.doGet(url));
                        Elements select = document0.select("#detail_image");
                        String src = select.attr("src");
                        String imgSrc = "https://bmw.7zap.com" + src;
                        select.attr("src", imgSrc);
                        saveHtml(path, document0.html());
                    } catch (Exception e) {
                        File dir2 = new File("F:\\BMW\\bugHtml\\" + middle + "\\" + name + "\\" + name + "-" + number + ".txt");
                        try {
                            FileUtils.write(dir2, url, "UTF-8");
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        logger.error(fileName + " - " + count.get() + "未写入，原因是：" + e.getMessage());
                    }
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
            return null;
        }
    }

    @Test//出错的单独输出
    public void test2() {
        int count = 0;
        File file = new File("F:\\BMW\\bugHtml");
        String[] list = file.list();
        for (int i = 0; i < list.length; i++) {
            File file1 = new File("F:\\BMW\\bugHtml\\" + list[i]);
            String[] list1 = file1.list();
            for (int i1 = 0; i1 < list1.length; i1++) {
                File file2 = new File("F:\\BMW\\bugHtml\\" + list[i] + "\\" + list1[i1]);
                String[] list2 = file2.list();
                for (int i2 = 0; i2 < list2.length; i2++) {
                    File file3 = new File("F:\\BMW\\bugHtml\\" + list[i] + "\\" + list1[i1] + "\\" + list2[i2]);
                    String filepath = null;
                    try {
                        InputStreamReader read = new InputStreamReader(new FileInputStream(file3), "GBK");
                        BufferedReader bufferedReader = new BufferedReader(read);
                        String lineTxt = null;
                        while ((lineTxt = bufferedReader.readLine()) != null) {
                            String s = lineTxt;
                            filepath = "F:\\BMW\\lessHtml\\" + list[i] + "\\" + list1[i1] + "\\" + list2[i2].substring(0, list2[i2].lastIndexOf(".")) + ".html";
                            File dir = new File("F:\\BMW\\lessHtml\\" + list[i] + "\\" + list1[i1]);
                            if (!dir.exists()) {
                                dir.mkdirs();
                            }
                            File targetFile = new File(dir, list2[i2].substring(0, list2[i2].lastIndexOf(".")) + ".html");
                            String path = targetFile.getAbsolutePath();
                            Document document0 = null;
                            try {
                                s = s.replace(" ", "");
                                document0 = Jsoup.parse(HttpServiceUtil.doGet(s));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Elements select = document0.select("#detail_image");
                            String src = select.attr("src");
                            String imgSrc = "https://bmw.7zap.com" + src;
                            select.attr("src", imgSrc);
                            saveHtml(path, document0.html());
                            count++;
                            System.out.println(count + ":" + path);
                        }
                    } catch (IOException e) {
                        File dir2 = new File("F:\\BMW\\hh\\" + filepath + ".txt");
                        try {
                            FileUtils.write(dir2, filepath, "UTF-8");
                        } catch (IOException e1) {
                            logger.error(e.getMessage());
                        }
                        logger.error(e.getMessage());
                    }
                }

            }

        }
    }

    @Test//多个车系解析html并导入到模板中
    public void importHtml() {
        StringBuilder stringBuilder = new StringBuilder();
        int num = 0;
        int count = 0;
        int num22 = 0;
        Writer writer = null;
        //递归文件夹下的所有文件
        File fileFile = new File("F:\\BMW\\html");
        String[] list = fileFile.list();
        for (int j = 0; j < list.length; j++) {
            File file = new File("F:\\BMW\\html\\" + list[j]);
            String[] list1 = file.list();
            for (int c = 0; c < list1.length; c++) {
                File file1 = new File("F:\\BMW\\html\\" + list[j] + "\\" + list1[c]);
                stringBuilder = new StringBuilder("");
                File[] filelist = file1.listFiles();
                for (int n = 0; n < filelist.length; n++) {
                    String name = filelist[n].getName();
                    String path = null;
                    try {
                        File compNameFile2 = new File("F:\\BMW\\data\\" + list[j] + "\\" + list1[c]);
                        if (!compNameFile2.exists()) {
                            compNameFile2.mkdirs();
                        }
                        File compNameFile = new File("F:\\BMW\\data\\" + list[j] + "\\" + list1[c] + "\\" + list1[c] + ".txt");
                        if (!compNameFile.exists()) {
                            compNameFile.createNewFile();
                        }
                        writer = new BufferedWriter(new FileWriter(compNameFile));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    File readfile2 = filelist[n];
                    num22++;
                    System.out.println(num22 + " - " + filelist[n]);
                    //继续读取文件
                    String imgUrl = null;
                    try {
                        //获取配件数据
                        Document document = Jsoup.parse(readfile2, "UTF-8");
                        num++;
                        Element select = document.select("body > div.row > div > table > tbody").first();
                        if (select != null && !"".equals(select)) {
                            Elements select1 = select.select("tr[class='one_part']");
                            for (Element element1 : select1) {
                                String text = document.select("#breadcrumbs > li:nth-child(2) > a").first().text();
                                String[] split1 = text.split(" ");
                                String pinpai = split1[1];
                                stringBuilder.append("\"" + pinpai + "\"" + ",");
                                //System.out.println(pinpai);//获取品牌

                                String ad = document.select("#breadcrumbs > li:nth-child(8)").first().text();
                                String chanyuandi = ad.substring(ad.indexOf("[") + 1, ad.lastIndexOf("]"));
                                //System.out.println(chanyuandi);//产源地
                                stringBuilder.append("\"" + chanyuandi + "\"" + ",");

                                String chexi = document.select("#breadcrumbs > li:nth-child(5)").first().text();
                                //System.out.println(chexi);//获取车系名称
                                stringBuilder.append("\"" + chexi + "\"" + ",");
//                                    stringBuilder.append("\"" + niankuan + "\"" + ",");
                                String chassis = document.select("#breadcrumbs > li:nth-child(6)").first().text();
                                stringBuilder.append("\"" + chassis + "\"" + ",");//获取车底盘名称


                                String zhuzu = document.select("#breadcrumbs > li:nth-child(10) > a").first().text();
                                //System.out.println(zhuzu);//获取零部件主组
                                String substring = zhuzu.substring(0, zhuzu.lastIndexOf("BMW"));
                                stringBuilder.append("\"" + substring + "\"" + ",");

                                String zizu = document.select("#breadcrumbs > li:nth-child(12)").first().text();//需修改
                                String substring2 = zizu.substring(0, zizu.lastIndexOf("BMW"));
                                //System.out.println(zizu);//获取零部件子组
                                stringBuilder.append("\"" + substring2 + "\"" + ",");

                                //获取配件图
                                Element img = document.select("img[class='parts_image1']").first();
                                String url = img.attr("src");
                                String s = url.replace("/ImgsWatermark", "");
                                stringBuilder.append("\"" + s + "\"" + ",");
                                Elements select2 = element1.select("td");
                                for (int v = 0; v < select2.size(); v++) {
                                    String get = select2.get(v).text();
                                    get.replace("&nbsp;&nbsp", " ");
                                    //System.out.println(get);
                                    stringBuilder.append("\"" + get + "\"" + ",");
                                    if (v >= 6) {
                                        Elements spans = select2.select("span");
                                        if (spans.size() > 0) {
                                            //获取适用性连接https://volkswagen.7zap.com/cat_scripts/get_analogs.php?lang=cn&detail=WHT004877&imagelink=/images/Tnrpics/WHT004877.jpg&showpic=info&_=1500623042332
                                            if (spans != null && !"".equals(spans)) {
                                                for (int a = 0; a < spans.size(); a++) {
                                                    String onclick = spans.get(a).attr("onclick");
                                                    String substring1 = onclick.substring(onclick.indexOf("(") + 1, onclick.lastIndexOf(")"));
                                                    String szz = substring1.trim().replaceAll("\"", "").replaceAll(" ", "").replaceAll("'", "");
                                                    //System.out.println(s);
                                                    String[] split2 = szz.split(",");
                                                    StringBuilder stringBuilder1 = null;
                                                    if (a == 0) {
                                                        count++;
                                                        //https://bmw.7zap.com/cat_scripts/get_cars_for_this_detail.php?classic=VT&lang=zh&detail=51927896863&window_type=info&_=1502959179551
                                                        String url1 = "https://bmw.7zap.com/cat_scripts/get_cars_for_this_detail.php?classic=VT&lang=zh&detail=" + split2[2] + "&window_type=" + split2[3] + "&_=" + count;
                                                        //System.out.println("适用性：" + url);
                                                        String s1 = url1.replaceAll("'", "");
                                                        stringBuilder1 = new StringBuilder();
                                                        stringBuilder1.append(s1 + ";");
                                                        if (spans.size() == 1) {
                                                            stringBuilder.append("\"" + stringBuilder1 + "\"" + ",");
                                                        } else {
                                                            stringBuilder.append("\"" + stringBuilder1 + "\"" + ",");
                                                        }
                                                    }
                                                    if (a == 1) {
                                                        count++;
                                                        //https://bmw.7zap.com/cat_scripts/get_cars_for_this_detail.php?classic=VT&lang=zh&detail=51927896863&window_type=analog&_=1502959179550
                                                        String url2 = "https://bmw.7zap.com/cat_scripts/get_cars_for_this_detail.php?classic=VT&lang=zh&detail=" + split2[2] + "&window_type=" + split2[3] + "&_=" + count;
                                                        //System.out.println("类似物：" + url);
                                                        String s1 = url2.replaceAll("'", "");
                                                        stringBuilder1 = new StringBuilder();
                                                        stringBuilder1.append(s1 + ";");
                                                        if (spans.size() == 2) {
                                                            stringBuilder.append("\"" + stringBuilder1 + "\"" + ",");
                                                        } else {
                                                            stringBuilder.append("\"" + stringBuilder1 + "\"" + ",");
                                                        }
                                                    }
                                                    if (a > 1) {
                                                        count++;
                                                        imgUrl = split2[2].replaceAll("'", "");
                                                        if (imgUrl != null && !"".equals(imgUrl)) {
                                                            String url3 = "https://bmw.7zap.com" + imgUrl;
                                                            //System.out.println("图片地址：" + imgUrl);
                                                            stringBuilder1 = new StringBuilder();
                                                            stringBuilder1.append(url3 + ";");
                                                        }
                                                        if (spans.size() == 3) {
                                                            stringBuilder.append("\"" + stringBuilder1 + "\"" + ",");
                                                        } else {
                                                            stringBuilder.append("\"" + stringBuilder1 + "\"" + ",");
                                                        }
                                                    }
                                                }
                                            }

                                        }
                                    }
                                }
                                //获取五项
                                Elements select3 = document.select("#breadcrumbs > li:nth-child(10)>a");
                                String href = select3.attr("href");
                                String onclick = href.substring(href.indexOf("(") + 1, href.lastIndexOf(")"));
                                String[] value2 = getValue2(onclick);
                                String year = value2[5];
                                if ("0".equals(year)) {
                                    year = "Not selected";
                                }
                                String month = value2[6];
                                if ("0".equals(month)) {
                                    month = "Not selected";
                                }
                                String wheel = value2[7];
                                if ("0".equals(wheel)) {
                                    wheel = "Not selected";
                                } else if ("l".equals(wheel)) {
                                    wheel = "Left hand drive";
                                } else if ("r".equals(wheel)) {
                                    wheel = "Right hand drive";
                                } else {
                                    wheel = value2[7];
                                }

                                String transmission = value2[8];
                                if ("0".equals(transmission)) {
                                    transmission = "Not selected";
                                } else if ("n".equals(transmission)) {
                                    transmission = "Neutral";
                                } else if ("a".equals(transmission)) {
                                    transmission = "Automatic";
                                } else if ("m".equals(transmission)) {
                                    transmission = "Manual";
                                } else {
                                    transmission = value2[8];
                                }
                                String region = value2[9];
                                if ("0".equals(region)) {
                                    region = "Not selected";
                                }
                                stringBuilder.append("\"" + year + "\"" + ",");//年
                                stringBuilder.append("\"" + month + "\"" + ",");//月
                                stringBuilder.append("\"" + wheel + "\"" + ",");//轮方向
                                stringBuilder.append("\"" + transmission + "\"" + ",");//传动方式
                                stringBuilder.append("\"" + region + "\"" + "," + "\r\n");//地域

                            }
                            writer.write(stringBuilder.toString());
                            writer.flush();

                        }
                    } catch (IOException e) {
                        File dir2 = new File("F:\\BMW\\bugData\\" + list[j] + "\\" + list1[c] + "\\" + filelist[n].getName() + ".txt");
                        try {
                            FileUtils.write(dir2, list[j] + " - " + list1[c] + " - " + filelist[n] + "出bug了" + imgUrl, "UTF-8");
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        logger.error(list[j] + " - " + list1[c] + "里的" + filelist[n] + "出bug了" + imgUrl + e.getMessage());
                    }
                }
//                }
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //下载图片
    @Test
    public void downloadPic() {
        ExecutorService fixedThreadPool2 = Executors.newFixedThreadPool(20);
        Queue<Future<String>> futList = new ConcurrentLinkedQueue<Future<String>>();
        File file3 = new File("F:\\BMW\\data");
        String[] list = file3.list();
        Map<String, Integer> imgMap = new ConcurrentHashMap<>();
        String one = null;
        String two = null;
        try {
            for (int i = 0; i < list.length; i++) {
                File file1 = new File("F:\\BMW\\data\\" + list[i]);
                String[] list1 = file1.list();
                for (int i1 = 0; i1 < list1.length; i1++) {
                    File file2 = new File("F:\\BMW\\data\\" + list[i] + "\\" + list1[i1]);
                    String[] list2 = file2.list();
                    for (int i2 = 0; i2 < list2.length; i2++) {
                        File file4 = new File("F:\\BMW\\data\\" + list[i] + "\\" + list1[i1] + "\\" + list2[i2]);
                        String filePath = file4.getAbsolutePath();
                        File imgDir = new File("F:\\BMW\\downPic");
                        for (File subDir : imgDir.listFiles()) {
                            for (File file : subDir.listFiles()) {
                                imgMap.put(subDir.getName() + "-" + file.getName(), 1);
                            }
                        }
                        File file = new File(filePath);

                        LineIterator lineIterator = FileUtils.lineIterator(file);
                        while (lineIterator.hasNext()) {
                            String line = lineIterator.nextLine();
                            String[] split = line.split("\",\"");
                            String s = split[6];
                            String s2 = s.replaceAll("//", "/");
                            String[] split1 = s2.split("/");
                            one = split1[3];
                            two = split1[4];
                            System.out.println(count.getAndIncrement());
                            try {
                                if (imgMap.get(one + "-" + two) == null) {
                                    futList.add(fixedThreadPool2.submit(new TestBMW2bbUrl.Parser3(one, two)));
                                    imgMap.put(one + "-" + two, 1);
                                }
                            } catch (Exception e) {
                                logger.error(line + "\t" + e.getMessage());
                                imgMap.put(one + "-" + two, 1);
                            }
                        }
                    }
                }
            }
            System.out.println("共有" + imgMap.size() + "个图片");
        } catch (Exception e) {
            File dir2 = new File("F:\\BMW\\bugPic\\" + one + " - " + two + ".txt");
            try {
                FileUtils.write(dir2, one + " - " + two, "UTF-8");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        try {
            Thread.currentThread().sleep(5 * 24 * 60 * 60 * 1000);
        } catch (InterruptedException e) {
            File dir2 = new File("F:\\BMW\\bugPic\\" + one + " - " + two + ".txt");
            try {
                FileUtils.write(dir2, one + " - " + two, "UTF-8");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        fixedThreadPool2.shutdown();
    }


    class Parser3 implements Callable<String> {

        private String one;
        private String two;

        public Parser3(String one, String two) {

            this.one = one;
            this.two = two;
        }

        @Override
        public String call() {
            try {
                System.out.println("在下载" + downloadCnt++);
                byte[] data = HttpServiceUtil.downloadPic("https://bmw.7zap.com/images/" + one + "/" + two);
                File dir = new File("F:\\BMW\\downPic\\" + one);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                File targetFile = new File(dir, two);
                FileUtils.writeByteArrayToFile(targetFile, data);
            } catch (Exception e) {
                File dir2 = new File("F:\\BMW\\bugPic\\" + one + " - " + two + ".txt");
                try {
                    FileUtils.write(dir2, one + " - " + two, "UTF-8");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                logger.error(e.getMessage());
            }
            return null;
        }
    }

    @Test//出错的图片
    public void downPic2(){
        String one="w_grafik";
        String two="468792_z.jpg";
        try {
            byte[] data = HttpServiceUtil.downloadPic("https://bmw.7zap.com/images/" + one + "/" + two);
            File dir = new File("F:\\BMW\\downPic\\" + one);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File targetFile = new File(dir, two);
            FileUtils.writeByteArrayToFile(targetFile, data);
        } catch (Exception e) {
            File dir2 = new File("F:\\BMW\\bugPic\\" + one + " - " + two + ".txt");
            try {
                FileUtils.write(dir2, one + " - " + two, "UTF-8");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            logger.error(e.getMessage());
        }
    }
}



