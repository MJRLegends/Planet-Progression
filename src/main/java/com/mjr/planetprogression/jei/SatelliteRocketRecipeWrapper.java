package com.mjr.planetprogression.jei;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;

import net.minecraft.item.ItemStack;

import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;

@SuppressWarnings("deprecation")
public class SatelliteRocketRecipeWrapper extends BlankRecipeWrapper implements ICraftingRecipeWrapper {
	@Nonnull
	private final INasaWorkbenchRecipe recipe;

	public SatelliteRocketRecipeWrapper(@Nonnull INasaWorkbenchRecipe recipe) {
		this.recipe = recipe;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputs(ItemStack.class, Lists.newArrayList(recipe.getRecipeInput().values()));
		ingredients.setOutput(ItemStack.class, this.recipe.getRecipeOutput());
	}
}