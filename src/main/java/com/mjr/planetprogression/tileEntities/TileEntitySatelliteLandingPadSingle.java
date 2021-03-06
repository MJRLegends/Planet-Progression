package com.mjr.planetprogression.tileEntities;

import java.util.ArrayList;

import com.mjr.planetprogression.blocks.PlanetProgression_Blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntitySatelliteLandingPadSingle extends TileEntity implements ITickable {
	private int corner = 0;

	@Override
	public void update() {
		if (!this.worldObj.isRemote && this.corner == 0) {
			final ArrayList<TileEntity> attachedLaunchPads = new ArrayList<TileEntity>();

			for (int x = this.getPos().getX() - 2; x < this.getPos().getX() + 3; x++) {
				for (int z = this.getPos().getZ() - 2; z < this.getPos().getZ() + 3; z++) {
					final TileEntity tile = this.worldObj.getTileEntity(new BlockPos(x, this.getPos().getY(), z));

					if (tile instanceof TileEntitySatelliteLandingPadSingle && !tile.isInvalid() && ((TileEntitySatelliteLandingPadSingle) tile).corner == 0) {
						attachedLaunchPads.add(tile);
					}
				}
			}

			if (attachedLaunchPads.size() == 25) {
				for (final TileEntity tile : attachedLaunchPads) {
					this.worldObj.markTileEntityForRemoval(tile);
					((TileEntitySatelliteLandingPadSingle) tile).corner = 1;
				}

				this.getPos().south(1);
				this.getPos().east(1);

				this.worldObj.setBlockState(this.getPos(), PlanetProgression_Blocks.ADVANCED_LAUCHPAD_FULL.getDefaultState(), 2);
			}
		}
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return oldState.getBlock() != newSate.getBlock();
	}
}