package com.mjr.planetprogression.blocks;

import java.lang.reflect.Constructor;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

import com.google.common.collect.ObjectArrays;
import com.mjr.mjrlegendslib.itemBlock.ItemBlockDefault;
import com.mjr.mjrlegendslib.util.RegisterUtilities;
import com.mjr.planetprogression.Config;
import com.mjr.planetprogression.Constants;
import com.mjr.planetprogression.PlanetProgression;
import com.mjr.planetprogression.itemBlocks.ItemBlockBasic;
import com.mjr.planetprogression.tileEntities.TileEntitySatelliteBuilder;
import com.mjr.planetprogression.tileEntities.TileEntitySatelliteController;
import com.mjr.planetprogression.tileEntities.TileEntityTelescope;
import com.mjr.planetprogression.tileEntities.TileEntityTelescopeFake;

public class PlanetProgression_Blocks {

	public static Block TELESCOPE;
	public static Block FAKE_TELESCOPE;

	public static Block SATTLLITE_BUILDER;
	public static Block SATTLLITE_CONTROLLER;
	public static Block SATTLLITE_LAUNCHER;

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

	public static void registerBlock(Block block, Class<? extends ItemBlock> itemclass, String name, Object... itemCtorArgs) throws NoSuchMethodException {
		if (block.getRegistryName() == null) {
			block.setRegistryName(name);
		}
		PlanetProgression.blocksList.add(block);
		if (itemclass != null) {
			ItemBlock item = null;
			Class<?>[] ctorArgClasses = new Class<?>[itemCtorArgs.length + 1];
			ctorArgClasses[0] = Block.class;
			for (int idx = 1; idx < ctorArgClasses.length; idx++) {
				ctorArgClasses[idx] = itemCtorArgs[idx - 1].getClass();
			}

			try {
				Constructor<? extends ItemBlock> constructor = itemclass.getConstructor(ctorArgClasses);
				item = constructor.newInstance(ObjectArrays.concat(block, itemCtorArgs));
			} catch (Exception e) {
				e.printStackTrace();
			}
			PlanetProgression.itemList.add(item);
			if (item.getRegistryName() == null) {
				item.setRegistryName(name);
			}
		}
	}

	public static void initBlocks() {
		TELESCOPE = new BlockTelescope("telescope");
		FAKE_TELESCOPE = new BlockTelescopeFake("telescope_fake_block");
		if (Config.researchMode == 2 || Config.researchMode == 3) {
			SATTLLITE_BUILDER = new BlockSatelliteBuilder("satellite_builder");
			SATTLLITE_CONTROLLER = new BlockSatelliteController("satellite_controller");
		}
	}

	public static void registerBlocks() throws NoSuchMethodException {
		registerBlock(TELESCOPE, ItemBlockBasic.class, TELESCOPE.getUnlocalizedName().substring(5));
		registerBlock(FAKE_TELESCOPE, ItemBlockDefault.class, FAKE_TELESCOPE.getUnlocalizedName().substring(5));
		if (Config.researchMode == 2 || Config.researchMode == 3) {
			registerBlock(SATTLLITE_BUILDER, ItemBlockBasic.class, SATTLLITE_BUILDER.getUnlocalizedName().substring(5));
			registerBlock(SATTLLITE_CONTROLLER, ItemBlockBasic.class, SATTLLITE_CONTROLLER.getUnlocalizedName().substring(5));
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
