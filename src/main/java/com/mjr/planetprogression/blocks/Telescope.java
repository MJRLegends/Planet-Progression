package com.mjr.planetprogression.blocks;

import micdoodle8.mods.galacticraft.core.blocks.BlockAdvancedTile;
import micdoodle8.mods.galacticraft.core.blocks.ISortableBlock;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalElectrical;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import com.mjr.planetprogression.PlanetProgression;
import com.mjr.planetprogression.tileEntities.TileEntityTelescope;

public class Telescope extends BlockAdvancedTile implements IShiftDescription, ISortableBlock {
	//private final Random rand = new Random();

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public Telescope(String assetName) {
		super(Material.ROCK);
		this.setHardness(1.0F);
		this.setUnlocalizedName(assetName);
	}

	@Override
	public boolean onMachineActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entityPlayer, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		entityPlayer.openGui(PlanetProgression.instance, -1, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public boolean onUseWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		int metadata = getMetaFromState(world.getBlockState(pos));
		int change = world.getBlockState(pos).getValue(FACING).rotateY().getHorizontalIndex();

		world.setBlockState(pos, this.getStateFromMeta(metadata - (metadata % 4) + change), 3);

		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileBaseUniversalElectrical) {
			((TileBaseUniversalElectrical) te).updateFacing();
		}

		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityTelescope();
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
//		final TileEntityTelescope var7 = (TileEntityTelescope) worldIn.getTileEntity(pos);
//
//		if (var7 != null) {
//			for (int var8 = 0; var8 < var7.getSizeInventory(); ++var8) {
//				final ItemStack var9 = var7.getStackInSlot(var8);
//
//				if (var9 != null) {
//					final float var10 = this.rand.nextFloat() * 0.8F + 0.1F;
//					final float var11 = this.rand.nextFloat() * 0.8F + 0.1F;
//					final float var12 = this.rand.nextFloat() * 0.8F + 0.1F;
//
//					while (var9.stackSize > 0) {
//						int var13 = this.rand.nextInt(21) + 10;
//
//						if (var13 > var9.stackSize) {
//							var13 = var9.stackSize;
//						}
//
//						var9.stackSize -= var13;
//						final EntityItem var14 = new EntityItem(worldIn, pos.getX() + var10, pos.getY() + var11, pos.getZ() + var12, new ItemStack(var9.getItem(), var13, var9.getItemDamage()));
//
//						if (var9.hasTagCompound()) {
//							var14.getEntityItem().setTagCompound(var9.getTagCompound().copy());
//						}
//
//						final float var15 = 0.05F;
//						var14.motionX = (float) this.rand.nextGaussian() * var15;
//						var14.motionY = (float) this.rand.nextGaussian() * var15 + 0.2F;
//						var14.motionZ = (float) this.rand.nextGaussian() * var15;
//						worldIn.spawnEntityInWorld(var14);
//					}
//				}
//			}
//		}

		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		final int angle = MathHelper.floor_double(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
		worldIn.setBlockState(pos, getStateFromMeta(EnumFacing.getHorizontal(angle).getOpposite().getHorizontalIndex()), 3);
	}

	@Override
	public String getShiftDescription(int meta) {
		return GCCoreUtil.translate(this.getUnlocalizedName() + ".description");
	}

	@Override
	public boolean showDescription(int meta) {
		return true;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.getHorizontal(meta);
		return this.getDefaultState().withProperty(FACING, enumfacing);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}

	@Override
	public EnumSortCategoryBlock getCategory(int meta) {
		return EnumSortCategoryBlock.MACHINE;
	}
}
