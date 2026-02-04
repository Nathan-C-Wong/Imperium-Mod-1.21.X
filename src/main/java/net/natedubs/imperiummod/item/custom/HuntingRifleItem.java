package net.natedubs.imperiummod.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class HuntingRifleItem extends Item {
    public HuntingRifleItem(Settings settings) {
        super(settings);
    }

    public void scopeOn(PlayerEntity user) {
        StatusEffectInstance scope = new StatusEffectInstance(StatusEffects.SLOWNESS, 20 * 999, 25);
        user.addStatusEffect(scope);
    }

    public void scopeOff(PlayerEntity user) {
        user.removeStatusEffect(StatusEffects.SLOWNESS);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        if (!world.isClient) {
            Vec3d start = user.getCameraPosVec(1.0f);  // (-13.525378382342131, 120.62000000476837, 1.4324871058871889)
            Vec3d direction = user.getRotationVec(1.0f);
            Vec3d end = start.add(direction.multiply(100.0f));

            Vec3d pos = user.getPos();
            //Box box = new Box();

            EntityHitResult entityHit = ProjectileUtil.getEntityCollision(
                    world,
                    user,
                    start,
                    end,
                    user.getBoundingBox().stretch(direction.multiply(100.0)),
                    entity -> entity instanceof LivingEntity && entity != user
            );

        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
