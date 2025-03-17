package io.redspace.bagels_spell.registry;

import io.redspace.bagels_spell.BagelsSpell;
import io.redspace.bagels_spell.effect.FlowerStrikeEffect;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.effect.*;
import io.redspace.ironsspellbooks.effect.guiding_bolt.GuidingBoltEffect;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import top.theillusivec4.caelus.api.CaelusApi;


public class ExampleMobEffectRegistry {
    public static final DeferredRegister<MobEffect> MOB_EFFECT_DEFERRED_REGISTER = DeferredRegister.create(Registries.MOB_EFFECT, BagelsSpell.MODID);

    public static void register(IEventBus eventBus) {
        MOB_EFFECT_DEFERRED_REGISTER.register(eventBus);
    }


    public static final RegistryObject<MobEffect> OAKSKIN = MOB_EFFECT_DEFERRED_REGISTER.register("oakskin", () -> new OakskinEffect(MobEffectCategory.BENEFICIAL, 0xffef95) );

    public static final RegistryObject<MobEffect> FLOWER_STRIKES = MOB_EFFECT_DEFERRED_REGISTER.register("flower_strikes", () -> new FlowerStrikeEffect(MobEffectCategory.BENEFICIAL, 0xfedbff));

}
