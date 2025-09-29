package dunno.smoking_kills.item;

import dunno.smoking_kills.NbtKeys;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.apache.http.annotation.Obsolete;

import java.util.List;

public class CigarettePackItem extends Item {
    public CigarettePackItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (this.isIn(group)) {
            stacks.add(CigaretteUtil.createPack(2, "Tobacco", true, 0));
            stacks.add(CigaretteUtil.createPack(2, "Tobacco", true, 1));
            stacks.add(CigaretteUtil.createPack(2, "Vanilla", true, 2));
            stacks.add(CigaretteUtil.createPack(2, "Menthol", true, 3));
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack packStack = user.getStackInHand(hand);

        if (world.isClient) {
            return TypedActionResult.pass(packStack);
        }

        pullCigarette(user, packStack);

        return TypedActionResult.success(packStack, false);
    }

    @Obsolete
    private void pushCigarette(ItemStack packStack, ItemStack cigStack) {
        if (packStack.getDamage() == 0) {
            return;
        }

        cigStack.decrement(1);
        packStack.setDamage(packStack.getDamage() - 1);
    }

    private void pullCigarette(PlayerEntity user, ItemStack stack) {
        if (stack.getMaxDamage() - stack.getDamage() > 0) {

            ItemStack cig = CigaretteUtil.createCigaretteFromPack(stack);

            if (user.giveItemStack(cig)) {
                stack.damage(1, user, x -> {

                });
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbt = stack.getOrCreateNbt().getCompound(NbtKeys.PACK_CONTENTS);

        int strength = nbt.getInt(NbtKeys.CIG_STRENGTH);
        boolean hasFilter = nbt.getBoolean(NbtKeys.CIG_HAS_FILTER);
        String flavor = nbt.getString(NbtKeys.CIG_FLAVOR);

        tooltip.add(Text.translatable("cigarette.smoking_kills.flavor", flavor));
        tooltip.add(Text.translatable("cigarette.smoking_kills.strength", strength));

        if (hasFilter) {
            tooltip.add(Text.translatable("cigarette.smoking_kills.filter"));
        }

        super.appendTooltip(stack, world, tooltip, context);
    }
}
