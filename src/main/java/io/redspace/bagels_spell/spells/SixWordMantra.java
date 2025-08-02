package io.redspace.bagels_spell.spells;

import io.redspace.bagels_spell.BagelsSpell;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.*;
import io.redspace.ironsspellbooks.entity.spells.fireball.SmallMagicFireball;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static io.redspace.ironsspellbooks.capabilities.magic.MagicManager.spawnParticles;

@AutoSpellConfig
public class SixWordMantra extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(BagelsSpell.MODID, "six_word_mantra");
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.RARE)
            .setSchoolResource(SchoolRegistry.HOLY_RESOURCE)
            .setMaxLevel(5)
            .setCooldownSeconds(3)
            .build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                //Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getDamage(spellLevel, caster), 2)),
                //Component.translatable("ui.irons_spellbooks.projectile_count", (int) (getRecastCount(spellLevel, caster)))
        );
    }

    public SixWordMantra() {
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 3;
        this.spellPowerPerLevel = 2;
        this.castTime = 80;
        this.baseManaCost = 80;
    }

    public void onClientPreCast(Level level, int spellLevel, LivingEntity entity, InteractionHand hand, @Nullable MagicData playerMagicData) {
        super.onClientPreCast(level, spellLevel, entity, hand, playerMagicData);

        Vec3 forward = entity.getForward().normalize(); // Forward direction
        for (int i = 0; i < 10; i++) {

            Vec3 randomOffset = new Vec3((Utils.random.nextDouble() - 0.5) * 0.8,
                    (Utils.random.nextDouble() - 0.5) * 0.8,
                    (Utils.random.nextDouble() - 0.5) * 0.8);
            Vec3 motion = forward.scale(Utils.random.nextDouble() * 0.25f).add(randomOffset);


            double x = entity.getX() + (Utils.random.nextDouble() - 0.5) * 1.6;
            double y = entity.getY() + (Utils.random.nextDouble() - 0.5) * 2.0 + 1.0;
            double z = entity.getZ() + (Utils.random.nextDouble() - 0.5) * 1.6;


            level.addParticle(ParticleTypes.ENCHANT, x, y, z, motion.x, motion.y, motion.z);
        }
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
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
    public int getRecastCount(int spellLevel, @Nullable LivingEntity entity) {
        return 6;
    }

    //@Override
    //public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        //return Utils.preCastTargetHelper(level, entity, playerMagicData, this, 64, .15f);
    //}

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        var recasts = playerMagicData.getPlayerRecasts();
        if (!recasts.hasRecastForSpell(getSpellId())) {
            recasts.addRecast(new RecastInstance(getSpellId(), spellLevel, 6, 120, castSource, null), playerMagicData);


        }
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public void onRecastFinished(ServerPlayer serverPlayer, RecastInstance recastInstance, RecastResult recastResult, ICastDataSerializable castDataSerializable) {
        super.onRecastFinished(serverPlayer, recastInstance, recastResult, castDataSerializable);

        int currentRecast = 6 - recastInstance.getRemainingRecasts();

        MobEffectInstance effect = null;
        if (serverPlayer.level instanceof ServerLevel serverLevel) {
            for (int i = 0; i < 40 + (currentRecast * 3); i++) {
                double x = serverPlayer.getX() + (Utils.random.nextDouble() - 0.5) * 1.2;
                double y = serverPlayer.getY() + (Utils.random.nextDouble()) * 2.0;
                double z = serverPlayer.getZ() + (Utils.random.nextDouble() - 0.5) * 1.2;

                Vec3 motion = new Vec3(0, 0.02, 0); // Slight upward motion

                serverLevel.sendParticles(ParticleTypes.ENCHANT, x, y, z, 1, motion.x, motion.y, motion.z, 0.01);
            }
        }
        switch (currentRecast) {
            case 1 -> effect = new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 3600, 0);  // Speed I
            case 2 -> effect = new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 3600, 0); // Resistance I
            case 3 -> effect = new MobEffectInstance(MobEffects.DAMAGE_BOOST, 3600, 0); // Strength I
            case 4 -> effect = new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 4800, 1); // Speed II
            case 5 -> effect = new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 4800, 1); // Resistance II
            case 6 -> {
                serverPlayer.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 6000, 1)); // Strength II
                serverPlayer.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000, 2));
                serverPlayer.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 6000, 1));
            } // Strength II, Resistance III, Speed II
        }

        if (effect != null) {
            serverPlayer.addEffect(effect);
        }


        Vec3 origin = serverPlayer.getEyePosition().add(serverPlayer.getForward().normalize().scale(.2f));
        serverPlayer.level.playSound(null, origin.x, origin.y, origin.z, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 2.0f, 1.0f);
    }

    private float getDamage(int spellLevel, LivingEntity caster) {
        return getSpellPower(spellLevel, caster);
    }

    @Override
    public ICastDataSerializable getEmptyCastData() {
        return new MultiTargetEntityCastData();
    }
}
