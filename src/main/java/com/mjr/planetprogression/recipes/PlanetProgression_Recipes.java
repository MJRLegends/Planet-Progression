package com.mjr.planetprogression.recipes;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import com.mjr.mjrlegendslib.util.RecipeUtilities;
import com.mjr.planetprogression.Config;
import com.mjr.planetprogression.blocks.PlanetProgression_Blocks;
import com.mjr.planetprogression.item.PlanetProgression_Items;

public class PlanetProgression_Recipes {

	public static void init() {
		registerRecipes();
		if (Config.researchMode == 2 || Config.researchMode == 3)
			SatelliteRocketRecipes.registerRocketCraftingRecipe();
	}

	private static void registerRecipes() {
		RecipeUtilities.addShapedRecipe(new ItemStack(PlanetProgression_Blocks.TELESCOPE), new Object[] { "TDT", "SGS", "TDT", 'T', new ItemStack(GCItems.basicItem, 1, 7), 'D', new ItemStack(Blocks.DIAMOND_BLOCK), 'G',
				new ItemStack(Blocks.GLASS_PANE), 'S', new ItemStack(GCItems.basicItem, 1, 8) });
		if (Config.researchMode == 2) {
			NonNullList<ItemStack> input1 = NonNullList.create();
			input1.add(new ItemStack(GCItems.basicItem, 12, 1));
			input1.add(new ItemStack(GCItems.basicItem, 12, 1));
			input1.add(new ItemStack(PlanetProgression_Items.satelliteBasicModule, 6, 0));
			MachineRecipeManager.addRecipe(new ItemStack(PlanetProgression_Items.satelliteBasic), input1);
			RecipeUtilities.addShapedRecipe(new ItemStack(PlanetProgression_Items.satelliteBasicModule), new Object[] { "WAW", "CGC", "WAW", 'G', new ItemStack(Items.DYE, 1, 2), 'C', new ItemStack(GCItems.basicItem, 1, 10), 'A',
					new ItemStack(GCItems.basicItem, 1, 14), 'W', new ItemStack(GCBlocks.aluminumWire, 1, 1) });
		}
		if (Config.researchMode == 2 || Config.researchMode == 3) {
			RecipeUtilities.addShapedRecipe(new ItemStack(PlanetProgression_Blocks.SATTLLITE_BUILDER), new Object[] { "SSS", "WAW", "SSS", 'S', new ItemStack(GCItems.basicItem, 1, 8), 'A', new ItemStack(GCItems.basicItem, 1, 14), 'W',
					new ItemStack(GCBlocks.aluminumWire, 1, 1) });
			RecipeUtilities.addShapedRecipe(new ItemStack(PlanetProgression_Blocks.SATTLLITE_CONTROLLER), new Object[] { "STS", "WAW", "STS", 'S', new ItemStack(GCItems.basicItem, 1, 8), 'A', new ItemStack(GCItems.basicItem, 1, 14), 'W',
					new ItemStack(GCBlocks.aluminumWire, 1, 1), 'T', new ItemStack(PlanetProgression_Items.satelliteBasic, 1) });
		}
		if (Config.researchMode == 3) {
			NonNullList<ItemStack> input1 = NonNullList.create();
			input1.add(new ItemStack(GCItems.basicItem, 1, 1));
			input1.add(new ItemStack(GCItems.basicItem, 1, 1));
			input1.add(new ItemStack(PlanetProgression_Items.satelliteModules, 1, 0));

			MachineRecipeManager.addRecipe(new ItemStack(PlanetProgression_Items.satelliteSurface), input1);
			input1 = NonNullList.create();
			input1.add(new ItemStack(GCItems.basicItem, 1, 1));
			input1.add(new ItemStack(GCItems.basicItem, 1, 1));
			input1.add(new ItemStack(PlanetProgression_Items.satelliteModules, 1, 1));
			MachineRecipeManager.addRecipe(new ItemStack(PlanetProgression_Items.satelliteDistance), input1);
			input1 = NonNullList.create();
			input1.add(new ItemStack(GCItems.basicItem, 1, 1));
			input1.add(new ItemStack(GCItems.basicItem, 1, 1));
			input1.add(new ItemStack(PlanetProgression_Items.satelliteModules, 1, 2));
			MachineRecipeManager.addRecipe(new ItemStack(PlanetProgression_Items.satelliteAtmosphere), input1);
		}
	}
}
