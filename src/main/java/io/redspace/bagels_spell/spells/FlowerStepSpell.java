package io.redspace.bagels_spell.spells;

import io.redspace.bagels_spell.BagelsSpell;
import io.redspace.bagels_spell.registry.PbSchoolRegistry;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.spells.ender.TeleportSpell;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class FlowerStepSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(BagelsSpell.MODID, "flower_step");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
               // Component.translatable("ui.irons_spellbooks.distance", Utils.stringTruncation(getDistance(spellLevel, caster), 1))
        );
    }

    public FlowerStepSpell() {
        this.baseSpellPower = 12;
        this.spellPowerPerLevel = 4;
        this.baseManaCost = 30;
        this.manaCostPerLevel = 10;
        this.castTime = 0;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.UNCOMMON)
            .setSchoolResource(PbSchoolRegistry.BLOSSOM_RESOURCE)

            .setMaxLevel(5)
            .setCooldownSeconds(5)
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
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.empty();
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundRegistry.BLOOD_STEP.get());
    }

    @Override
    public void onClientPreCast(Level level, int spellLevel, LivingEntity entity, InteractionHand hand, @Nullable MagicData playerMagicData) {
        super.onClientPreCast(level, spellLevel, entity, hand, playerMagicData);


        Vec3 forward = entity.getForward().normalize(); // Forward direction
        for (int i = 0; i < 70; i++) {

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
    /*public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        // Apply AIRBORNE effect
        entity.addEffect(new MobEffectInstance(MobEffectRegistry.AIRBORNE.get(), 10, 0, false, false, false));

        double launchForce = 1.5 + 0.5 * spellLevel; // scale launch with spell level
        Vec3 direction = entity.getLookAngle().normalize().scale(launchForce);
        entity.setDeltaMovement(direction);
        entity.hurtMarked = true;

        entity.resetFallDistance();
        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), getCastFinishSound().get(), SoundSource.NEUTRAL, 1f, 1f);

        // Apply Oakskin
        entity.addEffect(new MobEffectInstance(MobEffectRegistry.OAKSKIN.get(), 100, 0, true, false, false));

        // Apply Slow Falling for 10 seconds (200 ticks)
        entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 200, 0, true, false, false));

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }*/
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        Vec3 dest = null;
        var teleportData = (TeleportSpell.TeleportData) playerMagicData.getAdditionalCastData();
        if (teleportData != null) {
            var potentialTarget = teleportData.getTeleportTargetPosition();
            if (potentialTarget != null) {
                dest = potentialTarget;
                entity.teleportTo(dest.x, dest.y, dest.z);
            }
        } else {
            HitResult hitResult = Utils.raycastForEntity(level, entity, getDistance(spellLevel, entity), true);
            if (entity.isPassenger()) {
                entity.stopRiding();
            }
            if (hitResult.getType() == HitResult.Type.ENTITY && ((EntityHitResult) hitResult).getEntity() instanceof LivingEntity target) {
                //dest = target.position().subtract(new Vec3(0, 0, 1.5).yRot(target.getYRot()));
                for (int i = 0; i < 8; i++) {
                    dest = target.position().subtract(new Vec3(0, 0, 1.5).yRot(-(target.getYRot() + i * 45) * Mth.DEG_TO_RAD));
                    if (level.getBlockState(BlockPos.containing(dest).above()).isAir())
                        break;

                }
                entity.teleportTo(dest.x, dest.y + 1f, dest.z);
                entity.lookAt(EntityAnchorArgument.Anchor.EYES, target.getEyePosition().subtract(0, .15, 0));
            } else {
                dest = TeleportSpell.findTeleportLocation(level, entity, getDistance(spellLevel, entity));
                entity.teleportTo(dest.x, dest.y, dest.z);

            }
        }
        entity.resetFallDistance();
        level.playSound(null, dest.x, dest.y, dest.z, getCastFinishSound().get(), SoundSource.NEUTRAL, 1f, 1f);

        //Invis take 1 tick to set in
        //entity.setInvisible(true);
        entity.addEffect(new MobEffectInstance(MobEffectRegistry.OAKSKIN.get(), 100, 0, true, false, false));


        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float getDistance(int spellLevel, LivingEntity sourceEntity) {
        return (float) (Utils.softCapFormula(getEntityPowerMultiplier(sourceEntity)) * getSpellPower(spellLevel, null));


    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return AnimationHolder.none();
    }

}
