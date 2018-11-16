package com.mjr.planetprogression;

public class Constants {
	public static final String modID = "planetprogression";
	public static final String modName = "Planet Progression";

	public static final int LOCALMAJVERSION = 0;
	public static final int LOCALMINVERSION = 2;
	public static final int LOCALBUILDVERSION = 4;

	public static final String modVersion = "1.8.9" + "-" + LOCALMAJVERSION + "." + LOCALMINVERSION + "." + LOCALBUILDVERSION;

	public static final String MCVERSION = "[1.8.9]";
	public static final String DEPENDENCIES_FORGE = "required-after:Forge@[11.15.1.1764,);";
	public static final String DEPENDENCIES_MODS = "required-after:mjrlegendslib@[1.8.9-1.1.1,); required-after:GalacticraftCore@[4.0.1.177,); required-after:GalacticraftPlanets@[4.0.1.177,); after:extraplanets; after:moreplanets;";
	public static final String CERTIFICATEFINGERPRINT = "b02331787272ec3515ebe63ecdeea0d746653468";

	public static final String ASSET_PREFIX = modID;
	public static final String TEXTURE_PREFIX = ASSET_PREFIX + ":";
	public static final String PREFIX = modID + ".";

	public static final float RADIANS_TO_DEGREES = 180F / 3.1415927F;
	public static final double RADIANS_TO_DEGREES_D = 180D / Math.PI;

	public static final float twoPI = Constants.floatPI * 2F;
	public static final float halfPI = Constants.floatPI / 2F;
	public static final float quarterPI = Constants.halfPI / 2F;
	public static final float floatPI = 3.1415927F;

	public static final int SPACE_STATION_LOWER_Y_LIMIT = 10;
	public static final int SPACE_STATION_HIGHER_Y_LIMIT = 1200;

	public static final float PLANET_AND_MOON_SPAWN_HEIGHT = 900.0F;
	public static final double PLANET_AND_MOON_SPAWN_HEIGHT_D = 900.0D;

	public static final double SPACE_STATION_SPAWN_HEIGHT_D = 65.0D;

	public static final double PLANET_AND_MOON_PARA_CHEST_SPAWN_HEIGHT_D = 220.0D;
	public static final double SPACE_STATION_PARA_CHEST_SPAWN_HEIGHT_D = 90.0D;

	public static final String CONFIG_FILE = "config/PlanetProgression.cfg";

	public static final String CONFIG_CATEGORY_COMPATIBILITY = "compatibility support";
	public static final String CONFIG_CATEGORY_GENERAL_SETTINGS = "general settings";
}