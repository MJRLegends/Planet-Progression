package com.mjr.planetprogression.blocks;

import java.util.List;

import com.mjr.mjrlegendslib.util.TranslateUtilities;
import com.mjr.planetprogression.PlanetProgression;
import com.mjr.planetprogression.tileEntities.TileEntitySatelliteLandingPadSingle;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockAdvancedTile;
import micdoodle8.mods.galacticraft.core.blocks.ISortableBlock;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCustomLandingPad extends BlockAdvancedTile implements IPartialSealableBlock, IShiftDescription, ISortableBlock {
	public static final PropertyEnum<EnumLandingPadType> PAD_TYPE = PropertyEnum.create("type", EnumLandingPadType.class);
	protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.1875, 1.0);

	public enum EnumLandingPadType implements IStringSerializable {
		SATELLITE_ROCKET_PAD(0, "satellite_rocket_pad");

		private final int meta;
		private final String name;

		EnumLandingPadType(int meta, String name) {
			this.meta = meta;
			this.name = name;
		}

		public int getMeta() {
			return this.meta;
		}

		public static EnumLandingPadType byMetadata(int meta) {
			return values()[meta];
		}

		@Override
		public String getName() {
			return this.name;
		}
	}

	public BlockCustomLandingPad(String name) {
		super(Material.IRON);
		this.setHardness(1.0F);
		this.setResistance(10.0F);
		this.setSoundType(SoundType.METAL);
		this.setUnlocalizedName(name);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn() {
		return PlanetProgression.tab;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
		for (int i = 0; i < 1; i++) {
			par3List.add(new ItemStack(par1, 1, i));
		}
	}

	private boolean checkAxis(World world, BlockPos pos, Block block, EnumFacing facing) {
		int sameCount = 0;
		for (int i = 1; i <= 5; i++) {
			if (world.getBlockState(pos.offset(facing, i)).getBlock() == block) {
				sameCount++;
			}
		}

		return sameCount < 5;
	}

	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
		final Block id = PlanetProgression_Blocks.ADVANCED_LAUCHPAD;

		if (!checkAxis(world, pos, id, EnumFacing.EAST) || !checkAxis(world, pos, id, EnumFacing.WEST) || !checkAxis(world, pos, id, EnumFacing.NORTH) || !checkAxis(world, pos, id, EnumFacing.SOUTH)) {
			return false;
		}

		if (world.getBlockState(pos.offset(EnumFacing.DOWN)).getBlock() == GCBlocks.landingPad && side == EnumFacing.UP) {
			return false;
		} else {
			return this.canPlaceBlockAt(world, pos);
		}
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		switch (meta) {
		case 0:
			return new TileEntitySatelliteLandingPadSingle();
		default:
			return null;
		}
	}

	@Override
	public boolean isSealed(World world, BlockPos pos, EnumFacing direction) {
		return direction == EnumFacing.UP;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public String getShiftDescription(int meta) {
		if (meta == 0)
			return TranslateUtilities.translate(this.getUnlocalizedName() + ".satelliteLandingPad.desc");
		else
			return "";
	}

	@Override
	public boolean showDescription(int meta) {
		return true;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(PAD_TYPE, EnumLandingPadType.byMetadata(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(PAD_TYPE).getMeta();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, PAD_TYPE);
	}

	@Override
	public EnumSortCategoryBlock getCategory(int meta) {
		return EnumSortCategoryBlock.PAD;
	}
}