package com.mjr.planetprogression.client.handlers;

import java.util.List;

import com.google.common.collect.Lists;
import com.mjr.mjrlegendslib.util.TranslateUtilities;
import com.mjr.planetprogression.blocks.PlanetProgression_Blocks;
import com.mjr.planetprogression.client.gui.screen.CustomGuiCelestialSelection;
import com.mjr.planetprogression.network.PlanetProgressionPacketHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import micdoodle8.mods.galacticraft.core.client.gui.screen.GuiCelestialSelection;
import micdoodle8.mods.galacticraft.core.util.EnumColor;

public class MainHandlerClient {
	private static List<PlanetProgressionPacketHandler> packetHandlers = Lists.newCopyOnWriteArrayList();

	public static void addPacketHandler(PlanetProgressionPacketHandler handler) {
		MainHandlerClient.packetHandlers.add(handler);
	}

	@SubscribeEvent
	public void worldUnloadEvent(WorldEvent.Unload event) {
		for (PlanetProgressionPacketHandler packetHandler : packetHandlers) {
			packetHandler.unload(event.world);
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
		if (((event.gui instanceof GuiCelestialSelection))) {
			if (!event.gui.getClass().getName().equalsIgnoreCase("com.mjr.extraplanets.client.gui.screen.CustomCelestialSelection")) {
				if (GameSettings.isKeyDown(micdoodle8.mods.galacticraft.core.tick.KeyHandlerClient.galaxyMap)) {
					event.gui = new CustomGuiCelestialSelection(true, ((GuiCelestialSelection) event.gui).possibleBodies);
				} else {
					event.gui = new CustomGuiCelestialSelection(false, ((GuiCelestialSelection) event.gui).possibleBodies);
				}
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onItemTooltip(ItemTooltipEvent event) {
		if (event.itemStack.getItem().equals(Item.getItemFromBlock(PlanetProgression_Blocks.SATTLLITE_BUILDER)))
			event.toolTip.add(EnumColor.AQUA + TranslateUtilities.translate("satellite.builder.use.desc"));
		else if (event.itemStack.getItem().equals(Item.getItemFromBlock(PlanetProgression_Blocks.SATTLLITE_CONTROLLER))) {
			event.toolTip.add(EnumColor.AQUA + TranslateUtilities.translate("satellite.controller.use.desc"));
			event.toolTip.add(EnumColor.AQUA + TranslateUtilities.translate("satellite.controller.use.2.desc"));
		} else if (event.itemStack.getItem().equals(Item.getItemFromBlock(PlanetProgression_Blocks.TELESCOPE)))
			event.toolTip.add(EnumColor.AQUA + TranslateUtilities.translate("telescope.use.desc"));
	}

}
