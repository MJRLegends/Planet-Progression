package com.mjr.planetprogression.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.netty.buffer.ByteBuf;

import com.mjr.mjrlegendslib.util.PlayerUtilties;
import com.mjr.mjrlegendslib.util.TranslateUtilities;
import com.mjr.planetprogression.PlanetProgression;
import com.mjr.planetprogression.data.SatelliteData;
import com.mjr.planetprogression.handlers.capabilities.CapabilityStatsHandler;
import com.mjr.planetprogression.handlers.capabilities.IStatsCapability;
import com.mjr.planetprogression.item.ItemSatellite;
import com.mjr.planetprogression.item.PlanetProgression_Items;
import com.mjr.planetprogression.network.PacketSimplePP;
import com.mjr.planetprogression.network.PacketSimplePP.EnumSimplePacket;
import com.mjr.planetprogression.tileEntities.TileEntitySatelliteLandingPad;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import micdoodle8.mods.galacticraft.api.entity.IRocketType;
import micdoodle8.mods.galacticraft.api.tile.IFuelDock;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.ContainerRocketInventory;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;

public class EntitySatelliteRocket extends EntitySatelliteAutoRocket {

	public EnumRocketType rocketType;
	public float rumble;

	public EntitySatelliteRocket(World world) {
		super(world);
		this.setSize(3.0F, 16.0F);
	}

	public EntitySatelliteRocket(World world, double x, double y, double z, IRocketType.EnumRocketType type) {
		super(world, x, y, z);
		this.rocketType = type;
		this.cargoItems = new ItemStack[this.getSizeInventory()];
	}

	public EntitySatelliteRocket(World world, double x, double y, double z, IRocketType.EnumRocketType type, EntityPlayer playerIn) {
		super(world, x, y, z);
		this.rocketType = type;
		this.cargoItems = new ItemStack[this.getSizeInventory()];
		this.placedPlayerUUID = playerIn.getUniqueID().toString();
	}

	@Override
	public double getYOffset() {
		return 1.5F;
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(PlanetProgression_Items.SATELLITE_ROCKET, 1, this.rocketType.getIndex());
	}

	@Override
	public double getMountedYOffset() {
		return 0.5;
	}

	@Override
	public double getOnPadYOffset() {
		return 0.4D;
	}

	@Override
	public float getRenderOffsetY() {
		return 1.1F;
	}

	@Override
	public int getRocketTier() {
		return Integer.MAX_VALUE;
	}

	@Override
	public int getFuelTankCapacity() {
		return 1000;
	}

	@Override
	public int getPreLaunchWait() {
		return 0;
	}

	@Override
	public List<ItemStack> getItemsDropped(List<ItemStack> droppedItems) {
		super.getItemsDropped(droppedItems);
		ItemStack rocket = new ItemStack(PlanetProgression_Items.SATELLITE_ROCKET, 1, this.rocketType.getIndex());
		rocket.setTagCompound(new NBTTagCompound());
		rocket.getTagCompound().setInteger("RocketFuel", this.fuelTank.getFluidAmount());
		droppedItems.add(rocket);
		return droppedItems;
	}

	@Override
	public boolean isDockValid(IFuelDock dock) {
		return (dock instanceof TileEntitySatelliteLandingPad);
	}

	@Override
	public String getName() {
		return TranslateUtilities.translate("entity.planetprogression.EntitySatelliteRocket.name", false);
	}

