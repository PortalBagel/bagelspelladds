package io.redspace.bagels_spell.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/**
 * Marker as an effect that is affected by counterspell
 */
public class MagicMobEffect extends MobEffect {
    public MagicMobEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }
}
