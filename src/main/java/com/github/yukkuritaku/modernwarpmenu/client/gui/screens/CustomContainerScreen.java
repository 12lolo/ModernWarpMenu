package com.github.yukkuritaku.modernwarpmenu.client.gui.screens;

import com.github.yukkuritaku.modernwarpmenu.client.gui.components.CustomContainerButton;
import com.github.yukkuritaku.modernwarpmenu.client.gui.screens.grid.ScaledGrid;
import com.github.yukkuritaku.modernwarpmenu.data.layout.Layout;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;

public abstract class CustomContainerScreen extends ContainerScreen {

    protected Window window;
    protected ScaledGrid grid;
    protected ResourceLocation backgroundTextureLocation;
    /**
     * {@code true} renders the custom UI, {@code false} renders the default chest UI
     */
    protected boolean renderCustomUI;
    /**
     * {@code true} passes interactions to the custom UI, {@code false} passes them to the default chest UI
     */
    protected boolean customUIInteractionEnabled;
    /**
     * The button the user is currently pressing and holding left click on
     */
    protected Button selectedButton;


    public CustomContainerScreen(ChestMenu menu,
                                 Inventory playerInventory,
                                 ResourceLocation backgroundTextureLocation, Component title) {
        this(menu, playerInventory, backgroundTextureLocation, false, false, title);
    }
    /**
     * Creates a new {@code GuiChestMenu} instance with the default chest UI enabled.
     *
     * @param playerInventory the current player's inventory
     * @param menu the inventory of the chest to show
     * @param backgroundTextureLocation location of the image to render as a full-screen background
     */

    public CustomContainerScreen(ChestMenu menu,
                                 Inventory playerInventory,
                                 ResourceLocation backgroundTextureLocation,
                                 boolean renderCustomUI,
                                 boolean customUIInteractionEnabled, Component title) {
        super(menu, playerInventory, title);

        this.backgroundTextureLocation = backgroundTextureLocation;
        this.renderCustomUI = renderCustomUI;
        this.customUIInteractionEnabled = customUIInteractionEnabled;
    }
    protected abstract void renderButtons(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick);
    protected abstract void renderCustomUI(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick);
    protected abstract boolean customUIKeyPressed(KeyEvent event);
    protected abstract boolean customUIMouseClicked(MouseButtonEvent event, boolean isDoubleClick);

    protected void setCustomUIState(boolean renderCustomUI, boolean customUIInteractionEnabled) {
        this.renderCustomUI = renderCustomUI;
        this.customUIInteractionEnabled = customUIInteractionEnabled;
        updateButtonStates();
    }
    /**
     * Updates the enable and visibility states of the buttons in {@link this#children()} when
     * {@code customUIInteractionEnabled} or {@code renderCustomUI} changes
     */
    protected void updateButtonStates() {
        for (GuiEventListener listener : this.children()) {
            if (listener instanceof CustomContainerButton containerButton) {

                containerButton.setActive(this.customUIInteractionEnabled);
                containerButton.setVisible(this.renderCustomUI);
            }
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (this.renderCustomUI) {
            renderCustomUI(guiGraphics, mouseX, mouseY, partialTick);
        }
        else
            super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (this.renderCustomUI) {
            this.renderBg(guiGraphics, partialTick, mouseX, mouseY);
        }else {
            super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {

        if (this.renderCustomUI){
            if (this.backgroundTextureLocation != Layout.EMPTY){
                //TODO Maybe texture resolution include?
                renderMenuBackgroundTexture(guiGraphics, this.backgroundTextureLocation,
                        0, 0,
                        0.0f, 0.0f,
                        guiGraphics.guiWidth(), guiGraphics.guiHeight());
            }else {
                this.renderTransparentBackground(guiGraphics);
            }
        }else {
            super.renderBg(guiGraphics, partialTick, mouseX, mouseY);
        }
    }


    @Override
    public boolean keyPressed(KeyEvent event) {
        if (this.customUIInteractionEnabled){
            if (event.key() == InputConstants.KEY_ESCAPE || Minecraft.getInstance().options.keyInventory.matches(event)){
                // Pass through close window key presses
                return super.keyPressed(event);
            }else {
                return customUIKeyPressed(event);
            }
        }else {
            return super.keyPressed(event);
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean isDoubleClick) {
        if (this.customUIInteractionEnabled)
            return customUIMouseClicked(event, isDoubleClick);
        else
            return super.mouseClicked(event, isDoubleClick);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double mouseX, double mouseY) {
        if (!this.customUIInteractionEnabled) {
            return super.mouseDragged(event, mouseX, mouseY);
        }
        return true;
    }


    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        if (this.customUIInteractionEnabled){
            if (this.selectedButton != null && event.button() == InputConstants.MOUSE_BUTTON_LEFT){
                boolean result = this.selectedButton.mouseReleased(event);
                this.selectedButton = null;
                return result;
            }
        }
        return super.mouseReleased(event);
    }


}
