package com.mjr.planetprogression.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;

public class InventorySchematicSatelliteRocket implements IInventory {
	private final ItemStack[] stackList;
	private final int inventoryWidth;
	private final Container eventHandler;

	public InventorySchematicSatelliteRocket(Container par1Container) {
		this.stackList = new ItemStack[34];
		this.eventHandler = par1Container;
		this.inventoryWidth = 5;
	}

	@Override
	public int getSizeInventory() {
		return this.stackList.length;
	}

	@Override
	public ItemStack getStackInSlot(int par1) {
		return par1 >= this.getSizeInventory() ? null : this.stackList[par1];
	}

	public ItemStack getStackInRowAndColumn(int par1, int par2) {
		if (par1 >= 0 && par1 < this.inventoryWidth) {
			final int var3 = par1 + par2 * this.inventoryWidth;
			if (var3 >= 22) {
				return null;
			}
			return this.getStackInSlot(var3);
		} else {
			return null;
		}
	}

	@Override
	public String getName() {
		return "container.crafting";
	}

	@Override
	public ItemStack removeStackFromSlot(int par1) {
		if (this.stackList[par1] != null) {
			final ItemStack var2 = this.stackList[par1];
			this.stackList[par1] = null;
			return var2;
		} else {
			return null;
		}
	}

	@Override
	public ItemStack decrStackSize(int par1, int par2) {
		if (this.stackList[par1] != null) {
			ItemStack var3;

			if (this.stackList[par1].stackSize <= par2) {
				var3 = this.stackList[par1];
				this.stackList[par1] = null;
				this.eventHandler.onCraftMatrixChanged(this);
				return var3;
			} else {
				var3 = this.stackList[par1].splitStack(par2);

				if (this.stackList[par1].stackSize == 0) {
					this.stackList[par1] = null;
				}

				this.eventHandler.onCraftMatrixChanged(this);
				return var3;
			}
		} else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
		this.stackList[par1] = par2ItemStack;
		this.eventHandler.onCraftMatrixChanged(this);
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return false;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
	}

	@Override
	public IChatComponent getDisplayName() {
		return null;
	}
}
