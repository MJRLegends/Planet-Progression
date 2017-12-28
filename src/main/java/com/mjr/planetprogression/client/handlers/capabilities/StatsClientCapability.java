package com.mjr.planetprogression.client.handlers.capabilities;

import java.util.ArrayList;

import micdoodle8.mods.galacticraft.api.galaxies.Planet;

public class StatsClientCapability implements IStatsClientCapability {
	public ArrayList<Planet> unlockedPlanets = new ArrayList<Planet>();

	@Override
	public ArrayList<Planet> getunlockedPlanets() {
		return this.unlockedPlanets;
	}

	@Override
	public void setunlockedPlanets(ArrayList<Planet> unlockedPlanets) {
		this.unlockedPlanets = unlockedPlanets;
	}
}
