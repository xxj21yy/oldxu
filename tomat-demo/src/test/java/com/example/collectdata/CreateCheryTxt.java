package com.example.collectdata;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
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

public class CreateCheryTxt {

	private static Logger logger = LoggerFactory.getLogger(CreateCheryTxt.class);
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
			File fileData = new File("E:\\data\\cheryhtml");
			List<File> fileNameList = new ArrayList<File>();
			List<File> list = FileUtil.fileReader(fileData, fileNameList);
			for (File file : list) {
//				txt(file);
//				downLoadPic(file,map);
				execcomp.submit(new ParserHtml(file));
			}
			Thread.sleep(2000);
			new Thread(new writeFile()).start();
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
			System.out.println("正在解析" + count.getAndIncrement());
			txt(file);
//			downLoadPic(fileName,path1);
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
			Document document = Jsoup.parse(file,"utf-8");
			//图片
			Elements pic = document.select("img[id='imgPart']");
			String picUrl = "http://cherylist.com/"+pic.attr("src");
			
		    String path = picUrl.replace("//", "/");
		    String []paths = path.split("/");
		    String one = paths[3];
		    String name  = paths[4];
		    File dir = new File("E:\\data\\CheryPic\\"+one);
		         if (!dir.exists()) {
		              dir.mkdir();
		         }
		        
		         File targetFile = new File(dir, name);
		         if(!targetFile.exists()){
		        	 byte[] data = HttpServiceUtil.downloadPic(picUrl);
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
			File dir = new File("e:\\data\\CheryTxt\\");
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File targetFile = new File(dir,  "chery.txt");
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
			Document document = Jsoup.parse(file,"utf-8");
			//图片
			Elements pic = document.select("img[id='imgPart']");
			String picUrl = "http://cherylist.com/"+pic.attr("src");
			//数据
			Elements tab = document.select("table[id='gvParts']>tbody>tr");
			String [] paths = file.getParent().split("\\\\");
			for (Element element : tab) {
				if(paths.length < 8){
					int countTrim = 8 - paths.length;
					if(countTrim == 1){
						stringBuilder.append("\"" + paths[3]+ "\"" + ",");
						stringBuilder.append("\"" + paths[4]+ "\"" + ",");
						stringBuilder.append("\"" + paths[5]+ "\"" + ",");
						stringBuilder.append("\"" + paths[6]+ "\"" + ",");
						stringBuilder.append("\"\"" + ",");
					}else if(countTrim == 2){
						stringBuilder.append("\"" + paths[3]+ "\"" + ",");
						stringBuilder.append("\"" + paths[4]+ "\"" + ",");
						stringBuilder.append("\"" + paths[5]+ "\"" + ",");
						stringBuilder.append("\"\"" + ",");
						stringBuilder.append("\"\"" + ",");
					}else if(countTrim == 3){
						stringBuilder.append("\"" + paths[3]+ "\"" + ",");
						stringBuilder.append("\"" + paths[4]+ "\"" + ",");
						stringBuilder.append("\" \"" + ",");
						stringBuilder.append("\"\"" + ",");
						stringBuilder.append("\"\"" + ",");
					}
				}else{
					stringBuilder.append("\"" + paths[3]+ "\"" + ",");
					stringBuilder.append("\"" + paths[4]+ "\"" + ",");
					stringBuilder.append("\"" + paths[5]+ "\"" + ",");
					stringBuilder.append("\"" + paths[6]+ "\"" + ",");
					stringBuilder.append("\"" + paths[7]+ "\"" + ",");
				}
				Elements td = element.select("td");
				for (int i = 0;i<td.size();i++) {
					stringBuilder.append("\"" + td.get(i).text().replaceAll("\"", "'") + "\"" + ","); 
				}
				stringBuilder.append("\"" + picUrl+ "\"" + ",");
				stringBuilder.append("\r\n");
			}
			writeObject.setData(stringBuilder.toString());
			writeObject.setFile(targetFile);
			WriteQueue.getInstance().addQueue(writeObject);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	
	




	public static void main(String[] args) throws Exception {
		StringBuilder stringBuilder = new StringBuilder();
	
		System.out.println(stringBuilder.toString());
	}
}
