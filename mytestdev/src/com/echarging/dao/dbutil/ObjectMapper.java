package com.echarging.dao.dbutil;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ObjectMapper {
	
	public Object mapping(ResultSet rs) throws SQLException;

}
