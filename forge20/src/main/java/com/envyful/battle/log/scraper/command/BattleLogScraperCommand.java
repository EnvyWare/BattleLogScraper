package com.envyful.battle.log.scraper.command;

import com.envyful.api.command.annotate.Command;
import com.envyful.api.command.annotate.executor.CommandProcessor;
import com.envyful.api.command.annotate.executor.Sender;
import com.envyful.api.command.annotate.permission.Permissible;
import com.envyful.api.forge.chat.UtilChatColour;
import com.envyful.battle.log.scraper.BattleLogScraper;
import net.minecraft.commands.CommandSource;

@Command(
        value = {
                "battlelogscraper",
                "battlelogscraperreload"
        }
)
@Permissible("com.envyful.scraper.reload")
public class BattleLogScraperCommand {

    @CommandProcessor
    public void onCommand(@Sender CommandSource sender, String[] args) {
        BattleLogScraper.reloadConfig();
        sender.sendSystemMessage(UtilChatColour.colour("Reloaded configs"));
    }
}
