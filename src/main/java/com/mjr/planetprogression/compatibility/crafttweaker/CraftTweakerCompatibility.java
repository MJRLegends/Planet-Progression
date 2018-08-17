package com.mjr.planetprogression.compatibility.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.PlanetProgression")
public class CraftTweakerCompatibility {

	@ZenMethod
	public static void addSatelliteBuilderRecipe(IItemStack output, IItemStack input1, IItemStack input2, IItemStack input3) {
		CraftTweakerAPI.apply(new ActionAddSatelliteBuilderRecipe(input1, input2, input3, output));
	}

	@ZenMethod
	public static void removeSatelliteBuilderRecipe(IItemStack output) {
		CraftTweakerAPI.apply(new ActionRemoveSatelliteBuilderRecipe(output));
	}

	@ZenMethod
	public static void addSatelliteRocketRecipe(IItemStack input1, IItemStack input2, IItemStack input3, IItemStack input4, IItemStack input5, IItemStack input6, IItemStack input7, IItemStack input8, IItemStack input9, IItemStack input10,
			IItemStack input11, IItemStack input12, IItemStack input13, IItemStack input14, IItemStack input15, IItemStack input16, IItemStack input17, IItemStack input18, IItemStack input19, IItemStack input20, IItemStack input21,
			IItemStack input22, IItemStack input23, IItemStack input24, IItemStack input25, IItemStack input26, IItemStack input27, IItemStack input28, IItemStack input29, IItemStack input30) {
		CraftTweakerAPI.apply(new ActionAddSatelliteRocketRecipe(input1, input2, input3, input4, input5, input6, input7, input8, input9, input10, input11, input12, input13, input14, input15, input16, input17, input18, input19, input20, input21,
				input22, input23, input24, input25, input26, input27, input28, input29, input30));
	}

	@ZenMethod
	public static void removeSatelliteRocketRecipe() {
		CraftTweakerAPI.apply(new ActionRemoveSatelliteRocketRecipe());
	}
}
