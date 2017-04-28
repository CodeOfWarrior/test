package com.echarging.web.timer;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.echarging.pojo.GatewayInfoPOJO;
import com.echarging.service.IChargeUtilService;
import com.echarging.util.GameGatewayCache;

public class ChargeGatewayJob extends QuartzJobBean{
	
	private Logger log = Logger.getLogger(ChargeGatewayJob.class);
	@Autowired
	private IChargeUtilService chargeUtilService;
	

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		
	}
	
	public void start(){
		long begin = System.currentTimeMillis();
		StringBuffer buffer = new StringBuffer();
		buffer.append("begin execute chargeGatewayJob");
		List<GatewayInfoPOJO> list = new ArrayList<GatewayInfoPOJO>();
		list = chargeUtilService.getGateway();
		for (int i = 0; i < list.size(); i++) {
			GatewayInfoPOJO pojo = list.get(i);
			GameGatewayCache.setGatewayId(pojo.getGameId()+""+pojo.getGatewayId(), pojo.getServerUrl());
		}
		buffer.append("execute chargeGatewayJob end;times="+(System.currentTimeMillis()-begin)+"millis");
		log.info(buffer.toString());
	}

	public IChargeUtilService getChargeUtilService() {
		return chargeUtilService;
	}

	public void setChargeUtilService(IChargeUtilService chargeUtilService) {
		this.chargeUtilService = chargeUtilService;
	}
	

}
