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
import micdoodle8.mods.galacticraft.api.galaxies.Star;
import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiCelestialSelection;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
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

	public CustomGuiCelestialSelection(boolean mapMode, List<CelestialBody> possibleBodies, boolean canCreateStations) {
		super(mapMode, possibleBodies, canCreateStations);
	}

	@Override
	public void drawCircles() {
		final Minecraft minecraft = FMLClientHandler.instance().getClient();
		final EntityPlayerSP player = minecraft.player;
		final EntityPlayerSP playerBaseClient = PlayerUtil.getPlayerBaseClientFromPlayer(player, false);

		IStatsClientCapability stats = null;

		if (player != null) {
			stats = playerBaseClient.getCapability(CapabilityStatsClientHandler.PP_STATS_CLIENT_CAPABILITY, null);
		}

		GL11.glColor4f(1, 1, 1, 1);
		GL11.glLineWidth(3);
		int count = 0;

		final float theta = (float) (2 * Math.PI / 90);
		final float cos = (float) Math.cos(theta);
		final float sin = (float) Math.sin(theta);

		for (Planet planet : GalaxyRegistry.getRegisteredPlanets().values()) {
			if (stats.getUnlockedPlanets().contains(planet)) {
				if (planet.getParentSolarSystem() != null) {
					Vector3f systemOffset = this.getCelestialBodyPosition(planet.getParentSolarSystem().getMainStar());

					float x = this.getScale(planet);
					float y = 0;

					float alpha = 1.0F;

					if ((this.selectedBody instanceof IChildBody && ((IChildBody) this.selectedBody).getParentPlanet() != planet) || (this.selectedBody instanceof Planet && this.selectedBody != planet && this.isZoomed())) {
						if (this.lastSelectedBody == null && !(this.selectedBody instanceof IChildBody) && !(this.selectedBody instanceof Satellite)) {
							alpha = 1.0F - Math.min(this.ticksSinceSelection / 25.0F, 1.0F);
						} else {
							alpha = 0.0F;
						}
					}

					if (alpha != 0) {
						switch (count % 2) {
						case 0:
							GL11.glColor4f(0.0F / 1.4F, 0.6F / 1.4F, 1.0F / 1.4F, alpha / 1.4F);
							break;
						case 1:
							GL11.glColor4f(0.4F / 1.4F, 0.9F / 1.4F, 1.0F / 1.4F, alpha / 1.4F);
							break;
						}

						CelestialBodyRenderEvent.CelestialRingRenderEvent.Pre preEvent = new CelestialBodyRenderEvent.CelestialRingRenderEvent.Pre(planet, systemOffset);
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

						CelestialBodyRenderEvent.CelestialRingRenderEvent.Post postEvent = new CelestialBodyRenderEvent.CelestialRingRenderEvent.Post(planet);
						MinecraftForge.EVENT_BUS.post(postEvent);
					}
				}
			}
		}

		count = 0;

		if (this.selectedBody != null) {
			Vector3f planetPos = this.getCelestialBodyPosition(this.selectedBody);

			if (this.selectedBody instanceof IChildBody) {
				planetPos = this.getCelestialBodyPosition(((IChildBody) this.selectedBody).getParentPlanet());
			} else if (this.selectedBody instanceof Satellite) {
				planetPos = this.getCelestialBodyPosition(((Satellite) this.selectedBody).getParentPlanet());
			}

			GL11.glTranslatef(planetPos.x, planetPos.y, 0);

			List<CelestialBody> objects = Lists.newArrayList();
			objects.addAll(GalaxyRegistry.getRegisteredSatellites().values());
			objects.addAll(GalaxyRegistry.getRegisteredMoons().values());

			for (CelestialBody sat : objects) {
				boolean selected = sat == this.selectedBody || (((IChildBody) sat).getParentPlanet() == this.selectedBody && this.selectionState != EnumSelection.SELECTED);
				boolean isSibling = getSiblings(this.selectedBody).contains(sat);
				boolean isPossible = !(sat instanceof Satellite) || (this.possibleBodies != null && this.possibleBodies.contains(sat));
				boolean render = false;
				if (sat instanceof Moon) {
					if (stats.getUnlockedPlanets().contains((Moon) sat))
						render = true;
				} else if (sat instanceof Satellite)
					render = true;
				if (render) {
					if ((selected || isSibling) && isPossible) {
						if (this.drawCircle(sat, count, sin, cos)) {
							count++;
						}
					}
				}
			}
		}

		GL11.glLineWidth(1);
	}

	@Override
	protected List<CelestialBody> getChildren(Object object) {
		List<CelestialBody> bodyList = Lists.newArrayList();
		final Minecraft minecraft = FMLClientHandler.instance().getClient();
		final EntityPlayerSP player = minecraft.player;
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

	@Override
	public HashMap<CelestialBody, Matrix4f> drawCelestialBodies(Matrix4f worldMatrix) {
		final Minecraft minecraft = FMLClientHandler.instance().getClient();
		final EntityPlayerSP player = minecraft.player;
		final EntityPlayerSP playerBaseClient = PlayerUtil.getPlayerBaseClientFromPlayer(player, false);

		IStatsClientCapability stats = null;

		if (player != null) {
			stats = playerBaseClient.getCapability(CapabilityStatsClientHandler.PP_STATS_CLIENT_CAPABILITY, null);
		}

		GL11.glColor3f(1, 1, 1);
		FloatBuffer fb = BufferUtils.createFloatBuffer(16 * Float.SIZE);
		HashMap<CelestialBody, Matrix4f> matrixMap = Maps.newHashMap();

		for (SolarSystem solarSystem : GalaxyRegistry.getRegisteredSolarSystems().values()) {
			Star star = solarSystem.getMainStar();

			if (star != null && star.getBodyIcon() != null) {
				GL11.glPushMatrix();
				Matrix4f worldMatrix1 = setupMatrix(star, worldMatrix, fb);

				float alpha = 1.0F;

				if (this.selectedBody != null && this.selectedBody != star && this.isZoomed()) {
					alpha = 1.0F - Math.min(this.ticksSinceSelection / 25.0F, 1.0F);
				}

				if (this.selectedBody != null && this.isZoomed()) {
					if (star != this.selectedBody) {
						alpha = 1.0F - Math.min(this.ticksSinceSelection / 25.0F, 1.0F);

						if (!(this.lastSelectedBody instanceof Star) && this.lastSelectedBody != null) {
							alpha = 0.0F;
						}
					}
				}

				if (alpha != 0) {
					CelestialBodyRenderEvent.Pre preEvent = new CelestialBodyRenderEvent.Pre(star, star.getBodyIcon(), 8);
					MinecraftForge.EVENT_BUS.post(preEvent);

					GL11.glColor4f(1, 1, 1, alpha);
					if (preEvent.celestialBodyTexture != null) {
						this.mc.renderEngine.bindTexture(preEvent.celestialBodyTexture);
					}

					if (!preEvent.isCanceled()) {
						int size = getWidthForCelestialBodyStatic(star);
						if (star == this.selectedBody && this.selectionState == EnumSelection.SELECTED) {
							size /= 2;
							size *= 3;
						}
						this.drawTexturedModalRect(-size / 2, -size / 2, size, size, 0, 0, preEvent.textureSize, preEvent.textureSize, false, false, preEvent.textureSize, preEvent.textureSize);
						matrixMap.put(star, worldMatrix1);
					}

					CelestialBodyRenderEvent.Post postEvent = new CelestialBodyRenderEvent.Post(star);
					MinecraftForge.EVENT_BUS.post(postEvent);
				}

				fb.clear();
				GL11.glPopMatrix();
			}
		}

		for (Planet planet : GalaxyRegistry.getRegisteredPlanets().values()) {
			if (stats.getUnlockedPlanets().contains(planet)) {
				if (planet.getBodyIcon() != null) {
					GL11.glPushMatrix();
					Matrix4f worldMatrix1 = setupMatrix(planet, worldMatrix, fb);

					float alpha = 1.0F;

					if ((this.selectedBody instanceof IChildBody && ((IChildBody) this.selectedBody).getParentPlanet() != planet) || (this.selectedBody instanceof Planet && this.selectedBody != planet && this.isZoomed())) {
						if (this.lastSelectedBody == null && !(this.selectedBody instanceof IChildBody)) {
							alpha = 1.0F - Math.min(this.ticksSinceSelection / 25.0F, 1.0F);
						} else {
							alpha = 0.0F;
						}
					}

					if (alpha != 0) {
						CelestialBodyRenderEvent.Pre preEvent = new CelestialBodyRenderEvent.Pre(planet, planet.getBodyIcon(), 12);
						MinecraftForge.EVENT_BUS.post(preEvent);

						GL11.glColor4f(1, 1, 1, alpha);
						if (preEvent.celestialBodyTexture != null) {
							this.mc.renderEngine.bindTexture(preEvent.celestialBodyTexture);
						}

						if (!preEvent.isCanceled()) {
							int size = getWidthForCelestialBodyStatic(planet);
							this.drawTexturedModalRect(-size / 2, -size / 2, size, size, 0, 0, preEvent.textureSize, preEvent.textureSize, false, false, 16, 16);
							matrixMap.put(planet, worldMatrix1);
						}

						CelestialBodyRenderEvent.Post postEvent = new CelestialBodyRenderEvent.Post(planet);
						MinecraftForge.EVENT_BUS.post(postEvent);
					}

					fb.clear();
					GL11.glPopMatrix();
				}
			}
		}

		if (this.selectedBody != null) {
			List<CelestialBody> objects = Lists.newArrayList();
			objects.addAll(GalaxyRegistry.getRegisteredSatellites().values());
			objects.addAll(GalaxyRegistry.getRegisteredMoons().values());

			for (CelestialBody sat : objects) {
				boolean selected = sat == this.selectedBody || (((IChildBody) sat).getParentPlanet() == this.selectedBody && this.selectionState != EnumSelection.SELECTED);
				boolean ready = this.lastSelectedBody != null || this.ticksSinceSelection > 35;
				boolean isSibling = getSiblings(this.selectedBody).contains(sat);
				boolean isPossible = !(sat instanceof Satellite) || (this.possibleBodies != null && this.possibleBodies.contains(sat));
				boolean render = false;
				if (sat instanceof Moon) {
					if (stats.getUnlockedPlanets().contains((Moon) sat))
						render = true;
				} else if (sat instanceof Satellite)
					if (stats.getUnlockedPlanets().contains(((Satellite) sat).getParentPlanet()))
						render = true;
				if (render) {
					if (((selected && ready) || isSibling) && isPossible) {
						GL11.glPushMatrix();
						Matrix4f worldMatrix1 = setupMatrix(sat, worldMatrix, fb, 0.25F);

						CelestialBodyRenderEvent.Pre preEvent = new CelestialBodyRenderEvent.Pre(sat, sat.getBodyIcon(), 8);
						MinecraftForge.EVENT_BUS.post(preEvent);

						GL11.glColor4f(1, 1, 1, 1);
						if (preEvent.celestialBodyTexture != null) {
							this.mc.renderEngine.bindTexture(preEvent.celestialBodyTexture);
						}

						if (!preEvent.isCanceled()) {
							int size = getWidthForCelestialBodyStatic(sat);
							this.drawTexturedModalRect(-size / 2, -size / 2, size, size, 0, 0, preEvent.textureSize, preEvent.textureSize, false, false, preEvent.textureSize, preEvent.textureSize);
							matrixMap.put(sat, worldMatrix1);
						}

						CelestialBodyRenderEvent.Post postEvent = new CelestialBodyRenderEvent.Post(sat);
						MinecraftForge.EVENT_BUS.post(postEvent);
						fb.clear();
						GL11.glPopMatrix();
					}
				}
			}
		}

		return matrixMap;
	}
}
