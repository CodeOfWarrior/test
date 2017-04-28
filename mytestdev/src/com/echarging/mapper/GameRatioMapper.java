package com.echarging.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.echarging.dao.dbutil.ObjectMapper;
import com.echarging.pojo.GameRatioPOJO;

public class GameRatioMapper implements ObjectMapper{

	public Object mapping(ResultSet rs) throws SQLException {
		GameRatioPOJO pojo = new GameRatioPOJO();
		pojo.setGameId(rs.getInt("game_id"));
		pojo.setGameRatio(rs.getInt("charge_ratio"));
		return pojo;
	}

}
