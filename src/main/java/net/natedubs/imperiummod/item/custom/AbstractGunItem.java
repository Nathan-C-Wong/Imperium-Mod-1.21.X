package net.natedubs.imperiummod.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class AbstractGunItem extends Item {

    protected abstract double getRange();
    protected abstract ParticleEffect getParticleEffect();
    protected abstract RegistryEntry<SoundEvent> getSoundEffect();

    protected void onEntityHit(World world, PlayerEntity user, LivingEntity target) {}
    protected void onBlockHit(World world, PlayerEntity user, BlockPos pos) {}
    protected void onMiss(World world, PlayerEntity user, Vec3d end) {}

    public AbstractGunItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            fire(world, user);
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    protected void fire(World world, PlayerEntity user) {
        Vec3d start = user.getCameraPosVec(1.0f);
        Vec3d direction = user.getRotationVec(1.0f);
        Vec3d end = start.add(direction.multiply(getRange()));

        HitResult hitResult = user.raycast(getRange(), 1.0f, false);

        spawnTrail(world, start, end);
        playFireSound(world, user);

        switch(hitResult.getType()) {
            case ENTITY -> {
                EntityHitResult entityHit = (EntityHitResult)hitResult;
                if (entityHit.getEntity() instanceof LivingEntity livingEntity) {
                    onEntityHit(world, user, livingEntity);
                }
            }
            case BLOCK -> {
                BlockPos blockPos = ((BlockHitResult)hitResult).getBlockPos();
                onBlockHit(world, user, blockPos);
            }
            case MISS -> onMiss(world, user, end);
        }
    }

    protected void spawnTrail(World world, Vec3d start, Vec3d end) {
        Vec3d direction = end.subtract(start).normalize();
        double length = start.distanceTo(end);

        for (double i = 0; i < length; i+=0.15) {
            Vec3d position = start.add(direction.multiply(i));
            ((ServerWorld)world).spawnParticles(
                    getParticleEffect(),
                    position.x,
                    position.y,
                    position.z,
                    1,
                    0,0,0,
                    0
            );
        }
    }

    // The sound that is played
    protected void playFireSound(World world, PlayerEntity user) {
        world.playSound(
                null,
                user.getX(),
                user.getY(),
                user.getZ(),
                getSoundEffect(),
                SoundCategory.PLAYERS,
                2.0f,
                0.7f
        );
    }
}
