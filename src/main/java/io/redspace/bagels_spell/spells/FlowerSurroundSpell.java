package io.redspace.bagels_spell.spells;

import io.redspace.bagels_spell.BagelsSpell;
import io.redspace.bagels_spell.entity.flower_slash.FlowerSlashProjectile;
import io.redspace.bagels_spell.entity.flower_surround.FlowerSurroundProjectile;
import io.redspace.bagels_spell.registry.PbSchoolRegistry;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static io.redspace.ironsspellbooks.capabilities.magic.MagicManager.spawnParticles;

@AutoSpellConfig
public class FlowerSurroundSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(BagelsSpell.MODID, "flower_surround");
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.UNCOMMON)
            .setSchoolResource(PbSchoolRegistry.BLOSSOM_RESOURCE)
            .setMaxLevel(10)
            .setCooldownSeconds(30)
            .build();

    public FlowerSurroundSpell() {
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 1;
        this.castTime = 0;
        this.baseManaCost = 30;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.empty();
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.empty(); // Add one if needed
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getSpellPower(spellLevel, caster), 2))
        );
    }



    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        // No recast logic anymore; just a single cast
        int particleCount = 100; // Total number of particles
        double radius = 3.0; // Base radius of the swirl
        double height = 2.0; // Height of the swirl
        double speed = 0.1;
        double randomnessFactor = 2;
        double centerX = entity.getX();
        double centerY = entity.getY();
        double centerZ = entity.getZ();


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

        }
        for (int i = 0; i < particleCount; i++) {
            double randomOffset = (Utils.random.nextDouble() - 0.5) * randomnessFactor;

            double adjustedRadius = radius + randomOffset;
            double angle = -((i / (double) particleCount) * Math.PI * 4 + randomOffset);

            double x = centerX + Math.cos(angle) * adjustedRadius;
            double z = centerZ + Math.sin(angle) * adjustedRadius;
            double y = centerY + (double) i / particleCount * height + randomOffset;

            double deltaX = (Utils.random.nextDouble() - 0.5) * 0.1;
            double deltaY = (Utils.random.nextDouble() - 0.5) * 0.1;
            double deltaZ = (Utils.random.nextDouble() - 0.5) * 0.1;

            spawnParticles(level, ParticleTypes.CHERRY_LEAVES, x, y, z, 1, deltaX, deltaY, deltaZ, speed, true);
        }

        for (int i = 0; i < particleCount * 4; i++) {
            double randomOffset = (Utils.random.nextDouble() - 0.5) * 4 * randomnessFactor;

            double adjustedRadius = radius + randomOffset;
            double angle = ((i / (double) particleCount) * Math.PI * 4 + randomOffset);

            double x = centerX + Math.cos(angle) * adjustedRadius;
            double z = centerZ + Math.sin(angle) * adjustedRadius;
            double y = centerY + (double) i / particleCount * height + randomOffset;

            double deltaX = (Utils.random.nextDouble() - 0.5) * 0.1;
            double deltaY = (Utils.random.nextDouble() - 0.5) * 0.1;
            double deltaZ = (Utils.random.nextDouble() - 0.5) * 0.1;

            spawnParticles(level, ParticleTypes.CHERRY_LEAVES, x, y, z, 1, deltaX, deltaY, deltaZ, speed, true);
        }

        // Create multiple FlowerSurroundProjectiles in all directions around the caster
        int count = 4; // number of petals/projectiles
        for (int i = 0; i < count; i++) {
            double angle = Math.toRadians((360.0 / count) * i);
            double x = Math.cos(angle);
            double z = Math.sin(angle);
            double y = 0.1 * Math.sin(angle * 2); // add vertical wave/tilt for variation

            Vec3 direction = new Vec3(x, y, z).normalize();

            FlowerSurroundProjectile projectile = new FlowerSurroundProjectile(level, entity);
            projectile.setPos(entity.getX(), entity.getEyeY() - 0.2, entity.getZ()); // center launch point
            projectile.shoot(direction);
            projectile.setDamage(getSpellPower(spellLevel, entity));
            level.addFreshEntity(projectile);
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public SpellDamageSource getDamageSource(@Nullable Entity projectile, Entity attacker) {
        return super.getDamageSource(projectile, attacker).setLifestealPercent(0.15f);
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.SLASH_ANIMATION;
    }
}