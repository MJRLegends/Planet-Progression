package com.mjr.planetprogression.tileEntities;

import java.util.Map.Entry;

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

import com.mjr.mjrlegendslib.util.TranslateUtilities;
import com.mjr.planetprogression.blocks.BlockSatelliteBuilder;
import com.mjr.planetprogression.recipes.MachineRecipeManager;

public class TileEntitySatelliteBuilder extends TileBaseElectricBlockWithInventory implements ISidedInventory {
	public static final int PROCESS_TIME_REQUIRED = 100;
	@NetworkedField(targetSide = Side.CLIENT)
	public int processTicks = 0;
	private NonNullList<ItemStack> stacks = NonNullList.withSize(5, ItemStack.EMPTY);

	public ItemStack producingStack = null;

	public TileEntitySatelliteBuilder() {
	}

	@Override
	public void update() {
		super.update();

		this.producingStack = MachineRecipeManager.getOutputForInput(this.stacks.subList(1, 4));

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
		if (!this.stacks.get(4).isEmpty())
			return false;
		return true;
	}

	public boolean hasInputs() {
		if (this.stacks.get(1).isEmpty())
			return false;
		if (this.stacks.get(2).isEmpty())
			return false;
		if (this.stacks.get(3).isEmpty())
			return false;

		for (Entry<NonNullList<ItemStack>, ItemStack> recipe : MachineRecipeManager.getRecipes().entrySet()) {
			int i = 0;
			if (recipe.getValue().equals(this.producingStack)) {
				if (this.stacks.get(1).getCount() < recipe.getKey().get(i).getCount())
					return false;
				i++;
				if (this.stacks.get(2).getCount() < recipe.getKey().get(i).getCount())
					return false;
				i++;
				if (this.stacks.get(3).getCount() < recipe.getKey().get(i).getCount())
					return false;
			}
		}
		return true;
	}

	public void smeltItem() {
		ItemStack resultItemStack = this.producingStack;
		if (this.canProcess() && canOutput() && hasInputs()) {
			if (this.stacks.get(4).isEmpty()) {
				this.stacks.set(4, resultItemStack.copy());
			} else if (this.stacks.get(4).isItemEqual(resultItemStack)) {
				if (this.stacks.get(4).getCount() + resultItemStack.getCount() > 64) {
					for (int i = 0; i < this.stacks.get(3).getCount() + resultItemStack.getCount() - 64; i++) {
						float var = 0.7F;
						double dx = this.world.rand.nextFloat() * var + (1.0F - var) * 0.5D;
						double dy = this.world.rand.nextFloat() * var + (1.0F - var) * 0.5D;
						double dz = this.world.rand.nextFloat() * var + (1.0F - var) * 0.5D;
						EntityItem entityitem = new EntityItem(this.world, this.getPos().getX() + dx, this.getPos().getY() + dy, this.getPos().getZ() + dz, new ItemStack(resultItemStack.getItem(), 1, resultItemStack.getItemDamage()));
						entityitem.setPickupDelay(10);
						this.world.spawnEntity(entityitem);
					}
					this.stacks.get(4).setCount(64);
				} else {
					this.stacks.get(4).grow(resultItemStack.getCount());
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
		this.stacks = this.readStandardItemsFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("smeltingTicks", this.processTicks);
		this.writeStandardItemsToNBT(nbt, this.stacks);
		return nbt;
	}

	@Override
	protected NonNullList<ItemStack> getContainingItems() {
		return this.stacks;
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
		IBlockState state = this.world.getBlockState(getPos());
		if (state.getBlock() instanceof BlockSatelliteBuilder) {
			return (state.getValue(BlockSatelliteBuilder.FACING));
		}
		return EnumFacing.NORTH;
	}
}
