package net.natedubs.imperiummod.item.custom;

import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
                new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1, 1)
        );

        summonFireWork(world, target);
    }

    @Override
    protected void onBlockHit(World world, PlayerEntity user, BlockPos pos, HitResult hit) {
        if (world.isClient) return;
        if (world.getBlockState(pos).isAir()) return;

        Boolean transparent = world.getBlockState(pos).isTransparent(world, pos);
        Vec3d verticalOffset = new Vec3d(0,0.5,0);
        Vec3d start = user.getCameraPosVec(1.0f).subtract(verticalOffset);

        if (!transparent) {
            spawnTrail(world, start, hit.getPos());
        } else {
            Vec3d blockVec3 = hit.getPos();
            spawnTrail(world, start, blockVec3);
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

    protected void summonFireWork(World world, LivingEntity target) {
        ItemStack fireworkStack = new ItemStack(Items.FIREWORK_ROCKET);

        FireworkRocketEntity fireworkRocketEntity = new FireworkRocketEntity(
                world,
                target.getPos().x,
                target.getPos().y ,
                target.getPos().z,
                fireworkStack
        );

        world.spawnEntity(fireworkRocketEntity);
    }

    @Override
    protected void createBloodParticles(World world, Vec3d hitLocation) {
        ((ServerWorld)world).spawnParticles(
                new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.getDefaultState()),
                hitLocation.getX(),
                hitLocation.getY(),
                hitLocation.getZ(),
                50,
                0.1,0.1,0.1,
                0.5
        );
    }
}
