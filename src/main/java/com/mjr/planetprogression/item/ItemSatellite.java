package com.mjr.planetprogression.item;

import com.mjr.mjrlegendslib.item.BasicItem;

public class ItemSatellite extends BasicItem {
	
	private int type;

	public ItemSatellite(String name, int type) {
		super(name);
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
