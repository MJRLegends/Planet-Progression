package com.mjr.planetprogression.jei;

import javax.annotation.Nonnull;

import micdoodle8.mods.galacticraft.core.util.GCLog;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class SatelliteRocketRecipeHandler implements IRecipeHandler<SatelliteRocketRecipeWrapper> {
	@Nonnull
	@Override
	public Class<SatelliteRocketRecipeWrapper> getRecipeClass() {
		return SatelliteRocketRecipeWrapper.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return RecipeCategories.SATELLITE_ROCKET_ID;
	}

	@Override
	public String getRecipeCategoryUid(SatelliteRocketRecipeWrapper recipe) {
		return this.getRecipeCategoryUid();
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull SatelliteRocketRecipeWrapper recipe) {
		return recipe;
	}

	@Override
	public boolean isRecipeValid(@Nonnull SatelliteRocketRecipeWrapper recipe) {
		if (recipe.getInputs().size() != 33) {
			GCLog.severe(this.getClass().getSimpleName() + " JEI recipe has wrong number of inputs!");
		}
		if (recipe.getOutputs().size() != 1) {
			GCLog.severe(this.getClass().getSimpleName() + " JEI recipe has wrong number of outputs!");
		}
		return true;
	}
}