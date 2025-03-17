package io.redspace.bagels_spell.effect;

import io.redspace.bagels_spell.registry.ExampleMobEffectRegistry;
import io.redspace.bagels_spell.registry.ExampleSpellRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.bagels_spell.entity.FlowerStrikeEntity;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.level.Level;



import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class FlowerStrikeEffect extends MagicMobEffect {
    public FlowerStrikeEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @SubscribeEvent
    public static void createEcho(LivingHurtEvent event) {
        var damageSource = event.getSource();
        if (damageSource.getEntity() instanceof LivingEntity attacker && (damageSource.getDirectEntity() == attacker || damageSource.getDirectEntity() instanceof AbstractArrow) && !(damageSource instanceof SpellDamageSource)) {
            var effect = attacker.getEffect(ExampleMobEffectRegistry.FLOWER_STRIKES.get());
            if (effect != null) {
                var percent = getDamageModifier(effect.getAmplifier(), attacker);
                FlowerStrikeEntity echo = new FlowerStrikeEntity(attacker.level, attacker, event.getAmount() * percent, 3f);
                echo.setPos(event.getEntity().getBoundingBox().getCenter().subtract(0, echo.getBbHeight() * .5f, 0));
                attacker.level.addFreshEntity(echo);
            }
        }
    }



    public static float getDamageModifier(int effectAmplifier, @Nullable LivingEntity caster) {
        var power = caster == null ? 1 : ExampleSpellRegistry.FLOWER_STRIKES_SPELL.get().getEntityPowerMultiplier(caster);
        return (((effectAmplifier - 4) * power) + 5) * .1f; // create echo of 10% damage per level of the effect
    }
}