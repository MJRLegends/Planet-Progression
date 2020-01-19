package com.mjr.planetprogression;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {
	public static int researchMode = 0;
	public static int worldgenStructureAmount = 0;
	public static int worldgenStructureLootAmount = 0;
	public static boolean generateResearchPaperInLoot;
	public static boolean generateResearchPaperInStructure;
	public static double telescopeTimeModifier = 1.0F;
	public static double satelliteControllerModifier = 1.0F;
	public static String[] worldgenStructureWorldWhitelist;
	public static String[] worldgenStructurePaperBlacklist;
	public static String[] dungeonPaperBlacklist;

	public static boolean showOverworldSpawnMessage;

	public static void load() {
		Configuration config = new Configuration(new File(Constants.CONFIG_FILE));
		config.load();
		researchMode = config.get(Constants.CONFIG_CATEGORY_GENERAL_SETTINGS, "Research Mode", 2, "Research Modes: 1 - Basic Research Paper Method | 2 - Basic Satellite Research Method").getInt(2);
		worldgenStructureAmount = config.get(Constants.CONFIG_CATEGORY_GENERAL_SETTINGS, "World Gen Structure Weight", 100, "Will be 1 in x (x = being the number in this config option), Default: 100").getInt(100);
		worldgenStructureLootAmount = config.get(Constants.CONFIG_CATEGORY_GENERAL_SETTINGS, "World Gen Structure Loot Spawn Weight", 10, "Will be 1 in x (x = being the number in this config option), Default: 10").getInt(10);
		generateResearchPaperInLoot = config.get(Constants.CONFIG_CATEGORY_GENERAL_SETTINGS, "Add Research Papers to Dungeon Loot", false, "Will add the Research Papers to spawn in Vanilla Dungeon Loot").getBoolean(false);
		generateResearchPaperInStructure = config
				.get(Constants.CONFIG_CATEGORY_GENERAL_SETTINGS, "Add Research Papers to Custom WorldGen Structure", false, "Will add the Research Papers to spawn in Custom WorldGen Structure, Note will disable structure if set to false")
				.getBoolean(false);
		telescopeTimeModifier = config.get(Constants.CONFIG_CATEGORY_GENERAL_SETTINGS, "Telescope Research Time Modifier", 1.0, "Will increase the unlocking time for each paper in the telescope Default: 1.0F").getDouble(1.0);
		satelliteControllerModifier = config.get(Constants.CONFIG_CATEGORY_GENERAL_SETTINGS, "Satellite Controller Research Time Modifier", 1.0, "Will increase the research time for each paper for the satellite Default: 1.0F").getDouble(1.0);
		worldgenStructureWorldWhitelist = config.getStringList("Whitelist for World Gen Structure", Constants.CONFIG_CATEGORY_GENERAL_SETTINGS, new String[] { "0" }, "Default: 0 | Format: 'planet.NAME' OR 'moon.NAME' | Example: 'planet.venus'");
		worldgenStructurePaperBlacklist = config.getStringList("Blacklist for Research Papers in World Gen Structure", Constants.CONFIG_CATEGORY_GENERAL_SETTINGS, new String[] {}, "Format: 'planet.NAME' OR 'moon.NAME' | Example: 'planet.venus'");
		dungeonPaperBlacklist = config.getStringList("Blacklist for Research Papers in Dungeon Loot", Constants.CONFIG_CATEGORY_GENERAL_SETTINGS, new String[] {}, "Format: 'planet.NAME' OR 'moon.NAME' | Example: 'planet.venus'");
		showOverworldSpawnMessage = config.get(Constants.CONFIG_CATEGORY_GENERAL_SETTINGS, "Show Overworld As been Discovered Message on first joining world/server", true,
				"Setting to false will disable the 'You have discovered Earth' message you get in chat when first joining a world/server").getBoolean(true);

		config.save();
		if (researchMode != 1 && researchMode != 2)
			researchMode = 2;

		if (researchMode == 2) {
			generateResearchPaperInStructure = false;
		}

	}
}
