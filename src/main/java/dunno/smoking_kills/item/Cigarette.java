package dunno.smoking_kills.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class Cigarette extends Item {
    private static final int MAX_USE_TIME = 20;

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
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        int useDuration = this.getMaxUseTime(stack) - remainingUseTicks;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return MAX_USE_TIME;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (world.isClient()) {
            return stack;
        }

        if (user.isPlayer()) {
            smoke((PlayerEntity)user, user.getActiveHand());
            stack.decrement(1);
        }

        return stack;
    }

    private void smoke(PlayerEntity user, Hand hand) {
        //smoking effect WIP
        user.setCurrentHand(hand);
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
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            return TypedActionResult.pass(user.getStackInHand(hand));
        }

        user.setCurrentHand(hand);

        return TypedActionResult.consume(user.getStackInHand(hand));
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
