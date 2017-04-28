package com.echarging.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.echarging.dao.IChargingDao;
import com.echarging.dao.dbutil.JDBCUtilTemplate;
import com.echarging.mapper.AppleStoreProductMapper;
import com.echarging.mapper.ChargeOrderMapper;
import com.echarging.pojo.AppleStoreProductPOJO;
import com.echarging.pojo.ChargeOrderPOJO;
import com.echarging.util.Constant;

@Repository("chargingDaoImpl")
public class ChargingDaoImpl extends JDBCUtilTemplate implements IChargingDao {

	private Logger log = Logger.getLogger(ChargingDaoImpl.class);
	public Map<String, Object> chargeOrder(String passportName, int gameId, int gatewayId, int channelId,double chargeMoney,
			double chargeAmount,String code) {
		int result = Constant.SUCCESS;
		Map<String,Object> map = new HashMap<String,Object>();
		long orderId = this.getID("chargeId");
		map.put("orderId", orderId);
		String sql = "insert into charge_log_charge_common(charge_id,passport_name,game_id,gateway_id,charge_time, "+
                 "create_time,charge_money,charge_amount,deal_state,deal_time,charge_channel_id) values(?,?,?,?,?,?,?,?,?,?,?)";
		Object [] paramObj = new Object []{orderId,passportName,gameId,gatewayId,new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()),
				chargeMoney,chargeAmount,0,new Timestamp(System.currentTimeMillis()),channelId};
		if(!this.update(sql, paramObj)){
			result = Constant.ERROR_DB_UPDATE;
			log.error("execute insert charge_log_charge_common error!");
		}else{
			sql = "insert into charge_game_code(charge_id,game_id,code) values(?,?,?)";
			Object [] param = new Object[]{orderId,gameId,code};
			if(!this.update(sql, param)){
				result = Constant.ERROR_DB_UPDATE;
				log.error("execute insert charge_game_code error!");
			}
		}
		map.put("result", result);
		return map;
	}

	public Map<String, Object> charge(long chargeId) {
		int result = Constant.SUCCESS;
		Map<String,Object> map = new HashMap<String,Object>();
		String sql = "update charge_log_charge_common set deal_state = 2, deal_time = ? where deal_state = 1 and charge_id = ?";
		Object [] paramObj = new Object[]{Calendar.getInstance().getTime(),chargeId};
		if(!this.update(sql, paramObj)){
			result = Constant.ERROR_DB_UPDATE;
			log.error("execute update charge_log_charge_common = 2 error!");
		}
		map.put("result", result);
		return map;
	}

	public boolean validateUserExist(String passportName) {
		return this.userExist(passportName);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> updateOrderSate(long orderId) {
		Map<String,Object> map = new HashMap<String,Object>();
		int result = Constant.SUCCESS;
		/**
		 * 判断订单号是否存在
		 */
		String sql = "select charge_id,passport_name,game_id,gateway_id,charge_amount,deal_state from charge_log_charge_common where charge_id = ?";
		Object [] paramObj = new Object[]{orderId};
		List<ChargeOrderPOJO> list = new ArrayList<ChargeOrderPOJO>();
		list = (List<ChargeOrderPOJO>) this.query(sql, paramObj, new ChargeOrderMapper());
		if(list.size()<=0){
			map.put("result", Constant.ERROR_ORDER_EXIST);
			return map;
		}
		ChargeOrderPOJO pojo = list.get(0);
		map.put("value", pojo);
		if(pojo.getDealState() == 1 || pojo.getDealState() == 2){
			map.put("result", result);
			return map;
		}
		//map.put("value", list);
		//判断该订单是否已经退送成功
		sql = "update charge_log_charge_common set deal_state = 1, charge_time = ? where deal_state = 0 and charge_id = ?";
		Object [] param = new Object[]{new Timestamp(System.currentTimeMillis()),orderId};
		if(!this.update(sql, param)){
			result = Constant.ERROR_DB_UPDATE;
			log.error("execute update charge_log_charge_common = 1 error!");
		}
		map.put("result", result);
		return map;
	}

	public Map<String, Object> chargeSuccess(long orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<AppleStoreProductPOJO> getProductList(int gameId) {
		String sql = "select game_id,product_id,charge_amount,charge_money from charge_appstore_product where game_id = ?";
		Object [] paramObj = new Object[]{gameId};
		List<AppleStoreProductPOJO> list = new ArrayList<AppleStoreProductPOJO>();
		list = (List<AppleStoreProductPOJO>) this.query(sql, paramObj, new AppleStoreProductMapper());
		return list;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getPushOrder() {
		String sql = "select charge_id,passport_name,game_id,gateway_id,charge_amount,deal_state from charge_log_charge_common where deal_state = 1";
		Map<String,Object> map = new HashMap<String, Object>();
		List<ChargeOrderPOJO> list = new ArrayList<ChargeOrderPOJO>();
		Object [] paramObj = new Object[]{};
		list = (List<ChargeOrderPOJO>) this.query(sql, paramObj, new ChargeOrderMapper());
		map.put("orderList", list);
		return map;
	}

	public int getChargeAmount(int gameId, String productId) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select charge_amount from charge_appstore_product where game_id = ? and product_id = ?";

		int chargeAmount = 0;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, gameId);
			ps.setString(2, productId);
			rs = ps.executeQuery();
			while (rs.next()) {	
				chargeAmount = rs.getInt("charge_amount");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
			if(ps != null){
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return chargeAmount;
	}

	public String getCode(int gameId, long chargeId) {

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select code from charge_game_code where charge_id = ? and game_id = ?";

		String code = "";
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setLong(1, chargeId);
			ps.setInt(2, gameId);
			rs = ps.executeQuery();
			while (rs.next()) {	
				code = rs.getString("code");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
			if(ps != null){
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return code;
	
	}

	public int getChargeConfig(int gameId, int channelId) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select state from charge_config where channel_id = ? and game_id = ?";

		int  state = 0;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setLong(1, channelId);
			ps.setInt(2, gameId);
			rs = ps.executeQuery();
			while (rs.next()) {	
				state = rs.getInt("state");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
			if(ps != null){
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return state;
	}

	public int recordAppChargeInfo(long chargeId, String transactionId, String original_purchase_date_pst,
			String purchase_date_ms, String unique_identifier, String original_transaction_id, String bvrs,
			String quantity, String unique_vendor_identifier, String item_id, String product_id, String purchase_date,
			String original_purchase_date, String purchase_date_pst, String bid, String original_purchase_date_ms,
			String status) {
	//验证该订单是否已经存在
	if(this.transactionIDExist(transactionId)){
		return Constant.ERROR_TRANSATION_ID;
	}
	//验证productId 是否存
	if(!this.productExist(product_id)){
		return Constant.ERROR_PRODUCT_ID;
	}
	String sql = "INSERT INTO charge_appstore_order\n" +
				"(transaction_id,\n" + 
				"charge_id,\n" + 
				"original_purchase_date_pst,\n" + 
				"purchase_date_ms,\n" + 
				"unique_identifier,\n" + 
				"original_transaction_id,\n" + 
				"bvrs,\n" + 
				"quantity,\n" + 
				"unique_vendor_identifier,\n" + 
				"item_id,\n" + 
				"product_id,\n" + 
				"purchase_date,\n" + 
				"original_purchase_date,\n" + 
				"purchase_date_pst,\n" + 
				"bid,\n" + 
				"original_purchase_date_ms,\n" + 
				"record_time,\n" + 
				"status)\n" + 
				"VALUES\n" + 
				"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Object [] paramObj = new Object []{transactionId,chargeId,original_purchase_date_pst,purchase_date_ms,unique_identifier,original_transaction_id,bvrs,
				quantity,unique_vendor_identifier,item_id,product_id,purchase_date,original_purchase_date,purchase_date_pst,bid,original_purchase_date_ms,
				new Timestamp(System.currentTimeMillis()),status};
		int result = 1;
		if(!this.update(sql, paramObj)){
			result = Constant.ERROR_DB_UPDATE;
			log.error("execute insert charge_appstore_order error!");
		}
		
		return result;
	}
	
	
	

}
