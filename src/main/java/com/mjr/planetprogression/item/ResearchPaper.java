package com.mjr.planetprogression.item;

import java.util.List;

import javax.annotation.Nullable;

import com.mjr.mjrlegendslib.item.BasicItem;
import com.mjr.mjrlegendslib.util.TranslateUtilities;
import com.mjr.planetprogression.Config;
import com.mjr.planetprogression.PlanetProgression;

import micdoodle8.mods.galacticraft.core.util.EnumColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
		String name = getBodyName();
		if (body.contains("eris"))
			name = "planet.Eris";
		else if (body.contains("pluto"))
			name = "planet.Pluto";
		else if (body.contains("ceres"))
			name = "planet.Ceres";
		else if (body.contains("kuiperbelt"))
			name = "planet.kuiperBelt";
		return TranslateUtilities.translate(name);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, @Nullable World worldIn, List<String> list, ITooltipFlag flagIn) {
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

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return I18n.translateToLocal(I18n.translateToLocal("item.research_paper") + ".name").trim();
	}
}
