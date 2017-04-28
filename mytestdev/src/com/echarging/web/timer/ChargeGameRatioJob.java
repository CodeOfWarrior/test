package com.echarging.web.timer;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.echarging.pojo.GameRatioPOJO;
import com.echarging.service.IChargeUtilService;
import com.echarging.util.GameRatioCache;

public class ChargeGameRatioJob extends QuartzJobBean{
	
	private Logger log = Logger.getLogger(ChargeGameRatioJob.class);

	@Autowired
	private IChargeUtilService chargeUtilService;
	
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		
	}
	
	public void start(){
		StringBuffer buffer = new StringBuffer();
		long begin = System.currentTimeMillis();
		buffer.append("begin execute getGameRatioJob");
		List<GameRatioPOJO> list = new ArrayList<GameRatioPOJO>();
		list = chargeUtilService.getGameRatio();
		for (int i = 0; i < list.size(); i++) {
			GameRatioPOJO pojo = list.get(i);
			GameRatioCache.addGameRatio(pojo.getGameId(), pojo.getGameRatio());
		}
		buffer.append("execute end;times="+(System.currentTimeMillis()-begin)+"millis");
		log.info(buffer.toString());
	}

	public IChargeUtilService getChargeUtilService() {
		return chargeUtilService;
	}

	public void setChargeUtilService(IChargeUtilService chargeUtilService) {
		this.chargeUtilService = chargeUtilService;
	}
}
