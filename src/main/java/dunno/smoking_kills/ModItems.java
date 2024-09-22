package dunno.smoking_kills;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {
    public static final Item TOBACCO = register(
            new Tobacco(new FabricItemSettings()),
            "tobacco"
    );

    public static final Item TOBACCO_SEED = register(
            new AliasedBlockItem(ModBlocks.TOBACCO_CROP, new FabricItemSettings()),
            "tobacco_seeds"
    );

    public static Item register(Item item, String id) {
        Identifier itemId = Identifier.of(Smoking_kills.MOD_ID, id);
        return Registry.register(Registry.ITEM, itemId, item);
    }

    public static void initialize() { }
}
