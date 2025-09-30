package dunno.smoking_kills;

import dunno.smoking_kills.block.ModBlocks;
import dunno.smoking_kills.data.SmokingData;
import dunno.smoking_kills.item.ModItems;
import dunno.smoking_kills.misc.ModEffects;
import dunno.smoking_kills.recipes.ModRecipes;
import dunno.smoking_kills.villager.ModVillagers;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.WorldEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SmokingKills implements ModInitializer {
    public static final String MOD_ID = "smoking_kills";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Identifier INITIAL_SYNC = new Identifier(MOD_ID, "initial_sync");

    @Override
    public void onInitialize() {
        ModItems.initialize();
        LOGGER.info("Items initialized");
        ModBlocks.initialize();
        LOGGER.info("Blocks initialized");
        ModRecipes.initialize();
        LOGGER.info("Recipes initialized");
        ModVillagers.initialize();
        LOGGER.info("Goofy Ahh Villagers initialized");
        ModEffects.initialize();
//        LOGGER.info("Effects initialized");

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            SmokingData playerState = StateSaverAndLoader.getPlayerState(handler.getPlayer());
            PacketByteBuf data = PacketByteBufs.create();
            data.writeInt(playerState.smokePoints);
            server.execute(() -> {
                ServerPlayNetworking.send(handler.getPlayer(), INITIAL_SYNC, data);
            });
        });


        ServerTickEvents.END_SERVER_TICK.register((x) -> {
            long time = x.getTicks();

            List<ServerPlayerEntity> players = x.getPlayerManager().getPlayerList();

            for (ServerPlayerEntity player : players) {
                SmokingData state = StateSaverAndLoader.getPlayerState(player);

                int modifier = state.smokePoints / 10;
                if (modifier <= 0) {
                    return;
                }

                LOGGER.info("pts:{}; times: {}-{}; mod: {} {}", state.smokePoints, time, state.lastSmokingTime, time - state.lastSmokingTime, 24000 / modifier);

                long delay = time - state.lastSmokingTime;
                long cravingTime = 24000 / modifier;
                if (delay > cravingTime) {
                    int duration = 120 * state.smokePoints / 10;
                    player.addStatusEffect(new StatusEffectInstance(ModEffects.CRAVING, duration, 0, true, false));
                    state.lastSmokingTime = time + duration;
                }
            }
        });
    }
}