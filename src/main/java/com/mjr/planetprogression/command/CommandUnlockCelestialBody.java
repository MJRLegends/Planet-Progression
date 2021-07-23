package com.mjr.planetprogression.command;

import java.util.List;

import com.mjr.mjrlegendslib.util.PlayerUtilties;
import com.mjr.planetprogression.handlers.capabilities.CapabilityStatsHandler;
import com.mjr.planetprogression.handlers.capabilities.IStatsCapability;
import com.mjr.planetprogression.item.PlanetProgression_Items;
import com.mjr.planetprogression.item.ResearchPaper;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Moon;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;

public class CommandUnlockCelestialBody extends CommandBase {

	@Override
	public String getUsage(ICommandSender var1) {
		return "/" + this.getName() + " <player> <celestialBodyName>";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 3;
	}

	@Override
	public String getName() {
		return "unlockCelestialBody";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		EntityPlayerMP playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(sender.getName(), true);
		if (playerBase == null) {
			return;
		}
		if (args.length == 1) {
			String username = args[0];
			String body = args[1];
			EntityPlayerMP playerToAddFor;
			
			if(args[0].startsWith("@"))
				playerToAddFor = getPlayer(server, sender, args[0]);
			else
				 playerToAddFor = PlayerUtilties.getPlayerFromUUID(server.getPlayerProfileCache().getGameProfileForUsername(username).getId());
			try {
				IStatsCapability stats = null;
				if (playerToAddFor != null) {
					stats = playerToAddFor.getCapability(CapabilityStatsHandler.PP_STATS_CAPABILITY, null);
				}

				boolean found = false;
				for (Planet planet : GalaxyRegistry.getRegisteredPlanets().values()) {
					if (body.equalsIgnoreCase(planet.getUnlocalizedName().substring(planet.getUnlocalizedName().indexOf('.') + 1))) {
						if (!stats.getUnlockedPlanets().contains(planet)) {
							stats.addUnlockedPlanets(planet);
							playerToAddFor.sendMessage(new TextComponentString("Research Completed! You have unlocked " + planet.getLocalizedName()));
							playerBase.sendMessage(new TextComponentString(EnumColor.AQUA + "You have discovered " + planet.getLocalizedName() + "! for: " + playerToAddFor.getName()));
							found = true;
							break;
						}
					}
				}
				if (found == false) {
					for (Moon moon : GalaxyRegistry.getRegisteredMoons().values()) {
						if (body.equalsIgnoreCase(moon.getUnlocalizedName().substring(moon.getUnlocalizedName().indexOf('.') + 1))) {
							if (!stats.getUnlockedPlanets().contains(moon)) {
								stats.addUnlockedPlanets(moon);
								playerToAddFor.sendMessage(new TextComponentString("Research Completed! You have discovered " + moon.getLocalizedName()));
								playerBase.sendMessage(new TextComponentString(EnumColor.AQUA + "You have discovered " + moon.getLocalizedName() + "! for: " + playerToAddFor.getName()));
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
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
		if (args.length == 1)
			return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
		else if (args.length == 2) {
			String[] array = new String[PlanetProgression_Items.researchPapers.size()];
			int i = 0;
			for (Item paper : PlanetProgression_Items.researchPapers)
				array[i++] = ((ResearchPaper) paper).getBodyName().substring(((ResearchPaper) paper).getBodyName().indexOf('.') + 1);
			return getListOfStringsMatchingLastWord(args, array);
		} else
			return null;
	}

	@Override
	public boolean isUsernameIndex(String[] par1ArrayOfStr, int par2) {
		return par2 == 0;
	}
}
