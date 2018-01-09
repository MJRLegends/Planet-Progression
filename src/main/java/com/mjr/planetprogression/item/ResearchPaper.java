package com.mjr.planetprogression.item;

import com.mjr.mjrlegendslib.item.BasicItem;

public class ResearchPaper extends BasicItem {
	
	private String planet;

	public ResearchPaper(String name) {
		super(name + "_research_paper");
		this.planet = name.toLowerCase();
	}

	public String getPlanet() {
		return planet;
	}

	public void setPlanet(String planet) {
		this.planet = planet;
	}
}
