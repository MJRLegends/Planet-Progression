package com.mjr.planetprogression.recipes;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.mjr.mjrlegendslib.recipe.RecipeDumper;
import com.mjr.planetprogression.Constants;
import com.mjr.planetprogression.blocks.PlanetProgression_Blocks;
import com.mjr.planetprogression.item.PlanetProgression_Items;

public class PlanetProgression_RecipeGeneration {
	public static void generateConstants() {
		RecipeDumper.generateConstants();
	}

	public static void generate() {
		RecipeDumper.addShapedRecipe(new ItemStack(PlanetProgression_Blocks.TELESCOPE), new Object[] { "TDT", "SGS", "TDT", 'T', new ItemStack(GCItems.basicItem, 1, 7), 'D', new ItemStack(Blocks.DIAMOND_BLOCK), 'G', new ItemStack(Blocks.GLASS_PANE),
				'S', new ItemStack(GCItems.basicItem, 1, 8) });
		RecipeDumper.addShapedRecipeWithCondition(Constants.modID, "recipe_enabled", "research_mode_2", new ItemStack(PlanetProgression_Items.satelliteBasicModule), new Object[] { "WAW", "CGC", "WAW", 'G', new ItemStack(Items.DYE, 1, 2), 'C',
				new ItemStack(GCItems.basicItem, 1, 10), 'A', new ItemStack(GCItems.basicItem, 1, 14), 'W', new ItemStack(GCBlocks.aluminumWire, 1, 1) });
		RecipeDumper.addShapedRecipeWithCondition(Constants.modID, "recipe_enabled", "research_mode_2", new ItemStack(PlanetProgression_Blocks.SATTLLITE_BUILDER), new Object[] { "SSS", "WAW", "SSS", 'S', new ItemStack(GCItems.basicItem, 1, 8), 'A',
				new ItemStack(GCItems.basicItem, 1, 14), 'W', new ItemStack(GCBlocks.aluminumWire, 1, 1) });
		RecipeDumper.addShapedRecipeWithCondition(Constants.modID, "recipe_enabled", "research_mode_2", new ItemStack(PlanetProgression_Blocks.SATTLLITE_CONTROLLER), new Object[] { "STS", "WAW", "STS", 'S', new ItemStack(GCItems.basicItem, 1, 8),
				'A', new ItemStack(GCItems.basicItem, 1, 14), 'W', new ItemStack(GCBlocks.aluminumWire, 1, 1), 'T', new ItemStack(PlanetProgression_Items.satelliteBasic, 1) });
	}
}