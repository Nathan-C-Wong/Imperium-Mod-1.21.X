package net.natedubs.imperiummod.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.natedubs.imperiummod.ImperiumMod;
import net.natedubs.imperiummod.item.custom.GreaterWarpScepterItem;
import net.natedubs.imperiummod.item.custom.JudgementItem;
import net.natedubs.imperiummod.item.custom.PhilosophersStoneItem;
import net.natedubs.imperiummod.item.custom.WarpScepterItem;

public class ModItems {

    /*  ===============================================================================  */

    // Ingredients
    public static final Item BLACK_DIAMOND = registerItem("black_diamond", new Item(new Item.Settings()));
    public static final Item UNREFINED_BLACK_DIAMOND = registerItem("unrefined_black_diamond", new Item(new Item.Settings()));
    public static final Item CHAOS_DIAMOND = registerItem("chaos_diamond", new Item(new Item.Settings()));
    public static final Item UNREFINED_CHAOS_DIAMOND = registerItem("unrefined_chaos_diamond", new Item(new Item.Settings()));

    // Food
    public static final FoodComponent BURRITO_COMPONENT = new FoodComponent.Builder()
            .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 5*20, 2), 1.0f)
            .nutrition(12)
            .saturationModifier(10.0f)
            .build();
    public static final Item BURRITO = registerItem("burrito", new Item(new Item.Settings().food(BURRITO_COMPONENT)));

    // Combat
    public static final Item JUDGEMENT = registerItem("judgement", new JudgementItem(new Item.Settings().maxDamage(2500).fireproof().rarity(Rarity.RARE)));

    // Tools & misc items
    public static final Item PHILOSOPHERS_STONE = registerItem("philosophers_stone", new PhilosophersStoneItem(new Item.Settings().maxDamage(300).fireproof().rarity(Rarity.RARE)));
    public static final Item WARP_SCEPTER = registerItem("warp_scepter", new WarpScepterItem(new Item.Settings().maxDamage(250).rarity(Rarity.RARE)));
    public static final Item GREATER_WARP_SCEPTER = registerItem("greater_warp_scepter", new GreaterWarpScepterItem(new Item.Settings().fireproof().rarity(Rarity.EPIC)));

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
            entries.add(JUDGEMENT);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(PHILOSOPHERS_STONE);
            entries.add(WARP_SCEPTER);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
            entries.add(BURRITO);
        });
    }
}
