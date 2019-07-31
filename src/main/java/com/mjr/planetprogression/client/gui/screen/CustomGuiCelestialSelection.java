package com.mjr.planetprogression.client.gui.screen;

import java.util.Collections;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import com.google.common.collect.Lists;
import com.mjr.planetprogression.client.handlers.capabilities.CapabilityStatsClientHandler;
import com.mjr.planetprogression.client.handlers.capabilities.IStatsClientCapability;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Moon;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.api.galaxies.Satellite;
import micdoodle8.mods.galacticraft.api.galaxies.SolarSystem;
import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiCelestialSelection;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.client.FMLClientHandler;

public class CustomGuiCelestialSelection extends GuiCelestialSelection {

	public CustomGuiCelestialSelection(boolean mapMode, List<CelestialBody> possibleBodies, boolean canCreateStations) {
		super(mapMode, possibleBodies, canCreateStations);
	}

	/*
	 * Overriding for the purpose of to stop planets/moon from being initiated so they will be hidden from the screen in till unlocked
	 */
	@Override
	public void initGui() {
		final Minecraft minecraft = FMLClientHandler.instance().getClient();
		final EntityPlayerSP player = minecraft.thePlayer;
		final EntityPlayerSP playerBaseClient = PlayerUtil.getPlayerBaseClientFromPlayer(player, false);
		IStatsClientCapability stats = null;

		if (player != null) {
			stats = playerBaseClient.getCapability(CapabilityStatsClientHandler.PP_STATS_CLIENT_CAPABILITY, null);
		}
		this.celestialBodyTicks.clear();
		this.bodiesToRender.clear();
		for (SolarSystem star : GalaxyRegistry.getRegisteredSolarSystems().values()) {
			this.celestialBodyTicks.put(star.getMainStar(), 0);
		}

		for (Planet planet : GalaxyRegistry.getRegisteredPlanets().values()) {
			if (stats.getUnlockedPlanets().contains(planet)) {
				this.celestialBodyTicks.put(planet, 0);
				this.bodiesToRender.add(planet);
			}
		}

		for (Moon moon : GalaxyRegistry.getRegisteredMoons().values()) {
			if (stats.getUnlockedPlanets().contains(moon.getParentPlanet()) && stats.getUnlockedPlanets().contains(moon)) {
				this.celestialBodyTicks.put(moon, 0);
				this.bodiesToRender.add(moon);
			}
		}

		for (Satellite satellite : GalaxyRegistry.getRegisteredSatellites().values()) {
			if (stats.getUnlockedPlanets().contains(satellite.getParentPlanet())) {
				this.celestialBodyTicks.put(satellite, 0);
				this.bodiesToRender.add(satellite);
			}
		}

		GuiCelestialSelection.BORDER_SIZE = this.width / 65;
		GuiCelestialSelection.BORDER_EDGE_SIZE = GuiCelestialSelection.BORDER_SIZE / 4;

		for (SolarSystem solarSystem : GalaxyRegistry.getRegisteredSolarSystems().values()) {
			this.bodiesToRender.add(solarSystem.getMainStar());
		}
	}

	/*
	 * Overriding for the purpose of to hide planets/moon from the screen in till unlocked
	 */
	@Override
	protected List<CelestialBody> getChildren(Object object) {
		List<CelestialBody> bodyList = Lists.newArrayList();
		final Minecraft minecraft = FMLClientHandler.instance().getClient();
		final EntityPlayerSP player = minecraft.thePlayer;
		final EntityPlayerSP playerBaseClient = PlayerUtil.getPlayerBaseClientFromPlayer(player, false);

		IStatsClientCapability stats = null;

		if (player != null) {
			stats = playerBaseClient.getCapability(CapabilityStatsClientHandler.PP_STATS_CLIENT_CAPABILITY, null);
		}

		if (object instanceof Planet) {
			List<Moon> moons = GalaxyRegistry.getMoonsForPlanet((Planet) object);
			for (Moon moon : moons)
				if (stats.getUnlockedPlanets().contains(moon))
					bodyList.add(moon);
		} else if (object instanceof SolarSystem) {
			List<Planet> planets = GalaxyRegistry.getPlanetsForSolarSystem((SolarSystem) object);
			for (Planet planet : planets)
				if (stats.getUnlockedPlanets().contains(planet))
					bodyList.add(planet);
		}

		Collections.sort(bodyList);

		return bodyList;
	}

	/*
	 * Overriding for the purpose of to fix possible init issues due to network packets delay
	 */
	@Override
	protected Vector3f getCelestialBodyPosition(CelestialBody cBody) {
		if (this.celestialBodyTicks.get(cBody) == null)
			this.initGui();
		return super.getCelestialBodyPosition(cBody);
	}
}
