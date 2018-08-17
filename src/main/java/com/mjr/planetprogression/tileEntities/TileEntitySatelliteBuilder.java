package com.mjr.planetprogression.tileEntities;

import java.util.Arrays;
import java.util.Map.Entry;

import com.mjr.mjrlegendslib.util.TranslateUtilities;
import com.mjr.planetprogression.blocks.BlockSatelliteBuilder;
import com.mjr.planetprogression.item.PlanetProgression_Items;
import com.mjr.planetprogression.recipes.MachineRecipeManager;

import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;

public class TileEntitySatelliteBuilder extends TileBaseElectricBlockWithInventory implements ISidedInventory {
	public static final int PROCESS_TIME_REQUIRED = 100;
	@NetworkedField(targetSide = Side.CLIENT)
	public int processTicks = 0;
	private ItemStack[] containingItems = new ItemStack[5];

	public ItemStack producingStack = null;

	public TileEntitySatelliteBuilder() {
	}

	@Override
	public void update() {
		super.update();

		this.producingStack = MachineRecipeManager.getOutputForInput(Arrays.copyOfRange(this.containingItems, 1, 4));

		if (!this.worldObj.isRemote) {
			if (this.canProcess() && canOutput() && this.hasEnoughEnergyToRun) {
				if (this.processTicks == 0) {
					this.processTicks = TileEntitySatelliteBuilder.PROCESS_TIME_REQUIRED;
				} else {
					if (--this.processTicks <= 0) {
						this.smeltItem();
						this.processTicks = this.canProcess() ? TileEntitySatelliteBuilder.PROCESS_TIME_REQUIRED : 0;
					}
				}
			} else {
				this.processTicks = 0;
			}
		}
	}

	public boolean canProcess() {
		if (this.producingStack == null)
			return false;
		return !this.getDisabled(0);
	}

	public boolean canOutput() {
		if (this.containingItems[4] != null)
			return false;
		return true;
	}

	public boolean hasInputs() {
		if (this.containingItems[1] == null)
			return false;
		if (this.containingItems[2] == null)
			return false;
		if (this.containingItems[3] == null)
			return false;

		for (Entry<ItemStack[], ItemStack> recipe : MachineRecipeManager.getRecipes().entrySet()) {
			int i = 0;
			if (recipe.getValue().equals(this.producingStack)) {
				if (this.containingItems[1].stackSize < recipe.getKey()[i].stackSize)
					return false;
				i++;
				if (this.containingItems[2].stackSize < recipe.getKey()[i].stackSize)
					return false;
				i++;
				if (this.containingItems[3].stackSize < recipe.getKey()[i].stackSize)
					return false;
			}
		}
		return true;
	}

	public void smeltItem() {
		ItemStack resultItemStack = this.producingStack;
		if (this.canProcess() && canOutput() && hasInputs()) {
			if (this.containingItems[4] == null) {
				this.containingItems[4] = resultItemStack.copy();
			} else if (this.containingItems[4].isItemEqual(resultItemStack)) {
				if (this.containingItems[4].stackSize + resultItemStack.stackSize > 64) {
					for (int i = 0; i < this.containingItems[3].stackSize + resultItemStack.stackSize - 64; i++) {
						float var = 0.7F;
						double dx = this.worldObj.rand.nextFloat() * var + (1.0F - var) * 0.5D;
						double dy = this.worldObj.rand.nextFloat() * var + (1.0F - var) * 0.5D;
						double dz = this.worldObj.rand.nextFloat() * var + (1.0F - var) * 0.5D;
						EntityItem entityitem = new EntityItem(this.worldObj, this.getPos().getX() + dx, this.getPos().getY() + dy, this.getPos().getZ() + dz, new ItemStack(resultItemStack.getItem(), 1, resultItemStack.getItemDamage()));
						entityitem.setPickupDelay(10);
						this.worldObj.spawnEntityInWorld(entityitem);
					}
					this.containingItems[4].stackSize = 64;
				} else {
					this.containingItems[4].stackSize += resultItemStack.stackSize;
				}
			}
			this.decrStackSize(1, 12);
			this.decrStackSize(2, 12);
			this.decrStackSize(3, 6);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.processTicks = nbt.getInteger("smeltingTicks");
		this.containingItems = this.readStandardItemsFromNBT(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("smeltingTicks", this.processTicks);
		this.writeStandardItemsToNBT(nbt);
	}

	@Override
	protected ItemStack[] getContainingItems() {
		return this.containingItems;
	}

	@Override
	public String getName() {
		return TranslateUtilities.translate("container.satellite.builder.name");
	}

	@Override
	public boolean hasCustomName() {
		return true;
	}

	// ISidedInventory Implementation:

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		if (side == EnumFacing.DOWN) {
			return new int[] { 4 };
		} else if (side == EnumFacing.EAST) {
			return new int[] { 3 };
		} else if (side == EnumFacing.WEST) {
			return new int[] { 1, 2 };
		}
		return new int[] { 0, 1, 2, 3, 4 };
	}

	@Override
	public boolean canInsertItem(int slotID, ItemStack itemStack, EnumFacing side) {
		if (itemStack != null && this.isItemValidForSlot(slotID, itemStack)) {
			switch (slotID) {
			case 0:
				return ItemElectricBase.isElectricItemCharged(itemStack);
			case 1:
				return itemStack.getItem() == GCItems.basicItem && itemStack.getMetadata() == 1;
			case 2:
				return itemStack.getItem() == GCItems.basicItem && itemStack.getMetadata() == 1;
			case 3:
				return itemStack.getItem() == PlanetProgression_Items.satelliteBasicModule;
			default:
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean canExtractItem(int slotID, ItemStack itemStack, EnumFacing side) {
		if (itemStack != null && this.isItemValidForSlot(slotID, itemStack)) {
			switch (slotID) {
			case 0:
				return ItemElectricBase.isElectricItemEmpty(itemStack) || !this.shouldPullEnergy();
			case 4:
				return this.producingStack == null ? false : itemStack.getItem() == this.producingStack.getItem();
			default:
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemStack) {
		switch (slotID) {
		case 0:
			return itemStack != null && ItemElectricBase.isElectricItem(itemStack.getItem());
		case 1:
			return itemStack.getItem() == GCItems.basicItem && itemStack.getMetadata() == 1;
		case 2:
			return itemStack.getItem() == GCItems.basicItem && itemStack.getMetadata() == 1;
		case 3:
			return itemStack.getItem() == PlanetProgression_Items.satelliteBasicModule;
		case 4:
			return this.producingStack == null ? false : itemStack.getItem() == this.producingStack.getItem();
		}

		return false;
	}

	@Override
	public boolean shouldUseEnergy() {
		return this.canProcess();
	}

	@Override
	public EnumFacing getElectricInputDirection() {
		return EnumFacing.UP;
	}

	@Override
	public EnumFacing getFront() {
		IBlockState state = this.worldObj.getBlockState(getPos());
		if (state.getBlock() instanceof BlockSatelliteBuilder) {
			return (state.getValue(BlockSatelliteBuilder.FACING));
		}
		return EnumFacing.NORTH;
	}
}
