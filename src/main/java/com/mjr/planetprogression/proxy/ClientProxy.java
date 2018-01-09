package com.mjr.planetprogression.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.collect.ImmutableList;
import com.mjr.mjrlegendslib.util.ClientUtilities;
import com.mjr.planetprogression.Constants;
import com.mjr.planetprogression.blocks.PlanetProgression_Blocks;
import com.mjr.planetprogression.client.handlers.MainHandlerClient;
import com.mjr.planetprogression.client.model.ItemModelTelescope;
import com.mjr.planetprogression.client.render.tile.TileEntityTelescopeRenderer;
import com.mjr.planetprogression.tileEntities.TileEntityTelescope;

public class ClientProxy extends CommonProxy {

	// Event Methods
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
		// Register OBJ Domain
		OBJLoader.INSTANCE.addDomain(Constants.ASSET_PREFIX);

		// Register Variants
		registerVariants();

		super.preInit(event);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		// Register Custom Models
		registerCustomModel();

		// Register Block Json Files
		registerBlockJsons();

		// Register Item Json Files
		registerBlockJsons();
		
		// Register Client Main Handler
		MinecraftForge.EVENT_BUS.register(new MainHandlerClient());

		// Register TileEntity Special Renderers
		renderBlocksTileEntitySpecialRenderers();

		super.postInit(event);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onModelBakeEvent(ModelBakeEvent event) {
		ClientUtilities.replaceModelDefault(Constants.modID, event, "telescope", "telescope.obj",
				ImmutableList.of("Eyes_lens", "first_leg_tripod", "Body_Teleskope", "Primary_lens", "two__leg_tripod", "third_leg_tripod", "Stand", "swivel_ground", "small_gear", "Big_gear"), ItemModelTelescope.class, TRSRTransformation.identity());
	}

	private void registerCustomModel() {
		Item teleporter = Item.getItemFromBlock(PlanetProgression_Blocks.TELESCOPE);
		ModelResourceLocation modelResourceLocation = new ModelResourceLocation(Constants.TEXTURE_PREFIX + "telescope", "inventory");
		ModelLoader.setCustomModelResourceLocation(teleporter, 0, modelResourceLocation);
	}

	private void registerVariants() {

	}

	private void registerBlockJsons() {

	}

	private void renderBlocksTileEntitySpecialRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTelescope.class, new TileEntityTelescopeRenderer());
	}
}
