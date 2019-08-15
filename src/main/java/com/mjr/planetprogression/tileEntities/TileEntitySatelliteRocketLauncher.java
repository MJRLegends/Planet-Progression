package com.mjr.planetprogression.tileEntities;

import java.util.ArrayList;
import java.util.List;

import com.mjr.mjrlegendslib.util.TranslateUtilities;
import com.mjr.planetprogression.blocks.BlockCustomLandingPadFull;
import com.mjr.planetprogression.blocks.BlockSatelliteRocketLauncher;
import com.mjr.planetprogression.entities.EntitySatelliteRocket;

import micdoodle8.mods.galacticraft.api.entity.IDockable;
import micdoodle8.mods.galacticraft.api.tile.IFuelDock;
import micdoodle8.mods.galacticraft.api.tile.ILandingPadAttachable;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;

public class TileEntitySatelliteRocketLauncher extends TileBaseElectricBlockWithInventory implements ISidedInventory, ILandingPadAttachable {
	public static final int WATTS_PER_TICK = 1;
	private ItemStack[] containingItems = new ItemStack[1];
	private List<BlockPos> connectedPads = new ArrayList<BlockPos>();
	public Object attachedDock = null;
	@NetworkedField(targetSide = Side.CLIENT)
	public int launchDropdownSelection;
	@NetworkedField(targetSide = Side.CLIENT)
	public boolean launchEnabled;

	public TileEntitySatelliteRocketLauncher() {
		this.storage.setMaxExtract(6);
		this.noRedstoneControl = true;
		this.launchDropdownSelection = 0;
		this.launchEnabled = false;
	}

	public boolean canRun() {
		return this.hasEnoughEnergyToRun && this.launchEnabled;
	}

	@Override
	public void update() {
		super.update();

		if (!this.worldObj.isRemote) {

			if (this.ticks % 20 == 0 && canRun()) {
				if (connectedPads.size() == 0) {
					for (int x = -4; x <= 4; x++) {
						for (int z = -4; z <= 4; z++) {
							Block blockID = this.worldObj.getBlockState(this.getPos().add(x, 0, z)).getBlock();
							if (blockID instanceof BlockCustomLandingPadFull) {
								this.connectedPads.add(new BlockPos(this.getPos().getX() + x, this.getPos().getY(), this.getPos().getZ() + z));
							}
						}
					}
				} else
					updateRocketOnDockSettings();
			}
		}
	}

	@Override
	public boolean shouldUseEnergy() {
		return this.canRun();
	}

	@Override
	public void invalidate() {
		super.invalidate();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.containingItems = this.readStandardItemsFromNBT(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		this.writeStandardItemsToNBT(nbt);
		return nbt;
	}

	@Override
	public ItemStack[] getContainingItems() {
		return this.containingItems;
	}

	@Override
	public String getName() {
		return TranslateUtilities.translate("container.satellite_rocket_launcher.name");
	}

	@Override
	public boolean hasCustomName() {
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemStack) {
		return slotID == 0 && ItemElectricBase.isElectricItem(itemStack.getItem());
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[] { 0 };
	}

	@Override
	public boolean canInsertItem(int slotID, ItemStack par2ItemStack, EnumFacing par3) {
		return this.isItemValidForSlot(slotID, par2ItemStack);
	}

	@Override
	public boolean canExtractItem(int slotID, ItemStack par2ItemStack, EnumFacing par3) {
		return slotID == 0;
	}

	@Override
	public void setDisabled(int index, boolean disabled) {
		if (this.disableCooldown == 0) {
			switch (index) {
			case 0:
				this.disabled = disabled;
				this.disableCooldown = 10;
				break;
			}
		}
	}

	@Override
	public boolean getDisabled(int index) {
		switch (index) {
		case 0:
			return this.disabled;
		}

		return true;
	}

	@Override
	public boolean canAttachToLandingPad(IBlockAccess world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);

		return tile instanceof TileEntitySatelliteLandingPad;
	}

	public void setLaunchDropdownSelection(int newvalue) {
		if (newvalue != this.launchDropdownSelection) {
			this.launchDropdownSelection = newvalue;
			this.storage.extractEnergyGC(25, false);
			this.updateRocketOnDockSettings();
		}
	}

	public void updateRocketOnDockSettings() {
		if (this.attachedDock instanceof TileEntitySatelliteLandingPad) {
			TileEntitySatelliteLandingPad pad = ((TileEntitySatelliteLandingPad) this.attachedDock);
			IDockable rocket = pad.getDockedEntity();
			if (rocket instanceof EntitySatelliteRocket && canRun()) {
				((EntitySatelliteRocket) rocket).updateControllerSettings(pad);
			}
		}
	}

	public void setAttachedPad(IFuelDock pad) {
		this.attachedDock = pad;
	}

	@Override
	public EnumFacing getFront() {
		IBlockState state = this.worldObj.getBlockState(getPos());
		if (state.getBlock() instanceof BlockSatelliteRocketLauncher) {
			return state.getValue(BlockSatelliteRocketLauncher.FACING);
		}
		return EnumFacing.NORTH;
	}

	@Override
	public EnumFacing getElectricInputDirection() {
		return EnumFacing.UP;
	}
}
