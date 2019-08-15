package com.mjr.planetprogression.api.research;

import com.mjr.planetprogression.handlers.capabilities.CapabilityStatsHandler;
import com.mjr.planetprogression.handlers.capabilities.IStatsCapability;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
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
}