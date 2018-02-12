package com.mjr.planetprogression.jei.satelliteBuilder;

import java.util.List;

import javax.annotation.Nonnull;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import net.minecraft.item.ItemStack;

@SuppressWarnings("deprecation")
public class SatelliteBuilderRecipeWrapper extends BlankRecipeWrapper implements ICraftingRecipeWrapper {
	@Nonnull
	private final List<ItemStack> input;
	@Nonnull
	private final ItemStack output;

	public SatelliteBuilderRecipeWrapper(@Nonnull List<ItemStack> input, @Nonnull ItemStack output) {
		this.input = input;
		this.output = output;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputs(ItemStack.class, this.input);
		ingredients.setOutput(ItemStack.class, this.output);
	}
}
