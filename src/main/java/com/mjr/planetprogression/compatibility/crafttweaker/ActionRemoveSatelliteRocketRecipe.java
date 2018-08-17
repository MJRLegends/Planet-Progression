package com.mjr.planetprogression.compatibility.crafttweaker;

import com.mjr.planetprogression.recipes.SatelliteRocketRecipes;

import crafttweaker.IAction;

public class ActionRemoveSatelliteRocketRecipe implements IAction {

	public ActionRemoveSatelliteRocketRecipe() {
	}

	@Override
	public void apply() {
		SatelliteRocketRecipes.removeAllSatelliteRocketRecipes();
	}

	@Override
	public String describe() {
		return "Removing All Satellite Rocket Recipes";
	}

}
