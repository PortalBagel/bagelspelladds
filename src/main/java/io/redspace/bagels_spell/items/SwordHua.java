package io.redspace.bagels_spell.items;

import io.redspace.bagels_spell.registry.PbAttributeRegistry;
import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;

import java.util.Map;
import java.util.UUID;

public class SwordHua extends MagicSwordItem {
    public SwordHua(SpellDataRegistryHolder[] holder) {
        super(Tiers.NETHERITE, 6, -2.2f, holder,
                Map.of(
                        PbAttributeRegistry.BLOSSOM_MAGIC_POWER.get(), new AttributeModifier(UUID.fromString("667ad88f-901d-4691-b2a2-3664e42026d3"), "Weapon Modifier", 0.5, AttributeModifier.Operation.MULTIPLY_BASE)  ,
                        PbAttributeRegistry.BLOSSOM_MAGIC_RESIST.get(), new AttributeModifier(UUID.fromString("667ad88f-901d-4691-b2a2-3664e42026d3"), "Weapon Modifier", 0.5, AttributeModifier.Operation.MULTIPLY_BASE)),
                (new Properties()).rarity(Rarity.EPIC));
    }
}