package com.mjr.planetprogression.recipes;

import micdoodle8.mods.galacticraft.core.GCItems;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import com.mjr.planetprogression.Config;
import com.mjr.planetprogression.item.PlanetProgression_Items;

public class PlanetProgression_Recipes {

	public static void init() {
		registerRecipes();
	}
	
	public static void initRocketRecipes() {
		if (Config.researchMode == 2 || Config.researchMode == 3)
			SatelliteRocketRecipes.registerRocketCraftingRecipe();
	}

	private static void registerRecipes() {
		if (Config.researchMode == 2) {
			NonNullList<ItemStack> input1 = NonNullList.create();
			input1.add(new ItemStack(GCItems.basicItem, 12, 1));
			input1.add(new ItemStack(GCItems.basicItem, 12, 1));
			input1.add(new ItemStack(PlanetProgression_Items.satelliteBasicModule, 6, 0));
			MachineRecipeManager.addRecipe(new ItemStack(PlanetProgression_Items.satelliteBasic), input1);

		}
		if (Config.researchMode == 3) {
			NonNullList<ItemStack> input1 = NonNullList.create();
			input1.add(new ItemStack(GCItems.basicItem, 1, 1));
			input1.add(new ItemStack(GCItems.basicItem, 1, 1));
			input1.add(new ItemStack(PlanetProgression_Items.satelliteModules, 1, 0));
			MachineRecipeManager.addRecipe(new ItemStack(PlanetProgression_Items.satelliteSurface), input1);
			input1 = NonNullList.create();
			input1.add(new ItemStack(GCItems.basicItem, 1, 1));
			input1.add(new ItemStack(GCItems.basicItem, 1, 1));
			input1.add(new ItemStack(PlanetProgression_Items.satelliteModules, 1, 1));
			MachineRecipeManager.addRecipe(new ItemStack(PlanetProgression_Items.satelliteDistance), input1);
			input1 = NonNullList.create();
			input1.add(new ItemStack(GCItems.basicItem, 1, 1));
			input1.add(new ItemStack(GCItems.basicItem, 1, 1));
			input1.add(new ItemStack(PlanetProgression_Items.satelliteModules, 1, 2));
			MachineRecipeManager.addRecipe(new ItemStack(PlanetProgression_Items.satelliteAtmosphere), input1);
		}
	}
}
