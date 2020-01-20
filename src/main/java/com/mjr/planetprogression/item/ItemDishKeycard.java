package com.mjr.planetprogression.item;

import java.util.List;

import javax.annotation.Nullable;

import com.mjr.mjrlegendslib.item.BasicItem;
import com.mjr.mjrlegendslib.util.TranslateUtilities;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import micdoodle8.mods.galacticraft.core.blocks.BlockDish;
import micdoodle8.mods.galacticraft.core.util.EnumColor;

public class ItemDishKeycard extends BasicItem {

	public ItemDishKeycard(String name) {
		super(name);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			ItemStack itemStack = player.getHeldItem(hand);
			if (world.getBlockState(pos).getBlock() instanceof BlockDish) {
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setInteger("x", pos.getX());
				nbt.setInteger("y", pos.getY());
				nbt.setInteger("z", pos.getZ());
				itemStack.setTagCompound(nbt);
				player.sendMessage(new TextComponentString(EnumColor.DARK_AQUA + TranslateUtilities.translate("keycard.setcoords.name")));
			} else
				player.sendMessage(new TextComponentString(EnumColor.DARK_RED + TranslateUtilities.translate("keycard.nodishfound.name")));
		}
		return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, @Nullable World worldIn, List<String> list, ITooltipFlag flagIn) {
		list.add(EnumColor.BRIGHT_GREEN + TranslateUtilities.translate("keycard.use.desc"));
		if (itemStack.hasTagCompound()) {
			NBTTagCompound nbt = itemStack.getTagCompound();
			list.add(EnumColor.DARK_AQUA + "Dish Location");
			if (nbt.hasKey("x") && nbt.hasKey("y") && nbt.hasKey("z")) {
				list.add(EnumColor.DARK_AQUA + "X: " + EnumColor.YELLOW + nbt.getInteger("x"));
				list.add(EnumColor.DARK_AQUA + "Y: " + EnumColor.YELLOW + nbt.getInteger("y"));
				list.add(EnumColor.DARK_AQUA + "Z: " + EnumColor.YELLOW + nbt.getInteger("z"));
			} else {
				list.add(EnumColor.DARK_AQUA + "X: " + EnumColor.YELLOW + 0);
				list.add(EnumColor.DARK_AQUA + "Y: " + EnumColor.YELLOW + 0);
				list.add(EnumColor.DARK_AQUA + "Z: " + EnumColor.YELLOW + 0);
			}
		}
	}
}
