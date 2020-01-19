package com.mjr.planetprogression;

import java.util.ArrayList;
import java.util.List;

import com.mjr.mjrlegendslib.util.MessageUtilities;
import com.mjr.mjrlegendslib.util.NetworkUtilities;
import com.mjr.mjrlegendslib.util.RegisterUtilities;
import com.mjr.planetprogression.blocks.PlanetProgression_Blocks;
import com.mjr.planetprogression.client.gui.GuiHandler;
import com.mjr.planetprogression.client.handlers.capabilities.CapabilityStatsClientHandler;
import com.mjr.planetprogression.command.*;
import com.mjr.planetprogression.entities.EntitySatelliteRocket;
import com.mjr.planetprogression.handlers.MainHandlerServer;
import com.mjr.planetprogression.handlers.capabilities.CapabilityStatsHandler;
import com.mjr.planetprogression.item.PlanetProgression_Items;
import com.mjr.planetprogression.item.SchematicSatelliteRocket;
import com.mjr.planetprogression.network.PlanetProgressionChannelHandler;
import com.mjr.planetprogression.proxy.CommonProxy;
import com.mjr.planetprogression.recipes.PlanetProgression_RecipeGeneration;
import com.mjr.planetprogression.recipes.PlanetProgression_Recipes;
import com.mjr.planetprogression.world.WorldGenerater;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;

@Mod(modid = Constants.modID, name = Constants.modName, version = Constants.modVersion, dependencies = Constants.DEPENDENCIES_FORGE + Constants.DEPENDENCIES_MODS, certificateFingerprint = Constants.CERTIFICATEFINGERPRINT)
public class PlanetProgression {

	@SidedProxy(clientSide = "com.mjr.planetprogression.proxy.ClientProxy", serverSide = "com.mjr.planetprogression.proxy.CommonProxy")
	public static CommonProxy proxy;

	@Instance(Constants.modID)
	public static PlanetProgression instance;
	public static PlanetProgressionChannelHandler packetPipeline;

	// Generate recipe JSON's (For use in Dev Workspace Only)
	public static boolean generateRecipes = false;

	// Block/Item/Biome Events Registering Lists
	public static List<Item> itemList = new ArrayList<>();
	public static List<Block> blocksList = new ArrayList<>();

	// Blocks Creative Tab
	public static CreativeTabs tab = new CreativeTabs("PlanetProgressionTab") {
		@Override
		public ItemStack getTabIconItem() {
			if (Config.researchMode == 2)
				return new ItemStack(PlanetProgression_Items.satelliteBasic);
			else
				return new ItemStack(PlanetProgression_Items.researchPapers.get(0));
		}
	};

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Config.load();

		// Main Events
		RegisterUtilities.registerEventHandler(new MainHandlerServer());

		// Initialization/Registering Methods For Blocks/Items
		PlanetProgression_Blocks.init();
		PlanetProgression_Items.init();

		// Register RegistrationHandler
		RegisterUtilities.registerEventHandler(new RegistrationHandler());

		PlanetProgression_Recipes.initRocketRecipes();

		PlanetProgression.proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		packetPipeline = PlanetProgressionChannelHandler.init();

		// Register Non Mob Entities
		registerNonMobEntities();

		PlanetProgression.proxy.init(event);
	}

	@EventHandler
	public void postinit(FMLPostInitializationEvent event) {
		// Register Capability Handlers
		CapabilityStatsHandler.register();
		CapabilityStatsClientHandler.register();

		// Register GUI Handler
		NetworkUtilities.registerGuiHandler(PlanetProgression.instance, new GuiHandler());

		SchematicRegistry.registerSchematicRecipe(new SchematicSatelliteRocket());

		if (Config.generateResearchPaperInStructure)
			RegisterUtilities.registerWorldGenerator(new WorldGenerater());

		// Generate recipe JSON's (For use in Dev Workspace Only)
		if (generateRecipes) {
			PlanetProgression_RecipeGeneration.generate();
			PlanetProgression_RecipeGeneration.generateConstants();
		}

		PlanetProgression.proxy.postInit(event);
	}

	private void registerNonMobEntities() {
		if (Config.researchMode == 2 || Config.researchMode == 3)
			RegisterUtilities.registerNonMobEntity(Constants.modID, PlanetProgression.instance, EntitySatelliteRocket.class, "EntitySatelliteRocket", 150, 1, false);
	}

	@Mod.EventBusSubscriber(modid = Constants.modID)
	public static class RegistrationHandler {
		@SubscribeEvent
		public static void registerBlocksEvent(RegistryEvent.Register<Block> event) {
			for (Block block : PlanetProgression.blocksList) {
				event.getRegistry().register(block);
			}
		}

		@SubscribeEvent
		public static void registerItemsEvent(RegistryEvent.Register<Item> event) {
			PlanetProgression_Items.initResearchPaperItems();
			PlanetProgression_Items.registerResearchPaperItems();
			for (Item item : PlanetProgression.itemList) {
				event.getRegistry().register(item);
			}
		}

		@SubscribeEvent(priority = EventPriority.LOWEST)
		public static void registerRecipesEvent(RegistryEvent.Register<IRecipe> event) {
			PlanetProgression_Recipes.init();
		}
	}

	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandAddSatellite());
		event.registerServerCommand(new CommandRemoveAllSatellites());
		event.registerServerCommand(new CommandUnlockCelestialBody());
		event.registerServerCommand(new CommandUnlockAllCelestialBody());
		event.registerServerCommand(new CommandRemoveUnlockedCelestialBody());
		event.registerServerCommand(new CommandRemoveAllUnlockedCelestialBody());
	}

	@EventHandler
	public void onFingerprintViolation(FMLFingerprintViolationEvent event) {
		MessageUtilities.fatalErrorMessageToLog(Constants.modID, "Invalid fingerprint detected! The file " + event.getSource().getName() + " may have been tampered with. This version will NOT be supported!");
	}
}
