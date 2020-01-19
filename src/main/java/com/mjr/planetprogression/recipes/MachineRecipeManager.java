package com.mjr.planetprogression.recipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableMap;

import net.minecraft.item.ItemStack;

public class MachineRecipeManager {
	private static HashMap<ItemStack[], ItemStack> satelliteBuilderRecipes = new HashMap<ItemStack[], ItemStack>();
	public static ArrayList<ArrayList<ItemStack>> satelliteBuilderSlotValidItems = new ArrayList<ArrayList<ItemStack>>(3);

	public static void addRecipe(ItemStack output, ItemStack[] inputList) {
		if (inputList.length != 3) {
			throw new RuntimeException("Invalid recipe!");
		}

		MachineRecipeManager.satelliteBuilderRecipes.put(inputList, output);
		if (MachineRecipeManager.satelliteBuilderSlotValidItems.size() == 0) {
			for (int i = 0; i < 3; i++) {
				ArrayList<ItemStack> entry = new ArrayList<ItemStack>();
				MachineRecipeManager.satelliteBuilderSlotValidItems.add(entry);
			}
		}
		for (int i = 0; i < 3; i++) {
			ItemStack inputStack = inputList[i];
			if (inputStack == null) {
				continue;
			}

			ArrayList<ItemStack> validItems = MachineRecipeManager.satelliteBuilderSlotValidItems.get(i);

			boolean found = false;
			for (int j = 0; j < validItems.size(); j++) {
				if (inputStack.isItemEqual(validItems.get(j))) {
					found = true;
					break;
				}
			}
			if (!found) {
				validItems.add(inputStack.copy());
			}
		}
	}

	public static ItemStack getOutputForInput(ItemStack[] inputList) {
		if (inputList.length != 3) {
			return null;
		}

		for (Entry<ItemStack[], ItemStack> recipe : MachineRecipeManager.satelliteBuilderRecipes.entrySet()) {
			boolean found = true;

			for (int i = 0; i < 3; i++) {
				ItemStack recipeStack = recipe.getKey()[i];
				ItemStack inputStack = inputList[i];

				if (recipeStack == null || inputStack == null) {
					if (recipeStack != null || inputStack != null) {
						found = false;
					}
				} else if (recipeStack.getItem() != inputStack.getItem() || recipeStack.getItemDamage() != inputStack.getItemDamage()) {
					found = false;
				}
			}

			if (!found) {
				continue;
			}

			return recipe.getValue();
		}

		return MachineRecipeManager.satelliteBuilderRecipes.get(inputList);
	}

	public static void removeRecipe(ItemStack match) {
		for (Iterator<Map.Entry<ItemStack[], ItemStack>> it = MachineRecipeManager.satelliteBuilderRecipes.entrySet().iterator(); it.hasNext();) {
			Map.Entry<ItemStack[], ItemStack> recipe = it.next();
			if (ItemStack.areItemStacksEqual(match, recipe.getValue()))
				it.remove();
		}
	}

	public static ImmutableMap<ItemStack[], ItemStack> getRecipes() {
		return ImmutableMap.copyOf(satelliteBuilderRecipes);
	}
}
