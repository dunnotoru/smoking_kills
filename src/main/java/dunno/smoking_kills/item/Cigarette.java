package dunno.smoking_kills.item;

import net.fabricmc.fabric.impl.object.builder.TradeOfferInternals;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.TradeOffers;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class Cigarette extends Item {
    private static final Map<Integer, MutableText> tobaccoAmountToStrength = Map.ofEntries(
            Map.entry(0, Text.translatable("tobacco_strength.smoking_kills.mild")),
            Map.entry(1, Text.translatable("tobacco_strength.smoking_kills.mild")),
            Map.entry(2, Text.translatable("tobacco_strength.smoking_kills.medium")),
            Map.entry(3, Text.translatable("tobacco_strength.smoking_kills.strong"))
    );

    public Cigarette(Settings settings) {
		super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            return TypedActionResult.pass(user.getStackInHand(hand));
        }

        ItemStack heldStack = user.getStackInHand(hand);
        int tobaccoAmount = heldStack.getOrCreateNbt().getInt("TobaccoAmount");
        boolean hasFilter = heldStack.getOrCreateNbt().getBoolean("HasFilter");

        user.addStatusEffect(
                new StatusEffectInstance(
                        StatusEffects.REGENERATION, 40, Math.max(tobaccoAmount - 1, 0), true, false
                ));
        user.addStatusEffect(
                new StatusEffectInstance(
                        StatusEffects.SPEED, 1200, Math.max(tobaccoAmount - 1, 0), true, false
                ));

        user.getHungerManager().addExhaustion(2.5f * tobaccoAmount);

        user.getItemCooldownManager().set(ModItems.CIGARETTE, 40);
        user.getItemCooldownManager().set(ModItems.ROLLED_UP_CIGARETTE, 40);

        heldStack.decrement(1);
        return TypedActionResult.consume(heldStack);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        int tobaccoAmount = stack.getOrCreateNbt().getInt("TobaccoAmount");
        boolean hasFilter = stack.getOrCreateNbt().getBoolean("HasFilter");

        tooltip.add(tobaccoAmountToStrength.getOrDefault(tobaccoAmount, Text.literal("unknown")));
        if (hasFilter) {
            tooltip.add(Text.translatable("itemTooltip.smoking_kills.filter"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
