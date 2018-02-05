package com.mjr.planetprogression.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.collect.ImmutableList;
import com.mjr.mjrlegendslib.util.ClientUtilities;
import com.mjr.mjrlegendslib.util.RegisterUtilities;
import com.mjr.planetprogression.Config;
import com.mjr.planetprogression.Constants;
import com.mjr.planetprogression.blocks.PlanetProgression_Blocks;
import com.mjr.planetprogression.client.handlers.MainHandlerClient;
import com.mjr.planetprogression.client.model.ItemModelSatelliteRocket;
import com.mjr.planetprogression.client.model.ItemModelTelescope;
import com.mjr.planetprogression.client.render.entities.RenderSatelliteRocket;
import com.mjr.planetprogression.client.render.tile.TileEntityTelescopeRenderer;
import com.mjr.planetprogression.entities.EntitySatelliteRocket;
import com.mjr.planetprogression.item.PlanetProgression_Items;
import com.mjr.planetprogression.tileEntities.TileEntityTelescope;

public class ClientProxy extends CommonProxy {

	// Event Methods
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
		// Register OBJ Domain
		ClientUtilities.registerOBJInstance(Constants.ASSET_PREFIX);

		// Register Variants
		registerVariants();

		// Register Entity Renders
		registerEntityRenders();

		// Register Custom Models
		registerCustomModel();

		super.preInit(event);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		// Register Block Json Files
		registerBlockJsons();

		// Register Item Json Files
		registerItemJsons();

		// Register Client Main Handler
		RegisterUtilities.registerEventHandler(new MainHandlerClient());

		// Register TileEntity Special Renderers
		renderBlocksTileEntitySpecialRenderers();

		super.postInit(event);
	}

	private void registerEntityRenders() {
		ClientUtilities.registerEntityRenderer(EntitySatelliteRocket.class, (RenderManager manager) -> new RenderSatelliteRocket(manager));
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onModelBakeEvent(ModelBakeEvent event) {
		ClientUtilities.replaceModelDefault(Constants.modID, event, "telescope", "telescope.obj",
				ImmutableList.of("Eyes_lens", "first_leg_tripod", "Body_Teleskope", "Primary_lens", "two__leg_tripod", "third_leg_tripod", "Stand", "swivel_ground", "small_gear", "Big_gear"), ItemModelTelescope.class, TRSRTransformation.identity());

		if (Config.researchMode == 2 || Config.researchMode == 3)
			ClientUtilities.replaceModelDefault(Constants.modID, event, "satellite_rocket", "satellite_rocket.obj",
					ImmutableList.of("launch_vehicle", "flang2", "flang1", "Body_Satellite", "Antenn", "solar_panel4", "satellite_dish2", "solar_panel2", "joint1", "solar_panel1", "joint3", "solar_panel3", "joint2", "satellite_dish1"),
					ItemModelSatelliteRocket.class, TRSRTransformation.identity());
	}

	private void registerCustomModel() {
		ClientUtilities.registerCustomModel(Constants.TEXTURE_PREFIX, PlanetProgression_Blocks.TELESCOPE, "telescope");

		if (Config.researchMode == 2 || Config.researchMode == 3) {
			ModelResourceLocation modelResourceLocation = new ModelResourceLocation(Constants.TEXTURE_PREFIX + "satellite_rocket", "inventory");
			for (int i = 1; i < 2; ++i) {
				ClientUtilities.registerModel(PlanetProgression_Items.SATELLITE_ROCKET, i, modelResourceLocation);
			}
		}
	}

	private void registerVariants() {

	}

	private void registerBlockJsons() {

	}

	private void registerItemJsons() {
		ClientUtilities.registerItemJson(Constants.TEXTURE_PREFIX, PlanetProgression_Items.researchPapers, "research_paper");
	}

	private void renderBlocksTileEntitySpecialRenderers() {
		ClientUtilities.registerTileEntityRenderer(TileEntityTelescope.class, new TileEntityTelescopeRenderer());
	}
}
