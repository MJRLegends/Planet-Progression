package com.mjr.planetprogression.item;

import java.util.List;

import com.mjr.mjrlegendslib.item.BasicItem;
import com.mjr.planetprogression.PlanetProgression;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;

public class SatelliteModuleBasic extends BasicItem {

	public SatelliteModuleBasic(String assetName) {
		super(assetName);
		this.setCreativeTab(PlanetProgression.tab);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return ClientProxyCore.galacticraftItem;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> list, boolean par4) {
		if (itemStack != null && itemStack.getItemDamage() == 3) {
			if (player.world.isRemote) {
			}
		}
	}
}
