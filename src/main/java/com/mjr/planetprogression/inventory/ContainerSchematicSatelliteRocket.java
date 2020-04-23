package com.mjr.planetprogression.inventory;

import com.mjr.planetprogression.recipes.SatelliteRocketRecipes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import micdoodle8.mods.galacticraft.core.inventory.SlotRocketBenchResult;

public class ContainerSchematicSatelliteRocket extends Container {
	public InventorySchematicSatelliteRocket craftMatrix = new InventorySchematicSatelliteRocket(this);
	public IInventory craftResult = new InventoryCraftResult();
	private final World worldObj;

	public ContainerSchematicSatelliteRocket(InventoryPlayer par1InventoryPlayer, BlockPos pos) {
		final int change = 27;
		this.worldObj = par1InventoryPlayer.player.worldObj;
		this.addSlotToContainer(new SlotRocketBenchResult(par1InventoryPlayer.player, this.craftMatrix, this.craftResult, 0, 142, 18 + 69 + change));
		int var6;
		int var7;

		// Cone
		this.addSlotToContainer(new SlotSchematicSatelliteRocket(this.craftMatrix, 1, 41, -8 + change, pos, par1InventoryPlayer.player));

		// Body left
		for (var6 = 0; var6 < 8; ++var6) {
			this.addSlotToContainer(new SlotSchematicSatelliteRocket(this.craftMatrix, 2 + var6, 23, -6 + var6 * 18 + 16 + change, pos, par1InventoryPlayer.player));
		}

		// Body
		for (var6 = 0; var6 < 8; ++var6) {
			this.addSlotToContainer(new SlotSchematicSatelliteRocket(this.craftMatrix, 10 + var6, 41, -6 + var6 * 18 + 16 + change, pos, par1InventoryPlayer.player));
		}

		// Body Right
		for (var6 = 0; var6 < 8; ++var6) {
			this.addSlotToContainer(new SlotSchematicSatelliteRocket(this.craftMatrix, 18 + var6, 59, -6 + var6 * 18 + 16 + change, pos, par1InventoryPlayer.player));
		}

		// Left fins
		this.addSlotToContainer(new SlotSchematicSatelliteRocket(this.craftMatrix, 26, 5, 46 + change, pos, par1InventoryPlayer.player));
		this.addSlotToContainer(new SlotSchematicSatelliteRocket(this.craftMatrix, 27, 5, 64 + change, pos, par1InventoryPlayer.player));

		// Engine
		this.addSlotToContainer(new SlotSchematicSatelliteRocket(this.craftMatrix, 28, 41, 154 + change, pos, par1InventoryPlayer.player));

		// Right fins
		this.addSlotToContainer(new SlotSchematicSatelliteRocket(this.craftMatrix, 29, 77, 46 + change, pos, par1InventoryPlayer.player));
		this.addSlotToContainer(new SlotSchematicSatelliteRocket(this.craftMatrix, 30, 77, 64 + change, pos, par1InventoryPlayer.player));

		// Addons
		for (int var8 = 0; var8 < 3; var8++) {
			this.addSlotToContainer(new SlotSchematicSatelliteRocket(this.craftMatrix, 31 + var8, 93 + var8 * 26, -15 + change, pos, par1InventoryPlayer.player));
		}

		// Player inv:

		for (var6 = 0; var6 < 3; ++var6) {
			for (var7 = 0; var7 < 9; ++var7) {
				this.addSlotToContainer(new Slot(par1InventoryPlayer, var7 + var6 * 9 + 9, 88 + var7 * 18, 147 + var6 * 18 + change));
			}
		}

		for (var6 = 0; var6 < 9; ++var6) {
			this.addSlotToContainer(new Slot(par1InventoryPlayer, var6, 88 + var6 * 18, 18 + 187 + change));
		}

		this.onCraftMatrixChanged(this.craftMatrix);
	}

	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer) {
		super.onContainerClosed(par1EntityPlayer);

		if (!this.worldObj.isRemote) {
			for (int var2 = 1; var2 < this.craftMatrix.getSizeInventory(); ++var2) {
				final ItemStack var3 = this.craftMatrix.removeStackFromSlot(var2);

				if (var3 != null) {
					par1EntityPlayer.entityDropItem(var3, 0.0F);
				}
			}
		}
	}

	@Override
	public void onCraftMatrixChanged(IInventory par1IInventory) {
		this.craftResult.setInventorySlotContents(0, SatelliteRocketRecipes.findMatchingSatelliteRocketRecipe(this.craftMatrix));
	}

	@Override
	public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par1) {
		ItemStack var2 = null;
		final Slot var3 = this.inventorySlots.get(par1);

		if (var3 != null && var3.getHasStack()) {
			final ItemStack var4 = var3.getStack();
			var2 = var4.copy();
			if (par1 <= 33) {
				if (!this.mergeItemStack(var4, 22 + 12, 58 + 12, false)) {
					return null;
				}

				var3.onSlotChange(var4, var2);
			} else {
				boolean valid = false;
				for (int i = 1; i < 31; i++) {
					Slot testSlot = this.inventorySlots.get(i);
					if (!testSlot.getHasStack() && testSlot.isItemValid(var2)) {
						valid = true;
						break;
					}
				}
				if (valid) {
					if (!this.mergeOneItemTestValid(var4, 1, 31, false)) {
						return null;
					}
				} else {
					if (var2.getItem() == Item.getItemFromBlock(Blocks.chest)) {
						if (!this.mergeOneItemTestValid(var4, 31, 34, false)) {
							return null;
						}
					} else if (par1 >= 22 + 12 && par1 < 49 + 12) {
						if (!this.mergeItemStack(var4, 49 + 12, 58 + 12, false)) {
							return null;
						}
					} else if (par1 >= 49 + 12 && par1 < 58 + 12) {
						if (!this.mergeItemStack(var4, 22 + 12, 49 + 12, false)) {
							return null;
						}
					} else if (!this.mergeItemStack(var4, 22 + 12, 58 + 12, false)) {
						return null;
					}
				}
			}

			if (var4.stackSize == 0) {
				var3.putStack((ItemStack) null);
			} else {
				var3.onSlotChanged();
			}

			if (var4.stackSize == var2.stackSize) {
				return null;
			}

			var3.onPickupFromSlot(par1EntityPlayer, var4);
		}

		return var2;
	}

	protected boolean mergeOneItemTestValid(ItemStack par1ItemStack, int par2, int par3, boolean par4) {
		boolean flag1 = false;
		if (par1ItemStack.stackSize != 0) {
			Slot slot;
			ItemStack slotStack;

			for (int k = par2; k < par3; k++) {
				slot = this.inventorySlots.get(k);
				slotStack = slot.getStack();

				if (slotStack.stackSize == 0 && slot.isItemValid(par1ItemStack)) {
					ItemStack stackOneItem = par1ItemStack.copy();
					stackOneItem.stackSize = 1;
					par1ItemStack.stackSize--;
					slot.putStack(stackOneItem);
					slot.onSlotChanged();
					flag1 = true;
					break;
				}
			}
		}

		return flag1;
	}
}