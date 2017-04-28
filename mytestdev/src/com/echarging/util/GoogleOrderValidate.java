package com.echarging.util;

public class GoogleOrderValidate {
	
	public static boolean validateOrder(String signtureData,String signture){
		return RSASignature.doCheck(signtureData, signture);
	}

}
