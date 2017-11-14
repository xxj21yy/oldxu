package com.example.citroen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.example.collectdata.FileUtil;
import com.example.demo.util.HttpServiceUtil;

/**
 * Created by Administrator on 2017/9/11.
 */
public class Test01 {
	static AtomicInteger count = new AtomicInteger(1);
    @Test
    public void test01() throws Exception {
    	ExecutorService fixedThreadPool = Executors.newFixedThreadPool(100);
		CompletionService<String> execcomp = new ExecutorCompletionService<String>(
				fixedThreadPool);
        HttpClientContext context = HttpClientContext.create();

//        List<String> list = FileUtil.getLineFromFile(new File("E:\\data\\Citroen\\carLink400.txt"));
        List<String> list = FileUtil.getLineFromFile(new File("E:\\data\\Exception\\Citroen\\errorUrl100.txt"));
        Map<String,String> map  = new HashMap<String,String>();
        for (String string : list) {
			map.put(string, "1");
		}
        for (String s : map.keySet()) {
        	context = HttpClientContext.create();
			execcomp.submit(new ParserHtml(context, s));
//        	buildData(context,s);
		}

        try {
			Thread.currentThread().sleep(5 * 24 * 60 * 60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		fixedThreadPool.shutdown();
    }
   
    class ParserHtml implements Callable<String> {
    	private HttpClientContext context ;
		private String s;
		public ParserHtml(HttpClientContext context, String s) {
			this.context = context;
			this.s = s;
		}

		@Override
		public String call() {
				try {
					System.out.println("正在解析车型"+count.getAndIncrement()+"");
					buildData(context, s);
				} catch (Exception e) {
					
				}
			return null;
		}
		
	}
    
    private void buildData(HttpClientContext context, String s){
    	try {
			//请求登录页，获取window值
            String cnt = HttpUtil.doGet("http://service.citroen.com/socle/?start=true",context);
            Document doc = Jsoup.parse(cnt);
            String hidenVal = doc.select("#formul > form > input[type=\"hidden\"]").val();
            //登录
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("userid", "AC59853388"));
            formparams.add(new BasicNameValuePair("password", "123456"));
            formparams.add(new BasicNameValuePair(" window", hidenVal));
            String result = HttpUtil.doPost("http://service.citroen.com/do/login",context,formparams);
        	
			String link = s.substring(s.indexOf("?")+1);
			String []param = link.split("&");
			String vehCom = param[1].split("=")[1];
			String cat = param[0].split("=")[1];
			String thrParam = URLDecoder.decode(param[2]).substring(URLDecoder.decode(param[2]).indexOf("=")+1);
		    formparams = new ArrayList<NameValuePair>();
		    formparams.add(new BasicNameValuePair("vehCom", vehCom));
		    formparams.add(new BasicNameValuePair("cat", cat));
		    formparams.add(new BasicNameValuePair(" lcdv", thrParam));
		    HttpUtil.doPost("http://service.citroen.com/docapv/ajaxRequestGetVehCom.do",context,formparams);
		    result = HttpUtil.doGet(s,context);
		    Document document = Jsoup.parse(result);
		    Elements ele = document.select("ul[class='aside'] > li").eq(1).select("ul > li >a");
		    for (int i = 1;i< ele.size() - 1;i++) {
				String param1 = ele.get(i).attr("href");
				String param2 = param1.substring(param1.indexOf("(")+1,param1.indexOf(")"));
				String param3 = param2.split(",")[0].replaceAll("\'", "");
				String url1 = "http://service.citroen.com/docapv/fonction.do?idFction="+param3;
				result = HttpUtil.doGet(url1,context);
				Document document1 = Jsoup.parse(result);
				parseHtml(formparams,context,document1,s);
			}
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    private  void  parseHtml(List<NameValuePair> formparams,HttpClientContext context,Document document,String s) throws Exception{
        	String title = document.select("span[id='infosVehicule']").text();
        	if(title.indexOf("/")>0){
        		title = title.replaceAll("/", "&");
    		}
        	File dir = new File("E:\\data\\Citroen\\txt1\\"+title);
        	if(!dir.exists()){
        		dir.mkdirs();
        	}
        	List<DataJson> dataList = getJson(document);
    		for (DataJson dataJson : dataList) {
    			//二级目录
    			List<DataJson> list = dataJson.getFctionsDependantes();
    			String pathtemp = dataJson.getLibelleFct();
    			if(pathtemp.indexOf("/")>0){
    				pathtemp = pathtemp.replaceAll("/", "&");
    			}
    			String path = dir.getAbsolutePath()+"\\"+pathtemp;
    			File sec = new File(path);
    			if(!sec.exists()){
    				sec.mkdir();
    			}
    			int j = 0;
    			for (DataJson dataJson2 : list) {
//    				System.err.println("正在解析"+title+"下第"+k+"个1级目录，第"+(++j)+"个二级目录");
    				if(!dataJson2.isGrisee()){
    					String abs = dataJson2.getLibelleFct();
    					if(abs.indexOf("/") > 0){
    						abs = abs.replaceAll("/", "&");
    					}
    					String thrPath = sec.getAbsolutePath()+"\\"+abs;
    					File thrFile = new File(thrPath);
    					if(!thrFile.exists()){
    						thrFile.mkdir();
    					}
    					System.out.println(abs);
    					String idFct = dataJson.getId(); 
    					try {
    						File [] file = thrFile.listFiles();
    						if(null !=file && file.length > 0){
    							continue;
    						}
    						List<CarData> listPro = getCarData(dataJson,dataJson2,formparams,context,thrFile,s);
    						
        					int i = 0;
        				    for (CarData carData : listPro) {
        				    	System.out.println("下载第"+(++i)+"个html");
        				    	File htmlFile = new File (thrFile.getAbsolutePath(),carData.getReference()+".html");
        				    	if(htmlFile.exists()){
        				    		continue;
        				    	}
        				    	String url = "";
    	    				    try{
    	    				    	String refaff = URLEncoder.encode(carData.getReferenceAffichable());
    	    				    	url = "http://service.citroen.com/docapv/affiche.do?ref="+carData.getReference()+"&refaff="+refaff+"&idFct="+idFct;
    	    				    	String result = HttpUtil.doGet(url,context);
    	    				    	Document doc = Jsoup.parse(result);
    	    				        FileUtil.saveHtml(htmlFile.getAbsolutePath(), doc.html());
    	    				    } catch (Exception e) {
    	    						// TODO: handle exception
    	    						e.printStackTrace();
    	    						File dir2 = new File("E:\\data\\Exception\\Citroen\\errorUrl2.txt");
    	    						FileUtils.write(dir2, url, "UTF-8");
    	    						continue;
    	    					}
        				    	
    						}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
    					
    				}
    			}
    		}
    		
    }
    
    private  void  downLoadPic(List<NameValuePair> formparams,HttpClientContext context,Document document,String s) throws Exception{
    	List<DataJson> dataList = getJson(document);
		for (DataJson dataJson : dataList) {
			List<DataJson> list = dataJson.getFctionsDependantes();
			for (DataJson dataJson2 : list) {
				if(!dataJson2.isGrisee()){
					String idFct = dataJson.getId(); 
					try {
						List<CarData> listPro = getCarData(dataJson,dataJson2,formparams,context,s);
    				    for (CarData carData : listPro) {
    				    	String url = "";
	    				    try{
	    				    	System.out.println("正在下载图片.....");
	    				    	String refaff = URLEncoder.encode(carData.getReferenceAffichable());
	    				    	url = "http://service.citroen.com/docapv/affiche.do?ref="+carData.getReference()+"&refaff="+refaff+"&idFct="+idFct;
	    				    	String result = HttpUtil.doGet(url,context);
	    				    	Document doc = Jsoup.parse(result);
	    				    	Elements selectImg = doc.select("table.tableimage > tbody > tr > td > div >div>img");
	    						String src = "http://service.citroen.com/docapv/"+selectImg.attr("src");
	    						String src1 = src;
	    			            String path = src1.replace("//", "/");
	    			            String []paths = path.split("/");
	    			            String one = paths[8];
	    			            String two = paths[9];
	    			            String name = paths[10];
	    			            File dir = new File("E:\\data\\CitroenPic\\"+one+"\\"+two);
	    			            if (!dir.exists()) {
	    			                 dir.mkdir();
	    			            }
	    			            File targetFile = new File(dir, name);
	    			            if(targetFile.exists()){
	    			            	continue;
	    			            }
	    			        	System.out.println("正在下载图片.....");
	    			            byte[] data = HttpServiceUtil.downloadPic(src,context);
	    			            FileUtils.writeByteArrayToFile(targetFile, data);
	    				    } catch (Exception e) {
	    						// TODO: handle exception
	    						e.printStackTrace();
	    						File targetFile = new File("E:\\data\\Exception\\Citroen\\errorUrl2-400-1.txt");

	    						Writer writer = new BufferedWriter(new FileWriter(targetFile,
	    								true));
	    						writer.write(url+"\r\n");
	    						writer.flush();
	    						continue;
	    					}
    				    	
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
				}
			}
		}
		
}
    public static void main(String[] args) throws Exception{
//    	Document document = Jsoup.parse(new File("E:\\data\\Citroen\\Html\\1.html"), "UTF-8");
//    	System.out.println(document.select("span[id='infosVehicule']").text());;
//    	Elements e = document.getElementsByTag("script");
//    	for (Element element : e) {
//			if(element.data().contains("setFctNiv1Select")){
//				String data = element.data().substring(element.data().indexOf("setFctNiv1Select"));
//				String setFctNiv1Select = data.substring(0,data.indexOf(";"));
//				String text = setFctNiv1Select.substring(setFctNiv1Select.indexOf("(")+1);
//				String json = text.substring(0,text.lastIndexOf(")"));
//				JSONObject o = JSONObject.parseObject(json);
//				List<DataJson> list = JSONObject.parseArray(o.getString("fctionsDependantes"), DataJson.class);
//				System.out.println(list);
//				break;
//			}
//		}
//    	String thrPath = "E:\\data\\Citroen\\txt\\C2 3门轿车 1.1 i (TU1JP)\\制动\\ABS";
//    	new File(thrPath).mkdir();
    	List<File> fileList = new ArrayList<File>();
    	fileList = FileUtil.fileReader(new File("E:\\data\\Citroen\\txt"), fileList);
    	for (File file : fileList) {
    		String path = file.getParent();
    		String [] paths = path.split("\\\\");
    		System.out.println(file.getName());
			String pathtemp = "";
			Pattern pattern = Pattern.compile("(?<=txt)+.*"); 
			Matcher m = pattern.matcher(path);
			if(m.find()){
				pathtemp = m.group();
			}
			try {
				File dir = new File("e:\\data\\CitroenTxt\\" + pathtemp);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				File targetFile = new File(dir, dir.getName() + ".txt");
				System.out.println(targetFile.getAbsolutePath());
//				addQueue(targetFile,file,pathtemp);
			} catch (Exception e) {
				File dir2 = new File("e:\\data\\Exception\\CitroenTxt\\"
						+ pathtemp + "\\" + file.getName() + ".txt");
				try {
					FileUtils.write(dir2, pathtemp+"-"+file.getName(), "UTF-8");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
		}
	}
    
    private static List<DataJson> getJson(Document document){
    	Elements e = document.getElementsByTag("script");
    	for (Element element : e) {
			if(element.data().contains("setFctNiv1Select")){
				String data = element.data().substring(element.data().indexOf("setFctNiv1Select"));
				String setFctNiv1Select = data.substring(0,data.indexOf(";"));
				String text = setFctNiv1Select.substring(setFctNiv1Select.indexOf("(")+1);
				String json = text.substring(0,text.lastIndexOf(")"));
				JSONObject o = JSONObject.parseObject(json);
				List<DataJson> list = JSONObject.parseArray(o.getString("fctionsDependantes"), DataJson.class);
				return list;
			}
    	}
    	return null;
    }
    
    private static  List<CarData> getCarData(DataJson  dataJson,DataJson dataJson2,List<NameValuePair> formparams,HttpClientContext context,File file,String url) {
    	Writer writer = null;
    	System.out.println("请求json");
    	List<CarData> listPro = new ArrayList<CarData>();
    	try {
    		//第一步请求
    		String idFctParent = dataJson.getId();
    		String idFct = dataJson2.getId();
    		formparams = new ArrayList<NameValuePair>();
    		formparams.add(new BasicNameValuePair("idFct", idFct));
    	    formparams.add(new BasicNameValuePair("idFctParent", idFctParent));
    	    String result = HttpUtil.doPost("http://service.citroen.com/docapv/AjaxTabRecapDoc.do",context,formparams);
    	    //第二步请求
    	    formparams = new ArrayList<NameValuePair>();
    	    formparams.add(new BasicNameValuePair("typDoc", ""));
    	    result = HttpUtil.doPost("http://service.citroen.com/docapv/AjaxListeDoc.do",context,formparams);
    	    //json解析
    	    JSONObject json = JSONObject.parseObject(result);
    	    result = json.getJSONObject("listDocPourAffichage").getString("listeDesDocuments");
    	    
    	    //属性集合
    	    listPro = JSONObject.parseArray(result, CarData.class);
        	return listPro;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.err.println("异常了");
			try {
				File targetFile = new File("E:\\data\\Exception\\Citroen\\errorUrl100-1.txt");

				writer = new BufferedWriter(new FileWriter(targetFile,
						true));
				
				writer.write(url+"\r\n");
				writer.flush();
			} catch (Exception e2) {
				// TODO: handle exception
				e.printStackTrace();
			}
				
			return listPro;
		}
    	
    }
    
    private static  List<CarData> getCarData(DataJson  dataJson,DataJson dataJson2,List<NameValuePair> formparams,HttpClientContext context,String url) {
    	Writer writer = null;
    	List<CarData> listPro = new ArrayList<CarData>();
    	try {
    		//第一步请求
    		String idFctParent = dataJson.getId();
    		String idFct = dataJson2.getId();
    		formparams = new ArrayList<NameValuePair>();
    		formparams.add(new BasicNameValuePair("idFct", idFct));
    	    formparams.add(new BasicNameValuePair("idFctParent", idFctParent));
    	    String result = HttpUtil.doPost("http://service.citroen.com/docapv/AjaxTabRecapDoc.do",context,formparams);
    	    //第二步请求
    	    formparams = new ArrayList<NameValuePair>();
    	    formparams.add(new BasicNameValuePair("typDoc", ""));
    	    result = HttpUtil.doPost("http://service.citroen.com/docapv/AjaxListeDoc.do",context,formparams);
    	    //json解析
    	    JSONObject json = JSONObject.parseObject(result);
    	    result = json.getJSONObject("listDocPourAffichage").getString("listeDesDocuments");
    	    
    	    //属性集合
    	    listPro = JSONObject.parseArray(result, CarData.class);
        	return listPro;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			try {
				File targetFile = new File("E:\\data\\Exception\\Citroen\\errorUrl400-1.txt");

				writer = new BufferedWriter(new FileWriter(targetFile,
						true));
				
				writer.write(url+"\r\n");
				writer.flush();
			} catch (Exception e2) {
				// TODO: handle exception
				e.printStackTrace();
			}
				
			return listPro;
		}
    	
    }
}
