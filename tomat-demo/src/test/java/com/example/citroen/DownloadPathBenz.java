package com.example.citroen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import com.example.demo.util.HttpServiceUtil;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Created by admin on 2017/8/16.
 */
public class DownloadPathBenz {


	public static void main(String args[]) {
		try {
			String s4 = "http://service.citroen.com/docapv/vehCom.do?cat=VP01&vehCom=VCO0069&lcdv=B0D%3DB0DA3%26B0F%3DB0F6P";
//			String loginUrl = "http://service.citroen.com/do/login";
//			Map<String, String> map = new HashMap<String, String>();  
//			map.put("userid", "AC59853388d");  
//			map.put("password", "123456");  
//			Connection.Response res = Jsoup.connect(loginUrl)
//				    .data(map)
//				    .method(Method.POST)
//				    .timeout(20000)
//				    .execute();
//		    System.out.println(res.cookies());
		    Map<String, String> map1 = new HashMap<String, String>();
//		    String [] str = getSessionId().split(",");
//		    map1.put("PSACountry", "CN");
//		    map1.put("CodeLanguePaysOI", "zh_CN");
//		    map1.put("PGAC59853388", "10");
//		    map1.put("DISPLAYDOCAC59853388", "1");
		    map1.put("_pk_id.65.196e", "706c2993d2c599f8.1504776405.5.1504855470.1504852457.");
		    map1.put("_pk_id.78.196e", "738bdbc5e4179323.1504854299.1.1504854331.1504854299.");
//		    map1.put("_pk_ses.65.196e", "*");
		    map1.put("BIGipServerNEWAPVPROI_OIN_CITROEN.app~NEWAPVPROI_OIN_CITROEN_pool","529563658.20480.0000" );
		    map1.put("JSESSIONID", "0000KUBfVstXfAid9HKnJdPx2AV:15171flud");
		    Map<String, String> header = new HashMap<String, String>();
		    header.put("Referer", "http://service.citroen.com/docapv/");
		    
			Document document333 = Jsoup.connect(s4).cookies(map1).headers(header).get();
			System.out.println(document333);
//			httpLogin();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		

	}
	
	@Test
	public void htmlUnit() throws Exception {
		String url = "http://service.citroen.com/pages/index.jsp";
		// 1创建WebClient
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		// 2 启动JS
		webClient.getOptions().setJavaScriptEnabled(true);
		// 3 禁用Css，可避免自动二次請求CSS进行渲染
		webClient.getOptions().setCssEnabled(false);
		// 4 启动客戶端重定向
		webClient.getOptions().setRedirectEnabled(true);
		// 5 js运行错誤時，是否拋出异常
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		// 6 设置超时
		webClient.getOptions().setTimeout(50000);
		webClient.getOptions().setUseInsecureSSL(true);//忽略ssl认证 
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());// 设置Ajax异步  
		// 等待JS驱动dom完成获得还原后的网页
		webClient.waitForBackgroundJavaScript(10000);
		// 登陆
		HtmlPage htmlPage = webClient.getPage(url);
//		System.out.println(Jsoup.parse((htmlPage.asXml())));
		HtmlForm form = htmlPage.getFormByName("loginForm"); 
		HtmlInput user = form.getInputByName("userid");
		HtmlInput password = form.getInputByName("password");
		DomElement button = htmlPage.getElementById("btsubmit");
		user.setValueAttribute("AC59853388");
		password.setValueAttribute("123456");
		HtmlPage page = button.click();
//		System.out.println(Jsoup.parse(page.asXml()));
//		WebRequest request = new WebRequest(new URL("http://service.citroen.com/docapv/vehCom.do?cat=VP01&vehCom=VCO0069&lcdv=B0D%3DB0DA3%26B0F%3DB0F6P"));
//		request.setAdditionalHeader("Referer", "http://service.citroen.com/docapv/");
//		htmlPage = webClient.getPage(request); 
		//获取车型
		ScriptResult t = page.executeJavaScript("validChoixLangue('zh_CN');");
		HtmlPage page2 = (HtmlPage)t.getNewPage();
		t = page2.executeJavaScript("goTo('/docapv/','10')");
		page2 = (HtmlPage)t.getNewPage();
		t = page2.executeJavaScript("selectLcdv(this)");
		page2 = (HtmlPage)t.getNewPage();
//		//form赋值
		form = page2.getFormByName("frmLdp");
		HtmlInput cat = form.getInputByName("cat");
		HtmlInput vehCom = form.getInputByName("vehCom");
		HtmlInput lcdv = form.getInputByName("lcdv");
		cat.setValueAttribute("VP01");	
		vehCom.setValueAttribute("VCO0069");
		lcdv.setValueAttribute("B0D=B0DA3&B0F=B0F6P");
		t = page2.executeJavaScript("valider()");
		WebWindow window = webClient.getCurrentWindow();
		Page page1 = window.getEnclosedPage();
		Document doc=Jsoup.parse(page1.getWebResponse().getContentAsString()); 
		System.out.println(doc);
	}
	
