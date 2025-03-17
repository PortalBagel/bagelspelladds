package io.redspace.bagels_spell.spells;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.NuclearBombEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.NuclearExplosionEntity;
import io.redspace.bagels_spell.BagelsSpell;
import io.redspace.bagels_spell.entity.AoeEntity;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.entity.spells.target_area.TargetedAreaEntity;

import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ParticleUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.client.particle.CherryParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static io.redspace.ironsspellbooks.capabilities.magic.MagicManager.spawnParticles;

@AutoSpellConfig
public class FlowerDomainSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(BagelsSpell.MODID, "flower_domain");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                //Component.translatable("ui.irons_example_mod.radius", Utils.stringTruncation(getSpellPower(spellLevel, caster), 1))
        );
    }

    public FlowerDomainSpell() {
        this.manaCostPerLevel = 100;
        this.baseSpellPower = 6;
        this.spellPowerPerLevel = 1;
        this.castTime = 20;
        this.baseManaCost = 500;
        //hi
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.NATURE_RESOURCE)
            .setMaxLevel(1)
            .setCooldownSeconds(600)
            .build();


    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.empty();
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.empty();
    }


    @Override
    public void onClientPreCast(Level level, int spellLevel, LivingEntity entity, InteractionHand hand, @Nullable MagicData playerMagicData) {
        super.onClientPreCast(level, spellLevel, entity, hand, playerMagicData);

        Vec3 forward = entity.getForward().normalize(); // Forward direction
        for (int i = 0; i < 30; i++) {

            Vec3 randomOffset = new Vec3((Utils.random.nextDouble() - 0.5) * 0.8,
                    (Utils.random.nextDouble() - 0.5) * 0.8,
                    (Utils.random.nextDouble() - 0.5) * 0.8);
            Vec3 motion = forward.scale(Utils.random.nextDouble() * 0.25f).add(randomOffset);


            double x = entity.getX() + (Utils.random.nextDouble() - 0.5) * 1.6;
            double y = entity.getY() + (Utils.random.nextDouble() - 0.5) * 2.0 + 1.0;
            double z = entity.getZ() + (Utils.random.nextDouble() - 0.5) * 1.6;


            level.addParticle(ParticleTypes.CHERRY_LEAVES, x, y, z, motion.x, motion.y, motion.z);
        }
    }


    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.CHARGE_ANIMATION;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return SpellAnimations.FINISH_ANIMATION;
    }


    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);

        double centerX = entity.getX();
        double centerY = entity.getY();
        double centerZ = entity.getZ();

        int particleCount = 250; // Total number of particles
        double radius = 5.0; // Base radius of the swirl
        double height = 4.0; // Height of the swirl
        double speed = 0.1;
        double randomnessFactor = 2;

        for (int i = 0; i < particleCount; i++) {
            double randomOffset = (Utils.random.nextDouble() - 0.5) * randomnessFactor;

            double adjustedRadius = radius + randomOffset;

            double angle = (i / (double) particleCount) * Math.PI * 4 + randomOffset; // Adjust angle to spread particles

            double x = centerX + Math.cos(angle) * adjustedRadius; // Swirl horizontally (X)
            double z = centerZ + Math.sin(angle) * adjustedRadius; // Swirl horizontally (Z)

            double y = centerY + (double) i / particleCount * height + randomOffset;

            double deltaX = (Utils.random.nextDouble() - 0.5) * 0.1; // Slight random motion
            double deltaY = (Utils.random.nextDouble() - 0.5) * 0.1;
            double deltaZ = (Utils.random.nextDouble() - 0.5) * 0.1;


            spawnParticles(level, ParticleTypes.CHERRY_LEAVES, x, y, z, 1, deltaX, deltaY, deltaZ, speed, true);

/*
            Vec3 spawn = null;
            if (playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData castTargetingData) {

                var target = castTargetingData.getTarget((ServerLevel) level);
                if (target != null)
                    spawn = target.position();

                if (spawn == null) {
                    spawn = Utils.raycastForEntity(level, entity, 32, true, .15f).getLocation();
                    spawn = Utils.moveToRelativeGroundLevel(level, spawn, 6);
                }
            }



            AoeEntity aoeEntity = new AoeEntity(level);


            aoeEntity.setOwner(entity);
            aoeEntity.setCircular();
            aoeEntity.setRadius((float)radius);
            aoeEntity.setDuration(2);
            aoeEntity.setDamage(getDamage(spellLevel, entity));//or getHealing
            aoeEntity.setPos(spawn);
            level.addFreshEntity(aoeEntity);


*/

        }


    }




}