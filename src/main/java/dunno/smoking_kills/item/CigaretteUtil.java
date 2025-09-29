package dunno.smoking_kills.item;

import dunno.smoking_kills.NbtKeys;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.List;
import java.util.Map;

public abstract class CigaretteUtil {
    public static ItemStack createPack(int strength, String flavor, boolean hasFilter, int type) {
        NbtCompound nbt = new NbtCompound();
        nbt.putInt(NbtKeys.CIG_STRENGTH, strength);
        nbt.putString(NbtKeys.CIG_FLAVOR, flavor);
        nbt.putBoolean(NbtKeys.CIG_HAS_FILTER, hasFilter);

        ItemStack pack = new ItemStack(ModItems.CIGARETTE_PACK);
        pack.getOrCreateNbt().put(NbtKeys.PACK_CONTENTS, nbt);
        pack.getOrCreateNbt().putInt(NbtKeys.CUSTOM_MODEL_DATA, type);
        return pack;
    }

    public static ItemStack createCigarette(int strength, String flavor, boolean hasFilter) {
        NbtCompound nbt = new NbtCompound();
        nbt.putInt(NbtKeys.CIG_STRENGTH, strength);
        nbt.putString(NbtKeys.CIG_FLAVOR, flavor);
        nbt.putBoolean(NbtKeys.CIG_HAS_FILTER, hasFilter);

        ItemStack cig = new ItemStack(ModItems.CIGARETTE);
        cig.getOrCreateNbt().put(NbtKeys.CIGARETTE, nbt);
        return cig;
    }

    public static ItemStack createCigarette(int strength, String flavor, boolean hasFilter, int type) {
        NbtCompound nbt = new NbtCompound();
        nbt.putInt(NbtKeys.CIG_STRENGTH, strength);
        nbt.putString(NbtKeys.CIG_FLAVOR, flavor);
        nbt.putBoolean(NbtKeys.CIG_HAS_FILTER, hasFilter);

        ItemStack cig = new ItemStack(ModItems.CIGARETTE);
        cig.getOrCreateNbt().put(NbtKeys.CIGARETTE, nbt);
        cig.getOrCreateNbt().putInt(NbtKeys.CUSTOM_MODEL_DATA, type);
        return cig;
    }

    public static ItemStack createCigaretteFromPack(ItemStack pack) {
        NbtCompound nbt = pack.getOrCreateNbt();

        ItemStack cig = new ItemStack(ModItems.CIGARETTE);
        cig.getOrCreateNbt().put(NbtKeys.CIGARETTE, nbt.getCompound(NbtKeys.PACK_CONTENTS));
        cig.getOrCreateNbt().putInt(NbtKeys.CUSTOM_MODEL_DATA, nbt.getInt(NbtKeys.CUSTOM_MODEL_DATA));
        return cig;
    }

    private static final Map<String, List<StatusEffect>> effects = Map.ofEntries(
            Map.entry("Tobacco", List.of(StatusEffects.HASTE)),
            Map.entry("Vanilla", List.of(StatusEffects.HEALTH_BOOST)),
            Map.entry("Menthol", List.of(StatusEffects.RESISTANCE))
    );

    public static List<StatusEffect> getEffects(ItemStack cigarette) {
        String flavor = cigarette.getOrCreateNbt().getCompound(NbtKeys.CIGARETTE).getString(NbtKeys.CIG_FLAVOR);
        return effects.getOrDefault(flavor, effects.get("Tobacco"));
    }
}
