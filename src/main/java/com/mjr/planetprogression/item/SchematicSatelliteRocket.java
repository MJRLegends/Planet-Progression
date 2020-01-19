package com.mjr.planetprogression.item;

import com.mjr.planetprogression.client.gui.GuiSchematicSatelliteRocket;
import com.mjr.planetprogression.inventory.ContainerSchematicSatelliteRocket;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicPage;
import micdoodle8.mods.galacticraft.core.items.ItemSchematic;

public class SchematicSatelliteRocket extends ItemSchematic implements ISchematicPage {

	public SchematicSatelliteRocket() {
		super("schematic");
	}

	@Override
	public int getPageID() {
		return 2535;
	}

	@Override
	public int getGuiID() {
		return 2534;
	}

	@Override
	public ItemStack getRequiredItem() {
		return null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public GuiScreen getResultScreen(EntityPlayer player, BlockPos pos) {
		return new GuiSchematicSatelliteRocket(player.inventory, pos);
	}

	@Override
	public Container getResultContainer(EntityPlayer player, BlockPos pos) {
		return new ContainerSchematicSatelliteRocket(player.inventory, pos);
	}

	@Override
	public int compareTo(ISchematicPage o) {
		if (this.getPageID() > o.getPageID()) {
			return 1;
		} else {
			return -1;
		}
	}
}