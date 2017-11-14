package com.example.collectdata;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.util.HttpServiceUtil;
import com.virjar.dungproxy.client.httpclient.HttpInvoker;

public class DownHondaHtml {

	private static Logger logger = LoggerFactory.getLogger(DownHondaHtml.class);
	AtomicInteger count = new AtomicInteger(1);
	AtomicInteger downCount = new AtomicInteger(1);
	// 下载Html
	@Test
	public void downloadHtml() {
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(20);
		CompletionService<String> execcomp = new ExecutorCompletionService<String>(
				fixedThreadPool);
		try {
			List<String> list = FileUtil.getLineFromFile(new File("e:/data/honda/url1.txt"));
			for (String url : list) {
				Thread.sleep(2000);
//				downHtml1(url);
				downHtml(url);
			}
//			downUrl();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Thread.currentThread().sleep(5 * 24 * 60 * 60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		fixedThreadPool.shutdown();
	}

	class ParserHtml implements Callable<String> {
		private File file;
		private String fileName;

		public ParserHtml(File file, String fileName) {
			this.file = file;
			this.fileName = fileName;
		}
		
		public ParserHtml() {
		}

		@Override
		public String call() {
			
			return null;
		}
		
	}

	private static List<File> fileReader(File file, List<File> fileList) {
		File[] files = file.listFiles();
		if (null == files) {
			return fileList;
		}
		for (File f : files) {
			if (f.isDirectory()) {
				fileReader(f, fileList);
			} else {
				fileList.add(f);
			}
		}
		return fileList;
	}

	public static  void saveHtml(String filepath, String str) {

		try {
			OutputStreamWriter outs = new OutputStreamWriter(
					new FileOutputStream(filepath, false), "utf-8");
			outs.write(str);
			outs.close();
		} catch (Exception e) {
			logger.error("Error at save html...");
			e.printStackTrace();
		}
	}
	
	private static void downHtml(String url){
		try {
			Document document = Jsoup.parse(HttpServiceUtil.doGet(url));
			Elements li = document.select("ul[class='list-unstyled tree']>li");
			downHtml(document);
			for(int i = 1;i < li.size();i++){
				String path ="https://partsouq.com"+ li.get(i).select("div>a").attr("href");
				document = Jsoup.parse(HttpServiceUtil.doGet(path));
				downHtml(document);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	private static void downHtml(Document document) throws Exception{
		Elements divs = document.select("div[class='row display-flex']>div");
		for (Element div : divs) {
			Elements a = div.select("div >div").last().select("a");
			String url = "https://partsouq.com"+a.attr("href");
			String fileName = a.text().replaceAll(":", "").replaceAll("/", "");
			Document document1 = Jsoup.parse(HttpServiceUtil.doGet(url));
			File file = new File("e:/data/honda/html",fileName+".html");
			System.out.println("正在下载html");
			saveHtml(file.getPath(), document1.html());
		}
	}
	
	private static void downHtml1(String url){
		try {
			Document document = Jsoup.parse(HttpServiceUtil.doGet(url));
			Elements tab = document.select("table[class='table table-hover mb-0px']>tbody>tr");
			for(int i = 1;i < tab.size();i++){
				String path ="https://partsouq.com"+ tab.get(i).select("td").first().select("a").attr("href");
				System.out.println(path);
				FileUtil.writeFile(new File("e:/data/honda/url1.txt"), path);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	public static void downUrl(){
		try {
			Document document = Jsoup.parse(HttpServiceUtil
					.doGet("https://partsouq.com/en/catalog/genuine/pick?c=Honda&model=Asia&ssd=%24WiVK%24"));
			//获取model
			Elements models = document
					.select("select[id='f_model']>option");
			//循环model
			for (int i = 1;i < models.size();i++) {
				String model = models.get(i).text();
				model = URLEncoder.encode(model);
				String url = "https://partsouq.com/en/catalog/genuine/filter?c=Honda&ssd=%24WiVK%24&model=Asia&f_model="+model+"&f_year=";
				Document document1 = Jsoup.parse(HttpServiceUtil
						.doGet(url));
				Elements fyears = document1
						.select("select[id='f_year']>option");
				for (int j = 1;j < fyears.size();j++) {
					String year = fyears.get(j).text();
					if(null != year && !"".equals(year)){
						int parseYear = Integer.parseInt(year);
						if(parseYear < 2000){
							continue;
						}
						String url1 = url + parseYear;
						System.out.println(url1);
						File file = new File("e:/data/honda");
						if(!file.exists()){
							file.mkdir();
						}
						File targetFile = new File(file,"url.txt");
						FileUtil.writeFile(targetFile, url1);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	
	private static String replaceStr(String str){
		Pattern pattern = Pattern.compile("(?<=ImgsWatermark)+.*");
		Matcher m = pattern.matcher(str);
		if (m.find()) {
			return m.group();
		}
		return null;
	}

	public static void main(String[] args)throws Exception {
		
//		Document document1 = Jsoup.parse(HttpServiceUtil.doGet("https://partsouq.com/en/catalog/genuine/filter?c=Honda&ssd=%24WiVK%24&model=Asia&f_model=ACCORD%20AERODECK&f_year="));	
//		System.out.println(document1.html());
		for (int i = 0; i < 100; i++) {   
			System.out.println(HttpInvoker.get("https://partsouq.com"));
		}
		
			
	}
}
