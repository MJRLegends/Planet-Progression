package com.mjr.planetprogression.item;

import java.util.List;

import micdoodle8.mods.galacticraft.core.items.ItemBasic;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SatelliteModuleBasic extends ItemBasic {

	public SatelliteModuleBasic(String assetName) {
		super(assetName);
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
			if (player.worldObj.isRemote) {
			}
		}
	}
}
