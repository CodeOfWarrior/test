package com.echarging.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GameRatioCache {
	
	private static Map<Integer,Integer> map = Collections.synchronizedMap(new HashMap<Integer,Integer>());
	
	public static void addGameRatio(int gameId,int ratio){
		map.put(gameId, ratio);
	}
	
	public static int getGameRatio(int gameId){
		
		return map.get(gameId)==null?0:map.get(gameId);
	}

}
