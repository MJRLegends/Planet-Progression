package com.mjr.planetprogression.item;

import java.util.List;

import com.mjr.mjrlegendslib.item.BasicItem;
import com.mjr.mjrlegendslib.util.TranslateUtilities;
import com.mjr.planetprogression.Config;
import com.mjr.planetprogression.PlanetProgression;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import micdoodle8.mods.galacticraft.core.util.EnumColor;

public class ResearchPaper extends BasicItem {

	private String body;

	public ResearchPaper(String name, int number) {
		super("research_paper_" + number);
		this.body = name.toLowerCase();
		this.setCreativeTab(PlanetProgression.tab);
	}

	public String getBodyName() {
		return body;
	}

	public String getRealBodyName() {

		return TranslateUtilities.translate(convertBodyNameForLocalization(getBodyName()));
	}

	public static String convertBodyNameForLocalization(String input) {
		if (input.contains("eris"))
			input = "planet.Eris";
		else if (input.contains("pluto"))
			input = "planet.Pluto";
		else if (input.contains("ceres"))
			input = "planet.Ceres";
		else if (input.contains("kuiperbelt"))
			input = "planet.kuiperBelt";
		return input;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> list, boolean par4) {
		if (player.worldObj.isRemote) {
			String name = getRealBodyName();
			list.add(EnumColor.YELLOW + name.substring(0, 1).toUpperCase() + name.substring(1));

			if (Config.researchMode == 1) {
				if (Config.generateResearchPaperInLoot && Config.generateResearchPaperInStructure) {
					list.add(EnumColor.AQUA + TranslateUtilities.translate("research.paper.loot.desc"));
					list.add(EnumColor.AQUA + TranslateUtilities.translate("research.paper.woldgen.desc"));
				} else if (Config.generateResearchPaperInLoot)
					list.add(EnumColor.AQUA + TranslateUtilities.translate("research.paper.loot.desc"));
				else if (Config.generateResearchPaperInStructure)
					list.add(EnumColor.AQUA + TranslateUtilities.translate("research.paper.woldgen.desc"));
			} else
				list.add(EnumColor.AQUA + TranslateUtilities.translate("research.paper.satellite.controller.desc"));
			list.add(EnumColor.BRIGHT_GREEN + TranslateUtilities.translate("research.paper.use.desc"));
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return I18n.translateToLocal(I18n.translateToLocal("item.research_paper") + ".name").trim();
	}
}
