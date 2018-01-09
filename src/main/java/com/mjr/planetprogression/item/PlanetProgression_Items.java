package com.mjr.planetprogression.item;

import java.util.ArrayList;
import java.util.List;

import com.mjr.mjrlegendslib.util.RegisterUtilities;

import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Moon;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import net.minecraft.item.Item;

public class PlanetProgression_Items {
	public static List<Item> researchPapers = new ArrayList<>();

	public static void init() {
		initItems();
		registerItems();
	}

	public static void initItems() {
		for (Planet planet : GalaxyRegistry.getRegisteredPlanets().values()){
			if(!planet.getUnlocalizedName().contains("overworld"))
				researchPapers.add(new ResearchPaper(planet.getLocalizedName()));
		}
		for (Moon moon : GalaxyRegistry.getRegisteredMoons().values()){
			researchPapers.add(new ResearchPaper(moon.getLocalizedName()));
		}
	}

	public static void registerItems() {
		for (Item item : researchPapers){
			RegisterUtilities.registerItem(item, item.getUnlocalizedName().substring(5));
		}
	}
}
