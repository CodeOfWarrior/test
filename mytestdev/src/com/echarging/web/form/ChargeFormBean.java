package com.echarging.web.form;

public class ChargeFormBean {
	
	private long   chargeId;
	
	private String passportName;
	
	private int    gameId;
	
	private int    gatewayId;
	
	private int    channelId;
	
	private double chargeMoney;
	
	private double chargeAmount;
	
	private String signtureData;
	
	private String signture;
	
	private String receiptData;
	
	private String productId;
	
	private int    type;
	
	private String token;
	
	private String code;
	
	private String key;

	public long getChargeId() {
		return chargeId;
	}

	public void setChargeId(long chargeId) {
		this.chargeId = chargeId;
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

	public double getChargeMoney() {
		return chargeMoney;
	}

	public void setChargeMoney(double chargeMoney) {
		this.chargeMoney = chargeMoney;
	}

	public double getChargeAmount() {
		return chargeAmount;
	}

	public void setChargeAmount(double chargeAmount) {
		this.chargeAmount = chargeAmount;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSigntureData() {
		return signtureData;
	}

	public void setSigntureData(String signtureData) {
		this.signtureData = signtureData;
	}

	public String getSignture() {
		return signture;
	}

	public void setSignture(String signture) {
		this.signture = signture;
	}

	public String getReceiptData() {
		return receiptData;
	}

	public void setReceiptData(String receiptData) {
		this.receiptData = receiptData;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
	
}
