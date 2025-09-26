package dunno.smoking_kills;

import dunno.smoking_kills.block.ModBlocks;
import dunno.smoking_kills.data.SmokingData;
import dunno.smoking_kills.item.ModItems;
import dunno.smoking_kills.recipes.ModRecipes;
import dunno.smoking_kills.villager.ModVillagers;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            SmokingData playerState = StateSaverAndLoader.getPlayerState(handler.getPlayer());
            PacketByteBuf data = PacketByteBufs.create();
            data.writeInt(playerState.smokePoints);
            server.execute(() -> {
                ServerPlayNetworking.send(handler.getPlayer(), INITIAL_SYNC, data);
            });
        });
    }
}