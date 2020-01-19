package com.mjr.planetprogression.client.render.entities;

import org.lwjgl.opengl.GL11;

import com.mjr.mjrlegendslib.util.ModelUtilities;
import com.mjr.planetprogression.Constants;
import com.mjr.planetprogression.client.model.ItemModelSatelliteRocket;
import com.mjr.planetprogression.entities.EntitySatelliteRocket;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSatelliteRocket extends Render<EntitySatelliteRocket> {
	private ItemModelSatelliteRocket rocketModel;

	public RenderSatelliteRocket(RenderManager manager) {
		super(manager);
		this.shadowSize = 6F;
	}

	private void updateModel() {
		if (this.rocketModel == null) {
			this.rocketModel = (ItemModelSatelliteRocket) ModelUtilities.getModelFromRegistry(Constants.TEXTURE_PREFIX, "satellite_rocket");
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(EntitySatelliteRocket par1Entity) {
		return TextureMap.locationBlocksTexture;
	}

	@Override
	public void doRender(EntitySatelliteRocket entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.disableRescaleNormal();
		GlStateManager.pushMatrix();
		final float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks + 180;

        GlStateManager.translate((float) x - 2.6, (float) y + 0.55, (float) z - 2.6);
		GlStateManager.rotate(180.0F - entityYaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-pitch, 0.0F, 0.0F, 1.0F);
		GlStateManager.translate(0.0F, entity.getRenderOffsetY(), 0.0F);
		final float var28 = entity.rollAmplitude / 3 - partialTicks;

		if (var28 > 0.0F) {
			final float i = entity.getLaunched() ? (5 - MathHelper.floor_double(entity.timeUntilLaunch / 85)) / 10F : 0.3F;
			GlStateManager.rotate(MathHelper.sin(var28) * var28 * i * partialTicks, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(MathHelper.sin(var28) * var28 * i * partialTicks, 1.0F, 0.0F, 1.0F);
		}

		updateModel();
		this.bindTexture(TextureMap.locationBlocksTexture);

		if (Minecraft.isAmbientOcclusionEnabled()) {
			GlStateManager.shadeModel(GL11.GL_SMOOTH);
		} else {
			GlStateManager.shadeModel(GL11.GL_FLAT);
		}

		GlStateManager.scale(-1.0F, -1.0F, 1.0F);
		GlStateManager.scale(0.3F, 0.3F, 0.3F);
		GlStateManager.pushMatrix();
		GlStateManager.rotate(-60.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.rotate(165.0F, -14.0F, 0.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.popMatrix();

		ModelUtilities.drawBakedModel(this.rocketModel);
		GlStateManager.popMatrix();

		RenderHelper.enableStandardItemLighting();
	}
}