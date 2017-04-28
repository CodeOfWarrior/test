package com.echarging.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.echarging.dao.IChargingDao;
import com.echarging.pojo.AppleStoreProductPOJO;
import com.echarging.pojo.ChargeOrderPOJO;
import com.echarging.service.IChargingService;
import com.echarging.service.pushservice.PushGameServerService;
import com.echarging.util.Common;
import com.echarging.util.Constant;
import com.echarging.util.GameGatewayCache;
import com.echarging.util.GameRatioCache;
import com.echarging.util.IOS_Verify;
import com.echarging.util.OneStoreVerify;
import com.echarging.util.RSASignature;
import com.echarging.util.ThreadPoolUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Service("chargingServiceImpl")
public class ChargingServiceImpl implements IChargingService{
	private static final Logger log = Logger.getLogger(ChargingServiceImpl.class);
	@Autowired
	private IChargingDao chargingDao;

	public IChargingDao getChargingDao() {
		return chargingDao;
	}

	public void setChargingDao(IChargingDao chargingDao) {
		this.chargingDao = chargingDao;
	}

	public Map<String, Object> chargeOrder(String passportName, int gameId, int gatewayId, int channelId,
			double chargeMoney, double chargeAmount, String productId,String code,String key) {
		Map<String,Object> map = new HashMap<String,Object>();
		//验证key值是否正确
		if(!Common.validateKey(passportName+gameId+gatewayId+channelId, key)){
			map.put("result", Constant.PARAM_ERROR);
			map.put("orderId", "");
			return map;
		}
		//判断用户是否存在
		if(!chargingDao.validateUserExist(passportName)){
			map.put("result", Constant.ERROR_PASSPORT_EXIST);
			map.put("orderId","");
			return map;
		}
		//判断游戏充值比例是否存在
		if(GameRatioCache.getGameRatio(gameId) == 0){
			map.put("result",Constant.ERROR_GAME_RATIO);
			map.put("orderId", "");
			return map;
		}
		//验证充值游戏的网关是否存在
		if(GameGatewayCache.getGatewayId(gameId+""+gatewayId).equals("")){
			 map.put("result",Constant.ERROR_GAME_GATEWAY);
			 map.put("orderId","");
		}
		if(channelId == 1 || channelId == 2||channelId ==4){
			chargeAmount = chargingDao.getChargeAmount(gameId, productId);
			if(chargeAmount == 0){
				map.put("result",Constant.ERROR_APPSTORE_PRODUCTID);
				map.put("orderId","");
				return map;
			}
			chargeMoney = chargeAmount/GameRatioCache.getGameRatio(gameId);
		}else if(channelId == 3){
			chargeAmount = chargingDao.getChargeAmount(gameId, productId);
			if(chargeAmount == 0){
				map.put("result",Constant.ERROR_APPSTORE_PRODUCTID);
				map.put("orderId","");
				return map;
			}
		}else{
			chargeAmount = chargeMoney*GameRatioCache.getGameRatio(gameId);
		}
		//获取充值订单
		map = chargingDao.chargeOrder(passportName, gameId, gatewayId, channelId, chargeMoney, chargeAmount,code);
		return map;
	}

	public Map<String, Object> charge(long chargeId, String token ,int channelId,String key) {
		return null;
	}

