package com.mjr.planetprogression.jei;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import micdoodle8.mods.galacticraft.api.recipe.INasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.core.GCBlocks;

import com.mjr.planetprogression.blocks.PlanetProgression_Blocks;
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
		
        registry.addRecipeCatalyst(new ItemStack(GCBlocks.nasaWorkbench), RecipeCategories.SATELLITE_ROCKET_ID);
        registry.addRecipeCatalyst(new ItemStack(PlanetProgression_Blocks.SATTLLITE_BUILDER), RecipeCategories.SATELLITE_BUILDER_ID);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
		registry.addRecipeCategories(new SatelliteRocketRecipeCategory(guiHelper), new SatelliteBuilderRecipeCategory(guiHelper));
	}
}