package io.redspace.bagels_spell.registry;

import io.redspace.bagels_spell.BagelsSpell;
import io.redspace.bagels_spell.items.ExampleMagicSword;
import io.redspace.bagels_spell.items.SwordDustless;
import io.redspace.bagels_spell.items.SwordFiregod;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PbItemRegistry {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BagelsSpell.MODID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    public static final RegistryObject<Item> EXAMPLE_MAGIC_SWORD = ITEMS.register("example_magic_sword", () -> new ExampleMagicSword(new SpellDataRegistryHolder[]{new SpellDataRegistryHolder(PbSpellRegistry.FLOWER_SLASH_SPELL, 1)}));
    public static final RegistryObject<Item> SWORD_DUSTLESS = ITEMS.register("sword_dustless",
            () -> new SwordDustless(new SpellDataRegistryHolder[]{
                    new SpellDataRegistryHolder(PbSpellRegistry.FLOWER_STEP_SPELL, 5),
                    new SpellDataRegistryHolder(PbSpellRegistry.FLOWER_DOMAIN_SPELL, 1)
            }));

    public static final RegistryObject<Item> SWORD_FIREGOD = ITEMS.register("sword_firegod",
            () -> new SwordFiregod(new SpellDataRegistryHolder[]{
                    new SpellDataRegistryHolder(SpellRegistry.FLAMING_STRIKE_SPELL, 10),
                    new SpellDataRegistryHolder(SpellRegistry.HEAT_SURGE_SPELL, 10)
            }));;
}


