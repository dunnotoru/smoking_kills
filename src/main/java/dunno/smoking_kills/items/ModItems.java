package dunno.smoking_kills.items;

import dunno.smoking_kills.SmokingKills;
import dunno.smoking_kills.blocks.ModBlocks;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {
    public static final Item ROLLED_UP_CIGARETTE = register(
            new Cigarette(new FabricItemSettings()),
            "rolled_up_cigarette"
    );

    public static final Item TOBACCO_LEAVES = register(
            new Item(new FabricItemSettings()),
            "tobacco_leaves"
    );

    public static final Item DRIED_TOBACCO_LEAVES = register(
            new Item(new FabricItemSettings()),
            "dried_tobacco_leaves"
    );

    public static final Item CHOPPED_DRIED_TOBACCO_LEAVES = register(
            new Item(new FabricItemSettings()),
            "chopped_dried_tobacco_leaves"
    );

    public static final Item TOBACCO_SEED = register(
            new AliasedBlockItem(ModBlocks.TOBACCO_CROP, new FabricItemSettings()),
            "tobacco_seeds"
    );

    public static Item register(Item item, String id) {
        Identifier itemId = Identifier.of(SmokingKills.MOD_ID, id);
        return Registry.register(Registry.ITEM, itemId, item);
    }

    public static void initialize() { }
}
