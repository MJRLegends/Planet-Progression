package com.mjr.planetprogression.itemBlocks;

import java.util.List;

import javax.annotation.Nullable;

import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockTelescope extends ItemBlockDesc {
	public ItemBlockTelescope(Block block) {
		super(block);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, @Nullable World worldIn, List<String> par2List, ITooltipFlag flagIn) {
		super.addInformation(itemStack, worldIn, par2List, flagIn);
	}
}