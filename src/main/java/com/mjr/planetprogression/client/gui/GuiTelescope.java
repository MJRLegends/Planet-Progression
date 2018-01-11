package com.mjr.planetprogression.client.gui;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiContainerGC;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.energy.EnergyDisplayHelper;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;

import org.lwjgl.opengl.GL11;

import com.mjr.planetprogression.Constants;
import com.mjr.planetprogression.PlanetProgression;
import com.mjr.planetprogression.inventory.ContainerTelescope;
import com.mjr.planetprogression.network.PacketSimpleEP;
import com.mjr.planetprogression.tileEntities.TileEntityTelescope;

public class GuiTelescope extends GuiContainerGC {
	private static final ResourceLocation gui = new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/telescope.png");

	private TileEntityTelescope telescope;

	private GuiButton enableButton;
	private GuiButton leftButton;
	private GuiButton rightButton;

	private GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion(0, 0, 52, 9, null, 0, 0, this);

	public GuiTelescope(InventoryPlayer playerInventory, TileEntityTelescope telescope) {
		super(new ContainerTelescope(playerInventory, telescope, FMLClientHandler.instance().getClient().thePlayer));
		this.xSize = 400;
		this.ySize = 300;
		this.telescope = telescope;
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		if (this.telescope.disableCooldown > 0) {
			this.enableButton.enabled = false;
		} else {
			this.enableButton.enabled = true;
		}

		this.enableButton.displayString = this.telescope.getDisabled(0) ? GCCoreUtil.translate("gui.button.enable.name") : GCCoreUtil.translate("gui.button.disable.name");

		super.drawScreen(par1, par2, par3);
	}

	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.clear();
		final int var5 = (this.width - this.xSize) / 2;
		final int var6 = (this.height - this.ySize) / 2;
		this.enableButton = new GuiButton(0, var5 + 70 + 124 - 72, var6 + 16, 152, 20, GCCoreUtil.translate("gui.button.enable.name"));
		this.leftButton = new GuiButton(1, var5 + 5, var6 + 5, 15, 20, "<");
		this.rightButton = new GuiButton(2, var5 + 70 + 310, var6 + 5, 15, 20, ">");

		this.buttonList.add(this.enableButton);
		this.buttonList.add(this.leftButton);
		this.buttonList.add(this.rightButton);
		this.electricInfoRegion.tooltipStrings = new ArrayList<String>();
		this.electricInfoRegion.xPosition = (this.width - this.xSize) / 2 + 98;
		this.electricInfoRegion.yPosition = (this.height - this.ySize) / 2 + 113;
		this.electricInfoRegion.parentWidth = this.width;
		this.electricInfoRegion.parentHeight = this.height;
		this.infoRegions.add(this.electricInfoRegion);
		List<String> batterySlotDesc = new ArrayList<String>();
		batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.0"));
		batterySlotDesc.add(GCCoreUtil.translate("gui.battery_slot.desc.1"));
		this.infoRegions.add(new GuiElementInfoRegion((this.width - this.xSize) / 2 + 151, (this.height - this.ySize) / 2 + 104, 18, 18, batterySlotDesc, this.width, this.height, this));
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.enabled) {
			switch (par1GuiButton.id) {
			case 0:
				GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_DISABLEABLE_BUTTON, GCCoreUtil.getDimensionID(mc.theWorld), new Object[] { this.telescope.getPos(), 0 }));
				break;
			case 1:
				PlanetProgression.packetPipeline.sendToServer(new PacketSimpleEP(com.mjr.planetprogression.network.PacketSimpleEP.EnumSimplePacket.S_UPDATE_ROTATION, GCCoreUtil.getDimensionID(mc.theWorld),
						new Object[] { this.telescope.getPos(), 0.0F }));
				break;
			case 2:
				PlanetProgression.packetPipeline.sendToServer(new PacketSimpleEP(com.mjr.planetprogression.network.PacketSimpleEP.EnumSimplePacket.S_UPDATE_ROTATION, GCCoreUtil.getDimensionID(mc.theWorld),
						new Object[] { this.telescope.getPos(), 1.0F }));
				break;
			default:
				break;
			}
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		String displayString = this.telescope.getName();
		this.fontRendererObj.drawString(displayString, this.xSize / 2 - this.fontRendererObj.getStringWidth(displayString) / 2, 5, 4210752);

		this.fontRendererObj.drawString(GCCoreUtil.translate("container.inventory"), 8, 205, 4210752);
		this.fontRendererObj.drawString("Progress: " + (telescope.processTicks / 2) + " %", 165, 40, 4210752);

	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(GuiTelescope.gui);
		final int var5 = (this.width - this.xSize) / 2;
		final int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);

		List<String> electricityDesc = new ArrayList<String>();
		electricityDesc.add(GCCoreUtil.translate("gui.energy_storage.desc.0"));
		EnergyDisplayHelper.getEnergyDisplayTooltip(this.telescope.getEnergyStoredGC(), this.telescope.getMaxEnergyStoredGC(), electricityDesc);
		this.electricInfoRegion.tooltipStrings = electricityDesc;

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		if (this.telescope.getEnergyStoredGC() > 0) {
			int scale = this.telescope.getScaledElecticalLevel(54);
			this.drawTexturedModalRect(var5 + 99, var6 + 114, 176, 0, Math.min(scale, 54), 7);
		}

		GL11.glPopMatrix();
	}
}