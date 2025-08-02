package io.redspace.bagels_spell;

import com.mojang.logging.LogUtils;
import io.redspace.bagels_spell.registry.*;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BagelsSpell.MODID)
public  class BagelsSpell{
    public static final String MODID = "bagels_spell";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static MagicManager MAGIC_MANAGER;


    public BagelsSpell() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        PbSpellRegistry.register(modEventBus);
        PbEntityRegistry.register(modEventBus);
        PbItemRegistry.register(modEventBus);
        PbAttributeRegistry.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        PbSchoolRegistry.register(modEventBus);
        //ExampleMobEffectRegistry.MOB_EFFECT_DEFERRED_REGISTER.register(modEventBus);
        PbMobEffectRegistry.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }

    public static ResourceLocation id(@NotNull String path) {
        return new ResourceLocation(BagelsSpell.MODID, path);
    }
}
