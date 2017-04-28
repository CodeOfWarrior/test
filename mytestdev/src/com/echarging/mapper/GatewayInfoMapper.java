package com.echarging.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.echarging.dao.dbutil.ObjectMapper;
import com.echarging.pojo.GatewayInfoPOJO;

public class GatewayInfoMapper implements ObjectMapper{

	public Object mapping(ResultSet rs) throws SQLException {
		GatewayInfoPOJO pojo = new GatewayInfoPOJO();
		pojo.setGameId(rs.getInt("game_id"));
		pojo.setGatewayId(rs.getInt("gateway_id"));
		pojo.setGatewayName(rs.getString("gateway_name"));
		pojo.setServerUrl(rs.getString("server_address"));
		return pojo;
	}

}
