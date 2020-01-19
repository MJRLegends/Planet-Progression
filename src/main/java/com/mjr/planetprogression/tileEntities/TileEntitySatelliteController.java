package com.mjr.planetprogression.tileEntities;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
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

import com.mjr.mjrlegendslib.util.PlayerUtilties;
import com.mjr.mjrlegendslib.util.TranslateUtilities;
import com.mjr.planetprogression.Config;
import com.mjr.planetprogression.blocks.BlockSatelliteController;
import com.mjr.planetprogression.data.SatelliteData;
import com.mjr.planetprogression.handlers.capabilities.CapabilityStatsHandler;
import com.mjr.planetprogression.handlers.capabilities.IStatsCapability;
import com.mjr.planetprogression.item.PlanetProgression_Items;
import com.mjr.planetprogression.item.ResearchPaper;

public class TileEntitySatelliteController extends TileBaseElectricBlockWithInventory implements ISidedInventory {
	public static final int PROCESS_TIME_REQUIRED = (int) (SatelliteData.getMAX_DATA() * Config.satelliteControllerModifier);
	@NetworkedField(targetSide = Side.CLIENT)
	public int processTicks = 0;
	private ItemStack[] containingItems = new ItemStack[2];
	public SatelliteData currentSatellite = null;
	public boolean markForSatelliteUpdate = true;

	@NetworkedField(targetSide = Side.CLIENT)
	public int currentSatelliteNum = 0;
	@NetworkedField(targetSide = Side.CLIENT)
	public String currentSatelliteID = "";
	@NetworkedField(targetSide = Side.CLIENT)
	public String currentSatelliteResearchBody = "";
	@NetworkedField(targetSide = Side.CLIENT)
	public String owner = "";
	@NetworkedField(targetSide = Side.CLIENT)
	public boolean ownerOnline = false;
	@NetworkedField(targetSide = Side.CLIENT)
	public String ownerUsername = "";

	public ItemStack producingStack = null;

	public TileEntitySatelliteController() {
	}

