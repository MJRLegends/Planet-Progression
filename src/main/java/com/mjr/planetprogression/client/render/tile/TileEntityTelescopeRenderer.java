package com.mjr.planetprogression.client.render.tile;

import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mjr.planetprogression.Constants;
import com.mjr.planetprogression.tileEntities.TileEntityTelescope;

@SideOnly(Side.CLIENT)
public class TileEntityTelescopeRenderer extends TileEntitySpecialRenderer<TileEntityTelescope> {
	private static OBJModel.OBJBakedModel telescope;

	@SuppressWarnings("deprecation")
	private void updateModels() {
		if (telescope == null) {
			try {
				OBJModel model = (OBJModel) ModelLoaderRegistry.getModel(new ResourceLocation(Constants.ASSET_PREFIX, "telescope.obj"));
				model = (OBJModel) model.process(ImmutableMap.of("flip-v", "true"));

				Function<ResourceLocation, TextureAtlasSprite> spriteFunction = location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
				telescope = (OBJModel.OBJBakedModel) model.bake(
						new OBJModel.OBJState(ImmutableList.of("Eyes_lens", "first_leg_tripod", "Body_Teleskope", "Primary_lens", "two__leg_tripod", "third_leg_tripod", "Stand", "swivel_ground", "small_gear", "Big_gear"), false),
						DefaultVertexFormats.ITEM, spriteFunction);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public void renderTileEntityAt(TileEntityTelescope te, double x, double y, double z, float partialTicks, int destroyStage) {
		GL11.glPushMatrix();

		RenderHelper.enableStandardItemLighting();
		this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		if (Minecraft.isAmbientOcclusionEnabled()) {
			GlStateManager.shadeModel(GL11.GL_SMOOTH);
		} else {
			GlStateManager.shadeModel(GL11.GL_FLAT);
		}

		updateModels();

		GL11.glTranslatef((float) x + 0.5F, (float) y + 0.1F, (float) z + 0.5F);
		GL11.glRotatef(te.currentRotation, 0.0F, 1.0F, 0.0F);
		GL11.glScalef(0.04F, 0.04F, 0.04F);

		ClientUtil.drawBakedModel(telescope);

		GL11.glPopMatrix();
	}
}
