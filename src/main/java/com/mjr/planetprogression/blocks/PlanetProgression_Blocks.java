package com.mjr.planetprogression.blocks;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.mjr.mjrlegendslib.util.RegisterUtilities;
import com.mjr.planetprogression.Config;
import com.mjr.planetprogression.Constants;
import com.mjr.planetprogression.itemBlocks.ItemBlockTelescope;
import com.mjr.planetprogression.tileEntities.TileEntitySatelliteBuilder;
import com.mjr.planetprogression.tileEntities.TileEntitySatelliteController;
import com.mjr.planetprogression.tileEntities.TileEntityTelescope;

public class PlanetProgression_Blocks {

	public static Block TELESCOPE;
	public static Block SATTLLITE_BUILDER;
	public static Block SATTLLITE_CONTROLLER;

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
		if (Config.researchMode == 2 || Config.researchMode == 3) {
			SATTLLITE_BUILDER = new BlockSatelliteBuilder("satellite_builder");
			SATTLLITE_CONTROLLER = new BlockSatelliteController("satellite_controller");
		}
	}

	public static void registerBlocks() throws NoSuchMethodException {
		RegisterUtilities.registerBlock(TELESCOPE, ItemBlockTelescope.class, TELESCOPE.getUnlocalizedName().substring(5));
		if (Config.researchMode == 2 || Config.researchMode == 3) {
			RegisterUtilities.registerBlock(SATTLLITE_BUILDER, ItemBlockTelescope.class, SATTLLITE_BUILDER.getUnlocalizedName().substring(5));
			RegisterUtilities.registerBlock(SATTLLITE_CONTROLLER, ItemBlockTelescope.class, SATTLLITE_CONTROLLER.getUnlocalizedName().substring(5));
		}
	}

	private static void registerTileEntitys() {
		GameRegistry.registerTileEntity(TileEntityTelescope.class, Constants.modName + "Telescope");
		if (Config.researchMode == 2 || Config.researchMode == 3) {
			GameRegistry.registerTileEntity(TileEntitySatelliteBuilder.class, Constants.modName + "SatelliteBuilder");
			GameRegistry.registerTileEntity(TileEntitySatelliteController.class, Constants.modName + "SatelliteController");
		}
	}

	private static void setHarvestLevels() {

	}

	private static void OreDictionaryRegister() {

	}
}