	private static String getSessionId() throws Exception{
		String loginUrl = "http://service.citroen.com/do/login";
		HttpClient httpclient = new DefaultHttpClient();  
		//设置登录参数  
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
        formparams.add(new BasicNameValuePair("userid", "AC59853388d"));  
        formparams.add(new BasicNameValuePair("password", "123456"));  
        UrlEncodedFormEntity entity1 = new UrlEncodedFormEntity(formparams, "UTF-8"); 
        HttpPost httpPost = new HttpPost(loginUrl);
        httpPost.setEntity(entity1); 
        HttpResponse response = httpclient.execute(httpPost);
        String set_cookie = response.getFirstHeader("Set-Cookie").getValue();  
        Header header [] = response.getHeaders("Set-Cookie");
        String date = header[1].getValue();
        date = date.substring(0,date.indexOf(";"));
        //打印Cookie值  
        System.out.println(set_cookie.substring(0,set_cookie.indexOf(";"))); 
        return set_cookie.substring(0,set_cookie.indexOf(";")).split("=")[1]+","+date.split("=")[1];
	}
	
	private static void httpLogin() throws Exception{
		String loginUrl = "http://service.citroen.com/do/login";
		HttpClient httpclient = new DefaultHttpClient();  
		//设置登录参数  
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
        formparams.add(new BasicNameValuePair("userid", "AC59853388d"));  
        formparams.add(new BasicNameValuePair("password", "123456"));  
        UrlEncodedFormEntity entity1 = new UrlEncodedFormEntity(formparams, "UTF-8"); 
        HttpPost httpPost = new HttpPost(loginUrl);
        httpPost.setEntity(entity1); 
        HttpResponse response = httpclient.execute(httpPost);
        String set_cookie = response.getFirstHeader("Set-Cookie").getValue();  
        
        //打印Cookie值  
        System.out.println(set_cookie.substring(0,set_cookie.indexOf(";"))); 
        httpclient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://service.citroen.com/docapv/vehCom.do?cat=VP01&vehCom=VCO0069&lcdv=B0D%3DB0DA3%26B0F%3DB0F6P");
        httpGet.setHeader("Cookie", "_pk_id.78.196e=98d5beaeaec9216f.1504691624.1.1504691689.1504691624.; BIGipServerNEWAPVPROI_OIN_CITROEN.app~NEWAPVPROI_OIN_CITROEN_pool=496009226.20480.0000; PSACountry=CN; CodeLanguePaysOI=zh_CN; PGAC59853388=10; DISPLAYDOCAC59853388=1; JSESSIONID=0000d-sdakICBFXUxrQy1NHLRx6:174fi5rf9; _pk_id.65.196e=9860c52688c74248.1504688864.3.1504752002.1504749927.; _pk_ses.65.196e=*"); 
        httpGet.setHeader("Referer","http://service.citroen.com/docpr/");
//        httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//        httpGet.setHeader("Accept-Encoding", "gzip, deflate");
//        httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
//        httpGet.setHeader("Connection", "keep-alive");
//        httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
        response = httpclient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        StringBuilder result = new StringBuilder();  
        if (entity != null) {  
            InputStream instream = entity.getContent();  
            BufferedReader br = new BufferedReader(new InputStreamReader(instream));  
            String temp = "";  
            while ((temp = br.readLine()) != null) {  
                String str = new String(temp.getBytes(), "utf-8");  
                result.append(str);  
            }  
        }  
        System.out.println(result);  
	}

    public static Document connectUrl(String url) {
        Document document = null;
        int cnt = 0;
        cnt++;
        if (url.contains(" ")) {
            url = url.replaceAll(" ", "");
        }
        try {
            document = Jsoup.parse(HttpServiceUtil.doGet(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return document;
    }

    //整合取值onclick方法
    public static String[] getValue(String onclick) {
        String substring = onclick.substring(onclick.indexOf("(") + 1, onclick.length() - 1);
        String[] split = substring.split("','");
        String substring1 = null;
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            if (s.contains("'")) {
                substring1 = s.substring(s.indexOf("'") + 1, s.length()).trim();
            } else {
                substring1 = s.trim();
            }
            split[i] = substring1;
        }
        return split;
    }

    //整合取值onclick方法
    public static String[] getValue2(String onclick) {
        String substring = onclick.substring(onclick.indexOf("(") + 1, onclick.length() - 1);
        String s2 = substring.replaceAll(",", "','");
        String[] split = s2.split("','");
        String substring1 = null;
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            if (s.contains("'")) {
                substring1 = s.substring(s.indexOf("'") + 1, s.length()).trim();
            } else {
                substring1 = s.trim();
            }
            split[i] = substring1.replaceAll("'", "").trim();
        }
        return split;
    }


}
