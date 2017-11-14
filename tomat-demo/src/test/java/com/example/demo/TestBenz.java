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
public class TestBenz {
    private static Logger logger = LoggerFactory.getLogger(TestBenz.class);
    //    int count = 0;
    int downloadCnt = 0;
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
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(20);
        CompletionService<String> execcomp = new ExecutorCompletionService<String>(fixedThreadPool);
        String fileName = null;
        try {
            //生成html
            File file2 = new File("E:\\Mercedes\\path");
            String[] list1 = file2.list();
            for (int j = 0; j < list1.length; j++) {
                File file = new File("E:\\Mercedes\\path\\" + list1[j]);
                String[] files = file.list();
                for (int i = 0; i < files.length; i++) {
                    File readfile = new File("E:\\Mercedes\\path\\" + list1[j] + "\\" + files[i]);
                    String[] list = readfile.list();
                    for (int i1 = 0; i1 < list.length; i1++) {
                        File file1 = new File("E:\\Mercedes\\path\\" + list1[j] + "\\" + files[i] + "\\" + list[i1]);
                        fileName = list[i1];
                        execcomp.submit(new TestBenz.Parser2(file1, fileName));
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Thread.currentThread().sleep(5 * 24 * 60 * 60 * 1000);
        } catch (InterruptedException e) {
            File dir2 = new File("F:\\Mercedes\\bugHtml\\" + fileName + " - " + count.get() + ".txt");
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
                    String filepath = "F:\\Mercedes\\html\\" + middle + "\\" + name + "\\" + name + "-" + number + ".html";
                    File dir = new File("F:\\Mercedes\\html\\" + middle + "\\" + name);
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
                            imgSrc = "https://mercedes.7zap.com" + src;
                            stringBuilder.append(imgSrc + ";");
                        }
                        select.attr("src", imgSrc);
                        saveHtml(path, document0.html());
                    } catch (Exception e) {
                        File dir2 = new File("F:\\Mercedes\\bugHtml\\" + middle + "\\" + name + "\\" + name + "-" + number + ".txt");
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
        File file = new File("F:\\Mercedes\\bugHtml");
        String[] list = file.list();
        for (int i = 0; i < list.length; i++) {
            File file1 = new File("F:\\Mercedes\\bugHtml\\" + list[i]);
            String[] list1 = file1.list();
            for (int i1 = 0; i1 < list1.length; i1++) {
                File file2 = new File("F:\\Mercedes\\bugHtml\\" + list[i] + "\\" + list1[i1]);
                String[] list2 = file2.list();
                for (int i2 = 0; i2 < list2.length; i2++) {
                    File file3 = new File("F:\\Mercedes\\bugHtml\\" + list[i] + "\\" + list1[i1] + "\\" + list2[i2]);
                    String filepath = null;
                    try {
                        InputStreamReader read = new InputStreamReader(new FileInputStream(file3), "GBK");
                        BufferedReader bufferedReader = new BufferedReader(read);
                        String lineTxt = null;
                        while ((lineTxt = bufferedReader.readLine()) != null) {
                            String s = lineTxt;
                            filepath = "F:\\Mercedes\\lessHtml\\" + list[i] + "\\" + list1[i1] + "\\" + list2[i2].substring(0, list2[i2].lastIndexOf(".")) + ".html";
                            File dir = new File("F:\\Mercedes\\lessHtml\\" + list[i] + "\\" + list1[i1]);
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
                                imgSrc = "https://mercedes.7zap.com" + src;
                                stringBuilder.append(imgSrc + ";");
                            }
                            select.attr("src", imgSrc);
                            saveHtml(path, document0.html());
                            count++;
                            System.out.println(count + ":" + path);
                        }
                    } catch (IOException e) {
                        File dir2 = new File("F:\\Mercedes\\hh\\" + filepath + ".txt");
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
        File fileFile = new File("F:\\Mercedes\\html");
        String[] list = fileFile.list();
        for (int j = 0; j < list.length; j++) {
            File file = new File("F:\\Mercedes\\html\\" + list[j]);
            String[] list1 = file.list();
            for (int c = 0; c < list1.length; c++) {
                File file1 = new File("F:\\Mercedes\\html\\" + list[j] + "\\" + list1[c]);
                stringBuilder = new StringBuilder("");
                File[] filelist = file1.listFiles();
                for (int n = 0; n < filelist.length; n++) {
                    String name = filelist[n].getName();
                    String path = null;
                    try {
                        File compNameFile2 = new File("F:\\Mercedes\\data\\" + list[j] + "\\" + list1[c]);
                        if (!compNameFile2.exists()) {
                            compNameFile2.mkdirs();
                        }
                        File compNameFile = new File("F:\\Mercedes\\data\\" + list[j] + "\\" + list1[c] + "\\" + list1[c] + ".txt");
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
                                String brand = split1[2];
                                //System.out.println(pinpai);//获取品牌
                                stringBuilder.append("\"" + brand + "\"" + ",");
                                String type = split1[1].replaceAll("\\[", "").replaceAll("\\]", "").trim();
                                stringBuilder.append("\"" + type + "\"" + ",");
                                //获取车类型

                                String source = split1[3].replaceAll("\\[", "").replaceAll("\\]", "").trim();
                                //System.out.println(chanyuandi);//产源地
                                stringBuilder.append("\"" + source + "\"" + ",");

                                String chexi = document.select("#breadcrumbs > li:nth-child(4)").first().text().trim();
                                //System.out.println(chexi);//获取车系名称
                                stringBuilder.append("\"" + chexi + "\"" + ",");
//                                    stringBuilder.append("\"" + niankuan + "\"" + ",");
                                String chassis = document.select("#breadcrumbs > li:nth-child(6) ").first().text().trim();
                                stringBuilder.append("\"" + chassis + "\"" + ",");//获取车底盘名称


                                String first = document.select("#breadcrumbs > li:nth-child(9)").first().text().trim();
                                //System.out.println(zhuzu);//获取零部件主组
                                stringBuilder.append("\"" + first + "\"" + ",");

                                String second = document.select("#breadcrumbs > li:nth-child(10)").first().text().trim();
                                //System.out.println(zizu);//获取零部二级组
                                stringBuilder.append("\"" + second + "\"" + ",");

                                String third = document.select("#breadcrumbs > li.active.hidden-xs").first().text().trim();
                                //System.out.println(zizu);//获取零部件三级组
                                stringBuilder.append("\"" + third + "\"" + ",");

                                //获取配件图
                                Element img = document.select("img[alt='[image]']").first();
                                String url = img.attr("src");
                                String s = url.replace("/ImgsWatermark", "");
                                stringBuilder.append("\"" + s + "\"" + ",");
                                Elements select2 = element1.children();
                                for (int v = 0; v < select2.size(); v++) {
                                    String get = select2.get(v).text();
                                    get.replace("&nbsp;&nbsp", " ");
                                    if (v >= 4) {
                                        Elements spans = select2.select("span[data-toggle='modal']");
                                        if (spans.size() > 0) {
                                            //获取适用性连接
                                            if (spans != null && !"".equals(spans)) {
                                                for (int a = 0; a < spans.size(); a++) {
                                                    String onclick = spans.get(a).attr("onclick");
                                                    String substring1 = onclick.substring(onclick.indexOf("(") + 1, onclick.lastIndexOf(")"));
                                                    String szz = substring1.trim().replaceAll("\"", "").replaceAll(" ", "").replaceAll("'", "");
                                                    String[] split2 = szz.split(",");
                                                    StringBuilder stringBuilder1 = null;
                                                    if (a == 0) {
                                                        count++;
                                                        String url1 = "https://mercedes.7zap.com/cat_scripts/get_popup.php?lang=cn&catalog=" + split2[1] + "&window_type=" + split2[7] + "&maybach=" + split2[2] + "&wheel_class=" + split2[3] + "&set_aggtyp=" + split2[4] + "&set_partnum=" + split2[5] + "&_=" + count;
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
                                                        //类似物
                                                        String url2 = "https://mercedes.7zap.com/cat_scripts/get_popup.php?lang=cn&catalog=" + split2[1] + "&window_type=" + split2[7] + "&maybach=" + split2[2] + "&wheel_class=" + split2[3] + "&set_aggtyp=" + split2[4] + "&set_partnum=" + split2[5] + "&_=" + count;
                                                        String s1 = url2.replaceAll("'", "");
                                                        stringBuilder1 = new StringBuilder();
                                                        stringBuilder1.append(s1 + ";");
                                                        if (spans.size() == 2) {
                                                            stringBuilder.append("\"" + stringBuilder1 + "\"" + "," + "\r\n");
                                                        } else {
                                                            stringBuilder.append("\"" + stringBuilder1 + "\"" + ",");
                                                        }
                                                    }
                                                    if (a > 1) {
                                                        count++;
                                                        imgUrl = split2[2].replaceAll("'", "");
                                                        if (imgUrl != null && !"".equals(imgUrl)) {
                                                            String url3 = "https://mercedes.7zap.com" + imgUrl;
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

                                        }
                                    } else {
                                        stringBuilder.append("\"" + get + "\"" + ",");
                                    }
                                }
                            }
                            writer.write(stringBuilder.toString());
                            writer.flush();

                        }
                    } catch (IOException e) {
                        File dir2 = new File("F:\\Mercedes\\bugData\\" + list[j] + "\\" + list1[c] + "\\" + filelist[n].getName() + ".txt");
                        try {
                            FileUtils.write(dir2, list[j] + " - " + list1[c] + " - " + filelist[n] + "出bug了" + imgUrl, "UTF-8");
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        logger.error(list[j] + " - " + list1[c] + "里的" + filelist[n] + "出bug了" + imgUrl + e.getMessage());
                    }
                }
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
        File file3 = new File("F:\\Mercedes\\entranceData");
        String[] list = file3.list();
        Map<String, Integer> imgMap = new ConcurrentHashMap<>();
        String one = null;
        String two = null;
        try {
            for (int i = 0; i < list.length; i++) {
                File file1 = new File("F:\\Mercedes\\entranceData\\" + list[i]);
                String[] list1 = file1.list();
                for (int i1 = 0; i1 < list1.length; i1++) {
                    File file2 = new File("F:\\Mercedes\\entranceData\\" + list[i] + "\\" + list1[i1]);
                    String[] list2 = file2.list();
                    for (int i2 = 0; i2 < list2.length; i2++) {
                        File file4 = new File("F:\\Mercedes\\entranceData\\" + list[i] + "\\" + list1[i1] + "\\" + list2[i2]);
                        String filePath = file4.getAbsolutePath();
                        File imgDir = new File("F:\\Mercedes\\downPic");
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
                            String s = split[8];
                            String s2 = s.replaceAll("//", "/");
                            String[] split1 = s2.split("/");
                            one = split1[3];
                            two = split1[4];
                            System.out.println(count.getAndIncrement());
                            try {
                                if (imgMap.get(one + "-" + two) == null) {
                                    futList.add(fixedThreadPool2.submit(new TestBenz.Parser3(one, two)));
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
            File dir2 = new File("F:\\Mercedes\\bugPic\\" + one + " - " + two + ".txt");
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
            File dir2 = new File("F:\\Mercedes\\bugPic\\" + one + " - " + two + ".txt");
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
                byte[] data = HttpServiceUtil.downloadPic("https://mercedes.7zap.com//Imgs/" + one + "/" + two);
                File dir = new File("F:\\Mercedes\\downPic\\" + one);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                File targetFile = new File(dir, two);
                FileUtils.writeByteArrayToFile(targetFile, data);
            } catch (Exception e) {
                File dir2 = new File("F:\\Mercedes\\bugPic\\" + one + " - " + two + ".txt");
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

}



