package com.mjr.planetprogression;

import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.mjr.mjrlegendslib.util.NetworkUtilities;
import com.mjr.mjrlegendslib.util.RegisterUtilities;
import com.mjr.planetprogression.blocks.PlanetProgression_Blocks;
import com.mjr.planetprogression.client.gui.GuiHandler;
import com.mjr.planetprogression.client.handlers.capabilities.CapabilityStatsClientHandler;
import com.mjr.planetprogression.entities.EntitySatelliteRocket;
import com.mjr.planetprogression.handlers.MainHandlerServer;
import com.mjr.planetprogression.handlers.capabilities.CapabilityStatsHandler;
import com.mjr.planetprogression.item.PlanetProgression_Items;
import com.mjr.planetprogression.item.SchematicSatelliteRocket;
import com.mjr.planetprogression.network.PlanetProgressionChannelHandler;
import com.mjr.planetprogression.proxy.CommonProxy;
import com.mjr.planetprogression.recipes.PlanetProgression_Recipes;

@Mod(modid = Constants.modID, name = Constants.modName, version = Constants.modVersion, dependencies = Constants.DEPENDENCIES_FORGE + Constants.DEPENDENCIES_MODS)
public class PlanetProgression {

	@SidedProxy(clientSide = "com.mjr.planetprogression.proxy.ClientProxy", serverSide = "com.mjr.planetprogression.proxy.CommonProxy")
	public static CommonProxy proxy;

	@Instance(Constants.modID)
	public static PlanetProgression instance;

	public static PlanetProgressionChannelHandler packetPipeline;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Config.load();

		// Main Events
		RegisterUtilities.registerEventHandler(new MainHandlerServer());

		// Initialization/Registering Methods For Blocks/Items
		PlanetProgression_Blocks.init();
		PlanetProgression_Items.init();

		PlanetProgression.proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		packetPipeline = PlanetProgressionChannelHandler.init();

		// Register Non Mob Entities
		registerNonMobEntities();

		PlanetProgression_Items.initResearchPaperItems();
		PlanetProgression_Items.registerResearchPaperItems();

		PlanetProgression.proxy.init(event);
	}

	@EventHandler
	public void postinit(FMLPostInitializationEvent event) {
		// Register Capability Handlers
		CapabilityStatsHandler.register();
		CapabilityStatsClientHandler.register();

		// Register GUI Handler
		NetworkUtilities.registerGuiHandler(PlanetProgression.instance, new GuiHandler());

		PlanetProgression_Recipes.init();

		SchematicRegistry.registerSchematicRecipe(new SchematicSatelliteRocket());

		PlanetProgression.proxy.postInit(event);
	}

	private void registerNonMobEntities() {
		if (Config.researchMode == 2 || Config.researchMode == 3)
			RegisterUtilities.registerNonMobEntity(PlanetProgression.instance, EntitySatelliteRocket.class, "EntitySatelliteRocket", 150, 1, false);
	}
}
