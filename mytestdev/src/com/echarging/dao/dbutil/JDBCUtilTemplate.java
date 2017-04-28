package com.echarging.dao.dbutil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class JDBCUtilTemplate extends DBConnection{
	private static final Logger log = Logger.getLogger(JDBCUtilTemplate.class);
	/**
	 * insert/update/delete 操作
	 * @param String  sql 
	 *        sql语句
	 * @param Object  paramObj[]
	 *        参数数组
	 * @return boolean
	 */
	public boolean update(String sql,Object paramObj[]){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean result = false;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < paramObj.length; i++) {
				ps.setObject(i+1, paramObj[i]);
			}
			int i = ps.executeUpdate();
			if( i > 0){
				result = true;
				conn.commit();
			}else{
				conn.rollback();
			}
		} catch (SQLException e) {
			log.error("database connecton exception:"+e.getMessage());
			e.printStackTrace();
		}finally{
			try {
				 this.close(conn, ps, null);
			} catch (SQLException e) {
				log.error("dbconnection close error"+e.getMessage());
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 数据查询返回list对象
	 * @param String sql
	 *        sql语句
	 * @param Object paramObject[]
	 * @param ObjectMapper mapper
	 * @return List<?> list 
	 */
	public List<? extends Object> query(String sql,Object paramObj[],ObjectMapper mapper){
		Connection conn = null;
		PreparedStatement ps = null;
		List<Object> list = new ArrayList<Object>();
		ResultSet rs = null;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < paramObj.length; i++) {
				ps.setObject(i+1, paramObj[i]);
			}
			rs = ps.executeQuery();
			while(rs.next()){
				list.add(mapper.mapping(rs));
			}
		} catch (SQLException e) {
			log.error("database connecton exception:"+e.getMessage());
			e.printStackTrace();
		}finally{
			try {
				this.close(conn, ps, rs);
			} catch (SQLException e) {
				log.error("dbconnection close error:"+e.getMessage());
				e.printStackTrace();
			}
		}
		return list;
	}
	/**
	 * 获取ID
	 */
	public long getID(String name){
		long id = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement("select getSequence('"+name+"')");
			rs = ps.executeQuery();
			while(rs.next()){
				id = rs.getLong(1);
			}
		} catch (SQLException e) {
			log.error("database connecton exception:"+e.getMessage());
			e.printStackTrace();
		}finally{
			try {
				this.close(conn, ps, rs);
			} catch (SQLException e) {
				log.error("dbconnection close error:"+e.getMessage());
				e.printStackTrace();
			}
		}
		return id;
	}
	
	/**
	 * 判断用户是否存在
	 */
	public boolean userExist(String passportName){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int result = 0;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement("select count(1) from sys_passport where passport_name = ?");
			ps.setString(1, passportName);
			rs = ps.executeQuery();
			while(rs.next()){
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			log.error("database connecton exception:"+e.getMessage());
			e.printStackTrace();
		}finally{
			try {
				this.close(conn, ps, rs);
			} catch (SQLException e) {
				log.error("dbconnection close error:"+e.getMessage());
				e.printStackTrace();
			}
		}
		if(result == 0){
			return false;
		}else{
			return true;
		}
		
	}
	protected boolean transactionIDExist(String transactionId){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int result = 0;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement("select count(1) from charge_appstore_order where transaction_id = ?");
			ps.setString(1, transactionId);
			rs = ps.executeQuery();
			while(rs.next()){
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			log.error("database connecton exception:"+e.getMessage());
			e.printStackTrace();
		}finally{
			try {
				this.close(conn, ps, rs);
			} catch (SQLException e) {
				log.error("dbconnection close error:"+e.getMessage());
				e.printStackTrace();
			}
		}
		if(result == 0){
			return false;
		}else{
			return true;
		}
	}
	protected boolean productExist(String productId){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int result = 0;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement("select count(1) from charge_appstore_product where product_id = ?");
			ps.setString(1, productId);
			rs = ps.executeQuery();
			while(rs.next()){
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			log.error("database connecton exception:"+e.getMessage());
			e.printStackTrace();
		}finally{
			try {
				this.close(conn, ps, rs);
			} catch (SQLException e) {
				log.error("dbconnection close error:"+e.getMessage());
				e.printStackTrace();
			}
		}
		if(result == 0){
			return false;
		}else{
			return true;
		}
	}
	

}
