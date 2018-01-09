package com.mjr.planetprogression.client.handlers;

import java.util.List;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiCelestialSelection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.server.permission.PermissionAPI;

import com.google.common.collect.Lists;
import com.mjr.mjrlegendslib.util.MCUtilities;
import com.mjr.planetprogression.client.gui.screen.CustomGuiCelestialSelection;
import com.mjr.planetprogression.network.PlanetProgressionPacketHandler;

public class MainHandlerClient {
	private static List<PlanetProgressionPacketHandler> packetHandlers = Lists.newCopyOnWriteArrayList();

	public static void addPacketHandler(PlanetProgressionPacketHandler handler) {
		MainHandlerClient.packetHandlers.add(handler);
	}

	@SubscribeEvent
	public void worldUnloadEvent(WorldEvent.Unload event) {
		for (PlanetProgressionPacketHandler packetHandler : packetHandlers) {
			packetHandler.unload(event.getWorld());
		}
	}

	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		final Minecraft minecraft = FMLClientHandler.instance().getClient();
		final WorldClient world = minecraft.theWorld;

		if (event.phase == Phase.END) {
			if (world != null) {
				for (PlanetProgressionPacketHandler handler : packetHandlers) {
					handler.tick(world);
				}
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onGuiOpenEvent(GuiOpenEvent event) {
		if (((event.getGui() instanceof GuiCelestialSelection))) {
			if (GameSettings.isKeyDown(micdoodle8.mods.galacticraft.core.tick.KeyHandlerClient.galaxyMap)) {
				event.setGui(new CustomGuiCelestialSelection(true, null, PermissionAPI.hasPermission(MCUtilities.getMinecraft().thePlayer, Constants.PERMISSION_CREATE_STATION)));
			} else {
				event.setGui(new CustomGuiCelestialSelection(false, null, PermissionAPI.hasPermission(MCUtilities.getMinecraft().thePlayer, Constants.PERMISSION_CREATE_STATION)));
			}
		}
	}
}
