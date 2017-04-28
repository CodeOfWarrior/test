package com.echarging.web.timer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.echarging.pojo.ChargeOrderPOJO;
import com.echarging.service.IChargeUtilService;
import com.echarging.service.IChargingService;
import com.echarging.util.GameGatewayCache;
import com.echarging.util.PushGameServerUtil;

import net.sf.json.JSONObject;

public class PushGameServerJob extends QuartzJobBean{
	private static final Logger log = Logger.getLogger(PushGameServerJob.class);
	@Autowired
	private IChargingService     chargingService;
	@Autowired
	private IChargeUtilService   chargeUtilService;
	
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		
	}
	
	public void start(){
		
		Map<String, Object> map = new HashMap<String, Object>();
		map = chargingService.getPushOrder();
		if(!map.isEmpty()){
			List<ChargeOrderPOJO> list = (List<ChargeOrderPOJO>) map.get("orderList");
			for (int i = 0; i < list.size(); i++) {
				StringBuffer buffer = new StringBuffer();
				long begin = System.currentTimeMillis();
				//推送到游戏服务器
				ChargeOrderPOJO pojo = list.get(i);
				JSONObject json = new JSONObject();
				json.put("orderId", pojo.getOrderId());
				json.put("passportName", pojo.getPassportName());
				json.put("gameId", pojo.getGameId());
				json.put("gatewayId", pojo.getGatewayId());
				json.put("chargeAmount", pojo.getChargeAmount());
				json.put("code", chargingService.getCode(pojo.getGameId(), pojo.getOrderId()));
				buffer.append("push info="+json.toString()).append(";");
				String result = "";
				try {
					result = PushGameServerUtil.sendMsgJson(GameGatewayCache.getGatewayId(pojo.getGameId()+""+pojo.getGatewayId()).toString(),json);
				} catch (Exception e) {
					log.error(e.getMessage());
					e.printStackTrace();
				}
				buffer.append("reponse="+result+";").append("times=").append(System.currentTimeMillis()-begin).append("millis");
				log.info(buffer.toString());
				//判断支付是否成功
				if(result != null && !"".equals(result) && result.equals("success")){
					map = chargingService.updateSate(pojo.getOrderId());
					log.info("update order state response="+map.get("result"));
				}
			}
		}
	}

	public IChargingService getChargingService() {
		return chargingService;
	}

	public void setChargingService(IChargingService chargingService) {
		this.chargingService = chargingService;
	}

	public IChargeUtilService getChargeUtilService() {
		return chargeUtilService;
	}

	public void setChargeUtilService(IChargeUtilService chargeUtilService) {
		this.chargeUtilService = chargeUtilService;
	}

}
