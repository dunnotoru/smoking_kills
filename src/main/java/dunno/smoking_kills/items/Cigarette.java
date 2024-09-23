package dunno.smoking_kills.items;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class Cigarette extends Item {
    public Cigarette(Settings settings) {
		super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        user.addStatusEffect(
                new StatusEffectInstance(
                        StatusEffects.REGENERATION, 40, 2, true, false
                ));
        user.addStatusEffect(
                new StatusEffectInstance(
                        StatusEffects.HASTE, 1200, 2, true, false
                ));

        user.getHungerManager().addExhaustion(5);
        user.getItemCooldownManager().set(this, 40);

        world.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE,
                user.getX(),
                user.getEyeY(),
                user.getZ(),
                0,
                0.05,
                0
        );

        ItemStack heldStack = user.getStackInHand(hand);
        heldStack.decrement(1);
        return TypedActionResult.success(heldStack);
    }
}
