package com.mjr.planetprogression.data;

public class SatelliteData {
	public int type;
	public String uuid;
	
	public SatelliteData(int type, String uuid) {
		super();
		this.type = type;
		this.uuid = uuid;
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
}
