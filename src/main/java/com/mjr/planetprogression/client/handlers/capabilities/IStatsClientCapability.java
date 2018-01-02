package com.mjr.planetprogression.client.handlers.capabilities;

import java.util.ArrayList;

import micdoodle8.mods.galacticraft.api.galaxies.Planet;

public interface IStatsClientCapability {
	ArrayList<Planet> getUnlockedPlanets();

	void setUnlockedPlanets(ArrayList<Planet> unlockedPlanets);

	void addUnlockedPlanets(Planet unlockedPlanet);
}
