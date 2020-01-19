package com.mjr.planetprogression.itemBlocks;

import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;

public class ItemBlockCustomLandingPad extends ItemBlockDesc {
	public ItemBlockCustomLandingPad(Block block) {
		super(block);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		String name = "";

		switch (itemStack.getItemDamage()) {
		case 0:
			name = "satelliteLandingPad";
			break;
		}

		return this.getBlock().getUnlocalizedName() + "." + name;
	}

	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) {
		if (world.isRemote && stack.getItemDamage() == 0 && player instanceof EntityPlayerSP) {
			ClientProxyCore.playerClientHandler.onBuild(5, (EntityPlayerSP) player);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack itemStack) {
		return ClientProxyCore.galacticraftItem;
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}
}