package com.echarging.dao.dbutil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

public class DBConnection {
	@Autowired
	private DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	/**
	 * 获取数据库连接池连接
	 * @throws SQLException 
	 */
	protected Connection getConnection() throws SQLException{
		return this.dataSource.getConnection();
		
	}
	
	/**
	 * 关闭连接
	 * @throws SQLException 
	 */
	public void close(Connection conn,PreparedStatement ps ,ResultSet rs) throws SQLException{
		if(conn != null){
			conn.close();
		}
		if(ps != null){
			ps.close();
		}
		if(rs != null){
			rs.close();
		}
	}
	

}
