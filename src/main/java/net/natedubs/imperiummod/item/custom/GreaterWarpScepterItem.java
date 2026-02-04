package net.natedubs.imperiummod.item.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class GreaterWarpScepterItem extends Item {

    public GreaterWarpScepterItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    // Item text
    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("A superior version of the Warp Scepter").formatted(Formatting.BLUE));
        tooltip.add(Text.translatable("").formatted());
        tooltip.add(Text.translatable("").formatted());
        tooltip.add(Text.translatable("[Right click] to teleport to a block").formatted(Formatting.DARK_PURPLE));
        tooltip.add(Text.translatable("Block must have room above it").formatted(Formatting.LIGHT_PURPLE));
        tooltip.add(Text.translatable("").formatted());
        tooltip.add(Text.translatable("[Left click] to propel forward").formatted(Formatting.DARK_PURPLE));
        tooltip.add(Text.translatable("").formatted());
        tooltip.add(Text.translatable("Infinite durability, more range, and no cooldown").formatted(Formatting.AQUA));
    }

    // On Right Click
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        if(world.isClient) {
            return TypedActionResult.pass(user.getStackInHand(hand));
        }

        double maxReach = 200; //The farthest target the cameraEntity can detect
        float tickDelta = 1.0F; //Used for tracking animation progress; no tracking is 1.0F
        boolean includeFluids = false; //Whether to detect fluids as blocks

//        MinecraftClient client = MinecraftClient.getInstance();
//        HitResult hit = client.cameraEntity.raycast(maxReach, tickDelta, includeFluids);
        HitResult hit = user.raycast(maxReach, tickDelta, includeFluids);

        BlockPos teleportPos = null;

        switch(hit.getType()) {
            case MISS:
                break;
            case BLOCK:
                // First block up
                BlockHitResult blockHit = (BlockHitResult) hit;
                BlockPos blockPos = blockHit.getBlockPos().up();
                BlockState blockState = world.getBlockState(blockPos);
                Block block = blockState.getBlock(); // One block up from raycast

                // Second block up
                BlockPos blockPos1 = blockPos.up();
                BlockState blockState1 = world.getBlockState(blockPos1);
                Block block1 = blockState1.getBlock();

                if (block == Blocks.AIR && block1 == Blocks.AIR) {
                    teleportPos = blockPos;
                }

                break;
            case ENTITY:
                EntityHitResult entityHitResult = (EntityHitResult) hit;
                Entity entity = entityHitResult.getEntity();
                break;
        }

        // If teleport is possible
        if (teleportPos != null) {
            Vec3d vecPos = teleportPos.toCenterPos();  // toCenterPos returns a Vec3d instead of a BlockPos
            StatusEffectInstance resStatusEffect = new StatusEffectInstance(StatusEffects.RESISTANCE, 10, 4, false, false);
            StatusEffectInstance slowFallstatusEffect = new StatusEffectInstance(StatusEffects.SLOW_FALLING, 10, 0, false, false);
            user.addStatusEffect(resStatusEffect);
            user.addStatusEffect(slowFallstatusEffect);
            user.teleport(vecPos.getX(), teleportPos.getY(), vecPos.getZ(), true);  // Center x and z but not y (center y makes you float)
            world.playSound(null, teleportPos, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.HOSTILE);
        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }



}
