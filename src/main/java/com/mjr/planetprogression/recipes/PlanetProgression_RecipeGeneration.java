package com.mjr.planetprogression.recipes;

import com.mjr.mjrlegendslib.recipe.RecipeDumper;
import com.mjr.planetprogression.Constants;
import com.mjr.planetprogression.blocks.PlanetProgression_Blocks;
import com.mjr.planetprogression.item.PlanetProgression_Items;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCItems;

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

		RecipeDumper.addShapedRecipeWithCondition(Constants.modID, "recipe_enabled", "research_mode_2", new ItemStack(PlanetProgression_Blocks.SATTLLITE_LAUNCHER, 1, 0), new Object[] { "ZVZ", "YXY", "ZWZ", 'V', new ItemStack(GCItems.basicItem, 1, 19), 'W', new ItemStack(GCBlocks.aluminumWire, 1, 0), 'X', new ItemStack(GCItems.basicItem, 1, 14), 'Y', new ItemStack(GCItems.basicItem, 1, 9), 'Z', new ItemStack(GCItems.basicItem, 1, 9) });
		RecipeDumper.addShapedRecipeWithCondition(Constants.modID, "recipe_enabled", "research_mode_2", new ItemStack(PlanetProgression_Blocks.ADVANCED_LAUCHPAD, 9, 0), new Object[] { "YYY", "YYY", "XXX", 'X', "blockIron", 'Y', "compressedIron" });
		RecipeDumper.addShapedRecipeWithCondition(Constants.modID, "recipe_enabled", "research_mode_2", new ItemStack(PlanetProgression_Items.DISH_KEYCARD, 1, 0), new Object[] { "YYY", "ZZZ", "XXX", 'X', "blockIron", 'Y', "compressedIron", 'Z', new ItemStack(GCBlocks.radioTelescope, 1, 0) });

	}
}