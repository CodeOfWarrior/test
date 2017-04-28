package com.echarging.dao;

import java.util.List;
import java.util.Map;

import com.echarging.pojo.AppleStoreProductPOJO;

public interface IChargingDao {
	/**
	 * 充值下订单
	 * @param String passportName 充值玩家账号
	 * @param int    gameId		  充值游戏ID
	 * @param int    gatewayId    充值网关ID
	 * @param double chargeMoney  充值金额
	 * @param double chargeAmount 充值元宝数
	 * @return Map<String,Object>
	 * 
	 */
	public Map<String,Object> chargeOrder(String passportName,int gameId,int gatewayId,int channelId,double chargeMoney,double chargeAmount,String code);
	/**
	 * 充值回调成功
	 * @param Long chargeId
	 * @return Map<String,Object>
	 */
	public Map<String,Object> charge(long chargeId);
	/**
	 * 判断该用户是否存在
	 */
	public boolean validateUserExist(String passportName);
	/**
	 * 更新用户的订单状态为第三方充值成功
	 */
	public Map<String,Object> updateOrderSate(long orderId);
	/**
	 * 当退送游戏服务器成功时更新订单状态
	 */
	public Map<String,Object> chargeSuccess(long orderId);
	/**
	 * 通过游戏id获取苹果游戏列表
	 */
	public List<AppleStoreProductPOJO> getProductList(int gameId);
	/**
	 * 获取所有未推送成功的订单
	 */
	public Map<String,Object> getPushOrder();
	/**
	 * 通过游戏ID和产品id获取充值金额
	 */
	public int getChargeAmount(int gameId,String productId);
	/**
	 * 通过游戏ID和充值订单号获取物品代码
	 */
	public String getCode(int gameId,long chargeId);
	/**
	 * 获取充值配置信息开关
	 */
	public int getChargeConfig(int gameId,int channelId);
	/**
	 * 苹果充值记录
	 */
	public int recordAppChargeInfo(long chargeId,String transactionId,String original_purchase_date_pst,String purchase_date_ms,String unique_identifier,
			String original_transaction_id,String bvrs,String quantity,String unique_vendor_identifier,String item_id,String product_id,String purchase_date,
			String original_purchase_date,String purchase_date_pst,String bid,String original_purchase_date_ms,String status);

}
