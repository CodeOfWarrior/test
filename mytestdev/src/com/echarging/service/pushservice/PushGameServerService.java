package com.echarging.service.pushservice;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.echarging.dao.IChargingDao;
import com.echarging.util.PushGameServerUtil;

import net.sf.json.JSONObject;

public class PushGameServerService implements Runnable {
	private static final Logger log = Logger.getLogger(PushGameServerService.class);
	private IChargingDao chargeDao;
	private String       url;
	private JSONObject   obj;
	private long         chargeId;
	public PushGameServerService(String url,JSONObject obj,IChargingDao chargeDao,long chargeId){
		this.url = url;
		this.obj = obj;
		this.chargeDao = chargeDao;
		this.chargeId  = chargeId;
	}
	public void run() {
		String result = "";
		long begin = System.currentTimeMillis();
		StringBuffer buffer = new StringBuffer();
		buffer.append("push info=").append(obj.toString()).append(";");
		try {
			result = PushGameServerUtil.sendMsgJson(url,obj);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		buffer.append("response="+result.toString()).append("times=").append(System.currentTimeMillis()-begin).append("millis");
		Map<String,Object> map = new HashMap<String, Object>();
		log.info(buffer.toString());
		//判断支付是否成功
		if(result != null && !"".equals(result) && result.equals("success")){
			map = chargeDao.charge(chargeId);
			log.info("update order state response="+map.get("result"));
		}
	}
}
