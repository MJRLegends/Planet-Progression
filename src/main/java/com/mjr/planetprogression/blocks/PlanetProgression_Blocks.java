package com.mjr.planetprogression.blocks;

import java.lang.reflect.Constructor;

import com.google.common.collect.ObjectArrays;
import com.mjr.mjrlegendslib.itemBlock.ItemBlockDefault;
import com.mjr.mjrlegendslib.util.RegisterUtilities;
import com.mjr.planetprogression.Config;
import com.mjr.planetprogression.Constants;
import com.mjr.planetprogression.PlanetProgression;
import com.mjr.planetprogression.itemBlocks.ItemBlockBasic;
import com.mjr.planetprogression.itemBlocks.ItemBlockCustomLandingPad;
import com.mjr.planetprogression.tileEntities.*;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

import micdoodle8.mods.galacticraft.core.items.ItemBlockDummy;

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
			SATTLLITE_LAUNCHER = new BlockSatelliteRocketLauncher("satellite_launcher");
			ADVANCED_LAUCHPAD = new BlockCustomLandingPad("advanced_launch_pad");
			ADVANCED_LAUCHPAD_FULL = new BlockCustomLandingPadFull("advanced_launch_pad_full");
			FAKE_BLOCK = new BlockCustomMulti("block_multi");
		}
	}

	public static void registerBlocks() throws NoSuchMethodException {
		registerBlock(TELESCOPE, ItemBlockBasic.class, TELESCOPE.getUnlocalizedName().substring(5));
		registerBlock(FAKE_TELESCOPE, ItemBlockDefault.class, FAKE_TELESCOPE.getUnlocalizedName().substring(5));
		if (Config.researchMode == 2 || Config.researchMode == 3) {
			registerBlock(SATTLLITE_BUILDER, ItemBlockBasic.class, SATTLLITE_BUILDER.getUnlocalizedName().substring(5));
			registerBlock(SATTLLITE_CONTROLLER, ItemBlockBasic.class, SATTLLITE_CONTROLLER.getUnlocalizedName().substring(5));
			registerBlock(SATTLLITE_LAUNCHER, ItemBlockBasic.class, SATTLLITE_LAUNCHER.getUnlocalizedName().substring(5));
			registerBlock(ADVANCED_LAUCHPAD, ItemBlockCustomLandingPad.class, ADVANCED_LAUCHPAD.getUnlocalizedName().substring(5));
			registerBlock(ADVANCED_LAUCHPAD_FULL, ItemBlockDefault.class, ADVANCED_LAUCHPAD_FULL.getUnlocalizedName().substring(5));
			registerBlock(FAKE_BLOCK, ItemBlockDummy.class, FAKE_BLOCK.getUnlocalizedName().substring(5));
		}
	}

	private static void registerTileEntitys() {
		RegisterUtilities.registerTileEntity(TileEntityTelescope.class, Constants.modName + "Telescope");
		RegisterUtilities.registerTileEntity(TileEntityTelescopeFake.class, Constants.modName + "Telescope Fake");
		if (Config.researchMode == 2 || Config.researchMode == 3) {
			RegisterUtilities.registerTileEntity(TileEntitySatelliteBuilder.class, Constants.modName + "SatelliteBuilder");
			RegisterUtilities.registerTileEntity(TileEntitySatelliteController.class, Constants.modName + "SatelliteController");
			RegisterUtilities.registerTileEntity(TileEntitySatelliteRocketLauncher.class, Constants.modName + "SatelliteLauncher");
			RegisterUtilities.registerTileEntity(TileEntitySatelliteLandingPadSingle.class, "Satellite Landing Pad");
			RegisterUtilities.registerTileEntity(TileEntitySatelliteLandingPad.class, "Satellite Landing Pad Full");
		}
	}

	private static void setHarvestLevels() {

	}

	private static void OreDictionaryRegister() {

	}
}
