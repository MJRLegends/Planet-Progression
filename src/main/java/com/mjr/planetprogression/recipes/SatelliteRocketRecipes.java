package com.mjr.planetprogression.recipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mjr.planetprogression.inventory.InventorySchematicSatelliteRocket;
import com.mjr.planetprogression.item.PlanetProgression_Items;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.recipe.NasaWorkbenchRecipe;

public class SatelliteRocketRecipes {
	private static List<INasaWorkbenchRecipe> satelliteRocketRecipes = new ArrayList<INasaWorkbenchRecipe>();

	public static ItemStack findMatchingSatelliteRocketRecipe(InventorySchematicSatelliteRocket inventoryRocketBench) {
		for (INasaWorkbenchRecipe recipe : satelliteRocketRecipes) {
			if (recipe.matches(inventoryRocketBench)) {
				return recipe.getRecipeOutput();
			}
		}
		return ItemStack.EMPTY;
	}

	public static void addSatelliteRocketRecipe(ItemStack result, HashMap<Integer, ItemStack> input) {
		addSatelliteRocketRecipe(new NasaWorkbenchRecipe(result, input));
	}

	public static void addSatelliteRocketRecipe(INasaWorkbenchRecipe recipe) {
		satelliteRocketRecipes.add(recipe);
	}

	public static List<INasaWorkbenchRecipe> getSatelliteRocketRecipes() {
		return satelliteRocketRecipes;
	}

	public static void removeAllSatelliteRocketRecipes() {
		satelliteRocketRecipes.clear();
	}

	public static void registerRocketCraftingRecipe() {
		HashMap<Integer, ItemStack> input = new HashMap<Integer, ItemStack>();
		input.put(1, new ItemStack(GCItems.partNoseCone)); // Cone
		// Body
		input.put(2, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(3, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(4, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(5, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(6, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(7, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(8, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(9, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(10, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(11, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(12, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(13, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(14, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(15, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(16, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(17, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(18, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(19, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(20, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(21, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(22, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(23, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(24, new ItemStack(GCItems.heavyPlatingTier1));
		input.put(25, new ItemStack(GCItems.heavyPlatingTier1));

		input.put(26, new ItemStack(GCItems.partFins)); // Fin
		input.put(27, new ItemStack(GCItems.partFins)); // Fin
		input.put(28, new ItemStack(GCItems.rocketEngine)); // Engine
		input.put(29, new ItemStack(GCItems.partFins)); // Fin
		input.put(30, new ItemStack(GCItems.partFins)); // Fin
		input.put(31, ItemStack.EMPTY);

		HashMap<Integer, ItemStack> input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(31, new ItemStack(Blocks.CHEST));
		
		SatelliteRocketRecipes.addSatelliteRocketRecipe(new NasaWorkbenchRecipe(new ItemStack(PlanetProgression_Items.SATELLITE_ROCKET, 1, 1), input2));
	}
}
