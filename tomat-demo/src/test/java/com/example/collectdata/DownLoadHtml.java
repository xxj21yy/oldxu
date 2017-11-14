package com.example.collectdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.util.HttpServiceUtil;

public class DownLoadHtml {

	private static Logger logger = LoggerFactory.getLogger(DownLoadHtml.class);
	AtomicInteger count = new AtomicInteger(1);
	AtomicInteger downCount = new AtomicInteger(1);
	// 下载Html
	@Test
	public void downloadHtml() {
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(20);
		CompletionService<String> execcomp = new ExecutorCompletionService<String>(
				fixedThreadPool);
		try {
			File fileData = new File("E:\\data\\NissanHtml\\bugHtml");
			List<File> fileNameList = new ArrayList<File>();
			List<File> list = fileReader(fileData, fileNameList);
			for (File file : list) {
				execcomp.submit(new ParserHtml(file, file.getName()));
//				parseHtml(file,file.getName());
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
		private File file;
		private String fileName;

		public ParserHtml(File file, String fileName) {
			this.file = file;
			this.fileName = fileName;
		}

		@Override
		public String call() {
			String[] split = file.getAbsolutePath().split("\\\\");
			String middle = split[4];
			String number = fileName.substring(fileName.indexOf("-") + 1,
					fileName.lastIndexOf("."));
			String name = fileName.substring(0, fileName.lastIndexOf("-"));
			InputStreamReader read = null;// 考虑到编码格式
			try {
				read = new InputStreamReader(new FileInputStream(file), "GBK");
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					String url = lineTxt;
					File dir = new File("e:\\data\\NissanHtml\\" + middle
							+ "\\" + name);
					if (!dir.exists()) {
						dir.mkdirs();
					}
					File targetFile = new File(dir, name + "-" + number
							+ ".html");
					String path = targetFile.getAbsolutePath();
					//判断是否已经下载
					if(!targetFile.exists()){
						System.out.println("在下载" + count.getAndIncrement());
						try {
							Document document0 = null;
							url = url.replace(" ", "");
							document0 = Jsoup.parse(HttpServiceUtil.doGet(url));
							Elements select = document0
									.select("body > div.row > div > div > img");
							String imgSrc = null;
							String src = null;
							for (Element element : select) {
								src = element.attr("src");
								src = replaceStr(src);
								imgSrc = "https://nissan.7zap.com" + src;
								element.attr("src", imgSrc);
							}
							
								saveHtml(path, document0.html());
							
						} catch (Exception e) {
							File dir2 = new File("e:\\data\\NissanHtml\\bugHtml\\"
									+ middle + "\\" + name + "\\" + name + "-"
									+ number + ".txt");
							try {
		//						if (!dir2.exists()) {
		//							dir2.mkdir();
		//						}
								FileUtils.write(dir2, url, "UTF-8");
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
				  }else{
					  System.out.println("已经存在，不下载"+downCount.getAndIncrement());
				  }
				}
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
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
					new FileOutputStream(filepath, true), "utf-8");
			outs.write(str);
			outs.close();
		} catch (Exception e) {
			logger.error("Error at save html...");
			e.printStackTrace();
		}
	}
	
	public static void parseHtml(File file,String fileName){
		String[] split = file.getAbsolutePath().split("\\\\");
		String middle = split[4];
		String number = fileName.substring(fileName.indexOf("-") + 1,
				fileName.lastIndexOf("."));
		String name = fileName.substring(0, fileName.lastIndexOf("-"));
		InputStreamReader read = null;// 考虑到编码格式
		try {
			read = new InputStreamReader(new FileInputStream(file), "GBK");
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null;
			while ((lineTxt = bufferedReader.readLine()) != null) {
				String url = lineTxt;
				File dir = new File("e:\\data\\NissanHtml\\" + middle
						+ "\\" + name);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				File targetFile = new File(dir, name + "-" + number
						+ ".html");
				String path = targetFile.getAbsolutePath();
				//判断是否已经下载
				if(!targetFile.exists()){
					try {
						Document document0 = null;
						url = url.replace(" ", "");
						document0 = Jsoup.parse(HttpServiceUtil.doGet(url));
						Elements select = document0
								.select("body > div.row > div > div > img");
						String imgSrc = null;
						String src = null;
						for (Element element : select) {
							src = element.attr("src");
							src = replaceStr(src);
							imgSrc = "https://nissan.7zap.com" + src;
							element.attr("src", imgSrc);
						}
						
							saveHtml(path, document0.html());
						
					} catch (Exception e) {
						File dir2 = new File("e:\\data\\NissanHtml\\bugHtml\\"
								+ middle + "\\" + name + "\\" + name + "-"
								+ number + ".txt");
						try {
	//						if (!dir2.exists()) {
	//							dir2.mkdir();
	//						}
							FileUtils.write(dir2, url, "UTF-8");
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
			  }
			}
		} catch (IOException e) {
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
