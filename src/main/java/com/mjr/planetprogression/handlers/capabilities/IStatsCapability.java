package com.mjr.planetprogression.handlers.capabilities;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public interface IStatsCapability {

	void saveNBTData(NBTTagCompound nbt);

	void loadNBTData(NBTTagCompound nbt);

	void copyFrom(IStatsCapability oldData, boolean keepInv);

	WeakReference<EntityPlayerMP> getPlayer();

	void setPlayer(WeakReference<EntityPlayerMP> player);

	ArrayList<Planet> getUnlockedPlanets();

	void setUnlockedPlanets(ArrayList<Planet> unlockedPlanets);

	void addUnlockedPlanets(Planet unlockedPlanet);
}
