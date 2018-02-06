package com.mjr.planetprogression.jei;

import javax.annotation.Nonnull;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;

import com.mjr.planetprogression.jei.satelliteBuilder.SatelliteBuilderRecipeCategory;
import com.mjr.planetprogression.jei.satelliteBuilder.SatelliteBuilderRecipeHandler;
import com.mjr.planetprogression.jei.satelliteBuilder.SatelliteBuilderRecipeMaker;

@JEIPlugin
public class PlanetProgressionJEI extends BlankModPlugin {
	@Override
	public void register(@Nonnull IModRegistry registry) {
		IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
		registry.addRecipeCategories(new SatelliteRocketRecipeCategory(guiHelper), new SatelliteBuilderRecipeCategory(guiHelper));
		registry.addRecipeHandlers(new SatelliteRocketRecipeHandler(), new SatelliteBuilderRecipeHandler());
		registry.addRecipes(SatelliteRocketRecipeMaker.getRecipesList());
		registry.addRecipes(SatelliteBuilderRecipeMaker.getRecipesList());
	}
}