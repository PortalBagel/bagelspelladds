package io.redspace.bagels_spell.registry;

import io.redspace.bagels_spell.BagelsSpell;
import io.redspace.bagels_spell.spells.*;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class PbSpellRegistry {
    public static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(SpellRegistry.SPELL_REGISTRY_KEY, BagelsSpell.MODID);

    public static void register(IEventBus eventBus) {
        SPELLS.register(eventBus);
    }

    public static RegistryObject<AbstractSpell> registerSpell(AbstractSpell spell) {
        return SPELLS.register(spell.getSpellName(), () -> spell);
    }

    public static final RegistryObject<AbstractSpell> FLOWER_DOMAIN_SPELL = registerSpell(new FlowerDomainSpell());
    public static final RegistryObject<AbstractSpell> FLOWER_STEP_SPELL = registerSpell(new FlowerStepSpell());

    public static final RegistryObject<AbstractSpell> FLOWER_SURROUND_SPELL = registerSpell(new FlowerSurroundSpell());
    public static final RegistryObject<AbstractSpell> FLOWER_SLASH_SPELL = registerSpell(new FlowerSlashSpell());
    public static final RegistryObject<AbstractSpell> FLOWER_EVADE_SPELL = registerSpell(new FlowerEvadeSpell());
    public static final RegistryObject<AbstractSpell> THOUSAND_BLOSSOM_SPELL = registerSpell(new ThousandBlossomSpell());

    public static final RegistryObject<AbstractSpell> TRIPLE_SLASH_SPELL = registerSpell(new TripleStrikeSpell());
    public static final RegistryObject<AbstractSpell> SIX_WORD_MANTRA = registerSpell(new SixWordMantra());
    //doesn't work don't use

}