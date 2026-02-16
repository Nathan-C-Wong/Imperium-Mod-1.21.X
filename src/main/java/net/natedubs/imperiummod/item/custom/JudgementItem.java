package net.natedubs.imperiummod.item.custom;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class JudgementItem extends Item {

    public JudgementItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    // Item text
    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("You are the Judge, Jury and Executioner.").formatted(Formatting.BLUE));
        tooltip.add(Text.translatable("").formatted());
        tooltip.add(Text.translatable("[Right click] to Smite").formatted(Formatting.GOLD));
        tooltip.add(Text.translatable("").formatted());
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        // Ensure it doesn't only apply to client
        // Used to prevents desync
        if(world.isClient) {
            return TypedActionResult.pass(user.getStackInHand(hand));
        }

        double maxReach = 300; //The farthest target the cameraEntity can detect
        float tickDelta = 1.0F; //Used for tracking animation progress; no tracking is 1.0F
        boolean includeFluids = false; //Whether to detect fluids as blocks

        HitResult hit = user.raycast(maxReach, tickDelta, includeFluids);

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
            case ENTITY:  // Will fix later (doesnt work with user.Raycast)
                EntityHitResult entityHit = (EntityHitResult) hit;
                Entity entity = entityHit.getEntity();
                BlockPos entityPos = entity.getBlockPos();
                coord = entityPos;

                break;
        }

        if (coord != null) {
            ItemStack itemStack = user.getStackInHand(hand);

            // So the item will not be usable at 1 durability (to prevent from breaking)
            if (itemStack.getDamage() >= itemStack.getMaxDamage() - 1) {
                return TypedActionResult.pass(user.getStackInHand(hand));
            }

            itemStack.damage(
                    1,
                    ((ServerWorld) world),
                    ((ServerPlayerEntity) user),
                    item -> user.sendEquipmentBreakStatus(item, EquipmentSlot.MAINHAND)
            );

            // For explosion
            world.createExplosion(
                    null,
                    hit.getPos().getX(),  hit.getPos().getY(),  hit.getPos().getZ(),
                    2.4f,
                    false,
                    World.ExplosionSourceType.NONE
            );

            // Lightning Bolt
            LightningEntity lightningBolt = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
            lightningBolt.setPosition(coord.toCenterPos());
            world.spawnEntity(lightningBolt);

            // Particles
            ((ServerWorld)world).spawnParticles(
                    ParticleTypes.ENCHANT,
                    coord.toCenterPos().getX(),
                    coord.toCenterPos().getY() + 1,
                    coord.toCenterPos().getZ(),
                    100,
                    1,1,1,
                    0.2
            );

        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient) return;
        if (!(entity instanceof PlayerEntity)) return;
        if (!stack.isDamaged()) return;

        long now = world.getTime();

        // store per-item timer
        NbtCompound nbt = stack.getOrDefault(
                DataComponentTypes.CUSTOM_DATA,
                NbtComponent.DEFAULT
        ).copyNbt();

        long nextRepair = nbt.getLong("nextRepair");

        if (now < nextRepair) return;

        // repair item
        if (stack.isDamaged()) {
            stack.setDamage(stack.getDamage() - 1);
        }

        // schedule next repair in 1 second
        nbt.putLong("nextRepair", now + 20);
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));

    }
}
