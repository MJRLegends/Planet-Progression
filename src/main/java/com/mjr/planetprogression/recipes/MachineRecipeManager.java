package com.mjr.planetprogression.recipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import com.google.common.collect.ImmutableMap;

public class MachineRecipeManager {
	private static HashMap<NonNullList<ItemStack>, ItemStack> satelliteBuilderRecipes = new HashMap<>();
	public static ArrayList<ArrayList<ItemStack>> satelliteBuilderSlotValidItems = new ArrayList<ArrayList<ItemStack>>(3);

	public static void addRecipe(ItemStack output, NonNullList<ItemStack> inputList) {
		if (inputList.size() != 3) {
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
			ItemStack inputStack = inputList.get(i);
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

	public static ItemStack getOutputForInput(List<ItemStack> list) {
		if (list.size() != 3) {
			return null;
		}

		for (Entry<NonNullList<ItemStack>, ItemStack> recipe : MachineRecipeManager.satelliteBuilderRecipes.entrySet()) {
			boolean found = true;

			for (int i = 0; i < 3; i++) {
				ItemStack recipeStack = recipe.getKey().get(i);
				ItemStack inputStack = list.get(i);

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

		return MachineRecipeManager.satelliteBuilderRecipes.get(list);
	}

	public static void removeRecipe(ItemStack match) {
		MachineRecipeManager.satelliteBuilderRecipes.entrySet().removeIf(recipe -> ItemStack.areItemStacksEqual(match, recipe.getValue()));
	}

	public static ImmutableMap<NonNullList<ItemStack>, ItemStack> getRecipes() {
		return ImmutableMap.copyOf(satelliteBuilderRecipes);
	}
}
