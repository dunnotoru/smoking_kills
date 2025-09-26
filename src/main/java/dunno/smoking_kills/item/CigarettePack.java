package dunno.smoking_kills.item;

import dunno.smoking_kills.NbtKeys;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class CigarettePack extends Item {
    private final NbtCompound defaultCigaretteNbt;

    public CigarettePack(Settings settings, CigarettePackSettings cigarettePackSettings) {
        super(settings);
        defaultCigaretteNbt = new NbtCompound();
        defaultCigaretteNbt.putInt(NbtKeys.CIG_STRENGTH, cigarettePackSettings.strength);
        defaultCigaretteNbt.putString(NbtKeys.CIG_FLAVOR, cigarettePackSettings.flavor);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack packStack = user.getStackInHand(hand);

        if (world.isClient) {
            return TypedActionResult.pass(packStack);
        }

        Hand opposite = hand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND;
        ItemStack oppositeStack = user.getStackInHand(opposite);

        if (oppositeStack.isEmpty()) {
            pullCigarette(user, packStack);
            return TypedActionResult.success(packStack, false);
        }

        boolean isCrouching = user.getPose() == EntityPose.CROUCHING;
        String flavor = oppositeStack.getOrCreateNbt().getString(NbtKeys.CIG_FLAVOR);
        if (isCrouching
                && oppositeStack.isOf(ModItems.CIGARETTE)
                && flavor.equalsIgnoreCase(defaultCigaretteNbt.getString(NbtKeys.CIG_FLAVOR))
        ) {
            pushCigarette(packStack, oppositeStack);
        }

        return TypedActionResult.success(packStack, false);
    }

    private void pushCigarette(ItemStack packStack, ItemStack cigStack) {
        if (cigStack.isDamaged() || packStack.getDamage() == 0) {
            return;
        }

        cigStack.decrement(1);
        packStack.setDamage(packStack.getDamage() - 1);
    }

    private void pullCigarette(PlayerEntity user, ItemStack stack) {
        if (stack.getMaxDamage() - stack.getDamage() > 0) {
            ItemStack cig = new ItemStack(ModItems.CIGARETTE);
            cig.setNbt(defaultCigaretteNbt);
            if (user.giveItemStack(cig)) {
                stack.damage(1, user, this::onBreak);
            }
        }
    }

    private void onBreak(PlayerEntity user) {

    }
}
