package com.example.citroen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.collectdata.FileUtil;
import com.example.demo.util.HttpServiceUtil;

/**
 * Created by Administrator on 2017/9/11.
 */
public class Peogeot {
	static AtomicInteger count = new AtomicInteger(1);
    @Test
    public void peogeot() throws Exception {
    	
    	ExecutorService fixedThreadPool = Executors.newFixedThreadPool(388);
		CompletionService<String> execcomp = new ExecutorCompletionService<String>(
				fixedThreadPool);
    	HttpClientContext context = HttpClientContext.create();
    	List<String> list = FileUtil.getLineFromFile(new File("E:\\data\\Exception\\Peugeot\\errorUrl0925.txt"));
        Map<String,String> map  = new HashMap<String,String>();
        for (String string : list) {
  			map.put(string, "1");
  		}
        System.out.println(map.size());
        for (String s : map.keySet()) {
        	context = HttpClientContext.create();
//        	buildData(context,s);
        	execcomp.submit(new buildDataThread(context, s));
		}
    	try {
  			Thread.currentThread().sleep(5 * 24 * 60 * 60 * 1000);
  		} catch (InterruptedException e) {
  			e.printStackTrace();
  		}
//          System.out.println(document.html());
    }
    class buildDataThread implements Callable<String>{
    	private HttpClientContext context ;
		private String s;
		public buildDataThread(HttpClientContext context, String s) {
			this.context = context;
			this.s = s;
		}
		@Override
		public String call() throws Exception {
			// TODO Auto-generated method stub
			System.out.println("正在解析车型"+count.getAndIncrement()+"");
			buildData(context, s);
			return null;
		}
    	
    }
    
