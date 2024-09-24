package dunno.smoking_kills.villager;

import com.google.common.collect.ImmutableSet;
import dunno.smoking_kills.SmokingKills;
import dunno.smoking_kills.item.ModItems;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

public class ModVillagers {
    public static final RegistryKey<PointOfInterestType> TOBACCO_POI_KEY = of("tobacco_poi");
    public static final PointOfInterestType TOBACCO_POI = registerPOI("tobacco_poi", Blocks.GLASS);
    public static final VillagerProfession TOBACCO_MASTER = registerProfession("tobacco_master", TOBACCO_POI_KEY);

    private static VillagerProfession registerProfession(String id, RegistryKey<PointOfInterestType> type) {
        return Registry.register(
                Registry.VILLAGER_PROFESSION,
                new Identifier(SmokingKills.MOD_ID, id),
                new VillagerProfession(
                        id,
                        entry -> entry.matchesKey(type),
                        entry -> entry.matchesKey(type),
                        ImmutableSet.of(),
                        ImmutableSet.of(),
                        SoundEvents.ENTITY_CREEPER_DEATH

            ));
    }

    private static PointOfInterestType registerPOI(String id, Block block) {
        return PointOfInterestHelper.register(
                new Identifier(SmokingKills.MOD_ID, id),
                1,
                1,
                block
        );
    }

    private static RegistryKey<PointOfInterestType> of(String id) {
        return RegistryKey.of(
                Registry.POINT_OF_INTEREST_TYPE.getKey(),
                new Identifier(SmokingKills.MOD_ID, id)
        );
    }

    public static void registerTrades() {
        TradeOfferHelper.registerVillagerOffers(
                TOBACCO_MASTER,
                1,
                factories -> {
                    factories.add((entity, random) -> new TradeOffer(
                            new ItemStack(Items.EMERALD, 1),
                            new ItemStack(ModItems.ROLLED_UP_CIGARETTE),
                            6,
                            2,
                            0.02f
                    ));
                });
    }

    public static void initialize() {
        registerTrades();
    }
}
