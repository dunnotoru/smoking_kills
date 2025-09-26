package dunno.smoking_kills;

import dunno.smoking_kills.block.ModBlocks;
import dunno.smoking_kills.data.SmokingData;
import dunno.smoking_kills.item.ModItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SmokingKillsClient implements ClientModInitializer {
    public static SmokingData playerData = new SmokingData();

    public static Identifier FLAVOR = new Identifier(SmokingKills.MOD_ID, "flavor");
    public static Identifier ROLLED_UP_STRENGTH = new Identifier(SmokingKills.MOD_ID, "rolled_up_strength");

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.TOBACCO_CROP);

        ClientPlayNetworking.registerGlobalReceiver(SmokingKills.INITIAL_SYNC, (client, handler, buf, responseSender) -> {
            playerData.smokePoints = buf.readInt();

            client.execute(() -> {
                assert client.player != null;
                client.player.sendMessage(Text.literal("Initial cigarettes smoked: " + playerData.smokePoints));
            });
        });

        ModelPredicateProviderRegistry.register(FLAVOR, (itemStack, clientWorld, livingEntity, i) -> {
            if (!itemStack.isOf(ModItems.CIGARETTE)) {
                return 0f;
            }

            String flavor = itemStack.getOrCreateNbt().getString(NbtKeys.CIG_FLAVOR);
            return switch (flavor) {
                case "Tobacco" -> 0f;
                case "Vanilla" -> 0.1f;
                case "Menthol" -> 0.2f;
                default -> 0.0f;
            };
        });

        ModelPredicateProviderRegistry.register(ROLLED_UP_STRENGTH, (itemStack, clientWorld, livingEntity, i) -> {
            if (!itemStack.isOf(ModItems.ROLLED_UP_CIGARETTE)) {
                return 0f;
            }

            int strength = itemStack.getOrCreateNbt().getInt(NbtKeys.CIG_STRENGTH);

            if (strength <= 0) {
                return 0f;
            }

            if (strength > 3) {
                return 0.4f;
            }

            return switch (strength) {
                case 2 -> 0.2f;
                case 3 -> 0.3f;
                default -> 0.0f;
            };
        });
    }
}