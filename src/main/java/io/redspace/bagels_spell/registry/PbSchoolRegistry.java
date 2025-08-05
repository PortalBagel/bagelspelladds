package io.redspace.bagels_spell.registry;

import io.redspace.bagels_spell.BagelsSpell;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import io.redspace.bagels_spell.util.PbTags;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class PbSchoolRegistry extends SchoolRegistry {



    private static final DeferredRegister<SchoolType> BAGELS_SCHOOLS = DeferredRegister.create(SCHOOL_REGISTRY_KEY, BagelsSpell.MODID);

    public static void register(IEventBus eventBus)
    {
        BAGELS_SCHOOLS.register(eventBus);
    }

    private static RegistryObject<SchoolType> registerSchool(SchoolType type)
    {
        return BAGELS_SCHOOLS.register(type.getId().getPath(), () -> type);
    }

    // Blossom
    public static final ResourceLocation BLOSSOM_RESOURCE = BagelsSpell.id("blossom");

    public static final RegistryObject<SchoolType> BLOSSOM = registerSchool(new SchoolType
            (
                    BLOSSOM_RESOURCE,
                    PbTags.BLOSSOM_FOCUS,
                    Component.translatable("school.bagels_spell.blossom").withStyle(Style.EMPTY.withColor(0xffb8e4)),
                    LazyOptional.of(PbAttributeRegistry.BLOSSOM_MAGIC_POWER::get),
                    LazyOptional.of(PbAttributeRegistry.BLOSSOM_MAGIC_RESIST::get),
                    LazyOptional.of(SoundRegistry.ICE_CAST::get),
                    PbDamageTypes.BLOSSOM_MAGIC
            ));

}
