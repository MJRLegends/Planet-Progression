package com.mjr.planetprogression.item;

import java.util.List;

import micdoodle8.mods.galacticraft.core.util.EnumColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mjr.mjrlegendslib.item.BasicItem;

public class ResearchPaper extends BasicItem {

	private String planet;

	public ResearchPaper(String name) {
		super("research_paper");
		this.planet = name.toLowerCase();
	}

	public String getPlanet() {
		return planet;
	}

	public void setPlanet(String planet) {
		this.planet = planet;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> list, boolean par4) {
		if (player.worldObj.isRemote) {
			list.add(EnumColor.AQUA + planet);
		}
	}
}
