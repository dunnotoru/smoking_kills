package dunno.smoking_kills.misc;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class CravingEffect extends StatusEffect {

    protected CravingEffect(StatusEffectCategory statusEffectCategory) {
        super(statusEffectCategory, 0x488796);
        this.addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "5315d1ec-b781-4f37-8402-24129f60a209", -0.15F, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, "23fe6184-e036-426b-a348-5528f12b7ab4", -0.15F, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
