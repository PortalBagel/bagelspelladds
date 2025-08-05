package io.redspace.bagels_spell.spells;

import io.redspace.bagels_spell.entity.HealingAoe;
import io.redspace.bagels_spell.registry.PbSchoolRegistry;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.network.spell.ClientboundAborptionParticles;
import io.redspace.ironsspellbooks.network.spell.ClientboundFortifyAreaParticles;
import io.redspace.ironsspellbooks.network.spell.ClientboundParticleShockwave;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import io.redspace.ironsspellbooks.setup.Messages;
import io.redspace.ironsspellbooks.spells.TargetAreaCastData;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import io.redspace.bagels_spell.BagelsSpell;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;

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
import net.minecraftforge.common.Tags;
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
        this.manaCostPerLevel = 50;
        this.baseSpellPower = 6;
        this.spellPowerPerLevel = 1;
        this.castTime = 20;
        this.baseManaCost = 100;
        //hi
    }

    double radius = 12.0;

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(PbSchoolRegistry.BLOSSOM_RESOURCE)
            .setMaxLevel(1)
            .setCooldownSeconds(120)
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
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        //return entity.isHolding(ItemStack::isDamageableItem); // make sure the player is holding a tool
        return entity.isHolding(stack -> stack.is(Tags.Items.TOOLS));

    }


    public void onServerPreCast(Level level, int spellLevel, LivingEntity entity, @Nullable MagicData playerMagicData) {
        super.onServerPreCast(level, spellLevel, entity, playerMagicData);
        if (playerMagicData == null)
            return;
        TargetedAreaEntity targetedAreaEntity = TargetedAreaEntity.createTargetAreaEntity(level, entity.position(), (float) radius, 16777215, entity);
        playerMagicData.setAdditionalCastData(new TargetAreaCastData(entity.position(), targetedAreaEntity));
    }
    @Override
    public void onClientPreCast(Level level, int spellLevel, LivingEntity entity, InteractionHand hand, @Nullable MagicData playerMagicData) {
        super.onClientPreCast(level, spellLevel, entity, hand, playerMagicData);



        Vec3 forward = entity.getForward().normalize(); // Forward direction
        for (int i = 0; i < 20; i++) {

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
        return SpellAnimations.PREPARE_CROSS_ARMS;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return SpellAnimations.ONE_HANDED_HORIZONTAL_SWING_ANIMATION;
    }

    private float getDamage(int spellLevel, LivingEntity caster) {
        return getSpellPower(spellLevel, caster) * .5f;
    }


    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);

        double centerX = entity.getX();
        double centerY = entity.getY();
        double centerZ = entity.getZ();

        int particleCount = 500;

        double height = 4.0;
        double speed = 0.1;
        double randomnessFactor = 2;

        for (int i = 0; i < particleCount; i++) {
            double randomOffset = (Utils.random.nextDouble() - 0.5) * randomnessFactor;

            double adjustedRadius = radius + randomOffset;
            double angle = (i / (double) particleCount) * Math.PI * 4 + randomOffset;

            double x = centerX + Math.cos(angle) * adjustedRadius;
            double z = centerZ + Math.sin(angle) * adjustedRadius;
            double y = centerY + (double) i / particleCount * height + randomOffset;

            double deltaX = (Utils.random.nextDouble() - 0.5) * 0.1;
            double deltaY = (Utils.random.nextDouble() - 0.5) * 0.1;
            double deltaZ = (Utils.random.nextDouble() - 0.5) * 0.1;

            spawnParticles(world, ParticleTypes.CHERRY_LEAVES, x, y, z, 1, deltaX, deltaY, deltaZ, speed, true);
        }

        //Second swirl
        double tallHeight = height * 2;

        for (int i = 0; i < particleCount*4; i++) {
            double randomOffset = (Utils.random.nextDouble() - 0.5) * 4 *randomnessFactor;

            double adjustedRadius = radius + randomOffset;
            double angle = -((i / (double) particleCount) * Math.PI * 4 + randomOffset); // reversed angle

            double x = centerX + Math.cos(angle) * adjustedRadius;
            double z = centerZ + Math.sin(angle) * adjustedRadius;
            double y = centerY + (double) i / particleCount * tallHeight + randomOffset;

            double deltaX = (Utils.random.nextDouble() - 0.5) * 0.1;
            double deltaY = (Utils.random.nextDouble() - 0.5) * 0.1;
            double deltaZ = (Utils.random.nextDouble() - 0.5) * 0.1;

            spawnParticles(world, ParticleTypes.CHERRY_LEAVES, x, y, z, 1, deltaX, deltaY, deltaZ, speed, true);
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

            spawnParticles(world, ParticleTypes.CHERRY_LEAVES, x, y, z, 1, deltaX, deltaY, deltaZ, speed, true);
        }

        for (int i = 0; i < particleCount * 4; i++) {
            double randomOffset = (Utils.random.nextDouble() - 0.5) * 4 * randomnessFactor;

            double adjustedRadius = radius + randomOffset;
            double angle = ((i / (double) particleCount) * Math.PI * 4 + randomOffset);

            double x = centerX + Math.cos(angle) * adjustedRadius;
            double z = centerZ + Math.sin(angle) * adjustedRadius;
            double y = centerY + (double) i / particleCount * tallHeight + randomOffset;

            double deltaX = (Utils.random.nextDouble() - 0.5) * 0.1;
            double deltaY = (Utils.random.nextDouble() - 0.5) * 0.1;
            double deltaZ = (Utils.random.nextDouble() - 0.5) * 0.1;

            spawnParticles(world, ParticleTypes.CHERRY_LEAVES, x, y, z, 1, deltaX, deltaY, deltaZ, speed, true);
        }

        world.getEntitiesOfClass(LivingEntity.class,
                        new AABB(entity.position().subtract(radius, radius, radius), entity.position().add(radius, radius, radius)))
                .forEach((target) -> {
                    double distance = entity.distanceTo(target);

                    if (distance <= radius) {
                        // Allies → Give Strength
                        if (Utils.shouldHealEntity(entity, target)) {
                            target.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20 * 120, (int) 1, false, false, true));
                            Messages.sendToPlayersTrackingEntity(new ClientboundAborptionParticles(target.position()), entity, true);
                        }
                        // Non-players → Give Weakness
                        else if (!(target instanceof Player)) {
                            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20 * 120, 1));
                        }
                    }
                });


        //Messages.sendToPlayersTrackingEntity(new ClientboundFortifyAreaParticles(entity.position()), entity, true);


        super.onCast(world, spellLevel, entity, castSource, playerMagicData);



    }





}