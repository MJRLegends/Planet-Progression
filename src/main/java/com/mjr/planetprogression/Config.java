package com.mjr.planetprogression;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {
	public static int researchMode = 0;
	public static boolean generateResearchPaperInLoot;
	
	public static void load() {
		Configuration config = new Configuration(new File(Constants.CONFIG_FILE));
		config.load();
		researchMode = config.get(Constants.CONFIG_CATEGORY_GENERAL_SETTINGS, "Research Mode", 0, "Research Modes: 0 - Basic Research Paper Method | 2 - Not Used | 3 - Basic Satellite Research Method").getInt(0);
		generateResearchPaperInLoot = config.get(Constants.CONFIG_CATEGORY_GENERAL_SETTINGS, "Add Research Papers to Dungeon Loot", true, "Will add the Research Papers to spawn in Vanilla Dungeon Loot").getBoolean(true);
		config.save();
	}
}
