package com.mjr.planetprogression.tileEntities;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;

import com.mjr.mjrlegendslib.util.PlayerUtilties;
import com.mjr.mjrlegendslib.util.TranslateUtilities;
import com.mjr.planetprogression.blocks.BlockSatelliteController;
import com.mjr.planetprogression.data.SatelliteData;
import com.mjr.planetprogression.handlers.capabilities.CapabilityStatsHandler;
import com.mjr.planetprogression.handlers.capabilities.IStatsCapability;
import com.mjr.planetprogression.item.PlanetProgression_Items;
import com.mjr.planetprogression.item.ResearchPaper;

public class TileEntitySatelliteController extends TileBaseElectricBlockWithInventory implements ISidedInventory {
	public static final int PROCESS_TIME_REQUIRED = SatelliteData.MAX_DATA * 4;
	@NetworkedField(targetSide = Side.CLIENT)
	public int processTicks = 0;
	private ItemStack[] containingItems = new ItemStack[2];
	public SatelliteData currentSatellite = null;
	@NetworkedField(targetSide = Side.CLIENT)
	public int currentSatelliteNum = 0;
	public boolean markForSatelliteUpdate = true;
	@NetworkedField(targetSide = Side.CLIENT)
	public String owner = "";
	public ItemStack producingStack = null;

	public TileEntitySatelliteController() {
	}

