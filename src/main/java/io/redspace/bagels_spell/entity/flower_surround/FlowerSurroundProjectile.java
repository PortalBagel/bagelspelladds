package io.redspace.bagels_spell.entity.flower_surround;

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

public class FlowerSurroundProjectile extends Projectile implements AntiMagicSusceptible {
    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(FlowerSurroundProjectile.class, EntityDataSerializers.FLOAT);
    private static final double SPEED = 1d;
    private static final int EXPIRE_TIME = 1 * 3;
    public final int animationSeed;
    private final float maxRadius;
    public AABB oldBB;
    private int age;
    private float damage;
    public int animationTime;
    private List<Entity> victims;

    public FlowerSurroundProjectile(EntityType<? extends FlowerSurroundProjectile> entityType, Level level) {
        super(entityType, level);
        animationSeed = Utils.random.nextInt(9999);

        setRadius(.3f);
        maxRadius = 3;
        oldBB = getBoundingBox();
        victims = new ArrayList<>();
        this.setNoGravity(true);
    }

    public FlowerSurroundProjectile(EntityType<? extends FlowerSurroundProjectile> entityType, Level levelIn, LivingEntity shooter) {
        this(entityType, levelIn);
        setOwner(shooter);
        setYRot(shooter.getYRot());
        setXRot(shooter.getXRot());
    }

    public FlowerSurroundProjectile(Level levelIn, LivingEntity shooter) {
        this(PbEntityRegistry.FLOWER_SURROUND_PROJECTILE.get(), levelIn, shooter);
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
        setRadius(getRadius() + 0.2f);

        if (!level.isClientSide) {
            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() == HitResult.Type.BLOCK) {
                onHitBlock((BlockHitResult) hitresult);
            }
            float outerRadius = getRadius()+2;        // expanding radius (like slashes)
            float innerRadius = 0f;
            double minY = getY() - 1;
            double maxY = getY() + 2;

            AABB searchBox = new AABB(getX() - outerRadius, minY, getZ() - outerRadius,
                    getX() + outerRadius, maxY, getZ() + outerRadius);

            for (Entity entity : level.getEntitiesOfClass(LivingEntity.class, searchBox)) {
                if (canHitEntity(entity) && !victims.contains(entity)) {
                    double dx = entity.getX() - getX();
                    double dz = entity.getZ() - getZ();
                    double distanceSq = dx * dx + dz * dz;

                    if (distanceSq >= innerRadius * innerRadius && distanceSq <= outerRadius * outerRadius) {
                        damageEntity(entity);
                        MagicManager.spawnParticles(level, ParticleTypes.CHERRY_LEAVES,
                                entity.getX(), entity.getY(), entity.getZ(), 50, 0, 0, 0, .5, true);

                        if (entity instanceof ShieldPart || entity instanceof AbstractShieldEntity) {
                            discard();
                            return;
                        }
                    }
                }
            }
        }

        setPos(position().add(getDeltaMovement()));
        spawnParticles();
    }

    public EntityDimensions getDimensions(Pose p_19721_) {
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
            DamageSources.applyDamage(entity, damage, PbSpellRegistry.FLOWER_SURROUND_SPELL.get().getDamageSource(this, getOwner()));
            victims.add(entity);
        }
    }

    //https://forge.gemwire.uk/wiki/Particles
    public void spawnParticles() {
        /*if (level.isClientSide) {

            float width = (float) getBoundingBox().getXsize();
            float step = .5f;
            float radians = Mth.DEG_TO_RAD * getYRot();
            float speed = .3f;
            for (int i = 0; i < width / step; i++) {
                double x = getX();
                double y = getY();
                double z = getZ();
                double offset = step * (i - width / step / 2);
                double rotX = offset * Math.cos(radians);
                double rotZ = -offset * Math.sin(radians);

                double dx = Math.random() * speed * 2 - speed;
                double dy = Math.random() * speed * 2 - speed;
                double dz = Math.random() * speed * 2 - speed;
                level.addParticle(ParticleTypes.CHERRY_LEAVES, false, x + rotX + dx, y + dy, z + rotZ + dz, dx, dy, dz);
            }
        }*/
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
