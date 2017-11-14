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
public class TestBMW {
    private static Logger logger = LoggerFactory.getLogger(TestBMW.class);
    AtomicInteger count = new AtomicInteger();
    int num = 0;
    int downloadCnt = 0;

    @Test//生成path
    public void test() {
        int jia = 1;
        String s8 = null;
        String fileName = null;
        StringBuilder stringBuilder = new StringBuilder();
        List<Future<String>> futList = new ArrayList<Future<String>>();
        stringBuilder.append("https://bmw.7zap.com/zh/bmw/");
        String s = "https://bmw.7zap.com/cat_scripts/get_models.php?lang=en&mark=bmw&classic=VT&_=" + jia;
        Document document = null;//连接url
        document = connectUrl(s);


        Elements select = document.select("div[class='table-responsive']");
        Elements select3 = select.select("tr[onmouseout='this.classList.remove('model_select');']");
//        Elements select3 = document.select("#collapse_1 > div > div > table > tbody > tr");
//        Elements select4 = select3.select("tr[onmouseout='this.classList.remove('model_select');']");

        try {
            for (Element element66 : select3) {
                String onclick = element66.attr("onclick");
                String groupId = null;
                if (onclick != null && !"".equals(onclick)) {
                    String[] split = getValue(onclick);
                    jia++;
                    Document document1 = null;
                    Document document333 = null;
                    String s1 = "https://bmw.7zap.com/cat_scripts/get_model.php?lang=zh&modification=" + split[3].trim() + "&modification_type=" + split[4].trim() + "&modification_name=" + split[2].trim() + "&model_name=" + split[0].trim() + "&vin=" + split[7].trim() + "&_=" + jia;
                    String s2 = s1.replaceAll("'", "");
                    document1 = connectUrl(s2);//连接url

                    Elements select4 = document1.select("div[class='btn-group']");
                    Elements select5 = select4.select("input[name='wheelname']");
                    for (Element element : select5) {
                        String wheelname = element.attr("value");
                        Elements select1 = select4.select("input[name='transmissionname']");
                        for (Element element1 : select1) {
                            String transmissionname = element1.attr("value");
                            Elements select2 = select4.select("input[name='yearname']");
                            for (Element element2 : select2) {
                                String yearname = element2.attr("value");
                                Elements select6 = select4.select("input[name='monthname']");
                                for (Element element3 : select6) {
                                    String monthname = element3.attr("value");
                                    Elements select7 = select4.select("input[name='regionname']");
                                    for (Element element4 : select7) {
                                        String regionname = element4.attr("value");
                                        String s3 = "https://bmw.7zap.com/cat_scripts/get_part_groups.php?lang=zh&modification=" + split[2] + "&modelname=" + split[4] + "&year=" + yearname + "&month=" + monthname + "&wheel=" + wheelname + "&transmission=" + transmissionname + "&classic=VT&region=" + regionname + "&group=" + groupId + "&_=" + jia;
                                        String s4 = s3.replaceAll("'", "");
                                        s4 = s4.replace(" ", "");
                                        document333 = Jsoup.parse(HttpServiceUtil.doGet(s4));//选择年份、产地等的页面
                                        //得到三级页面
                                        Elements select8 = document333.select("div[class='panel-group  col-xs-48 col-sm-12 col-md-8 col-lg-8']");
                                        String s44 = null;
                                        for (Element element111 : select8) {
                                            Elements select9 = element111.select("div[onmouseout=' this.className='panel panel-default';']");
                                            String onclick1 = select9.attr("onclick");
                                            String[] split1 = getValue(onclick1);
                                            jia++;
                                            groupId = split1[10].trim();

                                            String s33 = "https://bmw.7zap.com/cat_scripts/get_part_subgroups.php?lang=zh&modification=" + split[2] + "&modelname=" + split[4] + "&year=" + yearname + "&month=" + monthname + "&wheel=" + wheelname + "&transmission=" + transmissionname + "&classic=VT&region=" + regionname + "&group=" + groupId + "&_=" + jia;
                                            s44 = s33.replaceAll("'", "").trim();
                                            //最终页面的获取
                                            Document document2 = connectUrl(s44);
                                            Elements select22 = document2.select("body > div.row > div > div");
                                            String substring3 = null;
                                            fileName = null;
                                            String str = null;
                                            for (Element element77 : select22) {
                                                String onclick77 = element77.attr("onclick");
                                                if (onclick77 != null && !"".equals(onclick77)) {
                                                    String[] split2 = getValue(onclick77);
                                                    String trim = split2[2];
                                                    fileName = trim.substring(trim.indexOf("'") + 1, trim.lastIndexOf("'")).trim();
                                                    jia++;
                                                    s8 = "https://bmw.7zap.com/cat_scripts/get_parts.php?lang=zh&modification=" + split[2] + "&modelname=" + split[4] + "&year=" + yearname + "&month=" + monthname + "&wheel=" + wheelname + "&transmission=" + transmissionname + "&classic=VT&region=" + regionname + "&group=" + groupId + "&subgroup=" + split2[11] + "&_=" + jia;
                                                    s8 = s8.replaceAll("'", "").trim();
                                                    System.out.println(fileName + " - " + s8);
                                                    //生成url的txt
                                                    count.getAndIncrement();
                                                    File dir = new File("F:\\BMW\\path\\" + fileName + "\\" + fileName + "-" + count.get() + ".txt");
                                                    FileUtils.write(dir, s8, "UTF-8");
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
        } catch (Exception e) {
            File dir = new File("F:\\Dazhong\\Europa\\bugpath\\" + fileName + " - " + count.get() + ".txt");
            try {
                FileUtils.write(dir, s8, "UTF-8");
            } catch (IOException e1) {
                logger.error("网址" + s8 + "出错了，问题是" + e1.getMessage());
            }
            logger.error("网址" + s8 + "出错了，问题是" + e.getMessage());
        }
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

    //HttpServiceUtil连接网址
    public static Document connectUrl(String url){
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
            File file = new File("F:\\Skoda\\path\\YETC");
            String[] files = file.list();
            for (int i = 0; i < files.length; i++) {
                File readfile = new File("F:\\Skoda\\path\\YETC\\" + files[i]);
                fileName = files[i];
                execcomp.submit(new TestBMW.Parser2(readfile, fileName));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Thread.currentThread().sleep(5 * 24 * 60 * 60 * 1000);
        } catch (InterruptedException e) {
            File dir2 = new File("F:\\Skoda\\bugHtml\\" + fileName + " - " + count.get() + ".txt");
            try {
                FileUtils.write(dir2, fileName, "UTF-8");
            } catch (IOException e1) {
                e1.printStackTrace();
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

            String number = fileName.substring(fileName.indexOf("-") + 1, fileName.lastIndexOf("."));
            String name = fileName.substring(0, fileName.lastIndexOf("-"));
            InputStreamReader read = null;//考虑到编码格式
            try {
                read = new InputStreamReader(new FileInputStream(file), "GBK");
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    String url = lineTxt;
                    String filepath = "F:\\Skoda\\html\\" + name + "-" + number + ".html";
                    File dir = new File("F:\\Skoda\\html\\" + name);
                    if (!dir.exists()) {
                        dir.mkdir();
                    }
                    File targetFile = new File(dir, name + "-" + number + ".html");
                    String path = targetFile.getAbsolutePath();
                    System.out.println(name + "-" + number + ":" + url);
                    try {
                        Document document0 = null;
                        url = url.replace(" ", "");
                        document0 = Jsoup.parse(HttpServiceUtil.doGet(url));
                        Elements select = document0.select("body > div > img");
                        String src = select.attr("src");
                        String imgSrc = "https://audi.7zap.com" + src;
                        select.attr("src", imgSrc);
                        saveHtml(path, document0.html());
                    } catch (Exception e) {
                        File dir2 = new File("F:\\Skoda\\bugHtml\\" + name + "-" + number + ".txt");
                        try {
                            FileUtils.write(dir2, url, "UTF-8");
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        logger.error(fileName + " - " + count.get() + "未写入，原因是：" + e.getMessage());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    @Test//出错的单独输出
    public void test2() {
        String s = "https://skoda.7zap.com/cat_scripts/parts_get.php?lang=cn&region=SVW&modification_date=2015&modification_type=768&modification_name=SUP&model_url=superb&group_id=8&model_name=Superb&hg_ug=821&pnc=821010&vin=&_=52924";
        String filepath = "F:\\Skoda\\bugHtml\\SUP-51933.html";
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


    @Test//多个车系解析html并导入到模板中
    public void importHtml() {
        StringBuilder stringBuilder = new StringBuilder();
        int num = 0;
        int count = 0;
        int num22 = 0;
        Writer writer = null;
        //递归文件夹下的所有文件
        File fileFile = new File("F:\\Skoda\\html");
        File[] filelist = fileFile.listFiles();
        for (int n = 0; n < filelist.length; n++) {
            File readfile = filelist[n];
            File[] filelist2 = readfile.listFiles();
            String path = null;
            try {
                File compNameFile = new File("F:\\Skoda\\data\\" + filelist[n].getName() + ".txt");
                if (!compNameFile.exists()) {
                    compNameFile.createNewFile();
                }
                writer = new BufferedWriter(new FileWriter(compNameFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < filelist2.length; i++) {
                stringBuilder = new StringBuilder("");
                File readfile2 = filelist2[i];
                num22++;
                System.out.println(num22 + " - " + filelist2[i]);
                //继续读取文件
                String imgUrl = null;
                try {
                    //获取配件数据
                    Document document = Jsoup.parse(readfile2, "UTF-8");
                    num++;
                    Element select = document.select("body > div.Ctree > table > tbody").first();
                    if (select != null && !"".equals(select)) {
                        Elements select1 = select.select("tr[class='one_part']");
                        for (Element element1 : select1) {
                            String text = document.select("#breadcrumbs > li:nth-child(2) > a").first().text();
                            String[] split1 = text.split(" ");
                            String pinpai = split1[1];
                            stringBuilder.append("\"" + pinpai + "\"" + ",");
                            //System.out.println(pinpai);//获取品牌

                            String text4 = document.select("#main_title > small").first().text();
                            String[] split = text4.split(" ");
                            String niankuan = split[1].trim();
                            //System.out.println(split[1].trim());//年款
                            String chanyuandi = split[0].substring(1, split[0].length()).trim();
                            //System.out.println(chanyuandi);//产源地
                            stringBuilder.append("\"" + chanyuandi + "\"" + ",");

                            String chexi = document.select("#breadcrumbs > li:nth-child(5)").first().text();
                            //System.out.println(chexi);//获取车系名称
                            stringBuilder.append("\"" + chexi + "\"" + ",");
                            stringBuilder.append("\"" + niankuan + "\"" + ",");

                            String zhuzu = document.select("#breadcrumbs > li:nth-child(9) > a").first().text();
                            //System.out.println(zhuzu);//获取零部件主组
                            stringBuilder.append("\"" + zhuzu + "\"" + ",");

//                            document.select("tr[style='background-color:Silver;'");
                            String zizu = document.select("#breadcrumbs > li:nth-child(11)").first().text();//需修改
                            //System.out.println(zizu);//获取零部件子组
                            stringBuilder.append("\"" + zizu + "\"" + ",");


                            //获取配件图
                            Element img = document.select("img[class='parts_image']").first();
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
                                                String szz = substring1.trim().replaceAll("\"", "").replaceAll(" ", "");
                                                //System.out.println(s);
                                                String[] split2 = szz.split(",");
                                                StringBuilder stringBuilder1 = null;
                                                if (a == 0) {
                                                    count++;
                                                    String url1 = "https://skoda.7zap.com/cat_scripts/get_analogs.php?lang=" + split2[0] + "&detail=" + split2[1] + "&imagelink=" + split2[2] + "&showpic=" + split2[3] + "&_=" + count;
                                                    //System.out.println("适用性：" + url);
                                                    stringBuilder1 = new StringBuilder();
                                                    stringBuilder1.append("\"" + url1 + "\"" + ";");
                                                    if (spans.size() == 1) {
                                                        stringBuilder.append("\"" + stringBuilder1 + "\"" + "," + "\r\n");
                                                    } else {
                                                        stringBuilder.append("\"" + stringBuilder1 + "\"" + ",");
                                                    }
                                                }
                                                if (a == 1) {
                                                    count++;
                                                    String url2 = "https://skoda.7zap.com/cat_scripts/get_analogs.php?lang=" + split2[0] + "&detail=" + split2[1] + "&imagelink=" + split2[2] + "&showpic=" + split2[3] + "&_=" + count;
                                                    //System.out.println("类似物：" + url);
                                                    stringBuilder1 = new StringBuilder();
                                                    stringBuilder1.append(url2 + ";");
                                                    if (spans.size() == 2) {
                                                        stringBuilder.append("\"" + stringBuilder1 + "\"" + "," + "\r\n");
                                                    } else {
                                                        stringBuilder.append("\"" + stringBuilder1 + "\"" + ",");
                                                    }
                                                }
                                                if (a > 1) {
                                                    count++;
                                                    imgUrl = split2[2];
                                                    if (imgUrl != null && !"".equals(imgUrl)) {
                                                        String url3 = "https://audi.7zap.com" + imgUrl;
                                                        //System.out.println("图片地址：" + imgUrl);
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
                        writer.write(stringBuilder.toString());
                        writer.flush();

                    }
                } catch (IOException e) {
                    File dir2 = new File("F:\\Skoda\\bugData\\" + filelist[n].getName() + ".txt");
                    try {
                        FileUtils.write(dir2, filelist2[i] + "出bug了" + imgUrl, "UTF-8");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    logger.error(filelist[n] + "里的" + filelist2[i] + "出bug了" + imgUrl + e.getMessage());
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
    public void downloadPic() {
        ExecutorService fixedThreadPool2 = Executors.newFixedThreadPool(5);
        Queue<Future<String>> futList = new ConcurrentLinkedQueue<Future<String>>();
        String filePath = "F:\\Skoda\\7zap-斯柯达汽车信息.txt";
        Map<String, Integer> imgMap = new ConcurrentHashMap<>();
        File imgDir = new File("F:\\Skoda\\downPic");
        for (File subDir : imgDir.listFiles()) {
            for (File file : subDir.listFiles()) {
                imgMap.put(subDir.getName() + "-" + file.getName(), 1);
            }
        }
        File file = new File(filePath);
        String one = null;
        String two = null;
        try {
            LineIterator lineIterator = FileUtils.lineIterator(file);
            while (lineIterator.hasNext()) {
                String line = lineIterator.nextLine();
                String[] split = line.split("\",\"");
                String s = split[6];
                String s2 = s.replaceAll("//", "/");
                String[] split1 = s2.split("/");
                one = split1[4];
                two = split1[5];
                System.out.println(count.getAndIncrement());
                try {
                    if (imgMap.get(one + "-" + two) == null) {
                        futList.add(fixedThreadPool2.submit(new TestBMW.Parser3(one, two)));
                        imgMap.put(one + "-" + two, 1);
                    }
                } catch (Exception e) {
                    logger.error(line + "\t" + e.getMessage());
                    imgMap.put(one + "-" + two, 1);
                }
            }
            System.out.println("共有" + imgMap.size() + "个图片");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        try {
            Thread.currentThread().sleep(5 * 24 * 60 * 60 * 1000);
        } catch (InterruptedException e) {
            File dir2 = new File("F:\\Dazhong\\Europa\\bugPic\\" + one + " - " + two + ".txt");
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
                byte[] data = HttpServiceUtil.downloadPic("https://audi.7zap.com/images//Bilder/" + one + "/" + two);
                File dir = new File("F:\\Skoda\\downPic\\" + one);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                File targetFile = new File(dir, two);
                FileUtils.writeByteArrayToFile(targetFile, data);
            } catch (Exception e) {
                File dir2 = new File("F:\\Skoda\\bugPic\\" + one + " - " + two + ".txt");
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



