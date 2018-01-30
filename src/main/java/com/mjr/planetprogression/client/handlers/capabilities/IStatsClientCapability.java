package com.mjr.planetprogression.client.handlers.capabilities;

import java.util.ArrayList;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;

import com.mjr.planetprogression.data.SatelliteData;

public interface IStatsClientCapability {
	ArrayList<CelestialBody> getUnlockedPlanets();

	void setUnlockedPlanets(ArrayList<CelestialBody> unlockedPlanets);

	void addUnlockedPlanets(CelestialBody unlockedPlanet);

	ArrayList<SatelliteData> getSatellites();

	void setSatellites(ArrayList<SatelliteData> satellites);

	void addSatellites(SatelliteData satellites);
}
