package com.echarging.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;

import net.sf.json.JSONObject;

public class Common {
	
	/**
	 * 公用生成JSON方法
	 * @param Map<String,Object> map
	 */
    public static String createJson(Map<String,Object> map){
    	JSONObject obj = JSONObject.fromObject(map);
    	return obj.toString();
    }
    
    public static String isSandbox(String str){
    	String s = new String(Base64.decodeBase64(str));
    	JSONObject obj = JSONObject.fromObject(s);
    	System.out.println(obj.get("environment"));
    	return null;
    }
    
    /**
     * 验证key值是否正确
     */
    public static boolean validateKey(String param,String key){
    	if(MD5.getMD5(param).equals(key)){
    		return true;
    	}else{
    		return false;
    	}
    }
    /**
     * 获取google支付状态
     * @param String signtrueData
     */
    public static String getGooglePaySate(String signtrueData){
    	JSONObject json = JSONObject.fromObject(signtrueData);
    	return json.get("purchaseState").toString();
    }
    
    public static List<String> getMolParam(){
    	List<String> list = new ArrayList<String>();
    	list.add("applicationCode");
    	list.add("referenceId");
    	list.add("version");
    	list.add("amount");
    	list.add("currencyCode");
    	list.add("paymentId");
    	list.add("paymentStatusCode");
    	list.add("paymentStatusDate");
    	list.add("channelId");
    	list.add("customerId");
//    	list.add("VirtualCurrencyAmount");
    	return list;
    }
    public static String getIP(String url){
    	Pattern p=Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
    	Matcher m=p.matcher(url);
    	return m.group();
    }
}
