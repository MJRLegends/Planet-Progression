package com.mjr.planetprogression.jei;

import javax.annotation.Nonnull;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;

import com.mjr.planetprogression.jei.satelliteBuilder.SatelliteBuilderRecipeCategory;
import com.mjr.planetprogression.jei.satelliteBuilder.SatelliteBuilderRecipeMaker;
import com.mjr.planetprogression.jei.satelliteBuilder.SatelliteBuilderRecipeWrapper;

@JEIPlugin
public class PlanetProgressionJEI extends BlankModPlugin {
	@Override
	public void register(@Nonnull IModRegistry registry) {
		registry.handleRecipes(INasaWorkbenchRecipe.class, SatelliteRocketRecipeWrapper::new, RecipeCategories.SATELLITE_ROCKET_ID);
		registry.handleRecipes(SatelliteBuilderRecipeWrapper.class, recipe -> recipe, RecipeCategories.SATELLITE_BUILDER_ID);
		registry.addRecipes(SatelliteBuilderRecipeMaker.getRecipesList(), RecipeCategories.SATELLITE_BUILDER_ID);
		registry.addRecipes(SatelliteRocketRecipeMaker.getRecipesList(), RecipeCategories.SATELLITE_ROCKET_ID);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
		registry.addRecipeCategories(new SatelliteRocketRecipeCategory(guiHelper), new SatelliteBuilderRecipeCategory(guiHelper));
	}
}