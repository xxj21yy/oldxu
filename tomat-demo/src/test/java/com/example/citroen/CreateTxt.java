package com.example.citroen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
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
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.collectdata.FileUtil;
import com.example.collectdata.WriteObject;
import com.example.collectdata.WriteQueue;
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
		try {
			File fileData = new File("E:\\data\\Citroen\\txt1");
			List<File> fileNameList = new ArrayList<File>();
			List<File> list = FileUtil.fileReader(fileData, fileNameList);
			for (File file : list) {
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
		public ParserHtml(File file) {
			this.file = file;
		}

		@Override
		public String call() {
				System.out.println("正在解析" + count.getAndIncrement());
				String path = file.getParent();
				String pathtemp = "";
				Pattern pattern = Pattern.compile("(?<=txt1)+.*"); 
				Matcher m = pattern.matcher(path);
				if(m.find()){
					pathtemp = m.group();
				}
				try {
					File dir = new File("e:\\data\\CitroenTxt1\\" + pathtemp);
					if (!dir.exists()) {
						dir.mkdirs();
					}
					File targetFile = new File(dir, dir.getName() + ".txt");
					addQueue(targetFile,file,pathtemp);
//					downLoadPic(file);
				} catch (Exception e) {
					File dir2 = new File("e:\\data\\Exception\\CitroenTxt1\\"
							+ pathtemp + "\\" + fileName + ".txt");
					try {
						FileUtils.write(dir2, pathtemp+"-"+fileName, "UTF-8");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					logger.error(e.getMessage());
				}
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
	
	private static void downLoadPic(File file){
		try {
			Document document = Jsoup.parse(file, "UTF-8");
			//图片
			Elements selectImg = document.select("table.tableimage > tbody > tr > td > div >div>img");
			String src = "http://service.citroen.com/docapv/"+selectImg.attr("src");
            byte[] data = HttpServiceUtil.downloadPic(src);
            String path = src.replace("//", "/");
            String []paths = path.split("/");
            String one = paths[8];
            String two = paths[9];
            String name = paths[10];
            File dir = new File("E:\\data\\CitroenPic\\"+one+"\\"+two);
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
	//入队列
	private static void addQueue(File targetFile,File file,String pathtemp){
		try {
			WriteObject writeObject = new WriteObject();
			StringBuilder stringBuilder = new StringBuilder();
			Document document = Jsoup.parse(file, "UTF-8");
			String [] paths = targetFile.getParent().split("\\\\");
			String carType = paths[3];
			String type1 = paths[4];
			String type2 = paths[5];
			if(carType.indexOf("&") > 0){
				carType = carType.replaceAll("&", "/");
			}
			if(type1.indexOf("&") > 0){
				type1 = type1.replaceAll("&", "/");
			}
			if(type2.indexOf("&") > 0){
				type2 = type2.replaceAll("&", "/");
			}
			//标题
			Elements selectTitle = document.select("table.tablefiche>tbody>tr>td");
			//二级标题
			Elements secTitle = document.select("div[class='section_appl']");
			//图片
			Elements selectImg = document.select("table.tableimage > tbody > tr > td > div >div>img");
			Elements table = document.select("div[class='contentpr']>table");
			for (int i= 0; i < table.size() -1;i++ ) {
				String tableId = table.get(i).attr("id");
				tableId = tableId.substring(tableId.lastIndexOf("e")+1);
				String tdId = tableId;
				if(tableId.indexOf("-") > 0){
					tdId = tableId.split("-")[0];
				}
				stringBuilder.append("\"" + carType + "\"" + ","); 
				stringBuilder.append("\"" + type1 + "\"" + ","); 
				stringBuilder.append("\"" + type2 + "\"" + ","); 
				
				stringBuilder.append("\"" + tdId + "\"" + ","); 
				//标题
				stringBuilder.append("\"" + selectTitle.first().text() + "\"" + ","); 
				stringBuilder.append("\"" + selectTitle.last().text() + "\"" + ","); 
				//二级标题
				stringBuilder.append("\"" + secTitle.text() + "\"" + ",");
				Elements td = table.get(i).select("tbody > tr >td");
				for (int j = 0; j < td.size(); j ++) {
					if(j == 0){
						continue;
					}
					stringBuilder.append("\"" + td.get(j).text() + "\"" + ","); 
				}
				String src = "http://service.citroen.com/docapv/"+selectImg.attr("src");
				stringBuilder.append("\"" + src + "\"" + "\r\n"); 
			}
			writeObject.setData(stringBuilder.toString());
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
