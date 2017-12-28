package com.mjr.planetprogression;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.mjr.planetprogression.handlers.MainHandlerServer;
import com.mjr.planetprogression.proxy.CommonProxy;

@Mod(modid = Constants.modID, name = Constants.modName, version = Constants.modVersion, dependencies = Constants.DEPENDENCIES_FORGE + Constants.DEPENDENCIES_MODS)
public class PlanetProgression {

	@SidedProxy(clientSide = "com.mjr.planetprogression.proxy.ClientProxy", serverSide = "com.mjr.planetprogression.proxy.CommonProxy")
	public static CommonProxy proxy;

	@Instance(Constants.modID)
	public static PlanetProgression instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Config.load();

		// Main Events
		MinecraftForge.EVENT_BUS.register(new MainHandlerServer());
		
		PlanetProgression.proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		PlanetProgression.proxy.init(event);
	}

	@EventHandler
	public void postinit(FMLPostInitializationEvent event) {
		PlanetProgression.proxy.postInit(event);
	}
}
