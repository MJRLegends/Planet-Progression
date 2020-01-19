package com.mjr.planetprogression.tileEntities;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

import com.mjr.mjrlegendslib.util.PlayerUtilties;
import com.mjr.mjrlegendslib.util.TranslateUtilities;
import com.mjr.planetprogression.Config;
import com.mjr.planetprogression.PlanetProgression;
import com.mjr.planetprogression.blocks.BlockTelescopeFake;
import com.mjr.planetprogression.blocks.PlanetProgression_Blocks;
import com.mjr.planetprogression.handlers.capabilities.CapabilityStatsHandler;
import com.mjr.planetprogression.handlers.capabilities.IStatsCapability;
import com.mjr.planetprogression.item.ResearchPaper;

import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Moon;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti.EnumBlockMultiType;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityTelescope extends TileBaseElectricBlockWithInventory implements IMultiBlock, ISidedInventory {

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
	public boolean alreadyResearchedInput = false;

	@NetworkedField(targetSide = Side.CLIENT)
	public float currentRotation;

	@NetworkedField(targetSide = Side.CLIENT)
	private AxisAlignedBB renderAABB;

	public TileEntityTelescope() {
		super("container.telescope.name");
		this.storage.setMaxExtract(100);
		this.inventory = NonNullList.withSize(2, ItemStack.EMPTY);
	}

	@Override
	public void update() {
		if (!this.world.isRemote) {
			if (this.owner != "") {
				if (ownerOnline)
					this.ownerUsername = PlayerUtilties.getUsernameFromUUID(this.owner);
				try {
					if(this.owner != "")
						this.ownerOnline = PlayerUtilties.isPlayerOnlineByUUID(this.owner);
				} catch (Exception e) {
					this.ownerOnline = false;
				}
				if (this.hasEnoughEnergyToRun) {
					if (ownerOnline) {
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
				if (((ResearchPaper) this.getInventory().get(1).getItem()).getBodyName().equalsIgnoreCase(planet.getUnlocalizedName())) {
					if (!stats.getUnlockedPlanets().contains(planet)) {
						stats.addUnlockedPlanets(planet);
						player.sendMessage(new TextComponentString(TranslateUtilities.translate("telescope.use.unlocked") + planet.getLocalizedName()));
						if (this.getInventory().get(1).getCount() != 1)
							this.getInventory().get(1).setCount(this.getInventory().get(1).getCount() - 1);
						else
							this.getInventory().set(1, ItemStack.EMPTY);
						found = true;
						break;
					}
				}
			}
			if (found == false) {
				for (Moon moon : GalaxyRegistry.getRegisteredMoons().values()) {
					if (((ResearchPaper) this.getInventory().get(1).getItem()).getBodyName().equalsIgnoreCase(moon.getUnlocalizedName())) {
						if (!stats.getUnlockedPlanets().contains(moon)) {
							stats.addUnlockedPlanets(moon);
							player.sendMessage(new TextComponentString(TranslateUtilities.translate("telescope.use.unlocked") + moon.getLocalizedName()));
							if (this.getInventory().get(1).getCount() != 1)
								this.getInventory().get(1).setCount(this.getInventory().get(1).getCount() - 1);
							else
								this.getInventory().set(1, ItemStack.EMPTY);
							break;
						}
					}
				}
			}
		}
	}

	private boolean canResearch() {
		if (this.getDisabled(0))
			return false;
		if (this.getInventory().get(1) != null && this.getInventory().get(1).getItem() instanceof ResearchPaper) {
			IStatsCapability stats = null;

			EntityPlayerMP player = PlayerUtilties.getPlayerFromUUID(this.owner);
			if (player != null) {
				stats = player.getCapability(CapabilityStatsHandler.PP_STATS_CAPABILITY, null);
			}
			boolean found = false;
			for (Planet planet : GalaxyRegistry.getRegisteredPlanets().values()) {
				if (((ResearchPaper) this.getInventory().get(1).getItem()).getBodyName().equalsIgnoreCase(planet.getUnlocalizedName())) {
					if (stats.getUnlockedPlanets().contains(planet)) {
						alreadyResearchedInput = true;
						return false;
					} else
						return true;
				}
			}
			if (found == false) {
				for (Moon moon : GalaxyRegistry.getRegisteredMoons().values()) {
					if (((ResearchPaper) this.getInventory().get(1).getItem()).getBodyName().equalsIgnoreCase(moon.getUnlocalizedName())) {
						if (stats.getUnlockedPlanets().contains(moon)) {
							alreadyResearchedInput = true;
							return false;
						} else
							return true;
					}
				}
			}
		}
		else
			alreadyResearchedInput = false;
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
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
		nbt.setString("owner", this.owner);
		nbt.setString("ownerUsername", this.ownerUsername);
		nbt.setFloat("currentRotation", this.currentRotation);
		nbt.setBoolean("ownerOnline", false); // False to trigger for Update on Load
		return nbt;
	}

	@Override
	public boolean onActivated(EntityPlayer entityPlayer) {
		entityPlayer.openGui(PlanetProgression.instance, -1, this.world, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
		return true;
	}

	@Override
	public void onCreate(World world, BlockPos placedPosition) {
		List<BlockPos> positions = new LinkedList<BlockPos>();
		this.getPositions(placedPosition, positions);
		for (BlockPos vecToAdd : positions)
			((BlockTelescopeFake) PlanetProgression_Blocks.FAKE_TELESCOPE).makeFakeBlock(world, vecToAdd, placedPosition,
					PlanetProgression_Blocks.FAKE_TELESCOPE.getDefaultState().withProperty(BlockTelescopeFake.TOP, vecToAdd.getY() == placedPosition.getY() + 2));
	}

	@Override
	public BlockMulti.EnumBlockMultiType getMultiType() {
		// Not actually used - maybe this shouldn't be an IMultiBlock at all?
		return EnumBlockMultiType.MINER_BASE;
	}

	@Override
	public void getPositions(BlockPos placedPosition, List<BlockPos> positions) {
		positions.add(placedPosition.add(0, 1, 0));
		positions.add(placedPosition.add(0, 2, 0));
	}

	@Override
	public void onDestroy(TileEntity callingBlock) {
		final BlockPos thisBlock = getPos();
		List<BlockPos> positions = new LinkedList<BlockPos>();
		this.getPositions(thisBlock, positions);

		for (BlockPos pos : positions) {
			IBlockState stateAt = this.world.getBlockState(pos);

			if (stateAt.getBlock() == PlanetProgression_Blocks.FAKE_TELESCOPE) {
				this.world.destroyBlock(pos, false);
			}
		}
		this.world.destroyBlock(thisBlock, true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		if (this.renderAABB == null) {
			this.renderAABB = new AxisAlignedBB(getPos().getX() - 1, getPos().getY(), getPos().getZ() - 1, getPos().getX() + 2, getPos().getY() + 4, getPos().getZ() + 2);
		}
		return this.renderAABB;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared() {
		return Constants.RENDERDISTANCE_MEDIUM;
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
	public int getInventoryStackLimit() {
		return 64;
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
		return true;
	}

	@Override
	public float receiveElectricity(EnumFacing from, float energy, int tier, boolean doReceive) {
		return 0;
	}

	@Override
	public EnumSet<EnumFacing> getElectricalInputDirections() {
		return EnumSet.noneOf(EnumFacing.class);
	}

	@Override
	public boolean canConnect(EnumFacing direction, NetworkType type) {
		if (direction == null || type == NetworkType.POWER) {
			return false;
		}
		return false;

	}

	@Override
	public ItemStack getBatteryInSlot() {
		return this.getStackInSlot(0);
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