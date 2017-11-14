package com.example.collectdata;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.util.HttpServiceUtil;


public class DownLoadPic {
    private static Logger logger = LoggerFactory.getLogger(DownLoadPic.class);
    int downloadCnt = 1;
    //下载图片
    @Test
	public void downloadPic() {
		ExecutorService fixedThreadPool2 = Executors.newFixedThreadPool(100);
		Queue<Future<String>> futList = new ConcurrentLinkedQueue<Future<String>>();
		Map<String, Integer> imgMap = new ConcurrentHashMap<>();
		String one = null;
		String two = null;
		try {
//			File fileData = new File("E:\\data\\ToyoData");
//			List<File> fileNameList = new ArrayList<File>();
//			List<File> list = fileReader(fileData, fileNameList);
//			//防止重复下载
//			File dir = new File ("E:\\data\\ToyoTaPic\\GR");
//			for (File subDir : dir.listFiles()) {
//                 for (File file1 : subDir.listFiles()) {
//                     imgMap.put(dir.getName()+"/"+subDir.getName() + "-" + file1.getName(), 1);
//                 }
//             }
//			for (File file : list) {
			File file  = new File("e:/data/image.txt");
				LineIterator lineIterator = FileUtils.lineIterator(file);
				while (lineIterator.hasNext()) {
					String line = lineIterator.nextLine();
					Pattern pattern = Pattern.compile("(?<=Bilder)+.*"); 
					Matcher m = pattern.matcher(line);
					String path = "";
					if(m.find()){
						path = m.group().split(",")[0];
						
					}
					String [] pathTemp = path.split("/");
					one = pathTemp[1];
					two = pathTemp[2];
					try {
//						if (imgMap.get(one + "-" + two) == null) {
							futList.add(fixedThreadPool2
									.submit(new DownLoadPic.Parser3(one, two)));
//							downPic(downloadCnt, one, two);
//							imgMap.put(one + "-" + two, 1);
//						}
					} catch (Exception e) {
						logger.error(line + "\t" + e.getMessage());
						imgMap.put(one + "-" + two, 1);
					}
				}
				System.out.println("共有" + imgMap.size() + "个图片");
//			}
		} catch (Exception e) {
			File dir2 = new File("E:\\data\\Exception\\" + one + " - " + two
					+ ".txt");
			try {
				if(!dir2.exists()){
					dir2.mkdir();
				}
				FileUtils.write(dir2, one + " - " + two, "UTF-8");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		try {
			Thread.currentThread().sleep(5 * 24 * 60 * 60 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    fixedThreadPool2.shutdown();
	}

    class Parser3 implements Callable<String> {

        private String one;
        private String two;

        public Parser3(String one, String two) {

            this.one = one;
            this.two = two;
        }

        @Override
        public String call() {
            try {
                System.out.println("在下载" + downloadCnt++);
                byte[] data = HttpServiceUtil.downloadPic("https://audi.7zap.com/images/Bilder/" + one + "/" + two);
                File dir = new File("E:\\data\\Audi\\" + one);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                File targetFile = new File(dir, two);
                FileUtils.writeByteArrayToFile(targetFile, data);
            } catch (Exception e) {
                File dir2 = new File("E:\\data\\Exception\\" + one + " - " + two + ".txt");
                try {
//                	if(!dir2.exists()){
//                       	dir2.mkdir();
//                    }
                    FileUtils.write(dir2, one + " - " + two, "UTF-8");
                } catch (IOException e1) {
                    e1.printStackTrace();
                } 	
                logger.error(e.getMessage());
            }
            return null;
        }
    }
    
    private static List<File> fileReader(File file ,List<File> fileList){
    	File [] files = file.listFiles();
    	if(null == files){
    		return fileList;
    	}
    	for (File f : files) {
			if(f.isDirectory()){
				fileReader(f,fileList);
			}else{
				fileList.add(f);
			}
		}
    	return fileList;
    }
    
    private static void downPic(int downloadCnt,String one ,String two){
    	try {
            System.out.println("在下载" + downloadCnt++);
            byte[] data = HttpServiceUtil.downloadPic("https://audi.7zap.com/images/Bilder/" + one + "/" + two);
            File dir = new File("E:\\data\\ToyoTaPic\\" + one);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File targetFile = new File(dir, two);
            FileUtils.writeByteArrayToFile(targetFile, data);
        } catch (Exception e) {
            File dir2 = new File("E:\\data\\Exception\\" + one + " - " + two + ".txt");
            try {
            	if(!dir2.exists()){
                   	dir2.mkdir();
                }
                FileUtils.write(dir2, one + " - " + two, "UTF-8");
            } catch (IOException e1) {
                e1.printStackTrace();
            } 	
            logger.error(e.getMessage());
        }
    }
    
    public static void main(String[] args) {
    	  String s1 = "'https://toyota.7zap.com/images/Img/GR/A1/090336A.png','t','d'";
			Pattern pattern = Pattern.compile("(?<=Img)+.*"); 
			Matcher m = pattern.matcher(s1);
			if(m.find()){
				System.out.println(m.group());
			}
			
	}
}



