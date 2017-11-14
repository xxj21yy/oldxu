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
 * Created by cc on 2017/8/3.
 */
public class TestPassatEuropa {
    private static Logger logger = LoggerFactory.getLogger(TestPassatEuropa.class);
    int count = 0;//相等
    int num=0;
    int downloadCnt = 0;
    @Test
    public void test() {


        int jia = 1;//-2
        String s8 = null;
        String fileName = null;
        StringBuilder stringBuilder = new StringBuilder();
        //ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);

        List<Future<String>> futList = new ArrayList<Future<String>>();
        stringBuilder.append("https://volkswagen.7zap.com/cn/rdw/");//Europa
        String s = "https://volkswagen.7zap.com/cat_scripts/models_get.php?lang=cn&region=RDW&_=" + jia;
        Document document = null;//连接url
        try {
            document = connectUrl(s);
        } catch (Exception e) {

            e.printStackTrace();
            File dir = new File("F:\\Dazhong\\Europa\\bugpath\\dangError.txt");
            try {
                FileUtils.write(dir, s8, "UTF-8");
            } catch (IOException e1) {
                logger.error("网址" + s8 + "出错了，问题是" + e1.getMessage());
            }
            logger.error("网址" + s8 + "出错了，问题是" + e.getMessage());
        }

        Elements select = document.select("div[class='table-responsive']");
//        Elements select3 = select.select("tr[onmouseout='this.classList.remove('model_select');']");
        Elements select3 = document.select("#collapse_41 > div > div > table > tbody > tr");
        Elements select4 = select3.select("tr[onmouseout='this.classList.remove('model_select');']");

        try {
            for (Element element66 : select4) {
                String onclick = element66.attr("onclick");
                String groupId = null;
                if (onclick != null && !"".equals(onclick)) {
                    String[] split = getValue(onclick);
                    jia++;
                    String s1 = "https://volkswagen.7zap.com/cat_scripts/part_groups_get.php?lang=cn&region=RDW&modification_date=" + split[3].trim() + "&modification_type=" + split[4].trim() + "&modification_name=" + split[2].trim() + "&model_name=" + split[0].trim() + "&vin=" + split[7].trim() + "&_=" + jia;
                    String s2 = s1.replaceAll("'", "");
                    Document document1 = connectUrl(s2);//连接url

                    Elements select1 = document1.select("div[class='group col-xs-20 col-sm-11 col-md-9']");
                    String s4 = null;
                    for (Element element : select1) {
                        String onclick1 = element.attr("onclick");
                        String[] split1 = getValue2(onclick1);
                        jia++;
                        String s3 = "https://volkswagen.7zap.com/cat_scripts/part_sub_groups_get.php?lang=cn&region=RDW&modification_date=" + split1[3].trim() + "&modification_type=" + split1[4].trim() + "&modification_name=" + split1[2].trim() + "&model_url=" + split1[1].trim() + "&group_id=" + split1[7].trim() + "&model_name=" + split1[0].trim() + "&vin=" + split1[8].trim() + "&_=" + jia;
                        s4 = s3.replaceAll("'", "").trim();
                        if (s4.contains(" ")) {
                            s4 = s4.replaceAll(" ", "%20");
                        }
                        groupId = split1[7].trim();
                        //最终页面的获取
                        Document document2 = connectUrl(s4);//连接url

                        Elements select2 = document2.select("body > div");
                        Elements select5 = select2.select("div[class='sub_group col-xs-48 col-sm-24 col-md-16']");
                        String substring3 = null;

                        String str = null;
                        for (Element element77 : select5) {
                            String onclick77 = element77.attr("onclick");
                            if (onclick77 != null && !"".equals(onclick77)) {
                                //String s5 = onclick77.trim();
                                String[] split7 = getValue2(onclick77);
                                fileName = split7[2];
                                //fileName = trim.substring(trim.indexOf("'") + 1, trim.lastIndexOf("'")).trim();
                                jia++;
                                s8 = "https://volkswagen.7zap.com/cat_scripts/parts_get.php?lang=cn&region=RDW&modification_date=" + split7[3].trim() + "&modification_type=" + split7[4].trim() + "&modification_name=" + split7[2].trim() + "&model_url=" + split7[1].trim() + "&group_id=" + groupId + "&model_name=" + split7[0].trim() + "&hg_ug=" + split7[8] + "&pnc=" + split7[9].trim() + "&vin=" + split7[10] + "&_=" + jia;
                                s8 = s8.replaceAll("'", "").trim();
                                System.out.println(fileName + " - " + s8);
                                //futList.add(fixedThreadPool.submit(new Parser(s8, fileName)));
                                //生成url的txt
                                count++;
                                File dir = new File("F:\\Dazhong\\Europa\\path\\" + fileName + "\\" + fileName + "-" + count + ".txt");
                                FileUtils.write(dir, s8, "UTF-8");
                            }

                        }
                    }
                }

            }
        } catch (Exception e) {

            e.printStackTrace();
            File dir = new File("F:\\Dazhong\\Europa\\bugpath\\" + fileName + " - " + count + ".txt");
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
        String substring = onclick.substring(onclick.indexOf("(") + 1, onclick.length()-1);
        String[] split = substring.split("','");
        String substring1 = null;
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            if (s.contains("'")) {
                substring1 = s.substring(s.indexOf("'") + 1, s.length()).trim();
            } else {
                substring1 = s.trim();
            }
//            substring1 = substring1.replace("20%25", "+");
//            substring1 = substring1.replace(" ", "20%");
            split[i]=substring1;
        }
        return split;
    }
    //整合取值onclick方法
    public static String[] getValue2(String onclick) {
        String substring = onclick.substring(onclick.indexOf("(") + 1, onclick.length()-1);
        String s2 = substring.replaceAll(",", "','");
        //String replace = s2.replaceAll("','", ",");
        //String replace2 = replace.replaceFirst("','", ",");
        String[] split = s2.split("','");
        String substring1 = null;
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            if (s.contains("'")) {
                substring1 = s.substring(s.indexOf("'") + 1, s.length()).trim();
            } else {
                substring1 = s.trim();
            }
//            substring1 = substring1.replace("20%25", "+");
//            substring1 = substring1.replace(" ", "20%");
            split[i]=substring1.replaceAll("'","").trim();
        }
        return split;
    }

    //HttpServiceUtil连接网址
    public static Document connectUrl(String url) throws Exception {
        Document document = null;
        int cnt = 0;
        cnt++;
        if (url.contains(" ")) {
            url = url.replaceAll(" ", "");
        }
        document = Jsoup.parse(HttpServiceUtil.doGet(url));
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

    class Parser implements Callable<String> {
        private String s;
        private String fileName;

        public Parser(String s, String fileName) {
            this.s = s;
            this.fileName = fileName;
        }

        @Override
        public String call() throws Exception {

            //生成html
            try {
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

    @Test//生成html
    public void html() throws InterruptedException {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(20);
        List<Future<String>> futList = new ArrayList<Future<String>>();
        try {
            //生成html
            File file = new File("F:\\Dazhong\\Europa\\path\\TRSY");
            String[] files = file.list();
            for (int i = 0; i < files.length; i++) {
                File readfile = new File("F:\\Dazhong\\Europa\\path\\TRSY\\" + files[i]);
                String fileName = files[i];
                futList.add(fixedThreadPool.submit(new TestPassatEuropa.Parser2(readfile, fileName)));
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
                String filepath = "F:\\Dazhong\\Europa\\html\\" + name + "-" + number + ".html";
                File dir = new File("F:\\Dazhong\\Europa\\html\\" + name);
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
                    File dir2 = new File("F:\\Dazhong\\Europa\\bugHtml\\"+name+"-"+number+".txt");
                    FileUtils.write(dir2,url,"UTF-8");
                    logger.error(fileName+" - "+count+"未写入，原因是："+e.getMessage());
                }
            }
            return null;
        }
    }


    @Test//出错的单独输出
    public void test2(){
        String s="https://volkswagen.7zap.com/cat_scripts/parts_get.php?lang=cn&region=RDW&modification_date=2016&modification_type=425&modification_name=IMD&model_url=diesel-industrie-motore&group_id=1&model_name=Diesel-Industrie-Motore&hg_ug=145&pnc=145035&vin=&_=7586";
        String filepath="F:\\Dazhong\\Europa\\chucuo\\IMD-7249.html";
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
        File fileFile = new File("F:\\Dazhong\\Europa\\html");
        File[] filelist = fileFile.listFiles();
        for (int n = 0; n < filelist.length; n++) {
            File readfile = filelist[n];
            File[] filelist2 =readfile.listFiles();
            String path=null;
            File compNameFile = new File("F:\\Dazhong\\Europa\\ceshi\\"+filelist[n].getName()+".txt");
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
                                String s = url.replace("/ImgsWatermark", "");
                                stringBuilder.append("\"" + s + "\"" + ",");
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
                                                    String url = "https://volkswagen.7zap.com/cat_scripts/get_analogs.php??lang=" + split2[0] + "&detail=" + split2[1] + "&imagelink=" + split2[2] + "&showpic=" + split2[3] + "&_=" + count;
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
                                                    String url = "https://volkswagen.7zap.com/cat_scripts/get_analogs.php?lang=" + split2[0] + "&detail=" + split2[1] + "&imagelink=" + split2[2] + "&showpic=" + split2[3] + "&_=" + count;
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
                    File dir2 = new File("F:\\Dazhong\\Europa\\bugdata\\"+filelist[n].getName()+".txt");
                    FileUtils.write(dir2,filelist2[i]+"出bug了" + imgUrl,"UTF-8");
                    logger.error(filelist[n]+"里的"+filelist2[i]+"出bug了" + imgUrl+e.getMessage());
                }
            }
        }
        writer.close();
    }

    //下载图片
    @Test
    public void downloadPic() {
        ExecutorService fixedThreadPool2 = Executors.newFixedThreadPool(5);
        Queue<Future<String>> futList = new ConcurrentLinkedQueue<Future<String>>();
        String filePath = "F:\\Dazhong\\Europa\\VolkswagenEuropaData.txt";
        Map<String, Integer> imgMap = new ConcurrentHashMap<>();
        File imgDir = new File("F:\\Dazhong\\Europa\\downloadPic");
        for (File subDir : imgDir.listFiles()) {
            for (File file : subDir.listFiles()) {
                imgMap.put(subDir.getName() + "-" + file.getName(), 1);
            }
        }
        File file = new File(filePath);
        String one=null;
        String two=null;
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
                System.out.println(count++);
                try {
                    if (imgMap.get(one + "-" + two) == null) {
                        futList.add(fixedThreadPool2.submit(new TestPassatEuropa.Parser3(one, two)));
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
        try {
            Thread.currentThread().sleep(5 * 24 * 60 * 60 * 1000);
        } catch (InterruptedException e) {
            File dir2 = new File("F:\\Dazhong\\Europa\\bugPic\\"+one+" - "+two+".txt");
            try {
                FileUtils.write(dir2,one+" - "+two,"UTF-8");
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

        public Parser3( String one, String two) {

            this.one = one;
            this.two = two;
        }

        @Override
        public String call() {
            try{
                System.out.println("在下载"+downloadCnt++);
                byte[] data = HttpServiceUtil.downloadPic("https://audi.7zap.com/images//Bilder/"+one+"/"+two);
                File dir = new File("F:\\Dazhong\\Europa\\downloadPic\\" + one);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                File targetFile = new File(dir, two);
                FileUtils.writeByteArrayToFile(targetFile, data);
            }catch (Exception e){
                File dir2 = new File("F:\\Dazhong\\Europa\\bugPic\\"+one+" - "+two+".txt");
                try {
                    FileUtils.write(dir2,one+" - "+two,"UTF-8");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                logger.error(e.getMessage());
            }
            return null;
        }
    }

}



