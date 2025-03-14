package io.redspace.bagels_spell.spells;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.item.NuclearBombEntity;
import com.github.alexmodguy.alexscaves.server.entity.item.NuclearExplosionEntity;
import io.redspace.bagels_spell.BagelsSpell;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class NukeSpell extends AbstractSpell {
    private final ResourceLocation spellId = new ResourceLocation(BagelsSpell.MODID, "nuke");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(
                //Component.translatable("ui.irons_example_mod.radius", Utils.stringTruncation(getSpellPower(spellLevel, caster), 1))
        );
    }

    public NukeSpell() {
        this.manaCostPerLevel = 100;
        this.baseSpellPower = 6;
        this.spellPowerPerLevel = 1;
        this.castTime = 200;
        this.baseManaCost = 500;
        //hi
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.LEGENDARY)
            .setSchoolResource(SchoolRegistry.FIRE_RESOURCE)
            .setMaxLevel(3)
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
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        HitResult raycast = Utils.raycastForEntity(level, entity, 50 * 1.5f, true);
        MagicNuclearBombEntity bomb = new MagicNuclearBombEntity(level, raycast.getLocation().x, raycast.getLocation().y, raycast.getLocation().z, playerMagicData.getCastingSpellLevel());
        bomb.explode();
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }


    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.CHARGE_ANIMATION;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return SpellAnimations.FINISH_ANIMATION;
    }

    public class MagicNuclearBombEntity extends NuclearBombEntity{
        private float spellPower;
        public MagicNuclearBombEntity(Level level, double x, double y, double z, float spellPower) {
            super(level, x, y, z);
            this.spellPower = spellPower;
        }

        //@Override
        public void explode() {
            NuclearExplosionEntity explosion = (NuclearExplosionEntity)((EntityType)ACEntityRegistry.NUCLEAR_EXPLOSION.get()).create(this.level());
            explosion.copyPosition(this);
            explosion.setSize(((Double) AlexsCaves.COMMON_CONFIG.nukeExplosionSizeModifier.get()).floatValue() * (1+(spellPower * 0.2f)));
            this.level().addFreshEntity(explosion);
        }
    }
}
