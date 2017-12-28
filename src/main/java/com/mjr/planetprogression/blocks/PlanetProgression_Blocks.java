package com.mjr.planetprogression.blocks;

import com.mjr.planetprogression.util.RegisterHelper;

import net.minecraft.block.Block;

public class PlanetProgression_Blocks {
	
	public static Block TELESCOPE;

	public static void init() {
		initializeBlocks();
		registerBlocks();
		registerTileEntitys();
		setHarvestLevels();
		OreDictionaryRegister();
	}

	private static void initializeBlocks() {
		TELESCOPE = new Telescope("telescope");
	}

	private static void registerBlocks() {
		RegisterHelper.registerBlock(TELESCOPE, TELESCOPE.getUnlocalizedName().substring(5));
	}

	private static void registerTileEntitys() {

	}

	private static void setHarvestLevels() {

	}

	private static void OreDictionaryRegister() {

	}
}
