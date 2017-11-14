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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by cc on 2017/7/26.
 */
public class TestAudi2 {
    private static Logger logger = LoggerFactory.getLogger(TestAudi2.class);
    int count = 0;//相等
    int num=0;
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
        Elements select3 = document.select("#collapse_2 > div > div > table > tbody > tr");
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
                            futList.add(fixedThreadPool.submit(new TestAudi2.Parser(s8, fileName)));
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
                String filepath = "F:\\Audi\\html\\A100\\" + fileName + "-" + num + ".html";
                Document document0 = null;
                try {
                    s = s.replace(" ", "");
                    document0 = Jsoup.parse(HttpServiceUtil.doGet(s));
                } catch (Exception e) {
                    logger.error("网页出bug了" + fileName + "-" + count + "问题是：" +s+e.getMessage());
                }
                Elements select = document0.select("body > div > img");
                String src = select.attr("src");
                String imgSrc = "https://audi.7zap.com" + src;
                select.attr("src", imgSrc);
                saveHtml(filepath, document0.html());

            } catch (Exception e) {
                logger.error("生成html出bug了==" + fileName + "-" + count + "问题是：" +s+e.getMessage());
            }
            return null;
        }
    }
    @Test//解析html
    public void html() throws InterruptedException {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(20);
        List<Future<String>> futList = new ArrayList<Future<String>>();
        try {
            //生成html
            File file = new File("F:\\Audi\\path1");
            String[] files = file.list();
            for (int i = 0; i < files.length; i++) {
                File readfile = new File("F:\\Audi\\path1\\" + files[i]);
                if (files[i].startsWith("A8")){
                    if (!readfile.isDirectory()) {
                        String fileName = files[i];
                        futList.add(fixedThreadPool.submit(new TestAudi2.Parser2(readfile, fileName)));
                    }
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        Thread.currentThread().sleep(5 * 24 * 60 * 60 * 1000);
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
        public String call() throws Exception {

            String number = fileName.substring(fileName.indexOf("-") + 1, fileName.lastIndexOf("."));
            String name = fileName.substring(0, fileName.lastIndexOf("-"));
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), "GBK");//考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                String url = lineTxt;
                num++;
                String filepath = "F:\\Audi\\html\\zhengshi\\" + name + "-" + number + ".html";
                File dir = new File("F:\\Audi\\html\\zhengshi\\" + name);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                File targetFile = new File(dir, name + "-" + number + ".html");
                String path = targetFile.getAbsolutePath();
                System.out.println(name+"-"+number+":"+url);
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
                    logger.error("网页出bug了" + name + "-" + number  + "问题是：" + url + e.getMessage());
                }
            }
            return null;
        }
    }

    @Test//出错的单独输出
    public void test2(){
        String s="https://audi.7zap.com/cat_scripts/parts_get.php?lang=cn&region=CA&modification_date=2010&modification_type=635&modification_name=A8&model_url=audi+a8&group_id=9&model_name=AudiA8&hg_ug=911&pnc=911020&vin=&_=160979";
        String filepath="F:\\Audi\\chucuo\\A8-158921.html";
        Document document0=null;
        try {
            s = s.replace(" ", "");
            document0 = Jsoup.parse(HttpServiceUtil.doGet(s));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Elements select = document0.select("body > div > img");
        String src = select.attr("src");
        String imgSrc="https://audi.7zap.com"+src;
        select.attr("src", imgSrc);
        saveHtml(filepath,document0.html());

    }


    @Test//解析html并导入到模板中
    public void importHtml() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int num = 0;
        int count = 0;
        int num22=0;
        Writer writer=null;
        //递归文件夹下的所有文件
        File fileFile = new File("F:\\Audi\\html\\zhengshi");
        File[] filelist = fileFile.listFiles();
        for (int n = 0; n < filelist.length; n++) {
            File readfile = filelist[n];
            File[] filelist2 =readfile.listFiles();
            String path=null;
            File compNameFile = new File("F:\\Audi\\txt\\"+filelist[n].getName()+".txt");
            if (!compNameFile.exists()) {
                compNameFile.createNewFile();
            }
            writer = new BufferedWriter(new FileWriter(compNameFile));
            for(int i = 0; i < filelist2.length; i++){
                stringBuilder = new StringBuilder("");
                File readfile2 = filelist2[i];
                num22++;
                System.out.println(num22+" - "+filelist2[i]);
                //继续读取文件
                String imgUrl=null;
                try {
                    //获取配件数据
                    Document document = Jsoup.parse(readfile2, "UTF-8");
                    num++;
                    Elements select = document.select("body > div.Ctree > table > tbody");
                    for (Element element : select) {
                        Elements select1 = element.select("tr[class='one_part']");
                        for (Element element1 : select1) {
                            String text = document.select("#breadcrumbs > li:nth-child(2) > a").text();
                            String[] split1 = text.split(" ");
                            String pinpai = split1[1];
                            stringBuilder.append("\"" + pinpai + "\"" + ",");
                            //System.out.println(pinpai);//获取品牌

                            String text4 = document.select("#main_title > small").text();
                            String[] split = text4.split(" ");
                            String niankuan = split[1].trim();
                            //System.out.println(split[1].trim());//年款
                            String chanyuandi = split[0].substring(1, split[0].length()).trim();
                            //System.out.println(chanyuandi);//产源地
                            stringBuilder.append("\"" + chanyuandi + "\"" + ",");

                            String chexi = document.select("#breadcrumbs > li:nth-child(5)").text();
                            //System.out.println(chexi);//获取车系名称
                            stringBuilder.append("\"" + chexi + "\"" + ",");
                            stringBuilder.append("\"" + niankuan + "\"" + ",");

                            String zhuzu = document.select("#breadcrumbs > li:nth-child(9) > a").text();
                            //System.out.println(zhuzu);//获取零部件主组
                            stringBuilder.append("\"" + zhuzu + "\"" + ",");

//                            document.select("tr[style='background-color:Silver;'");
                            String zizu = document.select("#breadcrumbs > li:nth-child(11)").text();//需修改
                            //System.out.println(zizu);//获取零部件子组
                            stringBuilder.append("\"" + zizu + "\"" + ",");


                            //获取配件图
                            Elements imgs = document.select("img[class='parts_image']");
                            for (Element img : imgs) {
                                String url = img.attr("src");
                                //System.out.println(url);
                                stringBuilder.append("\"" + url + "\"" + ",");
                            }
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
                                                String s = substring1.trim().replaceAll("\"", "").replaceAll(" ", "");
                                                //System.out.println(s);
                                                String[] split2 = s.split(",");
                                                StringBuilder stringBuilder1 = null;
                                                if (a == 0) {
                                                    count++;
                                                    String url = "https://audi.7zap.com/cat_scripts/get_analogs.php?lang=" + split2[0] + "&detail=" + split2[1] + "&imagelink=" + split2[2] + "&showpic=" + split2[3] + "&_=" + count;
                                                    //System.out.println("适用性：" + url);
                                                    stringBuilder1 = new StringBuilder();
                                                    stringBuilder1.append("\"" + url + "\"" + ";");
                                                    if (spans.size() == 1) {
                                                        stringBuilder.append("\"" + stringBuilder1 + "\"" + "," + "\r\n");
                                                    } else {
                                                        stringBuilder.append("\"" + stringBuilder1 + "\"" + ",");
                                                    }
                                                }
                                                if (a == 1) {
                                                    count++;
                                                    String url = "https://audi.7zap.com/cat_scripts/get_analogs.php?lang=" + split2[0] + "&detail=" + split2[1] + "&imagelink=" + split2[2] + "&showpic=" + split2[3] + "&_=" + count;
                                                    //System.out.println("类似物：" + url);
                                                    stringBuilder1 = new StringBuilder();
                                                    stringBuilder1.append(url + ";");
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
                                                        String url = "https://audi.7zap.com" + imgUrl;
                                                        //System.out.println("图片地址：" + imgUrl);
                                                        stringBuilder1 = new StringBuilder();
                                                        stringBuilder1.append(url + ";");
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
                    logger.error(filelist[n]+"里的"+filelist2[i]+"出bug了" + imgUrl+e.getMessage());
                }
            }
        }
        writer.close();
    }

    @Test
    public void downloadPic() throws IOException {
//        String url="https://volkswagen.7zap.com/images//Bilder/198/198035200.png";
        String filePath = "F:\\Audi\\7zap-奥迪汽车信息.txt";
        Map<String, Integer> imgMap = new HashMap<>();
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
                System.out.println(count++);
                String[] split = line.split("\",\"");
                String s = split[6];
                String s2 = s.replaceAll("//", "/");
                String[] split1 = s2.split("/");
                String one = split1[5];
                String two = split1[6];
                try {
                    if (imgMap.get(one + "-" + two) == null) {
                        byte[] data = HttpServiceUtil.downloadPic(s);
                        File dir = new File("F:\\Audi\\downPic\\" + one);
                        if (!dir.exists()) {
                            dir.mkdir();
                        }
                        File targetFile = new File(dir, two);
                        FileUtils.writeByteArrayToFile(targetFile, data);
                        imgMap.put(one + "-" + two, 1);
                    }
                } catch (Exception e) {
                    logger.error(line + "\t" + e.getMessage());
                    imgMap.put(one + "-" + two, 1);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

}



