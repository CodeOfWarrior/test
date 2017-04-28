package com.echarging.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.echarging.service.IChargingService;
import com.echarging.web.form.ChargeFormBean;

@Controller
public class ChargingController extends BaseController{
	@Autowired
	private IChargingService chargingService;

	public IChargingService getChargingService() {
		return chargingService;
	}

	public void setChargingService(IChargingService chargingService) {
		this.chargingService = chargingService;
	}
	
	/**
	 * 充值下订单
	 */
	@RequestMapping("chargeOrder")
	public void chargeOrder(ChargeFormBean form ,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		map = chargingService.chargeOrder(form.getPassportName(), form.getGameId(),
				form.getGatewayId(), form.getChannelId(),form.getChargeMoney(),form.getChargeAmount(),form.getProductId(),form.getCode(),form.getKey());
		this.response(response, map);
	}
//	/**
//	 * 充值成功回调
//	 */
//	@RequestMapping("charge")
//	public void charge(ChargeFormBean form ,HttpServletResponse response){
//		Map<String,Object> map = new HashMap<String,Object>();
//		map = chargingService.charge(form.getChargeId(), form.getToken(),form.getChannelId(),form.getKey());
//		this.response(response, map);
//	}
	/**
	 * googlepay 支付成功回调
	 */
	@RequestMapping("chargeByGoogle")
	public void chargeByGoogle(ChargeFormBean form,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		map = chargingService.chargeByGoogle(form.getChargeId(), form.getSigntureData(),form.getSignture(),form.getKey());
		this.response(response, map);
	}
	/**
	 * apple 支付回调
	 */
	@RequestMapping("chargeByApple")
	public void chargeByApple(ChargeFormBean form,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		map = chargingService.chargeByApple(form.getChargeId(), form.getReceiptData(),form.getType(), form.getKey());
		this.response(response, map);
	}
	/**
	 * onestore 支付成功回调
	 */
	@RequestMapping("chargeByOneStore")
	public void chargeByOneStore(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		map = chargingService.chargeByOneStore(request);
		this.response(response, map);
	}
	/**
	 * mol充值回调信息
	 */
	@RequestMapping("molChargeCallback")
	public void chargeByMol(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		map = chargingService.chargeByMol(request);
		if(Integer.parseInt(map.get("result").toString()) > 0){
			this.callbackResponse(response, "200 OK");
		}else{
			this.callbackResponse(response, "error");
		}
	}
	/**
	 * 通过游戏ID获取产品列表
	 */
	@RequestMapping("getProductList")
	public void getProductList(ChargeFormBean form,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		map = chargingService.getProductList(form.getGameId(), form.getKey());
		this.response(response, map);
	}
	/**
	 * SDK充值配置菜单
	 */
	@RequestMapping("chargeConfig")
	public void chargeConfig(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		String gameId = request.getParameter("gameId");
		String channelId = request.getParameter("channelId");
		String key = request.getParameter("key");
		map = chargingService.getChargeConfig(Integer.parseInt(gameId), Integer.parseInt(channelId), key);
		this.response(response, map);
	}
}
