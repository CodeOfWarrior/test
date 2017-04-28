package com.echarging.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.echarging.dao.IChargeUtilDao;
import com.echarging.pojo.GameRatioPOJO;
import com.echarging.pojo.GatewayInfoPOJO;
import com.echarging.service.IChargeUtilService;
@Service("chargeUtilServiceImpl")
public class ChargeUtilServiceImpl implements IChargeUtilService{

	@Autowired
	private IChargeUtilDao chargeUtilDao;
	public List<GameRatioPOJO> getGameRatio() {
		return chargeUtilDao.getGameRatio();
	}

	public List<GatewayInfoPOJO> getGateway() {
		return chargeUtilDao.getGateway();
	}

	public IChargeUtilDao getChargeUtilDao() {
		return chargeUtilDao;
	}

	public void setChargeUtilDao(IChargeUtilDao chargeUtilDao) {
		this.chargeUtilDao = chargeUtilDao;
	}
	
	

}