	public Map<String, Object> chargeByGoogle(long chargeId, String signtureData, String signtrue, String key) {
		Map<String,Object> map = new HashMap<String,Object>();
		//验证key值是否正确
		if(!Common.validateKey(chargeId+"", key)){
			 map.put("result", Constant.PARAM_ERROR);
			 return map;
		}
		//验签是否正确
		if(!RSASignature.doCheck(signtureData, signtrue)){
			map.put("result", Constant.ERROR_GOOGLE_VALIDATE);
			return map;
		}
		//判断google充值是否成功
		if(!Common.getGooglePaySate(signtureData).equals("0")){
			map.put("result", Constant.ERROR_GOOGLE_PAY);
			return map;
		}
		//验证订单号是否一致
		JSONObject j = JSONObject.fromObject(signtureData);
		String id = j.getString("developerPayload");
		if(Long.parseLong(id) == chargeId){
		    map = chargingDao.updateOrderSate(chargeId);
		}else{
			map = chargingDao.updateOrderSate(Long.parseLong(id));
		}
		if(Integer.parseInt(map.get("result").toString()) == Constant.SUCCESS){//更新成功
			//推送到游戏服务器
			ChargeOrderPOJO pojo = (ChargeOrderPOJO) map.get("value");
			JSONObject json = new JSONObject();
			json.put("orderId", pojo.getOrderId());
			json.put("passportName", pojo.getPassportName());
			json.put("gameId", pojo.getGameId());
			json.put("gatewayId", pojo.getGatewayId());
			json.put("chargeAmount", pojo.getChargeAmount());
			json.put("code", chargingDao.getCode(pojo.getGameId(), chargeId));
			//执行推送到游戏服务器信息
			ThreadPoolUtils.pool.execute(new PushGameServerService(GameGatewayCache.getGatewayId(pojo.getGameId()+""+pojo.getGatewayId()).toString(), json,
					chargingDao, chargeId));
			map.put("result","1");
			return map;
		}else{
			map.put("result",map.get("result"));
		}
		
		return map;
	}
	
