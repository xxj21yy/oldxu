package com.example.collectdata;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
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

public class DownLoadNotice {

	private static Logger logger = LoggerFactory.getLogger(DownLoadNotice.class);
	AtomicInteger count = new AtomicInteger(1);
	AtomicInteger downCount = new AtomicInteger(1);
	// 下载Html
	@Test
	public void downloadHtml() {
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(20);
		CompletionService<String> execcomp = new ExecutorCompletionService<String>(
				fixedThreadPool);
		try {
			for (int i = 298;i> 294;i--) {
				String url = "http://www.cn357.com/notice_"+i;
				
//				execcomp.submit(new ParserHtml(file, file.getName()));
				parseHtml(i,url);
			}
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
		private int  j;
		private int i;
		public ParserHtml(int j,int i) {
			this.j = j;
			this.i = i;
		}

		@Override
		public String call() {
			try {
				String url1 = "http://www.cn357.com/notice_299_" + j;
				Document document1 = Jsoup.parse(HttpServiceUtil.doGet(url1));
				Elements secTitle1 = document1.select("div[class='noticeLotItem']");
				int p = 0;
				for (Element element : secTitle1) {
					System.out.println("正在下载第"+j+"页第"+(++p)+"个html");
					String a1 = element.select("span[class='m']>a").attr("href");
					String url2 = "http://www.cn357.com" + a1;
					File file = new File("E:\\data\\Notice\\"+i);
					if(!file.exists()){
						file.mkdir();
					}
					File targetFile = new File(file,a1+".html");
					if(!targetFile.exists()){
						
						Document document0 = Jsoup.parse(HttpServiceUtil.doGet(url2));
						saveHtml(targetFile.getAbsolutePath(), document0.html());
					}
					
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			return "";
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

	public static void saveHtml(String filepath, String str) {

		try {
			OutputStreamWriter outs = new OutputStreamWriter(
					new FileOutputStream(filepath, true), "gbk");
			outs.write(str);
			outs.close();
		} catch (Exception e) {
			logger.error("Error at save html...");
			e.printStackTrace();
		}
	}
	
	public  void parseHtml(int i ,String url){
		try {
			Document document = Jsoup.parse(HttpServiceUtil.doGet(url));
			//获取页码
			Elements page = document.select("span[class='pageList'] > a");
			int pageNum = Integer.valueOf(page.get(page.size() - 2).text());
			for(int j = 1;j < pageNum + 1 ;j++){
//				ExecutorService fixedThreadPool = Executors.newFixedThreadPool(20);
//				CompletionService<String> execcomp = new ExecutorCompletionService<String>(
//						fixedThreadPool);
//			    execcomp.submit(new ParserHtml(j, i));
				String url1 = "http://www.cn357.com/notice_299_" + j;
				Document document1 = Jsoup.parse(HttpServiceUtil.doGet(url1));
				Elements secTitle1 = document1.select("div[class='noticeLotItem']");
				int p = 0;
				for (Element element : secTitle1) {
					System.out.println("正在下载第"+j+"页第"+(++p)+"个html");
					String a1 = element.select("span[class='m']>a").attr("href");
					String url2 = "http://www.cn357.com" + a1;
					File file = new File("E:\\data\\Notice\\"+i);
					if(!file.exists()){
						file.mkdir();
					}
					File targetFile = new File(file,a1+".html");
					if(targetFile.exists()){
						System.out.println(targetFile.getName()+"已经存在");
						continue;
					}
					Document document0 = Jsoup.parse(HttpServiceUtil.doGet(url2));
					saveHtml(targetFile.getAbsolutePath(), document0.html());
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
					
						
//		saveHtml(path, document0.html());
						
	}
	
	private static String replaceStr(String str){
		Pattern pattern = Pattern.compile("(?<=ImgsWatermark)+.*");
		Matcher m = pattern.matcher(str);
		if (m.find()) {
			return m.group();
		}
		return null;
	}

	public static void main(String[] args) {
//		String s1 = "/ImgsWatermark/img/GL/039/SECIMG/000048.gif";
//		Pattern pattern = Pattern.compile("(?<=ImgsWatermark)+.*");
//		Matcher m = pattern.matcher(s1);
//		if (m.find()) {
//			System.out.println(m.group());
//		}
		File fileData = new File("E:\\data\\NissanHtml\\bugHtml");
		List<File> fileNameList = new ArrayList<File>();
		List<File> list = fileReader(fileData, fileNameList);
		System.out.println("总文件数"+list.size());
		
//		File dir2 = new File("e:\\test\\NissanHtml\\bugHtml\\"
//				+ 1 + "\\" + 2 + "\\" +3  + "-"
//				+ 4 + ".txt");
//		try {
//			if (!dir2.exists()) {
//				dir2.mkdir();
//			}
//			FileUtils.write(dir2, "http:www.baidu.com", "UTF-8");
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
		
	}
}
