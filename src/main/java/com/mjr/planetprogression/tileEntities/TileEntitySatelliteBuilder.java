package com.mjr.planetprogression.tileEntities;

import java.util.Map.Entry;

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
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;

public class TileEntitySatelliteBuilder extends TileBaseElectricBlockWithInventory implements ISidedInventory {
	public static final int PROCESS_TIME_REQUIRED = 100;
	@NetworkedField(targetSide = Side.CLIENT)
	public int processTicks = 0;

	public ItemStack producingStack = null;

	public TileEntitySatelliteBuilder() {
		super("container.satellite.builder.name");
		this.inventory = NonNullList.withSize(5, ItemStack.EMPTY);
	}

	@Override
	public void update() {
		super.update();

		this.producingStack = MachineRecipeManager.getOutputForInput(this.getInventory().subList(1, 4));

		if (!this.world.isRemote) {
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
		if (!this.getInventory().get(4).isEmpty())
			return false;
		return true;
	}

	public boolean hasInputs() {
		if (this.getInventory().get(1).isEmpty())
			return false;
		if (this.getInventory().get(2).isEmpty())
			return false;
		if (this.getInventory().get(3).isEmpty())
			return false;

		for (Entry<NonNullList<ItemStack>, ItemStack> recipe : MachineRecipeManager.getRecipes().entrySet()) {
			int i = 0;
			if (recipe.getValue().equals(this.producingStack)) {
				if (this.getInventory().get(1).getCount() < recipe.getKey().get(i).getCount())
					return false;
				i++;
				if (this.getInventory().get(2).getCount() < recipe.getKey().get(i).getCount())
					return false;
				i++;
				if (this.getInventory().get(3).getCount() < recipe.getKey().get(i).getCount())
					return false;
			}
		}
		return true;
	}

	public void smeltItem() {
		ItemStack resultItemStack = this.producingStack;
		if (this.canProcess() && canOutput() && hasInputs()) {
			if (this.getInventory().get(4).isEmpty()) {
				this.getInventory().set(4, resultItemStack.copy());
			} else if (this.getInventory().get(4).isItemEqual(resultItemStack)) {
				if (this.getInventory().get(4).getCount() + resultItemStack.getCount() > 64) {
					for (int i = 0; i < this.getInventory().get(3).getCount() + resultItemStack.getCount() - 64; i++) {
						float var = 0.7F;
						double dx = this.world.rand.nextFloat() * var + (1.0F - var) * 0.5D;
						double dy = this.world.rand.nextFloat() * var + (1.0F - var) * 0.5D;
						double dz = this.world.rand.nextFloat() * var + (1.0F - var) * 0.5D;
						EntityItem entityitem = new EntityItem(this.world, this.getPos().getX() + dx, this.getPos().getY() + dy, this.getPos().getZ() + dz, new ItemStack(resultItemStack.getItem(), 1, resultItemStack.getItemDamage()));
						entityitem.setPickupDelay(10);
						this.world.spawnEntity(entityitem);
					}
					this.getInventory().get(4).setCount(64);
				} else {
					this.getInventory().get(4).grow(resultItemStack.getCount());
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
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("smeltingTicks", this.processTicks);
		return nbt;
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
		IBlockState state = this.world.getBlockState(getPos());
		if (state.getBlock() instanceof BlockSatelliteBuilder) {
			return (state.getValue(BlockSatelliteBuilder.FACING));
		}
		return EnumFacing.NORTH;
	}
}
