package io.redspace.bagels_spell.registry;

import io.redspace.bagels_spell.BagelsSpell;
import io.redspace.bagels_spell.spells.*;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ExampleSpellRegistry {
    public static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(SpellRegistry.SPELL_REGISTRY_KEY, BagelsSpell.MODID);

    public static void register(IEventBus eventBus) {
        SPELLS.register(eventBus);
    }

    public static RegistryObject<AbstractSpell> registerSpell(AbstractSpell spell) {
        return SPELLS.register(spell.getSpellName(), () -> spell);
    }

    public static final RegistryObject<AbstractSpell> NUKE_SPELL = registerSpell(new NukeSpell());
    public static final RegistryObject<AbstractSpell> FLOWER_DOMAIN_SPELL = registerSpell(new FlowerDomainSpell());
    public static final RegistryObject<AbstractSpell> FLOWER_STEP_SPELL = registerSpell(new FlowerStepSpell());
    public static final RegistryObject<AbstractSpell> FROST_STRIKE_SPELL = registerSpell(new FrostStrikeSpell());

    //doesn't work don't use
    public static final RegistryObject<AbstractSpell> FLOWER_STRIKES_SPELL = registerSpell(new FlowerStrikeSpell());
}