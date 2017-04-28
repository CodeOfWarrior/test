package com.echarging.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.echarging.dao.IChargeUtilDao;
import com.echarging.dao.dbutil.JDBCUtilTemplate;
import com.echarging.mapper.GameRatioMapper;
import com.echarging.mapper.GatewayInfoMapper;
import com.echarging.pojo.GameRatioPOJO;
import com.echarging.pojo.GatewayInfoPOJO;
@Repository("chargeUtilDaoImpl")
public class ChargeUtilDaoImpl extends JDBCUtilTemplate implements IChargeUtilDao{

	@SuppressWarnings("unchecked")
	public List<GameRatioPOJO> getGameRatio() {
		String sql = "select game_id,charge_ratio from charge_game_ratio";
		List<GameRatioPOJO> list = new ArrayList<GameRatioPOJO>();
		Object [] paramObj = new Object[]{};
		list = (List<GameRatioPOJO>) this.query(sql, paramObj, new GameRatioMapper());
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<GatewayInfoPOJO> getGateway() {
		String sql = "select game_id,gateway_id,gateway_name,server_address from sys_gateway_info where end_time >= now() and state = 1";
		List<GatewayInfoPOJO> list = new ArrayList<GatewayInfoPOJO>();
		Object [] paramObj = new Object []{};
		list = (List<GatewayInfoPOJO>) this.query(sql, paramObj, new GatewayInfoMapper());
		return list;
	}

}