    private static void buildData (HttpClientContext context, String s){
    	try {
    		String cnt = HttpUtil.doGet("http://public.servicebox.peugeot.com/socle/?start=true",context);
            Document doc = Jsoup.parse(cnt);
            String hidenVal = doc.select("#formul > form > input[type=\"hidden\"]").val();
            //登录
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("userid", "AP07257699"));
            formparams.add(new BasicNameValuePair("password", "6nKNKp5J"));
            formparams.add(new BasicNameValuePair(" window", hidenVal));
            String result = HttpUtil.doPost("http://public.servicebox.peugeot.com/do/login",context,formparams);
            
//            String url = s.substring(0,s.indexOf("lcdv")+5);
//            String link = s.substring(s.indexOf("?")+1);
//			String []param = link.split("&");
//			String vehCom = param[1].split("=")[1];
//			String cat = param[0].split("=")[1];
//			String path = param[2]+"&"+param[3];
//			String thrParam = path.substring(path.indexOf("=")+1);
            
            String link = s.substring(s.indexOf("?")+1);
			String []param = link.split("&");
			String vehCom = param[1].split("=")[1];
			String cat = param[0].split("=")[1];
			String thrParam = URLDecoder.decode(param[2]).substring(URLDecoder.decode(param[2]).indexOf("=")+1);
			
			formparams = new ArrayList<NameValuePair>();
			formparams.add(new BasicNameValuePair("vehCom", vehCom));
			formparams.add(new BasicNameValuePair("cat", cat));
			formparams.add(new BasicNameValuePair(" lcdv", thrParam));
			HttpUtil.doPost("http://public.servicebox.peugeot.com/docapv/ajaxRequestGetVehCom.do",context,formparams);
			String thrParam1 = URLEncoder.encode(thrParam);
//			result = HttpUtil.doGet(url+thrParam1,context);
			result = HttpUtil.doGet(s,context);
		    Document document = Jsoup.parse(result);
		    Elements ele = document.select("ul[class='aside'] > li").eq(1).select("ul > li >a");
		    for (int i = 0;i< ele.size() - 1;i++) {
				String param1 = ele.get(i).attr("href");
				String param2 = param1.substring(param1.indexOf("(")+1,param1.indexOf(")"));
				String param3 = param2.split(",")[0].replaceAll("\'", "");
				String url1 = "http://public.servicebox.peugeot.com/docapv/fonction.do?idFction="+param3;
				result = HttpUtil.doGet(url1,context);
				Document document1 = Jsoup.parse(result);
//				parseHtml(formparams,context,document1,url+thrParam1);
//				parseHtml(formparams,context,document1,s);
//				downLoadPic(formparams,context,document1,url+thrParam1);
				downLoadPic(formparams,context,document1,s);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    	
    }  
    
    private static  void  parseHtml(List<NameValuePair> formparams,HttpClientContext context,Document document,String s) throws Exception{
    	String title = document.select("span[id='infosVehicule']").text();
    	if(title.indexOf("/")>0){
    		title = title.replaceAll("/", "");
		}
    	File dir = new File("E:\\data\\Peugeot\\Html\\"+title);
    	if(!dir.exists()){
    		dir.mkdirs();
    	}
    	List<DataJson> dataList = getJson(document);
		for (DataJson dataJson : dataList) {
			//二级目录
			List<DataJson> list = dataJson.getFctionsDependantes();
			String pathtemp = dataJson.getLibelleFct();
			if(pathtemp.indexOf("/")>0){
				pathtemp = pathtemp.replaceAll("/", "");
			}
			String path = dir.getAbsolutePath()+"\\"+pathtemp;
			File sec = new File(path);
			if(!sec.exists()){
				sec.mkdir();
			}
			for (DataJson dataJson2 : list) {
//				System.err.println("正在解析"+title+"下第"+k+"个1级目录，第"+(++j)+"个二级目录");
				if(!dataJson2.isGrisee()){
					String abs = dataJson2.getLibelleFct();
					if(abs.indexOf("/") > 0){
						abs = abs.replaceAll("/", "");
					}
					System.out.println(abs);
					String thrPath = sec.getAbsolutePath()+"\\"+abs;
					File thrFile = new File(thrPath);
					if(!thrFile.exists()){
						thrFile.mkdir();
					}
					String idFct = dataJson.getId(); 
					try {
						File [] file = thrFile.listFiles();
						if(null !=file && file.length > 0){
//							System.out.println("已经下载过了");
							continue;
						}
						List<CarData> listPro = getCarData(dataJson,dataJson2,formparams,context,thrFile,s);
						
    					int i = 0;
    				    for (CarData carData : listPro) {
    				    	File htmlFile = new File (thrFile.getAbsolutePath(),carData.getReference()+".html");
    				    	if(htmlFile.exists()){
    				    		continue;
    				    	}
    				    	System.out.println("下载第"+(++i)+"个html");
    				    	String url = "";
	    				    try{
	    				    	String refaff = URLEncoder.encode(carData.getReferenceAffichable());
	    				    	url = "http://public.servicebox.peugeot.com/docapv/affiche.do?ref="+carData.getReference()+"&refaff="+refaff+"&idFct="+idFct;
	    				    	String result = HttpUtil.doGet(url,context);
	    				    	Document doc = Jsoup.parse(result);
	    				        FileUtil.saveHtml(htmlFile.getAbsolutePath(), doc.html());
	    				    } catch (Exception e) {
	    						// TODO: handle exception
	    						e.printStackTrace();
	    						File dir2 = new File("E:\\data\\Exception\\Peugeot\\errorUrl20170925.txt");
	    						FileUtil.writeFile(dir2, url);
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
    private static void  downLoadPic(List<NameValuePair> formparams,HttpClientContext context,Document document,String s) throws Exception{
    	List<DataJson> dataList = getJson(document);
		for (DataJson dataJson : dataList) {
			List<DataJson> list = dataJson.getFctionsDependantes();
			for (DataJson dataJson2 : list) {
				if(!dataJson2.isGrisee()){
					String idFct = dataJson.getId(); 
					try {
						List<CarData> listPro = getCarData(dataJson,dataJson2,formparams,context,null,s);
    				    for (CarData carData : listPro) {
    				    	String url = "";
	    				    try{
	    				    	System.out.println("正在下载图片.....");
	    				    	String refaff = URLEncoder.encode(carData.getReferenceAffichable());
	    				    	url = "http://public.servicebox.peugeot.com/docapv/affiche.do?ref="+carData.getReference()+"&refaff="+refaff+"&idFct="+idFct;
	    				    	String result = HttpUtil.doGet(url,context);
	    				    	Document doc = Jsoup.parse(result);
	    				    	Elements selectImg = doc.select("table.tableimage > tbody > tr > td > div >div>img");
	    						String src = "http://public.servicebox.peugeot.com/docapv/"+selectImg.attr("src");
	    						String src1 = src;
	    			            String path = src1.replace("//", "/");
	    			            String []paths = path.split("/");
	    			            String one = paths[8];
	    			            String two = paths[9];
	    			            String name = paths[10];
	    			            File dir = new File("E:\\data\\PeugeotPic\\"+one+"\\"+two);
	    			            if (!dir.exists()) {
	    			                 dir.mkdir();
	    			            }
	    			            File targetFile = new File(dir, name);
	    			            if(targetFile.exists()){
	    			            	continue;
	    			            }
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
    	    String result = HttpUtil.doPost("http://public.servicebox.peugeot.com/docapv/AjaxTabRecapDoc.do",context,formparams);
    	    //第二步请求
    	    formparams = new ArrayList<NameValuePair>();
    	    formparams.add(new BasicNameValuePair("typDoc", ""));
    	    result = HttpUtil.doPost("http://public.servicebox.peugeot.com/docapv/AjaxListeDoc.do",context,formparams);
    	    System.out.println(result);
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
				File targetFile = new File("E:\\data\\Exception\\Peugeot\\errorUrl0925-1.txt");

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
    private static List<PeogeotData> get(Document document){
    	List<PeogeotData> list  =new ArrayList<PeogeotData>();
    	Elements el = document.select("table[class='vehiculeSelection'] > tbody > tr ").eq(1);
    	for(int i = 0;i<el.size();i++){
    		Element td = el.get(i);
    		Elements childTd = td.select("ul > li");
    		for (Element element : childTd) {
				 Elements a = element.select("ul > li");
				 if(null == a || a.size() ==0 ){
					 a = element.select("a");
				 }
				 for (Element ael : a) {
				    if(!"".equals(ael.attr("onclick"))){
				    	String onclick = ael.attr("onclick");
				    	String js = onclick.substring(0,onclick.indexOf(";"));
				    	String param = js.substring(js.indexOf("(")+1,js.indexOf(")"));
				    	PeogeotData data = new PeogeotData();
				    	data.setCat(param.split(",")[1].replaceAll("\'", ""));
				    	data.setVehCom(param.split(",")[0].replaceAll("\'", ""));
				    	list.add(data);
				    }
				}
			}
    	}
    	return list;
    }
    
    private static void collectUrl(List<NameValuePair> formparams,List<PeogeotData> list,HttpClientContext context) throws Exception{
    	String result = "";
    	 for (PeogeotData peogeotData : list) {
       	  	 formparams = new ArrayList<NameValuePair>();
             formparams.add(new BasicNameValuePair("vehCom", peogeotData.getVehCom()));
             formparams.add(new BasicNameValuePair("cat", peogeotData.getCat()));
             formparams.add(new BasicNameValuePair("lcdv", ""));
             result =  HttpUtil.doPost("http://public.servicebox.peugeot.com/docapv/ajaxRequestGetVehCom.do", context, formparams);
             String json = result.substring(result.indexOf("{"),result.lastIndexOf("}")+1);
             JSONObject o = JSONObject.parseObject(json);
             json = o.getString("lcdvRequis");
             JSONArray array = JSONObject.parseArray(json);
             JSONObject object = (JSONObject) array.get(0);
             List<JsData> dataList = JSONObject.parseArray(object.getString("valeurs"),JsData.class);
             for (JsData jsData : dataList) {
	           	    String bod = jsData.getVal();
	           	     String param = "B0D="+bod+"&B0F=";
	           	     formparams = new ArrayList<NameValuePair>();
	                 formparams.add(new BasicNameValuePair("vehCom", peogeotData.getVehCom()));
	                 formparams.add(new BasicNameValuePair("cat", peogeotData.getCat()));
	                 formparams.add(new BasicNameValuePair("lcdv", param));
	                 result =  HttpUtil.doPost("http://public.servicebox.peugeot.com/docapv/ajaxRequestGetVehCom.do", context, formparams);
	                 String json1 = result.substring(result.indexOf("{"),result.lastIndexOf("}")+1);
	                 JSONObject o1 = JSONObject.parseObject(json1);
	                 json = o1.getString("lcdvRequis");
	                 JSONArray array1 = JSONObject.parseArray(json);
	                 JSONObject object1 = (JSONObject) array1.get(1);
	                 List<JsData> dataList1 = JSONObject.parseArray(object1.getString("valeurs"),JsData.class);
	                 for (JsData jsData2 : dataList1) {
		               	  System.out.println("正在收集url");
		               	  String s = param+jsData2.getVal();
		               	  String url = "http://public.servicebox.peugeot.com/docapv/vehCom.do?cat="+peogeotData.getCat()+"&vehCom="+peogeotData.getVehCom()+"&lcdv="+s;
		               	  File targetFile = new File("E:\\data\\Peugeot\\carLink.txt");
		                  FileUtil.writeFile(targetFile, url);
					}
			}
    }
    }
    public static void main(String[] args) throws Exception{
    	File fileData = new File("E:\\data\\CitroenTxt1");
		List<File> fileNameList = new ArrayList<File>();
		List<File> list = FileUtil.fileReader(fileData, fileNameList);
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0;i<list.size(); i++) {
			List<String> a = FileUtil.getLineFromFile(list.get(i));
			System.out.println("正在解析第"+(i+1)+"个文件");
			for (String s : a) {
	  			String path = s.split(",")[11];
	  			if(!"".equals(path) && path.indexOf("pr") > 0){
	  				Pattern pattern = Pattern.compile("(?<=pr)+.*");
	  				Matcher m = pattern.matcher(path);
	  				String pathtemp = "";
	  				if(m.find()){
	  					pathtemp = m.group();
	  				}
	  				pathtemp = pathtemp.substring(0,pathtemp.length() - 1);
	  				map.put(pathtemp, list.get(i).getAbsolutePath());
	  			}
	  			
	  		}
		}
		for (String  s : map.keySet()) {
			String path = "http://service.citroen.com/docapv/resources/4.24.8/AC/img/pr"+s;
			File targetFile = new File("E:\\data\\Exception\\CitroenTxt\\picUrl.txt");
            FileUtil.writeFile(targetFile, path);
		}
		
	}
    @Test
    public void downloadpic(){
    	ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
		CompletionService<String> execcomp = new ExecutorCompletionService<String>(
				fixedThreadPool);
		List<String> a = FileUtil.getLineFromFile(new File("e:\\data\\Exception\\CitroenTxt\\picUrl.txt"));
		for (String path1 : a) {
			String src1 = path1.split(",")[0];
	        String path = src1.replace("//", "/");
	        String []paths = path.split("/");
	        String one = paths[8];
	        String two = paths[9];
	        String name = paths[10];
	        File dir = new File("E:\\data\\CitroenPic1\\"+one+"\\"+two);
	        if (!dir.exists()) {
	             dir.mkdir();
	        }
	        File targetFile = new File(dir, name);
	        if(targetFile.exists()){
	        	continue;
	        }
	        execcomp.submit(new downloadPic(targetFile, src1));
    }
		try {
  			Thread.currentThread().sleep(5 * 24 * 60 * 60 * 1000);
  		} catch (InterruptedException e) {
  			e.printStackTrace();
  		}
    }
    class downloadPic implements Callable<String>{
    	private File targetFile ;
		private String s;
		public downloadPic(File targetFile, String s) {
			this.targetFile = targetFile;
			this.s = s;
		}
		@Override
		public String call() throws Exception {
			// TODO Auto-generated method stub
			System.out.println("正在解析车型"+s+"");
			downPic(targetFile,s);
			return null;
		}
    	
    }
    private static void downPic(File targetFile,String s) throws Exception{
    	byte[] data = HttpServiceUtil.downloadPic(s);
	    FileUtils.writeByteArrayToFile(targetFile, data);
    }
}
