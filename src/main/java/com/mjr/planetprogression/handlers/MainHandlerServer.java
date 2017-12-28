package com.mjr.planetprogression.handlers;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mjr.planetprogression.client.handlers.capabilities.CapabilityProviderStatsClient;
import com.mjr.planetprogression.client.handlers.capabilities.CapabilityStatsClientHandler;
import com.mjr.planetprogression.handlers.capabilities.CapabilityProviderStats;
import com.mjr.planetprogression.handlers.capabilities.CapabilityStatsHandler;
import com.mjr.planetprogression.handlers.capabilities.IStatsCapability;

public class MainHandlerServer {
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

}
