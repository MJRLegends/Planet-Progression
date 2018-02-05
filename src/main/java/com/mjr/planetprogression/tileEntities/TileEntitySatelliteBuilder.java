package com.mjr.planetprogression.tileEntities;

import java.util.Arrays;

import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;

import com.mjr.mjrlegendslib.util.TranslateUtilities;
import com.mjr.planetprogression.blocks.BlockSatelliteBuilder;
import com.mjr.planetprogression.recipes.MachineRecipeManager;

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
		if (this.containingItems[1].stackSize > 12)
			return false;
		if (this.containingItems[2].stackSize > 12)
			return false;
		if (this.containingItems[3].stackSize > 12)
			return false;
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
			this.decrStackSize(3, 1);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.processTicks = nbt.getInteger("smeltingTicks");
		this.containingItems = this.readStandardItemsFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("smeltingTicks", this.processTicks);
		this.writeStandardItemsToNBT(nbt);
		return nbt;
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
		return new int[] { 0, 1, 2, 3 };
	}

	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemStack) {
		return slotID == 0 && ItemElectricBase.isElectricItem(itemStack.getItem());
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return index == 0;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return this.isItemValidForSlot(index, itemStackIn);
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

	@Override
	public ITextComponent getDisplayName() {
		return null;
	}
}
