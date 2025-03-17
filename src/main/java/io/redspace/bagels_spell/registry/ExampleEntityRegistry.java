package io.redspace.bagels_spell.registry;
import io.redspace.bagels_spell.BagelsSpell;
import io.redspace.bagels_spell.entity.FlowerStrikeEntity;
import io.redspace.bagels_spell.entity.FrostStrike;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ExampleEntityRegistry {
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BagelsSpell.MODID);

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }

    public static final RegistryObject<EntityType<FrostStrike>> FROST_STRIKE =
            ENTITIES.register("frost_strike", () -> EntityType.Builder.<FrostStrike>of(FrostStrike::new, MobCategory.MISC)
                    .sized(5f, 1f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(BagelsSpell.MODID, "frost_strike").toString()));

    public static final RegistryObject<EntityType<FlowerStrikeEntity>> FLOWER_STRIKE =
            ENTITIES.register("flower_strike", () -> EntityType.Builder.<FlowerStrikeEntity>of(FlowerStrikeEntity::new, MobCategory.MISC)
                    .sized(2f, 2f)
                    .clientTrackingRange(64)
                    .build(new ResourceLocation(BagelsSpell.MODID, "flower_strike").toString()));
}
