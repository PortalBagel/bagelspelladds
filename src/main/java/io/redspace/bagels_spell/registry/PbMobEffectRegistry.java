package io.redspace.bagels_spell.registry;

import io.redspace.bagels_spell.BagelsSpell;
//import io.redspace.bagels_spell.effect.FireInfuseEffect;
import io.redspace.ironsspellbooks.effect.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;


public class PbMobEffectRegistry {
    public static final DeferredRegister<MobEffect> MOB_EFFECT_DEFERRED_REGISTER = DeferredRegister.create(Registries.MOB_EFFECT, BagelsSpell.MODID);

    public static void register(IEventBus eventBus) {
        MOB_EFFECT_DEFERRED_REGISTER.register(eventBus);
    }
    //public static final RegistryObject<MobEffect> FIREINFUSED = MOB_EFFECT_DEFERRED_REGISTER.register("fire_infused", () -> new FireInfuseEffect(MobEffectCategory.BENEFICIAL, 3311322).addAttributeModifier(Attributes.ATTACK_DAMAGE, "87733c95-909c-4fc3-9780-e35a89565666", ChargeEffect.ATTACK_DAMAGE_PER_LEVEL, AttributeModifier.Operation.MULTIPLY_TOTAL).addAttributeModifier(Attributes.MOVEMENT_SPEED, "87733c95-909c-4fc3-9780-e35a89565666", ChargeEffect.SPEED_PER_LEVEL, AttributeModifier.Operation.MULTIPLY_TOTAL).addAttributeModifier(AttributeRegistry.SPELL_POWER.get(), "87733c95-909c-4fc3-9780-e35a89565666", ChargeEffect.SPELL_POWER_PER_LEVEL, AttributeModifier.Operation.MULTIPLY_TOTAL));


    public static final RegistryObject<MobEffect> OAKSKIN = MOB_EFFECT_DEFERRED_REGISTER.register("oakskin", () -> new OakskinEffect(MobEffectCategory.BENEFICIAL, 0xffef95) );


}
