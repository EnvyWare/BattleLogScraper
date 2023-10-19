package com.envyful.battle.log.scraper;

import com.envyful.api.config.yaml.YamlConfigFactory;
import com.envyful.api.discord.DiscordWebHook;
import com.envyful.api.forge.command.ForgeCommandFactory;
import com.envyful.battle.log.scraper.command.BattleLogScraperCommand;
import com.envyful.battle.log.scraper.config.BattleLogScraperConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;

import java.io.IOException;

@Mod("battlelogscraper")
public class BattleLogScraper {

    public static int crashCounter = 0;
    public static int battleCounter = 0;

    private static BattleLogScraper instance;

    public static final String VERSION = "1.6.0";

    private BattleLogScraperConfig config;

    protected final ForgeCommandFactory commandFactory = new ForgeCommandFactory();

    public BattleLogScraper() {
        instance = this;
        MinecraftForge.EVENT_BUS.register(this);
        reloadConfig();
    }

    public static void reloadConfig() {
        try {
            instance.config = YamlConfigFactory.getInstance(BattleLogScraperConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BattleLogScraperConfig getConfig() {
        return instance.config;
    }

    @SubscribeEvent
    public void onRegisterCommand(RegisterCommandsEvent event) {
        this.commandFactory.registerCommand(event.getDispatcher(), new BattleLogScraperCommand());
    }

    @SubscribeEvent
    public void onServerShutdown(FMLServerStoppingEvent event) {
        for (String webhookURL : this.config.getWebhookURLs()) {
            try {
                DiscordWebHook.builder()
                        .url(webhookURL)
                        .username("Pixelmon BattleLog Scraper")
                        .content("BATTLE LOG [v" + BattleLogScraper.VERSION + "] Server shutting down with stats: Crashes: " + crashCounter + ". Total battles: " +
                                 battleCounter)
                        .build().execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
