package com.mjr.planetprogression.tileEntities;

import java.util.EnumSet;

import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Moon;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;

import com.mjr.mjrlegendslib.util.PlayerUtilties;
import com.mjr.planetprogression.Config;
import com.mjr.planetprogression.handlers.capabilities.CapabilityStatsHandler;
import com.mjr.planetprogression.handlers.capabilities.IStatsCapability;
import com.mjr.planetprogression.item.ResearchPaper;

public class TileEntityTelescope extends TileBaseElectricBlockWithInventory implements ISidedInventory {

	public static final int PROCESS_TIME_REQUIRED_BASE = (int) (200 * Config.telescopeTimeModifier);
	@NetworkedField(targetSide = Side.CLIENT)
	public int processTimeRequired = PROCESS_TIME_REQUIRED_BASE;
	@NetworkedField(targetSide = Side.CLIENT)
	public int processTicks = 0;
	@NetworkedField(targetSide = Side.CLIENT)
	public String owner = "";
	@NetworkedField(targetSide = Side.CLIENT)
	public boolean ownerOnline = false;
	@NetworkedField(targetSide = Side.CLIENT)
	public String ownerUsername = "";

	@NetworkedField(targetSide = Side.CLIENT)
	public float currentRotation;
	private NonNullList<ItemStack> stacks = NonNullList.withSize(2, ItemStack.EMPTY);

	public TileEntityTelescope() {
		super();
		this.storage.setMaxExtract(300);
	}

	@Override
	public void update() {
		if (!this.world.isRemote) {
			if (this.owner != "") {
				if (ownerOnline)
					this.ownerUsername = PlayerUtilties.getUsernameFromUUID(this.owner);
				this.ownerOnline = PlayerUtilties.isPlayerOnlineByUUID(this.owner);
				if (this.hasEnoughEnergyToRun && ownerOnline) {
					if (this.canResearch()) {
						++this.processTicks;

						this.processTimeRequired = TileEntityTelescope.PROCESS_TIME_REQUIRED_BASE * 2 / (1 + this.poweredByTierGC);

						if (this.processTicks >= this.processTimeRequired) {
							this.world.playSound(null, this.getPos(), SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.BLOCKS, 0.3F, this.world.rand.nextFloat() * 0.1F + 0.9F);
							this.processTicks = 0;
							this.doResearch();
						}
					} else {
						this.processTicks = 0;
					}
				} else {
					this.processTicks = 0;
				}
			}
		}
		super.update();
	}

	private void doResearch() {
		IStatsCapability stats = null;

		EntityPlayerMP player = PlayerUtilties.getPlayerFromUUID(this.owner);
		if (player != null) {
			stats = player.getCapability(CapabilityStatsHandler.PP_STATS_CAPABILITY, null);
		}

		if (Config.researchMode == 0 || Config.researchMode == 1 || Config.researchMode == 2 || Config.researchMode == 3) {
			boolean found = false;
			for (Planet planet : GalaxyRegistry.getRegisteredPlanets().values()) {
				if (((ResearchPaper) this.stacks.get(1).getItem()).getPlanetName().equalsIgnoreCase(planet.getUnlocalizedName())) {
					if (!stats.getUnlockedPlanets().contains(planet)) {
						stats.addUnlockedPlanets(planet);
						player.sendMessage(new TextComponentString("Research Completed! You have discovered " + planet.getLocalizedName()));
						this.stacks.set(1, ItemStack.EMPTY);
						found = true;
						break;
					}
				}
			}
			if (found == false) {
				for (Moon moon : GalaxyRegistry.getRegisteredMoons().values()) {
					if (((ResearchPaper) this.stacks.get(1).getItem()).getPlanetName().equalsIgnoreCase(moon.getUnlocalizedName())) {
						if (!stats.getUnlockedPlanets().contains(moon)) {
							stats.addUnlockedPlanets(moon);
							player.sendMessage(new TextComponentString("Research Completed! You have discovered " + moon.getLocalizedName()));
							this.stacks.set(1, ItemStack.EMPTY);
							break;
						}
					}
				}
			}
		}
	}

	private boolean canResearch() {
		if (this.stacks.get(1) != null && this.stacks.get(1).getItem() instanceof ResearchPaper) {
			return true;
		}
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.stacks = this.readStandardItemsFromNBT(nbt);
		this.processTicks = nbt.getInteger("smeltingTicks");
		this.owner = nbt.getString("owner");
		this.ownerUsername = nbt.getString("ownerUsername");
		this.currentRotation = nbt.getFloat("currentRotation");
		this.ownerOnline = nbt.getBoolean("ownerOnline");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("smeltingTicks", this.processTicks);
		this.writeStandardItemsToNBT(nbt, this.stacks);
		nbt.setString("owner", this.owner);
		nbt.setString("ownerUsername", this.ownerUsername);
		nbt.setFloat("currentRotation", this.currentRotation);
		nbt.setBoolean("ownerOnline", false); // False to trigger for Update on Load
		return nbt;
	}

	@Override
	protected NonNullList<ItemStack> getContainingItems() {
		return this.stacks;
	}

	@Override
	public String getName() {
		return GCCoreUtil.translate("container.telescope.name");
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[] { 0 };
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
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return index == 0;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return this.isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public EnumSet<EnumFacing> getElectricalOutputDirections() {
		return EnumSet.noneOf(EnumFacing.class);
	}

	@Override
	public boolean shouldUseEnergy() {
		return !this.getDisabled(0);
	}

	@Override
	public EnumFacing getElectricInputDirection() {
		return EnumFacing.getFront((this.getBlockMetadata() & 3) + 2);
	}

	@Override
	public void setDisabled(int index, boolean disabled) {
		if (this.disableCooldown == 0) {
			switch (index) {
			case 0:
				this.disabled = disabled;
				this.disableCooldown = 10;
				break;
			default:
				break;
			}
		}
	}

	@Override
	public boolean getDisabled(int index) {
		switch (index) {
		case 0:
			return this.disabled;
		default:
			break;
		}

		return true;
	}

	@Override
	public EnumFacing getFront() {
		return EnumFacing.NORTH;
	}
}