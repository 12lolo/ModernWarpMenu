package com.github.yukkuritaku.modernwarpmenu.client.gui.components;

import net.minecraft.util.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;


public class TimedMessageButton extends Button {
    private Component originalMessage;
    private long timedMessageExpiryTime;

    public TimedMessageButton(int x, int y, Component message, OnPress onPress, CreateNarration createNarration) {
        super(x, y, 0, 0, message, onPress, createNarration);
    }


    /**
     * Set a temporary label text for this button that will revert to the original label text after the given
     * time has elapsed.
     *
     * @param message the temporary label text
     * @param time the time in milliseconds this label text should be shown for
     */
    public void setTimedMessage(Component message, int time) {
        this.timedMessageExpiryTime = Util.getMillis() + time;
        this.originalMessage = this.getMessage();
        this.setMessage(message);
    }

    @Override
    protected void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.checkTimedMessage();
    }

    private void checkTimedMessage() {
        if (this.timedMessageExpiryTime > 0 && Util.getMillis() > this.timedMessageExpiryTime) {
            this.timedMessageExpiryTime = -1;
            this.setMessage(this.originalMessage);
        }
    }
}
