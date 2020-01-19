package com.mjr.planetprogression.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.netty.buffer.ByteBuf;

import com.mjr.mjrlegendslib.network.PacketSimpleBase;
import com.mjr.mjrlegendslib.util.ItemUtilities;
import com.mjr.mjrlegendslib.util.MCUtilities;
import com.mjr.planetprogression.Constants;
import com.mjr.planetprogression.client.gui.GuiSatelliteRocket;
import com.mjr.planetprogression.client.handlers.capabilities.CapabilityStatsClientHandler;
import com.mjr.planetprogression.client.handlers.capabilities.IStatsClientCapability;
import com.mjr.planetprogression.data.SatelliteData;
import com.mjr.planetprogression.entities.EntitySatelliteRocket;
import com.mjr.planetprogression.handlers.capabilities.CapabilityStatsHandler;
import com.mjr.planetprogression.handlers.capabilities.IStatsCapability;
import com.mjr.planetprogression.tileEntities.TileEntitySatelliteController;
import com.mjr.planetprogression.tileEntities.TileEntitySatelliteRocketLauncher;
import com.mjr.planetprogression.tileEntities.TileEntityTelescope;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Moon;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.network.NetworkUtil;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;

public class PacketSimplePP extends PacketSimpleBase {
	public enum EnumSimplePacket {
		// SERVER
		S_UPDATE_ROTATION(Side.SERVER, BlockPos.class, Float.class),
		S_UPDATE_CONTROLLER_SATLLITE_CHANGE(Side.SERVER, BlockPos.class, Float.class),
		S_UPDATE_SATELLITE_LAUNCHER_GUI(Side.SERVER, Integer.class, BlockPos.class, Integer.class),
		S_UPDATE_SATELLITE_ROCKET_STATUS(Side.SERVER, Integer.class, Integer.class),

		// CLIENT
		C_UPDATE_UNLOCKED_PLANET_LIST(Side.CLIENT, String[].class),
		C_UPDATE_SATELLITE_LIST(Side.CLIENT, Integer.class, String.class, Integer.class, String.class),
		C_OPEN_SATELLITE_ROCKET_GUI(Side.CLIENT, Integer.class, Integer.class);

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
			stats.setUnlockedPlanets(new ArrayList<CelestialBody>());
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
			String item = (String) this.data.get(3);
			stats.addSatellites(
					new SatelliteData((int) this.data.get(0), (String) this.data.get(1), (int) this.data.get(2), (item.equalsIgnoreCase("null") ? null : ItemUtilities.stringToItemStack(item, Constants.modID + ":UpdateSatellieList", true))));
			break;
		case C_OPEN_SATELLITE_ROCKET_GUI:
			int entityID = 0;
			Entity entity = null;
			entityID = (Integer) this.data.get(1);
			entity = player.world.getEntityByID(entityID);

			if (entity != null && entity instanceof EntitySatelliteRocket) {
				MCUtilities.getClient().displayGuiScreen(new GuiSatelliteRocket(player.inventory, (EntitySatelliteRocket) entity));
			}
			player.openContainer.windowId = (Integer) this.data.get(0);
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
			TileEntity tileAt = player.world.getTileEntity((BlockPos) this.data.get(0));

			if (tileAt instanceof TileEntityTelescope) {
				final TileEntityTelescope machine = (TileEntityTelescope) tileAt;

				if ((Float) this.data.get(1) == 0)
					machine.currentRotation += 45;
				else if ((Float) this.data.get(1) == 1)
					machine.currentRotation -= 45;
			}
			break;
		case S_UPDATE_CONTROLLER_SATLLITE_CHANGE:
			tileAt = player.world.getTileEntity((BlockPos) this.data.get(0));

			if (tileAt instanceof TileEntitySatelliteController) {
				final TileEntitySatelliteController machine = (TileEntitySatelliteController) tileAt;

				IStatsCapability statsPP = null;
				if (playerBase != null) {
					statsPP = player.getCapability(CapabilityStatsHandler.PP_STATS_CAPABILITY, null);
				}
				if ((Float) this.data.get(1) == 0) {
					if (machine.currentSatelliteNum == 0)
						machine.currentSatelliteNum = 0;
					else
						machine.currentSatelliteNum -= 1;
					machine.markForSatelliteUpdate = true;
				} else if ((Float) this.data.get(1) == 1) {
					if (machine.currentSatelliteNum == statsPP.getSatellites().size())
						machine.currentSatelliteNum = statsPP.getSatellites().size();
					else
						machine.currentSatelliteNum += 1;
					machine.markForSatelliteUpdate = true;
				}
			}
			break;
		case S_UPDATE_SATELLITE_LAUNCHER_GUI:
			TileEntity tile = player.world.getTileEntity((BlockPos) this.data.get(1));

			switch ((Integer) this.data.get(0)) {
			case 1:
				if (tile instanceof TileEntitySatelliteRocketLauncher) {
					TileEntitySatelliteRocketLauncher launchController = (TileEntitySatelliteRocketLauncher) tile;
					launchController.setLaunchDropdownSelection((Integer) this.data.get(2));
				}
				break;
			case 2:
				if (tile instanceof TileEntitySatelliteRocketLauncher) {
					TileEntitySatelliteRocketLauncher launchController = (TileEntitySatelliteRocketLauncher) tile;
					int bool = (Integer) this.data.get(2);
					launchController.launchEnabled = bool == 0 ? false : true;
					launchController.updateRocketOnDockSettings();
				}
				break;

			default:
				break;
			}
			break;
		case S_UPDATE_SATELLITE_ROCKET_STATUS:
			Entity entity2 = player.world.getEntityByID((Integer) this.data.get(0));

			if (entity2 instanceof EntitySatelliteRocket) {
				EntitySatelliteRocket rocket = (EntitySatelliteRocket) entity2;

				int subType = (Integer) this.data.get(1);

				switch (subType) {
				default:
					rocket.statusValid = rocket.checkLaunchValidity();
					break;
				}
			}
			break;
		default:
			break;
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void processPacket(INetHandler var1) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			this.handleClientSide(FMLClientHandler.instance().getClientPlayerEntity());
		}
	}
}
