package net.natedubs.imperiummod.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.natedubs.imperiummod.ImperiumMod;
import net.natedubs.imperiummod.block.ModBlocks;

public class ModItemGroups {

    public static final RegistryKey<ItemGroup> CUSTOM_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(ImperiumMod.MOD_ID, "item_group"));

    public static final ItemGroup CUSTOM_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.BLACK_DIAMOND))
            .displayName(Text.translatable("itemGroup.imperium-mod"))
            .build();

    public static void registerItemGroups() {
        ImperiumMod.LOGGER.info("Registering item groups for " + ImperiumMod.MOD_ID);

        // Register the group.
        Registry.register(Registries.ITEM_GROUP, CUSTOM_ITEM_GROUP_KEY, CUSTOM_ITEM_GROUP);

        // Register items to the custom item group.
        ItemGroupEvents.modifyEntriesEvent(CUSTOM_ITEM_GROUP_KEY).register(itemGroup -> {

            // Ingredients
            itemGroup.add(ModItems.BLACK_DIAMOND);
            itemGroup.add(ModItems.UNREFINED_BLACK_DIAMOND);
            itemGroup.add(ModItems.CHAOS_DIAMOND);
            itemGroup.add(ModItems.UNREFINED_CHAOS_DIAMOND);

            // Misc Crafting Mats
            itemGroup.add(ModItems.ACTION);
            itemGroup.add(ModItems.STOCK);
            itemGroup.add(ModItems.BARREL);

            // Food
            itemGroup.add(ModItems.BURRITO);

            // Custom Items
            itemGroup.add(ModItems.JUDGEMENT);
            itemGroup.add(ModItems.PHILOSOPHERS_STONE);
            itemGroup.add(ModItems.WARP_SCEPTER);
            itemGroup.add(ModItems.GREATER_WARP_SCEPTER);
            itemGroup.add(ModItems.REJUVENATION_SCEPTER);
            itemGroup.add(ModItems.HUNTING_RIFLE);
            itemGroup.add(ModItems.RAY_GUN);

            // Blocks
            itemGroup.add(ModBlocks.BLACK_DIAMOND_BLOCK);
            itemGroup.add(ModBlocks.BLACK_DIAMOND_ORE);
            itemGroup.add(ModBlocks.CHAOS_DIAMOND_BLOCK);
            itemGroup.add(ModBlocks.CHAOS_DIAMOND_ORE);
            itemGroup.add(ModBlocks.CUBE_OF_CORRUPTION);
        });
    }
}
