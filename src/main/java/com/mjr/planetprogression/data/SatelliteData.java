package com.mjr.planetprogression.data;

public class SatelliteData {
	public int type;
	public String uuid;
	public int dataAmount;
	public static final int MAX_DATA = 10000;
	
	public SatelliteData(int type, String uuid, int dataAmount) {
		super();
		this.type = type;
		this.uuid = uuid;
		this.dataAmount = dataAmount;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getDataAmount() {
		return dataAmount;
	}

	public void setDataAmount(int dataAmount) {
		this.dataAmount = dataAmount;
	}

	public int getMAX_DATA() {
		return MAX_DATA;
	}
}
