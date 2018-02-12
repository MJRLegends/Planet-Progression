package com.mjr.planetprogression.jei;

import javax.annotation.Nonnull;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraft.util.ResourceLocation;

import com.mjr.mjrlegendslib.util.TranslateUtilities;
import com.mjr.planetprogression.Constants;

public class SatelliteRocketRecipeCategory extends BlankRecipeCategory<IRecipeWrapper> {
	private static final ResourceLocation rocketGuiTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/schematic_rocket_t3_recipe.png");

	@Nonnull
	private final IDrawable background;
	@Nonnull
	private final String localizedName;

	public SatelliteRocketRecipeCategory(IGuiHelper guiHelper) {
		this.background = guiHelper.createDrawable(rocketGuiTexture, 0, 0, 168, 126);
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

		itemstacks.init(0, true, 44, 0);
		itemstacks.init(1, true, 35, 18);
		itemstacks.init(2, true, 35, 36);
		itemstacks.init(3, true, 35, 54);
		itemstacks.init(4, true, 35, 72);
		itemstacks.init(5, true, 35, 90);
		itemstacks.init(6, true, 53, 18);
		itemstacks.init(7, true, 53, 36);
		itemstacks.init(8, true, 53, 54);
		itemstacks.init(9, true, 53, 72);
		itemstacks.init(10, true, 53, 90);
		itemstacks.init(11, true, 17, 72); // Booster left
		itemstacks.init(12, true, 17, 90);
		itemstacks.init(13, true, 71, 90);
		itemstacks.init(14, true, 44, 108); // Rocket
		itemstacks.init(15, true, 71, 72); // Booster right
		itemstacks.init(16, true, 17, 108);
		itemstacks.init(17, true, 71, 108);
		itemstacks.init(18, true, 89, 7);
		itemstacks.init(19, true, 115, 7);
		itemstacks.init(20, true, 141, 7);
		itemstacks.init(21, false, 138, 95);

		itemstacks.set(ingredients);
	}

	@Override
	public String getModName() {
		return Constants.modName;
	}
}