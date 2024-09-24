package dunno.smoking_kills.items;

import dunno.smoking_kills.SmokingKills;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class Cigarette extends Item {
    public Cigarette(Settings settings) {
		super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            return TypedActionResult.pass(user.getStackInHand(hand));
        }

        ItemStack heldStack = user.getStackInHand(hand);
        ItemStack lighterStack;
        if (hand.name().equals("MAIN_HAND")) {
            lighterStack = user.getOffHandStack();
        } else {
            lighterStack = user.getMainHandStack();
        }

        Ingredient LIGHTER = Ingredient.ofItems(new ItemConvertible[]{Items.FLINT_AND_STEEL });
        if (!LIGHTER.test(lighterStack)) {
            return TypedActionResult.fail(heldStack);
        }

        lighterStack.damage(1, user, (x) -> {
            SmokingKills.LOGGER.info(x.toString());
        });
        
        int strength = heldStack.getOrCreateNbt().getInt("Strength");

        user.addStatusEffect(
                new StatusEffectInstance(
                        StatusEffects.REGENERATION, 40, Math.max(strength - 1, 0), true, false
                ));
        user.addStatusEffect(
                new StatusEffectInstance(
                        StatusEffects.SPEED, 1200, Math.max(strength - 1, 0), true, false
                ));

        user.getHungerManager().addExhaustion(5);
        user.getItemCooldownManager().set(this, 40);

        heldStack.decrement(1);
        return TypedActionResult.success(heldStack);
    }
}
