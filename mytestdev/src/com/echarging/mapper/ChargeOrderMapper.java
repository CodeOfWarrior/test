package com.echarging.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.echarging.dao.dbutil.ObjectMapper;
import com.echarging.pojo.ChargeOrderPOJO;

public class ChargeOrderMapper implements ObjectMapper{

	public Object mapping(ResultSet rs) throws SQLException {
		ChargeOrderPOJO pojo = new ChargeOrderPOJO();
		pojo.setOrderId(rs.getLong("charge_id"));
		pojo.setGameId(rs.getInt("game_id"));
		pojo.setGatewayId(rs.getInt("gateway_id"));
		pojo.setPassportName(rs.getString("passport_name"));
		pojo.setChargeAmount(rs.getInt("charge_amount"));
		pojo.setDealState(rs.getInt("deal_state"));
		return pojo;
	}

}
