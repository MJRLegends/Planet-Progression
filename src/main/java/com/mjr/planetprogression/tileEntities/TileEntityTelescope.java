package com.mjr.planetprogression.tileEntities;

import micdoodle8.mods.galacticraft.core.tile.TileEntityAdvanced;

public class TileEntityTelescope extends TileEntityAdvanced {

	@Override
	public double getPacketRange() {
		return 0;
	}

	@Override
	public int getPacketCooldown() {
		return 0;
	}

	@Override
	public boolean isNetworkedTile() {
		return false;
	}
}
