package com.mjr.planetprogression.handlers;

import java.util.List;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.collect.Lists;
import com.mjr.planetprogression.PlanetProgression;
import com.mjr.planetprogression.client.handlers.capabilities.CapabilityProviderStatsClient;
import com.mjr.planetprogression.client.handlers.capabilities.CapabilityStatsClientHandler;
import com.mjr.planetprogression.handlers.capabilities.CapabilityProviderStats;
import com.mjr.planetprogression.handlers.capabilities.CapabilityStatsHandler;
import com.mjr.planetprogression.handlers.capabilities.IStatsCapability;
import com.mjr.planetprogression.network.PacketSimpleEP;
import com.mjr.planetprogression.network.PacketSimpleEP.EnumSimplePacket;
import com.mjr.planetprogression.network.PlanetProgressionPacketHandler;

public class MainHandlerServer {
	private static List<PlanetProgressionPacketHandler> packetHandlers = Lists.newCopyOnWriteArrayList();

	public static void addPacketHandler(PlanetProgressionPacketHandler handler) {
		MainHandlerServer.packetHandlers.add(handler);
	}

	@SubscribeEvent
	public void worldUnloadEvent(WorldEvent.Unload event) {
		for (PlanetProgressionPacketHandler packetHandler : packetHandlers) {
			packetHandler.unload(event.getWorld());
		}
	}

	@SubscribeEvent
	public void onWorldTick(WorldTickEvent event) {
		if (event.phase == Phase.END) {
			final WorldServer world = (WorldServer) event.world;

			for (PlanetProgressionPacketHandler handler : packetHandlers) {
				handler.tick(world);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerCloned(PlayerEvent.Clone event) {
		if (event.isWasDeath()) {
			IStatsCapability oldStats = event.getOriginal().getCapability(CapabilityStatsHandler.PP_STATS_CAPABILITY, null);
			IStatsCapability newStats = event.getEntityPlayer().getCapability(CapabilityStatsHandler.PP_STATS_CAPABILITY, null);

			newStats.copyFrom(oldStats, false);
		}
	}

	@SuppressWarnings("unused")
	@SubscribeEvent
	public void onEntityDealth(LivingDeathEvent event) {
		if (event.getEntity() instanceof EntityPlayerMP) {
			final EntityLivingBase entityLiving = event.getEntityLiving();
			IStatsCapability stats = null;

			if (entityLiving != null) {
				stats = entityLiving.getCapability(CapabilityStatsHandler.PP_STATS_CAPABILITY, null);
			}
		}
	}

	@SubscribeEvent
	public void onAttachCapability(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof EntityPlayerMP) {
			event.addCapability(CapabilityStatsHandler.PP_PLAYER_PROP, new CapabilityProviderStats((EntityPlayerMP) event.getObject()));
		} else if (event.getObject() instanceof EntityPlayer && ((EntityPlayer) event.getObject()).worldObj.isRemote) {
			this.onAttachCapabilityClient(event);
		}
	}

	@SideOnly(Side.CLIENT)
	private void onAttachCapabilityClient(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof EntityPlayerSP)
			event.addCapability(CapabilityStatsClientHandler.PP_PLAYER_CLIENT_PROP, new CapabilityProviderStatsClient((EntityPlayerSP) event.getObject()));
	}

	@SubscribeEvent
	public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayerMP) {
			final EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();
			int tick = player.ticksExisted - 1;
			IStatsCapability stats = player.getCapability(CapabilityStatsHandler.PP_STATS_CAPABILITY, null);
			if (tick % 30 == 0) {
				this.sendUnlockedPlanetsPacket(player, stats);
			}
		}
	}

	protected void sendUnlockedPlanetsPacket(EntityPlayerMP player, IStatsCapability stats) {
		String[] planets;
		if (stats.getUnlockedPlanets().isEmpty())
			planets = new String[0];
		else {
			int size = stats.getUnlockedPlanets().size();
			planets = new String[size];
			for (int i = 0; i < size; i++) {
				planets[i] = stats.getUnlockedPlanets().get(i).getUnlocalizedName();
			}
		}
		PlanetProgression.packetPipeline.sendTo(new PacketSimpleEP(EnumSimplePacket.C_UPDATE_UNLOCKED_PLANET_LIST, player.worldObj.provider.getDimensionType().getId(), new Object[] { planets }), player);
	}

}
