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
        int strength = heldStack.getOrCreateNbt().getInt("Strength");
        boolean hasFilter = heldStack.getOrCreateNbt().getBoolean("HasFilter");

        user.addStatusEffect(
                new StatusEffectInstance(
                        StatusEffects.REGENERATION, 40, Math.max(strength - 1, 0), true, false
                ));
        user.addStatusEffect(
                new StatusEffectInstance(
                        StatusEffects.SPEED, 1200, Math.max(strength - 1, 0), true, false
                ));

        user.getHungerManager().addExhaustion(2.5f * strength);
        user.getItemCooldownManager().set(this, 40);
        heldStack.decrement(1);

        return TypedActionResult.consume(heldStack);
    }
}
