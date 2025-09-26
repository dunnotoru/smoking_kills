package dunno.smoking_kills.item;

import dunno.smoking_kills.NbtKeys;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class CigarettePackItem extends Item {
    private final NbtCompound defaultCigaretteNbt;

    public CigarettePackItem(Settings settings, CigarettePackSettings cigarettePackSettings) {
        super(settings);
        defaultCigaretteNbt = new NbtCompound();
        defaultCigaretteNbt.putInt(NbtKeys.CIG_STRENGTH, cigarettePackSettings.strength);
        defaultCigaretteNbt.putString(NbtKeys.CIG_FLAVOR, cigarettePackSettings.flavor);
        defaultCigaretteNbt.putBoolean(NbtKeys.CIG_HAS_FILTER, true);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack packStack = user.getStackInHand(hand);

        if (world.isClient) {
            return TypedActionResult.pass(packStack);
        }

        Hand opposite = hand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND;
        ItemStack oppositeStack = user.getStackInHand(opposite);

        boolean isCrouching = user.getPose() == EntityPose.CROUCHING;
        String flavor = oppositeStack.getOrCreateNbt().getString(NbtKeys.CIG_FLAVOR);
        if (isCrouching
                && oppositeStack.isOf(ModItems.CIGARETTE)
                && flavor.equalsIgnoreCase(defaultCigaretteNbt.getString(NbtKeys.CIG_FLAVOR))
        ) {
            pushCigarette(packStack, oppositeStack);
        }

        if (!isCrouching) {
            pullCigarette(user, packStack);
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
                stack.damage(1, user, x -> { });
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        int strength = defaultCigaretteNbt.getInt(NbtKeys.CIG_STRENGTH);
        boolean hasFilter = defaultCigaretteNbt.getBoolean(NbtKeys.CIG_HAS_FILTER);
        String flavor = defaultCigaretteNbt.getString(NbtKeys.CIG_FLAVOR);

        tooltip.add(Text.translatable("cigarette.smoking_kills.flavor", flavor));
        tooltip.add(Text.translatable("cigarette.smoking_kills.strength", strength));

        if (hasFilter) {
            tooltip.add(Text.translatable("cigarette.smoking_kills.filter"));
        }

        super.appendTooltip(stack, world, tooltip, context);
    }
}
