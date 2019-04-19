package com.mjr.planetprogression.client.gui;

import com.mjr.planetprogression.inventory.ContainerSatelliteBuilder;
import com.mjr.planetprogression.inventory.ContainerSatelliteController;
import com.mjr.planetprogression.inventory.ContainerSatelliteRocketLauncher;
import com.mjr.planetprogression.inventory.ContainerTelescope;
import com.mjr.planetprogression.tileEntities.TileEntitySatelliteBuilder;
import com.mjr.planetprogression.tileEntities.TileEntitySatelliteController;
import com.mjr.planetprogression.tileEntities.TileEntitySatelliteRocketLauncher;
import com.mjr.planetprogression.tileEntities.TileEntityTelescope;

import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiHandler implements IGuiHandler {
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		EntityPlayerMP playerBase = PlayerUtil.getPlayerBaseServerFromPlayer(player, false);

		if (playerBase == null) {
			player.addChatMessage(new TextComponentString("Planet Progression player instance null server-side. This is a bug."));
			return null;
		}

		BlockPos pos = new BlockPos(x, y, z);
		TileEntity tile = world.getTileEntity(pos);

		if (tile != null) {
			if (tile instanceof TileEntityTelescope) {
				return new ContainerTelescope(player.inventory, (TileEntityTelescope) tile, player);
			} else if (tile instanceof TileEntitySatelliteBuilder) {
				return new ContainerSatelliteBuilder(player.inventory, (TileEntitySatelliteBuilder) tile, player);
			} else if (tile instanceof TileEntitySatelliteController) {
				return new ContainerSatelliteController(player.inventory, (TileEntitySatelliteController) tile, player);
			} else if (tile instanceof TileEntitySatelliteRocketLauncher) {
				return new ContainerSatelliteRocketLauncher(player.inventory, (TileEntitySatelliteRocketLauncher) tile, player);
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			return this.getClientGuiElement(ID, player, world, new BlockPos(x, y, z));
		}

		return null;
	}

	@SideOnly(Side.CLIENT)
	private Object getClientGuiElement(int ID, EntityPlayer player, World world, BlockPos position) {
		TileEntity tile = world.getTileEntity(position);

		if (tile != null) {
			if (tile instanceof TileEntityTelescope) {
				return new GuiTelescope(player.inventory, (TileEntityTelescope) world.getTileEntity(position));
			} else if (tile instanceof TileEntitySatelliteBuilder) {
				return new GuiSatelliteBuilder(player.inventory, (TileEntitySatelliteBuilder) world.getTileEntity(position));
			} else if (tile instanceof TileEntitySatelliteController) {
				return new GuiSatelliteController(player.inventory, (TileEntitySatelliteController) world.getTileEntity(position));
			} else if (tile instanceof TileEntitySatelliteRocketLauncher) {
				return new GUISatelliteRocketLauncher(player.inventory, (TileEntitySatelliteRocketLauncher) tile);
			}
		}
		return null;
	}
}