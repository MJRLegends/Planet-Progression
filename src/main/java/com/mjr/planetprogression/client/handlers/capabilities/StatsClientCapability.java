package com.mjr.planetprogression.client.handlers.capabilities;

import java.util.ArrayList;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;

import com.mjr.planetprogression.data.SatelliteData;

public class StatsClientCapability implements IStatsClientCapability {
	private ArrayList<CelestialBody> unlockedPlanets = new ArrayList<CelestialBody>();
	private ArrayList<SatelliteData> satellites = new ArrayList<SatelliteData>();

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

	@Override
	public ArrayList<SatelliteData> getSatellites() {
		return this.satellites;
	}

	@Override
	public void setSatellites(ArrayList<SatelliteData> satellites) {
		this.satellites = satellites;
	}

	@Override
	public void addSatellites(SatelliteData satellites) {
		this.satellites.add(satellites);
	}
}
