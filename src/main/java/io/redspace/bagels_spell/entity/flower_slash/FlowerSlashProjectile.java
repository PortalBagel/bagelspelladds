package io.redspace.bagels_spell.entity.flower_slash;

import io.redspace.bagels_spell.registry.PbEntityRegistry;
import io.redspace.bagels_spell.registry.PbSpellRegistry;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractShieldEntity;
import io.redspace.ironsspellbooks.entity.spells.ShieldPart;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FlowerSlashProjectile extends Projectile implements AntiMagicSusceptible {
    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(FlowerSlashProjectile.class, EntityDataSerializers.FLOAT);
    private static final double SPEED = 0.4d;
    private static final int EXPIRE_TIME = 1 * 10;
    public final int animationSeed;
    private final float maxRadius;
    public AABB oldBB;
    private int age;
    private float damage;
    public int animationTime;
    private List<Entity> victims;

    public FlowerSlashProjectile(EntityType<? extends FlowerSlashProjectile> entityType, Level level) {
        super(entityType, level);
        animationSeed = Utils.random.nextInt(9999);

        setRadius(8f);
        maxRadius = 8;
        oldBB = getBoundingBox();
        victims = new ArrayList<>();
        this.setNoGravity(true);
    }

    public FlowerSlashProjectile(EntityType<? extends FlowerSlashProjectile> entityType, Level levelIn, LivingEntity shooter) {
        this(entityType, levelIn);
        setOwner(shooter);
        setYRot(shooter.getYRot());
        setXRot(shooter.getXRot());
    }

    public FlowerSlashProjectile(Level levelIn, LivingEntity shooter) {
        this(PbEntityRegistry.FLOWER_SLASH_PROJECTILE.get(), levelIn, shooter);
    }

    public void shoot(Vec3 rotation) {
        setDeltaMovement(rotation.scale(SPEED));
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    //TODO: override "doWaterSplashEffect"
    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(DATA_RADIUS, 0.5F);
    }

    public void setRadius(float newRadius) {
        if (newRadius <= maxRadius && !this.level.isClientSide) {
            this.getEntityData().set(DATA_RADIUS, Mth.clamp(newRadius, 0.0F, maxRadius));
        }
    }

    public float getRadius() {
        return this.getEntityData().get(DATA_RADIUS);
    }

    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }

    @Override
    public void tick() {
        super.tick();
        if (++age > EXPIRE_TIME) {
            discard();
            return;
        }
        oldBB = getBoundingBox();
        setRadius(getRadius() + 0.7f);

        //this.setDeltaMovement(Vec3.ZERO);

        animationTime++;
        int totalFrames = 8; //
        int frameDuration = 4; //
        int maxAnimationTime = totalFrames * frameDuration;


        if (animationTime >= maxAnimationTime) {
            discard(); // Removes entity from the world
        }

        if (!level.isClientSide) {
            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() == HitResult.Type.BLOCK) {
                onHitBlock((BlockHitResult) hitresult);
            }
            for (Entity entity : level.getEntities(this, this.getBoundingBox()).stream().filter(target -> canHitEntity(target) && !victims.contains(target)).collect(Collectors.toSet())) {
                damageEntity(entity);
                //IronsSpellbooks.LOGGER.info(entity.getName().getString());
                MagicManager.spawnParticles(level, ParticleTypes.CHERRY_LEAVES, entity.getX(), entity.getY(), entity.getZ(), 50, 0, 0, 0, .5, true);
                if (entity instanceof ShieldPart || entity instanceof AbstractShieldEntity) {
                    discard();
                    return;
                }
            }
        }

        setPos(position().add(getDeltaMovement()));
        spawnParticles();
    }

    public EntityDimensions getDimensions(Pose p_19721_) {
        //irons_spellbooks.LOGGER.info("Accessing Blood Slash Dimensions. Age: {}", age);
        this.getBoundingBox();
        return EntityDimensions.scalable(this.getRadius() * 2.0F, 0.5F);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> p_19729_) {
        //irons_spellbooks.LOGGER.info("onSynchedDataUpdated");

        if (DATA_RADIUS.equals(p_19729_)) {
            this.refreshDimensions();
        }

        super.onSyncedDataUpdated(p_19729_);
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        discard();
    }

    private void damageEntity(Entity entity) {
        if (!victims.contains(entity)) {
            DamageSources.applyDamage(entity, damage, PbSpellRegistry.FLOWER_SLASH_SPELL.get().getDamageSource(this, getOwner()));
            victims.add(entity);
        }
    }

    //https://forge.gemwire.uk/wiki/Particles
    public void spawnParticles() {
        if (level.isClientSide) {
            float width = (float) getBoundingBox().getXsize();
            float step = 0.5f;
            float radians = Mth.DEG_TO_RAD * getYRot();
            float speed = 0.1f;

            int extraParticles = 3 + level.random.nextInt(4);

            for (int i = 0; i < width / step + extraParticles; i++) {
                if (Math.random() < 0.8) {
                    double x = getX();
                    double y = getY();
                    double z = getZ();

                    double offsetX = (i - width / step / 2.0) * step;
                    offsetX += (Math.random() - 0.5) * 0.5;

                    double offsetY = (Math.random() - 0.5) * width; // Random vertical spread
                    double offsetZ = (Math.random() - 0.5) * width; // Slight Z randomization too

                    double rotX = offsetX * Math.cos(radians) - offsetZ * Math.sin(radians);
                    double rotZ = offsetX * Math.sin(radians) + offsetZ * Math.cos(radians);

                    double dx = (Math.random() - 0.5) * speed * 2;
                    double dy = (Math.random() - 0.5) * speed * 2;
                    double dz = (Math.random() - 0.5) * speed * 2;

                    level.addParticle(
                            ParticleTypes.CHERRY_LEAVES,
                            false,
                            x + rotX,
                            y + offsetY,
                            z + rotZ,
                            dx,
                            dy,
                            dz
                    );
                }
            }
        }
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return entity != getOwner() && super.canHitEntity(entity);
    }

    @Override
    public void onAntiMagic(MagicData playerMagicData) {
        this.discard();
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putFloat("Damage", this.damage);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.damage = pCompound.getFloat("Damage");
    }
}
