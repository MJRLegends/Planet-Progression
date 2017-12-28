package com.mjr.planetprogression.handlers.capabilities;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;

import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class StatsCapability implements IStatsCapability {
	public WeakReference<EntityPlayerMP> player;
	public ArrayList<Planet> unlockedPlanets = new ArrayList<Planet>();
	public int buildFlags = 0;

	@Override
	public ArrayList<Planet> getunlockedPlanets() {
		return this.unlockedPlanets;
	}

	@Override
	public void setunlockedPlanets(ArrayList<Planet> unlockedPlanets) {
		this.unlockedPlanets = unlockedPlanets;
	}

	@Override
	public void saveNBTData(NBTTagCompound nbt) {
		Collections.sort(this.unlockedPlanets);

		NBTTagList tagList = new NBTTagList();

		for (Planet planet : this.unlockedPlanets) {
			if (planet != null) {
				final NBTTagCompound nbttagcompound = new NBTTagCompound();
				nbttagcompound.setString("UnlockedPlanet", planet.getUnlocalizedName());
				tagList.appendTag(nbttagcompound);
			}
		}

		nbt.setTag("Planets", tagList);
	}

	@Override
	public void loadNBTData(NBTTagCompound nbt) {
		try {
			this.unlockedPlanets = new ArrayList<Planet>();

			if (this.player.get() != null) {
				for (int i = 0; i < nbt.getTagList("Planets", 10).tagCount(); ++i) {
					final NBTTagCompound nbttagcompound = nbt.getTagList("Planets", 10).getCompoundTagAt(i);

					final String j = nbttagcompound.getString("UnlockedPlanet");
					this.unlockedPlanets.add(GalaxyRegistry.getRegisteredPlanets().get(j));
				}
			}

			Collections.sort(this.unlockedPlanets);

		} catch (Exception e) {
			GCLog.severe("Found error in saved Planet Progression player data for " + player.get().getGameProfile().getName() + " - this should fix itself next relog.");
			e.printStackTrace();
		}

		GCLog.debug("Finished loading Planet Progression player data for " + player.get().getGameProfile().getName() + " : " + this.buildFlags);
	}

	@Override
	public void copyFrom(IStatsCapability oldData, boolean keepInv) {
		this.unlockedPlanets = oldData.getunlockedPlanets();
	}

	@Override
	public WeakReference<EntityPlayerMP> getPlayer() {
		return player;
	}

	@Override
	public void setPlayer(WeakReference<EntityPlayerMP> player) {
		this.player = player;
	}
}
