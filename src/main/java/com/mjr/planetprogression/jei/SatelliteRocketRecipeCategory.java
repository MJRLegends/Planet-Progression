package com.mjr.planetprogression.jei;

import javax.annotation.Nonnull;

import com.mjr.mjrlegendslib.util.TranslateUtilities;
import com.mjr.planetprogression.Constants;

import net.minecraft.util.ResourceLocation;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;

public class SatelliteRocketRecipeCategory extends BlankRecipeCategory<IRecipeWrapper> {
	private static final ResourceLocation rocketGuiTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/schematic_rocket_recipe.png");

	@Nonnull
	private final IDrawable background;
	@Nonnull
	private final String localizedName;

	public SatelliteRocketRecipeCategory(IGuiHelper guiHelper) {
		this.background = guiHelper.createDrawable(rocketGuiTexture, 0, 0, 168, 187);
		this.localizedName = TranslateUtilities.translate("tile.rocket_workbench.name");

	}

	@Nonnull
	@Override
	public String getUid() {
		return RecipeCategories.SATELLITE_ROCKET_ID;
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

		itemstacks.init(0, true, 37, 8);
		itemstacks.init(1, true, 19, 26);
		itemstacks.init(2, true, 19, 44);
		itemstacks.init(3, true, 19, 62);
		itemstacks.init(4, true, 19, 80);
		itemstacks.init(5, true, 19, 98);
		itemstacks.init(6, true, 19, 116);
		itemstacks.init(7, true, 19, 134);
		itemstacks.init(8, true, 19, 152);
		itemstacks.init(9, true, 37, 26);
		itemstacks.init(10, true, 37, 44);
		itemstacks.init(11, true, 37, 62);
		itemstacks.init(12, true, 37, 80);
		itemstacks.init(13, true, 37, 98);
		itemstacks.init(14, true, 37, 116);
		itemstacks.init(15, true, 37, 134);
		itemstacks.init(16, true, 37, 152);
		itemstacks.init(17, true, 55, 26);
		itemstacks.init(18, true, 55, 44);
		itemstacks.init(19, true, 55, 62);
		itemstacks.init(20, true, 55, 80);
		itemstacks.init(21, true, 55, 98);
		itemstacks.init(22, true, 55, 116);
		itemstacks.init(23, true, 55, 134);
		itemstacks.init(24, true, 55, 152);
		itemstacks.init(25, true, 1, 62); // Booster left
		itemstacks.init(26, true, 1, 80); // Booster left
		itemstacks.init(27, true, 37, 170); // engine
		itemstacks.init(28, true, 73, 62); // Booster right
		itemstacks.init(29, true, 73, 80); // Booster right
		itemstacks.init(30, true, 89, 1); // Booster right
		itemstacks.init(31, true, 115, 1); // Booster right
		itemstacks.init(32, true, 141, 1); // Booster right
		itemstacks.init(33, false, 138, 103); // Booster right

		itemstacks.set(ingredients);
	}

	@Override
	public String getModName() {
		return Constants.modName;
	}
}