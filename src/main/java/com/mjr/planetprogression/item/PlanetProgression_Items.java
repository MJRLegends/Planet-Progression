package com.mjr.planetprogression.item;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Moon;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import net.minecraft.item.Item;

import com.mjr.planetprogression.Config;
import com.mjr.planetprogression.PlanetProgression;

public class PlanetProgression_Items {
	public static List<Item> researchPapers = new ArrayList<>();

	public static Item satelliteModules;
	public static Item satelliteBasicModule;

	public static Item satelliteBasic;
	public static Item satelliteSurface;
	public static Item satelliteDistance;
	public static Item satelliteAtmosphere;

	public static Item SATELLITE_ROCKET;

	public static List<String> bodies = new ArrayList<String>();
	public static List<String> bodiesExtra = new ArrayList<String>();

	public static void init() {
		initItems();
		registerItems();
	}

	public static void registerItem(Item item, String name) {
		if (item.getRegistryName() == null) {
			item.setRegistryName(name);
		}
		PlanetProgression.itemList.add(item);
	}

	public static void initResearchPaperItems() {
		bodies.add("moon.moon");
		bodies.add("planet.mars");
		bodies.add("planet.venus");
		bodies.add("planet.asteroids");
		bodiesExtra.add("planet.mercury");
		bodiesExtra.add("planet.jupiter");
		bodiesExtra.add("planet.saturn");
		bodiesExtra.add("planet.uranus");
		bodiesExtra.add("planet.neptune");

		int temp = 0;
		for (String planet : bodies) {
			if (!planet.equalsIgnoreCase("overworld"))
				researchPapers.add(new ResearchPaper(planet, temp++));
		}
		for (Planet planet : GalaxyRegistry.getRegisteredPlanets().values()) {
			if (!planet.getUnlocalizedName().contains("overworld"))
				researchPapers.add(new ResearchPaper(planet.getUnlocalizedName(), temp++));
		}
		for (Moon moon : GalaxyRegistry.getRegisteredMoons().values()) {
			researchPapers.add(new ResearchPaper(moon.getUnlocalizedName(), temp++));
		}

		boolean found = false;
		for (String tempBody : bodiesExtra) {
			if (researchPapers.size() == 0)
				found = true;

			for (Item paper : researchPapers) {
				if (found == false) {
					if (((ResearchPaper) paper).getPlanetName().equalsIgnoreCase(tempBody)) {
						found = true;
					}
				}
			}
			if (!found) {
				researchPapers.add(new ResearchPaper(tempBody, temp++));
				found = false;
			}
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
			registerItem(item, item.getUnlocalizedName().substring(5));
		}
	}

	public static void registerItems() {
		if (Config.researchMode == 2 || Config.researchMode == 3)
			registerItem(SATELLITE_ROCKET, SATELLITE_ROCKET.getUnlocalizedName().substring(5));
		if (Config.researchMode == 2) {
			registerItem(satelliteBasicModule, satelliteBasicModule.getUnlocalizedName().substring(5));
			registerItem(satelliteBasic, satelliteBasic.getUnlocalizedName().substring(5));
		}
		if (Config.researchMode == 3) {
			registerItem(satelliteModules, satelliteModules.getUnlocalizedName().substring(5));
			registerItem(satelliteSurface, satelliteSurface.getUnlocalizedName().substring(5));
			registerItem(satelliteDistance, satelliteDistance.getUnlocalizedName().substring(5));
			registerItem(satelliteAtmosphere, satelliteAtmosphere.getUnlocalizedName().substring(5));
		}
	}
}
