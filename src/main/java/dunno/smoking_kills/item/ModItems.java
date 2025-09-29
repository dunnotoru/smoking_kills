package dunno.smoking_kills.item;

import dunno.smoking_kills.SmokingKills;
import dunno.smoking_kills.block.ModBlocks;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {
    public static final Item LIGHTER = register(
            new LighterItem(new FabricItemSettings()
                    .group(ItemGroup.TOOLS)
                    .maxCount(1)
                    .maxDamage(250)),
            "lighter"
    );

    public static final Item CIGARETTE_PACK = register(
            new CigarettePackItem(new FabricItemSettings()
                    .group(ItemGroup.TOOLS)
                    .maxCount(1)
                    .maxDamage(20)),
            "cigarette_pack"
    );

    public static final Item CIGARETTE = register(
            new CigaretteItem(new FabricItemSettings()
                    .group(ItemGroup.MISC)
                    .maxCount(1)
                    .maxDamage(100)),
            "cigarette"
    );

    public static final Item CIGARETTE_FILTER = register(
            new Item(new FabricItemSettings()
                    .group(ItemGroup.MISC)),
            "cigarette_filter"
    );

    public static final Item TOBACCO_LEAVES = register(
            new Item(new FabricItemSettings()
                    .group(ItemGroup.MISC)),
            "tobacco_leaves"
    );

    public static final Item DRIED_TOBACCO_LEAVES = register(
            new Item(new FabricItemSettings()
                    .group(ItemGroup.MISC)),
            "dried_tobacco_leaves"
    );

    public static final Item CHOPPED_DRIED_TOBACCO_LEAVES = register(
            new Item(new FabricItemSettings()
                    .group(ItemGroup.MISC)),
            "chopped_dried_tobacco_leaves"
    );

    public static final Item TOBACCO_SEED = register(
            new AliasedBlockItem(ModBlocks.TOBACCO_CROP, new FabricItemSettings()
                    .group(ItemGroup.MISC)),
            "tobacco_seeds"
    );

    public static Item register(Item item, String id) {
        Identifier itemId = Identifier.of(SmokingKills.MOD_ID, id);
        return Registry.register(Registry.ITEM, itemId, item);
    }

    public static void initialize() {

    }
}
