package com.mjr.planetprogression.client.handlers.capabilities;

import java.util.ArrayList;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;

public class StatsClientCapability implements IStatsClientCapability {
	private ArrayList<CelestialBody> unlockedPlanets = new ArrayList<CelestialBody>();

	@Override
	public ArrayList<CelestialBody> getUnlockedPlanets() {
		return this.unlockedPlanets;
	}

	@Override
	public void setUnlockedPlanets(ArrayList<CelestialBody> unlockedPlanets) {
		this.unlockedPlanets = unlockedPlanets;
	}

	@Override
	public void addUnlockedPlanets(CelestialBody unlockedPlanet) {
		this.unlockedPlanets.add(unlockedPlanet);
	}
}
