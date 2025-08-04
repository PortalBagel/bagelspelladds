package io.redspace.bagels_spell.registry;

import io.redspace.bagels_spell.BagelsSpell;
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

public class PbSchoolRegistry {
    public static final ResourceKey<Registry<SchoolType>> SCHOOL_REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(BagelsSpell.MODID, "schools"));
    private static final DeferredRegister<SchoolType> SCHOOLS = DeferredRegister.create(SCHOOL_REGISTRY_KEY, BagelsSpell.MODID);
    public static final Supplier<IForgeRegistry<SchoolType>> REGISTRY = SCHOOLS.makeRegistry(() -> new RegistryBuilder<SchoolType>().disableSaving().disableOverrides());

    public static void register(IEventBus eventBus) {
        SCHOOLS.register(eventBus);
        eventBus.addListener(PbSchoolRegistry::clientSetup);
    }

    private static RegistryObject<SchoolType> registerSchool(SchoolType schoolType) {
        return SCHOOLS.register(schoolType.getId().getPath(), () -> schoolType);
    }

    public static SchoolType getSchool(ResourceLocation resourceLocation) {
        return REGISTRY.get().getValue(resourceLocation);
    }


    public static final ResourceLocation BLOSSOM_RESOURCE = BagelsSpell.id("blossom");

   public static final RegistryObject<SchoolType> BLOSSOM = registerSchool(new SchoolType
           (
            BLOSSOM_RESOURCE,
            PbTags.BLOSSOM_FOCUS,
            Component.translatable("school.bagels_spell.blossom").withStyle(Style.EMPTY.withColor(0xf3495d)),
            LazyOptional.of(PbAttributeRegistry.BLOSSOM_MAGIC_POWER::get),
            LazyOptional.of(PbAttributeRegistry.BLOSSOM_MAGIC_RESIST::get),
            LazyOptional.of(SoundRegistry.ICE_CAST::get),
                   PbDamageTypes.BLOSSOM_MAGIC
        ));



    @Nullable
    public static SchoolType getSchoolFromFocus(ItemStack focusStack) {
        for (SchoolType school : REGISTRY.get().getValues()) {
            if (school.isFocus(focusStack)) {
                return school;
            }
        }
        return null;
    }

    public static void clientSetup(ModelEvent.RegisterAdditional event) {

    }
}