	public Map<String, Object> chargeByOneStore(HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			String data = request.getParameter("data");
			String parameters = new String(org.apache.commons.codec.binary.Base64.decodeBase64(data));
			log.info("chargeByOneStore parameters:"+parameters);
			if (!TextUtils.isEmpty(parameters)) {
				JSONObject jsonObject= JSONObject.fromObject(parameters);
				String chargeId = jsonObject.getString("chargeId");
				String onestore_txid = jsonObject.getString("onestore_txid");
				String produtId = jsonObject.getString("produtId");
				String result = OneStoreVerify.buyAppVerify(jsonObject.toString(), "erify");
				
				JSONObject jsonObject2 = JSONObject.fromObject(result);
				int resultCode = jsonObject2.getInt("status");
				String status = jsonObject2.getString("detail");
				
				if (resultCode==0&&status.equals("0000")) {
					String product  = jsonObject2.getString("product");
					log.info("chargeByOneStore product:"+product);
					// 这里最好再添加一个验证和记录，这里要在数据库中记录onestore_txid/chargeId/product_id/log_time并对这个做是否重复性推送验证
					JSONObject jsonObject3 = JSONObject.fromObject(product);
					String resultProductId = jsonObject3.getString("product_id");
					String resultTid = jsonObject3.getString("tid");
					if (resultProductId.equals(produtId)&&resultTid.equals(onestore_txid)) {
						map = chargingDao.updateOrderSate(Long.valueOf(chargeId));
						if(Integer.parseInt(map.get("result").toString()) == Constant.SUCCESS){//更新成功
							//推送到游戏服务器
							ChargeOrderPOJO pojo = (ChargeOrderPOJO) map.get("value");
							JSONObject json = new JSONObject();
							json.put("orderId", pojo.getOrderId());
							json.put("passportName", pojo.getPassportName());
							json.put("gameId", pojo.getGameId());
							json.put("gatewayId", pojo.getGatewayId());
							json.put("chargeAmount", pojo.getChargeAmount());
							json.put("code", chargingDao.getCode(pojo.getGameId(), Long.valueOf(chargeId)));
							//执行推送到游戏服务器信息
							ThreadPoolUtils.pool.execute(new PushGameServerService(GameGatewayCache.getGatewayId(pojo.getGameId()+""+pojo.getGatewayId()).toString(), json,
									chargingDao, Long.valueOf(chargeId)));
							map.put("result","1");
							return map;
						}else{
							map.put("result",map.get("result"));
						}
					}
				}
			}else{
				map.put("result", Constant.PARAM_ERROR);//参数错误
				return map;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Map<String, Object> chargeByApple(long chargeId, String receiptData,int type,String key) {
		Map<String,Object> map = new HashMap<String,Object>();
		//验证key值是否正确
		if(!Common.validateKey(chargeId+"", key)){
			 map.put("result", Constant.PARAM_ERROR);
			 return map;
		}
		//验证苹果数据
		String verifyResult = null;
		String verifySate = "";
//		if(type == 1){//1 沙箱环境 2 正式环境 
//			verifySate = "sandbox";
//		}
		verifyResult = IOS_Verify.buyAppVerify(receiptData, verifySate);
		if(verifyResult == null){
			map.put("result", Constant.ERROR_APPLE_RESPONSE);
			return map;
		}else{
			JSONObject obj = JSONObject.fromObject(verifyResult);
			if(obj.getString("status").equals("21007")){
				verifyResult = IOS_Verify.buyAppVerify(receiptData, "sandbox");
				JSONObject obj1 = JSONObject.fromObject(verifyResult);
				if(obj1.getString("status").equals("0")){
					int ret = validateAppStore(obj1, chargeId);
					if( ret >=1){
						map = chargingDao.updateOrderSate(chargeId);
						if(Integer.parseInt(map.get("result").toString()) == Constant.SUCCESS){//更新成功
							//推送到游戏服务器
							ChargeOrderPOJO pojo = (ChargeOrderPOJO) map.get("value");
							JSONObject json = new JSONObject();
							json.put("orderId", pojo.getOrderId());
							json.put("passportName", pojo.getPassportName());
							json.put("gameId", pojo.getGameId());
							json.put("gatewayId", pojo.getGatewayId());
							json.put("chargeAmount", pojo.getChargeAmount());
							json.put("code", chargingDao.getCode(pojo.getGameId(), chargeId));
							//执行推送到游戏服务器信息
							ThreadPoolUtils.pool.execute(new PushGameServerService(GameGatewayCache.getGatewayId(pojo.getGameId()+""+pojo.getGatewayId()).toString(), json,
									chargingDao, chargeId));
							map.put("result","1");
							return map;
						}else{
							map.put("result",map.get("result"));
						}
					}else{
						map.put("result",ret);
						return map;
					}
					
				}else{
					map.put("result", Constant.ERROR_APPLE_ORDER);
					return map;
				}
			}else{
				if(obj.getString("status").equals("0")){
					int ret = validateAppStore(obj, chargeId);
					if(ret >=1){
						map = chargingDao.updateOrderSate(chargeId);
						if(Integer.parseInt(map.get("result").toString()) == Constant.SUCCESS){//更新成功
							//推送到游戏服务器
							ChargeOrderPOJO pojo = (ChargeOrderPOJO) map.get("value");
							JSONObject json = new JSONObject();
							json.put("orderId", pojo.getOrderId());
							json.put("passportName", pojo.getPassportName());
							json.put("gameId", pojo.getGameId());
							json.put("gatewayId", pojo.getGatewayId());
							json.put("chargeAmount", pojo.getChargeAmount());
							json.put("code", chargingDao.getCode(pojo.getGameId(), chargeId));
							//执行推送到游戏服务器信息
							ThreadPoolUtils.pool.execute(new PushGameServerService(GameGatewayCache.getGatewayId(pojo.getGameId()+""+pojo.getGatewayId()).toString(), json,
									chargingDao, chargeId));
							map.put("result","1");
							return map;
						}else{
							map.put("result",map.get("result"));
						}
					}else{
						map.put("result",ret);
						return map;
					}
					
				}else{
					map.put("result", Constant.ERROR_APPLE_ORDER);
					return map;
				}
			}
		}
		return map;
	}

	public Map<String, Object> getProductList(int gameId, String key) {
		//验证key值是否正确
		Map<String,Object> map = new HashMap<String,Object>();
		//验证key值是否正确
		if(!Common.validateKey(gameId+"", key)){
			 map.put("result", Constant.PARAM_ERROR);
			 return map;
		}
		List<AppleStoreProductPOJO> list = new ArrayList<AppleStoreProductPOJO>();
		list = chargingDao.getProductList(gameId);
		map.put("result",list);
		return map;
	}

	public Map<String, Object> getPushOrder() {
		Map<String,Object> map = new HashMap<String, Object>();
		map = chargingDao.getPushOrder();
		return map;
	}

	public Map<String, Object> updateSate(long chargeId) {
		Map<String,Object> map = new HashMap<String, Object>();
		map = chargingDao.charge(chargeId);
		return map;
	}

	public int getChargeAmount(int gameId, String productId) {
		return chargingDao.getChargeAmount(gameId, productId);
	}

	public String getCode(int gameId, long chargeId) {
		return chargingDao.getCode(gameId, chargeId);
	}

	public Map<String,Object> chargeByMol(HttpServletRequest request) {
		long begin = System.currentTimeMillis();
		String virtualCurrencyAmount = "";
		Map<String,Object> map = new HashMap<String,Object>();
		List<String> list = Common.getMolParam();
		Collections.sort(list);
		list.add("VirtualCurrencyAmount");
		StringBuffer buffer = new StringBuffer();
		StringBuffer param = new StringBuffer();
		virtualCurrencyAmount = request.getParameter("VirtualCurrencyAmount");
		log.info("virtualCurrencyAmount:"+virtualCurrencyAmount);
		for (int i = 0; i < list.size(); i++) {
			buffer.append(request.getParameter(list.get(i))==null?"":request.getParameter(list.get(i)));
			param.append(list.get(i)).append("=").append(request.getParameter(list.get(i))).append("&");
		}
		String singture = request.getParameter("signature");
		log.info("buffer:"+buffer);
		String requestParam = ("molcallbackinfo="+param.toString()+"signature="+singture);
		//校验singture
		if(!Common.validateKey(buffer.toString()+Constant.MOL_SECRET_KEY, singture)){
			map.put("result", Constant.PARAM_ERROR);
			requestParam = requestParam+"&result="+Constant.PARAM_ERROR+"&times="+(System.currentTimeMillis()-begin)+"millis";
			log.info(requestParam);
			return map;
		}else{
			String paymentStatusCode = request.getParameter("paymentStatusCode");
			if(paymentStatusCode.equals("00")){//充值成功
				map = chargingDao.updateOrderSate(Long.parseLong(request.getParameter("referenceId")));
				if(Integer.parseInt(map.get("result").toString()) == Constant.SUCCESS){//更新成功
					//推送到游戏服务器
					ChargeOrderPOJO pojo = (ChargeOrderPOJO) map.get("value");
					JSONObject json = new JSONObject();
					json.put("orderId", pojo.getOrderId());
					json.put("passportName", pojo.getPassportName());
					json.put("gameId", pojo.getGameId());
					json.put("gatewayId", pojo.getGatewayId());
					if(!virtualCurrencyAmount.equals("")&&!virtualCurrencyAmount.equals(null)){
							int chargeAmount =(int) Math.ceil(Float.valueOf(request.getParameter("VirtualCurrencyAmount")));
							json.put("chargeAmount", chargeAmount+"");
					}else{
							json.put("chargeAmount",pojo.getChargeAmount());
					}
					json.put("code", chargingDao.getCode(pojo.getGameId(), Long.parseLong(request.getParameter("referenceId"))));
					//执行推送到游戏服务器信息
					ThreadPoolUtils.pool.execute(new PushGameServerService(GameGatewayCache.getGatewayId(pojo.getGameId()+""+pojo.getGatewayId()).toString(), json,
							chargingDao, Long.parseLong(request.getParameter("referenceId"))));
					map.put("result","1");
					requestParam = requestParam+"&result="+1+"&times="+(System.currentTimeMillis()-begin)+"millis";
					log.info(requestParam);
					log.info(json);
					return map;
				}else{
					requestParam = requestParam+"&result="+map.get("result")+"&times="+(System.currentTimeMillis()-begin)+"millis";
					log.info(requestParam);
					map.put("result",map.get("result"));
					return map;
				}
				
			}else{
				requestParam = requestParam+"&result="+map.get("result")+"&times="+(System.currentTimeMillis()-begin)+"millis";
				log.info(requestParam);
				map.put("result", -1);
				return map;
			}
		}
	//	return null;
	}

	public Map<String,Object> getChargeConfig(int gameId, int channelId,String key) {
		int result = Constant.SUCCESS;
		Map<String,Object> map = new HashMap<String,Object>();
		//验证key是否正确
		if(!Common.validateKey(gameId+""+channelId, key)){
			map.put("result", Constant.PARAM_ERROR);
			return map;
		 }
		result = chargingDao.getChargeConfig(gameId, channelId);
		map.put("result",result);
		return map;
	}
	
	public int validateAppStore(JSONObject obj,long chargeId){
		JSONObject json = obj.getJSONObject("receipt");
		JSONObject o = JSONArray.fromObject(json.getString("in_app")).getJSONObject(0);
		String transaction_id = o.getString("transaction_id");
		String original_purchase_date_pst = o.getString("original_purchase_date_pst");
		String purchase_date_ms = o.getString("purchase_date_ms");
		String unique_identifier = " ";
		String original_transaction_id = o.getString("original_transaction_id");
		String bvrs = " ";
		String quantity = o.getString("quantity");
		String unique_vendor_identifier = "";
		String item_id = json.getString("app_item_id");
		String product_id = o.getString("product_id");
		String purchase_date = o.getString("purchase_date");
		String original_purchase_date = o.getString("original_purchase_date");
		String purchase_date_pst = o.getString("purchase_date_pst");
		String bid = " ";
		String original_purchase_date_ms = o.getString("original_purchase_date_ms");
		String status = obj.getString("status");
		int result = 0;
		result = chargingDao.recordAppChargeInfo(chargeId, transaction_id, original_purchase_date_pst, purchase_date_ms, unique_identifier, original_transaction_id, bvrs, quantity, unique_vendor_identifier, item_id, product_id, purchase_date, original_purchase_date,
				purchase_date_pst, bid, original_purchase_date_ms, status);
		return result;
		
	}
//	public static void main(String[] args) {
//		String sql = "{\"status\":0, \"environment\":\"Sandbox\", \"receipt\":{\"receipt_type\":\"ProductionSandbox\", \"adam_id\":0, \"app_item_id\":0,"
//				+ " \"bundle_id\":\"com.adfox.legendofbattleships\", \"application_version\":\"5.0.2\", \"download_id\":0,"
//				+ " \"version_external_identifier\":0, \"receipt_creation_date\":\"2016-07-18 13:03:01 Etc/GMT\","
//				+ " \"receipt_creation_date_ms\":\"1468846981000\", \"receipt_creation_date_pst\":\"2016-07-18 06:03:01 America/Los_Angeles\", "
//				+ " \"request_date\":\"2016-07-18 13:03:04 Etc/GMT\", \"request_date_ms\":\"1468846984825\", \"request_date_pst\":\"2016-07-18 06:03:04 America/Los_Angeles\", "
//				+ " \"original_purchase_date\":\"2013-08-01 07:00:00 Etc/GMT\", \"original_purchase_date_ms\":\"1375340400000\", \"original_purchase_date_pst\":\"2013-08-01 00:00:00 America/Los_Angeles\", "
//				+ "\"original_application_version\":\"1.0\", \"in_app\":[{\"quantity\":\"1\", \"product_id\":\"com.adfox.battleships.60\", \"transaction_id\":\"1000000224240990\", "
//				+ "\"original_transaction_id\":\"1000000224240990\", \"purchase_date\":\"2016-07-18 13:03:00 Etc/GMT\", \"purchase_date_ms\":\"1468846980000\", "
//				+ "\"purchase_date_pst\":\"2016-07-18 06:03:00 America/Los_Angeles\", \"original_purchase_date\":\"2016-07-18 13:03:00 Etc/GMT\", \"original_purchase_date_ms\":\"1468846980000\", \"original_purchase_date_pst\":\"2016-07-18 06:03:00 America/Los_Angeles\", "
//				+ "\"is_trial_period\":\"false\"}]}}";
//		JSONObject obj = JSONObject.fromObject(sql);
//		ChargingServiceImpl c = new ChargingServiceImpl();
//		c.validateAppStore(obj,11111);
//	}



}
