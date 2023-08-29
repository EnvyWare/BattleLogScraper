package com.envyful.battle.log.scraper.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.Permissible;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.battle.log.scraper.BattleLogScraper;
import net.minecraft.command.ICommandSource;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;

@Command(
        value = "battlelogscraper",
        aliases = {
                "battlelogscraperreload"
        }
)
@Permissible("com.envyful.scraper.reload")
public class BattleLogScraperCommand {

    @CommandProcessor
    public void onCommand(@Sender ICommandSource sender, String[] args) {
        BattleLogScraper.reloadConfig();
        sender.sendMessage(new StringTextComponent("Reloaded configs"), Util.NIL_UUID);
    }
}
