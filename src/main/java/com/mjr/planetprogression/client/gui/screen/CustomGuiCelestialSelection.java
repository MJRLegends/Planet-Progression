package com.mjr.planetprogression.client.gui.screen;

import java.nio.FloatBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import micdoodle8.mods.galacticraft.api.event.client.CelestialBodyRenderEvent;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.IChildBody;
import micdoodle8.mods.galacticraft.api.galaxies.Moon;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.api.galaxies.Satellite;
import micdoodle8.mods.galacticraft.api.galaxies.SolarSystem;
import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiCelestialSelection;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mjr.planetprogression.client.handlers.capabilities.CapabilityStatsClientHandler;
import com.mjr.planetprogression.client.handlers.capabilities.IStatsClientCapability;

public class CustomGuiCelestialSelection extends GuiCelestialSelection {

	List<CelestialBody> bodiesToRender = Lists.newArrayList(); // Used to override GC ones since theirs is not accessible

	public CustomGuiCelestialSelection(boolean mapMode, List<CelestialBody> possibleBodies, boolean canCreateStations) {
		super(mapMode, possibleBodies, canCreateStations);
	}

	@Override
	public void initGui() {
		final Minecraft minecraft = FMLClientHandler.instance().getClient();
		final EntityPlayerSP player = minecraft.thePlayer;
		final EntityPlayerSP playerBaseClient = PlayerUtil.getPlayerBaseClientFromPlayer(player, false);
		IStatsClientCapability stats = null;

		if (player != null) {
			stats = playerBaseClient.getCapability(CapabilityStatsClientHandler.PP_STATS_CLIENT_CAPABILITY, null);
		}
		for (Planet planet : GalaxyRegistry.getRegisteredPlanets().values()) {
			if (stats.getUnlockedPlanets().contains(planet)) {
				this.celestialBodyTicks.put(planet, 0);
				bodiesToRender.add(planet);
			}
		}

		for (Moon moon : GalaxyRegistry.getRegisteredMoons().values()) {
			if (stats.getUnlockedPlanets().contains(moon.getParentPlanet()) && stats.getUnlockedPlanets().contains(moon)) {
				this.celestialBodyTicks.put(moon, 0);
				bodiesToRender.add(moon);
			}
		}

		for (Satellite satellite : GalaxyRegistry.getRegisteredSatellites().values()) {
			if(stats.getUnlockedPlanets().contains(satellite.getParentPlanet())){
				this.celestialBodyTicks.put(satellite, 0);
				this.bodiesToRender.add(satellite);
			}
		}

		GuiCelestialSelection.BORDER_SIZE = this.width / 65;
		GuiCelestialSelection.BORDER_EDGE_SIZE = GuiCelestialSelection.BORDER_SIZE / 4;

		for (SolarSystem solarSystem : GalaxyRegistry.getRegisteredSolarSystems().values()) {
			bodiesToRender.add(solarSystem.getMainStar());
		}
	}

	@Override
	protected List<CelestialBody> getChildren(Object object) {
		List<CelestialBody> bodyList = Lists.newArrayList();
		final Minecraft minecraft = FMLClientHandler.instance().getClient();
		final EntityPlayerSP player = minecraft.thePlayer;
		final EntityPlayerSP playerBaseClient = PlayerUtil.getPlayerBaseClientFromPlayer(player, false);

		IStatsClientCapability stats = null;

		if (player != null) {
			stats = playerBaseClient.getCapability(CapabilityStatsClientHandler.PP_STATS_CLIENT_CAPABILITY, null);
		}

		if (object instanceof Planet) {
			List<Moon> moons = GalaxyRegistry.getMoonsForPlanet((Planet) object);
			for (Moon moon : moons)
				if (stats.getUnlockedPlanets().contains(moon))
					bodyList.add(moon);
		} else if (object instanceof SolarSystem) {
			List<Planet> planets = GalaxyRegistry.getPlanetsForSolarSystem((SolarSystem) object);
			for (Planet planet : planets)
				if (stats.getUnlockedPlanets().contains(planet))
					bodyList.add(planet);
		}

		Collections.sort(bodyList);

		return bodyList;
	}