	@Override
	public void update() {
		super.update();
		if (!this.worldObj.isRemote) {
			if (this.owner != "") {
				if (ownerOnline)
					this.ownerUsername = PlayerUtilties.getUsernameFromUUID(this.owner);
				try {
					if(this.owner != "")
						this.ownerOnline = PlayerUtilties.isPlayerOnlineByUUID(this.owner);
				} catch (Exception e) {
					this.ownerOnline = false;
				}
				IStatsCapability stats = null;
				if (ownerOnline && PlayerUtilties.getPlayerFromUUID(this.owner) != null) {
					stats = PlayerUtilties.getPlayerFromUUID(this.owner).getCapability(CapabilityStatsHandler.PP_STATS_CAPABILITY, null);
				}

				// Check player is online
				if (ownerOnline && stats != null) {
					// Update Controller for new Satellite (Triggers: onBlockPlaced, onWorldLoad, onDisplayedSatelliteChanged)
					if (this.markForSatelliteUpdate) {
						if (this.currentSatellite != null)
							this.currentSatellite.setDataAmount(this.currentSatellite.getDataAmount());
						int size = stats.getSatellites().size() == 0 ? 0 : (stats.getSatellites().size() - 1);
						if (this.currentSatelliteNum > size)
							this.currentSatelliteNum = size;
						if (stats.getSatellites() != null && stats.getSatellites().size() != 0) {
							this.currentSatellite = stats.getSatellites().get(this.currentSatelliteNum);
							this.processTicks = this.currentSatellite.getDataAmount();
							this.markForSatelliteUpdate = false;
						}
						this.currentSatelliteResearchBody = "";
					}

					// Check Satellite does exist
					if (this.currentSatellite != null) {
						this.currentSatellite.setDataAmount(this.processTicks);

						// Update Satellite ID for ClientSide
						this.currentSatelliteID = (this.currentSatellite != null ? this.currentSatellite.getUuid() : "No Satellites Found!");

						// Update Current Research Body for Client Side
						if (this.currentSatelliteResearchBody.equals("") || this.currentSatelliteResearchBody.equals("Nothing!")) {
							if (this.currentSatellite.getCurrentResearchItem() == null || this.currentSatellite.getCurrentResearchItem().getItem() == null)
								this.currentSatelliteResearchBody = "Nothing!";
							else
								this.currentSatelliteResearchBody = ((ResearchPaper) this.currentSatellite.getCurrentResearchItem().getItem()).getBodyName();
						}

						// Check if has a already existing research item
						if (this.currentSatellite.getCurrentResearchItem() != null && this.currentSatellite.getDataAmount() < TileEntitySatelliteController.PROCESS_TIME_REQUIRED)
							this.producingStack = this.currentSatellite.getCurrentResearchItem();

						// Assign satellite without a research item with a item
						else if (this.currentSatellite.getCurrentResearchItem() == null) {
							List<String> temp = new ArrayList<String>();
							for (SatelliteData sat : stats.getSatellites()) {
								if (sat.getCurrentResearchItem() != null)
									temp.add(((ResearchPaper) sat.getCurrentResearchItem().getItem()).getBodyName());
							}
							for (CelestialBody body : stats.getUnlockedPlanets()) {
								temp.add(body.getUnlocalizedName());
							}
							if (temp.size() != PlanetProgression_Items.researchPapers.size()) {
								boolean skip = false;
								for (int i = 0; i < PlanetProgression_Items.researchPapers.size(); i++) {
									skip = false;
									ItemStack newItem = new ItemStack(PlanetProgression_Items.researchPapers.get(i), 1, 0);
									if (temp.size() == 0) {
										this.producingStack = newItem;
										this.currentSatellite.setCurrentResearchItem(this.producingStack);
										return;
									} else {
										String newName = ((ResearchPaper) newItem.getItem()).getBodyName();
										if (temp.contains(newName))
											skip = true;
										if (!skip) {
											this.producingStack = newItem;
											this.currentSatellite.setCurrentResearchItem(this.producingStack);
											this.currentSatellite.setDataAmount(0);
											return;
										}
									}
								}
							}
						}

						// Processing Code
						if (this.canProcess() && canOutput() && this.hasEnoughEnergyToRun) {
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
			}
		}
	}

	public boolean canProcess() {
		return !this.getDisabled(0);
	}

	public boolean canOutput() {
		if (this.containingItems[1] != null)
			return false;
		if (this.producingStack == null || this.currentSatellite.getCurrentResearchItem() == null)
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
			this.producingStack = null;
			this.currentSatelliteResearchBody = "";

			IStatsCapability stats = null;
			if (ownerOnline && PlayerUtilties.getPlayerFromUUID(this.owner) != null) {
				stats = PlayerUtilties.getPlayerFromUUID(this.owner).getCapability(CapabilityStatsHandler.PP_STATS_CAPABILITY, null);
			}

			// Change Research Item when completed the the last one
			ItemStack oldItem = this.currentSatellite.getCurrentResearchItem();
			List<String> temp = new ArrayList<String>();
			for (SatelliteData sat : stats.getSatellites()) {
				if (sat.getCurrentResearchItem() != null)
					temp.add(((ResearchPaper) sat.getCurrentResearchItem().getItem()).getBodyName());
			}
			for (CelestialBody body : stats.getUnlockedPlanets()) {
				temp.add(body.getUnlocalizedName());
			}
			if (temp.size() != PlanetProgression_Items.researchPapers.size()) {
				boolean skip = false;
				for (int i = 0; i < PlanetProgression_Items.researchPapers.size(); i++) {
					skip = false;
					ItemStack newItem = new ItemStack(PlanetProgression_Items.researchPapers.get(i), 1, 0);
					if (temp.size() == 0) {
						this.producingStack = newItem;
						this.currentSatellite.setCurrentResearchItem(this.producingStack);
						return;
					} else {
						String oldName = ((ResearchPaper) oldItem.getItem()).getBodyName();
						String newName = ((ResearchPaper) newItem.getItem()).getBodyName();
						if (temp.contains(newName) || oldName.equalsIgnoreCase(newName))
							skip = true;
						if (!skip) {
							this.producingStack = newItem;
							this.currentSatellite.setCurrentResearchItem(this.producingStack);
							this.currentSatellite.setDataAmount(0);
							markForSatelliteUpdate = true;
							return;
						}
					}
				}
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.containingItems = this.readStandardItemsFromNBT(nbt);
		this.processTicks = nbt.getInteger("smeltingTicks");
		this.currentSatelliteNum = nbt.getInteger("currentSatelliteNum");
		this.currentSatelliteID = nbt.getString("currentSatelliteID");
		this.currentSatelliteResearchBody = nbt.getString("currentSatelliteResearchBody");
		this.markForSatelliteUpdate = nbt.getBoolean("markForSatelliteUpdate");
		this.setOwner(nbt.getString("owner"));
		this.ownerUsername = nbt.getString("ownerUsername");
		this.containingItems = this.readStandardItemsFromNBT(nbt);
		this.ownerOnline = nbt.getBoolean("ownerOnline");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("smeltingTicks", this.processTicks);
		this.writeStandardItemsToNBT(nbt);
		nbt.setInteger("currentSatelliteNum", this.currentSatelliteNum);
		nbt.setString("currentSatelliteID", this.currentSatelliteID);
		nbt.setString("currentSatelliteResearchBody", this.currentSatelliteResearchBody);
		nbt.setBoolean("markForSatelliteUpdate", true); // Mark for Update on Load
		nbt.setString("owner", this.getOwner());
		nbt.setString("ownerUsername", this.ownerUsername);
		nbt.setBoolean("ownerOnline", false); // False to trigger for Update on Load
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
		return new int[] { 1 };
	}

	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemStack) {
		return slotID == 0 && ItemElectricBase.isElectricItem(itemStack.getItem());
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return index == 1;
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
		if (state.getBlock() instanceof BlockSatelliteController) {
			return (state.getValue(BlockSatelliteController.FACING));
		}
		return EnumFacing.NORTH;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwner() {
		return this.owner;
	}
}
