package io.redspace.bagels_spell.registry;

import io.redspace.bagels_spell.BagelsSpell;
import io.redspace.bagels_spell.items.ExampleMagicSword;
import io.redspace.bagels_spell.items.SwordDustless;
import io.redspace.bagels_spell.items.SwordFiregod;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.item.UpgradeOrbItem;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
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

    public static final RegistryObject<Item> BLOSSOM_RUNE = ITEMS.register("blossom_rune", () -> new Item(ItemPropertiesHelper.material()));
    //public static final RegistryObject<Item> BLOSSOM_UPGRADE_ORB = ITEMS.register("blossom_upgrade_orb",
      //      () -> new UpgradeOrbItem(CSUpgradeTypes.ABYSSAL_SPELL_POWER, ItemPropertiesHelper.material().rarity(Rarity.UNCOMMON)));



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


