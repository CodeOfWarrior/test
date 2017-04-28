package com.echarging.web.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.echarging.util.Common;

public class BaseController {
	
	protected void response(HttpServletResponse response,Map<String,Object> map){
		try {
			response.getWriter().println(Common.createJson(map));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void callbackResponse(HttpServletResponse response,String result){
		try {
			response.getWriter().println(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
