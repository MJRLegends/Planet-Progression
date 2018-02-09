package com.mjr.planetprogression.item;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Moon;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import net.minecraft.item.Item;

import com.mjr.mjrlegendslib.util.RegisterUtilities;
import com.mjr.planetprogression.Config;

public class PlanetProgression_Items {
	public static List<Item> researchPapers = new ArrayList<>();

	public static Item satelliteModules;
	public static Item satelliteBasicModule;

	public static Item satelliteBasic;
	public static Item satelliteSurface;
	public static Item satelliteDistance;
	public static Item satelliteAtmosphere;

	public static Item SATELLITE_ROCKET;

	public static void init() {
		initItems();
		registerItems();
	}

	public static void initResearchPaperItems() {
		int temp = 0;
		for (Planet planet : GalaxyRegistry.getRegisteredPlanets().values()) {
			if (!planet.getUnlocalizedName().contains("overworld"))
				researchPapers.add(new ResearchPaper(planet.getLocalizedName(), temp++));
		}
		for (Moon moon : GalaxyRegistry.getRegisteredMoons().values()) {
			researchPapers.add(new ResearchPaper(moon.getLocalizedName(), temp++));
		}
	}

	public static void initItems() {
		if (Config.researchMode == 2 || Config.researchMode == 3)
			SATELLITE_ROCKET = new ItemSatelliteRocket("item_satellite_rocket");
		if (Config.researchMode == 2) {
			satelliteBasicModule = new SatelliteModuleBasic("satellite_module_basic");
			satelliteBasic = new ItemSatellite("satellite_basic", 0);
		}
		if (Config.researchMode == 3) {
			satelliteModules = new SatelliteModule("satellite_module");
			satelliteSurface = new ItemSatellite("satellite_surface", 1);
			satelliteDistance = new ItemSatellite("satellite_distance", 2);
			satelliteAtmosphere = new ItemSatellite("satellite_atmosphere", 3);
		}
	}

	public static void registerResearchPaperItems() {
		for (Item item : researchPapers) {
			RegisterUtilities.registerItem(item, item.getUnlocalizedName().substring(5));
		}
	}

	public static void registerItems() {
		if (Config.researchMode == 2 || Config.researchMode == 3)
			RegisterUtilities.registerItem(SATELLITE_ROCKET, SATELLITE_ROCKET.getUnlocalizedName().substring(5));
		if (Config.researchMode == 2) {
			RegisterUtilities.registerItem(satelliteBasicModule, satelliteBasicModule.getUnlocalizedName().substring(5));
			RegisterUtilities.registerItem(satelliteBasic, satelliteBasic.getUnlocalizedName().substring(5));
		}
		if (Config.researchMode == 3) {
			RegisterUtilities.registerItem(satelliteModules, satelliteModules.getUnlocalizedName().substring(5));
			RegisterUtilities.registerItem(satelliteSurface, satelliteSurface.getUnlocalizedName().substring(5));
			RegisterUtilities.registerItem(satelliteDistance, satelliteDistance.getUnlocalizedName().substring(5));
			RegisterUtilities.registerItem(satelliteAtmosphere, satelliteAtmosphere.getUnlocalizedName().substring(5));
		}
	}
}
