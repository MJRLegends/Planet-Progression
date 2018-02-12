package com.mjr.planetprogression.item;

import java.util.List;

import javax.annotation.Nullable;

import micdoodle8.mods.galacticraft.core.util.EnumColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mjr.mjrlegendslib.item.BasicItem;
import com.mjr.mjrlegendslib.util.TranslateUtilities;
import com.mjr.planetprogression.Config;
import com.mjr.planetprogression.PlanetProgression;

public class ResearchPaper extends BasicItem {

	private String planet;

	public ResearchPaper(String name, int number) {
		super("research_paper_" + number);
		this.planet = name.toLowerCase();
		this.setCreativeTab(PlanetProgression.tab);
	}

	public String getPlanet() {
		return planet;
	}

	public void setPlanet(String planet) {
		this.planet = planet;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, @Nullable World worldIn, List<String> list, ITooltipFlag flagIn) {
		list.add(EnumColor.YELLOW + planet.substring(0, 1).toUpperCase() + planet.substring(1));

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
	}
}
