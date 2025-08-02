package io.redspace.bagels_spell.registry;
import io.redspace.bagels_spell.BagelsSpell;
import io.redspace.bagels_spell.entity.DamageAoe;
import io.redspace.bagels_spell.entity.flower_evade.FlowerEvadeProjectile;
import io.redspace.bagels_spell.entity.flower_slash.FlowerSlashProjectile;
import io.redspace.bagels_spell.entity.flower_surround.FlowerSurroundProjectile;
import io.redspace.bagels_spell.entity.thousand_blossoms.ThousandBlossomProjectile;
import io.redspace.bagels_spell.entity.triple_slash.TripleSlashProjectile;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PbEntityRegistry {
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BagelsSpell.MODID);

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }


    public static final RegistryObject<EntityType<FlowerSlashProjectile>> FLOWER_SLASH_PROJECTILE =
            ENTITIES.register("flower_slash", () -> EntityType.Builder.<FlowerSlashProjectile>of(FlowerSlashProjectile::new, MobCategory.MISC)
                    .sized(2f, .5f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(BagelsSpell.MODID, "flower_slash").toString()));

    public static final RegistryObject<EntityType<FlowerEvadeProjectile>> FLOWER_EVADE_PROJECTILE =
            ENTITIES.register("flower_evade", () -> EntityType.Builder.<FlowerEvadeProjectile>of(FlowerEvadeProjectile::new, MobCategory.MISC)
                    .sized(2f, .5f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(BagelsSpell.MODID, "flower_evade").toString()));

    public static final RegistryObject<EntityType<FlowerSurroundProjectile>> FLOWER_SURROUND_PROJECTILE =
            ENTITIES.register("flower_surround", () -> EntityType.Builder.<FlowerSurroundProjectile>of(FlowerSurroundProjectile::new, MobCategory.MISC)
                    .sized(2f, .5f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(BagelsSpell.MODID, "flower_surround").toString()));

    public static final RegistryObject<EntityType<TripleSlashProjectile>> TRIPLE_SLASH_PROJECTILE =
            ENTITIES.register("triple_slash", () -> EntityType.Builder.<TripleSlashProjectile>of(TripleSlashProjectile::new, MobCategory.MISC)
                    .sized(2f, .5f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(BagelsSpell.MODID, "triple_slash").toString()));

    public static final RegistryObject<EntityType<ThousandBlossomProjectile>> THOUSAND_BLOSSOM_PROJECTILE =
            ENTITIES.register("thousand_blossoms", () -> EntityType.Builder.<ThousandBlossomProjectile>of(ThousandBlossomProjectile::new, MobCategory.MISC)
                    .sized(2f, .5f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(BagelsSpell.MODID, "thousand_blossom").toString()));

    public static final RegistryObject<EntityType<DamageAoe>> DAMAGE_AOE =
        ENTITIES.register("damage_aoe", () -> EntityType.Builder.<DamageAoe>of(io.redspace.bagels_spell.entity.DamageAoe::new, MobCategory.MISC)
                        .sized(4f, .8f)
                        .clientTrackingRange(64)
                        .build(new ResourceLocation(BagelsSpell.MODID, "damage_aoe").toString()));
}
