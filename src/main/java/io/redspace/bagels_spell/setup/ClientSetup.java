package io.redspace.bagels_spell.setup;

import io.redspace.bagels_spell.BagelsSpell;
import io.redspace.bagels_spell.entity.flower_slash.FlowerSlashRenderer;
import io.redspace.bagels_spell.entity.thousand_blossoms.ThousandBlossomRenderer;
import io.redspace.bagels_spell.entity.triple_slash.TripleSlashRenderer;
import io.redspace.bagels_spell.entity.flower_surround.FlowerSurroundRenderer;
import io.redspace.bagels_spell.entity.flower_evade.FlowerEvadeRenderer;
import io.redspace.bagels_spell.registry.PbEntityRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BagelsSpell.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class


ClientSetup {
   // @SubscribeEvent
   @SubscribeEvent
   public static void rendererRegister(EntityRenderersEvent.RegisterRenderers event) {
       event.registerEntityRenderer(PbEntityRegistry.FLOWER_SLASH_PROJECTILE.get(), FlowerSlashRenderer::new);
       event.registerEntityRenderer(PbEntityRegistry.FLOWER_EVADE_PROJECTILE.get(), FlowerEvadeRenderer::new);
       event.registerEntityRenderer(PbEntityRegistry.FLOWER_SURROUND_PROJECTILE.get(), FlowerSurroundRenderer::new);
       event.registerEntityRenderer(PbEntityRegistry.THOUSAND_BLOSSOM_PROJECTILE.get(), ThousandBlossomRenderer::new);
       event.registerEntityRenderer(PbEntityRegistry.TRIPLE_SLASH_PROJECTILE.get(), TripleSlashRenderer::new);
   }


}