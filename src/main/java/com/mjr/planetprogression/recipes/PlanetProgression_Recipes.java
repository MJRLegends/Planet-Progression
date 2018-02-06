package com.mjr.planetprogression.recipes;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCItems;
import net.minecraft.item.ItemStack;

import com.mjr.mjrlegendslib.util.RecipeUtilities;
import com.mjr.planetprogression.Config;
import com.mjr.planetprogression.blocks.PlanetProgression_Blocks;
import com.mjr.planetprogression.item.PlanetProgression_Items;

public class PlanetProgression_Recipes {

	public static void init() {
		registerMachineRecipes();
		if (Config.researchMode == 2 || Config.researchMode == 3)
			SatelliteRocketRecipes.registerRocketCraftingRecipe();
	}

	private static void registerMachineRecipes() {
		if (Config.researchMode == 2) {
			MachineRecipeManager.addRecipe(new ItemStack(PlanetProgression_Items.satelliteBasic), new ItemStack[] { new ItemStack(GCItems.basicItem, 1, 1), new ItemStack(GCItems.basicItem, 1, 1),
					new ItemStack(PlanetProgression_Items.satelliteBasicModule, 1, 0) });
		}
		if (Config.researchMode == 2 || Config.researchMode == 3) {
			RecipeUtilities.addOreRecipe(new ItemStack(PlanetProgression_Blocks.SATTLLITE_BUILDER), new Object[] { "SSS", "WAW", "SSS", 'S', new ItemStack(GCItems.basicItem, 1, 8), 'A', new ItemStack(GCItems.basicItem, 1, 14), 'W',
					new ItemStack(GCBlocks.aluminumWire, 1, 1) });
			RecipeUtilities.addOreRecipe(new ItemStack(PlanetProgression_Blocks.SATTLLITE_CONTROLLER), new Object[] { "STS", "WAW", "STS", 'S', new ItemStack(GCItems.basicItem, 1, 8), 'A', new ItemStack(GCItems.basicItem, 1, 14), 'W',
					new ItemStack(GCBlocks.aluminumWire, 1, 1), 'T', new ItemStack(PlanetProgression_Items.satelliteBasic, 1) });
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
