package dunno.smoking_kills.villager;

import com.google.common.collect.ImmutableSet;
import dunno.smoking_kills.SmokingKills;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.village.*;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.List;

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
        var professionOffersMap = ModTradeOffers.PROFESSION_TO_LEVELED_TRADE;
        for (VillagerProfession villagerProfession : professionOffersMap.keySet()) {
            var levelOffersMap = professionOffersMap.get(villagerProfession);
            for (Integer level : levelOffersMap.keySet()) {
                TradeOfferHelper.registerVillagerOffers(
                        villagerProfession,
                        level,
                        factories -> {
                            factories.addAll(List.of(levelOffersMap.get(level)));
                        }
                );
            }
        }
        
    }

    public static void initialize() {
        registerTrades();
    }
}
