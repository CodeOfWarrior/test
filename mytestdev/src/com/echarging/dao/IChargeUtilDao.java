package com.echarging.dao;

import java.util.List;

import com.echarging.pojo.GameRatioPOJO;
import com.echarging.pojo.GatewayInfoPOJO;

public interface IChargeUtilDao {
	/**
	 * 获取所有游戏的充值比例信息
	 */
	public List<GameRatioPOJO> getGameRatio();
	
	/**
	 * 获取所有的网关信息
	 */
	public List<GatewayInfoPOJO> getGateway();

}
