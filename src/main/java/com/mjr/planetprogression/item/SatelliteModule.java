package com.mjr.planetprogression.item;

import java.util.List;

import javax.annotation.Nullable;

import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mjr.mjrlegendslib.item.ItemBasicMeta;
import com.mjr.planetprogression.PlanetProgression;

public class SatelliteModule extends ItemBasicMeta {

	public SatelliteModule(String assetName) {
		super(assetName, PlanetProgression.tab, getItemList());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return ClientProxyCore.galacticraftItem;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, @Nullable World worldIn, List<String> list, ITooltipFlag flagIn) {
	}

	public static String[] getItemList() {
		return new String[] { "surface", "distance", "atmosphere" };
	}

}
