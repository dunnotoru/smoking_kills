package dunno.smoking_kills.recipes;

import dunno.smoking_kills.SmokingKills;
import dunno.smoking_kills.items.ModItems;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class StuffWithTobaccoRecipe extends SpecialCraftingRecipe {
    private static final Ingredient PAPER;
    private static final Ingredient STRENGTH_MODIFIER;
    private static final Ingredient FILTER;

    private static final int MAX_TOBACCO_AMOUNT = 3;

    static {
        PAPER = Ingredient.ofItems(new ItemConvertible[] { Items.PAPER });
        STRENGTH_MODIFIER = Ingredient.ofItems(new ItemConvertible[] {ModItems.CHOPPED_DRIED_TOBACCO_LEAVES });
        FILTER = Ingredient.ofItems(new ItemConvertible[] { ModItems.CIGARETTE_FILTER });
    }

    public StuffWithTobaccoRecipe(Identifier id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingInventory inventory, World world) {
        if (world.isClient) {
            return false;
        }

        int i = 0;
        boolean hasPaper = false;
        boolean hasFilter = false;

        for (int j = 0; j < inventory.size(); j++) {
            ItemStack itemStack = inventory.getStack(j);
            if (itemStack.isEmpty()) {
                continue;
            }

            if (PAPER.test(itemStack)) {
                SmokingKills.LOGGER.debug("PAPER");
                if (hasPaper) {
                    return false;
                }

                hasPaper = true;
            } else if (STRENGTH_MODIFIER.test(itemStack)) {
                i++;
                if (i > MAX_TOBACCO_AMOUNT) {
                    return false;
                }
            } else if (FILTER.test(itemStack)) {
                if (hasFilter) {
                    return false;
                }

                hasFilter = true;
            }
        }

        return hasPaper && i >= 1;
    }

    @Override
    public ItemStack craft(CraftingInventory inventory) {
        SmokingKills.LOGGER.debug("START CRAFT");
        ItemStack output = new ItemStack(ModItems.ROLLED_UP_CIGARETTE, 1);

        int tobaccoAmount = 0;
        boolean hasFilter = false;
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack itemStack = inventory.getStack(i);
            if (itemStack.isEmpty()) {
                continue;
            }
            if (STRENGTH_MODIFIER.test(itemStack)) {
                tobaccoAmount++;
            } else if (FILTER.test(itemStack)) {
                hasFilter = true;
            }
        }

        output.getOrCreateNbt().putInt("Strength", tobaccoAmount);
        output.getOrCreateNbt().putBoolean("HasFilter", hasFilter);

        return output;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getOutput() {
        return new ItemStack(ModItems.ROLLED_UP_CIGARETTE);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.STUFF_WITH_TOBACCO;
    }
}
