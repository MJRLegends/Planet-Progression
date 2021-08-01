package com.mjr.planetprogression.compatibility.crafttweaker;

import java.util.HashMap;

import com.mjr.planetprogression.item.PlanetProgression_Items;
import com.mjr.planetprogression.recipes.SatelliteRocketRecipes;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import micdoodle8.mods.galacticraft.core.recipe.NasaWorkbenchRecipe;

import crafttweaker.IAction;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;

public class ActionAddSatelliteRocketRecipe implements IAction {

	private final IItemStack input1;
	private final IItemStack input2;
	private final IItemStack input3;
	private final IItemStack input4;
	private final IItemStack input5;
	private final IItemStack input6;
	private final IItemStack input7;
	private final IItemStack input8;
	private final IItemStack input9;
	private final IItemStack input10;
	private final IItemStack input11;
	private final IItemStack input12;
	private final IItemStack input13;
	private final IItemStack input14;
	private final IItemStack input15;
	private final IItemStack input16;
	private final IItemStack input17;
	private final IItemStack input18;
	private final IItemStack input19;
	private final IItemStack input20;
	private final IItemStack input21;
	private final IItemStack input22;
	private final IItemStack input23;
	private final IItemStack input24;
	private final IItemStack input25;
	private final IItemStack input26;
	private final IItemStack input27;
	private final IItemStack input28;
	private final IItemStack input29;
	private final IItemStack input30;

	public ActionAddSatelliteRocketRecipe(IItemStack input1, IItemStack input2, IItemStack input3, IItemStack input4, IItemStack input5, IItemStack input6, IItemStack input7, IItemStack input8, IItemStack input9, IItemStack input10,
			IItemStack input11, IItemStack input12, IItemStack input13, IItemStack input14, IItemStack input15, IItemStack input16, IItemStack input17, IItemStack input18, IItemStack input19, IItemStack input20, IItemStack input21,
			IItemStack input22, IItemStack input23, IItemStack input24, IItemStack input25, IItemStack input26, IItemStack input27, IItemStack input28, IItemStack input29, IItemStack input30) {
		this.input1 = input1;
		this.input2 = input2;
		this.input3 = input3;
		this.input4 = input4;
		this.input5 = input5;
		this.input6 = input6;
		this.input7 = input7;
		this.input8 = input8;
		this.input9 = input9;
		this.input10 = input10;
		this.input11 = input11;
		this.input12 = input12;
		this.input13 = input13;
		this.input14 = input14;
		this.input15 = input15;
		this.input16 = input16;
		this.input17 = input17;
		this.input18 = input18;
		this.input19 = input19;
		this.input20 = input20;
		this.input21 = input21;
		this.input22 = input22;
		this.input23 = input23;
		this.input24 = input24;
		this.input25 = input25;
		this.input26 = input26;
		this.input27 = input27;
		this.input28 = input28;
		this.input29 = input29;
		this.input30 = input30;
	}

	@Override
	public void apply() {
		HashMap<Integer, ItemStack> input = new HashMap<Integer, ItemStack>();
		input.put(1,  CraftTweakerMC.getItemStack(this.input1)); // Cone
		// Body
		input.put(2, CraftTweakerMC.getItemStack(this.input2));
		input.put(3, CraftTweakerMC.getItemStack(this.input3));
		input.put(4, CraftTweakerMC.getItemStack(this.input4));
		input.put(5, CraftTweakerMC.getItemStack(this.input5));
		input.put(6, CraftTweakerMC.getItemStack(this.input6));
		input.put(7, CraftTweakerMC.getItemStack(this.input7));
		input.put(8, CraftTweakerMC.getItemStack(this.input8));
		input.put(9, CraftTweakerMC.getItemStack(this.input9));
		input.put(10, CraftTweakerMC.getItemStack(this.input10));
		input.put(11, CraftTweakerMC.getItemStack(this.input11));
		input.put(12, CraftTweakerMC.getItemStack(this.input12));
		input.put(13, CraftTweakerMC.getItemStack(this.input13));
		input.put(14, CraftTweakerMC.getItemStack(this.input14));
		input.put(15, CraftTweakerMC.getItemStack(this.input15));
		input.put(16, CraftTweakerMC.getItemStack(this.input16));
		input.put(17, CraftTweakerMC.getItemStack(this.input17));
		input.put(18, CraftTweakerMC.getItemStack(this.input18));
		input.put(19, CraftTweakerMC.getItemStack(this.input19));
		input.put(20, CraftTweakerMC.getItemStack(this.input20));
		input.put(21, CraftTweakerMC.getItemStack(this.input21));
		input.put(22, CraftTweakerMC.getItemStack(this.input22));
		input.put(23, CraftTweakerMC.getItemStack(this.input23));
		input.put(24, CraftTweakerMC.getItemStack(this.input24));
		input.put(25, CraftTweakerMC.getItemStack(this.input25));

		input.put(26, CraftTweakerMC.getItemStack(this.input26)); // Fin
		input.put(27, CraftTweakerMC.getItemStack(this.input27)); // Fin
		input.put(28,  CraftTweakerMC.getItemStack(this.input28)); // Engine
		input.put(29,  CraftTweakerMC.getItemStack(this.input29)); // Fin
		input.put(30,  CraftTweakerMC.getItemStack(this.input30)); // Fin
		input.put(31, ItemStack.EMPTY);

		HashMap<Integer, ItemStack> input2 = new HashMap<Integer, ItemStack>(input);
		input2.put(31, new ItemStack(Blocks.CHEST));
		SatelliteRocketRecipes.addSatelliteRocketRecipe(new NasaWorkbenchRecipe(new ItemStack(PlanetProgression_Items.SATELLITE_ROCKET, 1, 1), input2));

	}

	@Override
	public String describe() {
		return "Adding a new Nasa Workbench recipe for Satellite Rocket";
	}

}
