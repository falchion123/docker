package com.yundaishidai.utils;

import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * @author rick
 */
public class PropertiesUtil {
	private static final String path = "config.properties";
	public static Properties props = null;
	
	static {
		loadProp(path);
	}

	public static synchronized void loadProp(String path){
			try {
				if(null == props){
					props = new Properties();
				}
		        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
		        props.load(inputStream);
		        inputStream.close();
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
	
	}
	

	
	/**
	 * 获取指定key的value
	 * 
	 * @param key
	 * @return
	 */
	public static String getKey(String key) {
		
		return props.getProperty(key);
	}
}