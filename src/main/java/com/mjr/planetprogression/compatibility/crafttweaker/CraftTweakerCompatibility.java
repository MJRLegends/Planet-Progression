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
}
