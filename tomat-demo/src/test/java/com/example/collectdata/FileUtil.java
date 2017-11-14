package com.example.collectdata;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
	
	public static List<String> getLineFromFile(File file){
		List<String> list = new ArrayList<String>();
		if(null == file){
			return list;
		}
		InputStreamReader read = null;
		try {
			read = new InputStreamReader(new FileInputStream(file),"GBK");
			BufferedReader readText = new BufferedReader(read);
			String lineText = "";
			while((lineText = readText.readLine()) != null){
				list.add(lineText);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return list;
		}
		return list;
	}
	
	public static void saveHtml(String filepath, String str) {

		try {
			OutputStreamWriter outs = new OutputStreamWriter(
					new FileOutputStream(filepath, true), "utf-8");
			outs.write(str);
			outs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static List<File> fileReader(File file, List<File> fileList) {
		int i = 0;
		File[] files = file.listFiles();
		if (null == files) {
			return fileList;
		}
		for (File f : files) {
			if (f.isDirectory()) {
				fileReader(f, fileList);
			} else {
				System.out.println("读取文件进度。。。"+(++i));
				fileList.add(f);
			}
		}
		return fileList;
	}
	
	public static void writeFile(File file,String content){
		Writer writer;
		try {
			writer = new BufferedWriter(new FileWriter(file,
					true));
			 writer.write(content+"\r\n");
			 writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	public static void main(String[] args) {
		BigDecimal a = new BigDecimal(70);
		BigDecimal b = new BigDecimal(55);
		System.out.println(a.divide(b, 10, RoundingMode.UP));
		System.out.println(70/55);
		System.out.println(Math.log(1.2727272727272727)/(2015-2010));
	}

}	
