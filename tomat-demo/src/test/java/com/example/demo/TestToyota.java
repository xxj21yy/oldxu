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
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by cc on 2017/8/14.
 */
//通过一级网址
public class TestToyota {
    private static Logger logger = LoggerFactory.getLogger(TestToyota.class);
    int num = 0;
    AtomicInteger count = new AtomicInteger();

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
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(20);
        CompletionService<String> execcomp = new ExecutorCompletionService<String>(fixedThreadPool);
        String fileName = null;
        try {
            //生成html
            File file2 = new File("E:\\Toyota\\path");
            String[] list1 = file2.list();
            for (int j = 0; j < list1.length; j++) {
                File file = new File("E:\\Toyota\\path\\" + list1[j]);
                String[] files = file.list();
                for (int i = 0; i < files.length; i++) {
                    File readfile = new File("E:\\Toyota\\path\\" + list1[j] + "\\" + files[i]);
                    String[] list = readfile.list();
                    for (int i1 = 0; i1 < list.length; i1++) {
                        File file1 = new File("E:\\Toyota\\path\\" + list1[j] + "\\" + files[i] + "\\" + list[i1]);
                        fileName = list[i1];
                        execcomp.submit(new TestToyota.Parser2(file1, fileName));
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Thread.currentThread().sleep(5 * 24 * 60 * 60 * 1000);
        } catch (InterruptedException e) {
            File dir2 = new File("F:\\Toyota\\bugHtml\\" + fileName + " - " + count.get() + ".txt");
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
                    String filepath = "F:\\Toyota\\html\\" + middle + "\\" + name + "\\" + name + "-" + number + ".html";
                    File dir = new File("F:\\Toyota\\html\\" + middle + "\\" + name);
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
                        Elements select = document0.select("body > div.row > div > img");
                        String imgSrc = null;
                        String src = null;
                        StringBuilder stringBuilder = new StringBuilder();
                        for (Element element : select) {
                            src = select.attr("src");
                            imgSrc = "https://toyota.7zap.com" + src;
                            stringBuilder.append(imgSrc + ";");
                        }
                        select.attr("src", imgSrc);
                        saveHtml(path, document0.html());
                    } catch (Exception e) {
                        File dir2 = new File("F:\\Toyota\\bugHtml\\" + middle + "\\" + name + "\\" + name + "-" + number + ".txt");
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
        File file = new File("F:\\Toyota\\bugHtml");
        String[] list = file.list();
        for (int i = 0; i < list.length; i++) {
            File file1 = new File("F:\\Toyota\\bugHtml\\" + list[i]);
            String[] list1 = file1.list();
            for (int i1 = 0; i1 < list1.length; i1++) {
                File file2 = new File("F:\\Toyota\\bugHtml\\" + list[i] + "\\" + list1[i1]);
                String[] list2 = file2.list();
                for (int i2 = 0; i2 < list2.length; i2++) {
                    File file3 = new File("F:\\Toyota\\bugHtml\\" + list[i] + "\\" + list1[i1] + "\\" + list2[i2]);
                    String filepath = null;
                    try {
                        InputStreamReader read = new InputStreamReader(new FileInputStream(file3), "GBK");
                        BufferedReader bufferedReader =new BufferedReader(read);
                        String lineTxt = null;
                        while ((lineTxt = bufferedReader.readLine()) != null) {
                            String s = lineTxt;
                            filepath = "F:\\Toyota\\lessHtml\\" + list[i] + "\\" + list1[i1] + "\\" + list2[i2].substring(0, list2[i2].lastIndexOf(".")) + ".html";
                            File dir = new File("F:\\Toyota\\lessHtml\\" + list[i] + "\\" + list1[i1]);
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
                            Elements select = document0.select("body > div.row > div > img");
                            String imgSrc = null;
                            String src = null;
                            StringBuilder stringBuilder = new StringBuilder();
                            for (Element element : select) {
                                src = select.attr("src");
                                imgSrc = "https://toyota.7zap.com" + src;
                                stringBuilder.append(imgSrc + ";");
                            }
                            select.attr("src", imgSrc);
                            saveHtml(path, document0.html());
                            count++;
                            System.out.println(count + ":" + path);
                        }
                    } catch (IOException e) {
                        File dir2 = new File("F:\\Toyota\\hh\\" + filepath + ".txt");
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
        File fileFile = new File("F:\\Toyota\\html");
        String[] list = fileFile.list();
        for (int j = 0; j < list.length; j++) {
            File file = new File("F:\\Toyota\\html\\" + list[j]);
            String[] list1 = file.list();
            for (int c = 0; c < list1.length; c++) {
                File file1 = new File("F:\\Toyota\\html\\" + list[j] + "\\" + list1[c]);
                stringBuilder = new StringBuilder("");
                File[] filelist = file1.listFiles();
                for (int n = 0; n < filelist.length; n++) {
                    String name = filelist[n].getName();
                    String path = null;
                    try {
                        File compNameFile2 = new File("F:\\Toyota\\data\\" + list[j] + "\\" + list1[c]);
                        if (!compNameFile2.exists()) {
                            compNameFile2.mkdirs();
                        }
                        File compNameFile = new File("F:\\Toyota\\data\\" + list[j] + "\\" + list1[c] + "\\" + list1[c] + ".txt");
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
                            Elements select7 = select.children();
                            for (Element element1 : select7) {
                                Elements tbody = element1.select("tbody");
                                if (tbody != null && !"".equals(tbody) && tbody.size() > 0) {
                                    Elements select22 = tbody.select("tr");
                                        if (select22 != null && !"".equals(select22) && select22.size() > 0) {
                                            for (Element element2 : select22) {
                                                String text = document.select("#breadcrumbs > li:nth-child(2) > a").first().text();
                                                String[] split1 = text.split(" ");
                                                String pinpai = split1[1];
                                                stringBuilder.append("\"" + pinpai + "\"" + ",");

                                                String chanyuandi = text.substring(text.indexOf("[") + 1, text.lastIndexOf("]"));
                                                stringBuilder.append("\"" + chanyuandi + "\"" + ",");

                                                String chexi = document.select("#breadcrumbs > li:nth-child(5)").first().text();
                                                stringBuilder.append("\"" + chexi + "\"" + ",");
                                                String chassis1 = document.select("#breadcrumbs > li:nth-child(8)").first().text();
                                                String[] split = chassis1.split("\\[");
                                                String s2 = split[1];
                                                String chassis = s2.substring(0, s2.length() - 1);
                                                stringBuilder.append("\"" + chassis + "\"" + ",");//获取车底盘名称

                                                String zhuzu = split[0];
                                                stringBuilder.append("\"" + zhuzu + "\"" + ",");
                                                String zizu = document.select("#breadcrumbs > li.active.hidden-xs").first().text();//需修改
                                                stringBuilder.append("\"" + zizu + "\"" + ",");

                                                //获取配件图#detail_image
                                                Element img = document.select("#detail_image").first();
                                                String url = img.attr("src");
                                                String s = url.replace("/ImgsWatermark", "");
                                                stringBuilder.append("\"" + s + "\"" + ",");

                                                Elements select2 = element2.select("td");
                                                for (int v = 0; v < select2.size(); v++) {
                                                    String get = select2.get(v).text();
                                                    get.replace("&nbsp;&nbsp", " ");
                                                    stringBuilder.append("\"" + get + "\"" + ",");
                                                    if (v > 1) {
                                                        Elements spans = select2.select("span[data-toggle='modal']");
                                                        //获取适用性连接https://volkswagen.7zap.com/cat_scripts/get_analogs.php?lang=cn&detail=WHT004877&imagelink=/images/Tnrpics/WHT004877.jpg&showpic=info&_=1500623042332
                                                        if (spans != null && !"".equals(spans) && spans.size() > 0) {
                                                            for (int a = 0; a < spans.size(); a++) {
                                                                String onclick = spans.get(a).attr("onclick");
                                                                String substring1 = onclick.substring(onclick.indexOf("(") + 1, onclick.lastIndexOf(")"));
                                                                String szz = substring1.trim().replaceAll("\"", "").replaceAll(" ", "").replaceAll("'", "");
                                                                String[] split2 = szz.split(",");
                                                                StringBuilder stringBuilder1 = null;
                                                                if (a == 0) {
                                                                    count++;
                                                                    String url1 = "https://toyota.7zap.com/cat_scripts/get_popup.php?catalog=" + split2[1] + "&lang=en&lexus=" + split2[2] + "&name=" + split2[3] + "&catalog_code=" + split2[4] + "&model_code=" + split2[5] + "&group=" + split2[6] + "&group_code=" + split2[7] + "&detail=" + split2[8] + "&detailName=" + split2[9] + "&window_type=" + split2[10] + "&_=0";
                                                                    //System.out.println("适用性：" + url);
                                                                    String s1 = url1.replaceAll("'", "");
                                                                    stringBuilder1 = new StringBuilder();
                                                                    stringBuilder1.append(s1 + ";");
                                                                    if (spans.size() == 1) {
                                                                        stringBuilder.append("\"" + stringBuilder1 + "\"" + "," + "\r\n");
                                                                    } else {
                                                                        stringBuilder.append("\"" + stringBuilder1 + "\"" + ",");
                                                                    }
                                                                }
                                                                if (a == 1) {
                                                                    count++;
                                                                    //https://bmw.7zap.com/cat_scripts/get_cars_for_this_detail.php?classic=VT&lang=zh&detail=51927896863&window_type=analog&_=1502959179550
                                                                    String url2 = "https://toyota.7zap.com/cat_scripts/get_popup.php?catalog=" + split2[1] + "&lang=en&lexus=" + split2[2] + "&name=" + split2[3] + "&catalog_code=" + split2[4] + "&model_code=" + split2[5] + "&group=" + split2[6] + "&group_code=" + split2[7] + "&detail=" + split2[8] + "&detailName=" + split2[9] + "&window_type=" + split2[10] + "&_=0";
                                                                    //System.out.println("类似物：" + url);
                                                                    String s1 = url2.replaceAll("'", "");
                                                                    stringBuilder1 = new StringBuilder();
                                                                    stringBuilder1.append(s1 + ";");
                                                                    if (spans.size() == 2) {
                                                                        stringBuilder.append("\"" + stringBuilder1 + "\"" + "," + "\r\n");
                                                                    } else {
                                                                        stringBuilder.append("\"" + stringBuilder1 + "\"" + ",");
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        writer.write(stringBuilder.toString());
                                        writer.flush();
                                } else {
                                    Elements select1 = element1.select("tr[style='background-color: #428bca;']");
                                    if (select1.size()>0){

                                    }else {
                                        String text = document.select("#breadcrumbs > li:nth-child(2) > a").first().text();
                                        String[] split1 = text.split(" ");
                                        String pinpai = split1[1];
                                        stringBuilder.append("\"" + pinpai + "\"" + ",");

                                        String chanyuandi = text.substring(text.indexOf("[") + 1, text.lastIndexOf("]"));
                                        stringBuilder.append("\"" + chanyuandi + "\"" + ",");

                                        String chexi = document.select("#breadcrumbs > li:nth-child(5)").first().text();
                                        stringBuilder.append("\"" + chexi + "\"" + ",");
                                        String chassis1 = document.select("#breadcrumbs > li:nth-child(8)").first().text();
                                        String[] split = chassis1.split("\\[");
                                        String s2 = split[1];
                                        String chassis = s2.substring(0, s2.length() - 1);
                                        stringBuilder.append("\"" + chassis + "\"" + ",");//获取车底盘名称


                                        String zhuzu = split[0];
                                        stringBuilder.append("\"" + zhuzu + "\"" + ",");

                                        String zizu = document.select("#breadcrumbs > li.active.hidden-xs").first().text();//需修改
                                        stringBuilder.append("\"" + zizu + "\"" + ",");

                                        //获取配件图#detail_image
                                        Element img = document.select("#detail_image").first();
                                        String url = img.attr("src");
                                        String s = url.replace("/ImgsWatermark", "");
                                        stringBuilder.append("\"" + s + "\"" + ",");
                                        Elements select2 = element1.select("td");
                                        if (select2 != null && !"".equals(select2) && select2.size() > 0) {
                                            for (int v = 0; v < select2.size(); v++) {
                                                String get = select2.get(v).text();
                                                get.replace("&nbsp;&nbsp", " ");
                                                stringBuilder.append("\"" + get + "\"" + ",");
                                                if (v > 2) {
                                                    Elements spans = select2.select("span[data-toggle='modal']");
                                                    //获取适用性连接https://volkswagen.7zap.com/cat_scripts/get_analogs.php?lang=cn&detail=WHT004877&imagelink=/images/Tnrpics/WHT004877.jpg&showpic=info&_=1500623042332
                                                    if (spans != null && !"".equals(spans) && spans.size() > 0) {
                                                        for (int a = 0; a < spans.size(); a++) {
                                                            String onclick = spans.get(a).attr("onclick");
                                                            String substring1 = onclick.substring(onclick.indexOf("(") + 1, onclick.lastIndexOf(")"));
                                                            String szz = substring1.trim().replaceAll("\"", "").replaceAll(" ", "").replaceAll("'", "");
                                                            String[] split2 = szz.split(",");
                                                            StringBuilder stringBuilder1 = null;
                                                            if (a == 0) {
                                                                count++;
                                                                String url1 = "https://toyota.7zap.com/cat_scripts/get_popup.php?catalog=" + split2[1] + "&lang=en&lexus=" + split2[2] + "&name=" + split2[3] + "&catalog_code=" + split2[4] + "&model_code=" + split2[5] + "&group=" + split2[6] + "&group_code=" + split2[7] + "&detail=" + split2[8] + "&detailName=" + split2[9] + "&window_type=" + split2[10] + "&_=0";
                                                                //System.out.println("适用性：" + url);
                                                                String s1 = url1.replaceAll("'", "");
                                                                stringBuilder1 = new StringBuilder();
                                                                stringBuilder1.append(s1 + ";");
                                                                if (spans.size() == 1) {
                                                                    stringBuilder.append("\"" + stringBuilder1 + "\"" + "," + "\r\n");
                                                                } else {
                                                                    stringBuilder.append("\"" + stringBuilder1 + "\"" + ",");
                                                                }
                                                            }
                                                            if (a == 1) {
                                                                count++;
                                                                //https://bmw.7zap.com/cat_scripts/get_cars_for_this_detail.php?classic=VT&lang=zh&detail=51927896863&window_type=analog&_=1502959179550
                                                                String url2 = "https://toyota.7zap.com/cat_scripts/get_popup.php?catalog=" + split2[1] + "&lang=en&lexus=" + split2[2] + "&name=" + split2[3] + "&catalog_code=" + split2[4] + "&model_code=" + split2[5] + "&group=" + split2[6] + "&group_code=" + split2[7] + "&detail=" + split2[8] + "&detailName=" + split2[9] + "&window_type=" + split2[10] + "&_=0";
                                                                //System.out.println("类似物：" + url);
                                                                String s1 = url2.replaceAll("'", "");
                                                                stringBuilder1 = new StringBuilder();
                                                                stringBuilder1.append(s1 + ";");
                                                                if (spans.size() == 2) {
                                                                    stringBuilder.append("\"" + stringBuilder1 + "\"" + "," + "\r\n");
                                                                } else {
                                                                    stringBuilder.append("\"" + stringBuilder1 + "\"" + ",");
                                                                }
                                                            }
                                                        }
                                                    }else{
                                                        stringBuilder.append("\"" + get + "\"" + ","+"\r\n");
                                                    }
                                                }
                                            }
                                            writer.write(stringBuilder.toString());
                                            writer.flush();
                                        }
                                    }

                                }
                            }
                        }

                    } catch (IOException e) {
                        File dir2 = new File("F:\\Toyota\\bugData\\" + list[j] + "\\" + list1[c] + "\\" + filelist[n].getName() + ".txt");
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

    @Test
    public void test1111(){
        String url="https://toyota.7zap.com/en/eu/corolla/151210/ae80r-ehhdcq/1/1603/ma6744i/#16268";
        try {
            Document parse = Jsoup.parse(HttpServiceUtil.doGet(url));
            File file=new File("F:\\Toyota\\nima\\a.html");
            String path = file.getPath();
            saveHtml(path,parse.html());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



