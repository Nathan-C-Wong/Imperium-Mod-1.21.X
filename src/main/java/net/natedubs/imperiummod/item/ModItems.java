package net.natedubs.imperiummod.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.natedubs.imperiummod.ImperiumMod;
import net.natedubs.imperiummod.item.custom.HeavensVerdictItem;
import net.natedubs.imperiummod.item.custom.PhilosophersStoneItem;

public class ModItems {

    /*  ===============================================================================  */

    // Ingredients
    public static final Item BLACK_DIAMOND = registerItem("black_diamond", new Item(new Item.Settings()));
    public static final Item UNREFINED_BLACK_DIAMOND = registerItem("unrefined_black_diamond", new Item(new Item.Settings()));
    public static final Item CHAOS_DIAMOND = registerItem("chaos_diamond", new Item(new Item.Settings()));
    public static final Item UNREFINED_CHAOS_DIAMOND = registerItem("unrefined_chaos_diamond", new Item(new Item.Settings()));

    // Combat
    public static final Item HEAVENS_VERDICT = registerItem("heavens_verdict", new HeavensVerdictItem(new Item.Settings().maxDamage(2500).fireproof()));

    // Tools & misc items
    public static final Item PHILOSOPHERS_STONE = registerItem("philosophers_stone", new PhilosophersStoneItem(new Item.Settings().fireproof()));

    /*  ===============================================================================  */

    private static Item registerItem(String name, Item item) {
        // Create identifier for item
        Identifier itemID = Identifier.of(ImperiumMod.MOD_ID, name);

        // Register the item and return it
        return Registry.register(Registries.ITEM, itemID, item);
    }

    public static void registerModItems() {
        ImperiumMod.LOGGER.info("Registering Mod Items for " + ImperiumMod.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(BLACK_DIAMOND);
            entries.add(UNREFINED_BLACK_DIAMOND);
            entries.add(CHAOS_DIAMOND);
            entries.add(UNREFINED_CHAOS_DIAMOND);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(HEAVENS_VERDICT);
        });

    }
}
