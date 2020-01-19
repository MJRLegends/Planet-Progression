package com.mjr.planetprogression.client.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.mjr.mjrlegendslib.util.TranslateUtilities;
import com.mjr.planetprogression.PlanetProgression;
import com.mjr.planetprogression.inventory.ContainerSatelliteRocketLauncher;
import com.mjr.planetprogression.network.PacketSimplePP;
import com.mjr.planetprogression.network.PacketSimplePP.EnumSimplePacket;
import com.mjr.planetprogression.tileEntities.TileEntitySatelliteRocketLauncher;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntityAutoRocket.EnumAutoLaunch;
import micdoodle8.mods.galacticraft.core.client.gui.container.GuiContainerGC;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementDropdown;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementDropdown.IDropboxCallback;
import micdoodle8.mods.galacticraft.core.client.gui.element.GuiElementInfoRegion;
import micdoodle8.mods.galacticraft.core.energy.EnergyDisplayHelper;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;

public class GUISatelliteRocketLauncher extends GuiContainerGC implements IDropboxCallback {
	private static final ResourceLocation launchControllerGui = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/gui/launch_controller.png");

	private TileEntitySatelliteRocketLauncher launchController;
	private GuiElementDropdown dropdownTest;
	private GuiButton launchButton;
	private GuiElementInfoRegion electricInfoRegion = new GuiElementInfoRegion(0, 0, 52, 9, null, 0, 0, this);

	public GUISatelliteRocketLauncher(InventoryPlayer playerInventory, TileEntitySatelliteRocketLauncher launchController) {
		super(new ContainerSatelliteRocketLauncher(playerInventory, launchController, FMLClientHandler.instance().getClient().player));
		this.ySize = 209;
		this.launchController = launchController;
	}

	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.clear();
		final int xLeft = (this.width - this.xSize) / 2;
		final int yTop = (this.height - this.ySize) / 2;
		this.dropdownTest = new GuiElementDropdown(0, this, xLeft + 92, yTop + 62, EnumAutoLaunch.INSTANT.getTitle(), EnumAutoLaunch.TIME_10_SECONDS.getTitle(), EnumAutoLaunch.TIME_30_SECONDS.getTitle(), EnumAutoLaunch.TIME_1_MINUTE.getTitle());
		this.launchButton = new GuiButton(1, xLeft - 145 + 158, yTop + 35, 150, 20,
				this.launchController.launchEnabled ? TranslateUtilities.translate("gui.satellite_launcher.auto_disable") : TranslateUtilities.translate("gui.satellite_launcher.auto_enable"));
		this.buttonList.add(this.dropdownTest);
		this.buttonList.add(this.launchButton);
		List<String> batterySlotDesc = new ArrayList<String>();
		batterySlotDesc.add(TranslateUtilities.translate("gui.battery_slot.desc.0"));
		batterySlotDesc.add(TranslateUtilities.translate("gui.battery_slot.desc.1"));
		this.infoRegions.add(new GuiElementInfoRegion(xLeft + 151, yTop + 104, 18, 18, batterySlotDesc, this.width, this.height, this));
		batterySlotDesc = new ArrayList<String>();
		batterySlotDesc.addAll(TranslateUtilities.translateWithSplit("gui.launch_controller.desc.2"));
		this.infoRegions.add(new GuiElementInfoRegion(xLeft + 27, yTop + 20, 13, 13, batterySlotDesc, this.width, this.height, this));
		batterySlotDesc = new ArrayList<String>();
		batterySlotDesc.addAll(TranslateUtilities.translateWithSplit("gui.launch_controller.desc.3"));
		this.infoRegions.add(new GuiElementInfoRegion(xLeft + 52, yTop + 53, 99, 13, batterySlotDesc, this.width, this.height, this));
		this.electricInfoRegion.tooltipStrings = new ArrayList<String>();
		this.electricInfoRegion.xPosition = xLeft + 98;
		this.electricInfoRegion.yPosition = yTop + 113;
		this.electricInfoRegion.parentWidth = this.width;
		this.electricInfoRegion.parentHeight = this.height;
		this.infoRegions.add(this.electricInfoRegion);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.enabled) {
			switch (button.id) {
			case 1:
				if (this.launchController.launchEnabled) {
					this.launchButton.displayString = TranslateUtilities.translate("gui.satellite_launcher.auto_enable.name");
					this.launchController.launchEnabled = false;
					PlanetProgression.packetPipeline.sendToServer(new PacketSimplePP(EnumSimplePacket.S_UPDATE_SATELLITE_LAUNCHER_GUI, GCCoreUtil.getDimensionID(mc.world), new Object[] { 2, this.launchController.getPos(), 0 }));
				} else {
					this.launchButton.displayString = TranslateUtilities.translate("gui.satellite_launcher.auto_disable.name");
					this.launchController.launchEnabled = true;
					PlanetProgression.packetPipeline.sendToServer(new PacketSimplePP(EnumSimplePacket.S_UPDATE_SATELLITE_LAUNCHER_GUI, GCCoreUtil.getDimensionID(mc.world), new Object[] { 2, this.launchController.getPos(), 1 }));
				}
				break;
			default:
				break;
			}
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		String displayString = this.launchController.getName();
		this.fontRenderer.drawString(displayString, this.xSize / 2 - this.fontRenderer.getStringWidth(displayString) / 2, 5, 4210752);
		this.fontRenderer.drawString(TranslateUtilities.translate("container.inventory"), 8, 115, 4210752);
		this.fontRenderer.drawString(TranslateUtilities.translate("gui.satellite_launcher.delay_type.name"), 8, 65, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glPushMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(GUISatelliteRocketLauncher.launchControllerGui);
		final int var5 = (this.width - this.xSize) / 2;
		final int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);

		List<String> electricityDesc = new ArrayList<String>();
		electricityDesc.add(GCCoreUtil.translate("gui.energy_storage.desc.0"));
		EnergyDisplayHelper.getEnergyDisplayTooltip(this.launchController.getEnergyStoredGC(), this.launchController.getMaxEnergyStoredGC(), electricityDesc);
		this.electricInfoRegion.tooltipStrings = electricityDesc;

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		if (this.launchController.getEnergyStoredGC() > 0) {
			int scale = this.launchController.getScaledElecticalLevel(54);
			this.drawTexturedModalRect(var5 + 99, var6 + 114, 176, 0, Math.min(scale, 54), 7);
		}

		GL11.glPopMatrix();
	}

	@Override
	public boolean canBeClickedBy(GuiElementDropdown dropdown, EntityPlayer player) {
		return true;
	}

	@Override
	public void onSelectionChanged(GuiElementDropdown dropdown, int selection) {
		if (dropdown.equals(this.dropdownTest)) {
			this.launchController.launchDropdownSelection = selection;
			PlanetProgression.packetPipeline
					.sendToServer(new PacketSimplePP(EnumSimplePacket.S_UPDATE_SATELLITE_LAUNCHER_GUI, GCCoreUtil.getDimensionID(mc.world), new Object[] { 1, this.launchController.getPos(), this.launchController.launchDropdownSelection }));
		}
	}

	@Override
	public int getInitialSelection(GuiElementDropdown dropdown) {
		if (dropdown.equals(this.dropdownTest)) {
			return this.launchController.launchDropdownSelection;
		}
		return 0;
	}

	@Override
	public void onIntruderInteraction() {

	}
}