package com.mjr.planetprogression.item;

import java.util.List;

import com.mjr.mjrlegendslib.item.BasicItem;
import com.mjr.mjrlegendslib.util.TranslateUtilities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
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
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			if (world.getBlockState(pos).getBlock() instanceof BlockDish) {
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setInteger("x", pos.getX());
				nbt.setInteger("y", pos.getY());
				nbt.setInteger("z", pos.getZ());
				stack.setTagCompound(nbt);
				player.addChatMessage(new ChatComponentText(EnumColor.DARK_AQUA + TranslateUtilities.translate("keycard.setcoords.name")));
			} else
				player.addChatMessage(new ChatComponentText(EnumColor.DARK_RED + TranslateUtilities.translate("keycard.nodishfound.name")));
		}
		return super.onItemUse(stack, player, world, pos, side, hitX, hitY, hitZ);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> list, boolean par4) {
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
