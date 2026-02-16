package net.natedubs.imperiummod.item.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.natedubs.imperiummod.damage.ModDamageTypes;

import java.util.List;

public class RayGunItem extends AbstractGunItem {

    public RayGunItem(Settings settings) {
        super(settings);
    }

    // Item text
    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("Emits a high energy pulse that destabilizes cells at an atomic level").formatted(Formatting.GREEN));
        tooltip.add(Text.translatable("[Right click] to shoot").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("").formatted());
        tooltip.add(Text.translatable("Damage: 15 hearts + explosion").formatted(Formatting.RED));
        tooltip.add(Text.translatable("Cooldown: " + getCooldown()/20 + " Seconds").formatted(Formatting.AQUA));
        tooltip.add(Text.translatable("Range: " + getRange() + " Blocks").formatted(Formatting.BLUE));
    }

    @Override
    protected double getRange() {
        return 150;
    }

    @Override
    protected ParticleEffect getParticleEffect() {
        return ParticleTypes.GLOW;
    }

    @Override
    protected RegistryEntry<SoundEvent> getSoundEffect() {
        return Registries.SOUND_EVENT.getEntry(SoundEvents.BLOCK_BEACON_ACTIVATE);
    }

    @Override
    protected boolean canPenetrate(BlockState state) {
        return state.isIn(BlockTags.LEAVES);
    }

    @Override
    protected int getCooldown() {
        return 1 * 20;
    }


    @Override
    protected void onEntityHit(World world, PlayerEntity user, LivingEntity target) {
        if (world.isClient) return;

        target.damage(ModDamageTypes.of(world, ModDamageTypes.RAY_GUN_DAMAGE, user), 30);

        // Effects given to entities hit
        if (target.getType().isIn(EntityTypeTags.UNDEAD)) {
            target.addStatusEffect(
                    new StatusEffectInstance(StatusEffects.SLOWNESS, 20*3, 3, true, false)
            );
        } else {
            target.addStatusEffect(
                    new StatusEffectInstance(StatusEffects.HUNGER, 20*1, 3, true, false)
            );
        }

        // Explosion at entity
        if (target.getHeight() >= 2) {  // So tall mobs dont get launched up
            world.createExplosion(
                    null,
                    target.getPos().x, target.getPos().y+2, target.getPos().z,
                    1.0f,
                    false,
                    World.ExplosionSourceType.NONE
            );
        } else {
            world.createExplosion(
                    null,
                    target.getPos().x, target.getPos().y+1, target.getPos().z,
                    1.0f,
                    false,
                    World.ExplosionSourceType.NONE
            );
        }

    }

    @Override
    protected void onBlockHit(World world, PlayerEntity user, BlockPos pos, HitResult hit) {
        if (world.isClient) return;
        if (world.getBlockState(pos).isAir()) return;

        Boolean transparent = world.getBlockState(pos).isTransparent(world, pos);
        Vec3d verticalOffset = new Vec3d(0,0.5,0);
        Vec3d start = user.getCameraPosVec(1.0f).subtract(verticalOffset);

        spawnTrail(world, start, hit.getPos());

        Vec3d exploPos = hit.getPos();
        world.createExplosion(
                null,
                exploPos.getX(), exploPos.getY(), exploPos.getZ(),
                1.4f,
                false,
                World.ExplosionSourceType.NONE
        );

        // Block Collision Particle Effect
        Vec3d center = Vec3d.ofCenter(pos);
        ((ServerWorld)world).spawnParticles(
                new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.EMERALD_BLOCK.getDefaultState()),
                center.getX(),
                center.getY(),
                center.getZ(),
                200,
                0.7,0.7,0.7,
                2
        );
    }

    @Override
    protected void createEntityHitParticles(World world, Vec3d hitLocation) {
        ((ServerWorld)world).spawnParticles(
                new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.EMERALD_BLOCK.getDefaultState()),
                hitLocation.getX(),
                hitLocation.getY(),
                hitLocation.getZ(),
                100,
                0.1,0.1,0.1,
                2
        );
    }

}
