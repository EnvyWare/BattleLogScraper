package com.envyful.battle.log.scraper.mixin;

import com.envyful.api.discord.DiscordWebHook;
import com.envyful.battle.log.scraper.BattleLogScraper;
import com.envyful.battle.log.scraper.NumberedBattle;
import com.pixelmonmod.pixelmon.battles.controller.BattleController;
import com.pixelmonmod.pixelmon.battles.controller.log.BattleLog;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.*;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Mixin(BattleLog.class)
public abstract class MixinBattleLog {

    @Shadow @Final protected BattleController bc;

    /**
     * @author
     * @reason
     */
    @Inject(
            method = "Lcom/pixelmonmod/pixelmon/battles/controller/log/BattleLog;exportLogFile(Ljava/lang/String;Ljava/lang/Exception;)Ljava/util/concurrent/CompletableFuture;",
            at = @At(value = "RETURN"),
            locals = LocalCapture.CAPTURE_FAILHARD,
            remap = false
    )
    public void exportLogFile(String message, Exception exception, CallbackInfoReturnable<CompletableFuture<File>> cir, File file, StringBuilder sb) {
        cir.getReturnValue().thenApply(file1 ->
         getResponse("https://paste.helpch.at/documents/", sb.toString()).whenComplete((httpEntity, throwable1) -> {
            HttpEntity entity = httpEntity.orElse(null);

            if (entity == null) {
                return;
            }

            try (InputStream instream = entity.getContent();
                 InputStreamReader reader = new InputStreamReader(instream);
                 BufferedReader bufferedReader = new BufferedReader(reader)) {
                String s = bufferedReader.readLine().replace("{\"key\":\"", "").replace("\"}", "");

                for (String webhookURL : BattleLogScraper.getConfig().getWebhookURLs()) {
                    if (webhookURL.equalsIgnoreCase("none")) {
                        continue;
                    }

                    try {
                        DiscordWebHook.builder()
                                .url(webhookURL)
                                .username("Pixelmon BattleLog Scraper")
                                .content("BATTLE LOG [v" + BattleLogScraper.VERSION + "] (" + ++BattleLogScraper.crashCounter + "/" +
                                        ((NumberedBattle) bc).id() + "/" + ((NumberedBattle) bc).total() + "): https://paste.helpch.at/" + s)
                                .build().execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }

    private CompletableFuture<Optional<HttpEntity>> getResponse(String url, String text) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpClient httpclient = HttpClients.createDefault();
                HttpPost post = new HttpPost(url);
                post.setEntity(new StringEntity(text));
                HttpResponse execute = httpclient.execute(post);
                return Optional.of(execute.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
                return Optional.empty();
            }
        });
    }
}
