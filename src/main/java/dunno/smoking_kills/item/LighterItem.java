package dunno.smoking_kills.item;

import dunno.smoking_kills.NbtKeys;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class LighterItem extends FlintAndSteelItem {
    public LighterItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (world.isClient()) {
            return TypedActionResult.pass(stack);
        }

        Hand opposite = hand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND;
        ItemStack oppositeStack = user.getStackInHand(opposite);

        if (!oppositeStack.isOf(ModItems.CIGARETTE)) {
            return TypedActionResult.fail(stack);
        }

        if (oppositeStack.getOrCreateNbt().getCompound(NbtKeys.CIGARETTE).getBoolean(NbtKeys.CIG_LIT)) {
            return TypedActionResult.fail(stack);
        }

        user.stopUsingItem();
        oppositeStack.getOrCreateNbt().getCompound(NbtKeys.CIGARETTE).putBoolean(NbtKeys.CIG_LIT, true);
        stack.damage(1, user, x -> {
        });

        return TypedActionResult.success(stack);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity user = context.getPlayer();

        if (user == null) {
            return ActionResult.PASS;
        }

        if (user.isSneaking()) {
            return super.useOnBlock(context);
        }

        if (context.getWorld().isClient()) {
            return ActionResult.PASS;
        }

        user.stopUsingItem();

        return ActionResult.success(false);
    }
}
