package com.envyful.battle.log.scraper.mixin;

import com.envyful.battle.log.scraper.BattleLogScraper;
import com.envyful.battle.log.scraper.NumberedBattle;
import com.pixelmonmod.pixelmon.api.battles.BattleEndCause;
import com.pixelmonmod.pixelmon.api.battles.BattleResults;
import com.pixelmonmod.pixelmon.battles.controller.BattleController;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mixin(BattleController.class)
public abstract class MixinBattleController implements NumberedBattle {

    @Shadow(remap = false) public List<BattleParticipant> participants;

    @Shadow(remap = false) public abstract ArrayList<PixelmonWrapper> getTeamPokemon(PixelmonEntity pokemon);

    @Shadow(remap = false) public abstract ArrayList<PixelmonWrapper> getActivePokemon();

    @Shadow(remap = false) public abstract ArrayList<PixelmonWrapper> getTeamPokemon(PixelmonWrapper pokemon);

    @Shadow(remap = false) private int doLaterTicks;
    @Shadow(remap = false) private Runnable doLaterAction;

    @Shadow(remap = false) public abstract boolean isPvP();

    @Shadow(remap = false) public abstract void endBattle();

    @Shadow(remap = false) public abstract HashMap<BattleParticipant, BattleResults> endBattle(BattleEndCause cause);

    @Shadow(remap = false) public abstract void sendToAll(String string, Object... data);

    private final int id = ++BattleLogScraper.battleCounter;

    @Override
    public int id() {
        return id;
    }

    @Override
    public int total() {
        return BattleLogScraper.battleCounter;
    }

}
