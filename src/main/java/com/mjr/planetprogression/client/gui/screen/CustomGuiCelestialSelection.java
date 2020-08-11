package com.mjr.planetprogression.client.gui.screen;

import java.util.Collections;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import com.google.common.collect.Lists;
import com.mjr.planetprogression.client.handlers.capabilities.CapabilityStatsClientHandler;
import com.mjr.planetprogression.client.handlers.capabilities.IStatsClientCapability;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.client.FMLClientHandler;

import micdoodle8.mods.galacticraft.api.galaxies.*;
import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiCelestialSelection;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;

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
		final EntityPlayerSP player = minecraft.player;
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
		final EntityPlayerSP player = minecraft.player;
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

	@Override
	public void drawButtons(int mousePosX, int mousePosY) {
		super.drawButtons(mousePosX, mousePosY);
		final int LHS = GuiCelestialSelection.BORDER_SIZE + GuiCelestialSelection.BORDER_EDGE_SIZE;
		CustomGuiCelestialSelection.drawRect(LHS + 1, (height - LHS) - 5, LHS + 500, (height - LHS)- 20, ColorUtil.to32BitColor(255, 0, 0, 0));
		this.fontRendererObj.drawString("Important: Want to unlock/see more celestial bodies? Research them via PlanetProgressions Mod", LHS + 5, (height - LHS) - 15, RED);
	}
}
