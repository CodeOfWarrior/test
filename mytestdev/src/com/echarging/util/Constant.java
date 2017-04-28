package com.echarging.util;

public class Constant {
	
	public static final int SUCCESS								= 1 ; 
	
	public static final int ERROR_DB_UPDATE						= -200;
	
	public static final int PARAM_ERROR			    			= -100;		 		//参数错误
	
	public static final int ERROR_PASSPORT_EXIST				= -108;				//用户不存在
	
	public static final int ERROR_GAME_RATIO					= -300;				//游戏充值比例没有配置
	
	public static final int ERROR_GAME_GATEWAY					= -301;				//游戏网关不存在
	
	public static final int ERROR_GOOGLE_VALIDATE				= -302;				//GOOGLE验证信息错误
	
	public static final int ERROR_GOOGLE_PAY					= -303;			    //GOOGLE订单没有支付
	
	public static final int ERROR_ORDER_EXIST					= -304;				//充值订单号不存在
	
	public static final int ERROR_APPLE_RESPONSE				= -305;				//苹果没有返回数据
	
	public static final int ERROR_APPLE_ORDER					= -306;				//苹果支付账单无效
	
	public static final int ERROR_APPSTORE_PRODUCTID			= -307;             //该渠道没有配置物品代码
	
//	public static final String MOL_SECRET_KEY					= "gQeFBlbQ55R6WQBW1fQdlUsoofmiROLP";				//MOL secret key
	public static final String MOL_SECRET_KEY					= "Iu0hK0UYOWMa91Y4g4E7s21hWXqvbS0N";				//正式key
	
	public static final int ERROR_TRANSATION_ID					= -308; 		   //订单已经存在
	
	public static final int ERROR_PRODUCT_ID					= -309;			   //product id 不存在
}
