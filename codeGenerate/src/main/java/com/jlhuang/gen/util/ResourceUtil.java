package com.jlhuang.gen.util;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * 资源工具类
 */
public class ResourceUtil {
	
	private static ResourceBundle resource;
	private static Map<String, String> msgMap = new HashMap<String, String>();
	
	
    static{
        resource = ResourceBundle.getBundle("datasource");
    }

    /**
     * 读取配置文件内容
     * @param key
     * @return
     */
    public static String getProperty(String key){
    	System.out.println( "------------------------------------------------------");
    	System.out.println(key + "--------"+resource.getString(key));
    	System.out.println( "------------------------------------------------------");
        return resource.getString(key);
    }
    
    public static String getMessage(String key){
    	return  msgMap.get(key);
    }
    
}