package net.natedubs.imperiummod.item.custom;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HeavensVerdictItem extends Item {

    public HeavensVerdictItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        // Ensure it doesn't only apply to client
        // Used to prevents desync
        if(world.isClient) {
            return TypedActionResult.pass(user.getStackInHand(hand));
        }

        MinecraftClient client = MinecraftClient.getInstance();

        double maxReach = 300; //The farthest target the cameraEntity can detect
        float tickDelta = 1.0F; //Used for tracking animation progress; no tracking is 1.0F
        boolean includeFluids = false; //Whether to detect fluids as blocks

        HitResult hit = client.cameraEntity.raycast(maxReach, tickDelta, includeFluids);

        // Coordinate for lightningbolt
        BlockPos coord = null;

        switch(hit.getType()) {
            case MISS:
                break;
            case BLOCK:
                BlockHitResult blockHit = (BlockHitResult) hit;
                BlockPos blockPos = blockHit.getBlockPos();
                coord = blockPos;
                break;
            case ENTITY:
                EntityHitResult entityHit = (EntityHitResult) hit;
                Entity entity = entityHit.getEntity();
                BlockPos entityPos = entity.getBlockPos();
                coord = entityPos;

                break;
        }

        if (coord != null) {

            ItemStack itemStack = user.getStackInHand(hand);
            itemStack.damage(1, ((ServerWorld) world), ((ServerPlayerEntity) user), item -> {
                user.sendEquipmentBreakStatus(item, EquipmentSlot.MAINHAND);
            });

            LightningEntity lightningBolt = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
            lightningBolt.setPosition(coord.toCenterPos());
            world.spawnEntity(lightningBolt);

            // For explosion
            world.createExplosion(user, coord.toCenterPos().getX(), coord.toCenterPos().getY(), coord.toCenterPos().getZ(), 0, false, World.ExplosionSourceType.TNT);

        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
