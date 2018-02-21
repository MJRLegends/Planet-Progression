package com.mjr.planetprogression.jei.satelliteBuilder;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import micdoodle8.mods.galacticraft.core.util.GCLog;

import com.mjr.planetprogression.jei.RecipeCategories;

public class SatelliteBuilderRecipeHandler implements IRecipeHandler<SatelliteBuilderRecipeWrapper> {
	@Nonnull
	@Override
	public Class<SatelliteBuilderRecipeWrapper> getRecipeClass() {
		return SatelliteBuilderRecipeWrapper.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return RecipeCategories.SATELLITE_BUILDER_ID;
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull SatelliteBuilderRecipeWrapper recipe) {
		return recipe;
	}

	@Override
	public boolean isRecipeValid(@Nonnull SatelliteBuilderRecipeWrapper recipe) {
		if (recipe.getInputs().size() != 3) {
			GCLog.severe(this.getClass().getSimpleName() + " JEI recipe has wrong number of inputs!");
		}
		if (recipe.getOutputs().size() != 1) {
			GCLog.severe(this.getClass().getSimpleName() + " JEI recipe has wrong number of outputs!");
		}
		return true;
	}
}
