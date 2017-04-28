package com.echarging.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface IChargingService {
	
	/**
	 * 充值下订单
	 * @param String passportName 充值玩家账号
	 * @param int    gameId		  充值游戏ID
	 * @param int    gatewayId    充值网关ID
	 * @param int    channelId 1 google 2 苹果 3 mol 4表示onestore
	 * @param double chargeMoney  充值金额
	 * @param double chargeAmount 充值元宝数
	 * @param String productId    产品ID
	 * @param String key
	 * @return Map<String,Object>
	 * 
	 */
	public Map<String,Object> chargeOrder(String passportName,int gameId,int gatewayId,int channelId,double chargeMoney,double chargeAmount,
			String productId,String code,String key);
	/**
	 * 充值回调成功
	 * @param Long chargeId
	 * @param String token
	 * @param int    channelId 1 google 2 苹果 3 mol 4 onestore
	 * @param String key
	 * @return Map<String,Object>
	 */
	public Map<String,Object> charge(long chargeId,String token,int channelId,String key);
	/**
	 * googlepay充值回调
	 */
	public Map<String,Object> chargeByGoogle(long chargeId,String signtureData,String singn,String key);
	
	/**
	 * onestore充值回调
	 */
	public Map<String,Object> chargeByOneStore(HttpServletRequest request);
	/**
	 * appstore充值回调
	 */
	public Map<String,Object> chargeByApple(long chargeId,String receiptData,int type,String key);
	/**
	 * 通过游戏ID获取产品列表
	 */
	public Map<String,Object> getProductList(int gameId,String key);
	/**
	 * 获取所有未推送成功的订单
	 */
	public Map<String,Object> getPushOrder();
	/**
	 * 执行推更新推送结果 state = 2
	 */
	public Map<String,Object> updateSate(long chargeId);
	/**
	 * 通过游戏ID和产品ID获取充值元宝数
	 */
	public int getChargeAmount(int gameId,String productId);
	/**
	 * 通过游戏ID和充值订单号获取物品代码
	 */
	public String getCode(int gameId,long chargeId);
	/**
	 * mol充值回调
	 */
	public Map<String,Object> chargeByMol(HttpServletRequest request);
	
	/**
	 * 获取充值配置信息开关
	 */
	public Map<String,Object> getChargeConfig(int gameId,int channelId,String key);

}
