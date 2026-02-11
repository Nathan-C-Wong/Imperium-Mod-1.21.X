package net.natedubs.imperiummod.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
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
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class AbstractGunItem extends Item {

    protected abstract double getRange();
    protected abstract ParticleEffect getParticleEffect();
    protected abstract RegistryEntry<SoundEvent> getSoundEffect();

    protected void onEntityHit(World world, PlayerEntity user, LivingEntity target) {}
    protected void onBlockHit(World world, PlayerEntity user, BlockPos pos, HitResult hit) {}
    protected void onMiss(World world, PlayerEntity user, Vec3d end) {}
    protected void createBloodParticles(World world, Vec3d hitLocation) {}

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
        Vec3d verticalOffset = new Vec3d(0,0.5,0);
        Vec3d start = user.getCameraPosVec(1.0f).subtract(verticalOffset);
        Vec3d direction = user.getRotationVec(1.0f);
        Vec3d end = start.add(verticalOffset).add(direction.multiply(getRange()));

        HitResult hitResult = user.raycast(getRange(), 1.0f, false);

        playFireSound(world, user);

        // Creates box for raycasting (side length: 0.2)
        Box box = new Box(start, start).expand(0.1);

        // Detecting mob
        EntityHitResult entityHitResult = ProjectileUtil.getEntityCollision(
                world,
                user,
                start,
                end,
                box.stretch(direction.multiply(300.0)),
                entity -> entity instanceof LivingEntity && entity != user
        );

        // Hitting an entity
        if (entityHitResult != null) {
            LivingEntity livingEntity = ((LivingEntity)entityHitResult.getEntity());

            Vec3d playerToTarget = entityHitResult.getPos().subtract(start);
            double lengthToTarget = playerToTarget.length();

            Vec3d targetHit = start.add(direction.multiply(lengthToTarget));

            spawnTrail(world, start, targetHit);
            createBloodParticles(world, targetHit);
            onEntityHit(world, user, livingEntity);
            return;
        }

        // Hitting a block or not hitting anything
        switch(hitResult.getType()) {
            case BLOCK -> {
                BlockPos blockPos = ((BlockHitResult)hitResult).getBlockPos();
                onBlockHit(world, user, blockPos, hitResult);
            }
            case MISS -> {
                spawnTrail(world, start, end);
                onMiss(world, user, end);
            }
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
