package net.natedubs.imperiummod.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class HuntingRifleItem extends AbstractGunItem {

    public HuntingRifleItem(Settings settings) {
        super(settings);
    }

    @Override
    protected double getRange() {
        return 300;
    }

    @Override
    protected ParticleEffect getParticleEffect() {
        return ParticleTypes.SMOKE;
    }

    @Override
    protected RegistryEntry<SoundEvent> getSoundEffect() {
        return SoundEvents.ENTITY_GENERIC_EXPLODE;
    }

    @Override
    protected void onEntityHit(World world, PlayerEntity user, LivingEntity target) {
        if (world.isClient) return;

        target.addStatusEffect(
                //new StatusEffectInstance(StatusEffects.GLOWING, 100, 0)
                new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1, 1)
        );
    }

    @Override
    protected void onBlockHit(World world, PlayerEntity user, BlockPos pos, HitResult hit) {
        if (world.isClient) return;
        if (world.getBlockState(pos).isAir()) return;

        Boolean transparent = world.getBlockState(pos).isTransparent(world, pos);

        if (!transparent) {
            spawnTrail(world, user.getCameraPosVec(1.0f), hit.getPos());
        } else {
            Vec3d blockVec3 = hit.getPos();
            spawnTrail(world, user.getCameraPosVec(1.0f), blockVec3);
        }

        // Block Collision Particle Effect
        Vec3d center = Vec3d.ofCenter(pos);
        ((ServerWorld)world).spawnParticles(
                new BlockStateParticleEffect(ParticleTypes.BLOCK, world.getBlockState(pos)),
                center.getX(),
                center.getY(),
                center.getZ(),
                50,
                0.5,0.5,0.5,
                0.1
        );
    }

}
