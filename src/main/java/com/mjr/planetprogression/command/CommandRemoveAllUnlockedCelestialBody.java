package com.mjr.planetprogression.command;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import com.mjr.mjrlegendslib.util.PlayerUtilties;
import com.mjr.planetprogression.handlers.capabilities.CapabilityStatsHandler;
import com.mjr.planetprogression.handlers.capabilities.IStatsCapability;
import com.mojang.authlib.GameProfile;

public class CommandRemoveAllUnlockedCelestialBody extends CommandBase {

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/" + this.getCommandName() + " <player>";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 3;
	}

	@Override
	public String getCommandName() {
		return "removeAllUnlockedCelestialBody";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		String var3 = null;
		EntityPlayerMP playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(sender.getName(), true);
		if (playerBase == null) {
            return;
        }
		if (args.length > 0) {
			var3 = args[0];
			GameProfile gameprofile = server.getPlayerProfileCache().getGameProfileForUsername(var3);

			EntityPlayerMP playerToAddFor = PlayerUtilties.getPlayerFromUUID(gameprofile.getId());
			try {
				IStatsCapability stats = null;
				if (playerToAddFor != null) {
					stats = playerToAddFor.getCapability(CapabilityStatsHandler.PP_STATS_CAPABILITY, null);
				}
				stats.setUnlockedPlanets(new ArrayList<CelestialBody>());
				playerToAddFor.addChatMessage(new TextComponentString("All your all Planets & Moons have been removed from your discovered list!"));
				playerBase.addChatMessage(new TextComponentString(EnumColor.AQUA + "You have removeed all Planets & Moons! from the discovered list for: " + gameprofile.getName()));

			} catch (final Exception var6) {
				throw new CommandException(var6.getMessage(), new Object[0]);
			}
		}
	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
		return args.length == 1 ? getListOfStringsMatchingLastWord(args, server.getAllUsernames()) : null;
	}

	@Override
	public boolean isUsernameIndex(String[] par1ArrayOfStr, int par2) {
		return par2 == 0;
	}
}
