package dunno.smoking_kills.misc;

import dunno.smoking_kills.SmokingKills;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEffects {
    public static final StatusEffect CRAVING;

    static {
        CRAVING = register("smoke_craving", (new CravingEffect(StatusEffectCategory.HARMFUL)));
    }

    private static StatusEffect register(String id, StatusEffect entry) {
        Identifier effectId = Identifier.of(SmokingKills.MOD_ID, id);
        return Registry.register(Registry.STATUS_EFFECT, effectId, entry);
    }

    public static void initialize() {

    }
}
