package com.mjr.planetprogression.jei;

import javax.annotation.Nonnull;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;

@JEIPlugin
public class PlanetProgressionJEI extends BlankModPlugin {
	@Override
	public void register(@Nonnull IModRegistry registry) {
		IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
		registry.addRecipeCategories(new SatelliteRocketRecipeCategory(guiHelper));
		registry.addRecipeHandlers(new SatelliteRocketRecipeHandler());
		registry.addRecipes(SatelliteRocketRecipeMaker.getRecipesList());
	}
}