	@Override
	public void onReachAtmosphere() {
		if (!this.worldObj.isRemote) {
			EntityPlayerMP player = PlayerUtilties.getPlayerFromUUID(this.placedPlayerUUID);
			if (player != null) {
				int found = 0;
				for (ItemStack item : this.cargoItems) {
					if (item != null) {
						if (item.getItem() instanceof ItemSatellite) {
							found++;
							IStatsCapability stats = null;
							if (player != null) {
								stats = player.getCapability(CapabilityStatsHandler.PP_STATS_CAPABILITY, null);
							}
							String id;
							String itemName = item.getDisplayName();
							if (!itemName.equalsIgnoreCase(item.getDisplayName()))
								id = UUID.randomUUID().toString();
							else
								id = itemName;
							stats.addSatellites(new SatelliteData(((ItemSatellite) item.getItem()).getType(), id, 0, null));
							player.addChatMessage(new TextComponentString(EnumColor.RED + "Satellite: " + id + " has been launched in to space!"));
						}
					}
				}
				if (found == 0)
					player.addChatMessage(new TextComponentString(EnumColor.RED + "No Satellites were found in the rocket!"));
			}

			// Destroy any rocket which reached the top of the atmosphere and is not controlled by a Launch Controller
		}
		this.setDead();
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		int i;

		if (this.timeUntilLaunch >= 100) {
			i = Math.abs(this.timeUntilLaunch / 100);
		} else {
			i = 1;
		}

		if ((this.getLaunched() || this.launchPhase == EnumLaunchPhase.IGNITED.ordinal() && this.rand.nextInt(i) == 0) && !ConfigManagerCore.disableSpaceshipParticles && this.hasValidFuel()) {
			if (this.worldObj.isRemote) {
				this.spawnParticles(this.getLaunched());
			}
		}

		if (this.launchPhase >= EnumLaunchPhase.LAUNCHED.ordinal() && this.hasValidFuel()) {
			if (this.launchPhase == EnumLaunchPhase.LAUNCHED.ordinal()) {
				double d = this.timeSinceLaunch / 150;

				d = Math.min(d, 1);

				if (d != 0.0) {
					this.motionY = -d * 2.5D * Math.cos((this.rotationPitch - 180) / 57.2957795D);
				}
			} else {
				this.motionY -= 0.008D;
			}

			double multiplier = 1.0D;

			if (this.worldObj.provider instanceof IGalacticraftWorldProvider) {
				multiplier = ((IGalacticraftWorldProvider) this.worldObj.provider).getFuelUsageMultiplier();

				if (multiplier <= 0) {
					multiplier = 1;
				}
			}

			if (this.timeSinceLaunch % MathHelper.floor_double(2 * (1 / multiplier)) == 0) {
				this.removeFuel(1);
				if (!this.hasValidFuel()) {
					this.stopRocketSound();
				}
			}
		} else if (!this.hasValidFuel() && this.getLaunched() && !this.worldObj.isRemote) {
			if (Math.abs(Math.sin(this.timeSinceLaunch / 1000)) / 10 != 0.0) {
				this.motionY -= Math.abs(Math.sin(this.timeSinceLaunch / 1000)) / 20;
			}
		}
	}

	protected void spawnParticles(boolean launched) {
		if (!this.isDead) {
			double x1 = 3.2 * Math.cos(this.rotationYaw / 57.2957795D) * Math.sin(this.rotationPitch / 57.2957795D);
			double z1 = 3.2 * Math.sin(this.rotationYaw / 57.2957795D) * Math.sin(this.rotationPitch / 57.2957795D);
			double y1 = 3.2 * Math.cos((this.rotationPitch - 180) / 57.2957795D);
			if (this.launchPhase == EnumLaunchPhase.LANDING.ordinal()) {
				double modifier = this.posY;
				modifier = Math.max(modifier, 1.0);
				x1 *= modifier / 60.0D;
				y1 *= modifier / 60.0D;
				z1 *= modifier / 60.0D;
			}

			final double y2 = this.prevPosY + (this.posY - this.prevPosY) + y1;

			final double x2 = this.posX + x1;
			final double z2 = this.posZ + z1;
			Vector3 motionVec = new Vector3(x1, y1, z1);
			Vector3 d1 = new Vector3(y1 * 0.1D, -x1 * 0.1D, z1 * 0.1D).rotate(315 - this.rotationYaw, motionVec);
			Vector3 d2 = new Vector3(x1 * 0.1D, -z1 * 0.1D, y1 * 0.1D).rotate(315 - this.rotationYaw, motionVec);
			Vector3 d3 = new Vector3(-y1 * 0.1D, x1 * 0.1D, z1 * 0.1D).rotate(315 - this.rotationYaw, motionVec);
			Vector3 d4 = new Vector3(x1 * 0.1D, z1 * 0.1D, -y1 * 0.1D).rotate(315 - this.rotationYaw, motionVec);
			Vector3 mv1 = motionVec.clone().translate(d1);
			Vector3 mv2 = motionVec.clone().translate(d2);
			Vector3 mv3 = motionVec.clone().translate(d3);
			Vector3 mv4 = motionVec.clone().translate(d4);
			// T3 - Four flameballs which spread
			EntityLivingBase riddenByEntity = this.getPassengers().isEmpty() || !(this.getPassengers().get(0) instanceof EntityLivingBase) ? null : (EntityLivingBase) this.getPassengers().get(0);
			Object[] rider = new Object[] { riddenByEntity };
			makeFlame(x2 + d1.x, y2 + d1.y, z2 + d1.z, mv1, this.getLaunched(), rider);
			makeFlame(x2 + d2.x, y2 + d2.y, z2 + d2.z, mv2, this.getLaunched(), rider);
			makeFlame(x2 + d3.x, y2 + d3.y, z2 + d3.z, mv3, this.getLaunched(), rider);
			makeFlame(x2 + d4.x, y2 + d4.y, z2 + d4.z, mv4, this.getLaunched(), rider);
		}
	}

