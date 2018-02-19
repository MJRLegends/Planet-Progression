package com.mjr.planetprogression.handlers.capabilities;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import com.mjr.planetprogression.data.SatelliteData;

public interface IStatsCapability {

	void saveNBTData(NBTTagCompound nbt);

	void loadNBTData(NBTTagCompound nbt);

	void copyFrom(IStatsCapability oldData, boolean keepInv);

	WeakReference<EntityPlayerMP> getPlayer();

	void setPlayer(WeakReference<EntityPlayerMP> player);

	ArrayList<CelestialBody> getUnlockedPlanets();

	void setUnlockedPlanets(ArrayList<CelestialBody> unlockedPlanets);

	void addUnlockedPlanets(CelestialBody unlockedPlanet);
	
	void removeUnlockedPlanets(CelestialBody unlockedPlanet);

	ArrayList<SatelliteData> getSatellites();

	void setSatellites(ArrayList<SatelliteData> satellites);

	void addSatellites(SatelliteData satellites);
	
	void removeSatellites(SatelliteData satellites);
}
