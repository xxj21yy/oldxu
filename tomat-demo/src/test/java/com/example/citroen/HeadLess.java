package com.example.citroen;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

public class HeadLess {
	public static void main(String[] args) {
		DesiredCapabilities dcaps = new DesiredCapabilities();
		dcaps.setJavascriptEnabled(true);
		dcaps.setCapability(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY,"C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
		//创建无界面浏览器对象
//        PhantomJSDriver  driver = new PhantomJSDriver(dcaps);
		System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
		ChromeDriver driver = new ChromeDriver(dcaps);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        try {
			driver.get("http://service.citroen.com/pages/index.jsp");
			WebElement userId = driver.findElementByName("userid");
			WebElement pwd = driver.findElementByName("password");
			WebElement button = driver.findElementById("btsubmit");
			userId.sendKeys("AC59853388");
			pwd.sendKeys("123456");
			button.submit();
            WebElement lang = driver.findElementByXPath("//*[@id='lg']");
            lang.click();
            lang = driver.findElementByXPath("//*[@id='zh_CN']");
            lang.click();
            lang = driver.findElementByXPath("//*[@id='menu']/li[2]/a");
            lang.click();
            lang = driver.findElementByXPath("//*[@id='menu']/li[2]/ul/li/a");
            lang.click();
            lang = driver.findElementByXPath("/html/body");
//            lang = driver.findElementByXPath("//*[@id='subMenu0']/li[1]/a");
//            lang.click();
//            lang = driver.findElementByXPath("//*[@id='modele_a0']");
//            lang.click();
//           
            List<WebElement> list = driver.findElementsByXPath("//*[@id='vehiculeSelection']/tbody/tr[2]/td");
            for (int i =0;i <list.size();i++) {
            	List<WebElement> liList = driver.findElements(By.xpath("//*[@id='vehiculeSelection']/tbody/tr[2]/td["+(i+1)+"]/ul/li"));
            	for (WebElement webElementLi : liList) {
            		//点击一级菜单
            		WebElement aList = webElementLi.findElement(By.className("hackfontie"));
            		List<WebElement> List = null;
            		if(ElementExist(webElementLi,By.tagName("ul"))){
            			WebElement  ul = webElementLi.findElement(By.tagName("ul"));
            			List = ul.findElements(By.tagName("li"));
            		}
					//点击二级菜单
					if (null != List && List.size() > 0) {
						for (WebElement liwebElement : List) {
							aList.click();
							WebElement a = liwebElement.findElement(By.tagName("a"));
							a.click();
							driver.executeScript("document.getElementsByTagName('body')[0].scrollTop = 0;",lang);

						}
					} else {
						aList.click();
						driver.executeScript("document.getElementsByTagName('body')[0].scrollTop = 0;",lang);

					}
						
				}
            	
			}
            //外形
//            lang  = driver.findElementByXPath("//*[@id='B0D']");
//            lang.click();
//            lang = driver.findElementByXPath("//*[@id='B0D']/option[2]");
//            lang.click();
//            //发动机
//            lang = driver.findElementByXPath("//*[@id='B0F']");
//            lang.click();
//            lang = driver.findElementByXPath("//*[@id='B0F']/option[2]");
//            lang.click();
           // 表单赋值
//            lang = driver.findElementByXPath("//*[@id='targetLcdv']");
//            driver.executeScript("document.getElementById(\"targetLcdv\").value ='B0D=B0DA3&B0F=B0F6P'", lang);
            //确认按钮
//            lang = driver.findElementByXPath("//*[@id='btnValider']");
//            Thread.sleep(2000);
//            lang.click();
//            System.out.println(driver.getPageSource());
//            System.out.println(lang.getAttribute("href"));
//            System.out.println(driver.getPageSource());
//            driver.executeScript("goTo()", "docapv,10");
            
//            driver.get("http://service.citroen.com/docapv/vehCom.do?cat=VP01&vehCom=VCO0069&lcdv=B0D%3DB0DA3%26B0F%3DB0F6P");
//            WebElement form = driver.findElementById("frmLdp");
//            WebElement cat = form.findElement(By.id("cat"));
//            WebElement vehCom = form.findElement(By.id("vehCom"));
//            WebElement lcdv = form.findElement(By.id("lcdv"));
//            cat.sendKeys("VP01");
//            vehCom.sendKeys("VCO0069");
//            lcdv.sendKeys("B0D=B0DA3&B0F=B0F6P");
//            form.submit();
//            System.out.println(driver.getPageSource());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
//			driver.close();
		}
	}
	
	static boolean ElementExist (WebElement webElementLi,By Locator )  
	{  
	     try  
	     {  
	    	 webElementLi.findElement( Locator );  
	          return true;  
	     }  
	     catch(org.openqa.selenium.NoSuchElementException ex)  
	     {  
	          return false;  
	     }  
	}  
}
