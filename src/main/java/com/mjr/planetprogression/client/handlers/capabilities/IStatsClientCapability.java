package com.mjr.planetprogression.client.handlers.capabilities;

import java.util.ArrayList;

import micdoodle8.mods.galacticraft.api.galaxies.Planet;

public interface IStatsClientCapability {
	ArrayList<Planet> getunlockedPlanets();
	void setunlockedPlanets(ArrayList<Planet> unlockedPlanets);
}
