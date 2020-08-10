package com.mjr.planetprogression.api.research;

import com.mjr.planetprogression.handlers.capabilities.CapabilityStatsHandler;
import com.mjr.planetprogression.handlers.capabilities.IStatsCapability;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.api.galaxies.SolarSystem;

import net.minecraft.entity.player.EntityPlayerMP;

public class ResearchHooksMP {

	public static boolean hasUnlockedCelestialBody(EntityPlayerMP player, CelestialBody body) {
		IStatsCapability stats = null;
		if (player != null) {
			stats = player.getCapability(CapabilityStatsHandler.PP_STATS_CAPABILITY, null);
			return stats.getUnlockedPlanets().contains(body);
		}
		return false;
	}

	public static boolean hasUnlockedBodiesInSystem(EntityPlayerMP player, SolarSystem body) {
		IStatsCapability stats = null;
		if (player != null) {
			stats = player.getCapability(CapabilityStatsHandler.PP_STATS_CAPABILITY, null);
		}
		for (Planet planet : GalaxyRegistry.getPlanetsForSolarSystem(body))
			if (stats.getUnlockedPlanets().contains(planet))
				return true;
		return false;
	}
}