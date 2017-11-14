package com.example.collectdata;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.util.HttpServiceUtil;

public class CreateTxt {

	private static Logger logger = LoggerFactory.getLogger(CreateTxt.class);
	static AtomicInteger count = new AtomicInteger(1);
	static AtomicInteger j = new AtomicInteger(1);
	// html转txt
	@Test
	public void importHtml() {
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
		CompletionService<String> execcomp = new ExecutorCompletionService<String>(
				fixedThreadPool);
		Map<String,Integer> map = new ConcurrentHashMap<String ,Integer>();
		try {
			File fileData = new File("E:\\data\\Notice");
			List<File> fileNameList = new ArrayList<File>();
			List<File> list = FileUtil.fileReader(fileData, fileNameList);
			for (File file : list) {
				System.out.println("正在下载" + j.getAndIncrement());
//				execcomp.submit(new ParserHtml(file,map));
//				txt(file);
//				downLoadPic(file,map);
				Document document = Jsoup.parse(file, "gbk");
				Elements e = document.select("table[class='noticeAttr mt5'] >tbody > tr").first().select("td").eq(1);
				String path1 = e.text();
				//图片
				Elements selectImg = document.select("div[id='noticeImage']>div");
				for (Element element : selectImg) {
					String src = element.select("img").attr("src");
					execcomp.submit(new ParserHtml(src,path1));
				}
			}
//			System.out.println(map.size());
//			for (String  src : map.keySet()) {
//				execcomp.submit(new ParserHtml(src));
//				downLoadPic(src);
//			}
//			Thread.sleep(2000);
//			new Thread(new writeFile()).start();
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
		private Map map;
		private String path1;
		public ParserHtml(File file) {
			this.file = file;
		}
		public ParserHtml(File file,Map map) {
			this.file = file;
			this.map = map;
		}
		public ParserHtml(String fileName,String path1) {
			this.fileName = fileName;
			this.path1 = path1;
		}
		@Override
		public String call() {
//			System.out.println("正在解析" + count.getAndIncrement());
//			txt(file);
			downLoadPic(fileName,path1);
			return null;
		}
		
	}
	
	class writeFile implements Runnable {
		Writer writer = null;

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				while (true) {
					Queue<Object> queue = WriteQueue.getInstance().getQueue();
					for (Object object : queue) {
						System.out.println("正在写第"+j.getAndIncrement()+"条数据");
						WriteObject o = (WriteObject) object;
						File targetFile = new File(o.getFile()
								.getAbsolutePath());

						writer = new BufferedWriter(new FileWriter(targetFile,
								true));
						writer.write(o.getData());
						writer.flush();
						queue.remove(object);
					}
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
	
	private static Map downLoadPic(File file,Map map){
		try {
			System.out.println("正在解析" + j.getAndIncrement());
			Document document = Jsoup.parse(file, "gbk");
			Elements e = document.select("table[class='noticeAttr mt5'] >tbody > tr").first().select("td").eq(1);
			String path1 = e.text();
			//图片
			Elements selectImg = document.select("div[id='noticeImage']");
			for (Element element : selectImg) {
				String src = element.select("div > img").attr("src");
				map.put(src, 0);
				byte[] data = HttpServiceUtil.downloadPic(src);
				 String path = src.replace("//", "/");
		         String []paths = path.split("/");
		         String one = paths[5];
		         String name  = paths[6];
		         File dir = new File("E:\\data\\NoticePic1\\"+path1+"\\"+one);
		         if (!dir.exists()) {
		              dir.mkdir();
		         }
		         File targetFile = new File(dir, name);
		         FileUtils.writeByteArrayToFile(targetFile, data);
			}
			
			 
           
            
           
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return map;
		
	}
	
	private static void downLoadPic(String src,String path1){
		try {
			System.out.println("正在下载图片" + count.getAndIncrement());
			 byte[] data = HttpServiceUtil.downloadPic(src);
			 String path = src.replace("//", "/");
	         String []paths = path.split("/");
	         String one = paths[5];
	         String name  = paths[6];
	         File dir = new File("E:\\data\\NoticePic1\\"+path1+"\\"+one);
	         if (!dir.exists()) {
	              dir.mkdir();
	         }
	         File targetFile = new File(dir, name);
	         FileUtils.writeByteArrayToFile(targetFile, data);
           
            
           
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	private static void txt(File file){
		String path = file.getParent();
		path = path.split("\\\\")[3];
		try {
//			File dir = new File("e:\\data\\NoticeTxt\\" + path);
			File dir = new File("e:\\data\\NoticeTxt\\");
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File targetFile = new File(dir,  "notice.txt");
//			File targetFile = new File(dir,  dir.getName()+".txt");
			addQueue(targetFile,file,path);
		} catch (Exception e) {
//			File dir2 = new File("e:\\data\\Exception\\NoticeTxt\\"
//					+ path + "\\" + fileName + ".txt");
//			try {
//				FileUtils.write(dir2, path+"-"+fileName, "UTF-8");
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//			
			logger.error(e.getMessage());
		}
	}
	
	//入队列
	private static void addQueue(File targetFile,File file,String pathtemp){
		try {
			WriteObject writeObject = new WriteObject();
			StringBuilder stringBuilder = new StringBuilder();
			Document document = Jsoup.parse(file, "gbk");
			//tr
			Elements e = document.select("table[class='noticeAttr mt5'] >tbody > tr");
			for (int i = 0;i < e.size();i++) {
				if(e.size() > 22){
					if(i != 21){
						Elements td = e.get(i).select("td");
						for(int j = 0;j <td.size();j++){
							if(j % 2 != 0){
								
								stringBuilder.append("\"" + td.get(j).text().replaceAll("\"", "'") + "\"" + ","); 
							}
						}
					}else{
						Elements td = e.get(i).select("td > table > tbody >tr").eq(1).select("td");
						for (int j = 0; j < td.size(); j++) {
							String str = td.get(j).html().replaceAll("\"", "").replace("<br>", "/");
							stringBuilder.append("\"" + str+ "\"" + ","); 
						}
						
					}
				}else{
					
					Elements td = e.get(i).select("td");
					if(i < 21){
						for(int j = 0;j <td.size();j++){
							if(j % 2 != 0){
								
								stringBuilder.append("\"" + td.get(j).text().replaceAll("\"", "'") + "\"" + ","); 
							}
						}
					}else{
						for (int j = 0; j < 5; j++) {
							stringBuilder.append("\"\"" + ","); 
						}
						for(int j = 0;j < td.size();j++){
							if(j % 2 != 0){
								
								stringBuilder.append("\"" + td.get(j).text().replaceAll("\"", "'") + "\"" + ","); 
							}
						}
					}
					
					
				}
				
			}
			writeObject.setData(stringBuilder.toString()+"\r\n");
			writeObject.setFile(targetFile);
			WriteQueue.getInstance().addQueue(writeObject);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			File dir2 = new File("e:\\data\\Exception\\CitroenTxt1\\"
					+ pathtemp + "\\" + file.getName() +  ".txt");
			try {
				FileUtils.write(dir2, pathtemp+"-"+file.getName(), "UTF-8");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	
	




	public static void main(String[] args) throws Exception {
		StringBuilder stringBuilder = new StringBuilder();
	
		System.out.println(stringBuilder.toString());
	}
}
