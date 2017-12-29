package com.mjr.planetprogression.blocks;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.mjr.mjrlegendslib.util.RegisterUtilities;
import com.mjr.planetprogression.Constants;
import com.mjr.planetprogression.itemBlocks.ItemBlockTelescope;
import com.mjr.planetprogression.tileEntities.TileEntityTelescope;

public class PlanetProgression_Blocks {

	public static Block TELESCOPE;

	public static void init() {
		initializeBlocks();
		try {
			registerBlocks();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		registerTileEntitys();
		setHarvestLevels();
		OreDictionaryRegister();
	}

	private static void initializeBlocks() {
		TELESCOPE = new BlockTelescope("telescope");
	}

	private static void registerBlocks() throws NoSuchMethodException {
		RegisterUtilities.registerBlock(Constants.modID, TELESCOPE, ItemBlockTelescope.class, TELESCOPE.getUnlocalizedName().substring(5));
	}

	private static void registerTileEntitys() {
		GameRegistry.registerTileEntity(TileEntityTelescope.class, Constants.modName + "Telescope");
	}

	private static void setHarvestLevels() {

	}

	private static void OreDictionaryRegister() {

	}
}
