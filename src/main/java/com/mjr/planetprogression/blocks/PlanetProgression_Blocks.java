package com.mjr.planetprogression.blocks;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.mjr.mjrlegendslib.util.RegisterUtilities;
import com.mjr.planetprogression.Constants;
import com.mjr.planetprogression.itemBlocks.ItemBlockTelescope;
import com.mjr.planetprogression.tileEntities.TileEntitySatelliteBuilder;
import com.mjr.planetprogression.tileEntities.TileEntityTelescope;

public class PlanetProgression_Blocks {

	public static Block TELESCOPE;
	public static Block SATTLLITE_BUILDER;

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
		SATTLLITE_BUILDER = new BlockSatelliteBuilder("satellite_builder");
	}

	public static void registerBlocks() throws NoSuchMethodException {
		RegisterUtilities.registerBlock(Constants.modID, TELESCOPE, ItemBlockTelescope.class, TELESCOPE.getUnlocalizedName().substring(5));
		RegisterUtilities.registerBlock(Constants.modID, SATTLLITE_BUILDER, ItemBlockTelescope.class, SATTLLITE_BUILDER.getUnlocalizedName().substring(5));
	}

	private static void registerTileEntitys() {
		GameRegistry.registerTileEntity(TileEntityTelescope.class, Constants.modName + "Telescope");
		GameRegistry.registerTileEntity(TileEntitySatelliteBuilder.class, Constants.modName + "SatelliteBuilder");
	}

	private static void setHarvestLevels() {

	}

	private static void OreDictionaryRegister() {

	}
}
