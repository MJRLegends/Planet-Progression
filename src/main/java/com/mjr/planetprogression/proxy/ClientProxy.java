package com.mjr.planetprogression.proxy;

import com.mjr.planetprogression.Constants;
import com.mjr.planetprogression.client.handlers.MainHandlerClient;

import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

	// Event Methods
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
		// Register OBJ Domain
		OBJLoader.INSTANCE.addDomain(Constants.ASSET_PREFIX);

		// Register Variants
		registerVariants();

		// Register Custom Models
		registerCustomModel();
		super.preInit(event);
	}


	@Override
	public void init(FMLInitializationEvent event) {
		// Register TileEntity Special Renderers
		renderBlocksTileEntitySpecialRenderers();

		// Register Block Json Files
		registerBlockJsons();

		// Register Item Json Files
		registerBlockJsons();
		super.init(event);
	}


	@Override
	public void postInit(FMLPostInitializationEvent event) {
		// Register Client Main Handler
		MinecraftForge.EVENT_BUS.register(new MainHandlerClient());

		super.postInit(event);
	}
	
	private void registerCustomModel() {
		
	}

	private void registerVariants() {
		
	}

	private void registerBlockJsons() {
		
	}

	private void renderBlocksTileEntitySpecialRenderers() {
		
	}
}
