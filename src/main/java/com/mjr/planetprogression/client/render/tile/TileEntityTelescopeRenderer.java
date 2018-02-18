package com.mjr.planetprogression.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.ImmutableList;
import com.mjr.mjrlegendslib.util.ModelUtilities;
import com.mjr.planetprogression.Constants;
import com.mjr.planetprogression.tileEntities.TileEntityTelescope;

@SideOnly(Side.CLIENT)
public class TileEntityTelescopeRenderer extends TileEntitySpecialRenderer<TileEntityTelescope> {
	private IBakedModel telescope;
	private IBakedModel telescopeLens;

	private void updateModels() {
		if (telescope == null) {
			try {
				this.telescope = ModelUtilities.modelFromOBJForge(new ResourceLocation(Constants.ASSET_PREFIX, "telescope.obj"),
						ImmutableList.of("first_leg_tripod", "Body_Teleskope", "two__leg_tripod", "third_leg_tripod", "Stand", "swivel_ground", "small_gear", "Big_gear"));
				this.telescopeLens = ModelUtilities.modelFromOBJForge(new ResourceLocation(Constants.ASSET_PREFIX, "telescope.obj"), ImmutableList.of("Eyes_lens", "Primary_lens"));
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

		ModelUtilities.drawBakedModel(this.telescope);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
		ModelUtilities.drawBakedModel(this.telescopeLens);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}
}
