package com.mjr.planetprogression.recipes;

import java.util.function.BooleanSupplier;

import com.google.gson.JsonObject;
import com.mjr.planetprogression.Config;

import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

public class RecipeEnabledCondition implements IConditionFactory {
	@Override
	public BooleanSupplier parse(JsonContext context, JsonObject json) {
		if (JsonUtils.getString(json, "value").equals("research_mode_2"))
			return () -> Config.researchMode == 2;

		throw new IllegalStateException("Config defined with recipe_enabled condition without a valid field defined!");
	}
}