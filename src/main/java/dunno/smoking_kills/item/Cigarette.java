package dunno.smoking_kills.item;

import dunno.smoking_kills.NbtKeys;
import dunno.smoking_kills.SmokingKills;
import dunno.smoking_kills.StateSaverAndLoader;
import dunno.smoking_kills.data.SmokingData;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public class Cigarette extends Item {
    private static final int MAX_USE_TIME = 20;

    private static final Map<Integer, MutableText> tobaccoAmountToStrength = Map.ofEntries(
            Map.entry(0, Text.translatable("tobacco_strength.smoking_kills.empty")),
            Map.entry(1, Text.translatable("tobacco_strength.smoking_kills.mild")),
            Map.entry(2, Text.translatable("tobacco_strength.smoking_kills.medium")),
            Map.entry(3, Text.translatable("tobacco_strength.smoking_kills.strong"))
    );

    private static final CigaretteInternals internals = new CigaretteInternals();

    public Cigarette(Settings settings) {
        super(settings);
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
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            return TypedActionResult.pass(user.getStackInHand(hand));
        }

        user.setCurrentHand(hand);

        return TypedActionResult.consume(user.getStackInHand(hand));
    }

    private void smoke(PlayerEntity user, ItemStack stack) {
        internals.smoke(user, stack);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (world.isClient()) {
            float distance = 0.5f;
            double yaw = Math.toRadians(user.getHeadYaw());
            world.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    user.getX() - Math.sin(yaw) * distance, user.getEyeY(), user.getZ() + Math.cos(yaw) * distance,
                    -Math.sin(yaw) * 0.01, 0.01, Math.cos(yaw) * 0.01);
            return stack;
        }

        if (!user.isPlayer()) {
            return stack;
        }


        this.smoke((PlayerEntity) user, stack);
        stack.decrement(1);

        return stack;
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        int strength = stack.getOrCreateNbt().getInt(NbtKeys.CIG_STRENGTH);
        boolean hasFilter = stack.getOrCreateNbt().getBoolean(NbtKeys.CIG_HAS_FILTER);
        String flavor = stack.getOrCreateNbt().getString(NbtKeys.CIG_FLAVOR);

        tooltip.add(Text.translatable("cigarette.smoking_kills.flavor", flavor));
        tooltip.add(Text.translatable("cigarette.smoking_kills.strength", strength));

        if (hasFilter) {
            tooltip.add(Text.translatable("cigarette.smoking_kills.filter"));
        }

        super.appendTooltip(stack, world, tooltip, context);
    }
}

class CigaretteInternals {
    @FunctionalInterface
    public interface SmokeDelegate {
        void smoke(PlayerEntity user, ItemStack stack);
    }

    private final Map<String, SmokeDelegate> flavorsToEffects = Map.ofEntries(
            Map.entry("Tobacco", this::tobacco),
            Map.entry("Vanilla", this::vanilla),
            Map.entry("Menthol", this::menthol)
    );

    public void smoke(PlayerEntity user, ItemStack stack) {
        String flavor = stack.getOrCreateNbt().getString(NbtKeys.CIG_FLAVOR);

        SmokeDelegate delegate = flavorsToEffects.getOrDefault(flavor, this::tobacco);
        delegate.smoke(user, stack);

        user.getItemCooldownManager().set(ModItems.CIGARETTE, 40);
        user.getItemCooldownManager().set(ModItems.ROLLED_UP_CIGARETTE, 40);
    }

    private void tobacco(PlayerEntity user, ItemStack stack) {
        SmokingKills.LOGGER.info("tobacco");
        int strength = stack.getOrCreateNbt().getInt(NbtKeys.CIG_STRENGTH);
        boolean hasFilter = stack.getOrCreateNbt().getBoolean(NbtKeys.CIG_HAS_FILTER);

        SmokingData state = StateSaverAndLoader.getPlayerState(user);
        int effectPower = strength;
        if (hasFilter && effectPower > 1) {
            effectPower -= 1;
        }

        state.smokePoints += effectPower;

        user.addStatusEffect(
                new StatusEffectInstance(
                        StatusEffects.REGENERATION, 40, Math.max(effectPower, 0), true, false
                ));
        user.addStatusEffect(
                new StatusEffectInstance(
                        StatusEffects.HASTE, 1200, Math.max(effectPower, 0), true, false
                ));

        user.getHungerManager().addExhaustion(2.5f * strength);
    }

    private void vanilla(PlayerEntity user, ItemStack stack) {
        SmokingKills.LOGGER.info("vanilla");
        int strength = stack.getOrCreateNbt().getInt(NbtKeys.CIG_STRENGTH);
        boolean hasFilter = stack.getOrCreateNbt().getBoolean(NbtKeys.CIG_HAS_FILTER);

        SmokingData state = StateSaverAndLoader.getPlayerState(user);
        int effectPower = strength;
        if (hasFilter && effectPower > 1) {
            effectPower -= 1;
        }

        state.smokePoints += effectPower;

        user.addStatusEffect(
                new StatusEffectInstance(
                        StatusEffects.REGENERATION, 40, Math.max(effectPower, 0), true, false
                ));
        user.addStatusEffect(
                new StatusEffectInstance(
                        StatusEffects.HEALTH_BOOST, 1200, Math.max(effectPower, 0), true, false
                ));

        user.getHungerManager().addExhaustion(2.5f * strength);
    }

    private void menthol(PlayerEntity user, ItemStack stack) {
        SmokingKills.LOGGER.info("menthol");
        int strength = stack.getOrCreateNbt().getInt(NbtKeys.CIG_STRENGTH);
        boolean hasFilter = stack.getOrCreateNbt().getBoolean(NbtKeys.CIG_HAS_FILTER);

        SmokingData state = StateSaverAndLoader.getPlayerState(user);
        int effectPower = strength;
        if (hasFilter && effectPower > 1) {
            effectPower -= 1;
        }

        state.smokePoints += effectPower;

        user.addStatusEffect(
                new StatusEffectInstance(
                        StatusEffects.REGENERATION, 40, Math.max(effectPower, 0), true, false
                ));
        user.addStatusEffect(
                new StatusEffectInstance(
                        StatusEffects.RESISTANCE, 1200, Math.max(effectPower, 0), true, false
                ));

        user.getHungerManager().addExhaustion(2.5f * strength);
    }
}
