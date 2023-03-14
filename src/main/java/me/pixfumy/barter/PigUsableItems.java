package me.pixfumy.barter;

import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PigUsableItems {
    private static final Set<Item> USABLE_ITEMS = new HashSet<>(
        Arrays.asList(
            Items.GOLD_INGOT,
            Items.GOLD_NUGGET,
            Items.GOLDEN_SWORD,
            Items.GOLDEN_AXE,
            Items.GOLDEN_SHOVEL,
            Items.GOLDEN_HOE,
            Items.GOLDEN_PICKAXE,
            Items.GOLDEN_HELMET,
            Items.GOLDEN_CHESTPLATE,
            Items.GOLDEN_LEGGINGS,
            Items.GOLDEN_BOOTS,
            Items.GOLDEN_CARROT,
            Items.GOLDEN_APPLE,
            Items.GLISTERING_MELON,
            Items.CLOCK,
            Item.fromBlock(Blocks.POWERED_RAIL),
            Item.fromBlock(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE),
            Item.fromBlock(Blocks.GOLD_ORE),
            Item.fromBlock(Blocks.GOLD_BLOCK)
        ));

    public static boolean isPigUsable(Item item) {
        return USABLE_ITEMS.contains(item);
    }
}
