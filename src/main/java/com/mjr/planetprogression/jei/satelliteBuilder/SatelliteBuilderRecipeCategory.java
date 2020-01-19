package com.mjr.planetprogression.jei.satelliteBuilder;

import javax.annotation.Nonnull;

import com.mjr.mjrlegendslib.util.TranslateUtilities;
import com.mjr.planetprogression.Constants;
import com.mjr.planetprogression.jei.RecipeCategories;

import net.minecraft.util.ResourceLocation;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;

public class SatelliteBuilderRecipeCategory extends BlankRecipeCategory<IRecipeWrapper> {
	private static final ResourceLocation guiTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/satellite_builder.png");

	@Nonnull
	private final IDrawable background;
	@Nonnull
	private final String localizedName;

	@Nonnull
	public SatelliteBuilderRecipeCategory(IGuiHelper guiHelper) {
		this.background = guiHelper.createDrawable(guiTexture, 3, 4, 168, 80);
		this.localizedName = TranslateUtilities.translate("container.satellite.builder.name");
	}

	@Nonnull
	@Override
	public String getUid() {
		return RecipeCategories.SATELLITE_BUILDER_ID;
	}

	@Nonnull
	@Override
	public String getTitle() {
		return this.localizedName;
	}

	@Nonnull
	@Override
	public IDrawable getBackground() {
		return this.background;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup itemstacks = recipeLayout.getItemStacks();

		itemstacks.init(0, true, 16, 26);
		itemstacks.init(1, true, 43, 26);
		itemstacks.init(2, true, 70, 26);
		itemstacks.init(3, false, 111, 26);

		itemstacks.set(ingredients);

	}

	@Override
	public String getModName() {
		return Constants.modName;
	}
}
