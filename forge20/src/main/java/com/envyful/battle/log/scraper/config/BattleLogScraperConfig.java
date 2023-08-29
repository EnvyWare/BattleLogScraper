package com.envyful.battle.log.scraper.config;

import com.envyful.api.config.data.ConfigPath;
import com.envyful.api.config.yaml.AbstractYamlConfig;
import com.google.common.collect.Lists;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

@ConfigSerializable
@ConfigPath("config/BattleLogScraper/config.yml")
public class BattleLogScraperConfig extends AbstractYamlConfig {

    private List<String> webhookURLs = Lists.newArrayList("none");

    public BattleLogScraperConfig() {
        super();
    }

    public List<String> getWebhookURLs() {
        return this.webhookURLs;
    }
}
