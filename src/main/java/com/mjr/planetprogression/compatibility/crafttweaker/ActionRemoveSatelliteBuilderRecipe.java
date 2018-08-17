package com.mjr.planetprogression.compatibility.crafttweaker;

import com.mjr.planetprogression.recipes.MachineRecipeManager;

import crafttweaker.IAction;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;

public class ActionRemoveSatelliteBuilderRecipe implements IAction {

	private final IItemStack output;

	public ActionRemoveSatelliteBuilderRecipe(IItemStack output2) {
		this.output = output2;
	}

	@Override
	public void apply() {
		MachineRecipeManager.removeRecipe(CraftTweakerMC.getItemStack(this.output));

	}

	@Override
	public String describe() {
		return "Removing Satellite Builder Recipe: Output" + this.output;
	}

}
