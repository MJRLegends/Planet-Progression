package com.mjr.planetprogression.handlers.capabilities;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.UUID;

import com.mjr.planetprogression.data.SatelliteData;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.core.util.GCLog;

public class StatsCapability implements IStatsCapability {
	public WeakReference<EntityPlayerMP> player;
	private ArrayList<CelestialBody> unlockedPlanets = new ArrayList<CelestialBody>();
	private ArrayList<SatelliteData> satellites = new ArrayList<SatelliteData>();
	public int buildFlags = 0;

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
	public void saveNBTData(NBTTagCompound nbt) {
		NBTTagList tagList = new NBTTagList();

		for (CelestialBody planet : this.unlockedPlanets) {
			if (planet != null) {
				final NBTTagCompound nbttagcompound = new NBTTagCompound();
				nbttagcompound.setString("UnlockedPlanet", planet.getUnlocalizedName());
				tagList.appendTag(nbttagcompound);
			}
		}

		nbt.setTag("Planets", tagList);

		tagList = new NBTTagList();
		for (SatelliteData satellite : this.satellites) {
			if (satellite != null) {
				final NBTTagCompound nbttagcompound = new NBTTagCompound();
				nbttagcompound.setString("uuid", satellite.getUuid());
				nbttagcompound.setInteger("type", satellite.getType());
				nbttagcompound.setInteger("dataAmount", satellite.getDataAmount());
				if (satellite.getCurrentResearchItem() != null)
					nbttagcompound.setTag("currentResearchItem", satellite.getCurrentResearchItem().writeToNBT(new NBTTagCompound()));
				tagList.appendTag(nbttagcompound);
			}
		}

		nbt.setTag("Satellites", tagList);
	}

	@Override
	public void loadNBTData(NBTTagCompound nbt) {
		try {
			this.unlockedPlanets = new ArrayList<CelestialBody>();
			this.satellites = new ArrayList<SatelliteData>();

			if (this.player.get() != null) {
				for (int i = 0; i < nbt.getTagList("Planets", 10).tagCount(); ++i) {
					this.unlockedPlanets.add(GalaxyRegistry.getCelestialBodyFromUnlocalizedName(nbt.getTagList("Planets", 10).getCompoundTagAt(i).getString("UnlockedPlanet")));
				}
				for (int i = 0; i < nbt.getTagList("Satellites", 10).tagCount(); ++i) {
					final NBTTagCompound nbttagcompound = nbt.getTagList("Satellites", 10).getCompoundTagAt(i);

					String j = nbttagcompound.getString("uuid");
					int k = nbttagcompound.getInteger("type");
					int l = nbttagcompound.getInteger("dataAmount");
					if (j == "")
						j = UUID.randomUUID().toString();
					NBTTagCompound tag = nbt.getCompoundTag("currentResearchItem");
					this.satellites.add(new SatelliteData(k, j, l, new ItemStack(tag).getUnlocalizedName().contains("air") ? null : new ItemStack(tag)));
				}
			}

		} catch (Exception e) {
			GCLog.severe("Found error in saved Planet Progression player data for " + player.get().getGameProfile().getName() + " - this should fix itself next relog.");
			e.printStackTrace();
		}

		GCLog.debug("Finished loading Planet Progression player data for " + player.get().getGameProfile().getName() + " : " + this.buildFlags);
	}

	@Override
	public void copyFrom(IStatsCapability oldData, boolean keepInv) {
		this.unlockedPlanets = oldData.getUnlockedPlanets();
		this.satellites = oldData.getSatellites();
	}

	@Override
	public WeakReference<EntityPlayerMP> getPlayer() {
		return player;
	}

	@Override
	public void setPlayer(WeakReference<EntityPlayerMP> player) {
		this.player = player;
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

	@Override
	public void removeSatellites(SatelliteData satellites) {
		this.satellites.remove(satellites);
	}

	@Override
	public void removeUnlockedPlanets(CelestialBody unlockedPlanet) {
		this.unlockedPlanets.remove(unlockedPlanet);
	}
}
