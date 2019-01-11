package com.mjr.planetprogression.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.mjr.mjrlegendslib.util.RegisterUtilities;
import com.mjr.planetprogression.Config;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Moon;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.item.Item;

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

	static enum SortType implements Comparator<CelestialBody> {
		TIER(1) {
			@Override
			public int compare(CelestialBody celestial1, CelestialBody celestial2) {
				return Integer.compare(celestial2.getTierRequirement(), celestial1.getTierRequirement());
			}
		};

		protected static final SortType[] values = SortType.values();
		private int id;

		private SortType(int id) {
			this.id = id;
		}

		@Override
		public int compare(CelestialBody celestial1, CelestialBody celestial2) {
			return celestial2.getName().compareTo(celestial1.getName());
		}

		@Nullable
		protected static SortType getTypeFromID(int id) {
			for (SortType type : SortType.values) {
				if (type.id == id) {
					return type;
				}
			}
			return null;
		}
	}

	public static void initResearchPaperItems() {
		List<CelestialBody> unReachableResearchPapers = new ArrayList<>();
		List<CelestialBody> reachablePlanetsMoons = new ArrayList<>();

		reachablePlanetsMoons.add(GalacticraftCore.planetOverworld);
		// Sort All Registered Planets/Moons by in to reachable & unreachable
		int temp = 0;
		for (Planet planet : GalaxyRegistry.getRegisteredPlanets().values()) {
			if (planet.getReachable())
				reachablePlanetsMoons.add(planet);
			else
				unReachableResearchPapers.add(planet);
		}
		for (Moon moon : GalaxyRegistry.getRegisteredMoons().values()) {
			if (moon.getReachable())
				reachablePlanetsMoons.add(moon);
			else
				unReachableResearchPapers.add(moon);
		}

		// Sort Research Paper order by Rocket Tier
		Collections.sort(reachablePlanetsMoons, SortType.getTypeFromID(1));

		// Reverse list to get it in the correct order
		reachablePlanetsMoons = Lists.reverse(reachablePlanetsMoons);

		// Create Research Papers for Sorted Planets/Moons
		for (CelestialBody body : reachablePlanetsMoons) {
			researchPapers.add(new ResearchPaper(body.getUnlocalizedName(), temp++));
		}
		for (CelestialBody body : unReachableResearchPapers) {
			researchPapers.add(new ResearchPaper(body.getUnlocalizedName(), temp++));
		}

		for (Item item : researchPapers) {
			System.out.println(((ResearchPaper) item).getPlanetName());
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
