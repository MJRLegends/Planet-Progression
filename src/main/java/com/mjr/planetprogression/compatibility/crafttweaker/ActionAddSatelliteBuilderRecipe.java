package com.mjr.planetprogression.compatibility.crafttweaker;

import com.mjr.planetprogression.recipes.MachineRecipeManager;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import crafttweaker.IAction;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;

public class ActionAddSatelliteBuilderRecipe implements IAction {

	private final IItemStack input1;
	private final IItemStack input2;
	private final IItemStack input3;
	private final IItemStack output;

	public ActionAddSatelliteBuilderRecipe(IItemStack input1, IItemStack input2, IItemStack input3, IItemStack output2) {
		this.input1 = input1;
		this.input2 = input2;
		this.input3 = input3;
		this.output = output2;
	}

	@Override
	public void apply() {
		NonNullList<ItemStack> inputs = NonNullList.create();
		inputs.add(CraftTweakerMC.getItemStack(this.input1));
		inputs.add(CraftTweakerMC.getItemStack(this.input2));
		inputs.add(CraftTweakerMC.getItemStack(this.input3));
		MachineRecipeManager.addRecipe(CraftTweakerMC.getItemStack(this.output), inputs);

	}

	@Override
	public String describe() {
		return "Adding Satellite Builder Recipe: Inputs " + this.input1 + " " + this.input2 + " " + this.input3 + " to Output " + this.output;
	}

}
