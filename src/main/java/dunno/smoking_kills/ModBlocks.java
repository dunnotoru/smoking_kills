package dunno.smoking_kills;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {
    public static Block register(Block block,  String id, boolean registerItem) {
        Identifier blockId = Identifier.of(Smoking_kills.MOD_ID, id);

        if (registerItem) {
            BlockItem blockItem = new BlockItem(block, new FabricItemSettings());
            Registry.register(Registry.ITEM, blockId, blockItem);
        }

        return Registry.register(Registry.BLOCK, blockId, block);
    }

    public static void initialize() { }
}
