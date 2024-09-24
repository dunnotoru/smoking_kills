package dunno.smoking_kills.blocks;

import dunno.smoking_kills.SmokingKills;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlocks {
    public static final Block TOBACCO_CROP = register(
            new TobaccoCrop(FabricBlockSettings.of(Material.PLANT)
                    .nonOpaque()
                    .noCollision()
                    .ticksRandomly()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.CROP)),
            "tobacco_crop",
            false
    );

    public static Block register(Block block, String id, boolean registerItem) {
        Identifier blockId = Identifier.of(SmokingKills.MOD_ID, id);

        if (registerItem) {
            BlockItem blockItem = new BlockItem(block, new FabricItemSettings());
            Registry.register(Registry.ITEM, blockId, blockItem);
        }

        return Registry.register(Registry.BLOCK, blockId, block);
    }

    public static void initialize() { }
}
