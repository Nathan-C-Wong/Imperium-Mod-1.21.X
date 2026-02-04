package net.natedubs.imperiummod.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class RejuvenationScepterItem extends Item {

    public RejuvenationScepterItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return super.hasGlint(stack);
    }

    // Item text
    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("This scepter emits a strong healing aura").formatted(Formatting.BLUE));
        tooltip.add(Text.translatable("").formatted());
        tooltip.add(Text.translatable("[Right click] to regenerate your health and hunger").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("").formatted());
        tooltip.add(Text.translatable("[Shift + Right Click] to heal another entity").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("").formatted());
        tooltip.add(Text.translatable("5 second cooldown for self heal").formatted(Formatting.AQUA));
        tooltip.add(Text.translatable("1 second cooldown for healing another entity").formatted(Formatting.AQUA));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        if (!world.isClient) {
            // Shift while right clicking heals another entity
            if (user.isSneaking()) {
                // user.raycast does not detect entities so we have
                // to do it this way
                Vec3d start = user.getCameraPosVec(1.0f);
                Vec3d direction = user.getRotationVec(1.0f);
                Vec3d end = start.add(direction.multiply(5.0f));

                EntityHitResult entityHit = ProjectileUtil.getEntityCollision(
                        world,
                        user,
                        start,
                        end,
                        user.getBoundingBox().stretch(direction.multiply(5.0)),
                        entity -> entity instanceof LivingEntity && entity != user
                );

                //System.out.println("Box: " + user.getBoundingBox().toString());

                if (entityHit != null) {
                    LivingEntity entity = (LivingEntity) entityHit.getEntity();


                    entity.addStatusEffect(
                            new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, 1)
                    );
                    entity.addStatusEffect(
                            new StatusEffectInstance(StatusEffects.SATURATION, 20, 1)
                    );

                    // Healing beam particles
                    Vec3d target = entity.getCameraPosVec(1.0f);
                    Vec3d beamDirection = target.subtract(start);
                    Vec3d normBeamDir = beamDirection.normalize();
                    double points = beamDirection.length();

                    for (double i = 0; i < points; i+=0.25) {
                        Vec3d position = start.add(normBeamDir.multiply(i));

                        ((ServerWorld) world).spawnParticles(
                                ParticleTypes.HAPPY_VILLAGER,
                                position.x,
                                position.y,
                                position.z,
                                1,
                                0, 0, 0,
                                0.02
                        );
                    }

                    // Heart Particles
                    ((ServerWorld) world).spawnParticles(
                            ParticleTypes.HEART,
                            entity.getX(),
                            entity.getBodyY(0.5),
                            entity.getZ(),
                            5,
                            0.2, 0.3, 0.2,
                            0.02
                    );

                    user.getItemCooldownManager().set(this, 20);
                }

                return TypedActionResult.pass(user.getStackInHand(hand));
            }

            StatusEffectInstance regenerationInstance = new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, 2);
            StatusEffectInstance saturationInstance = new StatusEffectInstance(StatusEffects.SATURATION, 1, 10);

            user.addStatusEffect(regenerationInstance);
            user.addStatusEffect(saturationInstance);

            // 5 second cooldown
            user.getItemCooldownManager().set(this, 5*20);
        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
