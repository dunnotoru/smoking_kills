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
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public class CigaretteItem extends Item {
    private final static NbtCompound defaultNbt = new NbtCompound();

    public CigaretteItem(Settings settings) {
        super(settings);
        defaultNbt.putBoolean(NbtKeys.CIG_HAS_FILTER, false);
        defaultNbt.putInt(NbtKeys.CIG_STRENGTH, 1);
        defaultNbt.putString(NbtKeys.CIG_FLAVOR, "Tobacco");
        defaultNbt.putBoolean(NbtKeys.CIG_LIT, false);
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = super.getDefaultStack();
        stack.getOrCreateNbt().put(NbtKeys.CIGARETTE, defaultNbt);
        return stack;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 40;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (world.isClient()) {
            return TypedActionResult.pass(stack);
        }

        NbtCompound nbt = stack.getOrCreateNbt().getCompound(NbtKeys.CIGARETTE);
        if (!nbt.getBoolean(NbtKeys.CIG_LIT)) {
            return TypedActionResult.fail(stack);
        }

        user.setCurrentHand(hand);

        return TypedActionResult.consume(stack);
    }

    private void smoke(PlayerEntity user, ItemStack stack) {
        CigaretteInternals.smoke(user, stack);
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
        NbtCompound data = stack.getOrCreateNbt().getCompound(NbtKeys.CIGARETTE);
        int strength = data.getInt(NbtKeys.CIG_STRENGTH);
        boolean hasFilter = data.getBoolean(NbtKeys.CIG_HAS_FILTER);
        String flavor = data.getString(NbtKeys.CIG_FLAVOR);
        boolean isLit = data.getBoolean(NbtKeys.CIG_LIT);

        tooltip.add(Text.translatable("cigarette.smoking_kills.flavor", flavor));
        tooltip.add(Text.translatable("cigarette.smoking_kills.strength", strength));

        if (hasFilter) {
            tooltip.add(Text.translatable("cigarette.smoking_kills.filter"));
        }

        if (isLit) {
            tooltip.add(Text.translatable("cigarette.smoking_kills.lit"));
        }

        super.appendTooltip(stack, world, tooltip, context);
    }
}

abstract class CigaretteInternals {
    @FunctionalInterface
    public interface SmokeDelegate {
        void smoke(PlayerEntity user, NbtCompound nbt);
    }

    private static final Map<String, SmokeDelegate> flavorsToEffects = Map.ofEntries(
            Map.entry("Tobacco", CigaretteInternals::tobacco),
            Map.entry("Vanilla", CigaretteInternals::vanilla),
            Map.entry("Menthol", CigaretteInternals::menthol)
    );

    public static void smoke(PlayerEntity user, ItemStack stack) {
        String flavor = stack.getOrCreateNbt().getString(NbtKeys.CIG_FLAVOR);

        SmokeDelegate delegate = flavorsToEffects.getOrDefault(flavor, CigaretteInternals::tobacco);
        delegate.smoke(user, stack.getOrCreateNbt().getCompound(NbtKeys.CIGARETTE));

        user.getItemCooldownManager().set(ModItems.CIGARETTE, 40);
        user.getItemCooldownManager().set(ModItems.ROLLED_UP_CIGARETTE, 40);
    }

    private static void tobacco(PlayerEntity user, NbtCompound nbt) {
        SmokingKills.LOGGER.info("tobacco");
        int strength = nbt.getInt(NbtKeys.CIG_STRENGTH);
        boolean hasFilter = nbt.getBoolean(NbtKeys.CIG_HAS_FILTER);

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

    private static void vanilla(PlayerEntity user, NbtCompound nbt) {
        SmokingKills.LOGGER.info("vanilla");
        int strength = nbt.getInt(NbtKeys.CIG_STRENGTH);
        boolean hasFilter = nbt.getBoolean(NbtKeys.CIG_HAS_FILTER);

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

    private static void menthol(PlayerEntity user, NbtCompound nbt) {
        SmokingKills.LOGGER.info("menthol");
        int strength = nbt.getInt(NbtKeys.CIG_STRENGTH);
        boolean hasFilter = nbt.getBoolean(NbtKeys.CIG_HAS_FILTER);

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