	private void makeFlame(double x2, double y2, double z2, Vector3 motionVec, boolean getLaunched, Object[] rider) {
		if (getLaunched) {
			GalacticraftCore.proxy.spawnParticle("launchFlameLaunched", new Vector3(x2 + 0.4 - this.rand.nextDouble() / 10, y2, z2 + 0.4 - this.rand.nextDouble() / 10), motionVec, rider);
			GalacticraftCore.proxy.spawnParticle("launchFlameLaunched", new Vector3(x2 - 0.4 + this.rand.nextDouble() / 10, y2, z2 + 0.4 - this.rand.nextDouble() / 10), motionVec, rider);
			GalacticraftCore.proxy.spawnParticle("launchFlameLaunched", new Vector3(x2 - 0.4 + this.rand.nextDouble() / 10, y2, z2 - 0.4 + this.rand.nextDouble() / 10), motionVec, rider);
			GalacticraftCore.proxy.spawnParticle("launchFlameLaunched", new Vector3(x2 + 0.4 - this.rand.nextDouble() / 10, y2, z2 - 0.4 + this.rand.nextDouble() / 10), motionVec, rider);
			GalacticraftCore.proxy.spawnParticle("launchFlameLaunched", new Vector3(x2, y2, z2), motionVec, rider);
			GalacticraftCore.proxy.spawnParticle("launchFlameLaunched", new Vector3(x2 + 0.4, y2, z2), motionVec, rider);
			GalacticraftCore.proxy.spawnParticle("launchFlameLaunched", new Vector3(x2 - 0.4, y2, z2), motionVec, rider);
			GalacticraftCore.proxy.spawnParticle("launchFlameLaunched", new Vector3(x2, y2, z2 + 0.4D), motionVec, rider);
			GalacticraftCore.proxy.spawnParticle("launchFlameLaunched", new Vector3(x2, y2, z2 - 0.4D), motionVec, rider);
			return;
		}

		double x1 = motionVec.x;
		double y1 = motionVec.y;
		double z1 = motionVec.z;
		GalacticraftCore.proxy.spawnParticle("launchFlameIdle", new Vector3(x2 + 0.4 - this.rand.nextDouble() / 10, y2, z2 + 0.4 - this.rand.nextDouble() / 10), new Vector3(x1 + 0.5D, y1 - 0.3D, z1 + 0.5D), rider);
		GalacticraftCore.proxy.spawnParticle("launchFlameIdle", new Vector3(x2 - 0.4 + this.rand.nextDouble() / 10, y2, z2 + 0.4 - this.rand.nextDouble() / 10), new Vector3(x1 - 0.5D, y1 - 0.3D, z1 + 0.5D), rider);
		GalacticraftCore.proxy.spawnParticle("launchFlameIdle", new Vector3(x2 - 0.4 + this.rand.nextDouble() / 10, y2, z2 - 0.4 + this.rand.nextDouble() / 10), new Vector3(x1 - 0.5D, y1 - 0.3D, z1 - 0.5D), rider);
		GalacticraftCore.proxy.spawnParticle("launchFlameIdle", new Vector3(x2 + 0.4 - this.rand.nextDouble() / 10, y2, z2 - 0.4 + this.rand.nextDouble() / 10), new Vector3(x1 + 0.5D, y1 - 0.3D, z1 - 0.5D), rider);
		GalacticraftCore.proxy.spawnParticle("launchFlameIdle", new Vector3(x2 + 0.4, y2, z2), new Vector3(x1 + 0.8D, y1 - 0.3D, z1), rider);
		GalacticraftCore.proxy.spawnParticle("launchFlameIdle", new Vector3(x2 - 0.4, y2, z2), new Vector3(x1 - 0.8D, y1 - 0.3D, z1), rider);
		GalacticraftCore.proxy.spawnParticle("launchFlameIdle", new Vector3(x2, y2, z2 + 0.4D), new Vector3(x1, y1 - 0.3D, z1 + 0.8D), rider);
		GalacticraftCore.proxy.spawnParticle("launchFlameIdle", new Vector3(x2, y2, z2 - 0.4D), new Vector3(x1, y1 - 0.3D, z1 - 0.8D), rider);
	}

