package dunno.smoking_kills.item;

import dunno.smoking_kills.SmokingKills;
import dunno.smoking_kills.block.ModBlocks;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.*;
import net.minecraft.potion.Potions;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {
    public static final Item LIGHTER = register(
            "lighter",
            new LighterItem(new FabricItemSettings()
                    .group(ItemGroup.TOOLS)
                    .maxCount(1)
                    .maxDamage(250))
    );

    public static final Item CIGARETTE_PACK = register(
            "cigarette_pack",
            new CigarettePackItem(new FabricItemSettings()
                    .group(ItemGroup.TOOLS)
                    .maxCount(1)
                    .maxDamage(20))
    );

    public static final Item CIGARETTE = register(
            "cigarette",
            new CigaretteItem(new FabricItemSettings()
                    .group(ItemGroup.MISC)
                    .maxCount(1)
                    .maxDamage(100))
    );

    public static final Item CIGARETTE_FILTER = register(
            "cigarette_filter",
            new Item(new FabricItemSettings()
                    .group(ItemGroup.MISC))
    );

    public static final Item TOBACCO_LEAVES = register(
            "tobacco_leaves",
            new Item(new FabricItemSettings()
                    .group(ItemGroup.MISC))
    );

    public static final Item DRIED_TOBACCO_LEAVES = register(
            "dried_tobacco_leaves",
            new Item(new FabricItemSettings()
                    .group(ItemGroup.MISC))
    );

    public static final Item CHOPPED_DRIED_TOBACCO_LEAVES = register(
            "chopped_dried_tobacco_leaves",
            new Item(new FabricItemSettings()
                    .group(ItemGroup.MISC))
    );

    public static final Item TOBACCO_SEED = register(
            "tobacco_seeds",
            new AliasedBlockItem(ModBlocks.TOBACCO_CROP, new FabricItemSettings()
                    .group(ItemGroup.MISC))
    );

    public static Item register(String id, Item item) {
        Identifier itemId = Identifier.of(SmokingKills.MOD_ID, id);
        return Registry.register(Registry.ITEM, itemId, item);
    }

    public static void initialize() {

    }
}
