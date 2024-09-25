package dunno.smoking_kills.villager;

import com.google.common.collect.ImmutableMap;
import dunno.smoking_kills.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ModTradeOffers {
    public ModTradeOffers() { }

    public static final Map<VillagerProfession, Map<Integer, TradeOffers.Factory[]>> PROFESSION_TO_LEVELED_TRADE;

    static {
        PROFESSION_TO_LEVELED_TRADE = new HashMap<>() {{
           put(ModVillagers.TOBACCO_MASTER, ImmutableMap.of(
                   1, new TradeOffers.Factory[] {
                           new SellCigaretteFactory(1, 16, 2, 0.05F),
                           new SellCigaretteFactory(2, 16, 2, 0.1F),
                   },
                   2, new TradeOffers.Factory[] {
                           new SellCigaretteFactory(20, 16, 2, 0.2F),
                   }

           ));
        }};
    }

    private static class SellCigaretteFactory implements TradeOffers.Factory {
        private final ItemStack output;
        private final int basePrice;
        private final int maxUses;
        private final int experience;
        private final float multiplier;

        public SellCigaretteFactory(int basePrice, int maxUses, int experience, float multiplier) {
            this.output = new ItemStack(ModItems.CIGARETTE);
            this.basePrice = basePrice;
            this.maxUses = maxUses;
            this.experience = experience;
            this.multiplier = multiplier;
        }

        @Override
        public @Nullable TradeOffer create(Entity entity, Random random) {
            int tobaccoAmount = Math.max(random.nextBetween(-1,1) + basePrice, 1);
            tobaccoAmount = Math.min(tobaccoAmount, 3);
            output.getOrCreateNbt().putInt("TobaccoAmount", tobaccoAmount);
            output.getOrCreateNbt().putBoolean("HasFilter", true);

            return new TradeOffer(new ItemStack(Items.EMERALD, basePrice), output, maxUses, experience, multiplier);
        }
    }
}
