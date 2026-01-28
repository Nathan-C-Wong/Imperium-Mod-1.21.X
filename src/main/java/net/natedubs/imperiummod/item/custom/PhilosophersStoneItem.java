package net.natedubs.imperiummod.item.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public class PhilosophersStoneItem extends Item {

    // Item text
    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("[Right click] to transmute certain").formatted(Formatting.DARK_RED));
        tooltip.add(Text.translatable("blocks into other related blocks.").formatted(Formatting.DARK_RED));
        tooltip.add(Text.translatable("").formatted());
        tooltip.add(Text.translatable("Craft with redstone to repair").formatted(Formatting.GOLD));
    }

    public static final Map<Block, Block> BLOCK_MAP =
            Map.ofEntries(
                    // Stone group
                    Map.entry(Blocks.COBBLESTONE, Blocks.STONE),
                    Map.entry(Blocks.STONE, Blocks.COBBLESTONE),

                    // Stone Bricks
                    Map.entry(Blocks.STONE_BRICKS, Blocks.MOSSY_STONE_BRICKS),
                    Map.entry(Blocks.MOSSY_STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS),
                    Map.entry(Blocks.CRACKED_STONE_BRICKS, Blocks.CHISELED_STONE_BRICKS),
                    Map.entry(Blocks.CHISELED_STONE_BRICKS, Blocks.STONE_BRICKS),

                    // Dirt group
                    Map.entry(Blocks.DIRT, Blocks.GRASS_BLOCK),
                    Map.entry(Blocks.GRASS_BLOCK, Blocks.SAND),
                    Map.entry(Blocks.SAND, Blocks.GRAVEL),
                    Map.entry(Blocks.GRAVEL, Blocks.DIRT),


                    // Diorite, Andesite, Granite group
                    Map.entry(Blocks.DIORITE, Blocks.ANDESITE),
                    Map.entry(Blocks.ANDESITE, Blocks.GRANITE),
                    Map.entry(Blocks.GRANITE, Blocks.DIORITE),


                    // Grass and Plant group
                    Map.entry(Blocks.SHORT_GRASS, Blocks.FERN),
                    Map.entry(Blocks.FERN, Blocks.DEAD_BUSH),
                    Map.entry(Blocks.DEAD_BUSH, Blocks.CACTUS),
                    Map.entry(Blocks.CACTUS, Blocks.SUGAR_CANE),
                    Map.entry(Blocks.SUGAR_CANE, Blocks.BAMBOO),
                    Map.entry(Blocks.BAMBOO, Blocks.AZALEA),
                    Map.entry(Blocks.AZALEA, Blocks.SWEET_BERRY_BUSH),
                    Map.entry(Blocks.SWEET_BERRY_BUSH, Blocks.SHORT_GRASS),

                    // Logs
                    Map.entry(Blocks.OAK_LOG, Blocks.BIRCH_LOG),
                    Map.entry(Blocks.BIRCH_LOG, Blocks.SPRUCE_LOG),
                    Map.entry(Blocks.SPRUCE_LOG, Blocks.ACACIA_LOG),
                    Map.entry(Blocks.ACACIA_LOG, Blocks.JUNGLE_LOG),
                    Map.entry(Blocks.JUNGLE_LOG, Blocks.DARK_OAK_LOG),
                    Map.entry(Blocks.DARK_OAK_LOG, Blocks.CHERRY_LOG),
                    Map.entry(Blocks.CHERRY_LOG, Blocks.MANGROVE_LOG),
                    Map.entry(Blocks.MANGROVE_LOG, Blocks.OAK_LOG),

                    // Nether Logs
                    Map.entry(Blocks.CRIMSON_STEM, Blocks.WARPED_STEM),
                    Map.entry(Blocks.WARPED_STEM, Blocks.CRIMSON_STEM),

                    // Planks
                    Map.entry(Blocks.OAK_PLANKS, Blocks.BIRCH_PLANKS),
                    Map.entry(Blocks.BIRCH_PLANKS, Blocks.SPRUCE_PLANKS),
                    Map.entry(Blocks.SPRUCE_PLANKS, Blocks.ACACIA_PLANKS),
                    Map.entry(Blocks.ACACIA_PLANKS, Blocks.JUNGLE_PLANKS),
                    Map.entry(Blocks.JUNGLE_PLANKS, Blocks.DARK_OAK_PLANKS),
                    Map.entry(Blocks.DARK_OAK_PLANKS, Blocks.CHERRY_PLANKS),
                    Map.entry(Blocks.CHERRY_PLANKS, Blocks.MANGROVE_PLANKS),
                    Map.entry(Blocks.MANGROVE_PLANKS, Blocks.OAK_PLANKS),

                    // One Block Tall Flowers
                    Map.entry(Blocks.ALLIUM, Blocks.AZURE_BLUET),
                    Map.entry(Blocks.AZURE_BLUET, Blocks.BLUE_ORCHID),
                    Map.entry(Blocks.BLUE_ORCHID, Blocks.CORNFLOWER),
                    Map.entry(Blocks.CORNFLOWER, Blocks.DANDELION),
                    Map.entry(Blocks.DANDELION, Blocks.LILY_OF_THE_VALLEY),
                    Map.entry(Blocks.LILY_OF_THE_VALLEY, Blocks.OXEYE_DAISY),
                    Map.entry(Blocks.OXEYE_DAISY, Blocks.POPPY),
                    Map.entry(Blocks.POPPY, Blocks.ORANGE_TULIP),
                    Map.entry(Blocks.ORANGE_TULIP, Blocks.PINK_TULIP),
                    Map.entry(Blocks.PINK_TULIP, Blocks.RED_TULIP),
                    Map.entry(Blocks.RED_TULIP, Blocks.ALLIUM),

                    // Mushrooms
                    Map.entry(Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM),
                    Map.entry(Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM),

                    // Mushroom Blocks
                    Map.entry(Blocks.RED_MUSHROOM_BLOCK, Blocks.BROWN_MUSHROOM_BLOCK),
                    Map.entry(Blocks.BROWN_MUSHROOM_BLOCK, Blocks.RED_MUSHROOM_BLOCK),

                    // Two Block Tall Flowers
                    Map.entry(Blocks.LILAC, Blocks.PEONY),
                    Map.entry(Blocks.PEONY, Blocks.ROSE_BUSH),
                    Map.entry(Blocks.ROSE_BUSH, Blocks.SUNFLOWER),
                    Map.entry(Blocks.SUNFLOWER, Blocks.LILAC),

                    // Crops
                    Map.entry(Blocks.WHEAT, Blocks.POTATOES),
                    Map.entry(Blocks.POTATOES, Blocks.CARROTS),
                    Map.entry(Blocks.CARROTS, Blocks.BEETROOTS),
                    Map.entry(Blocks.BEETROOTS, Blocks.NETHER_WART),
                    Map.entry(Blocks.NETHER_WART, Blocks.WHEAT),

                    // Stem Plants
                    Map.entry(Blocks.MELON_STEM, Blocks.PUMPKIN_STEM),
                    Map.entry(Blocks.PUMPKIN_STEM, Blocks.MELON_STEM),

                    // Pumpkin and Melon Blocks
                    Map.entry(Blocks.PUMPKIN, Blocks.MELON),
                    Map.entry(Blocks.MELON, Blocks.PUMPKIN)
            );

    public static final List<Block> DOUBLE_TALL_FLOWERS = List.of(Blocks.LILAC, Blocks.PEONY, Blocks.ROSE_BUSH, Blocks.SUNFLOWER);

    public PhilosophersStoneItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        World world = context.getWorld();
        Block affectedBlock = world.getBlockState(context.getBlockPos()).getBlock();

        if (BLOCK_MAP.containsKey(affectedBlock)) {
            if (!world.isClient()) {

                ItemStack itemStack = context.getStack();

                // So the item will not be usable at 1 durability (to prevent from breaking)
                if (itemStack.getDamage() >= itemStack.getMaxDamage() - 1) {
                    return ActionResult.PASS;
                }

                // For Double Tall Flowers
                if (DOUBLE_TALL_FLOWERS.contains(affectedBlock)) {

                    BlockPos pos = context.getBlockPos();
                    BlockState oldState = world.getBlockState(pos);

                    // Determine which half of the plant was clicked
                    DoubleBlockHalf half = oldState.get(TallPlantBlock.HALF);

                    // Get position of lower half
                    BlockPos basePos = (half == DoubleBlockHalf.LOWER)
                            ? pos
                            : pos.down();

                    Block next = BLOCK_MAP.get(oldState.getBlock());

                    // Remove old flower (both halves)
                    world.removeBlock(basePos, false);
                    world.removeBlock(basePos.up(), false);

                    // Plant the flower
                    TallPlantBlock.placeAt(world, next.getDefaultState(), basePos, 3);

                } else {
                    world.setBlockState(context.getBlockPos(), BLOCK_MAP.get(affectedBlock).getDefaultState());
                }

                itemStack.damage(
                        1,
                        ((ServerWorld) world),
                        ((ServerPlayerEntity) context.getPlayer()),
                        item -> context.getPlayer().sendEquipmentBreakStatus(item, EquipmentSlot.MAINHAND)
                );

                world.playSound(null, context.getBlockPos(), SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.BLOCKS);
            }
         }

        return ActionResult.SUCCESS;
    }
}
