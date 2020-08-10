package com.mjr.planetprogression.api.research;

import com.mjr.planetprogression.client.handlers.capabilities.CapabilityStatsClientHandler;
import com.mjr.planetprogression.client.handlers.capabilities.IStatsClientCapability;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.api.galaxies.SolarSystem;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ResearchHooksSP {

	@SideOnly(Side.CLIENT)
	public static boolean hasUnlockedCelestialBody(EntityPlayerSP player, CelestialBody body) {
		IStatsClientCapability stats = null;
		if (player != null) {
			stats = player.getCapability(CapabilityStatsClientHandler.PP_STATS_CLIENT_CAPABILITY, null);
			return stats.getUnlockedPlanets().contains(body);
		}
		return false;
	}

	public static boolean hasUnlockedBodiesInSystem(EntityPlayerSP player, SolarSystem body) {
		IStatsClientCapability stats = null;
		if (player != null) {
			stats = player.getCapability(CapabilityStatsClientHandler.PP_STATS_CLIENT_CAPABILITY, null);
		}
		for (Planet planet : GalaxyRegistry.getPlanetsForSolarSystem(body))
			if (stats.getUnlockedPlanets().contains(planet))
				return true;
		return false;
	}
}