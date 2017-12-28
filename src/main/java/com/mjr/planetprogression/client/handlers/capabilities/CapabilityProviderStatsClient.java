package com.mjr.planetprogression.client.handlers.capabilities;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class CapabilityProviderStatsClient implements ICapabilityProvider
{
    private EntityPlayerSP owner;
    private IStatsClientCapability statsCapability;

    public CapabilityProviderStatsClient(EntityPlayerSP owner)
    {
        this.setOwner(owner);
        this.statsCapability = CapabilityStatsClientHandler.PP_STATS_CLIENT_CAPABILITY.getDefaultInstance();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capability == CapabilityStatsClientHandler.PP_STATS_CLIENT_CAPABILITY;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (CapabilityStatsClientHandler.PP_STATS_CLIENT_CAPABILITY != null && capability == CapabilityStatsClientHandler.PP_STATS_CLIENT_CAPABILITY)
        {
            return CapabilityStatsClientHandler.PP_STATS_CLIENT_CAPABILITY.cast(statsCapability);
        }

        return null;
    }

	public EntityPlayerSP getOwner() {
		return owner;
	}

	public void setOwner(EntityPlayerSP owner) {
		this.owner = owner;
	}
}
