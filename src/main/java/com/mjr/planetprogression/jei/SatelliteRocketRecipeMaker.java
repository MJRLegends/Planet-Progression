package com.mjr.planetprogression.jei;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;

import com.mjr.planetprogression.recipes.SatelliteRocketRecipes;

public class SatelliteRocketRecipeMaker {
	public static List<SatelliteRocketRecipeWrapper> getRecipesList() {
		List<SatelliteRocketRecipeWrapper> recipes = new ArrayList<>();

		for (INasaWorkbenchRecipe recipe : SatelliteRocketRecipes.getSatelliteRocketRecipes()) {
			SatelliteRocketRecipeWrapper wrapper = new SatelliteRocketRecipeWrapper(recipe);
			recipes.add(wrapper);
		}

		return recipes;
	}
}