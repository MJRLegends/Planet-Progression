package com.mjr.planetprogression.command;

import java.util.List;

import com.mjr.mjrlegendslib.util.PlayerUtilties;
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

import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Moon;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;

public class CommandUnlockAllCelestialBody extends CommandBase {

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
		return "unlockAllCelestialBodies";
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

				for (Planet planet : GalaxyRegistry.getRegisteredPlanets().values()) {
					if (!stats.getUnlockedPlanets().contains(planet)) {
						stats.addUnlockedPlanets(planet);
						playerToAddFor.addChatMessage(new ChatComponentText("Research Completed! You have unlocked " + planet.getLocalizedName()));
						playerBase.addChatMessage(new ChatComponentText(EnumColor.AQUA + "You have discovered " + planet.getLocalizedName() + "! for: " + gameprofile.getName()));
					}
				}
				for (Moon moon : GalaxyRegistry.getRegisteredMoons().values()) {
					if (!stats.getUnlockedPlanets().contains(moon)) {
						stats.addUnlockedPlanets(moon);
						playerToAddFor.addChatMessage(new ChatComponentText("Research Completed! You have discovered " + moon.getLocalizedName()));
						playerBase.addChatMessage(new ChatComponentText(EnumColor.AQUA + "You have discovered " + moon.getLocalizedName() + "! for: " + gameprofile.getName()));
					}
				}

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
