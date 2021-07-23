package com.mjr.planetprogression.command;

import java.util.ArrayList;
import java.util.List;

import com.mjr.mjrlegendslib.util.PlayerUtilties;
import com.mjr.planetprogression.data.SatelliteData;
import com.mjr.planetprogression.handlers.capabilities.CapabilityStatsHandler;
import com.mjr.planetprogression.handlers.capabilities.IStatsCapability;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;

public class CommandRemoveAllSatellites extends CommandBase {

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
		return "removeAllSatellites";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		EntityPlayerMP playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(sender.getName(), true);
		if (playerBase == null) {
			return;
		}
		if(args.length == 0)
			args = new String[] {sender.getName()};
		if (args.length == 1) {
			String username = args[0];
			EntityPlayerMP playerToAddFor;
			
			if(args[0].startsWith("@"))
				playerToAddFor = getPlayer(sender, args[0]);
			else
				 playerToAddFor = PlayerUtilties.getPlayerFromUUID(MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(username).getId());
			try {
				IStatsCapability stats = null;
				if (playerToAddFor != null) {
					stats = playerToAddFor.getCapability(CapabilityStatsHandler.PP_STATS_CAPABILITY, null);
				}
				stats.setSatellites(new ArrayList<SatelliteData>());
				playerToAddFor.addChatMessage(new ChatComponentText(EnumColor.RED + "All your satellites have been deleted!"));
				playerBase.addChatMessage(new ChatComponentText(EnumColor.AQUA + "You have remove all of : " + playerToAddFor.getName() + "'s satellites"));
			} catch (final Exception var6) {
				throw new CommandException(var6.getMessage(), new Object[0]);
			}
		}
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		return args.length == 1 ? getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames()) : null;
	}

	@Override
	public boolean isUsernameIndex(String[] par1ArrayOfStr, int par2) {
		return par2 == 0;
	}
}
