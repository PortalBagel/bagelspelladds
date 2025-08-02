package io.redspace.bagels_spell.registry;

import io.redspace.bagels_spell.BagelsSpell;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

public class PbDamageTypes {

    public static ResourceKey<DamageType> register(String name)
    {
        return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(BagelsSpell.MODID, name));
    }

    // Magic
    public static final ResourceKey<DamageType> BLOSSOM_MAGIC = register("blossom_magic");



    // Do we actually need this?
    public static void bootstrap(BootstapContext<DamageType> context)
    {
        //context.register(ABYSSAL_MAGIC, new DamageType(ABYSSAL_MAGIC.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0F));
        context.register(BLOSSOM_MAGIC, new DamageType(BLOSSOM_MAGIC.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0F));
    }

}
