package com.example.collectdata;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
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

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ImportHtml {

	private static Logger logger = LoggerFactory.getLogger(ImportHtml.class);
	static AtomicInteger count = new AtomicInteger(1);
	
	// html转txt
	@Test
	public void importHtml() {
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(20);
		CompletionService<String> execcomp = new ExecutorCompletionService<String>(
				fixedThreadPool);
		try {
			File fileData = new File("E:\\data\\NissanHtml");
			List<File> fileNameList = new ArrayList<File>();
			List<File> list = fileReader(fileData, fileNameList);
//			importHtml(list);
			for (File file : list) {
				execcomp.submit(new ParserHtml(file, file.getName()));
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
		public ParserHtml(File file, String fileName) {
			this.file = file;
			this.fileName = fileName;
		}

		@Override
		public String call() {
				System.out.println("正在解析" + count.getAndIncrement());
				String[] split = file.getAbsolutePath().split("\\\\");
				String middle = split[3];
				String number = fileName.substring(fileName.indexOf("-") + 1,
						fileName.lastIndexOf("."));
				String name = fileName.substring(0, fileName.lastIndexOf("-"));
				try {
					File dir = new File("e:\\data\\NissanTxtTest\\" + middle + "\\"
							+ name);
					if (!dir.exists()) {
						dir.mkdirs();
					}
					File targetFile = new File(dir, name + ".txt");
					addQueue(targetFile, file, middle, name, number);
				} catch (Exception e) {
					File dir2 = new File("e:\\data\\NissanTxtTest\\bugTxt\\"
							+ middle + "\\" + name + "\\" + name + "-" + number
							+ ".txt");
					try {
						FileUtils.write(dir2, name+"-"+number, "UTF-8");
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
	
	private static void importHtml(List<File> list){
		Writer writer = null; 
		for (File file : list) {
			System.out.println("正在解析"+count.getAndIncrement());
			String fileName = file.getName();
			String[] split = file.getAbsolutePath().split("\\\\");
			String middle = split[3];
			String number = fileName.substring(fileName.indexOf("-") + 1,
					fileName.lastIndexOf("."));
			String name = fileName.substring(0, fileName.lastIndexOf("-"));
			
			try {
				File dir = new File("e:\\data\\NissanTxtTest\\" + middle + "\\"
						+ name);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				File targetFile = new File(dir, name + ".txt");
				writer = new BufferedWriter(new FileWriter(targetFile, true));
				parseHtml(writer, file);
			} catch (Exception e) {
				e.printStackTrace();
				File dir2 = new File("e:\\data\\NissanTxtTest\\bugTxt\\"
						+ middle + "\\" + name + "\\" + name + "-" + number
						+ ".txt");
				try {
					FileUtils.write(dir2, name+"-"+number, "UTF-8");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				logger.error(e.getMessage());
			}
		}
		
	}
	
	private static void parseHtml(Writer writer, File file) throws Exception {
		StringBuilder stringBuilder = new StringBuilder();
		// 获取配件数据
		Document document = Jsoup.parse(file, "UTF-8");
		Elements select = document.select("body > div.row >div> div.table-responsive > table > tbody");
		
		if (select != null && !"".equals(select)) {
			
			Elements select1 = select.select("tr[class='one_part']");
			//车型信息
			stringBuilder = createString(stringBuilder,document);
			StringBuilder strTemp = new StringBuilder(stringBuilder.toString());
			int i = 0;
			for (Element element1 : select1) {
				i++;
				//oem信息
				createTd(stringBuilder,element1);
				//换行
				stringBuilder.append("\r\n");
				if(i < select1.size()){
					stringBuilder.append(strTemp);
				}
			}
		}
		writer.write(stringBuilder.toString());
		writer.flush();
		writer.close();

	}
	
	//入队列
	private static void addQueue(File targetFile,File file,String middle,String name,String number){
		try {
			WriteObject writeObject = new WriteObject();
			StringBuilder stringBuilder = new StringBuilder();
			// 获取配件数据
			Document document = Jsoup.parse(file, "UTF-8");
			Elements select = document.select("body > div.row >div> div.table-responsive > table > tbody");
			if (select != null && !"".equals(select)) {
				Elements select1 = select.select("tr[class='one_part']");
				//车型信息
				stringBuilder = createString(stringBuilder,document);
				StringBuilder strTemp = new StringBuilder(stringBuilder.toString());
				int i = 0;
				for (Element element1 : select1) {
					i++;
//					//oem信息
					createTd(stringBuilder,element1);
//					//换行
					stringBuilder.append("\r\n");
				
					if(i < select1.size()){
						stringBuilder.append(strTemp);
					}
					
				}
			}
			writeObject.setData(stringBuilder.toString());
			writeObject.setFile(targetFile);
			WriteQueue.getInstance().addQueue(writeObject);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			File dir2 = new File("e:\\data\\NissanTxt\\bugTxt\\"
					+ middle + "\\" + name + "\\" + name + "-" + number
					+ ".txt");
			try {
				FileUtils.write(dir2, name+"-"+number, "UTF-8");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	private static  StringBuilder createString(StringBuilder stringBuilder,Document document) throws Exception {
		// 获取车型相关信息
		Elements infoSelect = document.select("#get_inf");
		String json = infoSelect.text();
		JSONObject map = JSON.parseObject(json);
		JSONObject main_features =  map.getJSONObject("main_features");
		JSONObject other_features =  map.getJSONObject("other_features");
		// 品牌
		String brand = main_features.getString("Catalog");
		stringBuilder.append("\"" + brand + "\"" + ",");
		// 地区
		String market = main_features.getString("Market");
		stringBuilder.append("\"" + market + "\"" + ",");
		// 车系
		String model = main_features.getString("Model");
		stringBuilder.append("\"" + model + "\"" + ",");
		// 车型
		String modification = main_features.getString("Modification");
		stringBuilder.append("\"" + modification + "\"" + ",");
		// 年份
		String carDate = main_features.getString("Date");
		stringBuilder.append("\"" + carDate + "\"" + ",");
		// 发动机号
		String engine = main_features.getString("Engine");
		stringBuilder.append("\"" + engine + "\"" + ",");
		// body
		String bodyTemp = other_features.getString("BODY");
//		String body = bodyTemp == null ? "" : bodyTemp.toString();
		stringBuilder.append("\"" + bodyTemp + "\"" + ",");
		// drive
		String driveTemp = other_features.getString("DRIVE");
//		String drive = driveTemp == null ? "" : driveTemp.toString();
		stringBuilder.append("\"" + driveTemp + "\"" + ",");
		// grade
		String gradeTemp = other_features.getString("GRADE");
//		String grade = gradeTemp == null ? "" : gradeTemp.toString();
		stringBuilder.append("\"" + gradeTemp + "\"" + ",");
		// area
		String areaTemp = other_features.getString("AREA");
//		String area = areaTemp == null ? "" : areaTemp.toString();
		stringBuilder.append("\"" + areaTemp + "\"" + ",");
		// intake
		String intakeTemp = other_features.getString("INTAKE");
//		String intake = intakeTemp == null ? "" : intakeTemp.toString();
		stringBuilder.append("\"" + intakeTemp + "\"" + ",");
		// trans
		String transTemp = other_features.getString("TRANS");
//		String trans = transTemp == null ? "" : transTemp.toString();
		stringBuilder.append("\"" + transTemp + "\"" + ",");
		// equip
		String equipTemp = other_features.getString("EQUIP");
//		String equip = equipTemp == null ? "" : equipTemp.toString();
		stringBuilder.append("\"" + equipTemp + "\"" + ",");
		 //主组
		String first = document.select("#breadcrumbs > li:nth-child(10)")
				.first().text().trim();
		stringBuilder.append("\"" + first + "\"" + ",");
		// 子组
		String second  = document.select("#breadcrumbs > li.active.hidden-xs")
				.first().text().trim();
		stringBuilder.append("\"" + second + "\"" + ",");
		// 获取配件图
		Elements img = document.select("body > div.row > div > div > img");
		StringBuilder url = new StringBuilder();
		for (Element element : img) {
			
			url.append(element.attr("src")+";");
		}
		stringBuilder.append("\"" + url + "\"" + ",");

		return stringBuilder;
	}
	
	private static StringBuilder createTd(StringBuilder stringBuilder,Element element)throws Exception{
		//获取oem信息
		Elements elementTd = element.select("td");
		for (int i = 0;i < elementTd.size();i++) {
			if(i < elementTd.size() -1){
				 String txt = elementTd.get(i).text();
                 stringBuilder.append("\"" + txt + "\"" + ",");
			}else{
				//链接
				Elements span = elementTd.get(elementTd.size() -1).select("span");
				if(null != span && span.size() > 0){
					for (Element elementSpan : span) {
						String onclick = elementSpan.attr("onclick");
						String params = onclick.substring(onclick.indexOf("(") + 1, onclick.lastIndexOf(")"));
						params = params.replace("'", "").replace(" ", "");
						String []param = params.split(",");
						//处理跳转
						if(span.size() == 1){
							StringBuffer url = new StringBuffer("https://nissan.7zap.com/cat_scripts/get_part.php?catalog=");
							url.append(param[1]).append("&lang=");
							url.append(param[0]).append("&model_code=");
							url.append(param[4]).append("&family=");
							url.append(param[3]).append("&infinity=");
							url.append(param[2]).append("&number_years=");
							url.append(param[5]).append("&group=");
							url.append(param[6]).append("&subgroup=");
							url.append(param[7]).append("&param=");
							url.append("&_=0");
							stringBuilder.append("\"" + url + "\"" + ",");
						}else{
							StringBuffer url = new StringBuffer("https://nissan.7zap.com/cat_scripts/get_popup.php?lang=");
							url.append(param[0]).append("&catalog=");
							url.append(param[1]).append("&detail_name=");
							url.append(param[3]).append("&detail_number=");
							url.append(param[4]).append("&infinity=");
							url.append(param[2]).append("&window_type=");
							url.append(param[5]).append("&partcode_get=");
							url.append(param[6]).append("&mdldir_get=");
							url.append(param[7]).append("&from=");
							url.append(param[8]).append("&to=");
							url.append(param[9]).append("&modvarno=");	
							url.append(param[10]).append("&_=0");
							stringBuilder.append("\"" + url + "\"" + ",");
						}
					}
				}
			}
		}
		return stringBuilder;
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
	


	public static void main(String[] args) {
		File fileData = new File("E:\\data\\NissanHtml");
		List<File> fileNameList = new ArrayList<File>();
		List<File> list = fileReader(fileData, fileNameList);
		System.out.println(list.size());
	}
}
