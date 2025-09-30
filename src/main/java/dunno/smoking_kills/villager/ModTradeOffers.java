package dunno.smoking_kills.villager;

import com.google.common.collect.ImmutableMap;
import dunno.smoking_kills.item.CigaretteUtil;
import dunno.smoking_kills.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ModTradeOffers {
    public static final Map<VillagerProfession, Map<Integer, TradeOffers.Factory[]>> PROFESSION_TO_LEVELED_TRADE;

    static {
        PROFESSION_TO_LEVELED_TRADE = new HashMap<>();
        PROFESSION_TO_LEVELED_TRADE.put(
                ModVillagers.TOBACCO_MASTER,
                Map.ofEntries(
                        Map.entry(1, new TradeOffers.Factory[]{
                                new SellCigaretteFactory(1, 16, 2, 0.05F),
                                new SellCigaretteFactory(2, 16, 2, 0.1F),
                        }),
                        Map.entry(2, new TradeOffers.Factory[]{
                                new SellCigaretteFactory(1, 16, 2, 0.4F),
                                new SellCigaretteFactory(2, 16, 2, 0.1F),
                        }),
                        Map.entry(3, new TradeOffers.Factory[]{
                                new SellCigaretteFactory(10, 16, 10, 0.4F),
                                new SellCigaretteFactory(10, 16, 10, 0.1F),
                        })
                )
        );
    }

    private static class SellCigaretteFactory implements TradeOffers.Factory {
        private final int basePrice;
        private final int maxUses;
        private final int experience;
        private final float multiplier;

        public SellCigaretteFactory(int basePrice, int maxUses, int experience, float multiplier) {
            this.basePrice = basePrice;
            this.maxUses = maxUses;
            this.experience = experience;
            this.multiplier = multiplier;
        }

        @Override
        public @Nullable TradeOffer create(Entity entity, Random random) {
            int strength = Math.max(random.nextBetween(-1, 1) + basePrice, 1);
            List<String> flavors = List.of("Tobacco", "Vanilla", "Menthol");
            String flavor = flavors.get(random.nextBetween(0, flavors.size() - 1));
            int type = random.nextBetween(1, 3);

            ItemStack pack = CigaretteUtil.createPack(strength, flavor, true, type);

            return new TradeOffer(new ItemStack(Items.EMERALD, basePrice), pack, maxUses, experience, multiplier);
        }
    }
}
