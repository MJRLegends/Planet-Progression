package com.mjr.planetprogression.recipes;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.mjr.mjrlegendslib.util.RecipeUtilities;
import com.mjr.planetprogression.Config;
import com.mjr.planetprogression.blocks.PlanetProgression_Blocks;
import com.mjr.planetprogression.item.PlanetProgression_Items;

public class PlanetProgression_Recipes {

	public static void init() {
		registerRecipes();
		if (Config.researchMode == 2 || Config.researchMode == 3)
			SatelliteRocketRecipes.registerRocketCraftingRecipe();
	}

	private static void registerRecipes() {
		RecipeUtilities.addShapedRecipe(new ItemStack(PlanetProgression_Blocks.TELESCOPE), new Object[] { "TDT", "SGS", "TDT", 'T', new ItemStack(GCItems.basicItem, 1, 7), 'D', new ItemStack(Blocks.diamond_block), 'G',
				new ItemStack(Blocks.glass_pane), 'S', new ItemStack(GCItems.basicItem, 1, 8) });
		if (Config.researchMode == 2) {
			MachineRecipeManager.addRecipe(new ItemStack(PlanetProgression_Items.satelliteBasic), new ItemStack[] { new ItemStack(GCItems.basicItem, 12, 1), new ItemStack(GCItems.basicItem, 12, 1),
					new ItemStack(PlanetProgression_Items.satelliteBasicModule, 6, 0) });
			RecipeUtilities.addShapedRecipe(new ItemStack(PlanetProgression_Items.satelliteBasicModule), new Object[] { "WAW", "CGC", "WAW", 'G', new ItemStack(Items.dye, 1, 2), 'C', new ItemStack(GCItems.basicItem, 1, 10), 'A',
					new ItemStack(GCItems.basicItem, 1, 14), 'W', new ItemStack(GCBlocks.aluminumWire, 1, 1) });
		}
		if (Config.researchMode == 2 || Config.researchMode == 3) {
			RecipeUtilities.addShapedRecipe(new ItemStack(PlanetProgression_Blocks.SATTLLITE_BUILDER), new Object[] { "SSS", "WAW", "SSS", 'S', new ItemStack(GCItems.basicItem, 1, 8), 'A', new ItemStack(GCItems.basicItem, 1, 14), 'W',
					new ItemStack(GCBlocks.aluminumWire, 1, 1) });
			RecipeUtilities.addShapedRecipe(new ItemStack(PlanetProgression_Blocks.SATTLLITE_CONTROLLER), new Object[] { "STS", "WAW", "STS", 'S', new ItemStack(GCItems.basicItem, 1, 8), 'A', new ItemStack(GCItems.basicItem, 1, 14), 'W',
					new ItemStack(GCBlocks.aluminumWire, 1, 1), 'T', new ItemStack(PlanetProgression_Items.satelliteBasic, 1) });
			RecipeUtilities.addShapedRecipe(new ItemStack(PlanetProgression_Blocks.SATTLLITE_LAUNCHER, 1, 0), new Object[] { "ZVZ", "YXY", "ZWZ", 'V', new ItemStack(GCItems.basicItem, 1, 19), 'W', new ItemStack(GCBlocks.aluminumWire, 1, 0), 'X', new ItemStack(GCItems.basicItem, 1, 14), 'Y', new ItemStack(GCItems.basicItem, 1, 9), 'Z', new ItemStack(GCItems.basicItem, 1, 9) });
			RecipeUtilities.addOreRecipe(new ItemStack(PlanetProgression_Blocks.ADVANCED_LAUCHPAD, 9, 0), new Object[] { "YYY", "YYY", "XXX", 'X', "blockIron", 'Y', "compressedIron" });
		}
		if (Config.researchMode == 3) {
			MachineRecipeManager.addRecipe(new ItemStack(PlanetProgression_Items.satelliteSurface), new ItemStack[] { new ItemStack(GCItems.basicItem, 1, 1), new ItemStack(GCItems.basicItem, 1, 1),
					new ItemStack(PlanetProgression_Items.satelliteModules, 1, 0) });
			MachineRecipeManager.addRecipe(new ItemStack(PlanetProgression_Items.satelliteDistance), new ItemStack[] { new ItemStack(GCItems.basicItem, 1, 1), new ItemStack(GCItems.basicItem, 1, 1),
					new ItemStack(PlanetProgression_Items.satelliteModules, 1, 1) });
			MachineRecipeManager.addRecipe(new ItemStack(PlanetProgression_Items.satelliteAtmosphere), new ItemStack[] { new ItemStack(GCItems.basicItem, 1, 1), new ItemStack(GCItems.basicItem, 1, 1),
					new ItemStack(PlanetProgression_Items.satelliteModules, 1, 2) });
		}
	}
}