	@Override
	public void onPadDestroyed() {
		if (!this.isDead && this.launchPhase != EnumLaunchPhase.LAUNCHED.ordinal()) {
			this.dropShipAsItem();
			this.setDead();
		}
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, ItemStack stack, EnumHand hand) {
		if (!this.worldObj.isRemote && player instanceof EntityPlayerMP) {
			EntityPlayerMP playerMP = (EntityPlayerMP) player;
			playerMP.getNextWindowId();
			playerMP.closeContainer();
			int windowId = playerMP.currentWindowId;
			PlanetProgression.packetPipeline.sendTo(new PacketSimplePP(EnumSimplePacket.C_OPEN_SATELLITE_ROCKET_GUI, GCCoreUtil.getDimensionID(playerMP.worldObj), new Object[] { windowId, this.getEntityId() }), playerMP);
			player.openContainer = new ContainerRocketInventory(playerMP.inventory, this, this.rocketType, playerMP);
			player.openContainer.windowId = windowId;
			player.openContainer.addListener(playerMP);
		}

		return false;
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		if (worldObj.isRemote)
			return;
		nbt.setInteger("Type", this.rocketType.getIndex());
		nbt.setString("PlacedPlayerUUID", this.placedPlayerUUID);

		super.writeEntityToNBT(nbt);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		this.rocketType = EnumRocketType.values()[nbt.getInteger("Type")];
		this.placedPlayerUUID = nbt.getString("PlacedPlayerUUID");

		super.readEntityFromNBT(nbt);
	}

	@Override
	public EnumRocketType getType() {
		return this.rocketType;
	}

	@Override
	public int getSizeInventory() {
		if (this.rocketType == null) {
			return 0;
		}
		return this.rocketType.getInventorySpace();
	}

	@Override
	public void decodePacketdata(ByteBuf buffer) {
		this.rocketType = EnumRocketType.values()[buffer.readInt()];
		super.decodePacketdata(buffer);
		this.posX = buffer.readDouble() / 8000.0D;
		this.posY = buffer.readDouble() / 8000.0D;
		this.posZ = buffer.readDouble() / 8000.0D;
	}

	@Override
	public void getNetworkedData(ArrayList<Object> list) {
		if (this.worldObj.isRemote) {
			return;
		}
		list.add(this.rocketType != null ? this.rocketType.getIndex() : 0);
		super.getNetworkedData(list);
		list.add(this.posX * 8000.0D);
		list.add(this.posY * 8000.0D);
		list.add(this.posZ * 8000.0D);
	}
}
