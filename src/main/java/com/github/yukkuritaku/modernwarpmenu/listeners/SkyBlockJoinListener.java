package com.github.yukkuritaku.modernwarpmenu.listeners;

import com.github.yukkuritaku.modernwarpmenu.data.settings.SettingsManager;
import com.github.yukkuritaku.modernwarpmenu.state.GameState;
import io.netty.channel.ChannelHandler;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.minecraft.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.scores.Scoreboard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ChannelHandler.Sharable
public class SkyBlockJoinListener {
    private static final String SERVER_BRAND_START = "Hypixel BungeeCord";
    private static final int SCOREBOARD_CHECK_TIME_OUT = 5000;

    private static final Logger LOGGER = LogManager.getLogger();
    private boolean serverBrandChecked;
    private boolean onHypixel;
    private boolean scoreboardChecked;
    private long lastWorldSwitchTime;

    public void registerEvents(){
        ClientLoginConnectionEvents.DISCONNECT.register((handler, client) -> {
            if (this.onHypixel){
                this.serverBrandChecked = false;
                this.onHypixel = false;
                GameState.setOnSkyBlock(false);
                LOGGER.info("Disconnected from Hypixel.");
            }
        });
        ClientWorldEvents.AFTER_CLIENT_WORLD_CHANGE.register((client, level) -> {
            this.lastWorldSwitchTime = Util.getMillis();
            this.serverBrandChecked = false;
            this.scoreboardChecked = false;
            GameState.setOnSkyBlock(false);
        });
        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            if (!this.serverBrandChecked || this.onHypixel && !this.scoreboardChecked){
                LocalPlayer player = Minecraft.getInstance().player;
                if (player == null) return;
                String serverBrand = player.connection.serverBrand();
                if (!this.serverBrandChecked){
                    this.onHypixel = serverBrand != null && serverBrand.startsWith(SERVER_BRAND_START);
                    if (serverBrand != null) {
                        this.serverBrandChecked = true;
                    }else {
                        LOGGER.warn("Server brand is null, retrying...");
                    }
                    if (SettingsManager.get().debug.debugModeEnabled){
                        LOGGER.info("Server Brand: {}", serverBrand);
                    }
                    if (this.onHypixel){
                        LOGGER.info("Player joined Hypixel.");
                    }
                }
                if (this.onHypixel && !this.scoreboardChecked){
                    Scoreboard scoreboard = player.level().getScoreboard();
                    boolean newSkyBlockState = scoreboard.getObjective("SBScoreboard") != null;
                    if (newSkyBlockState != GameState.isOnSkyBlock()) {
                        if (newSkyBlockState) {
                            LOGGER.info("Player joined SkyBlock.");
                        } else {
                            LOGGER.info("Player left SkyBlock.");
                        }
                        GameState.setOnSkyBlock(newSkyBlockState);
                        this.scoreboardChecked = true;
                    }
                    if (Util.getMillis() - this.lastWorldSwitchTime > SCOREBOARD_CHECK_TIME_OUT) {
                        LOGGER.warn("Scoreboard Check Time out.");
                        this.scoreboardChecked = true;
                    }
                }
            }
        });
    }
}
