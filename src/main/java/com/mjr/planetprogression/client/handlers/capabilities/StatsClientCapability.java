package com.mjr.planetprogression.client.handlers.capabilities;

import java.util.ArrayList;

import micdoodle8.mods.galacticraft.api.galaxies.Planet;

public class StatsClientCapability implements IStatsClientCapability {
	private ArrayList<Planet> unlockedPlanets = new ArrayList<Planet>();

	@Override
	public ArrayList<Planet> getUnlockedPlanets() {
		return this.unlockedPlanets;
	}

	@Override
	public void setUnlockedPlanets(ArrayList<Planet> unlockedPlanets) {
		this.unlockedPlanets = unlockedPlanets;
	}

	@Override
	public void addUnlockedPlanets(Planet unlockedPlanet) {
		this.unlockedPlanets.add(unlockedPlanet);
	}
}
