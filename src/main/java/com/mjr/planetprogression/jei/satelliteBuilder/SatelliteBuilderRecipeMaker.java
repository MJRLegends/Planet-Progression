package com.mjr.planetprogression.jei.satelliteBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import com.mjr.planetprogression.recipes.MachineRecipeManager;

public class SatelliteBuilderRecipeMaker {
	public static List<SatelliteBuilderRecipeWrapper> getRecipesList() {
		List<SatelliteBuilderRecipeWrapper> recipes = new ArrayList<>();
		SatelliteBuilderRecipeWrapper wrapper;
		for (Entry<NonNullList<ItemStack>, ItemStack> temp : MachineRecipeManager.getRecipes().entrySet()) {
			wrapper = new SatelliteBuilderRecipeWrapper(temp.getKey(), temp.getValue());
			recipes.add(wrapper);
		}

		return recipes;
	}
}
