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

public class DownCheryHtml {

	private static Logger logger = LoggerFactory.getLogger(DownCheryHtml.class);
	AtomicInteger count = new AtomicInteger(1);
	AtomicInteger downCount = new AtomicInteger(1);
	// 下载Html
	@Test
	public void downloadHtml() {
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(20);
		CompletionService<String> execcomp = new ExecutorCompletionService<String>(
				fixedThreadPool);
		try {
			downHtml();
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

	public static void saveHtml(String filepath, String str) {

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
	
	public static void downHtml(){
		try {
			Document document = Jsoup.parse(HttpServiceUtil
					.doGet("http://cherylist.com/chery-automobile.aspx"));
			Elements tr = document
					.select("div[id='pSelectModel'] > table > tbody > tr");
			Elements td = tr.select("td");
			File file = new File ("E:/data/cheryhtml");
			if(!file.exists()){
				file.mkdir();
			}
			for (Element element : td) {
				String url = "http://cherylist.com/"+element.select("a").attr("href");
				Document document1 = Jsoup.parse(HttpServiceUtil.doGet(url));
				Elements tables = document1.select("div[id='tvCategories']>table");
				//循环table
				for(int i = 1;i<tables.size();i++){
					//获取table
					Elements table =  tables.get(i).select("tbody>tr>td");
					String path1 = tables.get(1).select("tbody>tr>td").last().select("a").text();
					File file1 = new  File(file.getAbsolutePath(),path1);
					if(!file1.exists()){
						file1.mkdir();
					}
					if(table.size() == 6){
						System.out.println("正在下载第二层");
						String url1 = table.last().select("a").attr("href");
						String path2 = table.last().select("a").text();
						File file2 = new File(file1.getAbsolutePath(),path2);
						if(!file2.exists()){
							file2.mkdir();
						}
						Document document2 = null;
						try {
							document2= Jsoup.parse(HttpServiceUtil.doGet(url1));
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
							continue;
						}
						
						Elements et = document2.select("table[id='gvParts']");
						if(et.size() > 0){
							saveHtml(file2.getAbsolutePath()+"/"+path2+".html",document2.html());
							continue;
						}
						Elements tables1 = document2.select("div[id='tvCategories']>table");
						for(int j = 1;j<tables1.size();j++){
							Elements table1 =  tables1.get(j).select("tbody>tr>td");
							if(table1.size() == 8){
								System.out.println("正在下载第三层");
								String url2 = table1.last().select("a").attr("href");
								String path3 = table1.last().select("a").text();
								File file3 = new  File(file2.getAbsolutePath(),path3);
								if(!file3.exists()){
									file3.mkdir();
								}
								Document document3 = null;
								try {
									document3 = Jsoup.parse(HttpServiceUtil.doGet(url2));
								} catch (Exception e) {
									// TODO: handle exception
									e.printStackTrace();
									continue;
								}
								
								Elements et1 = document3.select("table[id='gvParts']");
								if(et1.size() > 0){
									saveHtml(file3.getAbsolutePath()+"/"+path3+".html",document3.html());
									continue;
								}
								Elements tables2 = document3.select("div[id='tvCategories']>table");
								for(int k = 1;k < tables2.size();k++){
									Elements table2 =  tables2.get(k).select("tbody>tr>td");
									if(table2.size() == 10){
										System.out.println("正在下载第四层");
										String url3 = table2.last().select("a").attr("href");
										String path4 = table2.last().select("a").text();
										File file4 = new  File(file3.getAbsolutePath(),path4);
										if(!file4.exists()){
											file4.mkdir();
										}
										Document document4 = null;
										try{
											document4 = Jsoup.parse(HttpServiceUtil.doGet(url3));
											Elements et2 = document4.select("table[id='gvParts']");
											if(et2.size() > 0){
												saveHtml(file4.getAbsolutePath()+"/"+path4+".html",document4.html());
												continue;
											}
											
										}catch(Exception e){
											e.printStackTrace();
											continue;
										}
										Elements tables3 = document4.select("div[id='tvCategories']>table");
										
										for(int l = 1;l<tables3.size();l++){
											Elements table3 =  tables3.get(l).select("tbody>tr>td");
											if(table3.size() == 12){
												System.out.println("正在下载第五层");
												String url4 = table3.last().select("a").attr("href");
												String path5 = table3.last().select("a").text();
												File file5 = new  File(file4.getAbsolutePath(),path5);
												if(!file5.exists()){
													file5.mkdir();
												}
												try{
													Document document5 = Jsoup.parse(HttpServiceUtil.doGet(url4));
													Elements et3 = document5.select("table[id='gvParts']");
													if(et3.size() > 0){
														saveHtml(file5.getAbsolutePath()+"/"+path5+".html",document5.html());
														continue;
													}
												}catch(Exception e){
													e.printStackTrace();
													continue;
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
		
		Document document1 = Jsoup.parse(HttpServiceUtil.doGet("http://cherylist.com/CHERY-QQ-SWEET-COVER-%2CTIMING-BELT-87316.aspx"));	
		System.out.println(document1.html());
			
	}
}
