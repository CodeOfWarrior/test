package com.echarging.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.echarging.dao.dbutil.ObjectMapper;
import com.echarging.pojo.AppleStoreProductPOJO;

public class AppleStoreProductMapper implements ObjectMapper{

	public Object mapping(ResultSet rs) throws SQLException {
		AppleStoreProductPOJO pojo = new AppleStoreProductPOJO();
		pojo.setGameId(rs.getInt("game_id"));
		pojo.setProductId(rs.getString("product_id"));
		pojo.setChargeAmount(rs.getInt("charge_amount"));
		pojo.setChargeMoney(rs.getInt("charge_money"));
		return pojo;
	}

}
