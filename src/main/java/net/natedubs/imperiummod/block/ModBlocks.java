package net.natedubs.imperiummod.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.natedubs.imperiummod.ImperiumMod;

public class ModBlocks {

    /*  ===================================================================================================================  */

    // Black Diamond Block
    public static final Block BLACK_DIAMOND_BLOCK = registerBlockItem("black_diamond_block",
            new Block(AbstractBlock.Settings.create().strength(4.0f, 1200.0F).requiresTool().sounds(BlockSoundGroup.AMETHYST_BLOCK)),
            true);

    public static final Block BLACK_DIAMOND_ORE = registerBlockItem("black_diamond_ore",
            new ExperienceDroppingBlock(
                    UniformIntProvider.create(3, 7),
                    AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 1200.0F)
            ),
            true);

    // Chaos Diamond Block
    public static final Block CHAOS_DIAMOND_BLOCK = registerBlockItem("chaos_diamond_block",
            new Block(AbstractBlock.Settings.create().strength(3f).requiresTool().sounds(BlockSoundGroup.AMETHYST_BLOCK)),
            true);

    public static final Block CHAOS_DIAMOND_ORE = registerBlockItem("chaos_diamond_ore",
            new ExperienceDroppingBlock(
                    UniformIntProvider.create(3, 7),
                    AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(3.0F, 3.0F)
            ),
            true);

    public static final Block CUBE_OF_CORRUPTION = registerBlockItem("cube_of_corruption",
            new ExperienceDroppingBlock(
                    UniformIntProvider.create(3, 7),
                    AbstractBlock.Settings.create().mapColor(MapColor.STONE_GRAY).sounds(BlockSoundGroup.AMETHYST_BLOCK).requiresTool().strength(30.0F, 1200.0F)
            ),
            true);

    /*  ===================================================================================================================  */

    private static Block registerBlockItem(String name, Block block, Boolean shouldRegisterItem) {
        // Register the block and its item
        Identifier blockID = Identifier.of(ImperiumMod.MOD_ID, name);

        // Registers items that should be registered (ex. something like minecraft:air would not be registered)
        if (shouldRegisterItem) {
            BlockItem blockItem = new BlockItem(block, new Item.Settings());
            Registry.register(Registries.ITEM, blockID, blockItem);
        }

        return Registry.register(Registries.BLOCK, blockID, block);
    }

    public static void registerModBlocks() {
        ImperiumMod.LOGGER.info("Registering Mod Blocks for " + ImperiumMod.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            entries.add(ModBlocks.BLACK_DIAMOND_BLOCK);
            entries.add(ModBlocks.BLACK_DIAMOND_ORE);
            entries.add(ModBlocks.CHAOS_DIAMOND_BLOCK);
            entries.add(ModBlocks.CHAOS_DIAMOND_ORE);
            entries.add(ModBlocks.CUBE_OF_CORRUPTION);

        });
    }
}
