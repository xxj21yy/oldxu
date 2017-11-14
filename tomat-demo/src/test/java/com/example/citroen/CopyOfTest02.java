package com.example.citroen;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.example.demo.util.HttpServiceUtil;

/**
 * Created by Administrator on 2017/9/11.
 */
public class CopyOfTest02 {
	static AtomicInteger count = new AtomicInteger(1);
    @Test
    public void test011() throws Exception {
    	byte[] data = HttpServiceUtil.downloadPic("http://service.citroen.com/docapv/resources/4.24.8/AC/img/pr/07/0009/00091507.jpg");
    	System.out.println(new String(data));
    }
}
