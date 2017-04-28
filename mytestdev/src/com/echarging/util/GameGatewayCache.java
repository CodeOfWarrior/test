package com.echarging.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GameGatewayCache {
	
	private static Map<String, Object> map = Collections.synchronizedMap(new HashMap<String, Object>());
	
	public static void setGatewayId(String key,Object value){
		map.put(key,value);
	}
	
	public static Object getGatewayId(String key){
		return map.get(key) == null?"":map.get(key);
	}
	public static void main(String[] args) {
		System.out.println(getGatewayId("1"));
	}

}