	/*
	 * Overriding for the purpose of to override GC bodiesToRender since theirs is not accessible, TODO Remove when GC adds protected to bodiesToRender
	 */
	public HashMap<CelestialBody, Matrix4f> drawCelestialBodies(Matrix4f worldMatrix) {
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		FloatBuffer fb = BufferUtils.createFloatBuffer(16 * Float.SIZE);
		HashMap<CelestialBody, Matrix4f> matrixMap = Maps.newHashMap();

		for (CelestialBody body : bodiesToRender) {
			boolean hasParent = body instanceof IChildBody;

			float alpha = getAlpha(body);

			if (alpha > 0.0F) {
				GlStateManager.pushMatrix();
				Matrix4f worldMatrixLocal = setupMatrix(body, worldMatrix, fb, hasParent ? 0.25F : 1.0F);
				CelestialBodyRenderEvent.Pre preEvent = new CelestialBodyRenderEvent.Pre(body, body.getBodyIcon(), 16);
				MinecraftForge.EVENT_BUS.post(preEvent);

				GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
				if (preEvent.celestialBodyTexture != null) {
					this.mc.renderEngine.bindTexture(preEvent.celestialBodyTexture);
				}

				if (!preEvent.isCanceled()) {
					int size = getWidthForCelestialBodyStatic(body);
					this.drawTexturedModalRect(-size / 2, -size / 2, size, size, 0, 0, preEvent.textureSize, preEvent.textureSize, false, false, preEvent.textureSize, preEvent.textureSize);
					matrixMap.put(body, worldMatrixLocal);
				}

				CelestialBodyRenderEvent.Post postEvent = new CelestialBodyRenderEvent.Post(body);
				MinecraftForge.EVENT_BUS.post(postEvent);
				GlStateManager.popMatrix();
			}
		}

		return matrixMap;
	}

	/*
	 * Overriding for the purpose of to override GC bodiesToRender since theirs is not accessible, TODO Remove when GC adds protected to bodiesToRender
	 */
	@Override
	public void drawCircles() {
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glLineWidth(3);
		int count = 0;

		final float theta = (float) (2 * Math.PI / 90);
		final float cos = (float) Math.cos(theta);
		final float sin = (float) Math.sin(theta);

		for (CelestialBody body : bodiesToRender) {
			Vector3f systemOffset = new Vector3f(0.0F, 0.0F, 0.0F);
			if (body instanceof IChildBody) {
				systemOffset = this.getCelestialBodyPosition(((IChildBody) body).getParentPlanet());
			} else if (body instanceof Planet) {
				systemOffset = this.getCelestialBodyPosition(((Planet) body).getParentSolarSystem().getMainStar());
			}

			float x = this.getScale(body);
			float y = 0;

			float alpha = getAlpha(body);

			if (alpha > 0.0F) {
				switch (count % 2) {
				case 0:
					GL11.glColor4f(0.0F / 1.4F, 0.6F / 1.4F, 1.0F / 1.4F, alpha / 1.4F);
					break;
				case 1:
					GL11.glColor4f(0.3F / 1.4F, 0.8F / 1.4F, 1.0F / 1.4F, alpha / 1.4F);
					break;
				}

				CelestialBodyRenderEvent.CelestialRingRenderEvent.Pre preEvent = new CelestialBodyRenderEvent.CelestialRingRenderEvent.Pre(body, systemOffset);
				MinecraftForge.EVENT_BUS.post(preEvent);

				if (!preEvent.isCanceled()) {
					GL11.glTranslatef(systemOffset.x, systemOffset.y, systemOffset.z);

					GL11.glBegin(GL11.GL_LINE_LOOP);

					float temp;
					for (int i = 0; i < 90; i++) {
						GL11.glVertex2f(x, y);

						temp = x;
						x = cos * x - sin * y;
						y = sin * temp + cos * y;
					}

					GL11.glEnd();

					GL11.glTranslatef(-systemOffset.x, -systemOffset.y, -systemOffset.z);

					count++;
				}

				CelestialBodyRenderEvent.CelestialRingRenderEvent.Post postEvent = new CelestialBodyRenderEvent.CelestialRingRenderEvent.Post(body);
				MinecraftForge.EVENT_BUS.post(postEvent);
			}
		}
	}
}
