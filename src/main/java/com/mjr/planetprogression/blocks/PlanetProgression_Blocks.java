package com.mjr.planetprogression.blocks;

import com.mjr.mjrlegendslib.itemBlock.ItemBlockDefault;
import com.mjr.mjrlegendslib.util.RegisterUtilities;
import com.mjr.planetprogression.Config;
import com.mjr.planetprogression.Constants;
import com.mjr.planetprogression.itemBlocks.ItemBlockBasic;
import com.mjr.planetprogression.tileEntities.TileEntitySatelliteBuilder;
import com.mjr.planetprogression.tileEntities.TileEntitySatelliteController;
import com.mjr.planetprogression.tileEntities.TileEntityTelescope;
import com.mjr.planetprogression.tileEntities.TileEntityTelescopeFake;

import net.minecraft.block.Block;

public class PlanetProgression_Blocks {

	public static Block TELESCOPE;
	public static Block FAKE_TELESCOPE;

	public static Block SATTLLITE_BUILDER;
	public static Block SATTLLITE_CONTROLLER;
	public static Block SATTLLITE_LAUNCHER;
	public static Block ADVANCED_LAUCHPAD;
	public static Block ADVANCED_LAUCHPAD_FULL;
	public static Block FAKE_BLOCK;

	public static void init() {
		initBlocks();
		try {
			registerBlocks();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		registerTileEntitys();
		setHarvestLevels();
		OreDictionaryRegister();
	}

	public static void initBlocks() {
		TELESCOPE = new BlockTelescope("telescope");
		FAKE_TELESCOPE = new BlockTelescopeFake("telescope_fake_block");
		if (Config.researchMode == 2 || Config.researchMode == 3) {
			SATTLLITE_BUILDER = new BlockSatelliteBuilder("satellite_builder");
			SATTLLITE_CONTROLLER = new BlockSatelliteController("satellite_controller");
			SATTLLITE_LAUNCHER = new BlockSatelliteRocketLauncher("satellite_launcher");
			ADVANCED_LAUCHPAD = new BlockCustomLandingPad("advanced_launch_pad");
			ADVANCED_LAUCHPAD_FULL = new BlockCustomLandingPadFull("advanced_launch_pad_full");
			FAKE_BLOCK = new BlockCustomMulti("block_multi");
		}
	}

	public static void registerBlocks() throws NoSuchMethodException {
		RegisterUtilities.registerBlock(TELESCOPE, ItemBlockBasic.class, TELESCOPE.getUnlocalizedName().substring(5));
		RegisterUtilities.registerBlock(FAKE_TELESCOPE, ItemBlockDefault.class, FAKE_TELESCOPE.getUnlocalizedName().substring(5));
		if (Config.researchMode == 2 || Config.researchMode == 3) {
			RegisterUtilities.registerBlock(SATTLLITE_BUILDER, ItemBlockBasic.class, SATTLLITE_BUILDER.getUnlocalizedName().substring(5));
			RegisterUtilities.registerBlock(SATTLLITE_CONTROLLER, ItemBlockBasic.class, SATTLLITE_CONTROLLER.getUnlocalizedName().substring(5));
		}
	}

	private static void registerTileEntitys() {
		RegisterUtilities.registerTileEntity(TileEntityTelescope.class, Constants.modName + "Telescope");
		RegisterUtilities.registerTileEntity(TileEntityTelescopeFake.class, Constants.modName + "Telescope Fake");
		if (Config.researchMode == 2 || Config.researchMode == 3) {
			RegisterUtilities.registerTileEntity(TileEntitySatelliteBuilder.class, Constants.modName + "SatelliteBuilder");
			RegisterUtilities.registerTileEntity(TileEntitySatelliteController.class, Constants.modName + "SatelliteController");
		}
	}

	private static void setHarvestLevels() {

	}

	private static void OreDictionaryRegister() {

	}
}
