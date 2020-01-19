package com.mjr.planetprogression.command;

import java.util.List;
import java.util.UUID;

import com.mjr.mjrlegendslib.util.PlayerUtilties;
import com.mjr.planetprogression.data.SatelliteData;
import com.mjr.planetprogression.handlers.capabilities.CapabilityStatsHandler;
import com.mjr.planetprogression.handlers.capabilities.IStatsCapability;
import com.mojang.authlib.GameProfile;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;

public class CommandAddSatellite extends CommandBase {

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
		return "addNewSatellite";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		String var3 = null;
		EntityPlayerMP playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(sender.getName(), true);
		if (playerBase == null) {
			return;
		}
		if (args.length > 0) {
			var3 = args[0];
			GameProfile gameprofile = MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(var3);

			EntityPlayerMP playerToAddFor = PlayerUtilties.getPlayerFromUUID(gameprofile.getId());
			try {
				IStatsCapability stats = null;
				if (playerToAddFor != null) {
					stats = playerToAddFor.getCapability(CapabilityStatsHandler.PP_STATS_CAPABILITY, null);
				}
				String id = UUID.randomUUID().toString();
				stats.addSatellites(new SatelliteData(0, id, 0, null));
				playerToAddFor.addChatMessage(new ChatComponentText(EnumColor.RED + "Satellite: " + id + " has been launched in to space!"));
				playerBase.addChatMessage(new ChatComponentText(EnumColor.AQUA + "You have launched a satellite in to space! for: " + gameprofile.getName() + " with id: " + id));
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
