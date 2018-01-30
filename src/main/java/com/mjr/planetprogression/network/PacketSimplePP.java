package com.mjr.planetprogression.network;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Moon;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.network.NetworkUtil;
import micdoodle8.mods.galacticraft.core.network.PacketBase;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mjr.planetprogression.client.handlers.capabilities.CapabilityStatsClientHandler;
import com.mjr.planetprogression.client.handlers.capabilities.IStatsClientCapability;
import com.mjr.planetprogression.data.SatelliteData;
import com.mjr.planetprogression.handlers.capabilities.CapabilityStatsHandler;
import com.mjr.planetprogression.handlers.capabilities.IStatsCapability;
import com.mjr.planetprogression.tileEntities.TileEntitySatelliteController;
import com.mjr.planetprogression.tileEntities.TileEntityTelescope;

@SuppressWarnings("rawtypes")
public class PacketSimplePP extends PacketBase implements Packet {
	public enum EnumSimplePacket {
		// SERVER
		S_UPDATE_ROTATION(Side.SERVER, BlockPos.class, Float.class), S_UPDATE_CONTROLLER_SATLLITE_CHANGE(Side.SERVER, BlockPos.class, Float.class),

		// CLIENT
		C_UPDATE_UNLOCKED_PLANET_LIST(Side.CLIENT, String[].class), C_UPDATE_SATELLITE_LIST(Side.CLIENT, ArrayList[].class);

		private Side targetSide;
		private Class<?>[] decodeAs;

		EnumSimplePacket(Side targetSide, Class<?>... decodeAs) {
			this.targetSide = targetSide;
			this.decodeAs = decodeAs;
		}

		public Side getTargetSide() {
			return this.targetSide;
		}

		public Class<?>[] getDecodeClasses() {
			return this.decodeAs;
		}
	}

	private EnumSimplePacket type;
	private List<Object> data;
	@SuppressWarnings("unused")
	static private String spamCheckString;

	public PacketSimplePP() {
		super();
	}

	public PacketSimplePP(EnumSimplePacket packetType, int dimID, Object[] data) {
		this(packetType, dimID, Arrays.asList(data));
	}

	public PacketSimplePP(EnumSimplePacket packetType, int dimID, List<Object> data) {
		super(dimID);
		if (packetType.getDecodeClasses().length != data.size()) {
			GCLog.info("Simple Packet Core found data length different than packet type");
			new RuntimeException().printStackTrace();
		}

		this.type = packetType;
		this.data = data;
	}

	@Override
	public void encodeInto(ByteBuf buffer) {
		super.encodeInto(buffer);
		buffer.writeInt(this.type.ordinal());

		try {
			NetworkUtil.encodeData(buffer, this.data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void decodeInto(ByteBuf buffer) {
		super.decodeInto(buffer);
		this.type = EnumSimplePacket.values()[buffer.readInt()];

		try {
			if (this.type.getDecodeClasses().length > 0) {
				this.data = NetworkUtil.decodeData(this.type.getDecodeClasses(), buffer);
			}
			if (buffer.readableBytes() > 0) {
				GCLog.severe("PlanetProgression packet length problem for packet type " + this.type.toString());
			}
		} catch (Exception e) {
			System.err.println("[PlanetProgression] Error handling simple packet type: " + this.type.toString() + " " + buffer.toString());
			e.printStackTrace();
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void handleClientSide(EntityPlayer player) {
		EntityPlayerSP playerBaseClient = null;
		IStatsClientCapability stats = null;

		if (player instanceof EntityPlayerSP) {
			playerBaseClient = (EntityPlayerSP) player;
			stats = playerBaseClient.getCapability(CapabilityStatsClientHandler.PP_STATS_CLIENT_CAPABILITY, null);
		}

		switch (this.type) {
		case C_UPDATE_UNLOCKED_PLANET_LIST:
			for (Object o : this.data) {
				for (Planet planet : GalaxyRegistry.getRegisteredPlanets().values()) {
					if (((String) o).equalsIgnoreCase(planet.getUnlocalizedName()))
						stats.addUnlockedPlanets(planet);
				}
				for (Moon planet : GalaxyRegistry.getRegisteredMoons().values()) {
					if (((String) o).equalsIgnoreCase(planet.getUnlocalizedName()))
						stats.addUnlockedPlanets(planet);
				}
			}
			break;
		case C_UPDATE_SATELLITE_LIST:
			for (Object o : this.data) {
				stats.addSatellites((SatelliteData) o);
			}
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("unused")
	@Override
	public void handleServerSide(EntityPlayer player) {
		EntityPlayerMP playerBase = PlayerUtil.getPlayerBaseServerFromPlayer(player, false);

		if (playerBase == null) {
			return;
		}

		GCPlayerStats stats = GCPlayerStats.get(playerBase);

		switch (this.type) {
		case S_UPDATE_ROTATION:
			TileEntity tileAt = player.worldObj.getTileEntity((BlockPos) this.data.get(0));

			if (tileAt instanceof TileEntityTelescope) {
				final TileEntityTelescope machine = (TileEntityTelescope) tileAt;

				if ((Float) this.data.get(1) == 0)
					machine.currentRotation += 45;
				else if ((Float) this.data.get(1) == 1)
					machine.currentRotation -= 45;
			}
			break;
		case S_UPDATE_CONTROLLER_SATLLITE_CHANGE:
			tileAt = player.worldObj.getTileEntity((BlockPos) this.data.get(0));

			if (tileAt instanceof TileEntitySatelliteController) {
				final TileEntitySatelliteController machine = (TileEntitySatelliteController) tileAt;

				IStatsCapability statsPP = null;
				if (playerBase != null) {
					statsPP = player.getCapability(CapabilityStatsHandler.PP_STATS_CAPABILITY, null);
				}
				if ((Float) this.data.get(1) == 0){
					if(machine.currentSatelliteNum >= statsPP.getSatellites().size())
						machine.currentSatelliteNum = statsPP.getSatellites().size();
					else
						machine.currentSatelliteNum += 1;
					machine.markForSatelliteUpdate = true;
				}
				else if ((Float) this.data.get(1) == 1){
					if(machine.currentSatelliteNum <= 0)
						machine.currentSatelliteNum = 0;
					else
						machine.currentSatelliteNum -= 1;
					machine.markForSatelliteUpdate = true;
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void readPacketData(PacketBuffer var1) {
		this.decodeInto(var1);
	}

	@Override
	public void writePacketData(PacketBuffer var1) {
		this.encodeInto(var1);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void processPacket(INetHandler var1) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			this.handleClientSide(FMLClientHandler.instance().getClientPlayerEntity());
		}
	}
}
