package com.mjr.planetprogression.command;

import java.util.List;

import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Moon;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import com.mjr.mjrlegendslib.util.PlayerUtilties;
import com.mjr.planetprogression.handlers.capabilities.CapabilityStatsHandler;
import com.mjr.planetprogression.handlers.capabilities.IStatsCapability;
import com.mjr.planetprogression.item.PlanetProgression_Items;
import com.mjr.planetprogression.item.ResearchPaper;
import com.mojang.authlib.GameProfile;

public class CommandUnlockCelestialBody extends CommandBase {

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/" + this.getCommandName() + " <player> <celestialBodyName>";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 3;
	}

	@Override
	public String getCommandName() {
		return "unlockCelestialBody";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		String var3 = null;
		String var4 = null;
		EntityPlayerMP playerBase = null;
		if (args.length > 0) {
			var3 = args[0];
			var4 = args[1];
			GameProfile gameprofile = server.getPlayerProfileCache().getGameProfileForUsername(var3);

			EntityPlayerMP playerToAddFor = PlayerUtilties.getPlayerFromUUID(gameprofile.getId());
			try {
				playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(sender.getName(), true);
				IStatsCapability stats = null;
				if (playerToAddFor != null) {
					stats = playerToAddFor.getCapability(CapabilityStatsHandler.PP_STATS_CAPABILITY, null);
				}

				boolean found = false;
				for (Planet planet : GalaxyRegistry.getRegisteredPlanets().values()) {
					if (var4.equalsIgnoreCase(planet.getLocalizedName())) {
						if (!stats.getUnlockedPlanets().contains(planet)) {
							stats.addUnlockedPlanets(planet);
							playerToAddFor.addChatMessage(new TextComponentString("Research Completed! You have unlocked " + planet.getLocalizedName()));
							playerBase.addChatMessage(new TextComponentString(EnumColor.AQUA + "You have discovered " + planet.getLocalizedName() + "! for: " + gameprofile.getName()));
							found = true;
							break;
						}
					}
				}
				if (found == false) {
					for (Moon moon : GalaxyRegistry.getRegisteredMoons().values()) {
						if (var4.equalsIgnoreCase(moon.getLocalizedName())) {
							if (!stats.getUnlockedPlanets().contains(moon)) {
								stats.addUnlockedPlanets(moon);
								playerToAddFor.addChatMessage(new TextComponentString("Research Completed! You have discovered " + moon.getLocalizedName()));
								playerBase.addChatMessage(new TextComponentString(EnumColor.AQUA + "You have discovered " + moon.getLocalizedName() + "! for: " + gameprofile.getName()));
								break;
							}
						}
					}
				}
			} catch (final Exception var6) {
				throw new CommandException(var6.getMessage(), new Object[0]);
			}
		}
	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
		if (args.length == 1)
			return getListOfStringsMatchingLastWord(args, server.getAllUsernames());
		else if (args.length == 2) {
			String[] array = new String[PlanetProgression_Items.researchPapers.size()];
			int i = 0;
			for (Item paper : PlanetProgression_Items.researchPapers)
				array[i++] = ((ResearchPaper) paper).getPlanet();
			return getListOfStringsMatchingLastWord(args, array);
		} else
			return null;
	}

	@Override
	public boolean isUsernameIndex(String[] par1ArrayOfStr, int par2) {
		return par2 == 0;
	}
}
