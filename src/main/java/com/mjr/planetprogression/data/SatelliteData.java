package com.mjr.planetprogression.data;

import net.minecraft.item.ItemStack;

public class SatelliteData {
	private int type;
	private String uuid;
	private int dataAmount;
	private static final int MAX_DATA = 20000;
	private ItemStack currentResearchItem = null;

	public SatelliteData(int type, String uuid, int dataAmount, ItemStack currentResearchItem) {
		super();
		this.type = type;
		this.uuid = uuid;
		this.dataAmount = dataAmount;
		this.currentResearchItem = currentResearchItem;
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

	public static int getMAX_DATA() {
		return MAX_DATA;
	}

	public ItemStack getCurrentResearchItem() {
		return currentResearchItem;
	}

	public void setCurrentResearchItem(ItemStack currentResearchItem) {
		this.currentResearchItem = currentResearchItem;
	}
}