	@Override
	public void update() {
		super.update();
		IStatsCapability stats = null;
		if (PlayerUtilties.getPlayerFromUUID(this.owner) != null) {
			stats = PlayerUtilties.getPlayerFromUUID(this.owner).getCapability(CapabilityStatsHandler.PP_STATS_CAPABILITY, null);
		}
		if (stats != null) {
			if (this.markForSatelliteUpdate) {
				if (this.currentSatellite != null)
					this.currentSatellite.dataAmount = this.processTicks;
				if (this.currentSatelliteNum > (stats.getSatellites().size() - 1))
					this.currentSatelliteNum = (stats.getSatellites().size() - 1);
				if (stats.getSatellites() != null && stats.getSatellites().size() != 0) {
					this.currentSatellite = stats.getSatellites().get(this.currentSatelliteNum);
					this.processTicks = this.currentSatellite.dataAmount;
					this.markForSatelliteUpdate = false;
				}
			}
			boolean found = false;
			for (Item item : PlanetProgression_Items.researchPapers) {
				if (item == null)
					continue;
				for (CelestialBody body : stats.getUnlockedPlanets()) {
					if (body == null)
						continue;
					if (((ResearchPaper) item).getPlanet().equalsIgnoreCase(body.getUnlocalizedName()))
						found = true;
				}
				if (!found) {
					this.producingStack = new ItemStack(item);
					break;
				}
			}
		}
		if (!this.worldObj.isRemote) {
			if (this.currentSatellite != null && this.canProcess() && canOutput() && this.hasEnoughEnergyToRun) {
				if (this.processTicks == 0) {
					this.processTicks = TileEntitySatelliteController.PROCESS_TIME_REQUIRED;
				} else {
					if (--this.processTicks <= 0) {
						this.smeltItem();
						this.processTicks = this.canProcess() ? TileEntitySatelliteController.PROCESS_TIME_REQUIRED : 0;
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
		if (this.containingItems[1] != null)
			return false;
		return true;
	}

	public boolean hasInputs() {
		return true;
	}

	public void smeltItem() {
		ItemStack resultItemStack = this.producingStack;
		if (this.canProcess() && canOutput() && hasInputs()) {
			if (this.containingItems[1] == null) {
				this.containingItems[1] = resultItemStack.copy();
			} else if (this.containingItems[1].isItemEqual(resultItemStack)) {
				if (this.containingItems[1].stackSize + resultItemStack.stackSize > 64) {
					for (int i = 0; i < this.containingItems[1].stackSize + resultItemStack.stackSize - 64; i++) {
						float var = 0.7F;
						double dx = this.worldObj.rand.nextFloat() * var + (1.0F - var) * 0.5D;
						double dy = this.worldObj.rand.nextFloat() * var + (1.0F - var) * 0.5D;
						double dz = this.worldObj.rand.nextFloat() * var + (1.0F - var) * 0.5D;
						EntityItem entityitem = new EntityItem(this.worldObj, this.getPos().getX() + dx, this.getPos().getY() + dy, this.getPos().getZ() + dz, new ItemStack(resultItemStack.getItem(), 1, resultItemStack.getItemDamage()));
						entityitem.setPickupDelay(10);
						this.worldObj.spawnEntityInWorld(entityitem);
					}
					this.containingItems[1].stackSize = 64;
				} else {
					this.containingItems[1].stackSize += resultItemStack.stackSize;
				}
			}
			this.currentSatellite.dataAmount = 0;
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		NBTTagList var2 = nbt.getTagList("Items", 10);
		this.containingItems = new ItemStack[this.getSizeInventory()];

		for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
			NBTTagCompound var4 = var2.getCompoundTagAt(var3);
			int var5 = var4.getByte("Slot") & 255;

			if (var5 < this.containingItems.length) {
				this.containingItems[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}
		this.processTicks = nbt.getInteger("smeltingTicks");
		this.currentSatelliteNum = nbt.getInteger("currentSatelliteNum");
		this.markForSatelliteUpdate = nbt.getBoolean("markForSatelliteUpdate");
		this.setOwner(nbt.getString("Owner"));
		this.containingItems = this.readStandardItemsFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		NBTTagList var2 = new NBTTagList();

		for (int var3 = 0; var3 < this.containingItems.length; ++var3) {
			if (this.containingItems[var3] != null) {
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte) var3);
				this.containingItems[var3].writeToNBT(var4);
				var2.appendTag(var4);
			}
		}

		nbt.setTag("Items", var2);
		nbt.setInteger("smeltingTicks", this.processTicks);
		nbt.setInteger("currentSatelliteNum", this.currentSatelliteNum);
		nbt.setBoolean("markForSatelliteUpdate", true);
		nbt.setString("Owner", this.getOwner());
		this.writeStandardItemsToNBT(nbt);
		return nbt;
	}

	@Override
	protected ItemStack[] getContainingItems() {
		return this.containingItems;
	}

	@Override
	public String getName() {
		return TranslateUtilities.translate("container.satellite.controller.name");
	}

	@Override
	public boolean hasCustomName() {
		return true;
	}

	// ISidedInventory Implementation:

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		if (side == EnumFacing.WEST) {
			return new int[] { 1 };
		} else if (side == EnumFacing.EAST) {
			return new int[] { 2 };
		} else if (side == EnumFacing.DOWN) {
			return new int[] { 3 };
		}
		return new int[] { 0, 1, 2, 3 };
	}

	@Override
	public boolean canInsertItem(int slotID, ItemStack itemstack, EnumFacing side) {
		if (itemstack != null && this.isItemValidForSlot(slotID, itemstack)) {
			switch (slotID) {
			case 0:
				return ItemElectricBase.isElectricItemCharged(itemstack);
			default:
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean canExtractItem(int slotID, ItemStack itemstack, EnumFacing side) {
		if (itemstack != null && this.isItemValidForSlot(slotID, itemstack)) {
			switch (slotID) {
			case 0:
				return ItemElectricBase.isElectricItemEmpty(itemstack) || !this.shouldPullEnergy();
			default:
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemstack) {
		switch (slotID) {
		case 0:
			return itemstack != null && ItemElectricBase.isElectricItem(itemstack.getItem());
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
		if (state.getBlock() instanceof BlockSatelliteController) {
			return (state.getValue(BlockSatelliteController.FACING));
		}
		return EnumFacing.NORTH;
	}

	@Override
	public ITextComponent getDisplayName() {
		return null;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwner() {
		return this.owner;
	}
}
