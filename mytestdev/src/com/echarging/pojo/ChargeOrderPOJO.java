package com.echarging.pojo;

public class ChargeOrderPOJO {
	
	private long       orderId;
	
	private String     passportName;
	
	private int        gameId;
	
	private int        gatewayId;
	
	private int        chargeAmount;
	
	private int		   dealState;

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getPassportName() {
		return passportName;
	}

	public void setPassportName(String passportName) {
		this.passportName = passportName;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getGatewayId() {
		return gatewayId;
	}

	public void setGatewayId(int gatewayId) {
		this.gatewayId = gatewayId;
	}

	public int getChargeAmount() {
		return chargeAmount;
	}

	public void setChargeAmount(int chargeAmount) {
		this.chargeAmount = chargeAmount;
	}

	public int getDealState() {
		return dealState;
	}

	public void setDealState(int dealState) {
		this.dealState = dealState;
	}

}
