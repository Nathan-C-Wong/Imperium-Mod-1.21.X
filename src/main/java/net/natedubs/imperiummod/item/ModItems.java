package net.natedubs.imperiummod.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.natedubs.imperiummod.ImperiumMod;

public class ModItems {

    // Black Diamond
    public static final Item BLACK_DIAMOND = registerItem("black_diamond", new Item(new Item.Settings()));
    public static final Item UNREFINED_BLACK_DIAMOND = registerItem("unrefined_black_diamond", new Item(new Item.Settings()));

    // Chaos Diamond
    public static final Item CHAOS_DIAMOND = registerItem("chaos_diamond", new Item(new Item.Settings()));
    public static final Item UNREFINED_CHAOS_DIAMOND = registerItem("unrefined_chaos_diamond", new Item(new Item.Settings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(ImperiumMod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        ImperiumMod.LOGGER.info("Registering Mod Items for " + ImperiumMod.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(BLACK_DIAMOND);
            entries.add(UNREFINED_BLACK_DIAMOND);
            entries.add(CHAOS_DIAMOND);
            entries.add(UNREFINED_CHAOS_DIAMOND);
        });

    }
}
