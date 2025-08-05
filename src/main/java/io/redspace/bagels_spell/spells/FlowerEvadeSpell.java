package io.redspace.bagels_spell.spells;

import io.redspace.bagels_spell.BagelsSpell;
import io.redspace.bagels_spell.entity.flower_evade.FlowerEvadeProjectile;
import io.redspace.bagels_spell.entity.flower_slash.FlowerSlashProjectile;
import io.redspace.bagels_spell.registry.PbSchoolRegistry;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class FlowerEvadeSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(BagelsSpell.MODID, "flower_evade");
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.UNCOMMON)
            .setSchoolResource(PbSchoolRegistry.BLOSSOM_RESOURCE)

            .setMaxLevel(5)
            .setCooldownSeconds(10)
            .build();

    public FlowerEvadeSpell() {
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
        this.castTime = 0;
        this.baseManaCost = 25;
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
    public int getRecastCount(int spellLevel, @Nullable LivingEntity caster) {
        return Math.min(1, spellLevel);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (!playerMagicData.getPlayerRecasts().hasRecastForSpell(getSpellId())) {
            playerMagicData.getPlayerRecasts().addRecast(
                    new RecastInstance(getSpellId(), spellLevel, getRecastCount(spellLevel, entity), 30, castSource, null),
                    playerMagicData
            );
        }

        // Apply AIRBORNE effect
        entity.addEffect(new MobEffectInstance(MobEffectRegistry.AIRBORNE.get(), 10, 0, false, false, false));

        //Calculate backward dash vector (~5 blocks back)
        double dashDistance = 2.0;
        Vec3 backwardDirection = entity.getLookAngle().normalize().scale(-dashDistance);
        entity.setDeltaMovement(backwardDirection);
        entity.hurtMarked = true;
        entity.resetFallDistance();



        // Apply Oakskin
        entity.addEffect(new MobEffectInstance(MobEffectRegistry.OAKSKIN.get(), 100, 0, true, false, false));

        // Apply Slow Falling for 10 seconds (200 ticks)
        //entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 200, 0, true, false, false));

        // Spawn slash projectile
        FlowerEvadeProjectile slash = new FlowerEvadeProjectile(level, entity);
        slash.setPos(entity.getEyePosition());
        slash.shoot(entity.getLookAngle());
        slash.setDamage(getSpellPower(spellLevel, entity));
        level.addFreshEntity(slash);

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