package dunno.smoking_kills;

import dunno.smoking_kills.block.ModBlocks;
import dunno.smoking_kills.data.SmokingData;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;

public class SmokingKillsClient implements ClientModInitializer {
    public static SmokingData playerData = new SmokingData();

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.TOBACCO_CROP);

        ClientPlayNetworking.registerGlobalReceiver(SmokingKills.INITIAL_SYNC, (client, handler, buf, responseSender) -> {
            playerData.cigarettesSmoked = buf.readInt();

            client.execute(() -> {
                assert client.player != null;
                client.player.sendMessage(Text.literal("Initial cigarettes smoked: " + playerData.cigarettesSmoked));
            });
        });
    }
}